package com.mikael.day01;

import com.mikael.bean.WaterSensor;
import com.mikael.funcation.MyMapFuncation;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @author
 * @version 1.0
 * @date 2025/4/7
 */

public class MapFlink {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(3);

        DataStreamSource<WaterSensor> streamSource = env.fromElements(
                new WaterSensor("1D", 1L, 1),
                new WaterSensor("2D", 2L, 2),
                new WaterSensor("3D", 3L, 3),
                new WaterSensor("3D", 4L, 3),
                new WaterSensor("4F", 44L, 444),
                new WaterSensor("5f", 5L, 15)
        );


        streamSource.map(new MyMapFuncation()).flatMap(new FlatMapFunction<WaterSensor, String>() {
            @Override
            public void flatMap(WaterSensor value, Collector<String> out) throws Exception {
                System.out.println(value+"========");
                if (value.getId().equals("3D")) {
                    System.out.println(value);
                    out.collect(value.getId());
                }
            }
        }).print();

        env.execute("MapFlink");

    }
}


