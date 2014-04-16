package org.eigengo.phillyete.api

import akka.actor.{ActorRefFactory, Props, ActorSystem}
import akka.io.IO
import spray.can.Http
import spray.routing._
import spray.http.StatusCodes

object Main extends App {
  val system = ActorSystem()

  Console.readLine()
  system.shutdown()
}
