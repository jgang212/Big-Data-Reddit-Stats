import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.functions._

object DelaysWithSD {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Build SD Table").setMaster("local[2]");
    // Create a Scala Spark Context.
    val sc = new SparkContext(conf)
    val sqlContext = new HiveContext(sc)
    val faw = sqlContext.table("flights_and_weather")

    val f_all = faw.groupBy("origin_name", "dest_name").
      agg(count(col("*")).as("all_flights"), sum(col("dep_delay")).as("all_delays"),
        avg(col("dep_delay")).as("all_avg"), stddev(col("dep_delay")).as("all_stddev"))
    
    f_all.sort(desc("all_avg")).limit(1).show()
  }
}