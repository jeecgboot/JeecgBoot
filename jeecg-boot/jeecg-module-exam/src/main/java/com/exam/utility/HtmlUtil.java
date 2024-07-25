package com.exam.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @version 3.5.0
 * @description: The type Html util.
 * Copyright (C), 2020-2024
 * @date 2021/12/25 9:45
 */
public class HtmlUtil {
    /**
     * Clear string.
     *
     * @param htmlStr the html str
     * @return the string
     */
    public static String clear(String htmlStr) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>";
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>";
        String regEx_html = "<[^>]+>";
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll("");
        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll("");
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll("");
        return htmlStr.trim();
    }
}
