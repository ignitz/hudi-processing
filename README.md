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
    --packages org.apache.hudi:hudi-spark-bundle_2.12:0.6.0,org.apache.spark:spark-avro_2.12:3.0.1,com.typesafe.play:play-json_2.12:2.9.1 \
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

