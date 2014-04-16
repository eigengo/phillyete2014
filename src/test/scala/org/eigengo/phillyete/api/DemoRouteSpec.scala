package org.eigengo.phillyete.api

import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest

class DemoRouteSpec extends Specification with Specs2RouteTest with DemoRoute {

  "Any request" should {
    "Reply with Hello, World" in {
      Get() ~> demoRoute ~> check {
        responseAs[String] mustEqual "Hello, world"
      }
    }
  }

}
