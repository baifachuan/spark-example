package com.fcbai.spark.example

import org.apache.spark.sql.SparkSession

class Examples {

}

object Examples {
  def main(args: Array[String]): Unit = {
    println("hello world")
    val logFile = "/Users/fcbai/Downloads/spark-examples/README.md"
    implicit val spark = SparkSession.builder
      .appName("Simple Spark Application")
      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .master("local[*]")
      .getOrCreate()
    val logData = spark.read.textFile(logFile).cache()
    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()
    println(s"Lines with a: $numAs, Lines with b: $numBs")
    HudiExamples.write
    spark.stop()
  }
}