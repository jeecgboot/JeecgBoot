package org.jeecg.common.util;

import cn.hutool.core.util.ReUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.exception.JeecgSqlInjectionException;
import java.util.ArrayList;
import java.util.List;
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
	 * 默认—sql注入关键词
	 */
	private final static String XSS_STR = "and |exec |peformance_schema|information_schema|extractvalue|updatexml|geohash|gtid_subset|gtid_subtract|insert |select |delete |update |drop |count |chr |mid |master |truncate |char |declare |;|or |+|--";
	/**
	 * online报表专用—sql注入关键词
	 */
	private static String specialReportXssStr = "exec |peformance_schema|information_schema|extractvalue|updatexml|geohash|gtid_subset|gtid_subtract|insert |alter |delete |grant |update |drop |master |truncate |declare |--";
	/**
	 * 字典专用—sql注入关键词
	 */
	private static String specialDictSqlXssStr = "exec |peformance_schema|information_schema|extractvalue|updatexml|geohash|gtid_subset|gtid_subtract|insert |select |delete |update |drop |count |chr |mid |master |truncate |char |declare |;|+|--";
	/**
	 * 完整匹配的key，不需要考虑前空格
	 */
	private static List<String> FULL_MATCHING_KEYWRODS = new ArrayList<>();
	static {
		FULL_MATCHING_KEYWRODS.add(";");
		FULL_MATCHING_KEYWRODS.add("+");
		FULL_MATCHING_KEYWRODS.add("--");
	}
	
	
	/**
	 * sql注入风险的 正则关键字
	 *
	 * 函数匹配，需要用正则模式
	 */
	private final static String[] XSS_REGULAR_STR_ARRAY = new String[]{
			"chr\\s*\\(",
			"mid\\s*\\(",
			" char\\s*\\(",
			"sleep\\s*\\(",
			"user\\s*\\(",
			"show\\s+tables",
			"user[\\s]*\\([\\s]*\\)",
			"show\\s+databases",
			"sleep\\(\\d*\\)",
			"sleep\\(.*\\)",
	};
	/**
	 * sql注释的正则
	 */
	private final static Pattern SQL_ANNOTATION = Pattern.compile("/\\*[\\s\\S]*\\*/");
	private final static  String SQL_ANNOTATION2 = "--";
	
	/**
	 * sql注入提示语
	 */
	private final static String SQL_INJECTION_KEYWORD_TIP = "请注意，存在SQL注入关键词---> {}";
	private final static String SQL_INJECTION_TIP = "请注意，值可能存在SQL注入风险!--->";
	private final static String SQL_INJECTION_TIP_VARIABLE = "请注意，值可能存在SQL注入风险!---> {}";
	

	/**
	 * sql注入过滤处理，遇到注入关键字抛异常
	 * @param values
	 */
	public static void filterContentMulti(String... values) {
		filterContent(values, null);
	}

	/**
	 * 校验比较严格
	 * 
	 * sql注入过滤处理，遇到注入关键字抛异常
	 *
	 * @param value
	 * @return
	 */
	public static void filterContent(String value, String customXssString) {
		if (value == null || "".equals(value)) {
			return;
		}
		// 一、校验sql注释 不允许有sql注释
		checkSqlAnnotation(value);
		// 转为小写进行后续比较
		value = value.toLowerCase().trim();
		
		// 二、SQL注入检测存在绕过风险 (普通文本校验)
		//https://gitee.com/jeecg/jeecg-boot/issues/I4NZGE
		String[] xssArr = XSS_STR.split("\\|");
		for (int i = 0; i < xssArr.length; i++) {
			if (value.indexOf(xssArr[i]) > -1) {
				log.error(SqlInjectionUtil.SQL_INJECTION_KEYWORD_TIP, xssArr[i]);
				log.error(SqlInjectionUtil.SQL_INJECTION_TIP_VARIABLE, value);
				throw new JeecgSqlInjectionException(SqlInjectionUtil.SQL_INJECTION_TIP + value);
			}
		}
		// 三、SQL注入检测存在绕过风险 (自定义传入普通文本校验)
		if (customXssString != null) {
			String[] xssArr2 = customXssString.split("\\|");
			for (int i = 0; i < xssArr2.length; i++) {
				if (value.indexOf(xssArr2[i]) > -1) {
					log.error(SqlInjectionUtil.SQL_INJECTION_KEYWORD_TIP, xssArr2[i]);
					log.error(SqlInjectionUtil.SQL_INJECTION_TIP_VARIABLE, value);
					throw new JeecgSqlInjectionException(SqlInjectionUtil.SQL_INJECTION_TIP + value);
				}
			}
		}

		// 四、SQL注入检测存在绕过风险 (正则校验)
		for (String regularOriginal : XSS_REGULAR_STR_ARRAY) {
			String regular = ".*" + regularOriginal + ".*";
			if (Pattern.matches(regular, value)) {
				log.error(SqlInjectionUtil.SQL_INJECTION_KEYWORD_TIP, regularOriginal);
				log.error(SqlInjectionUtil.SQL_INJECTION_TIP_VARIABLE, value);
				throw new JeecgSqlInjectionException(SqlInjectionUtil.SQL_INJECTION_TIP + value);
			}
		}
		return;
	}

	/**
	 * 判断是否存在SQL注入关键词字符串
	 *
	 * @param keyword
	 * @return
	 */
	@SuppressWarnings("AlibabaUndefineMagicConstant")
	private static boolean isExistSqlInjectKeyword(String sql, String keyword) {
		if (sql.startsWith(keyword.trim())) {
			return true;
		} else if (sql.contains(keyword)) {
			// 需要匹配的，sql注入关键词
			String matchingText = " " + keyword;
			if(FULL_MATCHING_KEYWRODS.contains(keyword)){
				matchingText = keyword;
			}
			
			if (sql.contains(matchingText)) {
				return true;
			} else {
				String regularStr = "\\s+\\S+" + keyword;
				List<String> resultFindAll = ReUtil.findAll(regularStr, sql, 0, new ArrayList<String>());
				for (String res : resultFindAll) {
					log.info("isExistSqlInjectKeyword —- 匹配到的SQL注入关键词：{}", res);
					/**
					 * SQL注入中可以替换空格的字符(%09  %0A  %0D  +都可以替代空格)
					 * http://blog.chinaunix.net/uid-12501104-id-2932639.html
					 * https://www.cnblogs.com/Vinson404/p/7253255.html
					 * */
					if (res.contains("%") || res.contains("+") || res.contains("#") || res.contains("/") || res.contains(")")) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * sql注入过滤处理，遇到注入关键字抛异常
	 * 
	 * @param values
	 * @return
	 */
	public static void filterContent(String[] values, String customXssString) {
		for (String val : values) {
			if (oConvertUtils.isEmpty(val)) {
				return;
			}
			filterContent(val, customXssString);
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
	public static void specialFilterContentForDictSql(String value) {
		String[] xssArr = specialDictSqlXssStr.split("\\|");
		if (value == null || "".equals(value)) {
			return;
		}
		// 一、校验sql注释 不允许有sql注释
		checkSqlAnnotation(value);
		value = value.toLowerCase().trim();
		
		// 二、SQL注入检测存在绕过风险 (普通文本校验)
		for (int i = 0; i < xssArr.length; i++) {
			if (isExistSqlInjectKeyword(value, xssArr[i])) {
				log.error(SqlInjectionUtil.SQL_INJECTION_KEYWORD_TIP, xssArr[i]);
				log.error(SqlInjectionUtil.SQL_INJECTION_TIP_VARIABLE, value);
				throw new JeecgSqlInjectionException(SqlInjectionUtil.SQL_INJECTION_TIP + value);
			}
		}

		// 三、SQL注入检测存在绕过风险 (正则校验)
		for (String regularOriginal : XSS_REGULAR_STR_ARRAY) {
			String regular = ".*" + regularOriginal + ".*";
			if (Pattern.matches(regular, value)) {
				log.error(SqlInjectionUtil.SQL_INJECTION_KEYWORD_TIP, regularOriginal);
				log.error(SqlInjectionUtil.SQL_INJECTION_TIP_VARIABLE, value);
				throw new JeecgSqlInjectionException(SqlInjectionUtil.SQL_INJECTION_TIP + value);
			}
		}
		return;
	}

    /**
	 * 【提醒：不通用】
     *  仅用于Online报表SQL解析，注入过滤
     * @param value
     * @return
     */
	public static void specialFilterContentForOnlineReport(String value) {
		String[] xssArr = specialReportXssStr.split("\\|");
		if (value == null || "".equals(value)) {
			return;
		}
		// 一、校验sql注释 不允许有sql注释
		checkSqlAnnotation(value);
		value = value.toLowerCase().trim();
		
		// 二、SQL注入检测存在绕过风险 (普通文本校验)
		for (int i = 0; i < xssArr.length; i++) {
			if (isExistSqlInjectKeyword(value, xssArr[i])) {
				log.error(SqlInjectionUtil.SQL_INJECTION_KEYWORD_TIP, xssArr[i]);
				log.error(SqlInjectionUtil.SQL_INJECTION_TIP_VARIABLE, value);
				throw new JeecgSqlInjectionException(SqlInjectionUtil.SQL_INJECTION_TIP + value);
			}
		}

		// 三、SQL注入检测存在绕过风险 (正则校验)
		for (String regularOriginal : XSS_REGULAR_STR_ARRAY) {
			String regular = ".*" + regularOriginal + ".*";
			if (Pattern.matches(regular, value)) {
				log.error(SqlInjectionUtil.SQL_INJECTION_KEYWORD_TIP, regularOriginal);
				log.error(SqlInjectionUtil.SQL_INJECTION_TIP_VARIABLE, value);
				throw new JeecgSqlInjectionException(SqlInjectionUtil.SQL_INJECTION_TIP + value);
			}
		}
		return;
	}


	/**
	 * 校验是否有sql注释 
	 * @return
	 */
	public static void checkSqlAnnotation(String str){
		if(str.contains(SQL_ANNOTATION2)){
			String error = "请注意，SQL中不允许含注释，有安全风险！";
			log.error(error);
			throw new RuntimeException(error);
		}

		
		Matcher matcher = SQL_ANNOTATION.matcher(str);
		if(matcher.find()){
			String error = "请注意，值可能存在SQL注入风险---> \\*.*\\";
			log.error(error);
			throw new JeecgSqlInjectionException(error);
		}
	}


	/**
	 * 返回查询表名
	 * <p>
	 * sql注入过滤处理，遇到注入关键字抛异常
	 *
	 * @param table
	 */
	private static Pattern tableNamePattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_\\$]{0,63}$");
	public static String getSqlInjectTableName(String table) {
		if(oConvertUtils.isEmpty(table)){
			return table;
		}

		//update-begin---author:scott ---date:2024-05-28  for：表单设计器列表翻译存在表名带条件，导致翻译出问题----
		int index = table.toLowerCase().indexOf(" where ");
		if (index != -1) {
			table = table.substring(0, index);
			log.info("截掉where之后的新表名：" + table);
		}
		//update-end---author:scott ---date::2024-05-28  for：表单设计器列表翻译存在表名带条件，导致翻译出问题----

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
		filterContentMulti(table);
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
		if(oConvertUtils.isEmpty(field)){
			return field;
		}
		
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
		filterContentMulti(field);
		return field;
	}

	/**
	 * 获取多个字段
	 * 返回: 逗号拼接
	 *
	 * @param fields
	 * @return
	 */
	public static String getSqlInjectField(String... fields) {
		for (String s : fields) {
			getSqlInjectField(s);
		}
		return String.join(SymbolConstant.COMMA, fields);
	}


	/**
	 * 获取排序字段
	 * 返回：字符串
	 *
	 * 1.将驼峰命名转化成下划线 
	 * 2.限制sql注入
	 * @param sortField  排序字段
	 * @return
	 */
	public static String getSqlInjectSortField(String sortField) {
		String field = SqlInjectionUtil.getSqlInjectField(oConvertUtils.camelToUnderline(sortField));
		return field;
	}

	/**
	 * 获取多个排序字段
	 * 返回：数组
	 *
	 * 1.将驼峰命名转化成下划线 
	 * 2.限制sql注入
	 * @param sortFields 多个排序字段
	 * @return
	 */
	public static List getSqlInjectSortFields(String... sortFields) {
		List list = new ArrayList<String>();
		for (String sortField : sortFields) {
			list.add(getSqlInjectSortField(sortField));
		}
		return list;
	}

	/**
	 * 获取 orderBy type
	 * 返回：字符串
	 * <p>
	 * 1.检测是否为 asc 或 desc 其中的一个
	 * 2.限制sql注入
	 *
	 * @param orderType
	 * @return
	 */
	public static String getSqlInjectOrderType(String orderType) {
		if (orderType == null) {
			return null;
		}
		orderType = orderType.trim();
		if (CommonConstant.ORDER_TYPE_ASC.equalsIgnoreCase(orderType)) {
			return CommonConstant.ORDER_TYPE_ASC;
		} else {
			return CommonConstant.ORDER_TYPE_DESC;
		}
	}

}
