package thw.vancann

import org.apache.spark.sql.{Dataset, SparkSession}
import org.scalatest.FlatSpec
import thw.vancann.sparktest.SharedSparkSession.spark
import thw.vancann.storage.{ChatLog, Storage}

class WordCountTestIT extends FlatSpec {

  import spark.implicits._

  class LocalStorage(spark: SparkSession) extends Storage {
    override def chatLogs: Dataset[ChatLog] = spark.read.csv("logs.csv").as[ChatLog]

    override def writeToParquet(ds: Dataset[_], location: String): Unit = {}
  }

  "wordcount" should "read and write to disk" in {
    val config = UsageConfig("2017-05-25")
    WordCount.run(spark, config, new LocalStorage(spark))
  }

}
