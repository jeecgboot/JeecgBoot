package org.jeecg.config.sign.interceptor;


import com.alibaba.fastjson.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.sign.util.BodyReaderHttpServletRequestWrapper;
import org.jeecg.config.sign.util.HttpUtils;
import org.jeecg.config.sign.util.SignUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;
import java.util.SortedMap;

/**
 * 签名拦截器
 * @author qinfeng
 */
@Slf4j
public class SignAuthInterceptor implements HandlerInterceptor {
    /**
     * 5分钟有效期
     */
    private final static long MAX_EXPIRE = 5 * 60;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("签名拦截器 Interceptor request URI = " + request.getRequestURI());
        
        try {
            // 调用验证逻辑
            validateSignature(request);
            return true;
        } catch (IllegalArgumentException e) {
            // 验证失败，返回错误响应
            log.error("Sign 签名校验失败！{}", e.getMessage());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter out = response.getWriter();
            Result<?> result = Result.error(e.getMessage());
            out.print(JSON.toJSON(result));
            return false;
        }
    }
    
    /**
     * 签名验证核心逻辑
     * 提取出来供AOP切面复用
     * @param request HTTP请求
     * @throws IllegalArgumentException 验证失败时抛出异常
     */
    public void validateSignature(HttpServletRequest request) throws IllegalArgumentException {
        try {
            log.debug("开始签名验证: {} {}", request.getMethod(), request.getRequestURI());
            
            HttpServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
            //获取全部参数(包括URL和body上的)
            SortedMap<String, String> allParams = HttpUtils.getAllParams(requestWrapper);
            log.debug("提取参数: {}", allParams);
            
            //对参数进行签名验证
            String headerSign = request.getHeader(CommonConstant.X_SIGN);
            String xTimestamp = request.getHeader(CommonConstant.X_TIMESTAMP);
            
            if(oConvertUtils.isEmpty(xTimestamp)){
                log.error("Sign签名校验失败，时间戳为空！");
                throw new IllegalArgumentException("Sign签名校验失败，请求参数不完整！");
            }

            //客户端时间
            Long clientTimestamp = Long.parseLong(xTimestamp);

            int length = 14;
            int length1000 = 1000;
            //1.校验签名时间（兼容X_TIMESTAMP的新老格式）
            if (xTimestamp.length() == length) {
                //a. X_TIMESTAMP格式是 yyyyMMddHHmmss (例子：20220308152143)
                long currentTimestamp = DateUtils.getCurrentTimestamp();
                long timeDiff = currentTimestamp - clientTimestamp;
                log.debug("时间戳验证(yyyyMMddHHmmss): 时间差{}秒", timeDiff);
                
                if (timeDiff > MAX_EXPIRE) {
                    log.error("时间戳已过期: {}秒 > {}秒", timeDiff, MAX_EXPIRE);
                    throw new IllegalArgumentException("签名验证失败，请求时效性验证失败！");
                }
            } else {
                //b. X_TIMESTAMP格式是 时间戳 (例子：1646552406000)
                long currentTime = System.currentTimeMillis();
                long timeDiff = currentTime - clientTimestamp;
                long maxExpireMs = MAX_EXPIRE * length1000;
                log.debug("时间戳验证(Unix): 时间差{}ms", timeDiff);
                
                if (timeDiff > maxExpireMs) {
                    log.error("时间戳已过期: {}ms > {}ms", timeDiff, maxExpireMs);
                    throw new IllegalArgumentException("签名验证失败，请求时效性验证失败！");
                }
            }

            //2.校验签名
            boolean isSigned = SignUtil.verifySign(allParams,headerSign);

            if (isSigned) {
                log.debug("签名验证通过");
            } else {
                log.error("签名验证失败, 参数: {}", allParams);
                throw new IllegalArgumentException("Sign签名校验失败！");
            }
        } catch (IllegalArgumentException e) {
            // 重新抛出签名验证异常
            throw e;
        } catch (Exception e) {
            // 包装其他异常（如IOException）
            log.error("签名验证异常: {}", e.getMessage());
            throw new IllegalArgumentException("Sign签名校验失败：" + e.getMessage());
        }
    }

}
