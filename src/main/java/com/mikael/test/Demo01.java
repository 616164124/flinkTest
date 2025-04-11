package com.mikael.test;


import org.apache.flink.api.java.tuple.Tuple3;

import java.time.Duration;

/**
 * @author
 * @version 1.0
 * @date 2025/4/7
 */

public class Demo01 {
    private final static long serialVersionUID = 1L;

    public static void main(String[] args) {


        System.out.println(Duration.ofSeconds(5L));
        new Tuple3("1L","1",12);
        "111".hashCode();
        System.out.println("111".hashCode());
        System.out.println(Long.MAX_VALUE);
        System.out.println("6961728318907695402L");
        Tuple3 tuple3 = new Tuple3();
    }
}
