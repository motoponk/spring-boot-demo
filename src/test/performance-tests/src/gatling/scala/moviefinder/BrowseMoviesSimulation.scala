package moviefinder

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BrowseMoviesSimulation extends Simulation {

  val feeder = csv("genres.csv").random

  // Let's split this big scenario into composable business processes,
  // like one would do with PageObject pattern with Selenium

  // object are native Scala singletons
  object Search {

    val search = exec(http("Home").get("/movies"))
      .feed(feeder)
      .pause(1)
      .exec(http("Search").get("/movies?genre=${genreName}"))
      .pause(1)
  }

  val httpConf = http
    .baseURL("http://localhost:8080")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  // Now, we can write the scenario as a composition
  val scn = scenario("Browsing Movies").exec(Search.search)

  //setUp(scn.inject(atOnceUsers(10)).protocols(httpConf))

  setUp(
    scn.inject(rampUsers(50) over (10 seconds))
  ).protocols(httpConf)


}
