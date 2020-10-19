package hudiprocessing

import org.apache.log4j.Logger

import org.apache.spark.sql.{SparkSession, DataFrame, Row, AnalysisException}

import java.util.Base64
import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime}

import org.apache.hudi
import org.apache.hudi.DataSourceWriteOptions
import org.apache.hudi.config.HoodieWriteConfig
class HudiHandler(
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
  logger.info(s"Running time (datetimeProcessing) ${now}")

  def run(): Boolean = {
    // check if dataset exists
    var df = spark.read.format(sourceFormat).load(sourcePath)
    df.show()
    // if exists run only cdc dataset from Kafka
    // if not exists, then create a dataset with entire historical data
    // if hudiPath is equal then targetPath
    return true
  }
}
