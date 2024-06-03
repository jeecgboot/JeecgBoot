package org.jeecg.common.util.sqlparse.vo;

import lombok.Data;
import net.sf.jsqlparser.statement.select.SelectBody;

import java.util.HashSet;
import java.util.Set;

/**
 * select 查询 sql 的信息
 */
@Data
public class SelectSqlInfo {

    /**
     * 查询的表名，如果是子查询，则此处为null
     */
    private String fromTableName;
    /**
     * 表别名
     */
    private String fromTableAliasName;
    /**
     * 通过子查询获取的表信息，例如：select name from (select * from user) u
     * 如果不是子查询，则为null
     */
    private SelectSqlInfo fromSubSelect;
    /**
     * 查询的字段集合，如果是 * 则为null，如果设了别名则为别名
     */
    private Set<String> selectFields;
    /**
     * 真实的查询字段集合，如果是 * 则为null，如果设了别名则为原始字段名
     */
    private Set<String> realSelectFields;
    /**
     * 是否是查询所有字段
     */
    private boolean selectAll;

    /**
     * 解析之后的 SQL （关键字都是大写）
     */
    private final String parsedSql;

    public SelectSqlInfo(String parsedSql) {
        this.parsedSql = parsedSql;
    }

    public SelectSqlInfo(SelectBody selectBody) {
        this.parsedSql = selectBody.toString();
    }

    public void addSelectField(String selectField, String realSelectField) {
        if (this.selectFields == null) {
            this.selectFields = new HashSet<>();
        }
        if (this.realSelectFields == null) {
            this.realSelectFields = new HashSet<>();
        }
        this.selectFields.add(selectField);
        this.realSelectFields.add(realSelectField);
    }

    /**
     * 获取所有字段，包括子查询里的。
     *
     * @return
     */
    public Set<String> getAllRealSelectFields() {
        Set<String> fields = new HashSet<>();
        // 递归获取所有字段，起个直观的方法名为：
        this.recursiveGetAllFields(this, fields);
        return fields;
    }

    /**
     * 递归获取所有字段
     */
    private void recursiveGetAllFields(SelectSqlInfo sqlInfo, Set<String> fields) {
        if (!sqlInfo.isSelectAll() && sqlInfo.getRealSelectFields() != null) {
            fields.addAll(sqlInfo.getRealSelectFields());
        }
        if (sqlInfo.getFromSubSelect() != null) {
            recursiveGetAllFields(sqlInfo.getFromSubSelect(), fields);
        }
    }

    @Override
    public String toString() {
        return "SelectSqlInfo{" +
                "fromTableName='" + fromTableName + '\'' +
                ", fromSubSelect=" + fromSubSelect +
                ", aliasName='" + fromTableAliasName + '\'' +
                ", selectFields=" + selectFields +
                ", realSelectFields=" + realSelectFields +
                ", selectAll=" + selectAll +
                "}";
    }

}
