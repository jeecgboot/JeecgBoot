package org.jeecg.common.util;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.SignedExpression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.parser.CCJSqlParserConstants;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 前端字典条件 SQL 结构校验工具。
 *
 * @author scott
 * @since 2026-08-25 issues/9840 字典过滤条件 SQL 注入防护
 */
public final class DictSqlConditionCheckUtil {

	/**
	 * 条件字段名：字母/数字/下划线组成的标识符，可带反引号或双引号，可带表别名前缀
	 */
	private static final Pattern COLUMN_NAME_PATTERN = Pattern.compile(
			"[`\"]?[\\p{L}_][\\p{L}\\p{N}_]*[`\"]?(\\.[`\"]?[\\p{L}_][\\p{L}\\p{N}_]*[`\"]?)*");

	private DictSqlConditionCheckUtil() {
	}

	/**
	 * 校验字典条件语法并返回条件中使用的字段。
	 *
	 * @param value 字典过滤条件
	 * @return 条件字段集合
	 * @throws IllegalArgumentException 条件包含不支持或不安全的 SQL 结构
	 */
	public static Set<String> checkAndGetFields(String value) {
		if (value == null || value.trim().isEmpty()) {
			return Set.of();
		}
		String trimmed = value.trim();
		Set<String> conditionFields = new LinkedHashSet<>();
		try {
			//update-begin---author:scott ---date:20261027  for：【GHSA-jgwm-2xxm-mmm4/GHSA-xmw4-rx44-4jcp】禁止反斜杠，JSqlParser不把\当转义符而MySQL会，两者对字符串边界理解不一致可绕过校验-----------
			if (trimmed.indexOf('\\') >= 0) {
				throw new IllegalArgumentException("字典过滤条件不允许包含反斜杠");
			}
			//update-end---author:scott ---date:20261027  for：【GHSA-jgwm-2xxm-mmm4/GHSA-xmw4-rx44-4jcp】禁止反斜杠，JSqlParser不把\当转义符而MySQL会，两者对字符串边界理解不一致可绕过校验-----------
			if (containsSqlComment(trimmed)) {
				throw new IllegalArgumentException("字典过滤条件不允许包含SQL注释");
			}
			int orderByIndex = findTopLevelOrderBy(trimmed);
			String conditionSql = orderByIndex < 0 ? trimmed : trimmed.substring(0, orderByIndex).trim();
			String orderBySql = orderByIndex < 0 ? null
					: trimmed.substring(orderByIndex).replaceFirst("(?i)^order\\s+by\\s+", "").trim();
			if ((!conditionSql.isEmpty() && !validateCondition(conditionSql, conditionFields))
					|| (orderBySql != null && !validateOrderBy(orderBySql, conditionFields))) {
				throw new IllegalArgumentException("不支持的字典过滤条件");
			}
			return conditionFields;
		} catch (Exception e) {
			throw new IllegalArgumentException("不支持的字典过滤条件", e);
		}
	}

	private static boolean validateCondition(String value, Set<String> conditionFields) throws Exception {
		CCJSqlParser parser = CCJSqlParserUtil.newParser(value);
		Expression expression = parser.Expression();
		return parser.getNextToken().kind == CCJSqlParserConstants.EOF
				&& validateExpression(expression, conditionFields);
	}

	private static boolean validateOrderBy(String value, Set<String> conditionFields) throws Exception {
		if (value.isEmpty()) {
			return false;
		}
		for (String orderItem : value.split(",")) {
			String[] parts = orderItem.trim().split("\\s+");
			if (parts.length == 0 || parts.length > 2
					|| (parts.length == 2 && !"ASC".equalsIgnoreCase(parts[1]) && !"DESC".equalsIgnoreCase(parts[1]))) {
				return false;
			}
			CCJSqlParser parser = CCJSqlParserUtil.newParser(parts[0]);
			Expression expression = parser.Expression();
			if (parser.getNextToken().kind != CCJSqlParserConstants.EOF
					|| !addConditionField(expression, conditionFields)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 定位字符串和括号之外的 ORDER BY，避免误判条件值中的普通文本。
	 */
	private static int findTopLevelOrderBy(String value) {
		char quote = 0;
		int depth = 0;
		for (int i = 0; i < value.length(); i++) {
			char current = value.charAt(i);
			if (quote != 0) {
				if (current == '\\' && i + 1 < value.length()) {
					i++;
				} else if (current == quote) {
					if (i + 1 < value.length() && value.charAt(i + 1) == quote) {
						i++;
					} else {
						quote = 0;
					}
				}
				continue;
			}
			if (current == '\'' || current == '"' || current == '`') {
				quote = current;
			} else if (current == '(') {
				depth++;
			} else if (current == ')') {
				depth--;
			} else if (depth == 0 && isKeyword(value, i, "order")) {
				int byIndex = i + "order".length();
				if (byIndex < value.length() && Character.isWhitespace(value.charAt(byIndex))) {
					while (byIndex < value.length() && Character.isWhitespace(value.charAt(byIndex))) {
						byIndex++;
					}
					if (isKeyword(value, byIndex, "by")) {
						return i;
					}
				}
			}
		}
		return -1;
	}

	private static boolean isKeyword(String value, int start, String keyword) {
		if (start < 0 || start + keyword.length() > value.length()
				|| !value.regionMatches(true, start, keyword, 0, keyword.length())) {
			return false;
		}
		boolean leftBoundary = start == 0 || !Character.isJavaIdentifierPart(value.charAt(start - 1));
		int end = start + keyword.length();
		boolean rightBoundary = end == value.length() || !Character.isJavaIdentifierPart(value.charAt(end));
		return leftBoundary && rightBoundary;
	}

	/**
	 * 仅识别字符串和引用标识符之外的 SQL 注释，避免误伤普通文本中的注释符号。
	 */
	private static boolean containsSqlComment(String value) {
		char quote = 0;
		for (int i = 0; i < value.length(); i++) {
			char current = value.charAt(i);
			if (quote != 0) {
				if (current == '\\' && i + 1 < value.length()) {
					i++;
				} else if (current == quote) {
					if (i + 1 < value.length() && value.charAt(i + 1) == quote) {
						i++;
					} else {
						quote = 0;
					}
				}
				continue;
			}
			if (current == '\'' || current == '"' || current == '`') {
				quote = current;
				continue;
			}
			//update-begin---author:scott ---date:20261027  for：【GHSA-xmw4-rx44-4jcp】MySQL的#也是行注释-----------
			if (current == '#') {
				return true;
			}
			//update-end---author:scott ---date:20261027  for：【GHSA-xmw4-rx44-4jcp】MySQL的#也是行注释-----------
			if (i + 1 < value.length()
					&& ((current == '-' && value.charAt(i + 1) == '-')
					|| (current == '/' && value.charAt(i + 1) == '*'))) {
				return true;
			}
		}
		return false;
	}

	private static boolean validateExpression(Expression expression, Set<String> conditionFields) {
		if (expression instanceof ParenthesedExpressionList<?> expressionList) {
			return expressionList.size() == 1 && validateExpression(expressionList.get(0), conditionFields);
		}
		if (expression instanceof AndExpression || expression instanceof OrExpression) {
			BinaryExpression logicalExpression = (BinaryExpression) expression;
			return validateExpression(logicalExpression.getLeftExpression(), conditionFields)
					&& validateExpression(logicalExpression.getRightExpression(), conditionFields);
		}
		if (isComparisonExpression(expression)) {
			BinaryExpression comparison = (BinaryExpression) expression;
			return addConditionField(comparison.getLeftExpression(), conditionFields)
					&& isLiteral(comparison.getRightExpression());
		}
		if (expression instanceof LikeExpression likeExpression) {
			return addConditionField(likeExpression.getLeftExpression(), conditionFields)
					&& likeExpression.getRightExpression() instanceof StringValue
					&& (likeExpression.getEscape() == null || likeExpression.getEscape() instanceof StringValue);
		}
		if (expression instanceof InExpression inExpression) {
			return addConditionField(inExpression.getLeftExpression(), conditionFields)
					&& isLiteralList(inExpression.getRightExpression());
		}
		if (expression instanceof Between between) {
			return addConditionField(between.getLeftExpression(), conditionFields)
					&& isLiteral(between.getBetweenExpressionStart())
					&& isLiteral(between.getBetweenExpressionEnd());
		}
		if (expression instanceof IsNullExpression isNullExpression) {
			return addConditionField(isNullExpression.getLeftExpression(), conditionFields);
		}
		return false;
	}

	private static boolean isComparisonExpression(Expression expression) {
		return expression instanceof EqualsTo
				|| expression instanceof NotEqualsTo
				|| expression instanceof GreaterThan
				|| expression instanceof GreaterThanEquals
				|| expression instanceof MinorThan
				|| expression instanceof MinorThanEquals;
	}

	private static boolean addConditionField(Expression expression, Set<String> conditionFields) {
		if (!(expression instanceof Column column)) {
			return false;
		}
		//update-begin---author:scott ---date:20261027  for：【GHSA-xmw4-rx44-4jcp】字段名只允许普通标识符，防止password#之类绕过敏感字段检查-----------
		if (!COLUMN_NAME_PATTERN.matcher(column.getFullyQualifiedName()).matches()) {
			return false;
		}
		//update-end---author:scott ---date:20261027  for：【GHSA-xmw4-rx44-4jcp】字段名只允许普通标识符，防止password#之类绕过敏感字段检查-----------
		conditionFields.add(column.getFullyQualifiedName());
		return true;
	}

	private static boolean isLiteralList(Expression expression) {
		if (!(expression instanceof ExpressionList<?> expressionList) || expressionList.isEmpty()) {
			return false;
		}
		return expressionList.stream().allMatch(DictSqlConditionCheckUtil::isLiteral);
	}

	private static boolean isLiteral(Expression expression) {
		if (expression instanceof LongValue || expression instanceof DoubleValue || expression instanceof StringValue) {
			return true;
		}
		if (expression instanceof SignedExpression signedExpression) {
			char sign = signedExpression.getSign();
			Expression number = signedExpression.getExpression();
			return (sign == '-' || sign == '+') && (number instanceof LongValue || number instanceof DoubleValue);
		}
		return false;
	}
}
