scalaVersion := "2.12.10"
name := "hudi-processing"
// organization := "com.yuriniitsuma"
version := "1.0"

val sparkVersion = "3.0.1"

// Spark libraries provided to intelSense on VSCode
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core",
  "org.apache.spark" %% "spark-sql",
  "org.apache.spark" %% "spark-hive"
).map(_ % sparkVersion % "provided")

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2"

// https://mvnrepository.com/artifact/org.apache.hudi/hudi-spark-bundle_2.12/0.6.0
libraryDependencies += "org.apache.hudi" %% "hudi-spark-bundle" % "0.6.0"
libraryDependencies += "org.apache.spark" %% "spark-avro" % sparkVersion

// https://mvnrepository.com/artifact/com.typesafe.play/play-json
// Works with 2.7.4 on spark 2.4.4 scala 2.11
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.9.1"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x                             => MergeStrategy.first
}
