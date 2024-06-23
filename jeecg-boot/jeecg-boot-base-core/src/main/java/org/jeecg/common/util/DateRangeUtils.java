package org.jeecg.common.util;

import cn.hutool.core.date.DateUtil;
import org.jeecg.common.constant.enums.DateRangeEnum;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期范围工具类
 *
 * @author scott
 * @date 20230801
 */
public class DateRangeUtils {

    /**
     * 根据日期范围枚举获取日期范围
     *
     * @param rangeEnum
     * @return Date[]
     */
    public static Date[] getDateRangeByEnum(DateRangeEnum rangeEnum) {
        if (rangeEnum == null) {
            return null;
        }
        Date[] ranges = new Date[2];
        switch (rangeEnum) {
            case TODAY:
                ranges[0] = getTodayStartTime();
                ranges[1] = getTodayEndTime();
                break;
            case YESTERDAY:
                ranges[0] = getYesterdayStartTime();
                ranges[1] = getYesterdayEndTime();
                break;
            case TOMORROW:
                ranges[0] = getTomorrowStartTime();
                ranges[1] = getTomorrowEndTime();
                break;
            case THIS_WEEK:
                ranges[0] = getThisWeekStartDay();
                ranges[1] = getThisWeekEndDay();
                break;
            case LAST_WEEK:
                ranges[0] = getLastWeekStartDay();
                ranges[1] = getLastWeekEndDay();
                break;
            case NEXT_WEEK:
                ranges[0] = getNextWeekStartDay();
                ranges[1] = getNextWeekEndDay();
                break;
            case LAST_7_DAYS:
                ranges[0] = getLast7DaysStartTime();
                ranges[1] = getLast7DaysEndTime();
                break;
            case THIS_MONTH:
                ranges[0] = getThisMonthStartDay();
                ranges[1] = getThisMonthEndDay();
                break;
            case LAST_MONTH:
                ranges[0] = getLastMonthStartDay();
                ranges[1] = getLastMonthEndDay();
                break;
            case NEXT_MONTH:
                ranges[0] = getNextMonthStartDay();
                ranges[1] = getNextMonthEndDay();
                break;
            default:
                return null;
        }
        return ranges;
    }

    /**
     * 获得下月第一天 周日 00:00:00
     */
    public static Date getNextMonthStartDay() {
        return DateUtil.beginOfMonth(DateUtil.nextMonth());
    }

    /**
     * 获得下月最后一天 23:59:59
     */
    public static Date getNextMonthEndDay() {
        return DateUtil.endOfMonth(DateUtil.nextMonth());
    }

    /**
     * 获得本月第一天 周日 00:00:00
     */
    public static Date getThisMonthStartDay() {
        return DateUtil.beginOfMonth(DateUtil.date());
    }

    /**
     * 获得本月最后一天 23:59:59
     */
    public static Date getThisMonthEndDay() {
        return DateUtil.endOfMonth(DateUtil.date());
    }

    /**
     * 获得上月第一天 周日 00:00:00
     */
    public static Date getLastMonthStartDay() {
        return DateUtil.beginOfMonth(DateUtil.lastMonth());
    }

    /**
     * 获得上月最后一天 23:59:59
     */
    public static Date getLastMonthEndDay() {
        return DateUtil.endOfMonth(DateUtil.lastMonth());
    }

    /**
     * 获得上周第一天 周一 00:00:00
     */
    public static Date getLastWeekStartDay() {
        return DateUtil.beginOfWeek(DateUtil.lastWeek());
    }

    /**
     * 获得上周最后一天 周日 23:59:59
     */
    public static Date getLastWeekEndDay() {
        return DateUtil.endOfWeek(DateUtil.lastWeek());
    }

    /**
     * 获得本周第一天 周一 00:00:00
     */
    public static Date getThisWeekStartDay() {
        Date today = new Date();
        return DateUtil.beginOfWeek(today);
    }

    /**
     * 获得本周最后一天 周日 23:59:59
     */
    public static Date getThisWeekEndDay() {
        Date today = new Date();
        return DateUtil.endOfWeek(today);
    }

    /**
     * 获得下周第一天 周一 00:00:00
     */
    public static Date getNextWeekStartDay() {
        return DateUtil.beginOfWeek(DateUtil.nextWeek());
    }

    /**
     * 获得下周最后一天 周日 23:59:59
     */
    public static Date getNextWeekEndDay() {
        return DateUtil.endOfWeek(DateUtil.nextWeek());
    }

    /**
     * 过去七天开始时间（不含今天）
     *
     * @return
     */
    public static Date getLast7DaysStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -7);
        return DateUtil.beginOfDay(calendar.getTime());
    }

    /**
     * 过去七天结束时间（不含今天）
     *
     * @return
     */
    public static Date getLast7DaysEndTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getLast7DaysStartTime());
        calendar.add(Calendar.DATE, 6);
        return DateUtil.endOfDay(calendar.getTime());
    }

    /**
     * 昨天开始时间
     *
     * @return
     */
    public static Date getYesterdayStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -1);
        return DateUtil.beginOfDay(calendar.getTime());
    }

    /**
     * 昨天结束时间
     *
     * @return
     */
    public static Date getYesterdayEndTime() {
        return DateUtil.endOfDay(getYesterdayStartTime());
    }

    /**
     * 明天开始时间
     *
     * @return
     */
    public static Date getTomorrowStartTime() {
        return DateUtil.beginOfDay(DateUtil.tomorrow());
    }

    /**
     * 明天结束时间
     *
     * @return
     */
    public static Date getTomorrowEndTime() {
        return DateUtil.endOfDay(DateUtil.tomorrow());
    }

    /**
     * 今天开始时间
     *
     * @return
     */
    public static Date getTodayStartTime() {
        return DateUtil.beginOfDay(new Date());
    }

    /**
     * 今天结束时间
     *
     * @return
     */
    public static Date getTodayEndTime() {
        return DateUtil.endOfDay(new Date());
    }

}
