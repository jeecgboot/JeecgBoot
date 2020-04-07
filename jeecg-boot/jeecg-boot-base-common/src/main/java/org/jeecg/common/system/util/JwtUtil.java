package org.jeecg.common.system.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.base.Joiner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.DataBaseConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.system.vo.SysUserCacheInfo;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;

/**
 * @Author Scott
 * @Date 2018-07-12 14:23
 * @Desc JWT工具类
 **/
public class JwtUtil {

	// Token过期时间30分钟（用户登录过期时间是此时间的两倍，以token在reids缓存时间为准）
	public static final long EXPIRE_TIME = 30 * 60 * 1000;

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
	 * 从session中获取变量
	 * 
	 * @param key
	 * @return
	 */
	public static String getSessionData(String key) {
		Matcher m = Pattern.compile(".*#\\{(.+)\\}.*").matcher(key);
		if (m.find()) {
			String acturalKey = m.group(1);
			HttpSession session = SpringContextUtils.getHttpServletRequest().getSession();
			String value = (String) session.getAttribute(acturalKey);
			if (value != null) {
				key = key.replace(String.format("#\\{%s\\}", acturalKey), value);
				return key;
			}
		}
		return null;
	}

	/**
	 * 从当前用户中获取变量
	 * 
	 * @param key
	 * @param user
	 * @return
	 */
	// TODO 急待改造 sckjkdsjsfjdk
	public static String getUserSystemData(String key, SysUserCacheInfo user) {
		if (user == null) {
			user = JeecgDataAutorUtils.loadUserInfo();
		}
		// 获取登录用户信息
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		Map<String, String> mappedValues = new HashMap<>();
		String value = null;
		if (user == null) {
			value = sysUser.getUsername();
		} else {
			value = user.getSysUserCode();
		}
		// 替换为系统登录用户帐号
		mappedValues.put(DataBaseConstant.SYS_USER_CODE, value);
		mappedValues.put(DataBaseConstant.SYS_USER_CODE_TABLE, value);
		// 替换为系统登录用户真实名字
		if (user == null) {
			value = sysUser.getRealname();
		} else {
			value = user.getSysUserName();
		}
		mappedValues.put(DataBaseConstant.SYS_USER_NAME, value);
		mappedValues.put(DataBaseConstant.SYS_USER_NAME_TABLE, value);

		// 替换为系统用户登录所使用的机构编码
		if (user == null) {
			value = sysUser.getOrgCode();
		} else {
			value = user.getSysOrgCode();
		}
		mappedValues.put(DataBaseConstant.SYS_ORG_CODE, value);
		mappedValues.put(DataBaseConstant.SYS_ORG_CODE_TABLE, value);

		// 替换为系统用户所拥有的所有机构编码
		if (user.isOneDepart()) {
			value = user.getSysMultiOrgCode().get(0);
		} else {
			value = Joiner.on(",").join(user.getSysMultiOrgCode());
		}
		mappedValues.put(DataBaseConstant.SYS_MULTI_ORG_CODE, value);
		mappedValues.put(DataBaseConstant.SYS_MULTI_ORG_CODE_TABLE, value);

		// 替换为当前系统时间(年月日)
		value = user.getSysDate();
		mappedValues.put(DataBaseConstant.SYS_DATE, value);
		mappedValues.put(DataBaseConstant.SYS_DATE_TABLE, value);

		// 替换为当前系统时间（年月日时分秒）
		value = user.getSysTime();
		mappedValues.put(DataBaseConstant.SYS_TIME, value);
		mappedValues.put(DataBaseConstant.SYS_TIME_TABLE, value);

		// 流程状态默认值（默认未发起）
		value = "1";
		mappedValues.put(DataBaseConstant.BPM_STATUS, value);
		mappedValues.put(DataBaseConstant.BPM_STATUS_TABLE, value);

		// 替换形如#{key}的值
		// for (String mappedKey : mappedValues.keySet()) {
		// 	key = key.replaceAll(String.format("#\\{%s\\}", mappedKey), mappedValues.get(mappedKey));
		// }
		// 优化替换逻辑
		Matcher m = Pattern.compile(".*?#\\{(.+?)\\}.*?").matcher(key);
		while(m.find()) {
			String mappedKey = m.group(1);
			key = key.replaceAll(String.format("#\\{%s\\}", mappedKey), mappedValues.get(mappedKey));
		}
		return key;
	}

	public static void main(String[] args) {
		String token =
				"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NjUzMzY1MTMsInVzZXJuYW1lIjoiYWRtaW4ifQ.xjhud_tWCNYBOg_aRlMgOdlZoWFFKB_givNElHNw3X0";
		System.out.println(JwtUtil.getUsername(token));
	}
}
