package com.mikael.funcation;

import com.mikael.bean.WaterSensor;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.util.Collector;

/**
 * @author
 * @version 1.0
 * @date 2025/4/7
 */

public class MyMapFuncation implements MapFunction<WaterSensor, WaterSensor>, FlatMapFunction<WaterSensor, String> {
    @Override
    public WaterSensor map(WaterSensor value) throws Exception {
        return value;
    }

    @Override
    public void flatMap(WaterSensor value, Collector<String> out) throws Exception {
        if(value.getId().equals("D")){
            out.collect(value.getId());
        }
    }
}
