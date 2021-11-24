package com.summerchill.juc;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateJuc {
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(new Task(simpleDateFormat)).start();
        }
    }
    //如上100个线程去解析"2019-12-31 12:59:59"字符串 运行报错.....
}


class Task implements Runnable {
    SimpleDateFormat simpleDateFormat;

    public Task(SimpleDateFormat simpleDateFormat) {
        this.simpleDateFormat = simpleDateFormat;
    }

    @Override
    public void run() {
        try {
            simpleDateFormat.parse("2019-12-31 12:59:59");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}