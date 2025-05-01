package com.mikael.test;

import com.mikael.bean.WaterSensor;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

/**
 * @author
 * @version 1.0
 * @date 2025/4/7
 */
public class Demo01 {
  private static final long serialVersionUID = 1L;

  public static void main(String[] args) throws JsonProcessingException {

    System.out.println(Duration.ofSeconds(5L));
    new Tuple3("1L", "1", 12);
    "111".hashCode();
    System.out.println("111".hashCode());
    System.out.println(Long.MAX_VALUE);
    System.out.println("6961728318907695402L");
    Tuple3 tuple3 = new Tuple3();
    ObjectMapper mapper = new ObjectMapper();
    WaterSensor sensor = new WaterSensor("3d", 2L, 3);
    String s = mapper.writeValueAsString(sensor);
    System.out.println(s);
    WaterSensor sensor1 = mapper.readValue(s, WaterSensor.class);
    System.out.println(sensor1.getId());

    System.out.println(new SimpleDateFormat("yyyy-mm-dd HH:MM:SS").format(new Date().getTime()));
  }
}
