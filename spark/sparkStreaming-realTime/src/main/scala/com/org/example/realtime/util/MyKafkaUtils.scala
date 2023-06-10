package com.org.example.realtime.util

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}

import scala.collection.mutable


/**
 * Kafka工具类，用于生产数据和消费数据
 */
object MyKafkaUtils {
  /**
   * 消费者配置
   */
  private val consumerConfigs: mutable.Map[String, Object] = mutable.Map[String, Object](
    //kafka集群位置
    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> MyPropsUtils(MyConfig.KAFKA_BOOTSTRAP_SERVERS),
    //kv反序列化器
    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringDeserializer",
    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringDeserializer",

    //groupId
    //offset提交 :自动，手动
    ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG -> "true",
    //offset重置 :latest,earliest
    ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> "latest"
  )

  /**
   * 基于SparkStreaming消费,获取KafkaDStream
   */
  def getKafkaDStream(ssc: StreamingContext, topic: String, groupId: String):InputDStream[ConsumerRecord[String,String]] = {
    consumerConfigs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId)

    val kafkaDStream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream(ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](Array(topic), consumerConfigs))
    kafkaDStream
  }

  /**
   * 生产者对象
   */
  val produce: KafkaProducer[String, String] = null

  /**
   * 创建生产者对象
   */
  /**
   * 生产
   */
}
