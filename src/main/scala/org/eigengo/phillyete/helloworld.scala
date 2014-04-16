package org.eigengo.phillyete

import akka.actor.{Props, ActorSystem, Actor}
import spray.http.{HttpEntity, HttpResponse, HttpRequest}
import spray.can.Http
import akka.io.IO
import akka.actor.Actor.Receive

class HelloWorldService extends Actor {
  def receive: Receive = {
    case r: HttpRequest =>
      sender ! HttpResponse(entity = HttpEntity("Hello, world"))
    case _: Http.Connected =>
      sender ! Http.Register(self)
  }
}

object HelloWorld extends App {
  val system = ActorSystem()
  val service = system.actorOf(Props[HelloWorldService])

  IO(Http)(system) ! Http.Bind(service, "0.0.0.0", port = 8080)

  Console.readLine()
  system.shutdown()
}