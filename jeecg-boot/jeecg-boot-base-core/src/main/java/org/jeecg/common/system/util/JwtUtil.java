package org.jeecg.common.system.util;

import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.constant.TenantConstant;
import org.jeecg.common.util.LoginUserUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.DataBaseConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.system.vo.SysUserCacheInfo;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;

/**
 * @Author Scott
 * @Date 2018-07-12 14:23
 * @Desc JWT工具类 - 已迁移到Sa-Token，此类作为兼容层保留
 **/
@Slf4j
public class JwtUtil {
	
	static final String WELL_NUMBER = SymbolConstant.WELL_NUMBER + SymbolConstant.LEFT_CURLY_BRACKET;
	
	/**
	 * 返回错误 JSON 字符串（用于 Sa-Token Filter）
	 * @param code 错误码
	 * @param errorMsg 错误信息
	 * @return JSON 字符串
	 */
	public static String responseErrorJson(Integer code, String errorMsg) {
		try {
			Result jsonResult = new Result(code, errorMsg);
			jsonResult.setSuccess(false);
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(jsonResult);
		} catch (IOException e) {
			log.error("生成错误 JSON 失败: {}", e.getMessage());
			// 返回备用的硬编码 JSON
			return "{\"success\":false,\"message\":\"" + errorMsg + "\",\"code\":" + code + ",\"result\":null,\"timestamp\":" + System.currentTimeMillis() + "}";
		}
	}
	
	/**
	 * 校验token是否正确
	 * 注意：此方法已废弃，使用Sa-Token自动校验
	 * 
	 * @param token
	 * @return
	 */
	@Deprecated
	public static boolean verify(String token){
		try {
			// 使用Sa-Token验证
			return StpUtil.getLoginIdByToken(token) != null;
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * 获得Token中的用户名（不校验token是否有效）
	 * <p>注意：现在 loginId 就是 username，直接返回
	 * 
	 * @param token JWT token
	 * @return 用户名（username），如果 token 无效则返回 null
	 */
	public static String getUsername(String token){
		try {
			if(oConvertUtils.isEmpty(token)) {
				return null;
			}
			// Sa-Token 的 loginId 现在就是 username，直接返回
			Object loginId = StpUtil.getLoginIdByToken(token);
			return loginId != null ? loginId.toString() : null;
		} catch (Exception e) {
			log.warn("获取用户名失败: {}", e.getMessage());
			return null;
		}
	}

	/**
	 * 根据request中的token获取用户账号
	 * 注意：此方法已适配Sa-Token
	 * 
	 * @param request
	 * @return
	 * @throws JeecgBootException
	 */
	public static String getUserNameByToken(HttpServletRequest request) throws JeecgBootException {
		String accessToken = request.getHeader("X-Access-Token");
		String username = getUsername(accessToken);
		if (oConvertUtils.isEmpty(username)) {
			throw new JeecgBootException("未获取到用户");
		}
		return username;
	}

	/**
	 *  从session中获取变量
	 * @param key
	 * @return
	 */
	public static String getSessionData(String key) {
		//${myVar}%
		//得到${} 后面的值
		String moshi = "";
		String wellNumber = WELL_NUMBER;

		if(key.indexOf(SymbolConstant.RIGHT_CURLY_BRACKET)!=-1){
			moshi = key.substring(key.indexOf("}")+1);
		}
		String returnValue = null;
		if (key.contains(wellNumber)) {
			key = key.substring(2,key.indexOf("}"));
		}
		if (oConvertUtils.isNotEmpty(key)) {
			HttpSession session = SpringContextUtils.getHttpServletRequest().getSession();
			returnValue = (String) session.getAttribute(key);
		}
		//结果加上${} 后面的值
		if(returnValue!=null){returnValue = returnValue + moshi;}
		return returnValue;
	}

	/**
	 * 从当前用户中获取变量
	 * @param key
	 * @param user
	 * @return
	 */
	public static String getUserSystemData(String key, SysUserCacheInfo user) {
		//1.优先获取 SysUserCacheInfo
		if (user == null) {
			try {
				user = JeecgDataAutorUtils.loadUserInfo();
			} catch (Exception e) {
				log.warn("获取用户信息异常：" + e.getMessage());
			}
		}
		//2.通过shiro获取登录用户信息
		LoginUser sysUser = null;
		try {
			sysUser = (LoginUser) LoginUserUtils.getSessionUser();
		} catch (Exception e) {
			log.warn("SecurityUtils.getSubject() 获取用户信息异常：" + e.getMessage());
		}

		//#{sys_user_code}%
		String moshi = "";
		String wellNumber = WELL_NUMBER;
		if (key.indexOf(SymbolConstant.RIGHT_CURLY_BRACKET) != -1) {
			moshi = key.substring(key.indexOf("}") + 1);
		}
		String returnValue = null;
		//针对特殊标示处理#{sysOrgCode}，判断替换
		if (key.contains(wellNumber)) {
			key = key.substring(2, key.indexOf("}"));
		} else {
			key = key;
		}
		// 是否存在字符串标志
		boolean multiStr;
		if (oConvertUtils.isNotEmpty(key) && key.trim().matches("^\\[\\w+]$")) {
			key = key.substring(1, key.length() - 1);
			multiStr = true;
		} else {
			multiStr = false;
		}
		//替换为当前系统时间(年月日)
		if (key.equals(DataBaseConstant.SYS_DATE) || key.toLowerCase().equals(DataBaseConstant.SYS_DATE_TABLE)) {
			returnValue = DateUtils.formatDate();
		}
		//替换为当前系统时间（年月日时分秒）
		else if (key.equals(DataBaseConstant.SYS_TIME) || key.toLowerCase().equals(DataBaseConstant.SYS_TIME_TABLE)) {
			returnValue = DateUtils.now();
		}
		//流程状态默认值（默认未发起）
		else if (key.equals(DataBaseConstant.BPM_STATUS) || key.toLowerCase().equals(DataBaseConstant.BPM_STATUS_TABLE)) {
			returnValue = "1";
		}

		//后台任务获取用户信息异常，导致程序中断
		if (sysUser == null && user == null) {
			return null;
		}

		//替换为系统登录用户帐号
		if (key.equals(DataBaseConstant.SYS_USER_CODE) || key.toLowerCase().equals(DataBaseConstant.SYS_USER_CODE_TABLE)) {
			if (user == null) {
				returnValue = sysUser.getUsername();
			} else {
				returnValue = user.getSysUserCode();
			}
		}

		// 替换为系统登录用户ID
		else if (key.equals(DataBaseConstant.SYS_USER_ID) || key.equalsIgnoreCase(DataBaseConstant.SYS_USER_ID_TABLE)) {
			if (user == null) {
				returnValue = sysUser.getId();
			} else {
				returnValue = user.getSysUserId();
			}
		}

		//替换为系统登录用户真实名字
		else if (key.equals(DataBaseConstant.SYS_USER_NAME) || key.toLowerCase().equals(DataBaseConstant.SYS_USER_NAME_TABLE)) {
			if (user == null) {
				returnValue = sysUser.getRealname();
			} else {
				returnValue = user.getSysUserName();
			}
		}

		//替换为系统用户登录所使用的机构编码
		else if (key.equals(DataBaseConstant.SYS_ORG_CODE) || key.toLowerCase().equals(DataBaseConstant.SYS_ORG_CODE_TABLE)) {
			if (user == null) {
				returnValue = sysUser.getOrgCode();
			} else {
				returnValue = user.getSysOrgCode();
			}
		}

		// 替换为系统用户登录所使用的机构ID
		else if (key.equals(DataBaseConstant.SYS_ORG_ID) || key.equalsIgnoreCase(DataBaseConstant.SYS_ORG_ID_TABLE)) {
			if (user == null) {
				returnValue = sysUser.getOrgId();
			} else {
				returnValue = user.getSysOrgId();
			}
		}

		//替换为系统用户所拥有的所有机构编码
		else if (key.equals(DataBaseConstant.SYS_MULTI_ORG_CODE) || key.toLowerCase().equals(DataBaseConstant.SYS_MULTI_ORG_CODE_TABLE)) {
			if (user == null) {
				returnValue = sysUser.getOrgCode();
				returnValue = multiStr ? "'" + returnValue + "'" : returnValue;
			} else {
				if (user.isOneDepart()) {
					returnValue = user.getSysMultiOrgCode().get(0);
					returnValue = multiStr ? "'" + returnValue + "'" : returnValue;
				} else {
					returnValue = user.getSysMultiOrgCode().stream()
							.filter(Objects::nonNull)
							.map(orgCode -> {
								if (multiStr) {
									return "'" + orgCode + "'";
								} else {
									return orgCode;
								}
							})
							.collect(Collectors.joining(", "));
				}
			}
		}

		// 替换为当前登录用户的角色code（多个逗号分割）
		else if (key.equals(DataBaseConstant.SYS_ROLE_CODE) || key.equalsIgnoreCase(DataBaseConstant.SYS_ROLE_CODE_TABLE)) {
			if (user == null) {
				returnValue = sysUser.getRoleCode();
			} else {
				returnValue = user.getSysRoleCode();
			}
		}

		// 多租户ID作为系统变量
		else if (key.equals(TenantConstant.TENANT_ID) || key.toLowerCase().equals(TenantConstant.TENANT_ID_TABLE)) {
			try {
				returnValue = SpringContextUtils.getHttpServletRequest().getHeader(CommonConstant.TENANT_ID);
			} catch (Exception e) {
				log.warn("获取系统租户异常：" + e.getMessage());
			}
		}
		if (returnValue != null) {
			returnValue = returnValue + moshi;
		}
		return returnValue;
	}
}
