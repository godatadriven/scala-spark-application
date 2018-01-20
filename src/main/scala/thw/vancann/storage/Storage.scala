package thw.vancann.storage

import org.apache.spark.sql.{Encoder, SparkSession, Dataset}

trait Storage {
  def chatLogs: Dataset[ChatLog]

  def writeToParquet(ds: Dataset[_], location: String)
}

class S3Storage(spark: SparkSession) extends Storage {
  /**
    * Storage defines all input and output logic. How and where tables and files
    * should be read and saved
    */

  import spark.implicits._

  private def readCsv[T: Encoder](location: String) = {
    spark
      .read
      .option("header", "true")
      .option("inferSchema", "true")
      .option("delimiter", ";")
      .csv(location)
      .as[T]
  }

  override def chatLogs: Dataset[ChatLog] = readCsv[ChatLog]("s3://someLocation")

  override def writeToParquet(ds: Dataset[_], location: String): Unit = {
    ds
      .write
      .parquet(location)
  }
}

case class ChatLog(date: String, text: String)
case class WordCountSchema(word: String, count: Long)
