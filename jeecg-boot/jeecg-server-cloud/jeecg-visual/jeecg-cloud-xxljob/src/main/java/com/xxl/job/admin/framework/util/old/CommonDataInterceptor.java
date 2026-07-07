//package com.xxl.job.admin.framework.web.interceptor;
//
//import com.xxl.job.admin.framework.util.I18nUtil;
//import com.xxl.tool.freemarker.FtlTool;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.AsyncHandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
///**
// * push cookies to model as cookieMap
// *
// * @author xuxueli 2015-12-12 18:09:04
// */
//@Configuration
//public class CommonDataInterceptor implements WebMvcConfigurer {
//
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(new AsyncHandlerInterceptor() {
//			@Override
//			public void postHandle(HttpServletRequest request,
//								   HttpServletResponse response,
//								   Object handler,
//								   ModelAndView modelAndView) throws Exception {
//
//				// static method
//				if (modelAndView != null) {
//					modelAndView.addObject("I18nUtil", FtlTool.generateStaticModel(I18nUtil.class.getName()));
//				}
//
//			}
//		}).addPathPatterns("/**");
//	}
//
//}
