package hudiprocessing

import org.apache.log4j.Logger

import play.api.libs.json.JsValue

class AWS_EMR(jsonParams: JsValue) {
  val sourcePath: String = jsonParams("source").as[String]
  val targetPath: String = jsonParams("target").as[String]

  println(s"sourcePath = ${sourcePath}")
  println(s"targetPath = ${targetPath}")

  def run(): Boolean = {
    return true
  }
}
