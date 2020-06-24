package org.jeecg.modules.system.aspect;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.IPUtils;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.modules.system.entity.SysLog;
import org.jeecg.modules.system.service.ISysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;


/**
 * 系统日志，切面处理类
 * 
 * @Author scott
 * @email jeecgos@163.com
 * @Date 2018年1月14日
 */
@Aspect
@Component
public class AutoLogAspect {
	@Autowired
	private ISysLogService sysLogService;
	
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
		saveSysLog(point, time);

		return result;
	}

	private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		SysLog sysLog = new SysLog();
		AutoLog syslog = method.getAnnotation(AutoLog.class);
		if(syslog != null){
			//注解上的描述,操作日志内容
			sysLog.setLogContent(syslog.value());
			sysLog.setLogType(syslog.logType());
			
		}

		//请求的方法名
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();
		sysLog.setMethod(className + "." + methodName + "()");
		
		
		//设置操作类型
		if (sysLog.getLogType() == CommonConstant.LOG_TYPE_2) {
			sysLog.setOperateType(getOperateType(methodName, syslog.operateType()));
		}

		//获取request
		HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
		//请求的参数
		sysLog.setRequestParam(getReqestParams(request,joinPoint));

		//设置IP地址
		sysLog.setIp(IPUtils.getIpAddr(request));

		//获取登录用户信息
		LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		if(sysUser!=null){
			sysLog.setUserid(sysUser.getUsername());
			sysLog.setUsername(sysUser.getRealname());

		}
		//耗时
		sysLog.setCostTime(time);
		sysLog.setCreateTime(new Date());
		//保存系统日志
		sysLogService.save(sysLog);
	}
	/**
	 * 获取操作类型
	 */
	private int getOperateType(String methodName,int operateType) {
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
	* @Description: 获取请求参数
	* @author: scott
	* @date: 2020/4/16 0:10
	* @param request:  request
	* @param joinPoint:  joinPoint
	* @Return: java.lang.String
	*/
	private String getReqestParams(HttpServletRequest request, JoinPoint joinPoint) {
		String httpMethod = request.getMethod();
		String params = "";
		if ("POST".equals(httpMethod) || "PUT".equals(httpMethod) || "PATCH".equals(httpMethod)) {
			Object[] paramsArray = joinPoint.getArgs();
			params = JSONObject.toJSONString(paramsArray);
		} else {
			MethodSignature signature = (MethodSignature) joinPoint.getSignature();
			Method method = signature.getMethod();
			// 请求的方法参数值
			Object[] args = joinPoint.getArgs();
			// 请求的方法参数名称
			LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
			String[] paramNames = u.getParameterNames(method);
			if (args != null && paramNames != null) {
				for (int i = 0; i < args.length; i++) {
					params += "  " + paramNames[i] + ": " + args[i];
				}
			}
		}
		return params;
	}
}
