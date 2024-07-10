package org.springframework.base.system.utils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil extends StringUtils {

    public static String replaceHtml(String html) {
        if (isBlank(html)) {
            return "";
        } else {
            String regEx = "<.+?>";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(html);
            String s = m.replaceAll("");
            return s;
        }
    }

    public static String abbr(String str, int length) {
        if (str == null) {
            return "";
        } else {
            try {
                StringBuilder sb = new StringBuilder();
                int currentLength = 0;
                char[] var7;
                int var6 = (var7 = replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray()).length;

                for (int var5 = 0; var5 < var6; ++var5) {
                    char c = var7[var5];
                    currentLength += String.valueOf(c).getBytes("GBK").length;
                    if (currentLength > length - 3) {
                        sb.append("...");
                        break;
                    }

                    sb.append(c);
                }

                return sb.toString();
            } catch (UnsupportedEncodingException var8) {
                var8.printStackTrace();
                return "";
            }
        }
    }

    public static Double toDouble(Object val) {
        if (val == null) {
            return 0.0;
        } else {
            try {
                return Double.valueOf(trim(val.toString()));
            } catch (Exception var2) {
                return 0.0;
            }
        }
    }

    public static Long toLong(Object val) {
        return toDouble(val).longValue();
    }

    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }
}
