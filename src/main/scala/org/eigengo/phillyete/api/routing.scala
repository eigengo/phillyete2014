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

trait UrlMatchingRoute extends Directives {

  case class Colour(r: Int, g: Int, b: Int) {
    require(r >= 0 && r <= 255)
    require(g >= 0 && g <= 255)
    require(b >= 0 && b <= 255)
  }

  val urlMatchingRoute =
    get {
      path("customer" / IntNumber) { id =>
        complete {
          s"Customer with id $id"
        }
      } ~
      path("customer") {
        parameter('id.as[Int]) { id =>
          complete {
            s"Customer with id $id"
          }
        }
      } ~
      path("colour") {
        parameters(('r.as[Int], 'g.as[Int], 'b.as[Int])).as(Colour) { colour: Colour =>
          import colour._
          complete {
            <html>
              <body>
                <p>{r}</p>
                <p>{g}</p>
                <p>{b}</p>
              </body>
            </html>
          }
        }
      }
    }

}
