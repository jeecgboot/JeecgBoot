package com.shop.common.core.utils;

import javax.servlet.http.HttpServletRequest;

public class DeviceUtils {

    public static boolean isMobileDevice(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");

        // 判断是否为移动设备的关键字
        String[] mobileKeywords = {"Mobile", "Android", "iPhone", "iPad", "Windows Phone"};

        // 遍历关键字，判断User-Agent中是否包含
        for (String keyword : mobileKeywords) {
            if (userAgent.contains(keyword)) {
                return true;
            }
        }

        return false;
    }
}
