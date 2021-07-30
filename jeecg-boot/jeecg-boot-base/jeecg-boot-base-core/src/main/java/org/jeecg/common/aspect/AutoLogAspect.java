package org.jeecg.common.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.PropertyFilter;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.jeecg.common.api.dto.LogDTO;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.enums.ModuleType;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.*;
import org.jeecg.modules.base.service.BaseCommonService;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;


/**
 * 系统日志，切面处理类
 *
 * @Author scott
 * @email jeecgos@163.com
 * @Date 2018年1月14日
 */
@Aspect
@Component
@RequiredArgsConstructor
public class AutoLogAspect {

    private static final List<Class<?>> ignoreArgClasses = Arrays.asList(BindingResult.class, ServletRequest.class,
            ServletResponse.class, MultipartFile.class, ShiroHttpServletRequest.class);

    private final BaseCommonService baseCommonService;

    @Pointcut("@annotation(org.jeecg.common.aspect.annotation.AutoLog)")
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
        saveSysLog(point, time, result);

        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time, Object obj) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        LogDTO dto = new LogDTO();
        AutoLog syslog = method.getAnnotation(AutoLog.class);
        if (syslog != null) {
            //update-begin-author:taoyan date:
            String content = syslog.value();
            if (syslog.module() == ModuleType.ONLINE) {
                content = getOnlineLogContent(obj, content);
            }
            //注解上的描述,操作日志内容
            dto.setLogType(syslog.logType());
            dto.setLogContent(content);
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        dto.setMethod(className + "." + methodName + "()");

        //设置操作类型
        if (dto.getLogType() == CommonConstant.LOG_TYPE_2) {
            dto.setOperateType(getOperateType(methodName, syslog.operateType()));
        }

        //获取request
        HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
        //请求的参数
        dto.setRequestParam(getRequestParams(joinPoint));
        //设置IP地址
        dto.setIp(IPUtils.getIpAddr(request));
        //获取登录用户信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (sysUser != null) {
            dto.setUserid(sysUser.getUsername());
            dto.setUsername(sysUser.getRealname());
        }
        //耗时
        dto.setCostTime(time);
        dto.setCreateTime(new Date());
        //保存系统日志
        baseCommonService.addLog(dto);
    }


    /**
     * 获取操作类型
     */
    private int getOperateType(String methodName, int operateType) {
        if (operateType > 0) {
            return operateType;
        }
        if (methodName.startsWith("list")) {
            return CommonConstant.OPERATE_TYPE_1;
        }
        if (methodName.startsWith("add")) {
            return CommonConstant.OPERATE_TYPE_2;
        }
        if (methodName.startsWith("edit")) {
            return CommonConstant.OPERATE_TYPE_3;
        }
        if (methodName.startsWith("delete")) {
            return CommonConstant.OPERATE_TYPE_4;
        }
        if (methodName.startsWith("import")) {
            return CommonConstant.OPERATE_TYPE_5;
        }
        if (methodName.startsWith("export")) {
            return CommonConstant.OPERATE_TYPE_6;
        }
        return CommonConstant.OPERATE_TYPE_1;
    }

    /**
     * @param joinPoint: joinPoint
     * @Description: 获取请求参数
     * @author: scott
     * @date: 2020/4/16 0:10
     * @Return: java.lang.String
     */
    //update-begin-author:season date:20210730 for:统一日志格式为JSON
    private String getRequestParams(JoinPoint joinPoint) {
        Map<String, Object> loggingArguments = getLoggingArguments(joinPoint);
        //update-begin-author:taoyan date:20200724 for:日志数据太长的直接过滤掉
        PropertyFilter propFilter = (o, name, value) -> value == null || value.toString().length() <= 500;
        //update-end-author:taoyan date:20200724 for:日志数据太长的直接过滤掉
        return JSON.toJSONString(loggingArguments, propFilter);
    }

    private Map<String, Object> getLoggingArguments(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] names = u.getParameterNames(method);
        Map<String, Object> result = new HashMap<>();
        for (int i = 0; i < names.length; i++) {
            if (isLoggingArgument(args[i])) {
                result.put(names[i], args[i]);
            }
        }
        return result;
    }

    /**
     * 是否为需要记录日志的参数
     *
     * @param arg
     * @return
     */
    protected boolean isLoggingArgument(Object arg) {
        return ignoreArgClasses.stream().filter(c -> arg != null && c.isAssignableFrom(arg.getClass())).count() == 0;
    }
    //update-end-author:season date:20210730 for:统一日志格式为JSON

    /**
     * online日志内容拼接
     *
     * @param obj
     * @param content
     * @return
     */
    private String getOnlineLogContent(Object obj, String content) {
        if (Result.class.isInstance(obj)) {
            Result res = (Result) obj;
            String msg = res.getMessage();
            String tableName = res.getOnlTable();
            if (oConvertUtils.isNotEmpty(tableName)) {
                content += ",表名:" + tableName;
            }
            if (res.isSuccess()) {
                content += "," + (oConvertUtils.isEmpty(msg) ? "操作成功" : msg);
            } else {
                content += "," + (oConvertUtils.isEmpty(msg) ? "操作失败" : msg);
            }
        }
        return content;
    }

}
