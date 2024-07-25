package com.shop.common.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * JSON解析工具类
 * 2017-06-10 10:10
 */
public class JSONUtil {

    /**
     * 获取code
     */
    public static int getCode(String json) {
        return getIntValue(json, "code");
    }

    /**
     * 获取message
     */
    public static String getMessage(String json) {
        return getString(json, "msg");
    }

    /**
     * 得到String类型值
     */
    public static String getString(String json, String key) {
        String result = null;
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            result = jsonObject.getString(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 得到int类型的值
     */
    public static int getIntValue(String json, String key) {
        int result = 0;
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            result = jsonObject.getIntValue(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 得到boolean类型的值
     */
    public static boolean getBooleanValue(String json, String key) {
        boolean result = false;
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            result = jsonObject.getBooleanValue(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 得到long类型的值
     */
    public static long getLongValue(String json, String key) {
        long result = 0;
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            result = jsonObject.getLongValue(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 得到double类型的值
     */
    public static double getDoubleValue(String json, String key) {
        double result = 0;
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            result = jsonObject.getDoubleValue(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 得到float类型的值
     */
    public static float getFloatValue(String json, String key) {
        float result = 0;
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            result = jsonObject.getFloatValue(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 得到byte类型的值
     */
    public static byte getByteValue(String json, String key) {
        byte result = 0;
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            result = jsonObject.getByteValue(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 得到short类型的值
     */
    public static short getShortValue(String json, String key) {
        short result = 0;
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            result = jsonObject.getShortValue(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 得到Integer类型的值
     */
    public static Integer getInteger(String json, String key) {
        Integer result = null;
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            result = jsonObject.getInteger(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 得到Boolean类型的值
     */
    public static Boolean getBoolean(String json, String key) {
        Boolean result = null;
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            result = jsonObject.getBoolean(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 得到Long类型的值
     */
    public static Long getLong(String json, String key) {
        Long result = null;
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            result = jsonObject.getLong(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 得到Double类型的值
     */
    public static Double getDouble(String json, String key) {
        Double result = null;
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            result = jsonObject.getDouble(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 得到Float类型的值
     */
    public static Float getFloat(String json, String key) {
        Float result = null;
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            result = jsonObject.getFloat(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 得到Byte类型的值
     */
    public static Byte getByte(String json, String key) {
        Byte result = null;
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            result = jsonObject.getByte(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 得到Short类型的值
     */
    public static Short getShort(String json, String key) {
        Short result = null;
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            result = jsonObject.getShort(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 得到byte[]类型的值
     */
    public static byte[] getBytes(String json, String key) {
        byte[] result = null;
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            result = jsonObject.getBytes(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 得到BigInteger类型的值
     */
    public static BigInteger getBigInteger(String json, String key) {
        BigInteger result = null;
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            result = jsonObject.getBigInteger(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 得到BigDecimal类型的值
     */
    public static BigDecimal getBigDecimal(String json, String key) {
        BigDecimal result = null;
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            result = jsonObject.getBigDecimal(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 得到Date类型的值
     *
     * @param json
     * @param key
     * @return
     */
    public static Date getDate(String json, String key) {
        Date result = null;
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            result = jsonObject.getDate(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 得到对象类型的值
     */
    public static <T> T getObject(String json, String key, Class<T> clazz) {
        T result = null;
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            result = jsonObject.getObject(key, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 得到对象类型的集合
     *
     * @param json
     * @param key
     * @param clazz
     * @return
     */
    public static <T> List<T> getArray(String json, String key, Class<T> clazz) {
        List<T> result = null;
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            String string = jsonObject.getString(key);
            result = JSON.parseArray(string, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result == null) {
            result = new ArrayList<>();
        }
        return result;
    }

    /**
     * json转换换成对象
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        T result = null;
        try {
            result = JSON.parseObject(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * json转换换成集合
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        List<T> result = null;
        try {
            result = JSON.parseArray(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result == null) {
            result = new ArrayList<>();
        }
        return result;
    }

}
