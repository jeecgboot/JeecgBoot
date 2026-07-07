package com.xxl.job.admin.framework.controller;

import com.xxl.job.admin.framework.constant.Consts;
import com.xxl.job.admin.framework.model.dto.XxlBootResourceDTO;
import com.xxl.job.admin.business.service.XxlJobService;
import com.xxl.job.admin.framework.util.I18nUtil;
import com.xxl.sso.core.annotation.XxlSso;
import com.xxl.sso.core.helper.XxlSsoHelper;
import com.xxl.sso.core.model.LoginInfo;
import com.xxl.tool.core.StringTool;
import com.xxl.tool.response.Response;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * index controller
 *
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
public class IndexController {

	@Resource
	private XxlJobService xxlJobService;

	/**
	 * index
	 */
	@RequestMapping("/")
	@XxlSso
	public String index(HttpServletRequest request, Model model) {

		// menu resource
		List<XxlBootResourceDTO> resourceList = findResourceList(request);
		model.addAttribute("resourceList", resourceList);

		return "framework/base/index";
	}

	/**
	 * fill menu data
	 */
	private List<XxlBootResourceDTO> findResourceList(HttpServletRequest request){
		// login check
		Response<LoginInfo> loginInfoResponse = XxlSsoHelper.loginCheckWithAttr(request);
		// init menu-list
		List<XxlBootResourceDTO> resourceDTOList = Arrays.asList(
				new XxlBootResourceDTO(1, 0, I18nUtil.getString("job_dashboard_name"),1, "", "/dashboard", "fa-home", 1, 0, null),
				new XxlBootResourceDTO(2, 0, I18nUtil.getString("jobinfo_name"),1, "", "/jobinfo", " fa-clock-o", 2, 0, null),
				new XxlBootResourceDTO(3, 0, I18nUtil.getString("joblog_name"),1, "", "/joblog", " fa-database", 3, 0, null),
				new XxlBootResourceDTO(4, 0, I18nUtil.getString("jobgroup_name"),1, Consts.ADMIN_ROLE, "/jobgroup", " fa-cloud", 4, 0,null),
				new XxlBootResourceDTO(5, 0, I18nUtil.getString("user_manage"),1, Consts.ADMIN_ROLE, "/user", "fa-users", 5, 0, null),
				new XxlBootResourceDTO(9, 0, I18nUtil.getString("admin_help"),1, "", "/help", "fa-book", 6, 0, null)
		);

		// filter by role
		if (!XxlSsoHelper.hasRole(loginInfoResponse.getData(), Consts.ADMIN_ROLE).isSuccess()) {
			resourceDTOList = resourceDTOList.stream()
					.filter(resourceDTO -> StringTool.isBlank(resourceDTO.getPermission() ))	// normal user had no permission
					.collect(Collectors.toList());
		}
		resourceDTOList.stream().sorted(Comparator.comparing(XxlBootResourceDTO::getOrder)).toList();
		return resourceDTOList;
	}

	/**
	 * dashboard
	 */
	@RequestMapping("/dashboard")
	@XxlSso
	public String dashboard(HttpServletRequest request, Model model) {

		Map<String, Object> dashboardMap = xxlJobService.dashboardInfo();
		model.addAllAttributes(dashboardMap);

		return "framework/base/dashboard";
	}

	@RequestMapping("/chartInfo")
	@ResponseBody
	public Response<Map<String, Object>> chartInfo(@RequestParam("startDate") Date startDate, @RequestParam("endDate") Date endDate) {
		Response<Map<String, Object>> chartInfo = xxlJobService.chartInfo(startDate, endDate);
		return chartInfo;
	}

	/**
	 * help
	 */
	@RequestMapping("/help")
	@XxlSso
	public String help() {
		return "framework/base/help";
	}

	@RequestMapping(value = "/errorpage")
	@XxlSso(login = false)
	public ModelAndView errorPage(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {

		String exceptionMsg = "HTTP Status Code: "+response.getStatus();

		mv.addObject("exceptionMsg", exceptionMsg);
		mv.setViewName("framework/common/common.errorpage");
		return mv;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
}
