package com.shop.common.core.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 日期类型转换器
 * Created by Panyoujie on 2018-08-17 08:43
 */
@Component
public class DateConverterConfig implements Converter<String, Date> {
    private static final List<String> formats = new ArrayList<>();

    /*
     * 以下几种时间格式自动转成Date类型
     */
    static {
        formats.add("yyyy-MM-dd");
        formats.add("yyyy-MM-dd HH:mm");
        formats.add("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public Date convert(String s) {
        if (s == null || s.trim().isEmpty()) {
            return null;
        } else if (s.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            return parseDate(s, formats.get(0));
        } else if (s.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
            return parseDate(s, formats.get(1));
        } else if (s.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return parseDate(s, formats.get(2));
        } else {
            throw new IllegalArgumentException("DateConverterConfig: Invalid date value '" + s + "'");
        }
    }

    private Date parseDate(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
