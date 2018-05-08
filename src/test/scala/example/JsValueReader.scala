package example

import play.api.libs.json.JsValue
import play.api.libs.json.Json

import scala.io.Source

object JsValueReader {
  def getJsValue(resource: String): JsValue = {
    Json.parse(
      Source.fromURL(getClass.getResource(resource)).mkString
    )
  }
}
