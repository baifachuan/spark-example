name :="spark-examples"
version :="0.1.0-SNAPSHOT"
organization := "com.fcbai.demo"
scalaVersion :="2.12.10"


lazy val root = project in file(".")


libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.1.2",
  "org.apache.spark" %% "spark-sql" % "3.1.2",
  "org.apache.spark" %% "spark-mllib" % "3.1.2",
  "org.apache.hudi" %% "hudi-spark-bundle" % "0.8.0",
  "org.apache.hive" % "hive-exec" % "3.1.2",
  "org.apache.logging.log4j" % "log4j-api" % "2.14.1",
  "org.apache.logging.log4j" % "log4j-core" % "2.14.1",
  "com.github.scopt" %% "scopt" % "4.0.1",
  "org.apache.hadoop" % "hadoop-client" % "3.3.1",
  "org.scalatest" %% "scalatest" % "3.2.9" % "test"
)