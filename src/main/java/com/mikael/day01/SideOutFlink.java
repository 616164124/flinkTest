package com.mikael.day01;

import com.mikael.bean.WaterSensor;
import com.mikael.funcation.WaterSensorMapFuncation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.SideOutputDataStream;
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

public class SideOutFlink {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(3);


        SingleOutputStreamOperator<WaterSensor> map = (SingleOutputStreamOperator<WaterSensor>) env.socketTextStream("127.0.0.1", 7777)
                .map(new WaterSensorMapFuncation());

        OutputTag<WaterSensor> s1tag = new OutputTag("s1", Types.POJO(WaterSensor.class));
        OutputTag<WaterSensor> s2tag = new OutputTag("s2", Types.POJO(WaterSensor.class));

        SingleOutputStreamOperator<WaterSensor> process = map.process(new ProcessFunction<WaterSensor, WaterSensor>() {
            @Override
            public void processElement(WaterSensor value, Context ctx, Collector<WaterSensor> out) throws Exception {
                if (value.getId().equals("s1")) {
                    ctx.output(s1tag, value);
                }
                if (value.getId().equals("s2")) {
                    ctx.output(s2tag, value);
                }
                out.collect(value);
            }
        });

        SideOutputDataStream<WaterSensor> s1 = process.getSideOutput(s1tag);
        SideOutputDataStream<WaterSensor> s2 = process.getSideOutput(s2tag);


        process.print("主流");

        s1.print("s1流");
        s2.print("s2流");
        env.execute();
    }
}
