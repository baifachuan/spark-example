package com.fcbai.spark.example

import org.apache.spark.sql.SparkSession

case class Config(op: String = "read") {}

object HudiApplication {

  def main(args: Array[String]): Unit = {
    import scopt.OParser
    val builder = OParser.builder[Config]
    val parser = {
      import builder._
      OParser.sequence(
        programName("scopt"),
        head("scopt", "4.x"),
        opt[String]('o', "operation")
          .action((x, c) => c.copy(op = x))
          .text("please input your operation, such as: read, write, query")
      )
    }

    implicit val spark = SparkSession.builder
      .appName("Simple Spark Application")
      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .master("local[*]")
      .getOrCreate()

    OParser.parse(parser, args, Config()) match {
      case Some(config) => {
        config.op match {
          case "read" => HudiQuickStartExample.apply().query
          case "write" => HudiQuickStartExample.apply().write
          case _ => HudiQuickStartExample.apply().query
        }
      }
      case _ => throw new RuntimeException("no parameter")
    }
    spark.stop()
  }
}