package org.jeecg.common.util.dynamic.db;

public enum DbValidationQueryEnum {
    ORACLE("oracle", "SELECT 1 FROM DUAL"),
    MYSQL("mysql", "select 1"),
    SQLSERVER("sqlserver", "SELECT 1 FROM DUAL"),;

    DbValidationQueryEnum(String dbType, String validationQuerySql) {
        this.dbType = dbType;
        this.validationQuerySql = validationQuerySql;
    }

    private String dbType;
    private String validationQuerySql;

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getValidationQuerySql() {
        return validationQuerySql;
    }

    public void setValidationQuerySql(String validationQuerySql) {
        this.validationQuerySql = validationQuerySql;
    }
}
