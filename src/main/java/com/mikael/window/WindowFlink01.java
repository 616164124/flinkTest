package com.mikael.window;

import com.mikael.bean.WaterSensor;
import com.mikael.funcation.MyMapFuncation;
import com.mikael.funcation.WaterSensorMapFuncation;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

/**
 * @author
 * @version 1.0
 * @date 2025/4/8
 */

public class WindowFlink01 {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);


        SingleOutputStreamOperator<WaterSensor> sensorDS = env
                .socketTextStream("127.0.0.1", 7777)
                .map(new WaterSensorMapFuncation());
        KeyedStream<WaterSensor, String> keyedStream = sensorDS.keyBy(value -> value.getId());

        WindowedStream<WaterSensor, String, GlobalWindow> windowedStream = keyedStream.countWindow(5, 2);

        windowedStream.process(new ProcessWindowFunction<WaterSensor, String, String, GlobalWindow>() {
            @Override
            public void process(String s, Context context, Iterable<WaterSensor> elements, Collector<String> out) throws Exception {
                long maxTimestamp = context.window().maxTimestamp();
                String maxTime = DateFormatUtils.format(maxTimestamp, "yyyy-MM-dd HH:mm:ss.SSS");
                long count = elements.spliterator().estimateSize();

                out.collect("key=" + s + "的窗口最大时间=" + maxTime + ",包含" + count + "条数据===>" + elements.toString());

            }
        }).print();

        env.execute();
    }
}
