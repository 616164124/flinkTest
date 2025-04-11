package com.mikael.day01;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * @author
 * @version 1.0
 * @date 2025/4/7
 */

public class SideOfDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(3);

        OutputTag<String> s1tag = new OutputTag<String>("s1", Types.STRING);
        OutputTag<String> s2tag = new OutputTag<String>("s2", Types.STRING);

        SingleOutputStreamOperator<String> process = env.socketTextStream("127.0.0.1", 7777).process(new ProcessFunction<String, String>() {
            @Override
            public void processElement(String value, Context ctx, Collector<String> out) throws Exception {
                if (Integer.parseInt(value) % 2 == 0) {
                    //偶数
                    ctx.output(s1tag, value);
                }else {
                    //奇数
                    ctx.output(s2tag,value);
                }
            }
        });

        process.getSideOutput(s1tag).print("偶数");
        process.getSideOutput(s2tag).print("奇数");
        process.print("主流");
        env.execute();

    }
}
