package com.treesoft.system.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Config {
    private String id;
    private String createTime;
    private String updateTime;
    private String databaseType;
    private String driver;
    private String url;
    private String isDefault;
    private String databaseName;
    private String userName;
    private String password;
    private String port;
    private String ip;
    private String name;
    private String isDefaultView;
    private String memo;
    private String exportLimit;
    private String clientId;
    private String isRead;
    private Integer sort;
    private Integer totals;
    private String status;

    public Map<String, Object> config2Map(Config config) {
        Map<String, Object> map = new HashMap();
        map.put("id", config.getId());
        map.put("driver_class_name", config.getDriver());
        map.put("jdbc_url", config.getUrl());
        map.put("databaseName", config.getDatabaseName());
        map.put("username", config.getUserName());
        map.put("password", config.getPassword());
        map.put("isRead", config.getIsRead());
        return map;
    }
}
