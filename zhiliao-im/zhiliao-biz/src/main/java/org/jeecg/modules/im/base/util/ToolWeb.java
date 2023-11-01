package org.jeecg.modules.im.base.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.im.base.tools.ToolDateTime;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * WEB工具类
 */
@Slf4j
public abstract class ToolWeb {

    private static boolean checkIP(String ip) {
        if (ip == null || ip.length() == 0 || "unkown".equalsIgnoreCase(ip)
                || ip.split(".").length != 4) {
            return false;
        }
        return true;
    }

    /**
     * * 获取服务器IP地址
     * * @return
     */
    public static String getServerIp() {
//        try {
//            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
//            while (networkInterfaces.hasMoreElements()) {
//                NetworkInterface ni = (NetworkInterface) networkInterfaces.nextElement();
//                Enumeration<InetAddress> nias = ni.getInetAddresses();
//                while (nias.hasMoreElements()) {
//                    InetAddress ia = (InetAddress) nias.nextElement();
//                    if (!ia.isLinkLocalAddress() && !ia.isLoopbackAddress() && ia instanceof Inet4Address) {
//                        return ia.getHostAddress();
//                    }
//                }
//            }
//        } catch (SocketException e) {
//        }
        Map map = ToolAddress.getIp();
        return map==null?null:(String)map.get(ToolAddress.IP);
    }

    /**
     * 获取项目根目录
     *
     * @param request
     * @return
     */
    public static String getServletContextPath(HttpServletRequest request) {
        return request.getServletContext().getRealPath("/");
    }

    public static String getHost(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        if (request.getServerPort() != 80) {
            sb.append(request.getScheme()).append("://").append(request.getServerName()).append(":").append(request.getServerPort());
        } else {
            sb.append(request.getScheme()).append("://").append(request.getServerName());
        }
        return sb.toString();
    }
    /**
     * 获取上下文URL全路径
     *
     * @param request
     * @return
     */
    public static String getContextPath(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        if (request.getServerPort() != 80) {
            sb.append(request.getScheme()).append("://").append(request.getServerName()).append(":").append(request.getServerPort()).append(request.getContextPath());
        } else {
            sb.append(request.getScheme()).append("://").append(request.getServerName()).append(request.getContextPath());
        }
        return sb.toString();
    }

    /**
     * 获取完整请求路径(含内容路径及请求参数)
     *
     * @param request
     * @return
     */
    public static String getRequestURIWithParam(HttpServletRequest request) {
        return request.getRequestURI() + (request.getQueryString() == null ? "" : "?" + request.getQueryString());
    }

    /**
     * 获取请求的完整地址
     *
     * @param request
     * @return
     */
    public static String getRequestURL(HttpServletRequest request) {
        String param = request.getQueryString();
        String href = "http://" + request.getServerName() //服务器地址
                + (request.getServerPort() == 80 ? "" : ":" + request.getServerPort())         //端口号
                + request.getContextPath()      //项目名称
                + request.getServletPath();     //请求页面或其他地址
        href += StringUtils.isBlank(param) ? "" : "?" + param;//参数
        return href;
    }

    /**
     * 请求的路径，不带参数
     * @param request
     * @return
     */
    public static String getActionKey(HttpServletRequest request) {
        return request.getContextPath() + request.getServletPath();
    }

    /**
     * 发送请求的页面地址
     */
    public static String getReferer(HttpServletRequest request) {
        return request.getHeader("Referer");
    }

    /**
     * 判断是否是异步请求
     */
    public static Boolean isAsync(HttpServletRequest request) {
        if ((StringUtils.isNoneBlank(request.getHeader("accept")) && request.getHeader("accept").indexOf("application/json") > -1 ||
                (request.getHeader("X-Requested-With") != null &&
                        request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))) {
            return true;
        }
        return false;
    }

    /**
     * 获取请求参数
     *
     * @param request
     * @param name
     * @return
     */
    public static String getParam(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (null != value && !value.isEmpty()) {
            try {
                return URLDecoder.decode(value, ToolString.encoding).trim();
            } catch (UnsupportedEncodingException e) {
                log.error("decode异常：" + value);
                return value;
            }
        }
        return value;
    }

    /**
     * 获取ParameterMap
     *
     * @param request
     * @return
     */
    public static Map<String, String> getParamMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<String> enume = request.getParameterNames();
        while (enume.hasMoreElements()) {
            String name = (String) enume.nextElement();
            map.put(name, request.getParameter(name));
        }
        return map;
    }

    /**
     * 输出servlet文本内容
     *
     * @param response
     * @param content
     * @param contentType
     * @author junko
     */
    public static void outPage(HttpServletResponse response, String content, String contentType) {
        try {
            outPage(response, content.getBytes(ToolString.encoding), contentType); // char to byte 性能提升
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 输出servlet文本内容
     *
     * @param response
     * @param content
     * @param contentType
     * @author junko
     */
    public static void outPage(HttpServletResponse response, byte[] content, String contentType) {
        if (contentType == null || contentType.isEmpty()) {
            contentType = "text/html; charset=UTF-8";
        }
        response.setContentType(contentType);
        response.setCharacterEncoding(ToolString.encoding);
        // PrintWriter out = response.getWriter();
        // out.print(content);
        try {
            response.getOutputStream().write(content);// char to byte 性能提升
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 输出CSV文件下载
     *
     * @param response
     * @param content  CSV内容
     * @author junko
     */
    public static void outDownCsv(HttpServletResponse response, String content) {
        response.setContentType("application/download; charset=gb18030");
        try {
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(ToolDateTime.format(ToolDateTime.getDate(), ToolDateTime.pattern_ymd_hms_s) + ".csv", ToolString.encoding));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        // PrintWriter out = response.getWriter();
        // out.write(content);
        try {
            response.getOutputStream().write(content.getBytes(ToolString.encoding));
        } catch (IOException e) {
            e.printStackTrace();
        }// char to byte 性能提升
        // out.flush();
        // out.close();
    }

    /**
     * 请求流转字符串
     *
     * @param request
     * @return
     */
    public static String requestStream(HttpServletRequest request) {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            request.setCharacterEncoding(ToolString.encoding);
            inputStream = (ServletInputStream) request.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, ToolString.encoding);
            bufferedReader = new BufferedReader(inputStreamReader);
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            log.error("request.getInputStream() to String 异常", e);
            return null;
        } finally { // 释放资源
            if (null != bufferedReader) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    log.error("bufferedReader.close()异常", e);
                }
                bufferedReader = null;
            }

            if (null != inputStreamReader) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    log.error("inputStreamReader.close()异常", e);
                }
                inputStreamReader = null;
            }

            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("inputStream.close()异常", e);
                }
                inputStream = null;
            }
        }
    }

    /**
     * 添加cookie
     * @param request		HttpServletRequest
     * @param response		HttpServletResponse
     * @param domain		设置cookie所在域
     * @param path			设置cookie所在路径
     * @param isHttpOnly	是否只读
     * @param name			cookie的名称
     * @param value			cookie的值
     * @param maxAge		cookie存放的时间(以秒为单位,假如存放三天,即3*24*60*60; 如果值为0,cookie将随浏览器关闭而清除)
     */
    public static void addCookie(HttpServletRequest request, HttpServletResponse response,
                                 String domain, String path, boolean isHttpOnly,
                                 String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);

        // 设置cookie所在路径
        if(StringUtils.isEmpty(path)){
            path = request.getContextPath();
        }
        if(StringUtils.isEmpty(path)){
            path = "/";
        }
        cookie.setPath(path);

        // 所在域：比如a1.4bu4.com 和 a2.4bu4.com 共享cookie
        if(StringUtils.isNotEmpty(domain)){
            cookie.setDomain(domain);
        }

        // 是否只读
        try {
            cookie.setHttpOnly(isHttpOnly);
        } catch (Exception e) {
            if(log.isErrorEnabled()) log.error("servlet容器版本太低，servlet3.0以前不支持设置cookie只读" + e.getMessage());
        }

        // https模式传递此cookie，否则忽略此cookie
        String scheme = request.getScheme();
        if(scheme.equals("https")){
            cookie.setSecure(true);
        }

        // 设置cookie的过期时间
        if (maxAge > 0){
            cookie.setMaxAge(maxAge);
        }
        response.addCookie(cookie);
    }

    /**
     * 获取cookie的值
     *
     * @param request
     * @param name
     *            cookie的名称
     * @return
     */
    public static String getCookieValueByName(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = ToolWeb.readCookieMap(request);
        // 判断cookie集合中是否有我们像要的cookie对象 如果有返回它的值
        if (cookieMap.containsKey(name)) {
            Cookie cookie = (Cookie) cookieMap.get(name);
            return cookie.getValue();
        } else {
            return null;
        }
    }

    /**
     * 获得cookie
     *
     * @param request
     * @param name
     * @return
     */
    public static Cookie getCookieByName(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = ToolWeb.readCookieMap(request);
        // 判断cookie集合中是否有我们像要的cookie对象 如果有返回它的值
        if (cookieMap.containsKey(name)) {
            Cookie cookie = (Cookie) cookieMap.get(name);
            return cookie;
        } else {
            return null;
        }
    }

    /**
     * 获得所有cookie
     *
     * @param request
     * @return
     */
    public static Map<String, Cookie> readCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
        // 从request范围中得到cookie数组 然后遍历放入map集合中
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (int i = 0; i < cookies.length; i++) {
                cookieMap.put(cookies[i].getName(), cookies[i]);
            }
        }
        return cookieMap;
    }

//    /**
//     * 效验Referer有效性
//     *
//     * @author junko
//     * @return
//     */
//    public static boolean authReferer(HttpServletRequest request) {
//        String referer = request.getHeader("Referer");
//        if (StringUtils.isNotEmpty(referer)) {
//            referer = referer.toLowerCase();
//            String domainStr = Constant.config_domain_key;
//            String[] domainArr = domainStr.split(",");
//            for (String domain : domainArr) {
//                if (referer.startsWith(domain.trim())) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }


}
