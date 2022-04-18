package org.jeecg.common.util;

import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.exception.JeecgBootException;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * sql注入处理工具类
 * 
 * @author zhoujf
 */
@Slf4j
public class SqlInjectionUtil {
	/**
	 * sign 用于表字典加签的盐值【SQL漏洞】
	 * （上线修改值 20200501，同步修改前端的盐值）
	 */
	private final static String TABLE_DICT_SIGN_SALT = "20200501";
	private final static String XSS_STR = "and |exec |insert |select |delete |update |drop |count |chr |mid |master |truncate |char |declare |;|or |+|user()";

    /**show tables*/
	private final static String SHOW_TABLES = "show\\s+tables";

	/**
	 * 针对表字典进行额外的sign签名校验（增加安全机制）
	 * @param dictCode:
	 * @param sign:
	 * @param request:
	 * @Return: void
	 */
	public static void checkDictTableSign(String dictCode, String sign, HttpServletRequest request) {
		//表字典SQL注入漏洞,签名校验
		String accessToken = request.getHeader("X-Access-Token");
		String signStr = dictCode + SqlInjectionUtil.TABLE_DICT_SIGN_SALT + accessToken;
		String javaSign = SecureUtil.md5(signStr);
		if (!javaSign.equals(sign)) {
			log.error("表字典，SQL注入漏洞签名校验失败 ：" + sign + "!=" + javaSign+ ",dictCode=" + dictCode);
			throw new JeecgBootException("无权限访问！");
		}
		log.info(" 表字典，SQL注入漏洞签名校验成功！sign=" + sign + ",dictCode=" + dictCode);
	}


	/**
	 * sql注入过滤处理，遇到注入关键字抛异常
	 * 
	 * @param value
	 * @return
	 */
	public static void filterContent(String value) {
		if (value == null || "".equals(value)) {
			return;
		}
		// 统一转为小写
		value = value.toLowerCase();
		//SQL注入检测存在绕过风险 https://gitee.com/jeecg/jeecg-boot/issues/I4NZGE
		value = value.replaceAll("/\\*.*\\*/","");

		String[] xssArr = XSS_STR.split("\\|");
		for (int i = 0; i < xssArr.length; i++) {
			if (value.indexOf(xssArr[i]) > -1) {
				log.error("请注意，存在SQL注入关键词---> {}", xssArr[i]);
				log.error("请注意，值可能存在SQL注入风险!---> {}", value);
				throw new RuntimeException("请注意，值可能存在SQL注入风险!--->" + value);
			}
		}
		if(Pattern.matches(SHOW_TABLES, value)){
			throw new RuntimeException("请注意，值可能存在SQL注入风险!--->" + value);
		}
		return;
	}

	/**
	 * sql注入过滤处理，遇到注入关键字抛异常
	 * 
	 * @param values
	 * @return
	 */
	public static void filterContent(String[] values) {
		String[] xssArr = XSS_STR.split("\\|");
		for (String value : values) {
			if (value == null || "".equals(value)) {
				return;
			}
			// 统一转为小写
			value = value.toLowerCase();
			//SQL注入检测存在绕过风险 https://gitee.com/jeecg/jeecg-boot/issues/I4NZGE
			value = value.replaceAll("/\\*.*\\*/","");

			for (int i = 0; i < xssArr.length; i++) {
				if (value.indexOf(xssArr[i]) > -1) {
					log.error("请注意，存在SQL注入关键词---> {}", xssArr[i]);
					log.error("请注意，值可能存在SQL注入风险!---> {}", value);
					throw new RuntimeException("请注意，值可能存在SQL注入风险!--->" + value);
				}
			}
			if(Pattern.matches(SHOW_TABLES, value)){
				throw new RuntimeException("请注意，值可能存在SQL注入风险!--->" + value);
			}
		}
		return;
	}

	/**
	 * 【提醒：不通用】
	 * 仅用于字典条件SQL参数，注入过滤
	 *
	 * @param value
	 * @return
	 */
	//@Deprecated
	public static void specialFilterContent(String value) {
		String specialXssStr = " exec | insert | select | delete | update | drop | count | chr | mid | master | truncate | char | declare |;|+|";
		String[] xssArr = specialXssStr.split("\\|");
		if (value == null || "".equals(value)) {
			return;
		}
		// 统一转为小写
		value = value.toLowerCase();
		//SQL注入检测存在绕过风险 https://gitee.com/jeecg/jeecg-boot/issues/I4NZGE
		value = value.replaceAll("/\\*.*\\*/","");

		for (int i = 0; i < xssArr.length; i++) {
			if (value.indexOf(xssArr[i]) > -1 || value.startsWith(xssArr[i].trim())) {
				log.error("请注意，存在SQL注入关键词---> {}", xssArr[i]);
				log.error("请注意，值可能存在SQL注入风险!---> {}", value);
				throw new RuntimeException("请注意，值可能存在SQL注入风险!--->" + value);
			}
		}
		if(Pattern.matches(SHOW_TABLES, value)){
			throw new RuntimeException("请注意，值可能存在SQL注入风险!--->" + value);
		}
		return;
	}


    /**
	 * 【提醒：不通用】
     *  仅用于Online报表SQL解析，注入过滤
     * @param value
     * @return
     */
	//@Deprecated
	public static void specialFilterContentForOnlineReport(String value) {
		String specialXssStr = " exec | insert | delete | update | drop | chr | mid | master | truncate | char | declare |";
		String[] xssArr = specialXssStr.split("\\|");
		if (value == null || "".equals(value)) {
			return;
		}
		// 统一转为小写
		value = value.toLowerCase();
		//SQL注入检测存在绕过风险 https://gitee.com/jeecg/jeecg-boot/issues/I4NZGE
		value = value.replaceAll("/\\*.*\\*/","");

		for (int i = 0; i < xssArr.length; i++) {
			if (value.indexOf(xssArr[i]) > -1 || value.startsWith(xssArr[i].trim())) {
				log.error("请注意，存在SQL注入关键词---> {}", xssArr[i]);
				log.error("请注意，值可能存在SQL注入风险!---> {}", value);
				throw new RuntimeException("请注意，值可能存在SQL注入风险!--->" + value);
			}
		}

		if(Pattern.matches(SHOW_TABLES, value)){
			throw new RuntimeException("请注意，值可能存在SQL注入风险!--->" + value);
		}
		return;
	}

}
