package com.org.example.realtime.app

import com.org.example.realtime.util.MyKafkaUtils
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet, Timestamp}
import java.text.SimpleDateFormat
import java.util.Date

object OdsBaseLogApp {

  case class RealTimeClick(userId: String, newsId: String, dwellTime: Int, endTime: String)

  def main(args:Array[String]):Unit={
    //准备实时环境
    val sparkConf = new SparkConf().setAppName("ods_base_log_app")
      .setMaster("local[*]")
      .set("spark.testing.memory","2147480000")
      .set("spark.testing","true")
      .set("spark.testing.reservedMemory","0")

    //准备数据库连接配置
    val url = "jdbc:mysql://Master:3306/BI"
    val username = "yfy"
    val password = "yfy123"

    val ssc:StreamingContext =new StreamingContext(sparkConf,Seconds(5))

    //从kafka中消费数据
    val topicName :String ="BI_News"
    val groupId:String="ODS_BASE_LOG_GROUP_1"
    val kafkaDStream:InputDStream[ConsumerRecord[String,String]] = MyKafkaUtils.getKafkaDStream(ssc, topicName, groupId)

    // 转换数据结构，实现流
    val stringDStream: DStream[String] = kafkaDStream.map(_.value())

    // 查看消费的数据(验证数据成功进入)
    stringDStream.print(100)

    // Task1：将基础的点击流数据结构化后实时写入MySQL
    stringDStream.foreachRDD { rdd =>
      rdd.foreachPartition { partitionOfRecords =>
        val connection: Connection = DriverManager.getConnection(url, username, password)
        writeToMySQL(connection, partitionOfRecords)
        connection.close()
      }
    }


    // Task2：统计用户的兴趣
    stringDStream.foreachRDD { rdd =>
      rdd.foreachPartition { partitionOfRecords =>
        val connection: Connection = DriverManager.getConnection(url, username, password)
        val statement: PreparedStatement = connection.prepareStatement("INSERT INTO real_time_user_interest (user_id, category, time_period, interest_clicks) VALUES (?, ?, ?, ?)")

        val parseRealTimeClickFunc = parseRealTimeClick _
        val getNewsCategoryFunc = getNewsCategory _

        partitionOfRecords.foreach { record =>
          val realTimeClick = parseRealTimeClickFunc(record) //解析日志数据
          if (realTimeClick.dwellTime > 5) {//超过5s的都算作感兴趣，筛除误触的点击
            val category = getNewsCategoryFunc(realTimeClick.newsId, connection) //去数据库查找电影种类
            val timePeriod = getCurrentHour(realTimeClick.endTime) // 使用 endTime 转换为精度到小时的时间字符串

            val interestClicks = calculateInterestClicks(realTimeClick.dwellTime, connection, realTimeClick.userId, category, timePeriod)

            // 设置参数并插入数据库
            statement.setString(1, realTimeClick.userId)
            statement.setString(2, category)
            statement.setString(3, timePeriod)
            statement.setInt(4, interestClicks)

            statement.executeUpdate()
          }
        }

        statement.close()
        connection.close()
      }
    }

    ssc.start()
    ssc.awaitTermination()
  }

  def writeToMySQL(connection: Connection, records: Iterator[String]): Unit = {
    val statement: PreparedStatement = connection.prepareStatement("INSERT INTO real_time_click (user_id, news_id, dwell_time, end_time) VALUES (?, ?, ?, ?)")
    records.foreach { record =>
      val realTimeClick = parseRealTimeClick(record)
      // 设置参数
      statement.setString(1, realTimeClick.userId)
      statement.setString(2, realTimeClick.newsId)
      statement.setInt(3, realTimeClick.dwellTime)
      statement.setString(4, realTimeClick.endTime)

      statement.executeUpdate()
    }
    statement.close()
  }

  //解析实时点击数据，将点击数据日志转换为格式化的数据
  def parseRealTimeClick(data: String): RealTimeClick = {
    val keyValuePairs = data.split(", ")
    var userId = ""
    var newsId = ""
    var dwellTime = 0
    var endTime: String = ""

    for (pair <- keyValuePairs) {
      val keyValue = pair.split(": ")
      val key = keyValue(0).trim
      val value = keyValue(1).trim

      key match {
        case "user_id" => userId = value
        case "news_id" => newsId = value
        case "dwell_time" => dwellTime = value.toInt
        case "end_time" => endTime = value
        case _ => // Handle unknown keys if necessary
      }
    }

    RealTimeClick(userId, newsId, dwellTime, endTime)
  }

  def getCurrentHour(endTime: String): String = {
    val format = new SimpleDateFormat("yyyy/MM/dd HH:mm")
    val parsedDate = format.parse(endTime)
    val hourFormat = new SimpleDateFormat("yyyy-MM-dd HH:00:00")
    hourFormat.format(parsedDate)
  }

  def getNewsCategory(newsId: String, connection: Connection): String = {
    val statement: PreparedStatement = connection.prepareStatement("SELECT category FROM news WHERE news_id = ?")
    statement.setString(1, newsId)
    val resultSet = statement.executeQuery()

    val category = if (resultSet.next()) {
      resultSet.getString("category")
    } else {
      // 如果没有找到匹配的新闻，可以在这里设置默认的分类或采取其他处理方式
      "未知分类"
    }

    resultSet.close()
    statement.close()

    category
  }

  def calculateInterestClicks(dwellTime: Int, connection: Connection, userId: String, category: String, timePeriod: String): Int = {
    // 查询数据库中当前用户、分类、时间段的累计点击次数
    val selectStatement: PreparedStatement = connection.prepareStatement("SELECT interest_clicks FROM real_time_user_interest WHERE user_id = ? AND category = ? AND time_period = ?")
    selectStatement.setString(1, userId)
    selectStatement.setString(2, category)
    selectStatement.setString(3, timePeriod)

    val resultSet: ResultSet = selectStatement.executeQuery()

    if (resultSet.next()) {
      val previousClicks = resultSet.getInt("interest_clicks")
      previousClicks + 1 // 累计点击次数加1
    } else {
      1 // 首次点击，返回点击次数1
    }
  }

}
