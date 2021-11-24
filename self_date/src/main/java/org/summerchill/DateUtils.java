package org.summerchill;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * @author kxh
 * @description 工具类
 */
public class DateUtils {
    public static void main(String[] args) {
        String dateStr = "2021-06-08";
        String dateStr2 = "2021-06-20";
        //日期字符串转换成LocalDate
        LocalDate currentDate = LocalDate.parse(dateStr);
        //日期字符串转换成LocalDate
        System.out.println(string2LocalDate(dateStr, "yyyy-MM-dd"));


        //20210608230225
        System.out.println(getCurrentTimeStampSpecifyFormat("yyyyMMddHHmmss"));
        //202106082302
        System.out.println(getCurrentTimeStampSpecifyFormat("yyyyMMddHHmm"));
        //20210608
        System.out.println(getCurrentTimeStampSpecifyFormat("yyyyMMdd"));
        //2021-06-08
        System.out.println(getCurrentTimeStampSpecifyFormat("yyyy-MM-dd"));
        //1623164545054(精确到毫秒)
        System.out.println(getCurrentTimeStampMillSecond());
        //1623164545(精确到秒)
        System.out.println(getCurrentTimeStampSecond());

        //获取指定一天的开始的unix时间(毫秒)
        System.out.println(atStartOfDayUnixTime(string2Date("2021-06-09","yyyy-MM-dd")));
        //获取指定一天的结束的unix时间(毫秒)
        System.out.println(atEndOfDayUnixTime(string2Date("2021-06-09","yyyy-MM-dd")));
        //求指定日期间隔多少天之后的日期
        System.out.println(getSpecifyIntervalDate(string2LocalDate(dateStr, "yyyy-MM-dd"),1));
        //求两个unix时间戳(毫秒) 之间的时间间隔
        System.out.println(formatDuration(1623170745000l, 1623170782000l));

        //求两个日期的时间间隔
        System.out.println(getTwoDateInterval(LocalDate.parse(dateStr),LocalDate.parse(dateStr2)));
        //求指定一个范围内,按照指定的间隔需对应的次数
        System.out.println(roundUp("20","6"));

        //unix时间转换成指定的
        System.out.println(unixTime2DateTime(String.valueOf(System.currentTimeMillis()/1000),"yyyy-MM-dd HH:mm:ss"));
        System.out.println(unixTime2DateTime2(String.valueOf(System.currentTimeMillis()/1000),"yyyy-MM-dd HH:mm:ss"));
    }


    public static String unixTime2DateTime(String unixTime, String pattern) {
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        Date lastModifiedDatetime = Date.from(Instant.ofEpochSecond(Long.valueOf(unixTime)));
        return formatter.format(lastModifiedDatetime);
    }

    public static String unixTime2DateTime2(String unixTime, String pattern) {
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        //zoneId.of中用"GMT+8" 和 "UTC+8" 都可以 也可以使用 ZoneId.systemDefault() 和 "+08:00"
        //return Instant.ofEpochSecond(Long.parseLong(unixTime)).atZone(ZoneId.of(("+08:00"))).format(formatter);
        //return Instant.ofEpochSecond(Long.parseLong(unixTime)).atZone(ZoneId.systemDefault()).format(formatter);
        return Instant.ofEpochSecond(Long.parseLong(unixTime)).atZone(ZoneId.of(("UTC+8"))).format(formatter);
    }


    /**
     * 计算一串时间的平均时间点
     * 这里的时间的形式都是System.currentTimeMillis() 这种形式.
     * @param time_list
     * @return
     */
    public static String caclAveTime(List<Long> time_list) {

        Long total_minute = 0L;
        Long ave_hour = 0L;
        Long ave_minute = 0L;
        for (Long time : time_list) {
            LocalDateTime start_time = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), TimeZone.getDefault().toZoneId());
            Long hour = Long.valueOf(start_time.getHour());
            Long minute = Long.valueOf(start_time.getMinute());

            total_minute = total_minute + hour * 60 + minute;
        }
        ave_hour = total_minute/time_list.size()/60;
        ave_minute = total_minute/time_list.size()%60;

        //System.out.println(ave_hour + ":" + ave_minute);
        return ave_hour + ":" + ave_minute;
    }

    /**
     * 计算一串时间的平均时间
     * List中的时间也是具体到毫秒的
     * @param time_list
     * @return
     */
    public static String caclAvePeriod(List<Long> time_list) {
        int count = time_list.size();
        long sum = 0L;
        //time_list中的数值做加法操作.
        for (Long time : time_list) {
            sum = sum + time/1000/60;
        }
        return String.valueOf(sum/count);
    }

    /**
     * 例子:有20个数字,每次数6个,至少需要几次才能全部数完.
     * @param num
     * @param divisor
     * @return
     */
    public static int roundUp(String num, String divisor) {
        return (Integer.valueOf(num) + Integer.valueOf(divisor) - 1) / Integer.valueOf(divisor);
    }

    public static long getTwoDateInterval(LocalDate startDate, LocalDate currentDate){
        long interval = DAYS.between(startDate, currentDate);
        return interval;
    }

    public static Date string2Date(String dateStr,String pattern) {
        try {
            //String string = "January 2, 2010";
            DateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
            return format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static LocalDate string2LocalDate(String dateStr,String pattern){
        //String string = "January 2, 2010";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, Locale.CHINA);
        return LocalDate.parse(dateStr, formatter);
    }

    /**
     * 根据指定的时间格式(比如:"yyyyMMddHHmmss") 返回对应的时间戳字符串
     *
     * @param dateFormat
     * @return
     */
    public static String getCurrentTimeStampSpecifyFormat(String dateFormat) {
        return new SimpleDateFormat(dateFormat).format(new Date());
    }

    /**
     * 获取当前的时间戳(毫秒级)
     *
     * @return
     */
    public static String getCurrentTimeStampMillSecond() {
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * 获取当前的时间戳(秒级)
     *
     * @return
     */
    public static String getCurrentTimeStampSecond() {
        return String.valueOf(Instant.now().getEpochSecond());
    }

    /**
     * 获取指定一天的开始的unix时间(毫秒)
     *
     * @param date
     * @return
     */
    public static long atStartOfDayUnixTime (Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay).getTime();
    }

    /**
     * 获取指定一天的结束的unix时间(毫秒)
     *
     * @param date
     * @return
     */
    public static long atEndOfDayUnixTime (Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(endOfDay).getTime();
    }

    /**
     * Date类型转换成LocalDateTime类型
     * @param date
     * @return
     */
    private static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * LocalDateTime类型转换成Date类型
     * @param localDateTime
     * @return
     */
    private static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 传入LocalDate实例，转换成Date类型
     */
    public static Date LocalDateToDate(LocalDate localDate) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    /**
     * 获取到指定时间间隔的日期
     * @param currentDate
     * @param i
     * @return
     */
    public static String getSpecifyIntervalDate(LocalDate currentDate, long i) {
        return currentDate.plusDays(i).toString();
    }

    /**
     * 求两个unix时间(精确到毫秒) 之间的时间间隔
     * 以 **h **m **s 或者 **sec 表示
     * 支持endTime为-1,表示startTime距离现在的时间
     * @param startTime
     * @param endTime
     * @return
     */
    public static String formatDuration(long startTime, long endTime) {
        if (startTime == -1) {
            return "-";
        }

        long durationMS;
        if (endTime == -1) {
            durationMS = System.currentTimeMillis() - startTime;
        } else {
            durationMS = endTime - startTime;
        }

        long seconds = durationMS / 1000;
        if (seconds < 60) {
            return seconds + " sec";
        }

        long minutes = seconds / 60;
        seconds %= 60;
        if (minutes < 60) {
            return minutes + "m " + seconds + "s";
        }

        long hours = minutes / 60;
        minutes %= 60;
        if (hours < 24) {
            return hours + "h " + minutes + "m " + seconds + "s";
        }

        long days = hours / 24;
        hours %= 24;
        return days + "d " + hours + "h " + minutes + "m";
    }

}
