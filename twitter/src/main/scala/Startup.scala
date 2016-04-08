import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter._
import org.apache.spark.streaming.StreamingContext._

object TwitterPopularTags {
  def main(args: Array[String]) {

    // Configure Twitter credentials
    System.setProperty("twitter4j.oauth.consumerKey", "XXXXX")
    System.setProperty("twitter4j.oauth.consumerSecret", "XXXXX")
    System.setProperty("twitter4j.oauth.accessToken", "XXXXX")
    System.setProperty("twitter4j.oauth.accessTokenSecret", "XXXXX")

    // Your code goes here
    val ssc = new StreamingContext(new SparkConf(), Seconds(1))
    val tweets = TwitterUtils.createStream(ssc, None)
    val statuses = tweets.map(status => status.getText())
    val words = statuses.flatMap(status => status.split(" "))
    val hashtags = words.filter(word => word.startsWith("#"))
    val counts = hashtags.map(tag => (tag, 1)).reduceByKeyAndWindow(_ + _, _ - _, Seconds(60*5), Seconds(1))
    val sortedCounts = counts.map{case(tag, count) => (count, tag) }.transform(rdd => rdd.sortByKey(false))
    sortedCounts.foreach(rdd => println("\nTop 10 hashtags:\n" + rdd.take(9).mkString("\n")))
    //statuses.print()

    ssc.checkpoint("./checkpoint")
    ssc.start()
    ssc.awaitTermination()
  }
}
