package org.jeecg.common.util.dynamic.db;

import com.baomidou.mybatisplus.annotation.DbType;
import org.jeecg.common.constant.DataBaseConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库类型判断
 * 【有些数据库引擎是一样的，以达到复用目的】
 * @author: jeecg-boot
 */
public class DbTypeUtils {

    public static Map<String, String> dialectMap = new HashMap<String, String>();
    static{
        dialectMap.put("mysql", "org.hibernate.dialect.MySQL5InnoDBDialect");
        // mariadb数据库 1  --
        dialectMap.put("mariadb", "org.hibernate.dialect.MariaDBDialect");
        //oracle数据库 1
        dialectMap.put("oracle", "org.hibernate.dialect.OracleDialect");
        // TODO 没找到不确定
        dialectMap.put("oracle12c", "org.hibernate.dialect.OracleDialect");
        // db2数据库 1xx
        dialectMap.put("db2", "org.hibernate.dialect.DB2390Dialect");
        // H2数据库
        dialectMap.put("h2", "org.hibernate.dialect.HSQLDialect");
        // HSQL数据库  1
        dialectMap.put("hsql", "org.hibernate.dialect.HSQLDialect");
        //SQLite数据库 应用平台mobile
        dialectMap.put("sqlite", "org.jeecg.modules.online.config.dialect.SQLiteDialect");
        //PostgreSQL数据库1  --
        dialectMap.put("postgresql", "org.hibernate.dialect.PostgreSQLDialect");
        dialectMap.put("sqlserver2005", "org.hibernate.dialect.SQLServer2005Dialect");
        //sqlserver数据库1
        dialectMap.put("sqlserver", "org.hibernate.dialect.SQLServerDialect");
        //达梦数据库 [国产] 1--
        dialectMap.put("dm", "org.hibernate.dialect.DmDialect");
        //虚谷数据库
        dialectMap.put("xugu", "org.hibernate.dialect.HSQLDialect");
        //人大金仓 [国产] 1
        dialectMap.put("kingbasees", "org.hibernate.dialect.PostgreSQLDialect");
        // Phoenix HBase数据库
        dialectMap.put("phoenix", "org.hibernate.dialect.HSQLDialect");
        // Gauss 数据库
        dialectMap.put("zenith", "org.hibernate.dialect.PostgreSQLDialect");
        //阿里云PolarDB
        dialectMap.put("clickhouse", "org.hibernate.dialect.MySQLDialect");
        // 南大通用数据库 TODO 没找到不确定
        dialectMap.put("gbase", "org.hibernate.dialect.PostgreSQLDialect");
        //神通数据库 [国产] TODO 没找到不确定
        dialectMap.put("oscar", "org.hibernate.dialect.PostgreSQLDialect");
        //Sybase ASE 数据库
        dialectMap.put("sybase", "org.hibernate.dialect.SybaseDialect");
        dialectMap.put("oceanbase", "org.hibernate.dialect.PostgreSQLDialect");
        dialectMap.put("Firebird", "org.hibernate.dialect.FirebirdDialect");
        //瀚高数据库
        dialectMap.put("highgo", "org.hibernate.dialect.HSQLDialect");
        dialectMap.put("other", "org.hibernate.dialect.PostgreSQLDialect");
    }

    public static boolean dbTypeIsMySql(DbType dbType) {
        return dbTypeIf(dbType, DbType.MYSQL, DbType.MARIADB, DbType.CLICK_HOUSE, DbType.SQLITE);
    }

    public static boolean dbTypeIsOracle(DbType dbType) {
        return dbTypeIf(dbType, DbType.ORACLE, DbType.ORACLE_12C, DbType.DM);
    }

    public static boolean dbTypeIsSqlServer(DbType dbType) {
        return dbTypeIf(dbType, DbType.SQL_SERVER, DbType.SQL_SERVER2005);
    }

    public static boolean dbTypeIsPostgre(DbType dbType) {
        return dbTypeIf(dbType, DbType.POSTGRE_SQL, DbType.KINGBASE_ES, DbType.GAUSS);
    }



    /**
     *  根据枚举类 获取数据库类型的字符串
     * @param dbType
     * @return
     */
    public static String getDbTypeString(DbType dbType){
        if(DbType.DB2.equals(dbType)){
            return DataBaseConstant.DB_TYPE_DB2;
        }else if(DbType.HSQL.equals(dbType)){
            return DataBaseConstant.DB_TYPE_HSQL;
        }else if(dbTypeIsOracle(dbType)){
            return DataBaseConstant.DB_TYPE_ORACLE;
        }else if(dbTypeIsSqlServer(dbType)){
            return DataBaseConstant.DB_TYPE_SQLSERVER;
        }else if(dbTypeIsPostgre(dbType)){
            return DataBaseConstant.DB_TYPE_POSTGRESQL;
        }
        return DataBaseConstant.DB_TYPE_MYSQL;
    }

    /**
     *  根据枚举类 获取数据库方言字符串
     * @param dbType
     * @return
     */
    public static String getDbDialect(DbType dbType){
        return dialectMap.get(dbType.getDb());
    }

    /**
     * 判断数据库类型
     */
    public static boolean dbTypeIf(DbType dbType, DbType... correctTypes) {
        for (DbType type : correctTypes) {
            if (type.equals(dbType)) {
                return true;
            }
        }
        return false;
    }

}
