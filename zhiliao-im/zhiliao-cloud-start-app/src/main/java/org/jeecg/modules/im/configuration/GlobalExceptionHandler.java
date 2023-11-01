package org.jeecg.modules.im.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.im.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @Value("${spring.profiles.active}")
    private String evn;

    protected Boolean isTest() {
        return StringUtils.equals(evn.split(",")[evn.split(",").length-1],"test");
    }


    /**
     * 处理自定义的业务异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public Result bizExceptionHandler(HttpServletRequest req, BusinessException e) {
        log.error("发生业务异常！原因是：{}", e.getMessage());
        return Result.error(CommonConstant.SC_BIZ_ERROR,e.getMessage());
    }

    /**
     * 处理空指针的异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest req, NullPointerException e) {
        log.error("发生空指针异常！原因是:", e);
        if(isTest()){
            return Result.error(CommonConstant.SC_NULL_ERROR,e.getCause().getMessage());
        }
        return Result.error(CommonConstant.SC_NULL_ERROR,"null error");
    }


    /**
     * 处理其他异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest req, Exception e) {
        log.error("未知异常！原因是:", e);
        if(!isTest()){
            return Result.error(CommonConstant.SC_INTERNAL_SERVER_ERROR_500,"sys error");
        }
        if(e.getCause()!=null){
            return Result.error(CommonConstant.SC_INTERNAL_SERVER_ERROR_500,e.getCause().getMessage());
        }
        return Result.error(CommonConstant.SC_INTERNAL_SERVER_ERROR_500,e.getMessage());
    }
}