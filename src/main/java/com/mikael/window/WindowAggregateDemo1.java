package com.mikael.window;

import com.mikael.bean.WaterSensor;
import com.mikael.funcation.WaterSensorMapFuncation;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

/**
 * @author
 * @version 1.0
 * @date 2025/4/9
 */

public class WindowAggregateDemo1 {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(3);
        DataStreamSource<String> streamSource = env.socketTextStream("127.0.0.1", 7777);


        SingleOutputStreamOperator<WaterSensor> map = streamSource.map(new WaterSensorMapFuncation());

        KeyedStream<WaterSensor, String> keyedStream = map.keyBy(value -> value.getId());

        WindowedStream<WaterSensor, String, TimeWindow> window = keyedStream.window(TumblingProcessingTimeWindows.of(Time.seconds(5)));

        window.aggregate(new AggregateFunction<WaterSensor, Integer, String>() {

            @Override
            public Integer createAccumulator() {
                System.out.println("调用createAccumulator方法");
                return 0;
            }

            @Override
            public Integer add(WaterSensor value, Integer accumulator) {
                System.out.println("调用add方法,value="+value);
                System.out.println("此时accumulator的值："+accumulator);
                return value.getVc()+accumulator;
            }

            @Override
            public String getResult(Integer accumulator) {
                System.out.println("调用getResult方法");
                return accumulator.toString();
            }

            @Override
            public Integer merge(Integer a, Integer b) {
                // 只有会话窗口才会用到
                return null;
            }
        }).print();

        env.execute();


    }
}
