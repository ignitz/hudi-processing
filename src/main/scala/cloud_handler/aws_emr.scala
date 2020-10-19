package hudiprocessing

import org.apache.log4j.Logger

import play.api.libs.json.{Json, JsValue}

import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.apache.commons.io.IOUtils

class AWS_EMR(jsonParams: JsValue) {
  val logger = Logger.getLogger(getClass().getName())

  val configPath: String = jsonParams("configPath").as[String]
  // val hudiPath = jsonParams("hudiPath")

  // import scala.util.matching.Regex

  def getContentFromS3(s3_path: String): String = {
    val regex = "s3://([^/]*)/(.*)".r
    val regex(bucket_name, key) = s3_path

    // TODO: I suspect that with big files get stream from part of file
    // https://alexwlchan.net/2019/09/streaming-large-s3-objects/
    lazy val s3 = AmazonS3ClientBuilder.defaultClient()
    val stream = s3.getObject(bucket_name, key).getObjectContent()
    val bytearray = IOUtils.toByteArray(stream)
    val content = new String(bytearray, "utf-8")

    return content
  }

  def run(): Boolean = {

    val configParams: JsValue = Json.parse(getContentFromS3(configPath))

    for (table <- configParams("tables").as[List[JsValue]]) {
      val hudiInstance = new HudiHandler(
        sourcePath = table("sourcePath").as[String],
        sourceFormat = table("sourceFormat").as[String],
        targetPath = table("targetPath").as[String],
        hudiPath = ""
      )

      hudiInstance.run()
    }
    return true
  }
}
