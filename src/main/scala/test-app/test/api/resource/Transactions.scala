package transactions

class Transactions(context: ActorRefFactory, dao: Dao) extends {
                                              val singular = "transaction"
                                              val plural = "transactions"
                                            } with Resource(context) {

  import
    context._

  def routes = {

    path("transaction") {
      get {
        traceName("transaction") {
          parameters("test") {
            (test) =>

            val request = Request(test)

            val data    = actorOf(Handler.props(dao)) ? request

            onSuccess(data) {
              case json: JsonResponse => complete(json)
            }
          }
        }
      } ~
      post {
        traceName("transaction") {
          complete("post to transaction")
        }
      }
    } ~
    path("transactions") {
      get {
        traceName("transactions") {
          parameters("test") {
            (test) =>

            validate(test == "test", "startDate must come before endDate") {
              parameterMultiMap {
                requestParameters =>

                import
                  Handler._

                val request = Request(test)

                val data    = actorOf(Handler.props(dao)) ? request

                onSuccess(data) {
                  case json: JsonResponse => complete(json)
                  case dunno              => complete(dunno.toString)

                }
              }
            }
          }
        }
      } ~ post {
        traceName("transactions GET") {
          complete("post to transactions")
        }
      }
    }
  }
}
