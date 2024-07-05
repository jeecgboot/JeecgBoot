package org.springframework.base.system.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ConfigDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public ConfigDao() {
    }

    public List<Map<String, Object>> getAllConfigList() {
        List<Map<String, Object>> list = jdbcTemplate.queryForList(" select id, name,database_type as databaseType , database_name as databaseName, user_name as userName , password, port, ip ,url ,is_default as isDefault from  treesoft_config ");
        return list;
    }

}
