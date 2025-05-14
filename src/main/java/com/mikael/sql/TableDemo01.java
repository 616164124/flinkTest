package com.mikael.sql;

import com.mikael.bean.WaterSensor;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

/**
 * @author
 * @version 1.0
 * @date 2025/4/18
 */

public class TableDemo01 {
  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
    environment.setParallelism(3);
    DataStreamSource<WaterSensor> streamSource =
        environment.fromElements(
            new WaterSensor("a1", 1L, 1),
            new WaterSensor("a1", 1L, 1),
            new WaterSensor("a2", 9L, 2),
            new WaterSensor("a3", 3L, 3));


    StreamTableEnvironment tableEnvironment = StreamTableEnvironment.create(environment);

    Table table = tableEnvironment.fromDataStream(streamSource);
    tableEnvironment.toDataStream(table).print();
    tableEnvironment.createTemporaryView("sernsor", table);
    int a=2;
    //根据sql来执行，将结果存入tableResult
    TableResult tableResult = tableEnvironment.executeSql("select * from sernsor where ts> 2");
    tableResult.print();
    environment.execute();
  }
}
