package com.mikael.day01;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 * @author
 * @version 1.0
 * @date 2025/4/7
 */

public class KafkaSourceFlink {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(3);


        KafkaSource<String> kafkaSourceBuilder = KafkaSource.<String>builder().setBootstrapServers("127.0.0.1:9092").setGroupId("").setTopics("test_topic")
                .setValueOnlyDeserializer(new SimpleStringSchema()).setStartingOffsets(OffsetsInitializer.latest()).build();

        env.fromSource(kafkaSourceBuilder, WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(3)), "kafka").flatMap(
                new FlatMapFunction<String, String>() {
                    @Override
                    public void flatMap(String value, Collector<String> out) throws Exception {
                        if (value.contains("lf")) {
                            out.collect(value);
                        }
                    }
                }

        ).print();


        env.execute();

    }
}
