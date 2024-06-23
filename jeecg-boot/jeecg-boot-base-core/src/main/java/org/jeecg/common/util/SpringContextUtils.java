package org.jeecg.common.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.ServiceNameConstants;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @Description: spring上下文工具类
 * @author: jeecg-boot
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {

	/**
	 * 上下文对象实例
	 */
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextUtils.applicationContext = applicationContext;
	}

	/**
	 * 获取applicationContext
	 *
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	  * 获取HttpServletRequest
	 */
	public static HttpServletRequest getHttpServletRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}
	/**
	 * 获取HttpServletResponse
	 */
	public static HttpServletResponse getHttpServletResponse() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
	}

	/**
	*  获取项目根路径 basePath
	*/
	public static String getDomain(){
		HttpServletRequest request = getHttpServletRequest();
		StringBuffer url = request.getRequestURL();
		//1.微服务情况下，获取gateway的basePath
		String basePath = request.getHeader(ServiceNameConstants.X_GATEWAY_BASE_PATH);
		if(oConvertUtils.isNotEmpty(basePath)){
			return basePath;
		}else{
			String domain = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
			//2.【兼容】SSL认证之后，request.getScheme()获取不到https的问题
			// https://blog.csdn.net/weixin_34376986/article/details/89767950
			String scheme = request.getHeader(CommonConstant.X_FORWARDED_SCHEME);
			if(scheme!=null && !request.getScheme().equals(scheme)){
				domain = domain.replace(request.getScheme(),scheme);
			}
			return domain;
		}
	}

	public static String getOrigin(){
		HttpServletRequest request = getHttpServletRequest();
		return request.getHeader("Origin");
	}
	
	/**
	 * 通过name获取 Bean.
	 *
	 * @param name
	 * @return
	 */
	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}

	/**
	 * 通过class获取Bean.
	 *
	 * @param clazz
	 * @param       <T>
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz) {
		return getApplicationContext().getBean(clazz);
	}

	/**
	 * 通过name,以及Clazz返回指定的Bean
	 *
	 * @param name
	 * @param clazz
	 * @param       <T>
	 * @return
	 */
	public static <T> T getBean(String name, Class<T> clazz) {
		return getApplicationContext().getBean(name, clazz);
	}
}
