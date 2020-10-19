# hudi-processing

Prototype for Hudi cdc. Spark Kafka not implemented yet.

## Amazon EMR

`emr-6.1.0` tem Spark `3.0.0`

## Build with docker

Linux and macOS

```
docker volume create sbt_data
docker volume create sbt_ivy_data
docker run -it --rm -v sbt_ivy_data:/root/.ivy2 -v sbt_data:/root/.sbt -v $PWD:/app -w /app mozilla/sbt sbt assembly
```

## Shell

```shell
spark-shell \
    --packages org.apache.hudi:hudi-spark-bundle_2.12:0.6.0,org.apache.spark:spark-avro_2.12:3.0.1,com.typesafe.play:play-json_2.12:2.9.1,com.amazonaws:aws-java-sdk-s3:1.11.882 \
    --conf 'spark.serializer=org.apache.spark.serializer.KryoSerializer'
```

## Run

```shell
```

# TODO

Pass Kafka Bootstrap Server to read direct from Kafka Broker

```scala
val lines = spark.readStream.format("kafka").option("kafka.bootstrap.servers", "XXXXXXXXXX").
            option("subscribe", "retail").
            load.select($"value".cast("string").alias("value"))
```


https://hudi.apache.org/docs/configurations.html

```scala
DataSourceWriteOptions.OPERATION_OPT_KEY:	hoodie.datasource.write.operation
DataSourceWriteOptions.BULK_INSERT_OPERATION_OPT_VAL:	bulk_insert
DataSourceWriteOptions.INSERT_OPERATION_OPT_VAL:	insert
DataSourceWriteOptions.UPSERT_OPERATION_OPT_VAL:	upsert
DataSourceWriteOptions.DELETE_OPERATION_OPT_VAL:	delete
DataSourceWriteOptions.BOOTSTRAP_OPERATION_OPT_VAL:	bootstrap
DataSourceWriteOptions.DEFAULT_OPERATION_OPT_VAL:	upsert
DataSourceWriteOptions.TABLE_TYPE_OPT_KEY:	hoodie.datasource.write.table.type
DataSourceWriteOptions.COW_TABLE_TYPE_OPT_VAL:	COPY_ON_WRITE
DataSourceWriteOptions.MOR_TABLE_TYPE_OPT_VAL:	MERGE_ON_READ
DataSourceWriteOptions.DEFAULT_TABLE_TYPE_OPT_VAL:	COPY_ON_WRITE
DataSourceWriteOptions.STORAGE_TYPE_OPT_KEY:	hoodie.datasource.write.storage.type
DataSourceWriteOptions.COW_STORAGE_TYPE_OPT_VAL:	COPY_ON_WRITE
DataSourceWriteOptions.MOR_STORAGE_TYPE_OPT_VAL:	MERGE_ON_READ
DataSourceWriteOptions.DEFAULT_STORAGE_TYPE_OPT_VAL:	COPY_ON_WRITE
DataSourceWriteOptions.TABLE_NAME_OPT_KEY:	hoodie.datasource.write.table.name
DataSourceWriteOptions.PRECOMBINE_FIELD_OPT_KEY:	hoodie.datasource.write.precombine.field
DataSourceWriteOptions.DEFAULT_PRECOMBINE_FIELD_OPT_VAL:	ts
DataSourceWriteOptions.PAYLOAD_CLASS_OPT_KEY:	hoodie.datasource.write.payload.class
DataSourceWriteOptions.DEFAULT_PAYLOAD_OPT_VAL:	org.apache.hudi.common.model.OverwriteWithLatestAvroPayload
DataSourceWriteOptions.RECORDKEY_FIELD_OPT_KEY:	hoodie.datasource.write.recordkey.field
DataSourceWriteOptions.DEFAULT_RECORDKEY_FIELD_OPT_VAL:	uuid
DataSourceWriteOptions.PARTITIONPATH_FIELD_OPT_KEY:	hoodie.datasource.write.partitionpath.field
DataSourceWriteOptions.DEFAULT_PARTITIONPATH_FIELD_OPT_VAL:	partitionpath
DataSourceWriteOptions.HIVE_STYLE_PARTITIONING_OPT_KEY:	hoodie.datasource.write.hive_style_partitioning
DataSourceWriteOptions.DEFAULT_HIVE_STYLE_PARTITIONING_OPT_VAL:	false
DataSourceWriteOptions.URL_ENCODE_PARTITIONING_OPT_KEY:	hoodie.datasource.write.partitionpath.urlencode
DataSourceWriteOptions.DEFAULT_URL_ENCODE_PARTITIONING_OPT_VAL:	false
DataSourceWriteOptions.KEYGENERATOR_CLASS_OPT_KEY:	hoodie.datasource.write.keygenerator.class
DataSourceWriteOptions.DEFAULT_KEYGENERATOR_CLASS_OPT_VAL:	org.apache.hudi.keygen.SimpleKeyGenerator
DataSourceWriteOptions.ENABLE_ROW_WRITER_OPT_KEY:	hoodie.datasource.write.row.writer.enable
DataSourceWriteOptions.DEFAULT_ENABLE_ROW_WRITER_OPT_VAL:	false
DataSourceWriteOptions.COMMIT_METADATA_KEYPREFIX_OPT_KEY:	hoodie.datasource.write.commitmeta.key.prefix
DataSourceWriteOptions.DEFAULT_COMMIT_METADATA_KEYPREFIX_OPT_VAL:	_
DataSourceWriteOptions.INSERT_DROP_DUPS_OPT_KEY:	hoodie.datasource.write.insert.drop.duplicates
DataSourceWriteOptions.DEFAULT_INSERT_DROP_DUPS_OPT_VAL:	false
DataSourceWriteOptions.STREAMING_RETRY_CNT_OPT_KEY:	hoodie.datasource.write.streaming.retry.count
DataSourceWriteOptions.DEFAULT_STREAMING_RETRY_CNT_OPT_VAL:	3
DataSourceWriteOptions.STREAMING_RETRY_INTERVAL_MS_OPT_KEY:	hoodie.datasource.write.streaming.retry.interval.ms
DataSourceWriteOptions.DEFAULT_STREAMING_RETRY_INTERVAL_MS_OPT_VAL:	2000
DataSourceWriteOptions.STREAMING_IGNORE_FAILED_BATCH_OPT_KEY:	hoodie.datasource.write.streaming.ignore.failed.batch
DataSourceWriteOptions.DEFAULT_STREAMING_IGNORE_FAILED_BATCH_OPT_VAL:	true
DataSourceWriteOptions.META_SYNC_CLIENT_TOOL_CLASS:	hoodie.meta.sync.client.tool.class
DataSourceWriteOptions.DEFAULT_META_SYNC_CLIENT_TOOL_CLASS:	org.apache.hudi.hive.HiveSyncTool
DataSourceWriteOptions.HIVE_SYNC_ENABLED_OPT_KEY:	hoodie.datasource.hive_sync.enable
DataSourceWriteOptions.META_SYNC_ENABLED_OPT_KEY:	hoodie.datasource.meta.sync.enable
DataSourceWriteOptions.HIVE_DATABASE_OPT_KEY:	hoodie.datasource.hive_sync.database
DataSourceWriteOptions.HIVE_TABLE_OPT_KEY:	hoodie.datasource.hive_sync.table
DataSourceWriteOptions.HIVE_BASE_FILE_FORMAT_OPT_KEY:	hoodie.datasource.hive_sync.base_file_format
DataSourceWriteOptions.HIVE_USER_OPT_KEY:	hoodie.datasource.hive_sync.username
DataSourceWriteOptions.HIVE_PASS_OPT_KEY:	hoodie.datasource.hive_sync.password
DataSourceWriteOptions.HIVE_URL_OPT_KEY:	hoodie.datasource.hive_sync.jdbcurl
DataSourceWriteOptions.HIVE_PARTITION_FIELDS_OPT_KEY:	hoodie.datasource.hive_sync.partition_fields
DataSourceWriteOptions.HIVE_PARTITION_EXTRACTOR_CLASS_OPT_KEY:	hoodie.datasource.hive_sync.partition_extractor_class
DataSourceWriteOptions.HIVE_ASSUME_DATE_PARTITION_OPT_KEY:	hoodie.datasource.hive_sync.assume_date_partitioning
DataSourceWriteOptions.HIVE_USE_PRE_APACHE_INPUT_FORMAT_OPT_KEY:	hoodie.datasource.hive_sync.use_pre_apache_input_format
DataSourceWriteOptions.HIVE_USE_JDBC_OPT_KEY:	hoodie.datasource.hive_sync.use_jdbc
DataSourceWriteOptions.DEFAULT_HIVE_SYNC_ENABLED_OPT_VAL:	false
DataSourceWriteOptions.DEFAULT_META_SYNC_ENABLED_OPT_VAL:	false
DataSourceWriteOptions.DEFAULT_HIVE_DATABASE_OPT_VAL:	default
DataSourceWriteOptions.DEFAULT_HIVE_TABLE_OPT_VAL:	unknown
DataSourceWriteOptions.DEFAULT_HIVE_BASE_FILE_FORMAT_OPT_VAL:	PARQUET
DataSourceWriteOptions.DEFAULT_HIVE_USER_OPT_VAL:	hive
DataSourceWriteOptions.DEFAULT_HIVE_PASS_OPT_VAL:	hive
DataSourceWriteOptions.DEFAULT_HIVE_URL_OPT_VAL:	jdbc:hive2://localhost:10000
DataSourceWriteOptions.DEFAULT_HIVE_PARTITION_FIELDS_OPT_VAL:
DataSourceWriteOptions.DEFAULT_HIVE_PARTITION_EXTRACTOR_CLASS_OPT_VAL:	org.apache.hudi.hive.SlashEncodedDayPartitionValueExtractor
DataSourceWriteOptions.DEFAULT_HIVE_ASSUME_DATE_PARTITION_OPT_VAL:	false
DataSourceWriteOptions.DEFAULT_USE_PRE_APACHE_INPUT_FORMAT_OPT_VAL:	false
DataSourceWriteOptions.DEFAULT_HIVE_USE_JDBC_OPT_VAL:	true
DataSourceWriteOptions.ASYNC_COMPACT_ENABLE_OPT_KEY:	hoodie.datasource.compaction.async.enable
DataSourceWriteOptions.DEFAULT_ASYNC_COMPACT_ENABLE_OPT_VAL:	true
```
