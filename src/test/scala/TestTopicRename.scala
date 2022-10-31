package com.github.aesteve

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.serialization.Serdes.StringSerde
import org.apache.kafka.streams.{StreamsBuilder, StreamsConfig, TopologyTestDriver}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers

import java.util.Properties

class TestTopicRename extends AnyFlatSpec, Matchers:

  "The topics" should "be renamed properly" in {
    val props = new Properties()
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy") // we're using TopologyTestDriver there's no need for it
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, classOf[StringSerde].getName)
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, classOf[StringSerde].getName)
    val driver = new TopologyTestDriver(renamingRawTopics, props)
    val serde = Serdes.String()
    val originTopicName1 = "something"
    val originTopicName2 = "somethingelse"
    val raw1 = driver.createInputTopic(s"raw.$originTopicName1", serde.serializer(), serde.serializer())
    val raw2 = driver.createInputTopic(s"raw.$originTopicName2", serde.serializer(), serde.serializer())
    val output1 = driver.createOutputTopic(s"event.$originTopicName1", serde.deserializer(), serde.deserializer())
    val output2 = driver.createOutputTopic(s"event.$originTopicName2", serde.deserializer(), serde.deserializer())
    val (rec1Key, rec1Value) = ("k1", "raw event number 1, some value 1")
    val (rec2Key, rec2Value) = ("k2", "raw event number 2, some value 2")
    raw1.pipeInput(rec1Key, rec1Value)
    raw2.pipeInput(rec2Key, rec2Value)

    val readFromOut1 = output1.readRecord()
    val readFromOut2 = output2.readRecord()

    readFromOut1.key() mustEqual rec1Key
    readFromOut1.value() mustEqual rec1Value
    readFromOut2.key() mustEqual rec2Key
    readFromOut2.value() mustEqual rec2Value
  }

