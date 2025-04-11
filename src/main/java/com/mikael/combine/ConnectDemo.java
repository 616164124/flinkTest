package com.mikael.combine;

import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;
import org.apache.flink.streaming.api.functions.co.CoProcessFunction;
import org.apache.flink.util.Collector;

/**
 * @author
 * @version 1.0
 * @date 2025/4/7
 */

public class ConnectDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(3);
        DataStreamSource<String> streamSource7777 = env.socketTextStream("127.0.0.1", 7777);
        DataStreamSource<String> streamSource8888 = env.socketTextStream("127.0.0.1", 8888);

        ConnectedStreams<String, String> connect = streamSource7777.connect(streamSource8888);

        connect.map(new CoMapFunction<String, String, String>() {
            @Override
            public String map1(String value) throws Exception {
                return "来自7777: " + value;
            }

            @Override
            public String map2(String value) throws Exception {
                return "来自8888: " + value;
            }
        }).print();


        env.execute();
    }

}
