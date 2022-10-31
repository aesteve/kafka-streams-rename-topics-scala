package com.github.aesteve

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.{StreamsBuilder, Topology}
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.KTable
import org.apache.kafka.streams.kstream.Materialized
import org.apache.kafka.streams.kstream.Produced
import org.apache.kafka.streams.processor.RecordContext
import org.apache.kafka.streams.state.KeyValueStore

import java.util
import scala.util.matching.Regex

def renamingRawTopics: Topology =
  val builder = new StreamsBuilder()
  val pattern: Regex = "raw[.](.*)".r
  val stream: KStream[String, String] = builder.stream(pattern.pattern)
  stream.to((_, _, ctx: RecordContext) => {
    ctx.topic().replace("raw.", "event.")
  })
  builder.build()


