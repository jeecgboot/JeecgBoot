package org.jeecg.common.util;

import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.exception.JeecgSqlInjectionException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.regex.Matcher;
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
	private final static String XSS_STR = "and |extractvalue|updatexml|geohash|gtid_subset|gtid_subtract|exec |insert |select |delete |update |drop |count |chr |mid |master |truncate |char |declare |;|or |+|user()";

	/**
	 * 正则 user() 匹配更严谨
	 */
	private final static String REGULAR_EXPRE_USER = "user[\\s]*\\([\\s]*\\)";
    /**正则 show tables*/
	private final static String SHOW_TABLES = "show\\s+tables";

	/**
	 * sleep函数
	 */
	private final static Pattern FUN_SLEEP = Pattern.compile("sleep\\(.*\\)", Pattern.CASE_INSENSITIVE);

	/**
	 * sql注释的正则
	 */
	private final static Pattern SQL_ANNOTATION = Pattern.compile("/\\*[\\s\\S]*\\*/");

	/**
	 * 针对表字典进行额外的sign签名校验（增加安全机制）
	 * @param dictCode:
	 * @param sign:
	 * @param request:
	 * @Return: void
	 */
	private static void checkDictTableSign(String dictCode, String sign, HttpServletRequest request) {
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
	 * 返回查询表名
	 * <p>
	 * sql注入过滤处理，遇到注入关键字抛异常
	 *
	 * @param table
	 */
	private static Pattern tableNamePattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{0,63}$");
	public static String getSqlInjectTableName(String table) {
		table = table.trim();
		/**
		 * 检验表名是否合法
		 *
		 * 表名只能由字母、数字和下划线组成。
		 * 表名必须以字母开头。
		 * 表名长度通常有限制，例如最多为 64 个字符。
		 */
		boolean isValidTableName = tableNamePattern.matcher(table).matches();
		if (!isValidTableName) {
			String errorMsg = "表名不合法，存在SQL注入风险!--->" + table;
			log.error(errorMsg);
			throw new JeecgSqlInjectionException(errorMsg);
		}

		//进一步验证是否存在SQL注入风险
		filterContent(table);
		return table;
	}


	/**
	 * 返回查询字段
	 * <p>
	 * sql注入过滤处理，遇到注入关键字抛异常
	 *
	 * @param field
	 */
	static final Pattern fieldPattern = Pattern.compile("^[a-zA-Z0-9_]+$");
	public static String getSqlInjectField(String field) {
		field = field.trim();

		if (field.contains(SymbolConstant.COMMA)) {
			return getSqlInjectField(field.split(SymbolConstant.COMMA));
		}
		
		/**
		 * 校验表字段是否有效
		 *
		 * 字段定义只能是是字母 数字 下划线的组合（不允许有空格、转义字符串等）
		 */
		boolean isValidField = fieldPattern.matcher(field).matches();
		if (!isValidField) {
			String errorMsg = "字段不合法，存在SQL注入风险!--->" + field;
			log.error(errorMsg);
			throw new JeecgSqlInjectionException(errorMsg);
		}

		//进一步验证是否存在SQL注入风险
		filterContent(field);
		return field;
	}

	public static String getSqlInjectField(String... fields) {
		for (String s : fields) {
			getSqlInjectField(s);
		}
		return String.join(SymbolConstant.COMMA, fields);
	}

	/**
	 * sql注入过滤处理，遇到注入关键字抛异常
	 * 
	 * @param value
	 * @return
	 */
	public static void filterContent(String value, String customXssString) {
		if (value == null || "".equals(value)) {
			return;
		}
		// 校验sql注释 不允许有sql注释
		checkSqlAnnotation(value);
		// 统一转为小写
		value = value.toLowerCase();
		//SQL注入检测存在绕过风险 https://gitee.com/jeecg/jeecg-boot/issues/I4NZGE
		//value = value.replaceAll("/\\*.*\\*/","");

		String[] xssArr = XSS_STR.split("\\|");
		for (int i = 0; i < xssArr.length; i++) {
			if (value.indexOf(xssArr[i]) > -1) {
				log.error("请注意，存在SQL注入关键词---> {}", xssArr[i]);
				log.error("请注意，值可能存在SQL注入风险!---> {}", value);
				throw new JeecgSqlInjectionException("请注意，值可能存在SQL注入风险!--->" + value);
			}
		}
		//update-begin-author:taoyan date:2022-7-13 for: 除了XSS_STR这些提前设置好的，还需要额外的校验比如 单引号
		if (customXssString != null) {
			String[] xssArr2 = customXssString.split("\\|");
			for (int i = 0; i < xssArr2.length; i++) {
				if (value.indexOf(xssArr2[i]) > -1) {
					log.error("请注意，存在SQL注入关键词---> {}", xssArr2[i]);
					log.error("请注意，值可能存在SQL注入风险!---> {}", value);
					throw new JeecgSqlInjectionException("请注意，值可能存在SQL注入风险!--->" + value);
				}
			}
		}
		//update-end-author:taoyan date:2022-7-13 for: 除了XSS_STR这些提前设置好的，还需要额外的校验比如 单引号
		if(Pattern.matches(SHOW_TABLES, value) || Pattern.matches(REGULAR_EXPRE_USER, value)){
			throw new JeecgSqlInjectionException("请注意，值可能存在SQL注入风险!--->" + value);
		}
		return;
	}

	/**
	 * sql注入过滤处理，遇到注入关键字抛异常
	 * @param values
	 */
	public static void filterContent(String... values) {
		filterContent(values, null);
	}

	/**
	 * sql注入过滤处理，遇到注入关键字抛异常
	 * 
	 * @param values
	 * @return
	 */
	public static void filterContent(String[] values, String customXssString) {
		String[] xssArr = XSS_STR.split("\\|");
		for (String value : values) {
			if (value == null || "".equals(value)) {
				return;
			}
			// 校验sql注释 不允许有sql注释
			checkSqlAnnotation(value);
			// 统一转为小写
			value = value.toLowerCase();
			//SQL注入检测存在绕过风险 https://gitee.com/jeecg/jeecg-boot/issues/I4NZGE
			//value = value.replaceAll("/\\*.*\\*/","");

			for (int i = 0; i < xssArr.length; i++) {
				if (value.indexOf(xssArr[i]) > -1) {
					log.error("请注意，存在SQL注入关键词---> {}", xssArr[i]);
					log.error("请注意，值可能存在SQL注入风险!---> {}", value);
					throw new JeecgSqlInjectionException("请注意，值可能存在SQL注入风险!--->" + value);
				}
			}
			//update-begin-author:taoyan date:2022-7-13 for: 除了XSS_STR这些提前设置好的，还需要额外的校验比如 单引号
			if (customXssString != null) {
				String[] xssArr2 = customXssString.split("\\|");
				for (int i = 0; i < xssArr2.length; i++) {
					if (value.indexOf(xssArr2[i]) > -1) {
						log.error("请注意，存在SQL注入关键词---> {}", xssArr2[i]);
						log.error("请注意，值可能存在SQL注入风险!---> {}", value);
						throw new JeecgSqlInjectionException("请注意，值可能存在SQL注入风险!--->" + value);
					}
				}
			}
			//update-end-author:taoyan date:2022-7-13 for: 除了XSS_STR这些提前设置好的，还需要额外的校验比如 单引号
			if(Pattern.matches(SHOW_TABLES, value) || Pattern.matches(REGULAR_EXPRE_USER, value)){
				throw new JeecgSqlInjectionException("请注意，值可能存在SQL注入风险!--->" + value);
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
	public static void specialFilterContentForDictSql(String value) {
		String specialXssStr = " exec |extractvalue|updatexml|geohash|gtid_subset|gtid_subtract| insert | select | delete | update | drop | count | chr | mid | master | truncate | char | declare |;|+|user()";
		String[] xssArr = specialXssStr.split("\\|");
		if (value == null || "".equals(value)) {
			return;
		}
		// 校验sql注释 不允许有sql注释
		checkSqlAnnotation(value);
		// 统一转为小写
		value = value.toLowerCase();
		//SQL注入检测存在绕过风险 https://gitee.com/jeecg/jeecg-boot/issues/I4NZGE
		//value = value.replaceAll("/\\*.*\\*/","");

		for (int i = 0; i < xssArr.length; i++) {
			if (value.indexOf(xssArr[i]) > -1 || value.startsWith(xssArr[i].trim())) {
				log.error("请注意，存在SQL注入关键词---> {}", xssArr[i]);
				log.error("请注意，值可能存在SQL注入风险!---> {}", value);
				throw new JeecgSqlInjectionException("请注意，值可能存在SQL注入风险!--->" + value);
			}
		}
		if(Pattern.matches(SHOW_TABLES, value) || Pattern.matches(REGULAR_EXPRE_USER, value)){
			throw new JeecgSqlInjectionException("请注意，值可能存在SQL注入风险!--->" + value);
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
		String specialXssStr = " exec |extractvalue|updatexml|geohash|gtid_subset|gtid_subtract| insert | delete | update | drop | chr | mid | master | truncate | char | declare |user()";
		String[] xssArr = specialXssStr.split("\\|");
		if (value == null || "".equals(value)) {
			return;
		}
		// 校验sql注释 不允许有sql注释
		checkSqlAnnotation(value);
		// 统一转为小写
		value = value.toLowerCase();
		//SQL注入检测存在绕过风险 https://gitee.com/jeecg/jeecg-boot/issues/I4NZGE
		//value = value.replaceAll("/\\*.*\\*/"," ");

		for (int i = 0; i < xssArr.length; i++) {
			if (value.indexOf(xssArr[i]) > -1 || value.startsWith(xssArr[i].trim())) {
				log.error("请注意，存在SQL注入关键词---> {}", xssArr[i]);
				log.error("请注意，值可能存在SQL注入风险!---> {}", value);
				throw new JeecgSqlInjectionException("请注意，值可能存在SQL注入风险!--->" + value);
			}
		}

		if(Pattern.matches(SHOW_TABLES, value) || Pattern.matches(REGULAR_EXPRE_USER, value)){
			throw new JeecgSqlInjectionException("请注意，值可能存在SQL注入风险!--->" + value);
		}
		return;
	}


	/**
	 * 判断给定的字段是不是类中的属性
	 * @param field 字段名
	 * @param clazz 类对象
	 * @return
	 */
	public static boolean isClassField(String field, Class clazz){
		Field[] fields = clazz.getDeclaredFields();
		for(int i=0;i<fields.length;i++){
			String fieldName = fields[i].getName();
			String tableColumnName = oConvertUtils.camelToUnderline(fieldName);
			if(fieldName.equalsIgnoreCase(field) || tableColumnName.equalsIgnoreCase(field)){
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断给定的多个字段是不是类中的属性
	 * @param fieldSet 字段名set
	 * @param clazz 类对象
	 * @return
	 */
	public static boolean isClassField(Set<String> fieldSet, Class clazz){
		Field[] fields = clazz.getDeclaredFields();
		for(String field: fieldSet){
			boolean exist = false;
			for(int i=0;i<fields.length;i++){
				String fieldName = fields[i].getName();
				String tableColumnName = oConvertUtils.camelToUnderline(fieldName);
				if(fieldName.equalsIgnoreCase(field) || tableColumnName.equalsIgnoreCase(field)){
					exist = true;
					break;
				}
			}
			if(!exist){
				return false;
			}
		}
		return true;
	}

	/**
	 * 校验是否有sql注释 
	 * @return
	 */
	public static void checkSqlAnnotation(String str){
		Matcher matcher = SQL_ANNOTATION.matcher(str);
		if(matcher.find()){
			String error = "请注意，值可能存在SQL注入风险---> \\*.*\\";
			log.error(error);
			throw new JeecgSqlInjectionException(error);
		}
		
		// issues/4737 sys/duplicate/check SQL注入 #4737
		Matcher sleepMatcher = FUN_SLEEP.matcher(str);
		if(sleepMatcher.find()){
			String error = "请注意，值可能存在SQL注入风险---> sleep";
			log.error(error);
			throw new JeecgSqlInjectionException(error);
		}
	}
}
