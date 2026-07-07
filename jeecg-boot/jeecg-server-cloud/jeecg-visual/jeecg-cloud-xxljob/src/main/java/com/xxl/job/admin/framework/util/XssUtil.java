package com.xxl.job.admin.framework.util;

import java.util.regex.Pattern;

public class XssUtil {

    /**
     * 定义常见的 XSS 攻击正则模式
     *
     * 1. <script> 标签及其变体
     * 2. javascript: vbscript: 等伪协议
     * 3. 常见的事件处理器 (onload, onclick, onerror 等)
     * 4. eval(), expression() 等危险函数
     */
    private static final Pattern[] PATTERNS = {
            Pattern.compile("<(no)?script[^>]*>.*?</(no)?script>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("on(load|error|click|mouseover|focus|blur|submit|change|input)\\s*=", Pattern.CASE_INSENSITIVE),
            Pattern.compile("eval\\s*\\(", Pattern.CASE_INSENSITIVE),
            Pattern.compile("expression\\s*\\(", Pattern.CASE_INSENSITIVE),
            Pattern.compile("url\\s*\\(", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<iframe[^>]*>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<object[^>]*>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<embed[^>]*>", Pattern.CASE_INSENSITIVE)
    };

    /**
     * check XSS attack patterns in a string
     *
     * @param value original string
     * @return true if XSS attack detected, false otherwise
     */
    public static boolean hasXss(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }

        // 遍历所有正则模式进行匹配
        for (Pattern pattern : PATTERNS) {
            if (pattern.matcher(value).find()) {
                return true;
            }
        }

        return false;
    }

    /**
     * 清洗字符串，移除潜在的 XSS 攻击代码
     *
     * @param value original string
     * @return cleaned string
     */
    public static String cleanXss(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }

        String result = value;

        // 1. 移除危险标签和脚本
        for (Pattern pattern : PATTERNS) {
            result = pattern.matcher(result).replaceAll("");
        }

        // 2. 特殊字符 HTML 实体转义
        result = escapeHtml(result);

        return result;
    }

    /**
     * 基础 HTML 字符转义
     */
    private static String escapeHtml(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }

    /*public static void main(String[] args) {
        String safeInput = "Hello World";
        String xssInput1 = "<script>alert('XSS')</script>";
        String xssInput2 = "<img src=x onerror=alert(1)>";
        String xssInput3 = "javascript:alert(1)";

        System.out.println("Safe Input Has XSS: " + hasXss(safeInput)); // false
        System.out.println("XSS Input 1 Has XSS: " + hasXss(xssInput1)); // true
        System.out.println("XSS Input 2 Has XSS: " + hasXss(xssInput2)); // true
        System.out.println("XSS Input 3 Has XSS: " + hasXss(xssInput3)); // true

        System.out.println("Cleaned XSS 1: " + cleanXss(xssInput1)); // alert('XSS') (script tags removed, content escaped if needed)
        System.out.println("Cleaned XSS 2: " + cleanXss(xssInput2)); // (img tag and event handler removed)
    }*/

}
