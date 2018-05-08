package example

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.scalatest._
import play.api.libs.json._
import play.api.libs.ws.JsonBodyReadables._
import play.api.libs.ws.JsonBodyWritables._
import play.api.libs.ws.StandaloneWSResponse
import play.api.libs.ws.ahc._

import scala.concurrent.Future

class NavKlaraVideoSpecAsync extends AsyncFeatureSpec with GivenWhenThen with BeforeAndAfter {

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  val ws = StandaloneAhcWSClient()

  after {
    ws.url("http://es-content.dev.bonnier.news:9200/klara1/klara/dn.screen9.1uwHxJLDuuBKBHGHQcissw/")
      .delete() map {
      response => assert(response.status == 404 || response.status == 200)
    }
    ws.close()
    system.terminate()
  }

  feature("Videos") {

    scenario("Fetch a single video") {

      Given("there is a video added to elasticsearch")
        val videoEpiJson: JsValue = JsValueReader.getJsValue("/es-dn.screen9.1uwHxJLDuuBKBHGHQcissw.json")
        val postVideoResponse: Future[StandaloneWSResponse] =
          ws.url("http://es-content.dev.bonnier.news:9200/klara1/klara/dn.screen9.1uwHxJLDuuBKBHGHQcissw/")
            .post(videoEpiJson)
        postVideoResponse map {
          response => assert(response.status == 201 || response.status == 200 )
        }

      When("fetching a video with id dn.screen9.1uwHxJLDuuBKBHGHQcissw from nav-klara-dn")
        val getVideoResponse: Future[StandaloneWSResponse] =
          ws.url("http://nav-klara-dn.dev.internal.bonnier.news/videos/dn.screen9.1uwHxJLDuuBKBHGHQcissw")
            .get()
        val jsValueFuture: Future[JsValue] = getVideoResponse map { response =>
          assert(response.status == 200)
          response.body[JsValue]
      }

      Then("response should contain the right data")
        jsValueFuture map { jsValue =>
          assert(jsValue == JsValueReader.getJsValue("/nav-dn.screen9.1uwHxJLDuuBKBHGHQcissw.json"))
        }
    }
  }
}


