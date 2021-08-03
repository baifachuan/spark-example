name :="spark-examples"

version :="0.1.0-SNAPSHOT"

scalaVersion :="2.12.2"

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.fcbai"
ThisBuild / scalaVersion := "2.12.2"

val root = (project in file("."))

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.1.2",
  "org.apache.spark" %% "spark-sql" % "3.1.2",
  "org.apache.spark" %% "spark-mllib" % "3.1.2"
//  "org.apache.hudi" %% "hudi-spark-bundle" % "0.8.0"
)