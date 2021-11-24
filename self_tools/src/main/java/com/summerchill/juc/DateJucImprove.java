package com.summerchill.juc;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateJucImprove {
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(new Task2()).start();
        }
    }
    //如上100个线程去解析"2019-12-31 12:59:59"字符串 运行报错.....
}


class Task2 implements Runnable {
    public static ThreadLocal<SimpleDateFormat> simpleDateFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    @Override
    public void run() {
        try {
            System.out.println(simpleDateFormat.get().parse("2019-12-31 12:59:59"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}