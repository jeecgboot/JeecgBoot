package org.jeecg.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;

import java.util.List;

/**
 * 复写租户条件
 */
public class JeecgTenantParser extends TenantSqlParser {

    /**
     * @param expression
     * @param table
     * @return
     */
    protected Expression processTableAlias(Expression expression, Table table) {
        String tableAliasName;
        if (table.getAlias() == null) {
            tableAliasName = table.getName();
        } else {
            tableAliasName = table.getAlias().getName();
        }

        // in
        if (expression instanceof InExpression) {
            InExpression in = (InExpression) expression;
            if (in.getLeftExpression() instanceof Column) {
                setTableAliasNameForColumn((Column) in.getLeftExpression(), tableAliasName);
            }

            // 比较操作
        } else if (expression instanceof BinaryExpression) {
            BinaryExpression compare = (BinaryExpression) expression;
            if (compare.getLeftExpression() instanceof Column) {
                setTableAliasNameForColumn((Column) compare.getLeftExpression(), tableAliasName);
            } else if (compare.getRightExpression() instanceof Column) {
                setTableAliasNameForColumn((Column) compare.getRightExpression(), tableAliasName);
            }

            // between
        } else if (expression instanceof Between) {
            Between between = (Between) expression;
            if (between.getLeftExpression() instanceof Column) {
                setTableAliasNameForColumn((Column) between.getLeftExpression(), tableAliasName);
            }
        }
        return expression;
    }

    private void setTableAliasNameForColumn(Column column, String tableAliasName) {
        column.setColumnName(tableAliasName + "." + column.getColumnName());
    }

    /**
     * 默认是按 tenant_id=1 按等于条件追加
     *
     * @param currentExpression 现有的条件：比如你原来的sql查询条件
     * @param table
     * @return
     */
    @Override
    protected Expression builderExpression(Expression currentExpression, Table table) {
        final Expression tenantExpression = this.getTenantHandler().getTenantId(true);
        Expression appendExpression;
        if (!(tenantExpression instanceof SupportsOldOracleJoinSyntax)) {
            appendExpression = new EqualsTo();
            ((EqualsTo) appendExpression).setLeftExpression(this.getAliasColumn(table));
            ((EqualsTo) appendExpression).setRightExpression(tenantExpression);
        } else {
            appendExpression = processTableAlias(tenantExpression, table);
        }
        if (currentExpression == null) {
            return appendExpression;
        }
        if (currentExpression instanceof BinaryExpression) {
            BinaryExpression binaryExpression = (BinaryExpression) currentExpression;
            if (binaryExpression.getLeftExpression() instanceof FromItem) {
                processFromItem((FromItem) binaryExpression.getLeftExpression());
            }
            if (binaryExpression.getRightExpression() instanceof FromItem) {
                processFromItem((FromItem) binaryExpression.getRightExpression());
            }
        } else if (currentExpression instanceof InExpression) {
            InExpression inExp = (InExpression) currentExpression;
            ItemsList rightItems = inExp.getRightItemsList();
            if (rightItems instanceof SubSelect) {
                processSelectBody(((SubSelect) rightItems).getSelectBody());
            }
        }
        if (currentExpression instanceof OrExpression) {
            return new AndExpression(new Parenthesis(currentExpression), appendExpression);
        } else {
            return new AndExpression(currentExpression, appendExpression);
        }
    }

    @Override
    protected void processPlainSelect(PlainSelect plainSelect, boolean addColumn) {
        FromItem fromItem = plainSelect.getFromItem();
        if (fromItem instanceof Table) {
            Table fromTable = (Table) fromItem;
            if (!this.getTenantHandler().doTableFilter(fromTable.getName())) {
                plainSelect.setWhere(builderExpression(plainSelect.getWhere(), fromTable));
                if (addColumn) {
                    plainSelect.getSelectItems().add(new SelectExpressionItem(new Column(this.getTenantHandler().getTenantIdColumn())));
                }
            }
        } else {
            processFromItem(fromItem);
        }
        List<Join> joins = plainSelect.getJoins();
        if (joins != null && joins.size() > 0) {
            joins.forEach(j -> {
                processJoin(j);
                processFromItem(j.getRightItem());
            });
        }
    }

}
