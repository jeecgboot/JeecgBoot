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
import org.jeecg.common.util.sqlparse.vo.SelectSqlInfo;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析所有表名和字段的类
 */
@Slf4j
public class JSqlParserAllTableManager {

    private final String sql;
    private final Map<String, SelectSqlInfo> allTableMap = new HashMap<>();
    /**
     * 别名对应实际表名
     */
    private final Map<String, String> tableAliasMap = new HashMap<>();

    /**
     * 解析后的sql
     */
    private String parsedSql = null;

    JSqlParserAllTableManager(String selectSql) {
        this.sql = selectSql;
    }

    /**
     * 开始解析
     *
     * @return
     * @throws JSQLParserException
     */
    public Map<String, SelectSqlInfo> parse() throws JSQLParserException {
        // 1. 创建解析器
        CCJSqlParserManager mgr = new CCJSqlParserManager();
        // 2. 使用解析器解析sql生成具有层次结构的java类
        Statement stmt = mgr.parse(new StringReader(this.sql));
        if (stmt instanceof Select) {
            Select selectStatement = (Select) stmt;
            SelectBody selectBody = selectStatement.getSelectBody();
            this.parsedSql = selectBody.toString();
            // 3. 解析select查询sql的信息
            if (selectBody instanceof PlainSelect) {
                PlainSelect plainSelect = (PlainSelect) selectBody;
                // 4. 合并 fromItems
                List<FromItem> fromItems = new ArrayList<>();
                fromItems.add(plainSelect.getFromItem());
                // 4.1 处理join的表
                List<Join> joins = plainSelect.getJoins();
                if (joins != null) {
                    joins.forEach(join -> fromItems.add(join.getRightItem()));
                }
                // 5. 处理 fromItems
                for (FromItem fromItem : fromItems) {
                    // 5.1 通过表名的方式from
                    if (fromItem instanceof Table) {
                        this.addSqlInfoByTable((Table) fromItem);
                    }
                    // 5.2 通过子查询的方式from
                    else if (fromItem instanceof SubSelect) {
                        this.handleSubSelect((SubSelect) fromItem);
                    }
                }
                // 6. 解析 selectFields
                List<SelectItem> selectItems = plainSelect.getSelectItems();
                for (SelectItem selectItem : selectItems) {
                    // 6.1 查询的是全部字段
                    if (selectItem instanceof AllColumns) {
                        // 当 selectItem 为 AllColumns 时，fromItem 必定为 Table
                        String tableName = plainSelect.getFromItem(Table.class).getName();
                        // 此处必定不为空，因为在解析 fromItem 时，已经将表名添加到 allTableMap 中
                        SelectSqlInfo sqlInfo = this.allTableMap.get(tableName);
                        assert sqlInfo != null;
                        // 设置为查询全部字段
                        sqlInfo.setSelectAll(true);
                        sqlInfo.setSelectFields(null);
                        sqlInfo.setRealSelectFields(null);
                    }
                    // 6.2 查询的是带表别名（ u.* )的全部字段
                    else if (selectItem instanceof AllTableColumns) {
                        AllTableColumns allTableColumns = (AllTableColumns) selectItem;
                        String aliasName = allTableColumns.getTable().getName();
                        // 通过别名获取表名
                        String tableName = this.tableAliasMap.get(aliasName);
                        if (tableName == null) {
                            tableName = aliasName;
                        }
                        SelectSqlInfo sqlInfo = this.allTableMap.get(tableName);
                        // 如果此处为空，则说明该字段是通过子查询获取的，所以可以不处理，只有实际表才需要处理
                        if (sqlInfo != null) {
                            // 设置为查询全部字段
                            sqlInfo.setSelectAll(true);
                            sqlInfo.setSelectFields(null);
                            sqlInfo.setRealSelectFields(null);
                        }
                    }
                    // 6.3 各种字段表达式处理
                    else if (selectItem instanceof SelectExpressionItem) {
                        SelectExpressionItem selectExpressionItem = (SelectExpressionItem) selectItem;
                        Expression expression = selectExpressionItem.getExpression();
                        Alias alias = selectExpressionItem.getAlias();
                        this.handleExpression(expression, alias, plainSelect.getFromItem());
                    }
                }
            } else {
                log.warn("暂时尚未处理该类型的 SelectBody: {}", selectBody.getClass().getName());
                throw new JeecgBootException("暂时尚未处理该类型的 SelectBody");
            }
        } else {
            // 非 select 查询sql，不做处理
            throw new JeecgBootException("非 select 查询sql，不做处理");
        }
        return this.allTableMap;
    }

    /**
     * 处理子查询
     *
     * @param subSelect
     */
    private void handleSubSelect(SubSelect subSelect) {
        try {
            String subSelectSql = subSelect.getSelectBody().toString();
            // 递归调用解析
            Map<String, SelectSqlInfo> map = JSqlParserUtils.parseAllSelectTable(subSelectSql);
            if (map != null) {
                this.assignMap(map);
            }
        } catch (Exception e) {
            log.error("解析子查询出错", e);
        }
    }

    /**
     * 处理查询字段表达式
     *
     * @param expression
     */
    private void handleExpression(Expression expression, Alias alias, FromItem fromItem) {
        // 处理函数式字段  CONCAT(name,'(',age,')')
        if (expression instanceof Function) {
            Function functionExp = (Function) expression;
            List<Expression> expressions = functionExp.getParameters().getExpressions();
            for (Expression expItem : expressions) {
                this.handleExpression(expItem, null, fromItem);
            }
            return;
        }
        // 处理字段上的子查询
        if (expression instanceof SubSelect) {
            this.handleSubSelect((SubSelect) expression);
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

        // 处理字段
        if (expression instanceof Column) {
            Column column = (Column) expression;
            // 查询字段名
            String fieldName = column.getColumnName();
            String aliasName = fieldName;
            if (alias != null) {
                aliasName = alias.getName();
            }
            String tableName;
            if (column.getTable() != null) {
                // 通过列的表名获取 sqlInfo
                // 例如 user.name，这里的 tableName 就是 user
                tableName = column.getTable().getName();
                // 有可能是别名，需要转换为真实表名
                if (this.tableAliasMap.get(tableName) != null) {
                    tableName = this.tableAliasMap.get(tableName);
                }
            } else {
                // 当column的table为空时，说明是 fromItem 中的字段
                tableName = ((Table) fromItem).getName();
            }
            SelectSqlInfo $sqlInfo = this.allTableMap.get(tableName);
            if ($sqlInfo != null) {
                $sqlInfo.addSelectField(aliasName, fieldName);
            } else {
                log.warn("发生意外情况，未找到表名为 {} 的 SelectSqlInfo", tableName);
            }
        }
    }

    /**
     * 根据表名添加sqlInfo
     *
     * @param table
     */
    private void addSqlInfoByTable(Table table) {
        String tableName = table.getName();
        // 解析 aliasName
        if (table.getAlias() != null) {
            this.tableAliasMap.put(table.getAlias().getName(), tableName);
        }
        SelectSqlInfo sqlInfo = new SelectSqlInfo(this.parsedSql);
        sqlInfo.setFromTableName(table.getName());
        this.allTableMap.put(sqlInfo.getFromTableName(), sqlInfo);
    }

    /**
     * 合并map
     *
     * @param source
     */
    private void assignMap(Map<String, SelectSqlInfo> source) {
        for (Map.Entry<String, SelectSqlInfo> entry : source.entrySet()) {
            SelectSqlInfo sqlInfo = this.allTableMap.get(entry.getKey());
            if (sqlInfo == null) {
                this.allTableMap.put(entry.getKey(), entry.getValue());
            } else {
                // 合并
                if (sqlInfo.getSelectFields() == null) {
                    sqlInfo.setSelectFields(entry.getValue().getSelectFields());
                } else {
                    sqlInfo.getSelectFields().addAll(entry.getValue().getSelectFields());
                }
                if (sqlInfo.getRealSelectFields() == null) {
                    sqlInfo.setRealSelectFields(entry.getValue().getRealSelectFields());
                } else {
                    sqlInfo.getRealSelectFields().addAll(entry.getValue().getRealSelectFields());
                }
            }
        }
    }

}
