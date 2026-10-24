package org.jeecg.modules.openapi.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.IpUtils;
import org.jeecg.modules.openapi.entity.OpenApi;
import org.jeecg.modules.openapi.entity.OpenApiAuth;
import org.jeecg.modules.openapi.entity.OpenApiLog;
import org.jeecg.modules.openapi.entity.OpenApiPermission;
import org.jeecg.modules.openapi.service.OpenApiAuthService;
import org.jeecg.modules.openapi.service.OpenApiLogService;
import org.jeecg.modules.openapi.service.OpenApiPermissionService;
import org.jeecg.modules.openapi.service.OpenApiService;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @date 2024/12/19 16:55
 */
@Slf4j
public class ApiAuthFilter implements Filter {

    private OpenApiLogService openApiLogService;
    private OpenApiAuthService openApiAuthService;
    private OpenApiPermissionService openApiPermissionService;
    private OpenApiService openApiService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long startTime = System.currentTimeMillis();
        Date callTime = new Date();

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //update-begin---author:jeecg---for：OPTIONS 预检只应答 CORS，不得进入后续业务链路，否则可绕过签名/AK/授权校验---
        // OPTIONS 预检请求：仅回写 CORS 响应头并结束，绝不放行到 filterChain（防止 OPTIONS 绕过 OpenApi 签名鉴权）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            String origin = request.getHeader("Origin");
            if (StringUtils.hasText(origin)) {
                response.setHeader("Access-Control-Allow-Origin", origin);
                response.setHeader("Access-Control-Allow-Credentials", "true");
                response.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,PATCH,OPTIONS");
                String reqHeaders = request.getHeader("Access-Control-Request-Headers");
                if (StringUtils.hasText(reqHeaders)) {
                    response.setHeader("Access-Control-Allow-Headers", reqHeaders);
                }
            }
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        //update-end---author:jeecg---for：OPTIONS 预检只应答 CORS，不得进入后续业务链路---

        String ip = IpUtils.getIpAddr(request);

        String appkey = request.getHeader("appkey");
        String signature = request.getHeader("signature");
        String timestamp = request.getHeader("timestamp");
        String requestPath = request.getRequestURI();
        String requestMethod = request.getMethod();
        String requestParams = buildParamsSummary(request);
        //update-begin---author:jeecg ---date:2026-05-27  for：【增加header日志记录】采集请求Header信息-----------
        String requestHeaders = buildHeadersSummary(request);
        //update-end---author:jeecg ---date:2026-05-27  for：【增加header日志记录】采集请求Header信息-----------

        OpenApi openApi = null;
        OpenApiAuth openApiAuth = null;
        int responseCode = HttpServletResponse.SC_OK;
        String errorMsg = null;

        try {
            openApi = findOpenApi(request);
            // IP 白名单核验
            checkWhiteList(openApi, ip);
            // 签名核验
            checkSignValid(appkey, signature, timestamp);
            openApiAuth = openApiAuthService.getByAppkey(appkey);
            // 认证信息核验
            checkSignature(appkey, signature, timestamp, openApiAuth);
            // 业务核验
            checkPermission(openApi, openApiAuth);

            filterChain.doFilter(servletRequest, servletResponse);
            responseCode = response.getStatus();
        } catch (JeecgBootException e) {
            responseCode = HttpServletResponse.SC_UNAUTHORIZED;
            errorMsg = e.getMessage();
            writeErrorResponse(response, request.getHeader("Origin"), responseCode, errorMsg);
        } catch (Exception e) {
            responseCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            errorMsg = e.getMessage();
            writeErrorResponse(response, request.getHeader("Origin"), responseCode, "服务器内部错误");
        } finally {
            long endTime = System.currentTimeMillis();
            //update-begin---author:jeecg ---date:2026-05-27  for：【增加header日志记录】传入requestHeaders参数-----------
            saveLog(openApi, openApiAuth, appkey, requestPath, requestMethod, ip, requestParams,
                    requestHeaders, responseCode, errorMsg, callTime, endTime - startTime);
            //update-end---author:jeecg ---date:2026-05-27  for：【增加header日志记录】传入requestHeaders参数-----------
        }
    }

    /**
     * 保存调用记录
     * @param openApi
     * @param openApiAuth
     * @param callerAk
     * @param requestPath
     * @param requestMethod
     * @param sourceIp
     * @param requestParams
     * @param responseCode
     * @param errorMsg
     * @param callTime
     * @param usedTime
     */
    //update-begin---author:jeecg ---date:2026-05-27  for：【增加header日志记录】saveLog方法新增requestHeaders参数-----------
    private void saveLog(OpenApi openApi, OpenApiAuth openApiAuth, String callerAk,
                         String requestPath, String requestMethod, String sourceIp,
                         String requestParams, String requestHeaders, int responseCode, String errorMsg,
                         Date callTime, long usedTime) {
        try {
            OpenApiLog openApiLog = new OpenApiLog();
            openApiLog.setApiId(openApi != null ? openApi.getId() : null);
            openApiLog.setCallAuthId(openApiAuth != null ? openApiAuth.getId() : null);
            openApiLog.setCallerAk(callerAk);
            openApiLog.setRequestPath(requestPath);
            openApiLog.setRequestMethod(requestMethod);
            openApiLog.setIp(sourceIp);
            openApiLog.setRequestParams(requestParams);
            openApiLog.setRequestHeaders(requestHeaders);
            openApiLog.setResponseCode(responseCode);
            openApiLog.setErrorMsg(errorMsg);
            openApiLog.setCallTime(callTime);
            openApiLog.setUsedTime(usedTime);
            openApiLog.setResponseTime(new Date());
            openApiLogService.save(openApiLog);
        } catch (Exception e) {
            log.error("保存OpenAPI调用日志失败:{}",e.getMessage(), e);
        }
    }
    //update-end---author:jeecg ---date:2026-05-27  for：【增加header日志记录】saveLog方法新增requestHeaders参数-----------

    /**
     * 向响应写入 JSON 错误体，并携带 CORS 头（确保跨域调用方如 Swagger UI 能读取错误信息）
     */
    private void writeErrorResponse(HttpServletResponse response, String origin, int status, String message) {
        try {
            if (StringUtils.hasText(origin)) {
                response.setHeader("Access-Control-Allow-Origin", origin);
                response.setHeader("Access-Control-Allow-Credentials", "true");
            }
            response.setStatus(status);
            response.setContentType("application/json;charset=UTF-8");
            // 转义双引号，防止 message 中含有引号破坏 JSON
            String safeMsg = message != null ? message.replace("\"", "\\\"") : "";
            response.getWriter().write("{\"success\":false,\"code\":" + status + ",\"message\":\"" + safeMsg + "\"}");
        } catch (IOException ex) {
            log.error("写入OpenAPI错误响应失败", ex);
        }
    }

    private String buildParamsSummary(HttpServletRequest request) {
        String queryString = request.getQueryString();
        if (StringUtils.hasText(queryString)) {
            return queryString.length() > 500 ? queryString.substring(0, 500) + "..." : queryString;
        }
        return null;
    }

    //update-begin---author:jeecg ---date:2026-05-27  for：【增加header日志记录】构建请求Header摘要（JSON格式，最多2000字符）-----------
    private String buildHeadersSummary(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder("{");
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            boolean first = true;
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String value = request.getHeader(name);
                if (!first) {
                    sb.append(", ");
                }
                sb.append("\"").append(name).append("\": \"")
                  .append(value != null ? value.replace("\\", "\\\\").replace("\"", "\\\"") : "")
                  .append("\"");
                first = false;
            }
        }
        sb.append("}");
        String result = sb.toString();
        return result.length() > 2000 ? result.substring(0, 2000) + "...}" : result;
    }
    //update-end---author:jeecg ---date:2026-05-27  for：【增加header日志记录】构建请求Header摘要（JSON格式，最多2000字符）-----------

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        WebApplicationContext applicationContext = (WebApplicationContext)servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        this.openApiService = applicationContext.getBean(OpenApiService.class);
        this.openApiLogService = applicationContext.getBean(OpenApiLogService.class);
        this.openApiAuthService = applicationContext.getBean(OpenApiAuthService.class);
        this.openApiPermissionService = applicationContext.getBean(OpenApiPermissionService.class);
    }

    //update-begin---author:scott ---date:20260416  for：【PR/9083】OpenAPI白名单增强，支持CIDR网段和通配符匹配-----------
    /**
     * IP 白名单核验，支持精确IP、CIDR网段（如192.168.1.0/24）、通配符（如10.2.3.*）
     * @param openApi
     * @param ip
     */
    protected void checkWhiteList(OpenApi openApi, String ip) {
        if (!StringUtils.hasText(openApi.getWhiteList())) {
            return;
        }

        List<String> whiteList = Arrays.stream(openApi.getWhiteList().split("[,\\n]"))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());

        for (String item : whiteList) {
            if (isIpMatch(ip, item)) {
                return;
            }
        }
        throw new JeecgBootException("IP[" + ip + "]不在白名单中，禁止访问");
    }

    /**
     * IP匹配：支持精确匹配、CIDR网段匹配、通配符匹配
     * @param ip 客户端IP
     * @param pattern 白名单条目（IP/CIDR/通配符）
     * @return 是否匹配
     */
    private boolean isIpMatch(String ip, String pattern) {
        if (!ip.contains(".") || !pattern.contains(".")) {
            return ip.equals(pattern);
        }
        if (pattern.contains("/")) {
            return isCidrMatch(ip, pattern);
        }
        if (pattern.contains("*")) {
            return isWildcardMatch(ip, pattern);
        }
        return ip.equals(pattern);
    }

    /**
     * CIDR网段匹配（仅IPv4），如 192.168.1.0/24
     */
    private boolean isCidrMatch(String ip, String cidr) {
        String[] parts = cidr.split("/");
        if (parts.length != 2) {
            return false;
        }
        try {
            long ipLong = ipToLong(ip);
            long cidrLong = ipToLong(parts[0]);
            int prefixLength = Integer.parseInt(parts[1]);
            if (prefixLength < 0 || prefixLength > 32) {
                return false;
            }
            long mask = prefixLength == 0 ? 0 : (-1L << (32 - prefixLength));
            return (ipLong & mask) == (cidrLong & mask);
        } catch (Exception e) {
            log.warn("CIDR匹配解析失败: cidr={}, ip={}", cidr, ip);
            return false;
        }
    }

    /**
     * 通配符匹配，如 10.2.3.*
     */
    private boolean isWildcardMatch(String ip, String pattern) {
        String[] ipParts = ip.split("\\.");
        String[] patternParts = pattern.split("\\.");
        if (ipParts.length != 4 || patternParts.length != 4) {
            return false;
        }
        for (int i = 0; i < 4; i++) {
            if ("*".equals(patternParts[i])) {
                continue;
            }
            if (!ipParts[i].equals(patternParts[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * IPv4地址转long
     */
    private long ipToLong(String ip) {
        String[] parts = ip.split("\\.");
        if (parts.length != 4) {
            throw new IllegalArgumentException("非法IPv4地址: " + ip);
        }
        long result = 0;
        for (int i = 0; i < 4; i++) {
            result = (result << 8) | (Integer.parseInt(parts[i]) & 0xFF);
        }
        return result;
    }
    //update-end---author:scott ---date:20260416  for：【PR/9083】OpenAPI白名单增强，支持CIDR网段和通配符匹配-----------

    /**
     * 签名验证
     * @param appkey
     * @param signature
     * @param timestamp
     * @return
     */
    protected void checkSignValid(String appkey, String signature, String timestamp) {
        if (!StringUtils.hasText(appkey)) {
            throw new JeecgBootException("appkey为空");
        }
        if (!StringUtils.hasText(signature)) {
            throw new JeecgBootException("signature为空");
        }
        if (!StringUtils.hasText(timestamp)) {
            throw new JeecgBootException("timastamp时间戳为空");
        }
        if (!timestamp.matches("[0-9]*")) {
            throw new JeecgBootException("timastamp时间戳不合法");
        }
        if (System.currentTimeMillis() - Long.parseLong(timestamp) > 5 * 60 * 1000) {
            throw new JeecgBootException("signature签名已过期(超过五分钟)");
        }
    }

    /**
     * 认证信息核验
     * @param appKey
     * @param signature
     * @param timestamp
     * @param openApiAuth
     * @return
     * @throws Exception
     */
    protected void checkSignature(String appKey, String signature, String timestamp, OpenApiAuth openApiAuth) {
        if(openApiAuth==null){
            throw new JeecgBootException("不存在认证信息");
        }

        //update-begin---author:scott ---date:20261014  for：SK为空时签名退化为md5(appKey+timestamp)，知道AK即可伪造，空SK一律拒绝-----------
        if (!StringUtils.hasText(openApiAuth.getSk())) {
            throw new JeecgBootException("该凭据未配置SK，请在【OpenAPI-授权管理】重新生成AK/SK");
        }
        //update-end---author:scott ---date:20261014  for：SK为空时签名退化为md5(appKey+timestamp)，知道AK即可伪造，空SK一律拒绝-----------

        if(!appKey.equals(openApiAuth.getAk())){
            throw new JeecgBootException("appkey错误");
        }

        if (!signature.equals(md5(appKey + openApiAuth.getSk() + timestamp))) {
            throw new JeecgBootException("signature签名错误");
        }
    }

    protected void checkPermission(OpenApi openApi, OpenApiAuth openApiAuth) {
        List<OpenApiPermission> permissionList = openApiPermissionService.findByAuthId(openApiAuth.getId());

        boolean hasPermission = false;
        for (OpenApiPermission permission : permissionList) {
            if (permission.getApiId().equals(openApi.getId())) {
                hasPermission = true;
                break;
            }
        }

        if (!hasPermission) {
            throw new JeecgBootException("该appKey未授权当前接口");
        }
    }

    /**
     * @return String    返回类型
     * @Title: MD5
     * @Description: 【MD5加密】
     */
    protected static String md5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes("utf-8"));
            byte[] hash = md.digest();
            int i;
            StringBuffer buf = new StringBuffer(32);
            for (int offset = 0; offset < hash.length; offset++) {
                i = hash[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (Exception e) {
            log.error("sign签名错误", e);
        }
        return result;
    }

    protected OpenApi findOpenApi(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String path = uri.substring(uri.lastIndexOf("/") + 1);
        return openApiService.findByPath(path);
    }

    public static void main(String[] args) {
        long timestamp = System.currentTimeMillis();
        System.out.println("timestamp:"  + timestamp);
        System.out.println("signature:" + md5("ak-eAU25mrMxhtaZsyS" + "rjxMqB6YyUXpSHAz4DCIz8vZ5aozQQiV" + timestamp));
    }
}
