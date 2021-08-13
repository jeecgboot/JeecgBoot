package org.jeecg.common.util.dynamic.db;

import com.baomidou.mybatisplus.annotation.DbType;

/**
 * 数据库类型判断
 * 【有些数据库引擎是一样的，以达到复用目的】
 */
public class DbTypeUtils {

    public static boolean dbTypeIsMySQL(DbType dbType) {
        return dbTypeIf(dbType, DbType.MYSQL, DbType.MARIADB, DbType.CLICK_HOUSE, DbType.SQLITE);
    }

    public static boolean dbTypeIsOracle(DbType dbType) {
        return dbTypeIf(dbType, DbType.ORACLE, DbType.ORACLE_12C, DbType.DM);
    }

    public static boolean dbTypeIsSQLServer(DbType dbType) {
        return dbTypeIf(dbType, DbType.SQL_SERVER, DbType.SQL_SERVER2005);
    }

    public static boolean dbTypeIsPostgre(DbType dbType) {
        return dbTypeIf(dbType, DbType.POSTGRE_SQL, DbType.KINGBASE_ES, DbType.GAUSS);
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
