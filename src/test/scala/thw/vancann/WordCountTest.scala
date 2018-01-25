package thw.vancann

import org.scalatest.FlatSpec
import SharedSparkSession.spark
import thw.vancann.storage.{ChatLog, WordCountSchema}

class WordCountTest extends FlatSpec {

  import spark.implicits._

  "transform" should "count the each word" in {
    val ds = spark.createDataset(Seq(
      ChatLog("2017-05-25",
        """
Mary had a little lamb
It's fleece was white as snow, yeah
Everywhere the child went
The lamb, the lamb was sure to go, yeah
He followed her to school one day
And broke the teacher's rule
And what a time did they have
That day at school
Tisket, tasket, baby alright
A green and yellow basket, now
I wrote a letter to my baby
And on my way I passed it, now
        """.stripMargin),
      ChatLog("2017-05-26", "foo")))

    val res: Seq[WordCountSchema] = WordCount
      .transform(spark, ds, "2017-05-25")
      .collect

    val lamb = res
      .filter(_.word == "lamb")
      .map(_.count)
      .head

    assert(lamb === 3)
  }

}
