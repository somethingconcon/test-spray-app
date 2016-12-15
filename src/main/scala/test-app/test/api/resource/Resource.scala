package resources

trait Makeable[T] {
  def apply: T
}

object Makeable {

  def apply[T](make: => T): Makeable[T] = {
    new Makeable[T] {
      def apply(): T = make
    }
  }

  object Builders {

    implicit def make(dao: Dao)(implicit context: ActorRefFactory): Makeable[Transactions] = {
      Makeable { new Transactions(context, dao) }
    }
  }
}

abstract class Resource(context: ActorRefFactory) {

  implicit val timeout = Timeout(5 seconds)

  val name = this.getClass.getSimpleName.toLowerCase
  val singular: String
  val plural: String

  val routed = {
    (pathPrefixTest(singular) | pathPrefixTest(plural)) { requestCtx =>
      router ! requestCtx
    }
  }

  val router = {
    context.actorOf(RoutedHttpService.props(this), name = "Routed-Http-Service-" + name.toUpperCase)
  }

  def routes: Route

}