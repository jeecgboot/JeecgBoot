package org.springframework.base.system.entity;

import lombok.Data;

@Data
public class TempDto {
    private String id;
    private String sql;
    private String dbName;
    private String tableName;
    private String oldPass;
    private String newPass;
    private String name;
    private String personNumber;
    private String company;
    private String token;
    private boolean isvalidate;
    private String configId;
}
