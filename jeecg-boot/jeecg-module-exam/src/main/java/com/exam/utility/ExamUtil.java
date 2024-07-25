package com.exam.utility;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @version 3.5.0
 * @description: The type Exam util.
 * Copyright (C), 2020-2024
 * @date 2021/12/25 9:45
 */
public class ExamUtil {

    /**
     * Score to vm string.
     *
     * @param score the score
     * @return the string
     */
    public static String scoreToVM(Integer score) {
        if (score % 10 == 0) {
            return String.valueOf(score / 10);
        } else {
            return String.format("%.1f", score / 10.0);
        }
    }

    /**
     * Score from vm integer.
     *
     * @param score the score
     * @return the integer
     */
    public static Integer scoreFromVM(String score) {
        if (score == null) {
            return null;
        } else {
            return (int) (Float.parseFloat(score) * 10);
        }
    }

    /**
     * Second to vm string.
     *
     * @param second the second
     * @return the string
     */
    public static String secondToVM(Integer second) {
        String dateTimes;
        long days = second / (60 * 60 * 24);
        long hours = (second % (60 * 60 * 24)) / (60 * 60);
        long minutes = (second % (60 * 60)) / 60;
        long seconds = second % 60;
        if (days > 0) {
            dateTimes = days + "天 " + hours + "时 " + minutes + "分 " + seconds + "秒";
        } else if (hours > 0) {
            dateTimes = hours + "时 " + minutes + "分 " + seconds + "秒";
        } else if (minutes > 0) {
            dateTimes = minutes + "分 " + seconds + "秒";
        } else {
            dateTimes = seconds + " 秒";
        }
        return dateTimes;
    }

    private static final String ANSWER_SPLIT = ",";

    /**
     * Content to string string.
     *
     * @param contentArray the content array
     * @return the string
     */
    public static String contentToString(List<String> contentArray) {
        return contentArray.stream().sorted().collect(Collectors.joining(ANSWER_SPLIT));
    }


    /**
     * Content to array list.
     *
     * @param contentArray the content array
     * @return the list
     */
    public static List<String> contentToArray(String contentArray) {
        return Arrays.asList(contentArray.split(ANSWER_SPLIT));
    }

    private static final String FORM_ANSWER_SPLIT = "_";

    /**
     * Last num integer.
     *
     * @param str the str
     * @return the integer
     */
    public static Integer lastNum(String str) {
        Integer start = str.lastIndexOf(FORM_ANSWER_SPLIT);
        return Integer.parseInt(str.substring(start + 1));
    }
}
