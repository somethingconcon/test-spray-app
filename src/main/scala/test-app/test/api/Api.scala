package api

abstract class Service extends HttpServiceActor
                          with ActorLogging
                          with FailureHandling {

  implicit val system: ActorSystem = context.system

  import
    akka.util.Timeout,
    scala.concurrent.duration._,
    scala.language.postfixOps

  implicit val timeout = Timeout(5 seconds)

}

object Api {
  def props(implicit dao: Dao) = Props { new Api(dao) }
}

class Api(dao: Dao) extends Service {

  import
    context._

  def receive = {
    case RequestContext(HttpRequest(_, _, _, _, _), responder, path) => {
      become(handleRequest)
    }
  }

  private def handleRequest = runRoute(routes)

  def resource[T <: Resource](implicit context: ActorRefFactory) = {

    val resource = make(dao).apply

    resource.routed

  }

  def routes = {
    pathSingleSlash {
      detach() {

        val handler  = actorOf(RootHandler.props(dao), name = "Root-Handler")
        val request = handler ? RootHandler.AreYouAlive

        onComplete(request) {
          case Success(data) => complete(data.toString)
          case Failure(e)    => complete(e.toString)
        }
      }
    } ~
    resource[Transactions]
  }

}
