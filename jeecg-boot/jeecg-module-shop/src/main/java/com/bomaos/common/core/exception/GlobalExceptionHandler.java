package com.bomaos.common.core.exception;

import com.alibaba.fastjson.JSON;
import com.bomaos.common.core.Constants;
import com.bomaos.common.core.web.JsonResult;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 全局异常处理器
 * Created by Panyoujie on 2018-02-22 11:29
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class.getName());

    @ExceptionHandler(Exception.class)
    public String errorHandler(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        // 对不同错误进行不同处理
        if (ex instanceof IException) {
            return doHandler("error/500.html", ((IException) ex).getCode(), ex.getMessage(), ex.toString(), request, response);
        } else if (ex instanceof UnauthorizedException) {
            return doHandler("error/403.html", 403, "没有访问权限", ex.toString(), request, response);
        }
        logger.error(ex.getMessage(), ex);
        return doHandler("error/500.html", Constants.RESULT_ERROR_CODE, "系统错误", ex.toString(), request, response);
    }

    /**
     * 处理错误,ajax返回json，非ajax跳转页面
     */
    private String doHandler(String url, Integer code, String msg, String error, HttpServletRequest request, HttpServletResponse response) {
        if (isAjax(request)) {
            cross(response);  // 支持跨域
            response.setContentType("application/json;charset=utf-8");
            try {
                PrintWriter out = response.getWriter();
                out.write(JSON.toJSONString(JsonResult.error(code, msg).put("error", error)));
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        return url;
    }

    /**
     * 判断是不是ajax请求
     */
    private boolean isAjax(HttpServletRequest request) {
        String xHeader = request.getHeader("X-Requested-With");
        return (xHeader != null && xHeader.contains("XMLHttpRequest"));
    }

    /**
     * 支持跨域请求
     */
    private void cross(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with, X-Custom-Header, Authorization");
    }

}
