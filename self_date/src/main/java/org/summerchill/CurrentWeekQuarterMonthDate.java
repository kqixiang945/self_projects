package org.summerchill;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;

/**
 * java中计算基于某个时间的工具类
 */
public class CurrentWeekQuarterMonthDate {
    public static void main(String[] args) {
        String dateStr = "2021-06-08";
        //日期字符串转换成LocalDate
        LocalDate currentDate = LocalDate.parse(dateStr);
        //当前日期增加一个月
        currentDate.plusMonths(1);
        //当前日期增加一个月
        currentDate.minusMonths(1);
        //当前日期增加一个季度
        currentDate.plus(1, IsoFields.QUARTER_YEARS);
        //当前日期减少一个季度
        currentDate.minus(1, IsoFields.QUARTER_YEARS);
        //当前日期增加一个星期
        currentDate.plusWeeks(1);
        //当前日期减少一个星期
        currentDate.minusWeeks(1);

        //获得当前时间所在月的月初和月末
        getCurrentMonthBeginEnd(currentDate, "BEGIN");
        getCurrentMonthBeginEnd(currentDate, "END");
        //获得当前时间所在的季度第一天和最后一天
        getCurrentQuarterBeginEnd(currentDate, "BEGIN");
        getCurrentQuarterBeginEnd(currentDate, "END");
        //获得当前时间所在星期的第一天和最后一天
        getCurrentWeekBeginEnd(currentDate, "BEGIN");
        getCurrentWeekBeginEnd(currentDate, "END");

    }
    //获得月末 月初 (本次,上次,下次)
    private static String getCurrentMonthBeginEnd(LocalDate currentDate, String type) {
        if ("BEGIN".equalsIgnoreCase(type)) {
            LocalDate current_month_begin = currentDate.withDayOfMonth(1);
            return current_month_begin.toString();
        } else {
            LocalDate current_month_end = currentDate.plusMonths(1).withDayOfMonth(1).minusDays(1);
            return current_month_end.toString();
        }
    }

    //获得季末 季初 (本次,上次,下次)
    private static String getCurrentQuarterBeginEnd(LocalDate currentDate, String type) {
        if ("BEGIN".equalsIgnoreCase(type)) {
            LocalDate current_quarter_begin = currentDate.with(currentDate.getMonth().firstMonthOfQuarter())
                    .with(TemporalAdjusters.firstDayOfMonth());
            return current_quarter_begin.toString();
        } else {
            LocalDate current_quarter_begin_current = currentDate.with(currentDate.getMonth().firstMonthOfQuarter())
                    .with(TemporalAdjusters.firstDayOfMonth());
            LocalDate current_quarter_end = current_quarter_begin_current.plusMonths(2)
                    .with(TemporalAdjusters.lastDayOfMonth());
            return current_quarter_end.toString();
        }
    }

    //获得周末 周初 (本次,上次,下次  )
    private static String getCurrentWeekBeginEnd(LocalDate currentDate, String type) {
        if ("BEGIN".equalsIgnoreCase(type)) {
            LocalDate monday = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            return monday.toString();
        } else {
            LocalDate sunday = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
            return sunday.toString();
        }
    }
}
