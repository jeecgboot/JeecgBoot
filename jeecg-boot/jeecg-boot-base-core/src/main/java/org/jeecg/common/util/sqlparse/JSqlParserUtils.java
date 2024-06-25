package org.jeecg.common.util.sqlparse;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.common.util.sqlparse.vo.SelectSqlInfo;

import java.io.StringReader;
import java.util.List;
import java.util.Map;

@Slf4j
public class JSqlParserUtils {

    /**
     * 解析 查询（select）sql的信息，
     * 此方法会展开所有子查询到一个map里，
     * key只存真实的表名，如果查询的没有真实的表名，则会被忽略。
     * value只存真实的字段名，如果查询的没有真实的字段名，则会被忽略。
     * <p>
     * 例如：SELECT a.*,d.age,(SELECT count(1) FROM sys_depart) AS count FROM (SELECT username AS foo, realname FROM sys_user) a, demo d
     * 解析后的结果为：{sys_user=[username, realname], demo=[age], sys_depart=[]}
     *
     * @param selectSql
     * @return
     */
    public static Map<String, SelectSqlInfo> parseAllSelectTable(String selectSql) throws JSQLParserException {
        if (oConvertUtils.isEmpty(selectSql)) {
            return null;
        }
        // log.info("解析查询Sql：{}", selectSql);
        JSqlParserAllTableManager allTableManager = new JSqlParserAllTableManager(selectSql);
        return allTableManager.parse();
    }

    /**
     * 解析 查询（select）sql的信息，子查询嵌套
     *
     * @param selectSql
     * @return
     */
    public static SelectSqlInfo parseSelectSqlInfo(String selectSql) throws JSQLParserException {
        if (oConvertUtils.isEmpty(selectSql)) {
            return null;
        }
        // log.info("解析查询Sql：{}", selectSql);
        // 使用 JSqlParer 解析sql
        // 1、创建解析器
        CCJSqlParserManager mgr = new CCJSqlParserManager();
        // 2、使用解析器解析sql生成具有层次结构的java类
        Statement stmt = mgr.parse(new StringReader(selectSql));
        if (stmt instanceof Select) {
            Select selectStatement = (Select) stmt;
            // 3、解析select查询sql的信息
            return JSqlParserUtils.parseBySelectBody(selectStatement.getSelectBody());
        } else {
            // 非 select 查询sql，不做处理
            throw new JeecgBootException("非 select 查询sql，不做处理");
        }
    }

    /**
     * 解析 select 查询sql的信息
     *
     * @param selectBody
     * @return
     */
    private static SelectSqlInfo parseBySelectBody(SelectBody selectBody) {
        // 简单的select查询
        if (selectBody instanceof PlainSelect) {
            SelectSqlInfo sqlInfo = new SelectSqlInfo(selectBody);
            PlainSelect plainSelect = (PlainSelect) selectBody;
            FromItem fromItem = plainSelect.getFromItem();
            // 解析 aliasName
            if (fromItem.getAlias() != null) {
                sqlInfo.setFromTableAliasName(fromItem.getAlias().getName());
            }
            // 解析 表名
            if (fromItem instanceof Table) {
                // 通过表名的方式from
                Table fromTable = (Table) fromItem;
                sqlInfo.setFromTableName(fromTable.getName());
            } else if (fromItem instanceof SubSelect) {
                // 通过子查询的方式from
                SubSelect fromSubSelect = (SubSelect) fromItem;
                SelectSqlInfo subSqlInfo = JSqlParserUtils.parseBySelectBody(fromSubSelect.getSelectBody());
                sqlInfo.setFromSubSelect(subSqlInfo);
            }
            // 解析 selectFields
            List<SelectItem> selectItems = plainSelect.getSelectItems();
            for (SelectItem selectItem : selectItems) {
                if (selectItem instanceof AllColumns || selectItem instanceof AllTableColumns) {
                    // 全部字段
                    sqlInfo.setSelectAll(true);
                    sqlInfo.setSelectFields(null);
                    sqlInfo.setRealSelectFields(null);
                    break;
                } else if (selectItem instanceof SelectExpressionItem) {
                    // 获取单个查询字段名
                    SelectExpressionItem selectExpressionItem = (SelectExpressionItem) selectItem;
                    Expression expression = selectExpressionItem.getExpression();
                    Alias alias = selectExpressionItem.getAlias();
                    JSqlParserUtils.handleExpression(sqlInfo, expression, alias);
                }
            }
            return sqlInfo;
        } else {
            log.warn("暂时尚未处理该类型的 SelectBody: {}", selectBody.getClass().getName());
            throw new JeecgBootException("暂时尚未处理该类型的 SelectBody");
        }
    }

    /**
     * 处理查询字段表达式
     *
     * @param sqlInfo
     * @param expression
     * @param alias      是否有别名，无传null
     */
    private static void handleExpression(SelectSqlInfo sqlInfo, Expression expression, Alias alias) {
        // 处理函数式字段  CONCAT(name,'(',age,')')
        if (expression instanceof Function) {
            JSqlParserUtils.handleFunctionExpression((Function) expression, sqlInfo);
            return;
        }
        // 处理字段上的子查询
        if (expression instanceof SubSelect) {
            SubSelect subSelect = (SubSelect) expression;
            SelectSqlInfo subSqlInfo = JSqlParserUtils.parseBySelectBody(subSelect.getSelectBody());
            // 注：字段上的子查询，必须只查询一个字段，否则会报错，所以可以放心合并
            sqlInfo.getSelectFields().addAll(subSqlInfo.getSelectFields());
            sqlInfo.getRealSelectFields().addAll(subSqlInfo.getAllRealSelectFields());
            return;
        }
        // 不处理字面量
        if (expression instanceof StringValue ||
                expression instanceof NullValue ||
                expression instanceof LongValue ||
                expression instanceof DoubleValue ||
                expression instanceof HexValue ||
                expression instanceof DateValue ||
                expression instanceof TimestampValue ||
                expression instanceof TimeValue
        ) {
            return;
        }

        // 查询字段名
        String selectField = expression.toString();
        // 实际查询字段名
        String realSelectField = selectField;
        // 判断是否有别名
        if (alias != null) {
            selectField = alias.getName();
        }
        // 获取真实字段名
        if (expression instanceof Column) {
            Column column = (Column) expression;
            realSelectField = column.getColumnName();
        }
        sqlInfo.addSelectField(selectField, realSelectField);
    }

    /**
     * 处理函数式字段
     *
     * @param functionExp
     * @param sqlInfo
     */
    private static void handleFunctionExpression(Function functionExp, SelectSqlInfo sqlInfo) {
        List<Expression> expressions = functionExp.getParameters().getExpressions();
        for (Expression expression : expressions) {
            JSqlParserUtils.handleExpression(sqlInfo, expression, null);
        }
    }

}
