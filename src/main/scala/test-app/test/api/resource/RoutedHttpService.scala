package api


object RoutedHttpService {
  def props(resource : Resource) = {
    Props(new RoutedHttpService(resource))
  }
}

class RoutedHttpService(resource: Resource) extends Service {

  def receive = {
    runRoute {
      resource.routes
    }
  }

}