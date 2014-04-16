package org.eigengo.phillyete.api

import spray.routing.{Route, Directives}
import akka.actor.{Props, Actor, ActorRefFactory, ActorRef}
import spray.http._
import spray.can.Http
import spray.json._
import spray.http.HttpResponse
import spray.routing.RequestContext
import spray.http.HttpHeaders.RawHeader
import spray.http.ChunkedResponseStart
import org.eigengo.phillyete.core.{TweetReaderActor, OAuthTwitterAuthorization}

trait TweetAnalysisRoute extends Directives {

  def tweetAnalysisRoute(implicit actorRefFactory: ActorRefFactory): Route =
    post {
      path("tweets" / Segment)(sendTweetAnalysis)
    }

  def sendTweetAnalysis(query: String)(ctx: RequestContext)(implicit actorRefFactory: ActorRefFactory): Unit = {
    actorRefFactory.actorOf(Props(new TweetAnalysisStreamingActor(query, ctx.responder)))
  }

  class TweetAnalysisStreamingActor(query: String, responder: ActorRef) extends Actor {
    val allCrossOrigins =
      RawHeader("Access-Control-Allow-Origin", "*") ::
        RawHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE") :: Nil

    import ContentTypes._
    val reader = context.actorOf(Props(new TweetReaderActor(TweetReaderActor.twitterUri, self)
      with OAuthTwitterAuthorization))
    val responseStart = HttpResponse(entity = HttpEntity(`application/json`, "{}"), headers = allCrossOrigins)
    responder ! ChunkedResponseStart(responseStart).withAck('start)

    def receive: Receive = {
      case 'start =>
        reader ! query
      case _: Http.ConnectionClosed =>
        responder ! ChunkedMessageEnd
        context.stop(reader)
        context.stop(self)
      case analysed: Map[String, Map[String, Int]] =>
        val items = analysed.map { case (category, elements) => category ->
          JsArray(elements.map {
            case (k, v) => JsObject("name" -> JsString(k), "value" -> JsNumber(v))
          }.toList)
        }
        val body = CompactPrinter(JsObject(items))
        responder ! MessageChunk(body)
    }
  }

}
