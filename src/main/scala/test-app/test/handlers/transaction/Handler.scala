package handler

object Handler {

  import
    Dao

  def props(dao: Dao) = Props { new Handler(dao.reader) }

  case class Request(data: String)

}

class Handler(reader: Read) extends Actor {


  def receive = {

    case Request(data) => {


      val trace   = Tracer.currentContext.startSegment("db", "http-something", "clouds-man")
      val request = Request(data)

      val requestSender = sender

      // reader is a DB client to make async requests agaisnt
      // returns Future[Db-Data]
      reader.get(request).onComplete { db-data =>

        val (data, errors) = db-data match {

          case Success(response) => {
            (db-objects, errors)
          }
          case Failure(ex)  => {
            (Vector(), Vector(ex.getMessage))
          }
        }
        requestSender ! Response(data, errors)
        trace.finish
      }
    }
  }
}
