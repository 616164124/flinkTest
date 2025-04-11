package com.mikael.window;

import com.mikael.bean.WaterSensor;
import com.mikael.funcation.WaterSensorMapFuncation;
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
 * @date 2025/4/9
 */
public class WindowCountDemo1 {
  public static void main(String[] args) throws Exception {
    //
    StreamExecutionEnvironment env =
        StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(3);

    KeyedStream<WaterSensor, String> keyedStream =
        env.socketTextStream("127.0.0.1", 7777)
            .map(new WaterSensorMapFuncation())
            .keyBy(value -> value.getId());

    WindowedStream<WaterSensor, String, GlobalWindow> windowedStream = keyedStream.countWindow(3);
    SingleOutputStreamOperator<Object> process =
        windowedStream.process(
            new ProcessWindowFunction<WaterSensor, Object, String, GlobalWindow>() {
              @Override
              public void process(
                  String s, Context context, Iterable<WaterSensor> elements, Collector<Object> out)
                  throws Exception {
                System.out.println("s="+s);


              }
            });

    process.print();
    env.execute();
  }
}
