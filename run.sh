#!/bin/bash

spark-submit \
    --conf 'spark.serializer=org.apache.spark.serializer.KryoSerializer' \
    --class hudiprocessing.Main \
    target/scala-2.12/hudi-processing-assembly-1.0.jar \
    '''{
        "service": "emr",
        "params": {
            "source": "s3://yuriniitsuma/kafka/databases/EXAMPLE.dbo.persons",
            "target": "hdfs:///local/hudi/table/EXAMPLE.dbo.persons",
            "hudiParams": {}
        }
    }'''

# HoodieWriteConfig.TABLE_NAME -> this.tableName,
#       DataSourceWriteOptions.STORAGE_TYPE_OPT_KEY -> this.storageType,
#       DataSourceWriteOptions.RECORDKEY_FIELD_OPT_KEY -> recordKey,
#       DataSourceWriteOptions.PARTITIONPATH_FIELD_OPT_KEY -> partitionPath,
#       DataSourceWriteOptions.PRECOMBINE_FIELD_OPT_KEY -> this.precombineKey,
#       "hoodie.cleaner.commits.retained" -> "1",
#       "hoodie.bulkinsert.shuffle.parallelism" -> "100",
#       "hoodie.insert.shuffle.parallelism" -> "100",
#       "hoodie.upsert.shuffle.parallelism" -> "100"