package com.mikael.cep;

import com.mikael.bean.WaterSensor;
import com.mikael.funcation.WaterSensorMapFuncation;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternFlatSelectFunction;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.List;
import java.util.Map;

/**
 * @author
 * @version 1.0
 * @date 2025/4/18
 */
public class CepDemo01 {
  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(3);

    SingleOutputStreamOperator<WaterSensor> streamOperator =
        env.socketTextStream("127.0.0.1", 7777).map(new WaterSensorMapFuncation());

    streamOperator.print("000000");

    Pattern<WaterSensor, WaterSensor> pattern =
        Pattern.<WaterSensor>begin("begin")
            .where(
                new SimpleCondition<WaterSensor>() {
                  @Override
                  public boolean filter(WaterSensor value) throws Exception {
                    System.out.println(value.getId() + "====");
                    return (value.getVc() > 2);
                  }
                })
            .next("next....")
            .where(
                new SimpleCondition<WaterSensor>() {
                  @Override
                  public boolean filter(WaterSensor value) throws Exception {

                    return value.getVc() < 10;
                  }
                })
            .oneOrMore();

    PatternStream<WaterSensor> patternStream =
        CEP.pattern(streamOperator.keyBy(value -> value.getId()), pattern);

    patternStream
        .select(
            new PatternSelectFunction<WaterSensor, WaterSensor>() {
              @Override
              public WaterSensor select(Map<String, List<WaterSensor>> pattern) throws Exception {
                System.out.println("]]]]]]]");
                return new WaterSensor(
                    pattern.get("begin").get(0).getId(),
                    pattern.get("begin").get(0).getTs(),
                    pattern.get("begin").get(0).getVc());
              }
            })
        .print();
    env.execute();
  }
}
