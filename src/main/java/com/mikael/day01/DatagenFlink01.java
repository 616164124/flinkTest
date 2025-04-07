package com.mikael.day01;

import com.mikael.bean.WaterSensor;
import com.mikael.funcation.MyMapFuncation;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.connector.source.util.ratelimit.RateLimiterStrategy;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.connector.datagen.source.DataGeneratorSource;
import org.apache.flink.connector.datagen.source.GeneratorFunction;
import org.apache.flink.shaded.netty4.io.netty.handler.codec.http2.Http2FrameWriter;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.datagen.DataGenerator;

/**
 * @author
 * @version 1.0
 * @date 2025/4/7
 */

public class DatagenFlink01 {
    public static void main(String[] args) throws Exception {

//        Configuration configuration = new Configuration();
//        configuration.set(RestOptions.BIND_PORT, "8083");

        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment(new Configuration());
        env.setParallelism(3);

        DataGeneratorSource<WaterSensor> generatorSource = new DataGeneratorSource<>(new GeneratorFunction<Long, WaterSensor>() {
            @Override
            public WaterSensor map(Long aLong) throws Exception {
                return new WaterSensor(aLong + "", aLong, Integer.parseInt(aLong + ""));
            }
        }, 10, RateLimiterStrategy.perSecond(10), TypeInformation.of(WaterSensor.class));


        env.fromSource(generatorSource, WatermarkStrategy.noWatermarks(), "datagen").flatMap(new MyMapFuncation()).print();

        env.execute();

    }
}
