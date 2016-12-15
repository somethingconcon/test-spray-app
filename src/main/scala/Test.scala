package test

import
  akka.actor._,
  akka.io.IO,
  com.typesafe.config._,
  kamon.Kamon,
  org.slf4j.LoggerFactory,
  spray.can.Http

class SupervisorConfig extends SupervisorStrategyConfigurator {

  import akka.actor.SupervisorStrategy._

  def create() = {
    OneForOneStrategy() {
      case _: ActorInitializationException => Escalate
      case _: ActorKilledException         => Stop
      case _: Exception                    => Restart
    }
  }
}

object Test extends App {

  Kamon.start

  implicit val actorSystem = ActorSystem(name, serverSettings)

  val api                  = actorSystem.actorOf(Api.props)

  IO(Http) ! Http.Bind(api, "localhost", 8080) // this is different in real app
}
