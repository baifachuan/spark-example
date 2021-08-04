package com.fcbai.spark.example

import org.apache.hudi.DataSourceWriteOptions._
import org.apache.hudi.QuickstartUtils._
import org.apache.hudi.config.HoodieWriteConfig._
import org.apache.spark.sql.SaveMode._
import org.apache.spark.sql.SparkSession
import scala.collection.JavaConversions._

class HudiQuickStartExample {
  val tableName = "hudi_trips_cow"
  val basePath = "file:///tmp/hudi_trips_cow"
  val dataGen = new DataGenerator

  def write(implicit spark: SparkSession): Unit = {
    val inserts = convertToStringList(dataGen.generateInserts(10))
    val df = spark.read.json(spark.sparkContext.parallelize(inserts, 2))

    df.write.format("hudi").
      options(getQuickstartWriteConfigs).
      option(PRECOMBINE_FIELD_OPT_KEY, "ts").
      option(RECORDKEY_FIELD_OPT_KEY, "uuid").
      option(PARTITIONPATH_FIELD_OPT_KEY, "partitionpath").
      option(TABLE_NAME, tableName).
      mode(Overwrite).
      save(basePath)
    df.show()
  }

  def query(implicit spark: SparkSession): Unit = {
    val tripsSnapshotDF = spark.
      read.
      format("hudi").
      load(basePath + "/*/*/*/*")
    //load(basePath) use "/partitionKey=partitionValue" folder structure for Spark auto partition discovery
    tripsSnapshotDF.createOrReplaceTempView("hudi_trips_snapshot")

    spark.sql("select fare, begin_lon, begin_lat, ts from  hudi_trips_snapshot where fare > 20.0").show()
    spark.sql("select _hoodie_commit_time, _hoodie_record_key, _hoodie_partition_path, rider, driver, fare from  hudi_trips_snapshot").show()
  }

  def update(implicit spark: SparkSession): Unit = {
    val updates = convertToStringList(dataGen.generateUpdates(10))
    val df = spark.read.json(spark.sparkContext.parallelize(updates, 2))
    df.write.format("hudi").
      options(getQuickstartWriteConfigs).
      option(PRECOMBINE_FIELD_OPT_KEY, "ts").
      option(RECORDKEY_FIELD_OPT_KEY, "uuid").
      option(PARTITIONPATH_FIELD_OPT_KEY, "partitionpath").
      option(TABLE_NAME, tableName).
      mode(Append).
      save(basePath)
  }

  def delete(implicit spark: SparkSession): Unit = {
    spark.sql("select uuid, partitionpath from hudi_trips_snapshot").count()
    // fetch two records to be deleted
    val ds = spark.sql("select uuid, partitionpath from hudi_trips_snapshot").limit(2)

    // issue deletes
    val deletes = dataGen.generateDeletes(ds.collectAsList())
    val df = spark.read.json(spark.sparkContext.parallelize(deletes, 2))

    df.write.format("hudi").
      options(getQuickstartWriteConfigs).
      option(OPERATION_OPT_KEY,"delete").
      option(PRECOMBINE_FIELD_OPT_KEY, "ts").
      option(RECORDKEY_FIELD_OPT_KEY, "uuid").
      option(PARTITIONPATH_FIELD_OPT_KEY, "partitionpath").
      option(TABLE_NAME, tableName).
      mode(Append).
      save(basePath)

    // run the same read query as above.
    val roAfterDeleteViewDF = spark.
      read.
      format("hudi").
      load(basePath + "/*/*/*/*")

    roAfterDeleteViewDF.registerTempTable("hudi_trips_snapshot")
    // fetch should return (total - 2) records
    spark.sql("select uuid, partitionpath from hudi_trips_snapshot").count()
  }
}

object HudiQuickStartExample {
  def apply(): HudiQuickStartExample = new HudiQuickStartExample()
}
