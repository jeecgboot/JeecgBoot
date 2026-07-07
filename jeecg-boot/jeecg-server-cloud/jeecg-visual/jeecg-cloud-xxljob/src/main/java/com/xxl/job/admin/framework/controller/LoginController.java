package com.xxl.job.admin.framework.controller;

import com.xxl.job.admin.framework.mapper.XxlJobUserMapper;
import com.xxl.job.admin.framework.model.XxlJobUser;
import com.xxl.job.admin.framework.util.I18nUtil;
import com.xxl.sso.core.annotation.XxlSso;
import com.xxl.sso.core.helper.XxlSsoHelper;
import com.xxl.sso.core.model.LoginInfo;
import com.xxl.tool.core.StringTool;
import com.xxl.tool.crypto.Sha256Tool;
import com.xxl.tool.id.UUIDTool;
import com.xxl.tool.response.Response;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * index controller
 *
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
@RequestMapping("/auth")
public class LoginController {

	@Resource
	private XxlJobUserMapper xxlJobUserMapper;

	@RequestMapping("/login")
	@XxlSso(login = false)
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {

		// xxl-sso, logincheck
		Response<LoginInfo> loginInfoResponse = XxlSsoHelper.loginCheckWithCookie(request, response);

		if (loginInfoResponse.isSuccess()) {
			modelAndView.setView(new RedirectView("/",true,false));
			return modelAndView;
		}
		return new ModelAndView("framework/base/login");
	}

	@RequestMapping(value="/doLogin", method=RequestMethod.POST)
	@ResponseBody
	@XxlSso(login=false)
	public Response<String> doLogin(HttpServletRequest request, HttpServletResponse response, String userName, String password, String ifRemember){

		// param
		boolean ifRem = StringTool.isNotBlank(ifRemember) && "on".equals(ifRemember);
		if (StringTool.isBlank(userName) || StringTool.isBlank(password)){
			return Response.ofFail( I18nUtil.getString("login_param_empty") );
		}

		// valid user、status
		XxlJobUser xxlJobUser = xxlJobUserMapper.loadByUserName(userName);
		if (xxlJobUser == null) {
			return Response.ofFail( I18nUtil.getString("login_param_invalid") );
		}

		// valid passowrd
		String passwordHash = Sha256Tool.sha256(password);
		if (!passwordHash.equals(xxlJobUser.getPassword())) {
			return Response.ofFail( I18nUtil.getString("login_param_invalid") );
		}

		// xxl-sso, do login
		LoginInfo loginInfo = new LoginInfo(String.valueOf(xxlJobUser.getId()), UUIDTool.getSimpleUUID());
		Response<String> result= XxlSsoHelper.loginWithCookie(loginInfo, response, ifRem);

		return Response.of(result.getCode(), result.getMsg());
	}
	
	@RequestMapping(value="/logout", method=RequestMethod.POST)
	@ResponseBody
	@XxlSso(login=false)
	public Response<String> logout(HttpServletRequest request, HttpServletResponse response){

		// xxl-sso, do logout
		Response<String> result = XxlSsoHelper.logoutWithCookie(request, response);

		return Response.of(result.getCode(), result.getMsg());
	}

	@RequestMapping("/updatePwd")
	@ResponseBody
	@XxlSso
	public Response<String> updatePwd(HttpServletRequest request, String oldPassword, String password){

		// valid
		if (oldPassword==null || oldPassword.trim().isEmpty()){
			return Response.ofFail(I18nUtil.getString("system_please_input") + I18nUtil.getString("change_pwd_field_oldpwd"));
		}
		if (password==null || password.trim().isEmpty()){
			return Response.ofFail(I18nUtil.getString("system_please_input") + I18nUtil.getString("change_pwd_field_oldpwd"));
		}
		password = password.trim();
		if (!(password.length()>=4 && password.length()<=20)) {
			return Response.ofFail(I18nUtil.getString("system_length_limit")+"[4-20]" );
		}

		// md5 password
		String oldPasswordHash = Sha256Tool.sha256(oldPassword);
		String passwordHash = Sha256Tool.sha256(password);

		// valid old pwd
		Response<LoginInfo> loginInfoResponse = XxlSsoHelper.loginCheckWithAttr(request);
		XxlJobUser existUser = xxlJobUserMapper.loadByUserName(loginInfoResponse.getData().getUserName());
		if (!oldPasswordHash.equals(existUser.getPassword())) {
			return Response.ofFail(I18nUtil.getString("change_pwd_field_oldpwd") + I18nUtil.getString("system_invalid"));
		}

		// write new
		existUser.setPassword(passwordHash);
		xxlJobUserMapper.update(existUser);

		return Response.ofSuccess();
	}

}
