package hudiprocessing

import org.apache.log4j.Logger

import org.apache.spark.sql.{SparkSession, DataFrame, Row, AnalysisException}
import org.apache.spark.sql.functions._

import java.util.Base64
import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime}

import org.apache.hudi
import org.apache.hudi.DataSourceWriteOptions
import org.apache.hudi.config.HoodieWriteConfig

import utils._
class HudiHandler(
    tableName: String,
    sourcePath: String,
    sourceFormat: String,
    targetPath: String,
    hudiPath: String
) {
  val logger = Logger.getLogger(getClass().getName())

  val spark = SparkSession.builder
    .appName("replicator")
    .config("spark.master", "local[*]")
    .getOrCreate()

  /**
   * *************************************************************************
   * Data String format to use on dataprocessing directory.
   * Ex:
   * hdfs:///local/hudi/processing/run=YYYY-MM-dd-HH-MM-SS/
   * hdfs:///local/hudi/repartition/run=YYYY-MM-dd-HH-MM-SS/
   *
   * This is useful for DEBUG failed jobs
   * *************************************************************************
   */
  val pathCompatibleFormatter =
    DateTimeFormatter.ofPattern("YYYY-MM-dd-HH-MM-SS")
  val now = LocalDateTime.now
  val datetimeProcessing = pathCompatibleFormatter.format(now)
  // logger.info(s"Running time (datetimeProcessing) ${now}")

  // def createDataFrame(df: DataFrame):

  private def checkIfDatasetHudiExists(): Boolean = {
    var datasetExist = false
    try {
      val dfHudi =
        spark.read
          .format("org.apache.hudi")
          .load(hudiPath + "/*/")
      datasetExist = true
      logger.info("HUDI's dataset found.")
    } catch {
      case e: org.apache.hudi.exception.TableNotFoundException => {
        logger.info("No HUDI's dataset found. Try to create a new one.", e)
      }
      case e: Throwable => {
        throw e
      }
    }

    return datasetExist
  }

  private def createFullLoadDataset() {
    val df = spark.read.format(sourceFormat).load(sourcePath)

    df.select("after.*", "ts_ms")
      .withColumn("created_at", col("created_at").cast("timestamp"))
      .write
      .format("org.apache.hudi")
      .options(getDefaultHudiOptions())
      .option(
        DataSourceWriteOptions.OPERATION_OPT_KEY,
        DataSourceWriteOptions.INSERT_OPERATION_OPT_VAL
      )
      .mode("overwrite")
      .save(
        s"file:////Users/yuriniitsuma/Desktop/poc2-project/hudi-processing/${hudiPath}"
      )
  }

  private def getDefaultHudiOptions(): Map[String, String] = {
    return Map[String, String](
      HoodieWriteConfig.TABLE_NAME -> tableName,
      DataSourceWriteOptions.STORAGE_TYPE_OPT_KEY -> "COPY_ON_WRITE",
      DataSourceWriteOptions.RECORDKEY_FIELD_OPT_KEY -> "id",
      DataSourceWriteOptions.PARTITIONPATH_FIELD_OPT_KEY -> "created_at",
      DataSourceWriteOptions.PRECOMBINE_FIELD_OPT_KEY -> "ts_ms"
    )
  }

  def run(): Boolean = {
    // check if dataset exists
    // var df = spark.read.format(sourceFormat).load(sourcePath)
    // df.show()

    if (checkIfDatasetHudiExists()) {
      println("CONTINUA")
    } else {
      createFullLoadDataset()
    }

    // if exists run only cdc dataset from Kafka
    // if not exists, then create a dataset with entire historical data
    // if hudiPath is equal then targetPath
    return true
  }
}
