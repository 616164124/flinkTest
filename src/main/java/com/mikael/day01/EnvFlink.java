package com.mikael.day01;

import com.mikael.bean.WaterSensor;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author
 * @version 1.0
 * @date 2025/4/7
 */

public class EnvFlink {
    public static void main(String[] args) throws Exception {
        System.out.println("11111");
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        env.setParallelism(3);
//
//        DataStreamSource<WaterSensor> streamSource = env.fromElements(
//                new WaterSensor("1D", 11L, 1),
//                new WaterSensor("2D", 11L, 2),
//                new WaterSensor("3D", 33L, 3),
//                new WaterSensor("3D", 33L, 3)
//        );
//        streamSource.map(value -> value.getId()).print();
//
//        env.execute("envFlink");


        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // TODO �Ӽ��϶�ȡ����
        DataStreamSource<Integer> source = env
                .fromElements(1,2,33); // ��Ԫ�ض�
//                .fromCollection(Arrays.asList(1, 22, 3));  // �Ӽ��϶�


        source.print();

        env.execute();


    }


}
