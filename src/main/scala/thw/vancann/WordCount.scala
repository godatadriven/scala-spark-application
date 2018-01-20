package thw.vancann

import org.apache.spark.sql.{Dataset, SparkSession}
import thw.vancann.storage._


object WordCount extends SparkJob {

  @Override
  def run(spark: SparkSession, config: UsageConfig, storage: Storage): Unit = {
    /**
      * This function only does I/O, no logic
      */

    val logs = storage.chatLogs

    val res = transform(spark, logs, config.date)

    storage.writeToParquet(res, s"s3://${config.date}/words")
  }

  def transform(spark: SparkSession, logs: Dataset[ChatLog], date: String): Dataset[WordCountSchema] = {
    /**
      * Any logic is implemented in transform. No side effects here!
      * Testability is key :)
      */
    import spark.implicits._

    logs
      .filter($"date" === date)
      .flatMap(x => x.text
        .replaceAll("""[\p{Punct}&&[^.]]""", "")
        .replaceAll("\n", " ")
        .split(" "))
      .map(x => (x, 1))
      .groupByKey(_._1)
      .reduceGroups((x, y) => (x._1, x._2 + y._2))
      .map(x => WordCountSchema(x._1, x._2._2))
  }

  @Override
  def appName: String = "word count"
}
