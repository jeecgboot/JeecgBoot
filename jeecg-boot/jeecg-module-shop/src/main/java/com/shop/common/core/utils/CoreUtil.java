package com.shop.common.core.utils;

import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常用工具方法
 * 2017-6-10 10:10
 */
public class CoreUtil {
    private static final String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    /**
     * toString，为null返回空白字符
     */
    public static String toString(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    /**
     * 连接多个字符串，null自动过滤
     */
    public static String connect(Object... objects) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : objects) sb.append(toString(obj));
        return sb.toString();
    }

    /**
     * 首字母大写
     */
    public static String upperHead(String str) {
        if (str == null || str.length() == 0) return str;
        if (str.length() == 1) return str.toUpperCase();
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 生成8位uuid
     */
    public static String randomUUID8() {
        StringBuffer buffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            buffer.append(chars[x % 0x3E]);
        }
        return buffer.toString();
    }

    /**
     * 生成16位uuid
     */
    public static String randomUUID16() {
        StringBuffer buffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 16; i++) {
            String str = uuid.substring(i * 2, i * 2 + 2);
            int x = Integer.parseInt(str, 16);
            buffer.append(chars[x % 0x3E]);
        }
        return buffer.toString();
    }

    /**
     * 把对象转成Map
     */
    public static <T> Map<String, Object> objectToMap(T t) {
        return objectToMap(t, null);
    }

    /**
     * 把对象集合转成Map集合
     */
    public static <T> List<Map<String, Object>> objectToMap(List<T> ts) {
        return objectToMap(ts, null);
    }

    /**
     * 把对象转成Map，只包含指定字段
     *
     * @param t      对象
     * @param fields 包含的字段
     * @return Map
     */
    public static <T> Map<String, Object> objectToMap(T t, String[] fields) {
        if (t == null) return null;
        List<String> fieldList = null;
        if (fields != null) fieldList = Arrays.asList(fields);
        Map<String, Object> map = new HashMap<>();
        Field[] fieldArray = t.getClass().getDeclaredFields();
        for (Field field : fieldArray) {
            field.setAccessible(true);
            if (fieldList == null || fieldList.contains(field.getName())) {
                try {
                    map.put(field.getName(), field.get(t));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    /**
     * 把对个对象集合转成Map集合，只包含指定字段
     *
     * @param ts     对象集合
     * @param fields 包含的字段
     * @return List<Map>
     */
    public static <T> List<Map<String, Object>> objectToMap(List<T> ts, String[] fields) {
        List<Map<String, Object>> rs = new ArrayList<>();
        for (T t : ts) {
            Map<String, Object> map = objectToMap(t, fields);
            if (map != null) rs.add(map);
        }
        return rs;
    }

    /**
     * 复制父类的属性的值到子类
     *
     * @param f 父类对象
     * @param c 子类对象
     */
    public static <F, C extends F> void copyAttribute(F f, C c) {
        for (Field field : f.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Field cf = c.getClass().getField(field.getName());
                cf.setAccessible(true);
                cf.set(c, field.get(f));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 把父类克隆为子类
     *
     * @param father 父类对象
     * @param clazz  子类类型
     */
    public static <F, C extends F> C cloneToChild(F father, Class<C> clazz) {
        try {
            C child = clazz.newInstance();
            copyAttribute(father, child);
            return child;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断excel某列是否有空值
     */
    public static String excelCheckBlank(List<List<Object>> list, int startRow, int... cols) {
        StringBuilder sb = new StringBuilder();
        for (int col : cols) {
            for (int i = 0; i < list.size(); i++) {
                Object value = list.get(i).get(col);
                if (value == null || StrUtil.isBlank(value.toString())) {
                    if (sb.length() != 0) sb.append("\r\n");
                    sb.append("第").append(i + startRow + 1).append("行第");
                    sb.append(col + 1).append("列不能为空.");
                }
            }
        }
        return sb.toString();
    }

    /**
     * 判断excel某列是否有重复值
     */
    public static String excelCheckRepeat(List<List<Object>> list, int startRow, int... cols) {
        StringBuilder sb = new StringBuilder();
        for (int col : cols) {
            for (int i = 0; i < list.size(); i++) {
                Object value = list.get(i).get(col);
                for (int j = 0; j < list.size(); j++) {
                    if (i != j && value != null && value.equals(list.get(j).get(col))) {
                        if (sb.length() != 0) sb.append("\r\n");
                        sb.append("第").append(i + startRow + 1).append("行第").append(col + 1).append("列与第");
                        sb.append(j + startRow + 1).append("行第").append(col + 1).append("列重复.");
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * 检查list集合中元素字段是否有重复
     *
     * @param list  集合
     * @param field 字段名称
     * @return 返回重复的元素
     */
    public static <T> T listCheckRepeat(List<T> list, String field) {
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (i != j) {
                    Object value = getFieldValue(list.get(i), field);
                    if (value != null && value.equals(getFieldValue(list.get(j), field))) {
                        return list.get(j);
                    }
                }
            }
        }
        return null;
    }

    /**
     * 检查list集合中元素字段是否有重复
     *
     * @param list  集合
     * @param field 字段名称
     * @param field 字段中文名称，用于错误提示
     * @return 返回错误提示信息
     */
    public static <T> String listCheckRepeat(List<T> list, String field, String zhName) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (i != j) {
                    Object value = getFieldValue(list.get(i), field);
                    if (value != null && value.equals(getFieldValue(list.get(j), field))) {
                        if (sb.length() != 0) sb.append("\r\n");
                        sb.append("第").append(i + 1).append("条与第").append(j + 1).append("条数据的").append(zhName).append("重复.");
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * 检查list集合中元素字段是否有空值
     *
     * @param list  集合
     * @param field 字段名称
     * @return 返回为空的元素
     */
    public static <T> T listCheckBlank(List<T> list, String field) {
        for (int i = 0; i < list.size(); i++) {
            Object value = getFieldValue(list.get(i), field);
            if (value == null || StrUtil.isBlank(value.toString())) {
                return list.get(i);
            }
        }
        return null;
    }

    /**
     * 检查list集合中元素字段是否有空值
     *
     * @param list  集合
     * @param field 字段名称
     * @param field 字段中文名称，用于错误提示
     * @return 返回错误提示信息
     */
    public static <T> String listCheckBlank(List<T> list, String field, String zhName) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            Object value = getFieldValue(list.get(i), field);
            if (value == null || StrUtil.isBlank(value.toString())) {
                if (sb.length() != 0) sb.append("\r\n");
                sb.append("第").append(i + 1).append("条数据的").append(zhName).append("不能为空.");
            }
        }
        return sb.toString();
    }

    /**
     * 获取某个对象的某个字段的值
     */
    public static Object getFieldValue(Object t, String field) {
        if (t == null || field == null) return null;
        try {
            Field clazzField = t.getClass().getDeclaredField(field);
            clazzField.setAccessible(true);
            return clazzField.get(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 去除回车和空格
     *
     * @param str
     * @return
     */
    public static String getStringNoBlank(String str) {
        if (str != null && !"".equals(str)) {
            str.replaceAll("\n", "");
            Pattern p = Pattern.compile("(^\\s*)|(\\s*$)");
            Matcher m = p.matcher(str);
            String strNoBlank = m.replaceAll("");
            return strNoBlank;
        } else {
            return "";
        }
    }

}
