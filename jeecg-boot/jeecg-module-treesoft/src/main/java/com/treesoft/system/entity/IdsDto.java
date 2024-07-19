package com.treesoft.system.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class IdsDto implements Serializable {
    private static final long serialVersionUID = -8443145976158317075L;
    private String id;
    private String[] ids;
    private String tableName;
    private String databaseName;
    private String primary_key;
    private String column_name;
    private String column_name2;
    private String is_nullable;
    private String column_key;
    private String checkedItems;
    private String newTableName;
    private String clientId;
    private String remark;
    private String databaseConfigId;
    private String indexName;
    private String triggerName;
    private String foreignKeyName;
    private String noSQLDbName;
}
