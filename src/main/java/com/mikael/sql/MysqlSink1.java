package com.mikael.sql;

import com.mikael.bean.WaterSensor;
import com.mikael.funcation.MyMapFuncation;
import com.mikael.funcation.WaterSensorMapFuncation;
import com.mysql.cj.MysqlConnection;
import com.mysql.cj.jdbc.JdbcConnection;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.mysql.cj.jdbc.MysqlXADataSource;
import org.apache.flink.api.connector.sink.Sink;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.runtime.execution.Environment;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.table.api.bridge.java.StreamStatementSet;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author
 * @version 1.0
 * @date 2025/4/18
 */
public class MysqlSink1 {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(3);

    SingleOutputStreamOperator<WaterSensor> map = env.socketTextStream("127.0.0.1", 7777).map(new WaterSensorMapFuncation());




    SinkFunction<WaterSensor> jdbcSink = JdbcSink.sink(
            "insert into watersensor values(?,?,?)",
            new JdbcStatementBuilder<WaterSensor>() {
              @Override
              public void accept(PreparedStatement preparedStatement, WaterSensor waterSensor) throws SQLException {
                //每收到一条WaterSensor，如何去填充占位符
                preparedStatement.setString(1, waterSensor.getId());
                preparedStatement.setLong(2, waterSensor.getTs());
                preparedStatement.setInt(3, waterSensor.getVc());
              }
            },
            JdbcExecutionOptions.builder()
                    .withMaxRetries(3) // 重试次数
                    .withBatchSize(100) // 批次的大小：条数
                    .withBatchIntervalMs(3000) // 批次的时间
                    .build(),
            new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                    .withUrl("jdbc:mysql://localhost:3306/flink?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8")
                    .withUsername("root")
                    .withPassword("123456")
                    .withConnectionCheckTimeoutSeconds(60) // 重试的超时时间
                    .build()
    );

      map.print();
      map.addSink(jdbcSink);

      env.execute();






  }
}
