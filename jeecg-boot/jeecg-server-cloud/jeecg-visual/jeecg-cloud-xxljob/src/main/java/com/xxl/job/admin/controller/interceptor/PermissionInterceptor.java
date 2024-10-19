package com.xxl.job.admin.controller.interceptor;

import com.xxl.job.admin.controller.annotation.PermissionLimit;
import com.xxl.job.admin.core.model.XxlJobUser;
import com.xxl.job.admin.core.util.I18nUtil;
import com.xxl.job.admin.service.LoginService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


/**
 * 权限拦截
 *
 * @author xuxueli 2015-12-12 18:09:04
 */
@Component
public class PermissionInterceptor implements AsyncHandlerInterceptor {

	@Resource
	private LoginService loginService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		if (!(handler instanceof HandlerMethod)) {
			return true;	// proceed with the next interceptor
		}

		// if need login
		boolean needLogin = true;
		boolean needAdminuser = false;
		HandlerMethod method = (HandlerMethod)handler;
		PermissionLimit permission = method.getMethodAnnotation(PermissionLimit.class);
		if (permission!=null) {
			needLogin = permission.limit();
			needAdminuser = permission.adminuser();
		}

		if (needLogin) {
			XxlJobUser loginUser = loginService.ifLogin(request, response);
			if (loginUser == null) {
				response.setStatus(302);
				response.setHeader("location", request.getContextPath()+"/toLogin");
				return false;
			}
			if (needAdminuser && loginUser.getRole()!=1) {
				throw new RuntimeException(I18nUtil.getString("system_permission_limit"));
			}
			request.setAttribute(LoginService.LOGIN_IDENTITY_KEY, loginUser);
		}

		return true;	// proceed with the next interceptor
	}

	/**

	 * @param request request
	 * @param response response
	 * @param handler handler
	 * @param modelAndView modelAndView
	 * @throws Exception
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		// 此处数据是preHandle方法set的loginUser信息
		Object loginIdentityKey = request.getAttribute("XXL_JOB_LOGIN_IDENTITY");
		if (null != modelAndView && null != loginIdentityKey) {
			modelAndView.addObject("XXL_JOB_LOGIN_IDENTITY", loginIdentityKey);
		}
	}


}
