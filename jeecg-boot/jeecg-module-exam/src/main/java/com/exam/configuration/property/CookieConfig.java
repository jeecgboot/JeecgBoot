package com.exam.configuration.property;

/**
 * @version 3.5.0
 * @description: The type Cookie config.
 * Copyright (C), 2020-2024
 * @date 2021/12/25 9:45
 */
public class CookieConfig {

    /**
     * Gets name.
     *
     * @return the name
     */
    public static String getName() {
        return "xzs";
    }

    /**
     * Gets interval.
     *
     * @return the interval
     */
    public static Integer getInterval() {
        return 30 * 24 * 60 * 60;
    }
}
