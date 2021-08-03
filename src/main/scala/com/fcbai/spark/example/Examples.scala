package com.fcbai.spark.example

import org.apache.spark.sql.SparkSession

class Examples {

}

object Examples {
  def main(args: Array[String]): Unit = {
    val logFile = "/Users/fcbai/Downloads/spark-examples/README.md" // Should be some file on your system
    val spark = SparkSession.builder.appName("Simple Application").getOrCreate()
    val logData = spark.read.textFile(logFile).cache()
    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()
    println(s"Lines with a: $numAs, Lines with b: $numBs")
    spark.stop()
  }
}