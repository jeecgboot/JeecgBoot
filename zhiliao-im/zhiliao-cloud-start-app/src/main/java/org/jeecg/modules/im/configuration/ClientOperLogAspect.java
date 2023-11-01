package org.jeecg.modules.im.configuration;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jeecg.boot.starter.rabbitmq.client.RabbitMqClient;
import org.jeecg.common.base.BaseMap;
import org.jeecg.common.system.util.JwtUtilApp;
import org.jeecg.modules.im.base.constant.ConstantMQ;
import org.jeecg.modules.im.base.constant.ConstantWeb;
import org.jeecg.modules.im.base.util.IPUtil;
import org.jeecg.modules.im.entity.Device;
import org.jeecg.modules.im.entity.ExceptionLog;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.entity.UserLog;
import org.jeecg.modules.im.service.DeviceService;
import org.jeecg.modules.im.service.ExceptionLogService;
import org.jeecg.modules.im.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 切面处理类，操作日志异常日志记录处理
 */
@Aspect
@Component
@Slf4j
public class ClientOperLogAspect {


    @Resource
    private ExceptionLogService exceptionLogService;
    @Resource
    private DeviceService deviceService;
    @Resource
    private UserService userService;

    @Autowired
    private RabbitMqClient rabbitMqClient;

    /**
     * 设置操作日志切入点 记录操作日志 在注解的位置切入代码
     */
    @Pointcut("@annotation(org.jeecg.modules.im.configuration.ClientOperLog)")
    public void operLogPoinCut() {
    }

    /**
     * 设置操作异常切入点记录异常日志 扫描所有controller包下操作
     */
    @Pointcut("execution(* org.jeecg.modules.im.controller..*.*(..))")
    public void operExceptionLogPoinCut() {
    }

    /**
     * 正常返回通知，拦截用户操作日志，连接点正常执行完成后执行， 如果连接点抛出异常，则不会执行
     *
     * @param joinPoint 切入点
     * @param keys      返回结果
     */
    @AfterReturning(value = "operLogPoinCut()", returning = "keys")
    public void saveOperLog(JoinPoint joinPoint, Object keys) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);

        UserLog userLog = new UserLog();
        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            // 获取操作
            ClientOperLog opLog = method.getAnnotation(ClientOperLog.class);
            if (opLog != null) {
                userLog.setModule(opLog.module()); // 操作模块
                userLog.setType(opLog.type()); // 操作类型
                userLog.setDetail(opLog.desc()); // 操作描述
            }
            // 获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            // 获取请求的方法名
            String methodName = method.getName();
            methodName = className + "." + methodName;

            userLog.setMethod(methodName); // 请求方法

            // 请求的参数
            Map<String, String> rtnMap = converMap(request.getParameterMap());
            // 将参数所在的数组转换成json
            String params = JSONObject.toJSONString(rtnMap);

            userLog.setReqParam(params); // 请求参数
            userLog.setResData(JSONObject.toJSONString(keys)); // 返回结果
            try {
                userLog.setUserId(JwtUtilApp.getUserId(request)); // 请求用户ID
            }catch(Exception e){
                log.info(e.getMessage());
            }
            userLog.setIp(getIp(request)); // 请求IP
            userLog.setIpInfo(IPUtil.getCityInfo(userLog.getIp()));
            userLog.setUri(request.getRequestURI()); // 请求URI
            userLog.setTsCreate(new Date().getTime());
            userLog.setDeviceNo(getDeviceNo(request));
            userLog.setDeviceName(getDeviceName(request));
            userLog.setDevicePlatform(getDevicePlatform(request));
            userLog.setDeviceSysVer(getDeviceSystemVersion(request));
            userLog.setDeviceClientVer(getDeviceClientVer(request));
            if(userLog.getUserId()!=null){
                User user = userService.getById(userLog.getUserId());
                if(user!=null){
                    Device device = null;
                    if(StringUtils.isNotBlank(getDeviceDetail(request))){
                        device = deviceService.findByDetail(getDeviceDetail(request),userLog.getUserId());
                    }
                    if(device==null){
                        device = deviceService.findByPlatform(getDeviceNo(request),getDevicePlatform(request),userLog.getUserId());
                    }
                    userLog.setDeviceId(device.getId());
                }
            }
            //保存，可改成放入消息队列处理
            GsonBuilder builder = new GsonBuilder();
            String msg = builder.create().toJson(userLog);
            Map map = new BaseMap();
            map.put("msg", msg);
            rabbitMqClient.sendMessage(ConstantMQ.USER_LOG,map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String getDeviceNo(HttpServletRequest request){
        return request.getHeader(ConstantWeb.DEVICE_NO);
    }
    protected String getIp(HttpServletRequest request){
        String ip = request.getHeader(ConstantWeb.IP);
        if(StringUtils.isBlank(ip)){
            ip = IPUtil.getIpAddr(request);
        }
        return ip;
    }
    protected String getDeviceName(HttpServletRequest request){
        try {
            return URLDecoder.decode(request.getHeader(ConstantWeb.DEVICE_NAME),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "Error";
        }
    }
    protected String getDeviceDetail(HttpServletRequest request){
        try {
            return URLDecoder.decode(request.getHeader(ConstantWeb.DEVICE_DETAIL),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "Error";
        }
    }
    protected String getDevicePlatform(HttpServletRequest request){
        return request.getHeader(ConstantWeb.DEVICE_PLATFORM);
    }
    protected String getJPushId(HttpServletRequest request){
        return request.getHeader(ConstantWeb.JPUSH_ID);
    }
    protected String getDeviceSystemVersion(HttpServletRequest request){
        return request.getHeader(ConstantWeb.DEVICE_SYS_VER);
    }
    protected String getDeviceClientVer(HttpServletRequest request){
        return request.getHeader(ConstantWeb.DEVICE_CLIENT_VER);
    }

    /**
     * 异常返回通知，用于拦截异常日志信息 连接点抛出异常后执行
     *
     * @param joinPoint 切入点
     * @param e         异常信息
     */
    @AfterThrowing(pointcut = "operExceptionLogPoinCut()", throwing = "e")
    public void saveExceptionLog(JoinPoint joinPoint, Throwable e) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);

        ExceptionLog excepLog = new ExceptionLog();
        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            // 获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            // 获取请求的方法名
            String methodName = method.getName();
            methodName = className + "." + methodName;
            // 请求的参数
            Map<String, String> rtnMap = converMap(request.getParameterMap());
            // 将参数所在的数组转换成json
            String params = JSONObject.toJSONString(rtnMap);
            excepLog.setReqParam(params); // 请求参数
            excepLog.setMethod(methodName); // 请求方法名
            excepLog.setExcName(e.getClass().getName()); // 异常名称
            excepLog.setExcMessage(stackTraceToString(e.getClass().getName(), e.getMessage(), e.getStackTrace())); // 异常信息
            try {
                excepLog.setUserId(JwtUtilApp.getUserId(request)); //用户id
            }catch(Exception ex){
                log.info(ex.getMessage());
            }
            excepLog.setUri(request.getRequestURI()); // 操作URI
            excepLog.setIp(getIp(request)); // 请求IP
            excepLog.setIpInfo(IPUtil.getCityInfo(excepLog.getIp()));
            excepLog.setTsCreate(new Date().getTime());
            exceptionLogService.save(excepLog);
        } catch (Exception e2) {
            e2.printStackTrace();
        }

    }

    /**
     * 转换request 请求参数
     *
     * @param paramMap request获取的参数数组
     */
    public Map<String, String> converMap(Map<String, String[]> paramMap) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }

    /**
     * 转换异常信息为字符串
     *
     * @param exceptionName    异常名称
     * @param exceptionMessage 异常信息
     * @param elements         堆栈信息
     */
    public String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements) {
        StringBuffer strbuff = new StringBuffer();
        for (StackTraceElement stet : elements) {
            strbuff.append(stet + "\n");
        }
        return exceptionName + ":" + exceptionMessage + "\n\t" + strbuff.toString();
    }
}