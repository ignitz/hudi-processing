#!/bin/bash

time (sbt assembly && spark-submit \
    --conf 'spark.serializer=org.apache.spark.serializer.KryoSerializer' \
    --class hudiprocessing.Main \
    target/scala-2.12/hudi-processing-assembly-1.0.jar \
    '''{
        "service": "emr",
        "params": {
            "configPath": "s3://333728661073-dev-configs/hudi/config.json"
        }
    }''')

    # {
    #     "service": "emr",
    #     "params": {
    #         "sourcePath": "fs/s3/333728661073-dev-kafka-raw/kafka/cdc/DATAFEEDER_register.dbo.persons/",
    #         "sourceFormat": "parquet",
    #         "targetPath": "fs/hdfs/local/hudi/table/EXAMPLE.dbo.persons",
    #         "hudiParams": {}
    #     }
    # }

    # s3/333728661073-dev-kafka-raw/kafka/cdc/

    # DATAFEEDER_register.dbo.addresses/        
    # DATAFEEDER_register.dbo.companies/        
    # DATAFEEDER_register.dbo.persons/          
    # DATAFEEDER_register.dbo.persons_companies/
    # DATAFEEDER_tracker.public.posts/          
    # DATAFEEDER_tracker.public.users/   

# HoodieWriteConfig.TABLE_NAME -> this.tableName,
#       DataSourceWriteOptions.STORAGE_TYPE_OPT_KEY -> this.storageType,
#       DataSourceWriteOptions.RECORDKEY_FIELD_OPT_KEY -> recordKey,
#       DataSourceWriteOptions.PARTITIONPATH_FIELD_OPT_KEY -> partitionPath,
#       DataSourceWriteOptions.PRECOMBINE_FIELD_OPT_KEY -> this.precombineKey,
#       "hoodie.cleaner.commits.retained" -> "1",
#       "hoodie.bulkinsert.shuffle.parallelism" -> "100",
#       "hoodie.insert.shuffle.parallelism" -> "100",
#       "hoodie.upsert.shuffle.parallelism" -> "100"