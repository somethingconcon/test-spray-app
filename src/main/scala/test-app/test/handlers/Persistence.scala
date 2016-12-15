package handlers

import
  akka.actor._

abstract class Persistence extends Actor with ActorLogging {
  // possible to create different resource pool to allow async DB calls a place to
  // do their own thing
  // val dispatcher =  system.dispatchers().lookup("io-blocking")
  import
    context.dispatcher

  implicit val timeout = Timeout(5 seconds)

  def deliverData(deliverable: Response, destination: ActorRef) = {
    destination ! deliverable
  }

}