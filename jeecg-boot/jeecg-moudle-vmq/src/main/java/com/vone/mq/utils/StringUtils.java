package com.vone.mq.utils;

import com.google.common.collect.Maps;
import org.springframework.cglib.beans.BeanMap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

/**
 * @author Exrickx
 */
public class StringUtils {

    private static SimpleDateFormat dateFormat =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 格式化 日期 yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String getTimeStamp(Date date){
        if(date == null){
            return dateFormat.format(new Date());
        } else {
            return dateFormat.format(date);
        }
    }

    /**
     * 格式化 日期 yyyy-MM-dd HH:mm:ss
     * @param time
     * @return
     */
    public static Date getDate(String time){

        try {
            return dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * 判断字符创是否为空
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    /**
     * 判断字符创是否为空
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * Bean 转 Map
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = Maps.newHashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key+"", beanMap.get(key));
            }
        }
        return map;
    }

    /**
     * 随机4位数生成
     */
    public static String getRandomNum() {

        Random random = new Random();
        int end2 = random.nextInt(9999);
        //如果不足两位前面补0
        String str = String.format("%04d", end2);
        return str;
    }

    public static String format(long time, String format) {
        SimpleDateFormat dateFormat =  new SimpleDateFormat(format);
        return dateFormat.format(time);
    }

    public static String format(Date time, String format) {
        SimpleDateFormat dateFormat =  new SimpleDateFormat(format);
        return dateFormat.format(time);
    }
}
