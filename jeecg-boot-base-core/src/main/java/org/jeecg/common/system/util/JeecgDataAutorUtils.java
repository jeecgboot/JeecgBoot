package org.jeecg.common.system.util;

import org.jeecg.common.system.vo.SysPermissionDataRuleModel;
import org.jeecg.common.system.vo.SysUserCacheInfo;
import org.jeecg.common.util.SpringContextUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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
	 * @param dataRules
	 */
	public static synchronized void installDataSearchConditon(HttpServletRequest request, List<SysPermissionDataRuleModel> dataRules) {
		@SuppressWarnings("unchecked")
        // 1.先从request获取MENU_DATA_AUTHOR_RULES，如果存则获取到LIST
		List<SysPermissionDataRuleModel> list = (List<SysPermissionDataRuleModel>)loadDataSearchConditon();
		if (list==null) {
			// 2.如果不存在，则new一个list
			list = new ArrayList<SysPermissionDataRuleModel>();
		}
		for (SysPermissionDataRuleModel tsDataRule : dataRules) {
			list.add(tsDataRule);
		}
        // 3.往list里面增量存指
		request.setAttribute(MENU_DATA_AUTHOR_RULES, list);
	}

	/**
	 * 获取请求对应的数据权限规则
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static synchronized List<SysPermissionDataRuleModel> loadDataSearchConditon() {
		return (List<SysPermissionDataRuleModel>) SpringContextUtils.getHttpServletRequest().getAttribute(MENU_DATA_AUTHOR_RULES);
				
	}

	/**
	 * 获取请求对应的数据权限SQL
	 * 
	 * @return
	 */
	public static synchronized String loadDataSearchConditonSqlString() {
		return (String) SpringContextUtils.getHttpServletRequest().getAttribute(MENU_DATA_AUTHOR_RULE_SQL);
	}

	/**
	 * 往链接请求里面，传入数据查询条件
	 * 
	 * @param request
	 * @param sql
	 */
	public static synchronized void installDataSearchConditon(HttpServletRequest request, String sql) {
		String ruleSql = (String) loadDataSearchConditonSqlString();
		if (!StringUtils.hasText(ruleSql)) {
			request.setAttribute(MENU_DATA_AUTHOR_RULE_SQL,sql);
		}
	}

	/**
	 * 将用户信息存到request
	 * @param request
	 * @param userinfo
	 */
	public static synchronized void installUserInfo(HttpServletRequest request, SysUserCacheInfo userinfo) {
		request.setAttribute(SYS_USER_INFO, userinfo);
	}

	/**
	 * 将用户信息存到request
	 * @param userinfo
	 */
	public static synchronized void installUserInfo(SysUserCacheInfo userinfo) {
		SpringContextUtils.getHttpServletRequest().setAttribute(SYS_USER_INFO, userinfo);
	}

	/**
	 * 从request获取用户信息
	 * @return
	 */
	public static synchronized SysUserCacheInfo loadUserInfo() {
		return (SysUserCacheInfo) SpringContextUtils.getHttpServletRequest().getAttribute(SYS_USER_INFO);
				
	}
}
