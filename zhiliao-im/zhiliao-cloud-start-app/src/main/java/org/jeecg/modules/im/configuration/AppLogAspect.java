package org.jeecg.modules.im.configuration;

import org.jeecg.modules.im.base.constant.ConstantWeb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.jeecg.modules.im.base.tools.ToolDateTime;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * 拦截请求，打印参数等相关信息
 */
@Aspect
@Component
public class AppLogAspect {
    public static Logger logger = LogManager.getLogger(AppLogAspect.class);
    private static int maxOutputLengthOfParaValue = 512;
    private static final String title = "\n -------- action report -------- ";

    @Pointcut("execution(public * org.jeecg.modules.im.controller..*.*(..))")
    public void appLog() {
    }


    @Before("appLog()")
    public void doBefore(JoinPoint joinPoint) {
        StringBuilder sb = new StringBuilder(title).append(ToolDateTime.getDate(ToolDateTime.pattern_ymd_hms)).append(" -------------------------------\n");

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        sb.append("Url    : ").append(request.getMethod()).append(" ").append(request.getRequestURL().toString()).append("\n");
        sb.append("Ip     : ").append("Client[").append(request.getHeader(ConstantWeb.IP)).append("]\n");
        sb.append("Ctrl   : ").append(joinPoint.getSignature().getDeclaringTypeName()).append(".java").append("\n");
        sb.append("Method : ").append(joinPoint.getSignature().getName()).append("\n");
        sb.append("    ↘ : ").append(Arrays.toString(joinPoint.getArgs())).append("\n");
        Enumeration<String> e = request.getParameterNames();
        if (e.hasMoreElements()) {
            sb.append("Params : ");
            while (e.hasMoreElements()) {
                String name = e.nextElement();
                String[] values = request.getParameterValues(name);
                if (values.length == 1) {
                    sb.append(name).append("=");
                    if (values[0] != null && values[0].length() > maxOutputLengthOfParaValue) {
                        sb.append(values[0], 0, maxOutputLengthOfParaValue).append("...");
                    } else {
                        sb.append(values[0]);
                    }
                } else {
                    sb.append(name).append("[]={");
                    for (int i = 0; i < values.length; i++) {
                        if (i > 0)
                            sb.append(",");
                        sb.append(values[i]);
                    }
                    sb.append("}");
                }
                sb.append("  ");
            }
            sb.append("\n");
        }
//        e = request.getHeaderNames();
//        if (e.hasMoreElements()) {
//            sb.append("Headers : ");
//            while (e.hasMoreElements()) {
//                String name = e.nextElement();
//                Enumeration<String> values = request.getHeaders(name);
//                if(values.hasMoreElements()){
//                    sb.append(name).append("=");
//                    String value = values.nextElement();
//                    if (value != null && value.length() > maxOutputLengthOfParaValue) {
//                        sb.append(value, 0, maxOutputLengthOfParaValue).append("...");
//                    } else {
//                        sb.append(value);
//                    }
//                }
//                sb.append("  ");
//            }
//            sb.append("\n");
//        }
        sb.append("-----------------------------------------------------------------------------------------\n");
        logger.info(sb.toString());
    }

    @AfterReturning("appLog()")
    public void doAfterReturning(JoinPoint joinPoint) {

    }
}