package com.treesoft.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;

@Slf4j
public class DateUtil extends DateUtils {

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

    public static String getTime() {
        return DateFormatUtils.format(System.currentTimeMillis(), "HH:mm:ss");
    }

    public static String getDateTime() {
        return DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
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
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
