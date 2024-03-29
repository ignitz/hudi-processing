package hudiprocessing

import org.apache.log4j.Logger

import scala.io.Source
import play.api.libs.json.{Json, JsValue}
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.apache.commons.io.IOUtils

class AWS_EMR(jsonParams: JsValue) {
  val logger = Logger.getLogger(getClass().getName())

  val configPath: String = jsonParams("configPath").as[String]

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

  def getContentFromLocal(local_path: String): String = {
    val bufferedSource = Source.fromFile(local_path)
    val fileContents = bufferedSource.getLines.mkString
    bufferedSource.close()

    return fileContents
  }

  def run(): Boolean = {
    val configParams: JsValue =
      if (configPath.take(5) == "s3://") {
        Json.parse(getContentFromS3(configPath))
      } else {
        Json.parse(getContentFromLocal(configPath))
      }

    var tableName: String = null
    var sourcePath: String = null
    var sourceFormat: String = null
    var targetPath: String = null
    var hudiPath: String = null

    for (table <- configParams("tables").as[List[JsValue]]) {
      try {
        tableName = table("tableName").as[String]
        sourcePath = table("sourcePath").as[String]
        sourceFormat = table("sourceFormat").as[String]
        targetPath = table("targetPath").as[String]
        hudiPath = table("hudiPath").as[String]
      } catch {
        case e: Throwable => { throw new Exception(e) }
      }

      // Add HudiParams later
      val hudiInstance = new HudiHandler(
        tableName = tableName,
        sourcePath = sourcePath,
        sourceFormat = sourceFormat,
        targetPath = targetPath,
        hudiPath = hudiPath
      )

      hudiInstance.run()
    }
    return true
  }
}
