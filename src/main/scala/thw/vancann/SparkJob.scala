package thw.vancann

import org.apache.spark.sql.SparkSession
import scopt.OptionParser
import thw.vancann.storage.{S3Storage, Storage}

trait SparkJob[T <: Config] {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder
      .appName(appName)
      .enableHiveSupport()
      .getOrCreate()

    parseAndRun(spark, args)

    def parseAndRun(spark: SparkSession, args: Array[String]): Unit = {
      configurationParser.parse(args, configuration) match {
        case Some(config) => run(spark, config, new S3Storage(spark))
        case None => throw new IllegalArgumentException("arguments provided to job are not valid")
      }
    }
  }

  def configuration: T
  def configurationParser: OptionParser[T]

  def run(spark: SparkSession, config: T, storage: Storage)

  def appName: String
}
