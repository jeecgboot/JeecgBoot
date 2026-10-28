package org.jeecg.common.util;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.exception.JeecgSqlInjectionException;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * 系统敏感表字段校验工具。
 *
 * @author scott
 * @since 2026-08-25 issues/9840 防止通过前端字典条件探测系统敏感信息
 */
@Slf4j
public final class SensitiveTableCheckUtil {

	private static final String SQL_INJECTION_TIP = "请注意，值可能存在SQL注入风险!--->";
	private static final Map<String, Set<String>> SENSITIVE_FIELDS = Map.of(
			"sys_user", Set.of("password", "salt"),
			"sys_data_source", Set.of("db_url", "db_username", "db_password"),
			"open_api_auth", Set.of("ak", "sk"),
			"airag_model", Set.of("credential")
	);

	private SensitiveTableCheckUtil() {
	}

	/**
	 * 校验字典查询是否访问禁止字段。
	 *
	 * @param table 查询表名
	 * @param fields 查询字段
	 */
	public static void checkForbiddenFields(String table, String... fields) {
		//update-begin---author:scott ---date:20261027  for：【GHSA-g87f-mgx5-wj9c】isEmpty会把字面量null当空直接放行，改为拒绝-----------
		if (table != null && "null".equalsIgnoreCase(table.trim())) {
			log.error("字典查询表名非法：{}", table);
			throw new JeecgSqlInjectionException(SQL_INJECTION_TIP + table);
		}
		//update-end---author:scott ---date:20261027  for：【GHSA-g87f-mgx5-wj9c】isEmpty会把字面量null当空直接放行，改为拒绝-----------
		if (oConvertUtils.isEmpty(table) || fields == null || fields.length == 0) {
			return;
		}
		String tableName = getTableName(table);
		Set<String> sensitiveFields = SENSITIVE_FIELDS.get(tableName);
		if (sensitiveFields == null) {
			return;
		}

		for (String field : fields) {
			if (oConvertUtils.isEmpty(field)) {
				continue;
			}
			for (String fieldItem : field.split(",")) {
				checkSensitiveField(tableName, sensitiveFields, fieldItem);
			}
		}
	}

	private static void checkSensitiveField(String tableName, Set<String> sensitiveFields, String field) {
		String fieldName = normalizeName(field);
		if ("*".equals(fieldName) || sensitiveFields.contains(fieldName)) {
			log.error("字典查询不允许使用敏感字段：{}.{}", tableName, fieldName);
			throw new JeecgSqlInjectionException(SQL_INJECTION_TIP + tableName + "." + fieldName);
		}
	}

	/**
	 * 提取 where 条件之前的真实表名。
	 */
	private static String getTableName(String table) {
		String tableName = table.trim().split("(?i)\\s+where\\s+", 2)[0].trim();
		return normalizeName(tableName.split("\\s+", 2)[0]);
	}

	/**
	 * 统一表名，兼容大小写及限定名前缀。
	 *
	 * @param name 表名
	 * @return 标准名称
	 */
	private static String normalizeName(String name) {
		String normalizedName = name.trim().replace("`", "").replace("\"", "").toLowerCase(Locale.ROOT);
		int separatorIndex = normalizedName.lastIndexOf('.');
		return separatorIndex >= 0 ? normalizedName.substring(separatorIndex + 1) : normalizedName;
	}
}
