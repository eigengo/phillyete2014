package org.eigengo.phillyete.api

import spray.routing.{Route, Directives}
import spray.http.HttpCookie

trait DemoRoute extends Directives {

  val demoRoute: Route =
    get {
      complete {
        "Hello, world"
      }
    }
}
