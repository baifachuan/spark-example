name :="spark-examples"
version :="0.1.0-SNAPSHOT"
organization := "com.fcbai.demo"
scalaVersion :="2.12.10"


lazy val root = project in file(".")


libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.1.2",
  "org.apache.spark" %% "spark-sql" % "3.1.2",
  "org.apache.spark" %% "spark-mllib" % "3.1.2",
  "org.apache.hudi" %% "hudi-spark-bundle" % "0.8.0"
)