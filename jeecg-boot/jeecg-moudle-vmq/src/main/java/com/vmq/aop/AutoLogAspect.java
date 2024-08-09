package com.vmq.aop;

import com.vmq.annotation.AutoLog;
import com.vmq.constant.Constant;
import com.vmq.constant.OperateTypeEnum;
import com.vmq.entity.SysLog;
import com.vmq.service.SysLogService;
import com.vmq.utils.HttpRequest;
import com.vmq.utils.LogUtil;
import com.vmq.utils.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;


/**
 * 保存系统操作日志
 */
@Aspect
@Component
public class AutoLogAspect {

    @Autowired
    private SysLogService sysLogService;

    @Pointcut("@annotation(com.vmq.annotation.AutoLog)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        //保存日志
        saveSysLog(point, time);
        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 获取reques对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = (attributes == null ? null : attributes.getRequest());
        SysLog dto = new SysLog();
        AutoLog syslog = method.getAnnotation(AutoLog.class);
        if(syslog != null){
            String content = syslog.value();
            //注解上的描述,操作日志内容
            dto.setLogType(syslog.logType());
            dto.setLogContent(content);
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        dto.setMethod(className + "." + methodName + "()");

        //设置用户信息
        String username = (String) request.getSession(true).getAttribute("username");
        dto.setUsername(username);
        dto.setUserid(username);

        //请求的参数
        if (Constant.NUMBER_1 != dto.getLogType()) {
            //设置操作类型
            if (Constant.NUMBER_2 == dto.getLogType()) {
                dto.setOperateType(getOperateType(methodName, syslog.operateType()));
            }
            dto.setRequestParam(StringUtils.join(LogUtil.getResquestParam(joinPoint)));
        }
        //设置IP地址
        dto.setIp(HttpRequest.getIpAddr(request));

        //耗时
        dto.setCostTime(time);
        dto.setCreateTime(new Date());
        //保存系统日志
        sysLogService.saveAsync(dto);
    }


    /**
     * 获取操作类型
     */
    private int getOperateType(String methodName,int operateType) {
        if (operateType > 0) {
            return operateType;
        }
        return OperateTypeEnum.getTypeByMethodName(methodName);
    }

}
