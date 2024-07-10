package org.springframework.base.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil extends DateUtils {
    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    private static final String[] parsePatterns = {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm"};

    public static String getDate() {
        return getDate("yyyy-MM-dd");
    }

    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    public static String formatDate(Date date, Object... pattern) {
        String formatDate = null;
        if ((pattern != null) && (pattern.length > 0)) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    public static String formatDateTime(Date date) {
        return DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getTime() {
        return DateFormatUtils.format(System.currentTimeMillis(), "HH:mm:ss");
    }

    public static String getDateTime() {
        return DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
    }

    public static String getDateTimeStringNotTime() {
        return DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd");
    }

    public static String getDateTimeString() {
        return DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss");
    }

    public static String getYear() {
        return DateFormatUtils.format(System.currentTimeMillis(), "yyyy");
    }

    public static String getMonth() {
        return DateFormatUtils.format(System.currentTimeMillis(), "MM");
    }

    public static String getDay() {
        return DateFormatUtils.format(System.currentTimeMillis(), "dd");
    }

    public static String getWeek() {
        return DateFormatUtils.format(System.currentTimeMillis(), "E");
    }

    public static Date parseDate(String str) {
        try {
            if (StringUtils.isNotBlank(str)) {
                return parseDate(str, parsePatterns);
            }
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static long pastDays(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / 86400000L;
    }

    public static Date getDateStart(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = sdf.parse(formatDate(date, "yyyy-MM-dd") + " 00:00:00");
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        return date;
    }

    public static Date getDateEnd(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = sdf.parse(formatDate(date, "yyyy-MM-dd") + " 23:59:59");
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        return date;
    }

    public static boolean isDate(String timeString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        try {
            format.parse(timeString);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
