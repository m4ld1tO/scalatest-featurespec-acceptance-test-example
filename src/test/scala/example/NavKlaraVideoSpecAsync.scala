package example

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.scalatest._
import play.api.libs.json._
import play.api.libs.ws.JsonBodyReadables._
import play.api.libs.ws.JsonBodyWritables._
import play.api.libs.ws.StandaloneWSResponse
import play.api.libs.ws.ahc._

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.io.Source

class NavKlaraVideoSpecAsync extends AsyncFeatureSpec with GivenWhenThen with BeforeAndAfter {

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  val ws = StandaloneAhcWSClient()


  feature("Videos") {

    scenario("Fetch a single video") {

      Given("there is a video added to elasticsearch with id dn.screen9.1uwHxJLDuuBKBHGHQcissw")
        val videoElasticJson = Source.fromURL(getClass.getResource("/es-dn.screen9.1uwHxJLDuuBKBHGHQcissw.json")).mkString
        val videoElasticJsValue = Json.parse(videoElasticJson)
        val postVideoElasticResponse: Future[StandaloneWSResponse] =
          ws.url("http://es-content.dev.bonnier.news:9200/klara1/klara/dn.screen9.1uwHxJLDuuBKBHGHQcissw/")
            .post(videoElasticJsValue)
      Await.result(postVideoElasticResponse, 5 seconds) //AsyncFeatureSpec only blocks async call in Then
        postVideoElasticResponse map {
          response =>
            println("#### GIVEN")
            assert(response.status == 201)
        }

      When("fetching a video with id dn.screen9.1uwHxJLDuuBKBHGHQcissw")
        val getVideoKlaraResponse: Future[StandaloneWSResponse] =
          ws.url("http://nav-klara-dn.dev.internal.bonnier.news/videos/dn.screen9.1uwHxJLDuuBKBHGHQcissw")
            .get()
        Await.result(getVideoKlaraResponse, 5 seconds) //AsyncFeatureSpec only blocks async call in Then
        val jsValueFuture: Future[JsValue] = getVideoKlaraResponse map {
          response =>
            println("#### WHEN")
            assert(response.status == 200)
            response.body[JsValue]
        }

      Then("response should contain the right data")
        jsValueFuture map { jsValue => //Automatically blocked by AsyncFeatureSpec
          println("#### THEN")
          val expectedKlaraJson = Source.fromURL(getClass.getResource("/nav-dn.screen9.1uwHxJLDuuBKBHGHQcissw.json")).mkString
          val expectedKlaraJsValue = Json.parse(expectedKlaraJson)
          assert(jsValue == expectedKlaraJsValue)
        }

    }

    after {
      println("#### AFTER")
      ws.url("http://es-content.dev.bonnier.news:9200/klara1/klara/dn.screen9.1uwHxJLDuuBKBHGHQcissw/")
        .delete() map {
        response => assert(response.status == 404 || response.status == 200)
      }
      ws.close()
      system.terminate()
    }
  }
}


