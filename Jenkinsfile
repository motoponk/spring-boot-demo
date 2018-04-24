pipeline {
    agent any

    environment {
        DOCKER_USERNAME = 'sivaprasadreddy'
        APPLICATION_NAME = 'movie-finder'
    }

    parameters {
        booleanParam(name: 'PUBLISH_TO_DOCKERHUB', defaultValue: true,
                     description: 'Should build and publish Docker Image to DockerHub?')

        booleanParam(name: 'DEPLOY_TO_KUBERNETES', defaultValue: false,
                     description: 'Deploy to Kubernetes?')
    }

    triggers {
        pollSCM('* * * * *')
    }

    stages {
        stage("Compile") {
          steps {
               sh "./gradlew compileJava"
            }
        }

        stage("Unit test") {
          steps {
               sh "./gradlew test"
          }
        }

        stage("Integration test") {
            steps {
              sh "./gradlew integrationTest"
            }
        }

        stage("Code coverage") {
            steps {
               sh "./gradlew jacocoTestReport"
               publishHTML (target: [
                 reportDir: 'build/reports/jacoco/test/html',
                 reportFiles: 'index.html',
                 reportName: "JaCoCo Report"
               ])
               sh "./gradlew jacocoTestCoverageVerification"
            }
        }

        stage("Static code analysis") {
            steps {
              sh "./gradlew checkstyleMain"
              publishHTML (target: [
                     reportDir: 'build/reports/checkstyle/',
                     reportFiles: 'main.html',
                     reportName: "Checkstyle Report" ])
            }
        }

        stage("Package") {
            steps {
                 sh "./gradlew build -x test"
            }
        }

        stage("Publish to DockerHub") {
            when {
                // Only publish if PUBLISH_TO_DOCKERHUB=true
                expression { params.PUBLISH_TO_DOCKERHUB == true }
            }
            steps {
              sh "docker build -t ${env.DOCKER_USERNAME}/${env.APPLICATION_NAME}:${BUILD_NUMBER} -t ${env.DOCKER_USERNAME}/${env.APPLICATION_NAME}:latest ."

              withCredentials([[$class: 'UsernamePasswordMultiBinding',
                                credentialsId: 'docker-hub-credentials',
                                usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                  sh "docker login --username $USERNAME --password $PASSWORD"
              }
              sh "docker push ${env.DOCKER_USERNAME}/${env.APPLICATION_NAME}:latest"
              sh "docker push ${env.DOCKER_USERNAME}/${env.APPLICATION_NAME}:${BUILD_NUMBER}"
            }
        }

        stage("Deploy to Kubernetes") {
            when {
                expression { params.DEPLOY_TO_KUBERNETES == true }
            }
            agent {
                label 'master'
            }
            steps {
                sh "kubectl delete -f k8s/app-mysql.yml --ignore-not-found"
                sh "kubectl delete -f k8s/mysql.yml --ignore-not-found"
                sh "kubectl delete -f k8s/config.yml --ignore-not-found"

                sleep 30

                sh "kubectl create -f k8s/config.yml"
                sh "kubectl create -f k8s/mysql.yml"
                sh "kubectl create -f k8s/app-mysql.yml"
            }
        }

    }

}
