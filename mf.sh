#!/bin/sh

declare project_dir=$(dirname $0)
declare docker_compose_file=${project_dir}/docker-compose.yml
declare moviefinder="movie-finder"

function start() {
    echo 'Starting movie-finder....'
    build_app
    stop
    docker-compose -f ${docker_compose_file} up --build --force-recreate -d ${moviefinder}
    docker-compose -f ${docker_compose_file} logs -f
}

function stop() {
    echo 'Stopping movie-finder....'
    docker-compose -f ${docker_compose_file} stop
    docker-compose -f ${docker_compose_file} rm -f
}

function build_app() {
    ./gradlew clean build
}

action="start"

if [ $1 != "0"  ]
then
    action=$@
fi

eval ${action}
