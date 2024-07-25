package com.shop.common.core.utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

/**
 * 格式校验工具类
 * 2018-12-14 8:38
 */
public class FormCheckUtil {

    /**
     * 密码是否符合格式(5-12位非空白字符)
     */
    public static boolean isPassword(String password) {
        return test(password, "^[\\S]{5,12}$");
    }

    /**
     * 两个对象是否相等
     */
    public static boolean equals(Object obj1, Object obj2) {
        return Objects.equals(obj1, obj2);
    }

    /**
     * 是否是手机号
     */
    public static boolean isPhone(String phone) {
        return test(phone, "^1\\d{10}$");
    }

    /**
     * 是否是邮箱
     */
    public static boolean isEmail(String email) {
        return test(email, "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$");
    }

    /**
     * 是否是网址
     */
    public static boolean isUrl(String url) {
        return test(url, "(^#)|(^http(s*):\\/\\/[^\\s]+\\.[^\\s]+)");
    }

    /**
     * 是否是数字
     */
    public static boolean isNumber(String number) {
        return test(number, "^1\\d{10}$");
    }

    /**
     * 是否是日期
     */
    public static boolean isDate(String date) {
        return test(date, "^(\\d{4})[-\\/](\\d{1}|0\\d{1}|1[0-2])([-\\/](\\d{1}|0\\d{1}|[1-2][0-9]|3[0-1]))*$");
    }

    /**
     * 是否是身份证
     */
    public static boolean isIdentity(String identity) {
        return test(identity, "(^\\d{15}$)|(^\\d{17}(x|X|\\d)$)");
    }

    /**
     * 是否是整数
     */
    public static boolean isDigits(String str) {
        return test(str, "^-?\\d+$");
    }

    /**
     * 是否是正整数
     */
    public static boolean isDigitsP(String str) {
        return test(str, "^[1-9]\\d*$");
    }

    /**
     * 是否是负整数
     */
    public static boolean isDigitsN(String str) {
        return test(str, "^-[1-9]\\d*$");
    }

    /**
     * 是否是非负整数(正整数或0)
     */
    public static boolean isDigitsPZ(String str) {
        return test(str, "^\\d+$");
    }

    /**
     * 是否是非正整数(负整数或0)
     */
    public static boolean isDigitsNZ(String str) {
        return test(str, "^-[1-9]\\d*|0");
    }

    /**
     * 验证最大长度、最小长度
     */
    public static boolean maxMinLength(String str, Integer maxLength, Integer minLength) {
        if (maxLength != null && (str == null || str.length() > maxLength)) {
            return false;
        }
        return !(minLength != null && (str == null || str.length() < minLength));
    }

    /**
     * 验证最大值、最小值
     */
    public static boolean maxMin(Integer value, Integer max, Integer min) {
        if (max != null && value != null && value > max) {
            return false;
        }
        return !(min != null && value != null && value < min);
    }

    /**
     * 字符串是否匹配正则表达式
     */
    public static boolean test(String str, String reg) {
        return str != null && str.matches(reg);
    }

    /**
     * 是否是身份证(强校验)
     */
    public static String isIdentityStrong(String identity) {
        if (!isIdentity(identity)) {
            return "身份证号码格式错误";
        }
        String ai;
        if (identity.length() == 18) {
            ai = identity.substring(0, 17);
        } else {
            ai = identity.substring(0, 6) + "19" + identity.substring(6, 15);
        }
        // 验证出生年月
        String year = ai.substring(6, 10);  // 年
        String birthday = year + "-" + ai.substring(10, 12) + "-" + ai.substring(12, 14);
        if (!isDate(birthday)) {
            return "身份证号码出生日期无效";
        }
        try {
            long time = new SimpleDateFormat("yyyy-MM-dd").parse(birthday).getTime();
            GregorianCalendar gc = new GregorianCalendar();
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(year)) > 150 || (gc.getTime().getTime() - time) < 0) {
                return "身份证号码出生日期不在有效范围";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "身份证号码校验失败";
        }
        // 验证地区码
        String[] areaCodes = new String[]{"11", "12", "13", "14", "15", "21", "22", "23",
                "31", "32", "33", "34", "35", "36", "37", "41", "42", "43", "44", "45", "46",
                "50", "51", "52", "53", "54", "61", "62", "63", "64", "65", "71", "81", "82", "91"};
        if (!Arrays.asList(areaCodes).contains(ai.substring(0, 2))) {
            return "身份证号码地区编码错误";
        }
        // 验证最后一位
        if (identity.length() == 18) {
            String[] valCode = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
            String[] wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};
            int totalMulAiWi = 0;
            for (int i = 0; i < 17; i++) {
                totalMulAiWi += Integer.parseInt(String.valueOf(ai.charAt(i))) * Integer.parseInt(wi[i]);
            }
            ai += valCode[totalMulAiWi % 11];
            if (!ai.equals(identity)) {
                return "身份证号码最后一位错误";
            }
        }
        return null;
    }

    /**
     * 值是否在给定值内
     */
    public static boolean isIn(Object value, Object... values) {
        if (value != null) {
            for (Object obj : values) {
                if (value.equals(obj)) {
                    return true;
                }
            }
        }
        return false;
    }

}
