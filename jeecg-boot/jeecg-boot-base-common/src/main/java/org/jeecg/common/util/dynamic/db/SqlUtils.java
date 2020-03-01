package org.jeecg.common.util.dynamic.db;

import org.apache.commons.lang.StringUtils;
import org.jeecg.common.constant.DataBaseConstant;
import org.jeecg.common.system.vo.DynamicDataSourceModel;

import java.text.MessageFormat;
import java.util.Map;

/**
 * 根据不同的数据库，动态生成SQL，例如分页
 */
public class SqlUtils {

    public static final String DATABSE_TYPE_MYSQL = "mysql";
    public static final String DATABSE_TYPE_POSTGRE = "postgresql";
    public static final String DATABSE_TYPE_ORACLE = "oracle";
    public static final String DATABSE_TYPE_SQLSERVER = "sqlserver";


    /**
     * 分页SQL
     */
    public static final String MYSQL_SQL = "select * from ( {0}) sel_tab00 limit {1},{2}";
    public static final String POSTGRE_SQL = "select * from ( {0}) sel_tab00 limit {2} offset {1}";
    public static final String ORACLE_SQL = "select * from (select row_.*,rownum rownum_ from ({0}) row_ where rownum <= {1}) where rownum_>{2}";
    public static final String SQLSERVER_SQL = "select * from ( select row_number() over(order by tempColumn) tempRowNumber, * from (select top {1} tempColumn = 0, {0}) t ) tt where tempRowNumber > {2}";

    /**
     * 获取所有表的SQL
     */
    public static final String MYSQL_ALLTABLES_SQL = "select distinct table_name from information_schema.columns where table_schema = {0}";
    public static final String POSTGRE__ALLTABLES_SQL = "SELECT distinct c.relname AS  table_name FROM pg_class c";
    public static final String ORACLE__ALLTABLES_SQL = "select distinct colstable.table_name as  table_name from user_tab_cols colstable";
    public static final String SQLSERVER__ALLTABLES_SQL = "select distinct c.name as  table_name from sys.objects c";

    /**
     * 获取指定表的所有列名
     */
    public static final String MYSQL_ALLCOLUMNS_SQL = "select column_name from information_schema.columns where table_name = {0} and table_schema = {1}";
    public static final String POSTGRE_ALLCOLUMNS_SQL = "select table_name from information_schema.columns where table_name = {0}";
    public static final String ORACLE_ALLCOLUMNS_SQL = "select column_name from all_tab_columns where table_name ={0}";
    public static final String SQLSERVER_ALLCOLUMNS_SQL = "select name from syscolumns where id={0}";

    /*
     * 判断数据库类型
     */

    public static boolean dbTypeIsMySQL(String dbType) {
        return dbTypeIf(dbType, DATABSE_TYPE_MYSQL, DataBaseConstant.DB_TYPE_MYSQL_NUM);
    }

    public static boolean dbTypeIsOracle(String dbType) {
        return dbTypeIf(dbType, DATABSE_TYPE_ORACLE, DataBaseConstant.DB_TYPE_ORACLE_NUM);
    }

    public static boolean dbTypeIsSQLServer(String dbType) {
        return dbTypeIf(dbType, DATABSE_TYPE_SQLSERVER, DataBaseConstant.DB_TYPE_SQLSERVER_NUM);
    }

    public static boolean dbTypeIsPostgre(String dbType) {
        return dbTypeIf(dbType, DATABSE_TYPE_POSTGRE, DataBaseConstant.DB_TYPE_POSTGRESQL_NUM);
    }

    /**
     * 判断数据库类型
     */
    public static boolean dbTypeIf(String dbType, String... correctTypes) {
        for (String type : correctTypes) {
            if (type.equalsIgnoreCase(dbType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取全 SQL
     * 拼接 where 条件
     *
     * @param sql
     * @param params
     * @return
     */
    public static String getFullSql(String sql, Map params) {
        return getFullSql(sql, params, null, null);
    }

    /**
     * 获取全 SQL
     * 拼接 where 条件
     * 拼接 order 排序
     *
     * @param sql
     * @param params
     * @param orderColumn 排序字段
     * @param orderBy     排序方式，只能是 DESC 或 ASC
     * @return
     */
    public static String getFullSql(String sql, Map params, String orderColumn, String orderBy) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT t.* FROM ( ").append(sql).append(" ) t ");
        if (params != null && params.size() >= 1) {
            sqlBuilder.append("WHERE 1=1 ");
            for (Object key : params.keySet()) {
                String value = String.valueOf(params.get(key));
                if (StringUtils.isNotBlank(value)) {
                    sqlBuilder.append(" AND (").append(key).append(" = N'").append(value).append("')");
                }
            }
            if (StringUtils.isNotBlank(orderColumn) && StringUtils.isNotBlank(orderBy)) {
                sqlBuilder.append("ORDER BY ").append(orderColumn).append(" ").append("DESC".equalsIgnoreCase(orderBy) ? "DESC" : "ASC");
            }
        }
        return sqlBuilder.toString();
    }

    /**
     * 获取求数量 SQL
     *
     * @param sql
     * @return
     */
    public static String getCountSql(String sql) {
        return String.format("SELECT COUNT(1) \"total\" FROM ( %s ) temp_count", sql);
    }

    /**
     * 生成分页查询 SQL
     *
     * @param dbType 数据库类型
     * @param sql
     * @param page
     * @param rows
     * @return
     */
    public static String createPageSqlByDBType(String dbType, String sql, int page, int rows) {
        int beginNum = (page - 1) * rows;
        Object[] sqlParam = new Object[3];
        sqlParam[0] = sql;
        sqlParam[1] = String.valueOf(beginNum);
        sqlParam[2] = String.valueOf(rows);
        if (dbTypeIsMySQL(dbType)) {
            sql = MessageFormat.format(MYSQL_SQL, sqlParam);
        } else if (dbTypeIsPostgre(dbType)) {
            sql = MessageFormat.format(POSTGRE_SQL, sqlParam);
        } else {
            int beginIndex = (page - 1) * rows;
            int endIndex = beginIndex + rows;
            sqlParam[2] = Integer.toString(beginIndex);
            sqlParam[1] = Integer.toString(endIndex);
            if (dbTypeIsOracle(dbType)) {
                sql = MessageFormat.format(ORACLE_SQL, sqlParam);
            } else if (dbTypeIsSQLServer(dbType)) {
                sqlParam[0] = sql.substring(getAfterSelectInsertPoint(sql));
                sql = MessageFormat.format(SQLSERVER_SQL, sqlParam);
            }
        }
        return sql;
    }

    /**
     * 生成分页查询 SQL
     *
     * @param sql
     * @param page
     * @param rows
     * @return
     */
    public static String createPageSqlByDBKey(String dbKey, String sql, int page, int rows) {
        DynamicDataSourceModel dynamicSourceEntity = DataSourceCachePool.getCacheDynamicDataSourceModel(dbKey);
        String dbType = dynamicSourceEntity.getDbType();
        return createPageSqlByDBType(dbType, sql, page, rows);
    }

    private static int getAfterSelectInsertPoint(String sql) {
        int selectIndex = sql.toLowerCase().indexOf("select");
        int selectDistinctIndex = sql.toLowerCase().indexOf("select distinct");
        return selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);
    }

    public static String getAllTableSql(String dbType, Object... params) {
        if (StringUtils.isNotEmpty(dbType)) {
            if (dbTypeIsMySQL(dbType)) {
                return MessageFormat.format(MYSQL_ALLTABLES_SQL, params);
            } else if (dbTypeIsOracle(dbType)) {
                return ORACLE__ALLTABLES_SQL;
            } else if (dbTypeIsPostgre(dbType)) {
                return POSTGRE__ALLTABLES_SQL;
            } else if (dbTypeIsSQLServer(dbType)) {
                return SQLSERVER__ALLTABLES_SQL;
            }
        }
        return null;
    }

    public static String getAllColumnSQL(String dbType, Object... params) {
        if (StringUtils.isNotEmpty(dbType)) {
            if (dbTypeIsMySQL(dbType)) {
                return MessageFormat.format(MYSQL_ALLCOLUMNS_SQL, params);
            } else if (dbTypeIsOracle(dbType)) {
                return MessageFormat.format(ORACLE_ALLCOLUMNS_SQL, params);
            } else if (dbTypeIsPostgre(dbType)) {
                return MessageFormat.format(POSTGRE_ALLCOLUMNS_SQL, params);
            } else if (dbTypeIsSQLServer(dbType)) {
                return MessageFormat.format(SQLSERVER_ALLCOLUMNS_SQL, params);
            }
        }
        return null;
    }

}
