package handlers

import
  akka.actor._

class RootHandler(dao: Dao) extends Actor {

  import
    dao._,
    RootHandler._

  def receive = {
    case AreYouAlive => {
      sender ! alive
    }
  }
}

object RootHandler {

  case object AreYouAlive

  val alive = "ALIVE!"

  def props(dao: Dao) = Props { new RootHandler(dao) }
}
