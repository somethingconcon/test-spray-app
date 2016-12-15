package api

case class ErrorResponseException(
  responseStatus: StatusCode,
  response: Option[HttpEntity]) extends Exception

trait FailureHandling {

  this: HttpService =>

  implicit def exceptionHandler(implicit log: LoggingContext) =
    FailureHandling.exception(log)
  implicit def rejectionHandler(implicit log: LoggingContext) =
    FailureHandling.rejection(log)

}

object FailureHandling {

  def exception(log: LoggingContext) =
    ExceptionHandler {
      // Matched exception assigned e to the thrown exception
      // ctx is the request RequestContext
      case e: IllegalArgumentException => ctx =>
        responder(ctx, e, "Illegal Argument: " + e.getMessage, NotAcceptable, log)
      case e: NoSuchElementException   => ctx =>
        responder(ctx, e, "No Such Element: " + e.getMessage, NotFound, log)
      case e: Throwable                => ctx =>
        responder(ctx, e, "The Server cannot respond.", InternalServerError,log)
    }

  def rejection(log: LoggingContext) =
    RejectionHandler {
      // Matched List[Rejection] :: Nil
      case MalformedQueryParamRejection(p, m, e) :: Nil => ctx =>
        responder(ctx, "Malformed params: " + p, BadRequest, log)
      case MissingQueryParamRejection(p)         :: Nil => ctx =>
        responder(ctx, "Missing params: " + p, BadRequest, log)
      case ValidationRejection(m, c)             :: Nil => ctx =>
        responder(ctx, "Invalid params: " + m, BadRequest, log)
    }
    // additions
    /*
      MissingFormFieldRejection(fieldName: String)
      MalformedFormFieldRejection(fieldName: String, errorMsg: String,
                                       cause: Option[Throwable] = None)
      UnsupportedRequestContentTypeRejection(errorMsg: String)
      MalformedRequestContentRejection(message: String, cause: Option[Throwable] = None)
      MalformedRequestContentRejection(message: String, cause: Option[Throwable] = None)
      SchemeRejection(supported: String) extends Rejection
    */

  private def responder(ctx: RequestContext,
                          e: Throwable,
                          m: String,
                          s: StatusCode,
                        log: LoggingContext) = {
    val error = s"${m} :: ${e.toString}"
    log.error(e, "ERROR: " + ctx.request.toString + "caused an issue")
    ctx.complete(s, JsonResponse(data = Vector(), errors = Vector(error)))
  }

  private def responder(ctx: RequestContext,
                          m: String,
                          s: StatusCode,
                        log: LoggingContext) = {
    log.error("REJECTION: " + m)
    ctx.complete(s, JsonResponse(data = Vector(), errors = Vector(m)))
  }
}