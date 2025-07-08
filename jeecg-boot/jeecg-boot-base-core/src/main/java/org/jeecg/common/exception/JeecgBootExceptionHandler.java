package org.jeecg.common.exception;

import cn.hutool.core.util.ObjectUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import io.undertow.server.RequestTooBigException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.jeecg.common.api.dto.LogDTO;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.enums.ClientTerminalTypeEnum;
import org.jeecg.common.enums.SentinelErrorInfoEnum;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.BrowserUtils;
import org.jeecg.common.util.IpUtils;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.base.service.BaseCommonService;
import org.springframework.beans.BeansException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.connection.PoolException;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 异常处理器
 * 
 * @Author scott
 * @Date 2019
 */
@RestControllerAdvice
@Slf4j
public class JeecgBootExceptionHandler {

	@Resource
	BaseCommonService baseCommonService;

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Result<?> handleValidationExceptions(MethodArgumentNotValidException e) {
		log.error(e.getMessage(), e);
		addSysLog(e);
		return Result.error("校验失败！" + e.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(",")));
	}
	
	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(JeecgBootException.class)
	public Result<?> handleJeecgBootException(JeecgBootException e){
		log.error(e.getMessage(), e);
		addSysLog(e);
		return Result.error(e.getErrCode(), e.getMessage());
	}
	
	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(JeecgBootBizTipException.class)
	public Result<?> handleJeecgBootBizTipException(JeecgBootBizTipException e){
		log.error(e.getMessage());
		return Result.error(e.getErrCode(), e.getMessage());
	}

	/**
	 * 处理自定义微服务异常
	 */
	@ExceptionHandler(JeecgCloudException.class)
	public Result<?> handleJeecgCloudException(JeecgCloudException e){
		log.error(e.getMessage(), e);
		addSysLog(e);
		return Result.error(e.getMessage());
	}

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(JeecgBoot401Exception.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public Result<?> handleJeecgBoot401Exception(JeecgBoot401Exception e){
		log.error(e.getMessage(), e);
		addSysLog(e);
		return new Result(401,e.getMessage());
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public Result<?> handlerNoFoundException(Exception e) {
		log.error(e.getMessage(), e);
		addSysLog(e);
		return Result.error(404, "路径不存在，请检查路径是否正确");
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public Result<?> handleDuplicateKeyException(DuplicateKeyException e){
		log.error(e.getMessage(), e);
		addSysLog(e);
		return Result.error("数据库中已存在该记录");
	}

	@ExceptionHandler({UnauthorizedException.class, AuthorizationException.class})
	public Result<?> handleAuthorizationException(AuthorizationException e){
		log.error(e.getMessage(), e);
		return Result.noauth("没有权限，请联系管理员分配权限！");
	}

	@ExceptionHandler(Exception.class)
	public Result<?> handleException(Exception e){
		log.error(e.getMessage(), e);
		//update-begin---author:zyf ---date:20220411  for：处理Sentinel限流自定义异常
		Throwable throwable = e.getCause();
		SentinelErrorInfoEnum errorInfoEnum = SentinelErrorInfoEnum.getErrorByException(throwable);
		if (ObjectUtil.isNotEmpty(errorInfoEnum)) {
			return Result.error(errorInfoEnum.getError());
		}
		//update-end---author:zyf ---date:20220411  for：处理Sentinel限流自定义异常
		addSysLog(e);
		return Result.error("操作失败，"+e.getMessage());
	}
	
	/**
	 * @Author 政辉
	 * @param e
	 * @return
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public Result<?> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
		StringBuffer sb = new StringBuffer();
		sb.append("不支持");
		sb.append(e.getMethod());
		sb.append("请求方法，");
		sb.append("支持以下");
		String [] methods = e.getSupportedMethods();
		if(methods!=null){
			for(String str:methods){
				sb.append(str);
				sb.append("、");
			}
		}
		log.error(sb.toString(), e);
		//return Result.error("没有权限，请联系管理员授权");
		addSysLog(e);
		return Result.error(405,sb.toString());
	}
	
	 /** 
	  * spring默认上传大小100MB 超出大小捕获异常MaxUploadSizeExceededException 
	  */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
    	log.error(e.getMessage(), e);
		addSysLog(e);
        return Result.error("文件大小超出10MB限制, 请压缩或降低文件质量! ");
    }

	/**
	 * 处理文件过大异常.
	 * jdk17中的MultipartException异常类已经被拆分成了MultipartException和MaxUploadSizeExceededException
	 * for [QQYUN-11716]上传大图片失败没有精确提示
	 * @param e
	 * @return
	 * @author chenrui
	 * @date 2025/4/8 16:13
	 */
	@ExceptionHandler(MultipartException.class)
	public Result<?> handleMaxUploadSizeExceededException(MultipartException e) {
		Throwable cause = e.getCause();
		if (cause instanceof IllegalStateException && cause.getCause() instanceof RequestTooBigException) {
			log.error("文件大小超出限制: {}", cause.getMessage(), e);
			addSysLog(e);
			return Result.error("文件大小超出限制, 请压缩或降低文件质量!");
		} else {
			return handleException(e);
		}
	}

    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result<?> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
    	log.error(e.getMessage(), e);
		addSysLog(e);
    	//【issues/3624】数据库执行异常handleDataIntegrityViolationException提示有误 #3624
        return Result.error("执行数据库异常,违反了完整性例如：违反惟一约束、违反非空限制、字段内容超出长度等");
    }

    @ExceptionHandler(PoolException.class)
    public Result<?> handlePoolException(PoolException e) {
    	log.error(e.getMessage(), e);
		addSysLog(e);
        return Result.error("Redis 连接异常!");
    }


	/**
	 * SQL注入风险，全局异常处理
	 *
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(JeecgSqlInjectionException.class)
	public Result<?> handleSQLException(Exception exception) {
		String msg = exception.getMessage().toLowerCase();
		final String extractvalue = "extractvalue";
		final String updatexml = "updatexml";
		boolean hasSensitiveInformation = msg.indexOf(extractvalue) >= 0 || msg.indexOf(updatexml) >= 0;
		if (msg != null && hasSensitiveInformation) {
			log.error("校验失败，存在SQL注入风险！{}", msg);
			return Result.error("校验失败，存在SQL注入风险！");
		}
		addSysLog(exception);
		return Result.error("校验失败，存在SQL注入风险！" + msg);
	}

	//update-begin---author:chenrui ---date:20240423  for：[QQYUN-8732]把错误的日志都抓取了 方便后续处理，单独弄个日志类型------------
	/**
	 * 添加异常新系统日志
	 * @param e 异常
	 * @author chenrui
	 * @date 2024/4/22 17:16
	 */
    private void addSysLog(Throwable e) {
        LogDTO log = new LogDTO();
        log.setLogType(CommonConstant.LOG_TYPE_4);
        log.setLogContent(e.getClass().getName()+":"+e.getMessage());
		log.setRequestParam(ExceptionUtils.getStackTrace(e));
        //获取request
        HttpServletRequest request = null;
        try {
            request = SpringContextUtils.getHttpServletRequest();
        } catch (NullPointerException | BeansException ignored) {
        }
        if (null != request) {
			//update-begin---author:chenrui ---date:20250408  for：[QQYUN-11716]上传大图片失败没有精确提示------------
			//请求的参数
			if (!isTooBigException(e)) {
				// 文件上传过大异常时不能获取参数,否则会报错
				Map<String, String[]> parameterMap = request.getParameterMap();
				if(!CollectionUtils.isEmpty(parameterMap)) {
					log.setMethod(oConvertUtils.mapToString(request.getParameterMap()));
				}
			}
			//update-end---author:chenrui ---date:20250408  for：[QQYUN-11716]上传大图片失败没有精确提示------------
            // 请求地址
            log.setRequestUrl(request.getRequestURI());
            //设置IP地址
            log.setIp(IpUtils.getIpAddr(request));
            //设置客户端
			if(BrowserUtils.isDesktop(request)){
				log.setClientType(ClientTerminalTypeEnum.PC.getKey());
			}else{
				log.setClientType(ClientTerminalTypeEnum.APP.getKey());
			}
        }

       
		//获取登录用户信息
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if(sysUser!=null){
			log.setUserid(sysUser.getUsername());
			log.setUsername(sysUser.getRealname());

		}

        baseCommonService.addLog(log);
    }
	//update-end---author:chenrui ---date:20240423  for：[QQYUN-8732]把错误的日志都抓取了 方便后续处理，单独弄个日志类型------------

	/**
	 * 是否文件过大异常
	 * for [QQYUN-11716]上传大图片失败没有精确提示
	 * @param e
	 * @return
	 * @author chenrui
	 * @date 2025/4/8 20:21
	 */
	private static boolean isTooBigException(Throwable e) {
		boolean isTooBigException = false;
		if(e instanceof MultipartException){
			Throwable cause = e.getCause();
			if (cause instanceof IllegalStateException && cause.getCause() instanceof RequestTooBigException){
				isTooBigException = true;
			}
		}
		if(e instanceof MaxUploadSizeExceededException){
			isTooBigException = true;
		}
		return isTooBigException;
	}

}
