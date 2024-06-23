package org.jeecg.common.system.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.DataBaseConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.constant.TenantConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.system.vo.SysUserCacheInfo;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;

/**
 * @Author Scott
 * @Date 2018-07-12 14:23
 * @Desc JWT工具类
 **/
@Slf4j
public class JwtUtil {

	/**Token有效期为7天（Token在reids中缓存时间为两倍）*/
	public static final long EXPIRE_TIME = (7 * 12) * 60 * 60 * 1000;
	static final String WELL_NUMBER = SymbolConstant.WELL_NUMBER + SymbolConstant.LEFT_CURLY_BRACKET;

    /**
     *
     * @param response
     * @param code
     * @param errorMsg
     */
    public static void responseError(ServletResponse response, Integer code, String errorMsg) {
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		// issues/I4YH95浏览器显示乱码问题
		httpServletResponse.setHeader("Content-type", "text/html;charset=UTF-8");
        Result jsonResult = new Result(code, errorMsg);
		jsonResult.setSuccess(false);
        OutputStream os = null;
        try {
            os = httpServletResponse.getOutputStream();
			httpServletResponse.setCharacterEncoding("UTF-8");
			httpServletResponse.setStatus(code);
            os.write(new ObjectMapper().writeValueAsString(jsonResult).getBytes("UTF-8"));
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	/**
	 * 校验token是否正确
	 *
	 * @param token  密钥
	 * @param secret 用户的密码
	 * @return 是否正确
	 */
	public static boolean verify(String token, String username, String secret) {
		try {
			// 根据密码生成JWT效验器
			Algorithm algorithm = Algorithm.HMAC256(secret);
			JWTVerifier verifier = JWT.require(algorithm).withClaim("username", username).build();
			// 效验TOKEN
			DecodedJWT jwt = verifier.verify(token);
			return true;
		} catch (Exception exception) {
			return false;
		}
	}

	/**
	 * 获得token中的信息无需secret解密也能获得
	 *
	 * @return token中包含的用户名
	 */
	public static String getUsername(String token) {
		try {
			DecodedJWT jwt = JWT.decode(token);
			return jwt.getClaim("username").asString();
		} catch (JWTDecodeException e) {
			return null;
		}
	}

	/**
	 * 生成签名,5min后过期
	 *
	 * @param username 用户名
	 * @param secret   用户的密码
	 * @return 加密的token
	 */
	public static String sign(String username, String secret) {
		Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
		Algorithm algorithm = Algorithm.HMAC256(secret);
		// 附带username信息
		return JWT.create().withClaim("username", username).withExpiresAt(date).sign(algorithm);

	}

	/**
	 * 根据request中的token获取用户账号
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
		if(user==null) {
			try {
				user = JeecgDataAutorUtils.loadUserInfo();
			} catch (Exception e) {
				log.warn("获取用户信息异常：" + e.getMessage());
			}
		}
		//2.通过shiro获取登录用户信息
		LoginUser sysUser = null;
		try {
			sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		} catch (Exception e) {
			log.warn("SecurityUtils.getSubject() 获取用户信息异常：" + e.getMessage());
		}

		//#{sys_user_code}%
		String moshi = "";
        String wellNumber = WELL_NUMBER;
		if(key.indexOf(SymbolConstant.RIGHT_CURLY_BRACKET)!=-1){
			 moshi = key.substring(key.indexOf("}")+1);
		}
		String returnValue = null;
		//针对特殊标示处理#{sysOrgCode}，判断替换
		if (key.contains(wellNumber)) {
			key = key.substring(2,key.indexOf("}"));
		} else {
			key = key;
		}
		//替换为当前系统时间(年月日)
		if (key.equals(DataBaseConstant.SYS_DATE)|| key.toLowerCase().equals(DataBaseConstant.SYS_DATE_TABLE)) {
			returnValue = DateUtils.formatDate();
		}
		//替换为当前系统时间（年月日时分秒）
		else if (key.equals(DataBaseConstant.SYS_TIME)|| key.toLowerCase().equals(DataBaseConstant.SYS_TIME_TABLE)) {
			returnValue = DateUtils.now();
		}
		//流程状态默认值（默认未发起）
		else if (key.equals(DataBaseConstant.BPM_STATUS)|| key.toLowerCase().equals(DataBaseConstant.BPM_STATUS_TABLE)) {
			returnValue = "1";
		}

		//后台任务获取用户信息异常，导致程序中断
		if(sysUser==null && user==null){
			return null;
		}
		
		//替换为系统登录用户帐号
		if (key.equals(DataBaseConstant.SYS_USER_CODE)|| key.toLowerCase().equals(DataBaseConstant.SYS_USER_CODE_TABLE)) {
			if(user==null) {
				returnValue = sysUser.getUsername();
			}else {
				returnValue = user.getSysUserCode();
			}
		}

		// 替换为系统登录用户ID
		else if (key.equals(DataBaseConstant.SYS_USER_ID) || key.equalsIgnoreCase(DataBaseConstant.SYS_USER_ID_TABLE)) {
			if(user==null) {
				returnValue = sysUser.getId();
			}else {
				returnValue = user.getSysUserId();
			}
		}

		//替换为系统登录用户真实名字
		else if (key.equals(DataBaseConstant.SYS_USER_NAME)|| key.toLowerCase().equals(DataBaseConstant.SYS_USER_NAME_TABLE)) {
			if(user==null) {
				returnValue = sysUser.getRealname();
			}else {
				returnValue = user.getSysUserName();
			}
		}
		
		//替换为系统用户登录所使用的机构编码
		else if (key.equals(DataBaseConstant.SYS_ORG_CODE)|| key.toLowerCase().equals(DataBaseConstant.SYS_ORG_CODE_TABLE)) {
			if(user==null) {
				returnValue = sysUser.getOrgCode();
			}else {
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
		else if (key.equals(DataBaseConstant.SYS_MULTI_ORG_CODE)|| key.toLowerCase().equals(DataBaseConstant.SYS_MULTI_ORG_CODE_TABLE)) {
			if(user==null){
				//TODO 暂时使用用户登录部门，存在逻辑缺陷，不是用户所拥有的部门
				returnValue = sysUser.getOrgCode();
			}else{
				if(user.isOneDepart()) {
					returnValue = user.getSysMultiOrgCode().get(0);
				}else {
					returnValue = Joiner.on(",").join(user.getSysMultiOrgCode());
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

		//update-begin-author:taoyan date:20210330 for:多租户ID作为系统变量
		else if (key.equals(TenantConstant.TENANT_ID) || key.toLowerCase().equals(TenantConstant.TENANT_ID_TABLE)){
			try {
				returnValue = SpringContextUtils.getHttpServletRequest().getHeader(CommonConstant.TENANT_ID);
			} catch (Exception e) {
				log.warn("获取系统租户异常：" + e.getMessage());
			}
		}
		//update-end-author:taoyan date:20210330 for:多租户ID作为系统变量
		if(returnValue!=null){returnValue = returnValue + moshi;}
		return returnValue;
	}
	
//	public static void main(String[] args) {
//		 String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NjUzMzY1MTMsInVzZXJuYW1lIjoiYWRtaW4ifQ.xjhud_tWCNYBOg_aRlMgOdlZoWFFKB_givNElHNw3X0";
//		 System.out.println(JwtUtil.getUsername(token));
//	}
}
