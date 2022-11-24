import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import scala.concurrent.duration.DurationInt

class BasicSimulation extends Simulation {

  // Параметры запуска
  val threads : Int = Integer.getInteger("threads", 1)   // желаемое количество виртуальных пользователей
  val rampUp : Int = Integer.getInteger("rampUp", 1)     // время, за которое достигнется желаемое количество пользователей
  val holdLoad: Int = Integer.getInteger("holdLoad", 60) // время, которое будет поддерживаться нагрузка


  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl("https://localhost.ru")
    .contentTypeHeader("application/json;charset=utf-8")
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate, br")
    .connectionHeader("keep-alive")
    .header("Locale", "ru")
    .header("Country", "RU")
    .userAgentHeader("your_user_agent")


  // Сценарий
  val scn = scenario("Тестовый сценарий")
    .forever("") {
      exec(http("Запрос ")
        .get("/")
        .header("Locale", "ru")
        .queryParam("yredirect", "true"))
        .pause(500.millisecond)
      .randomSwitch(
        50.0 -> exec(http("Запрос вкладки видео")
          .get("/video"))
          .pause(500.millisecond))
    }

  setUp(scn.inject(rampConcurrentUsers(1).to(threads).during(rampUp.seconds)).protocols(httpProtocol)).maxDuration((holdLoad+rampUp).seconds)
}