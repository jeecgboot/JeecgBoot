package org.jeecg.common.system.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jeecg.common.system.vo.SysUserCacheInfo;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.modules.system.entity.SysPermissionDataRule;
import org.springframework.util.StringUtils;

/**
 * @ClassName: JeecgDataAutorUtils
 * @Description: 数据权限查询规则容器工具类
 * @Author: 张代浩
 * @Date: 2012-12-15 下午11:27:39
 * 
 */
public class JeecgDataAutorUtils {
	
	public static final String MENU_DATA_AUTHOR_RULES = "MENU_DATA_AUTHOR_RULES";
	
	public static final String MENU_DATA_AUTHOR_RULE_SQL = "MENU_DATA_AUTHOR_RULE_SQL";
	
	public static final String SYS_USER_INFO = "SYS_USER_INFO";

	/**
	 * 往链接请求里面，传入数据查询条件
	 * 
	 * @param request
	 * @param MENU_DATA_AUTHOR_RULES
	 */
	public static synchronized void installDataSearchConditon(HttpServletRequest request, List<SysPermissionDataRule> dataRules) {
		@SuppressWarnings("unchecked")
		List<SysPermissionDataRule> list = (List<SysPermissionDataRule>)loadDataSearchConditon();// 1.先从request获取MENU_DATA_AUTHOR_RULES，如果存则获取到LIST
		if (list==null) {
			// 2.如果不存在，则new一个list
			list = new ArrayList<SysPermissionDataRule>();
		}
		for (SysPermissionDataRule tsDataRule : dataRules) {
			list.add(tsDataRule);
		}
		request.setAttribute(MENU_DATA_AUTHOR_RULES, list); // 3.往list里面增量存指
	}

	/**
	 * 获取请求对应的数据权限规则
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static synchronized List<SysPermissionDataRule> loadDataSearchConditon() {
		return (List<SysPermissionDataRule>) SpringContextUtils.getHttpServletRequest().getAttribute(MENU_DATA_AUTHOR_RULES);
				
	}

	/**
	 * 获取请求对应的数据权限SQL
	 * 
	 * @param request
	 * @return
	 */
	public static synchronized String loadDataSearchConditonSQLString() {
		return (String) SpringContextUtils.getHttpServletRequest().getAttribute(MENU_DATA_AUTHOR_RULE_SQL);
	}

	/**
	 * 往链接请求里面，传入数据查询条件
	 * 
	 * @param request
	 * @param MENU_DATA_AUTHOR_RULE_SQL
	 */
	public static synchronized void installDataSearchConditon(HttpServletRequest request, String sql) {
		String ruleSql = (String)loadDataSearchConditonSQLString();
		if (!StringUtils.hasText(ruleSql)) {
			request.setAttribute(MENU_DATA_AUTHOR_RULE_SQL,sql);
		}
	}
	
	public static synchronized void installUserInfo(HttpServletRequest request, SysUserCacheInfo userinfo) {
		request.setAttribute(SYS_USER_INFO, userinfo);
	}
	
	public static synchronized SysUserCacheInfo loadUserInfo() {
		return (SysUserCacheInfo) SpringContextUtils.getHttpServletRequest().getAttribute(SYS_USER_INFO);
				
	}
}
