package org.jeecg.modules.openapi.filter;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.openapi.entity.OpenApi;
import org.jeecg.modules.openapi.entity.OpenApiAuth;
import org.jeecg.modules.openapi.entity.OpenApiPermission;
import org.jeecg.modules.openapi.entity.OpenApiRecord;
import org.jeecg.modules.openapi.service.OpenApiAuthService;
import org.jeecg.modules.openapi.service.OpenApiPermissionService;
import org.jeecg.modules.openapi.service.OpenApiRecordService;
import org.jeecg.modules.openapi.service.OpenApiService;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @date 2024/12/19 16:55
 */
@Slf4j
public class ApiAuthFilter implements Filter {

    private OpenApiRecordService openApiRecordService;
    private OpenApiAuthService openApiAuthService;
    private OpenApiPermissionService openApiPermissionService;
    private OpenApiService openApiService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long startTime = System.currentTimeMillis();
        Date callTime = new Date();

        HttpServletRequest request = (HttpServletRequest)servletRequest;
        String ip = request.getRemoteAddr();

        String appkey = request.getHeader("appkey");
        String signature = request.getHeader("signature");
        String timestamp = request.getHeader("timestamp");

        OpenApi openApi = findOpenApi(request);

        // IP 黑名单核验
        checkBlackList(openApi, ip);

        // 签名核验
        checkSignValid(appkey, signature, timestamp);

        OpenApiAuth openApiAuth = openApiAuthService.getByAppkey(appkey);
        // 认证信息核验
        checkSignature(appkey, signature, timestamp, openApiAuth);
        // 业务核验
        checkPermission(openApi, openApiAuth);

        filterChain.doFilter(servletRequest, servletResponse);
        long endTime = System.currentTimeMillis();

        OpenApiRecord record = new OpenApiRecord();
        record.setApiId(openApi.getId());
        record.setCallAuthId(openApiAuth.getId());
        record.setCallTime(callTime);
        record.setUsedTime(endTime - startTime);
        record.setResponseTime(new Date());
        openApiRecordService.save(record);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        WebApplicationContext applicationContext = (WebApplicationContext)servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        this.openApiService = applicationContext.getBean(OpenApiService.class);
        this.openApiRecordService = applicationContext.getBean(OpenApiRecordService.class);
        this.openApiAuthService = applicationContext.getBean(OpenApiAuthService.class);
        this.openApiPermissionService = applicationContext.getBean(OpenApiPermissionService.class);
    }

    /**
     * IP 黑名单核验
     * @param openApi
     * @param ip
     */
    protected void checkBlackList(OpenApi openApi, String ip) {
        if (!StringUtils.hasText(openApi.getBlackList())) {
            return;
        }

        List<String> blackList = Arrays.asList(openApi.getBlackList().split(","));
        if (blackList.contains(ip)) {
            throw new JeecgBootException("目标接口限制IP[" + ip + "]进行访问，IP已记录，请停止访问");
        }
    }

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
