package org.eigengo.phillyete

import org.specs2.mutable.SpecificationLike
import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import spray.http.{HttpEntity, HttpResponse, HttpRequest}

class HelloWorldServiceSpec extends TestKit(ActorSystem())
  with SpecificationLike with ImplicitSender {

  val service = TestActorRef[HelloWorldService]

  "Any request" should {
    "Reply with Hello, world" in {
      service ! HttpRequest()
      expectMsgType[HttpResponse].entity mustEqual HttpEntity("Hello, world")
    }
  }

}
