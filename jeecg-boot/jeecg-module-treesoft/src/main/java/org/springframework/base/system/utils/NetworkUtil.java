package org.springframework.base.system.utils;

import javax.servlet.http.HttpServletRequest;

public class NetworkUtil {
    public static final String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
            if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip)))  {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip)))  {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip)))  {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip)))  {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip)))  {
                ip = request.getRemoteAddr();
            }
        }
        else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++)  {
                String strIp = ips[index];
                if (!"unknown".equalsIgnoreCase(strIp)) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }
}
