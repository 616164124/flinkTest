package com.mikael.window;

import com.mikael.bean.WaterSensor;
import com.mikael.funcation.WaterSensorMapFuncation;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

/**
 * @author
 * @version 1.0
 * @date 2025/4/8
 */

public class WindowsReduceDemo1 {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(3);


        SingleOutputStreamOperator<WaterSensor> stream = environment.socketTextStream("127.0.0.1", 7777).map(new WaterSensorMapFuncation());

        KeyedStream<WaterSensor, String> keyedStream = stream.keyBy(value -> value.getId());
        //窗口分配器
        //WindowedStream<WaterSensor, String, TimeWindow> window = keyedStream.window(TumblingProcessingTimeWindows.of(Time.seconds(10)));
        WindowedStream<WaterSensor, String, TimeWindow> window = keyedStream.window(SlidingProcessingTimeWindows.of(Time.seconds(2), Time.seconds(3), Time.seconds(2)));


        //窗口函数
        SingleOutputStreamOperator<WaterSensor> process = window.reduce(new ReduceFunction<WaterSensor>() {
            //只有key相同才进入reduce方法
            @Override
            public WaterSensor reduce(WaterSensor value1, WaterSensor value2) throws Exception {
                System.out.println("value1:"+value1.getId()+","+ value1.getTs()+","+value1.getVc());
                System.out.println("value2:"+value2.getId()+","+ value2.getTs()+","+value2.getVc());
                return new WaterSensor(value1.getId(), value1.getTs(), value1.getVc() + value2.getVc());
            }
        });

        process.print();

        environment.execute();


    }
}
