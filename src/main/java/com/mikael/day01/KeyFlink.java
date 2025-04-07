package com.mikael.day01;

import com.mikael.bean.WaterSensor;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author
 * @version 1.0
 * @date 2025/4/7
 */

public class KeyFlink {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(3);

        DataStreamSource<WaterSensor> streamSource = env.fromElements(new WaterSensor("1D", 1L, 1),
                new WaterSensor("2D", 2L, 2),
                new WaterSensor("3D", 3L, 3),
                new WaterSensor("3D", 4L, 3),
                new WaterSensor("4F", 44L, 444),
                new WaterSensor("5f", 5L, 15));

        streamSource.keyBy(var->var.getId()).print();

        env.execute("keyby");

    }
}
