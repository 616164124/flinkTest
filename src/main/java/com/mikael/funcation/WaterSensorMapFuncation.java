package com.mikael.funcation;

import com.mikael.bean.WaterSensor;
import org.apache.flink.api.common.functions.MapFunction;

/**
 * @author
 * @version 1.0
 * @date 2025/4/7
 */

public class WaterSensorMapFuncation implements MapFunction<String, WaterSensor> {
    @Override
    public WaterSensor map(String value) throws Exception {
        String[] split = value.split(",");
        return  new WaterSensor(split[0],Long.parseLong(split[1]),Integer.parseInt(split[2]));
    }
}
