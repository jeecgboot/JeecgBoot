package org.springframework.base.system.dao;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.base.common.utils.DateUtil;
import org.springframework.base.system.core.BusiDataBaseUtil;
import org.springframework.base.system.entity.Config;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.CryptoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class PermissionDao {
    private static final Logger logger = LoggerFactory.getLogger(PermissionDao.class);
    
    Pattern numPattern = Pattern.compile("\\s+");
    
    Pattern datePattern = Pattern.compile("(\\d{4})(\\-)(\\d{2})(\\-)(\\d{2})(\\s+)(\\d{2})(\\:)(\\d{2})(\\:)(\\d{2})");
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Autowired
    NamedParameterJdbcTemplate namedJdbcTemplate;
    
    public List<Map<String, Object>> getAllDataBase(String databaseConfigId) {
        Map<String, Object> map0 = getConfig(databaseConfigId);
        String databaseName = (String)map0.get("databaseName");
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(Collections.singletonMap("SCHEMA_NAME", databaseName));
        return list;
    }

    public List<Map<String, Object>> getAllDataBaseById(String datascope) {
        String sql = " select id, name, database_type as databaseType, database_name as databaseName, port, ip  from  treesoft_config where id in (" + datascope + ") order by is_default desc ";
        return jdbcTemplate.queryForList(sql);
    }
    
    public List<Map<String, Object>> getAllTables(String dbName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        String sql = " select TABLE_NAME from information_schema.TABLES where table_schema='" + dbName + "' and table_type='BASE TABLE' ";
        return db.queryForList(sql);
    }

    public List<Map<String, Object>> getAllViews(String dbName, String databaseConfigId)
        throws Exception {
        String sql = " select TABLE_NAME from information_schema.TABLES where table_schema='" + dbName + "' and table_type='VIEW' ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        return db.queryForList(sql);
    }
    
    public List<Map<String, Object>> getAllFuntion(String dbName, String databaseConfigId)
        throws Exception {
        String sql = " select ROUTINE_NAME from information_schema.ROUTINES where routine_schema='" + dbName + "' ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        return db.queryForList(sql);
    }
    
    public List<Map<String, Object>> getTableColumns(String dbName, String tableName, String databaseConfigId) {
        String sql = "select * from  `" + dbName + "`.`" + tableName + "` limit 1 ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        return db.queryForColumnOnly(sql);
    }
    
    public List<Map<String, Object>> getTableColumns3(String dbName, String tableName, String databaseConfigId)
        throws Exception {
        String sql =
            " select column_name as TREESOFTPRIMARYKEY, COLUMN_NAME,COLUMN_TYPE, DATA_TYPE,COLUMN_DEFAULT,CHARACTER_MAXIMUM_LENGTH, NUMERIC_PRECISION,NUMERIC_SCALE, IS_NULLABLE, COLUMN_KEY,extra, COLUMN_COMMENT  from information_schema.columns where table_name='"
                + tableName + "' and table_schema='" + dbName + "'  ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        return db.queryForList(sql);
    }
    
    public Page<Map<String, Object>> getData(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId)
        throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        String orderBy = page.getOrderBy();
        String order = page.getOrder();
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        List<Map<String, Object>> list3 = getPrimaryKeyss(dbName, tableName, databaseConfigId);
        String tem = "";
        for (Map<String, Object> map : list3) {
            tem = tem + map.get("column_name") + ",";
        }
        String primaryKey = "";
        if (!tem.equals("")) {
            primaryKey = tem.substring(0, tem.length() - 1);
        }
        String sql = "select count(*) from  `" + dbName + "`.`" + tableName + "`";
        String sql2 = "";
        if ((orderBy == null) || (orderBy.equals(""))) {
            sql2 = "select  *  from  `" + dbName + "`.`" + tableName + "`  LIMIT " + limitFrom + "," + pageSize;
        }
        else {
            sql2 = "select  *  from  `" + dbName + "`.`" + tableName + "` order by " + orderBy + " " + order + "  LIMIT " + limitFrom + "," + pageSize;
        }
        List<Map<String, Object>> list = db.queryForList(sql2);
        int rowCount = db.executeQueryForCount(sql);
        List<Map<String, Object>> columns = getTableColumns(dbName, tableName, databaseConfigId);
        List<Map<String, Object>> tempList = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("field", "treeSoftPrimaryKey");
        map1.put("checkbox", Boolean.valueOf(true));
        tempList.add(map1);
        for (Map<String, Object> map : columns) {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("field", map.get("column_name"));
            map2.put("title", map.get("column_name"));
            map2.put("sortable", Boolean.valueOf(true));
            if (map.get("data_type").equals("DATETIME"))  {
                map2.put("editor", "datetimebox");
            } else if ((map.get("data_type").equals("INT")) || (map.get("data_type").equals("SMALLINT")) || (map.get("data_type").equals("TINYINT")))  {
                map2.put("editor", "numberbox");
            } else if (map.get("data_type").equals("DOUBLE"))  {
                map2.put("editor", "numberbox");
            } else if ((!map.get("data_type").equals("BLOB")) && (!map.get("data_type").equals("CLOB")))  {
                if ((!map.get("data_type").equals("VARBINARY")) && (!map.get("data_type").equals("BINARY"))) {
                    map2.put("editor", "text");
                }
            }
            tempList.add(map2);
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";
        page.setTotalCount(rowCount);
        page.setResult(list);
        page.setColumns(jsonfromList);
        page.setPrimaryKey(primaryKey);
        return page;
    }
    
    public Page<Map<String, Object>> executeSql(Page<Map<String, Object>> page, String sql, String dbName, String databaseConfigId)
        throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        sql = sql.trim();
        String sql2 = " select * from ( " + sql + " ) tab  LIMIT " + limitFrom + "," + pageSize;
        if ((sql.indexOf("show") == 0) || (sql.indexOf("SHOW") == 0)) {
            sql2 = sql;
        }
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForList(sql2);
        int rowCount = db.executeQueryForCount2(sql);
        List<Map<String, Object>> columns = executeSqlForColumns(sql, dbName, databaseConfigId);
        List<Map<String, Object>> tempList = new ArrayList<>();
        for (Map<String, Object> map : columns) {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("field", map.get("column_name"));
            map2.put("title", map.get("column_name"));
            map2.put("sortable", Boolean.valueOf(true));
            if (map.get("data_type").equals("DATETIME"))  {
                map2.put("editor", "datetimebox");
            } else if ((map.get("data_type").equals("INT")) || (map.get("data_type").equals("SMALLINT")) || (map.get("data_type").equals("TINYINT")))  {
                map2.put("editor", "numberbox");
            } else if (map.get("data_type").equals("DOUBLE"))  {
                map2.put("editor", "numberbox");
            } else if ((!map.get("data_type").equals("BLOB")) && (!map.get("data_type").equals("CLOB")))  {
                if ((!map.get("data_type").equals("VARBINARY")) && (!map.get("data_type").equals("BINARY"))) {
                    map2.put("editor", "text");
                }
            }
            tempList.add(map2);
        }
        String primaryKey = "";
        String tableName = "";
        String temp = "";
        if (checkSqlIsOneTableForMySql(dbName, sql, databaseConfigId)) {
            Matcher m = numPattern.matcher(sql);
            temp = m.replaceAll(" ");
            temp = temp.toLowerCase();
            for (int y = 14; y < temp.length(); y++)  {
                String c = String.valueOf(temp.charAt(y));
                if (c.equals(" ")) {
                    break;
                }
                tableName = tableName + c;
            }
            List<Map<String, Object>> list3 = getPrimaryKeyss(dbName, tableName, databaseConfigId);
            String tem = "";
            for (Map<String, Object> map3 : list3)  {
                tem = tem + map3.get("column_name") + ",";
            }
            if (!tem.equals(""))  {
                primaryKey = tem.substring(0, tem.length() - 1);
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";
        page.setTotalCount(rowCount);
        page.setResult(list);
        page.setColumns(jsonfromList);
        page.setPrimaryKey(primaryKey);
        page.setTableName(tableName);
        return page;
    }
    
    public List<Map<String, Object>> selectAllDataFromSQLForMysql(String databaseName, String databaseConfigId, String sql)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        return db.queryForList(sql);
    }
    
    public boolean checkSqlIsOneTableForMySql(String dbName, String sql, String databaseConfigId) {
        String temp = "";
        String tableName = "";
        try {
            BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
            Matcher m = numPattern.matcher(sql);
            temp = m.replaceAll(" ");
            temp = temp.toLowerCase();
            if (temp.indexOf("select * from") >= 0)  {
                for (int y = 14; y < temp.length(); y++) {
                    String c = String.valueOf(temp.charAt(y));
                    if (c.equals(" "))
                    {
                        break;
                    }
                    tableName = tableName + c;
                }
                List<Map<String, Object>> list = db.queryForList(sql);
                if (!list.isEmpty()) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
    
    public List<Map<String, Object>> executeSqlForColumns(String sql, String dbName, String databaseConfigId)
        throws Exception {
        String sql2 = " select * from  ( " + sql + " ) tab  limit 1 ";
        if ((sql.indexOf("show") == 0) || (sql.indexOf("SHOW") == 0)) {
            sql2 = sql;
        }
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        return db.executeSqlForColumns(sql2);
    }

    public boolean saveSearchHistory(String name, String sql, String dbName, String userId) {
        return jdbcTemplate.update("insert into treesoft_search_history (create_time, sqls, name, data_base,user_id) values (now(), ?, ?, ?, ?)", sql, name, dbName, userId) > 0;
    }

    public boolean updateSearchHistory(String id, String name, String sql, String dbName) {
        return jdbcTemplate.update("update treesoft_search_history set create_time=now(), sqls=?, name =?, data_base=? where id=?", sql, name, dbName, id) > 0;
    }

    public boolean deleteSearchHistory(String id) {
        return jdbcTemplate.update("delete from treesoft_search_history where id=?", id) > 0;
    }

    public List<Map<String, Object>> selectSearchHistory(String userId) {
        return jdbcTemplate.queryForList(" select * from treesoft_search_history where getRootFromHistory(pid) = 'SQL收藏' and (user_id=? or is_share='Y')",userId);
    }
    
    public boolean configUpdate(Config config) {
        String id = config.getId();
        String isDefault = StringUtils.defaultIfBlank(config.getIsDefault(), "0");
        String sql;
        boolean bl;
        if (StringUtils.isNotBlank(id)) {
            if (StringUtils.isNotBlank(config.getPassword()))  {
                sql = " update treesoft_config  set database_type=?, database_name=?, user_name=?,  password=?, is_default=?, name =?, port=?, ip=?, url=? where id=?";
                bl = jdbcTemplate.update(sql,
                    config.getDatabaseType(),
                    config.getDatabaseName(),
                    config.getUserName(),
                    CryptoUtil.encode(new StringBuilder(String.valueOf(config.getUserName())).append("`").append(config.getPassword()).toString()),
                    isDefault,
                    config.getName(),
                    config.getPort(),
                    config.getIp(),
                    config.getUrl(),
                    id) > 0;
            } else {
                sql = " update treesoft_config  set database_type=?, database_name=?, user_name=?,  is_default=?, name =?, port=?, ip=?, url=? where id=?";
                bl = jdbcTemplate
                    .update(sql, config.getDatabaseType(), config.getDatabaseName(), config.getUserName(), isDefault, config.getName(), config.getPort(), config.getIp(), config.getUrl(), id) > 0;
            }
        }
        else {
            sql = " insert into treesoft_config (name,  create_time, database_type, database_name, user_name, password, port, ip, is_default, url ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
            bl = jdbcTemplate.update(sql,
                config.getName(),
                DateUtil.getDateTime(),
                config.getDatabaseType(),
                config.getDatabaseName(),
                config.getUserName(),
                CryptoUtil.encode(new StringBuilder(String.valueOf(config.getUserName())).append("`").append(config.getPassword()).toString()),
                config.getPort(),
                config.getIp(),
                isDefault,
                config.getUrl()) > 0;
        }
        List<Map<String, Object>> list3 = jdbcTemplate.queryForList("select id from  treesoft_config order by is_default desc");
        StringBuilder ids = new StringBuilder();
        for (Map<String, Object> map : list3) {
            ids.append(map.get("id")).append(",");
        }
        String updateSQL = "update treesoft_users set datascope =? where username in ('admin','treesoft')";
        jdbcTemplate.update(updateSQL, ids.toString().substring(0, ids.length() - 1));
        return bl;
    }
    
    public List<Map<String, Object>> selectUserByName(String userName) {
        return jdbcTemplate.queryForList("select * from  treesoft_users where username=?", userName);
    }
    
    public boolean updateUserPass(String userId, String newPass) {
        return jdbcTemplate.update("update treesoft_users set password=? where id=?", newPass, userId) > 0;
    }
    
    public int executeSqlNotRes(String sql, String dbName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        return db.setupdateData(sql);
    }

    public int deleteRowsNew(String databaseName, String tableName, String primary_key, List<String> condition, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        int y = 0;
        for (int i = 0; i < condition.size(); i++) {
            String whereStr = condition.get(i);
            String sql = "delete from " + databaseName + "." + tableName + " where 1=1 " + whereStr;
            y += db.setupdateData(sql);
        }
        return y;
    }
    
    public int saveRows(Map<String, Object> map, String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql = " insert into " + databaseName + "." + tableName;
        String colums = " ";
        String values = " ";
        String columnType = "";
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            colums = colums + entry.getKey() + ",";
            columnType = selectOneColumnType(databaseName, tableName, entry.getKey(), databaseConfigId);
            String str = (String)entry.getValue();
            if (str.equals(""))  {
                values = values + " null,";
            } else if ((columnType.indexOf("integer") >= 0) || (columnType.indexOf("bit") >= 0) || (columnType.indexOf("int") >= 0) || (columnType.indexOf("float") >= 0))  {
                values = values + entry.getValue() + ",";
            } else {
                values = values + "'" + entry.getValue() + "',";
            }
        }
        colums = colums.substring(0, colums.length() - 1);
        values = values.substring(0, values.length() - 1);
        sql = sql + " (" + colums + ") values (" + values + ")";
        return db.setupdateData(sql);
    }
    
    public List<Map<String, Object>> getOneRowById(String databaseName, String tableName, String id, String idValues, String databaseConfigId) {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql2 = "select * from " + databaseName + "." + tableName + " where " + id + "='" + idValues + "' ";
        return db.queryForListWithType(sql2);
    }
    
    public int updateRows(Map<String, Object> map, String databaseName, String tableName, String id, String idValues, String databaseConfigId)
        throws Exception {
        if ((id == null) || ("".equals(id))) {
            throw new Exception("数据不完整,保存失败!");
        }
        if ((idValues == null) || ("".equals(idValues))) {
            throw new Exception("数据不完整,保存失败!");
        }
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql = " update  " + databaseName + "." + tableName;
        String ss = " set  ";
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String str = (String)entry.getValue();
            if (str.equals(""))  {
                ss = ss + entry.getKey() + "= null,";
            } else if ((entry.getValue() instanceof String))  {
                ss = ss + entry.getKey() + "= '" + entry.getValue() + "',";
            } else {
                ss = ss + entry.getKey() + "= " + entry.getValue() + ",";
            }
        }
        ss = ss.substring(0, ss.length() - 1);
        sql = sql + ss + " where " + id + "='" + idValues + "'";
        return db.setupdateData(sql);
    }
    
    public String getViewSql(String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        String sql = "select view_definition from information_schema.VIEWS where table_name='" + tableName + "' and table_schema='" + databaseName + "'  ";
        String str = "";
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForList(sql);
        if (list.size() == 1) {
            Map<String, Object> map = list.get(0);
            str = (String)map.get("view_definition");
        }
        return str;
    }

    public List<Map<String, Object>> getPrimaryKeyss(String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        String sql = "select column_name from information_schema.columns where table_name='" + tableName + "' and table_schema='" + databaseName + "' and column_key='PRI' ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        return db.queryForList(sql);
    }
    
    public boolean testConn(String databaseType, String databaseName, String ip, String port, String user, String pass) {
        return BusiDataBaseUtil.testConnection(databaseType, databaseName, ip, port, user, pass);
    }

    public List<Map<String, Object>> selectSqlStudy() {
        return jdbcTemplate.queryForList("select id,name as title,sqls as content,pid,user_name as icon from treesoft_search_history where getRootFromHistory(pid) = 'SQL帮助'");
    }

    public int saveDesginColumn(Map<String, String> map, String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql = "alter table " + databaseName + "." + tableName + " add column ";
        sql = sql + map.get("COLUMN_NAME") + "  ";
        sql = sql + map.get("DATA_TYPE");
        if ((map.get("CHARACTER_MAXIMUM_LENGTH") != null) && (!(map.get("CHARACTER_MAXIMUM_LENGTH")).equals(""))) {
            sql = sql + " (" + map.get("CHARACTER_MAXIMUM_LENGTH") + ") ";
        }
        if ((map.get("COLUMN_COMMENT") != null) && (!(map.get("COLUMN_COMMENT")).equals(""))) {
            sql = sql + " comment '" + map.get("COLUMN_COMMENT") + "'";
        }
        return db.setupdateData(sql);
    }
    
    public int deleteTableColumn(String databaseName, String tableName, String[] ids, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        int y = 0;
        for (int i = 0; i < ids.length; i++) {
            String sql = "alter table " + databaseName + "." + tableName + " drop column  " + ids[i];
            y += db.setupdateData(sql);
        }
        return y;
    }
    
    public int updateTableColumn(Map<String, Object> map, String databaseName, String tableName, String columnName, String idValues, String databaseConfigId)
        throws Exception {
        if ((columnName == null) || ("".equals(columnName))) {
            throw new Exception("数据不完整,保存失败!");
        }
        if ((idValues == null) || ("".equals(idValues))) {
            throw new Exception("数据不完整,保存失败!");
        }
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String old_field_name = (String)map.get("TREESOFTPRIMARYKEY");
        String column_name = (String)map.get("COLUMN_NAME");
        String dataType = (String)map.get("DATA_TYPE");
        String character_maximum_length = (String)map.get("CHARACTER_MAXIMUM_LENGTH");
        String column_comment = (String)map.get("COLUMN_COMMENT");
        if (!old_field_name.endsWith(column_name)) {
            String sql = " alter table  " + databaseName + "." + tableName + " change ";
            sql = sql + old_field_name + " " + column_name + " " + dataType;
            if ((character_maximum_length != null) && (!character_maximum_length.equals("")))  {
                sql = sql + " (" + character_maximum_length + ")";
            }
            db.setupdateData(sql);
        }
        String sql2 = " alter table  " + databaseName + "." + tableName + " modify column " + column_name + " " + dataType;
        if ((character_maximum_length != null) && (!character_maximum_length.equals(""))) {
            sql2 = sql2 + " (" + character_maximum_length + ")";
        }
        if ((column_comment != null) && (!column_comment.equals(""))) {
            sql2 = sql2 + " comment '" + column_comment + "'";
        }
        return db.setupdateData(sql2);
    }
    
    public int savePrimaryKey(String databaseName, String tableName, String column_name, String isSetting, String databaseConfigId)
        throws Exception {
        String sql4 = "";
        if ((column_name != null) && (!column_name.equals(""))) {
            BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
            List<String> list2 = selectTablePrimaryKey(databaseName, tableName, databaseConfigId);
            if (isSetting.equals("true"))  {
                list2.add(column_name);
            } else {
                list2.remove(column_name);
            }
            String tem = list2.toString();
            String primaryKey = tem.substring(1, tem.length() - 1);
            if (primaryKey.equals(""))  {
                sql4 = " alter table  " + databaseName + "." + tableName + " drop primary key ";
            } else if ((list2.size() == 1) && (isSetting.equals("true")))  {
                sql4 = " alter table  " + databaseName + "." + tableName + " add primary key (" + primaryKey + ")";
            } else {
                sql4 = " alter table  " + databaseName + "." + tableName + " drop primary key, add primary key (" + primaryKey + ")";
            }
            db.setupdateData(sql4);
        }
        return 0;
    }
    
    public List<String> selectTablePrimaryKey(String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        String sql = " select column_name from information_schema.columns where table_name='" + tableName + "' and table_schema='" + databaseName + "'  and column_key='PRI' ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForList(sql);
        List<String> list2 = new ArrayList<>();
        for (Map<String, Object> map : list) {
            list2.add((String)map.get("column_name"));
        }
        return list2;
    }
    
    public String selectOneColumnType(String databaseName, String tableName, String column_name, String databaseConfigId)
        throws Exception {
        String sql = " select column_type  from information_schema.columns where table_name='" + tableName + "' and table_schema='" + databaseName + "' and column_name='" + column_name + "'";
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForList(sql);
        return (String)(list.get(0)).get("column_type");
    }
    
    public int updateTableNullAble(String databaseName, String tableName, String column_name, String is_nullable, String databaseConfigId)
        throws Exception {
        String sql4 = "";
        if ((column_name != null) && (!column_name.equals(""))) {
            BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
            String column_type = selectOneColumnType(databaseName, tableName, column_name, databaseConfigId);
            if (is_nullable.equals("true"))  {
                sql4 = " alter table  " + databaseName + "." + tableName + " modify column " + column_name + " " + column_type + "  null ";
            } else {
                sql4 = " alter table  " + databaseName + "." + tableName + " modify column " + column_name + " " + column_type + " not null ";
            }
            db.setupdateData(sql4);
        }
        return 0;
    }
    
    public int upDownColumn(String databaseName, String tableName, String column_name, String column_name2, String databaseConfigId)
        throws Exception {
        String sql4 = "";
        if ((column_name != null) && (!column_name.equals(""))) {
            BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
            String column_type = selectOneColumnType(databaseName, tableName, column_name, databaseConfigId);
            if ((column_name2 == null) || (column_name2.equals("")))  {
                sql4 = "alter table " + databaseName + "." + tableName + " modify column " + column_name + " " + column_type + " first ";
            } else {
                sql4 = "alter table " + databaseName + "." + tableName + " modify column " + column_name + " " + column_type + " after " + column_name2;
            }
            db.setupdateData(sql4);
        }
        return 0;
    }
    
    public List<Map<String, Object>> getAllDataBaseForOracle(String databaseConfigId) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map0 = getConfig(databaseConfigId);
        String databaseName = (String)map0.get("databaseName");
        Map<String, Object> map = new HashMap<>();
        map.put("SCHEMA_NAME", databaseName);
        list.add(map);
        return list;
    }
    
    public List<Map<String, Object>> getAllTablesForOracle(String dbName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        return db.queryForList("select TABLE_NAME from user_tables  ");
    }
    
    public List<Map<String, Object>> getTableColumns3ForOracle(String dbName, String tableName, String databaseConfigId)
        throws Exception {
        String sql =
            "select t1.column_name as TREESOFTPRIMARYKEY, t1.COLUMN_NAME, nvl2( t1.CHAR_COL_DECL_LENGTH, t1.DATA_TYPE||'(' ||CHAR_COL_DECL_LENGTH||')',t1.DATA_TYPE ) as COLUMN_TYPE,t1.dataType, t1.data_length as CHARACTER_MAXIMUM_LENGTH,CASE t1.nullable when 'Y' then 'YES' END as IS_NULLABLE, nvl2(t3.column_name,'PRI','')  as COLUMN_KEY, t2.comments as COLUMN_COMMENT  from user_tab_columns  t1 left join user_col_comments t2  on  t1.table_name = t2.table_name and t1.COLUMN_NAME = t2.COLUMN_NAME left join (select a.table_name, a.column_name from user_cons_columns a, user_constraints b  where a.constraint_name = b.constraint_name  and b.constraint_type = 'P' ) t3  on t1.TABLE_NAME = t3.table_name  and t1.COLUMN_NAME = t3.COLUMN_NAME  where t1.table_name= '"
                + tableName + "'  ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        return db.queryForList(sql);
    }
    
    public List<Map<String, Object>> getAllViewsForOracle(String dbName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        return db.queryForList("select view_name as TABLE_NAME from  user_views  ");
    }
    
    public String getViewSqlForOracle(String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        String sql = " select TEXT from all_views where view_name = '" + tableName + "'";
        String str = "";
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForList(sql);
        if (list.size() == 1) {
            Map<String, Object> map = list.get(0);
            str = "create view " + tableName + " as " + (String)map.get("TEXT") + ";";
        }
        return str;
    }
    
    public List<Map<String, Object>> getAllFuntionForOracle(String dbName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        return db.queryForList("select object_name as ROUTINE_NAME from user_procedures ");
    }
    
    public Page<Map<String, Object>> getDataForOracle(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId)
        throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        String orderBy = page.getOrderBy();
        String order = page.getOrder();
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        List<Map<String, Object>> list3 = getPrimaryKeyssForOracle(dbName, tableName, databaseConfigId);
        String tem = "";
        for (Map<String, Object> map : list3) {
            tem = tem + map.get("COLUMN_NAME") + ",";
        }
        String primaryKey = "";
        if (!tem.equals("")) {
            primaryKey = tem.substring(0, tem.length() - 1);
        }
        String sql = "select * from  " + tableName;
        String sql2 = "";
        if ((orderBy == null) || (orderBy.equals(""))) {
            sql2 = "select * from (select rownum rn, t1.* from " + tableName + " t1) where rn between " + limitFrom + " and  " + (limitFrom + pageSize);
        }
        else {
            sql2 = "select * from (select rownum rn, t1.* from " + tableName + " t1) where rn between " + limitFrom + " and  " + (limitFrom + pageSize) + " order by " + orderBy + " " + order;
        }
        List<Map<String, Object>> list = db.queryForList(sql2);
        int rowCount = db.executeQueryForCountForOracle(sql);
        List<Map<String, Object>> columns = getTableColumnsForOracle(dbName, tableName, databaseConfigId);
        List<Map<String, Object>> tempList = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("field", "treeSoftPrimaryKey");
        map1.put("checkbox", Boolean.valueOf(true));
        tempList.add(map1);
        for (Map<String, Object> map : columns) {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("field", map.get("column_name"));
            map2.put("title", map.get("column_name"));
            map2.put("sortable", Boolean.valueOf(true));
            if ((map.get("data_type").equals("DATETIME")) || (map.get("data_type").equals("DATE")) || (map.get("data_type").equals("TIMESTAMP")))  {
                map2.put("editor", "datetimebox");
            } else if ((map.get("data_type").equals("INT")) || (map.get("data_type").equals("SMALLINT")) || (map.get("data_type").equals("TINYINT")))  {
                map2.put("editor", "numberbox");
            } else if (map.get("data_type").equals("DOUBLE"))  {
                map2.put("editor", "numberbox");
            } else {
                map2.put("editor", "text");
            }
            tempList.add(map2);
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";
        page.setTotalCount(rowCount);
        page.setResult(list);
        page.setColumns(jsonfromList);
        page.setPrimaryKey(primaryKey);
        return page;
    }
    
    public List<Map<String, Object>> getPrimaryKeyssForOracle(String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        return db.queryForList(
            "select  COLUMN_NAME from user_cons_columns  where constraint_name= (select  constraint_name  from user_constraints  where table_name = '" + tableName + "' and constraint_type ='P') ");
    }
    
    public List<Map<String, Object>> getTableColumnsForOracle(String dbName, String tableName, String databaseConfigId) {
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        return db.queryForColumnOnly("select * from " + tableName + " where rownum =1 ");
    }
    
    public Page<Map<String, Object>> executeSqlHaveResForOracle(Page<Map<String, Object>> page, String sql, String dbName, String databaseConfigId)
        throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        String sql2 = "SELECT * FROM (SELECT A.*, ROWNUM RN  FROM (  " + sql + " ) A ) WHERE RN BETWEEN " + limitFrom + " AND " + (limitFrom + pageSize);
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForList(sql2);
        int rowCount = db.executeQueryForCountForOracle(sql);
        List<Map<String, Object>> columns = executeSqlForColumnsForOracle(sql, dbName, databaseConfigId);
        List<Map<String, Object>> tempList = new ArrayList<>();
        for (Map<String, Object> map : columns) {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("field", map.get("column_name"));
            map2.put("title", map.get("column_name"));
            map2.put("sortable", Boolean.valueOf(true));
            tempList.add(map2);
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";
        page.setTotalCount(rowCount);
        page.setResult(list);
        page.setColumns(jsonfromList);
        return page;
    }
    
    public List<Map<String, Object>> executeSqlForColumnsForOracle(String sql, String dbName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        return db.executeSqlForColumns("select * from (" + sql + ") where  rownum = 1 ");
    }
    
    public int updateTableNullAbleForOracle(String databaseName, String tableName, String column_name, String is_nullable, String databaseConfigId)
        throws Exception {
        String sql4 = "";
        if ((column_name != null) && (!column_name.equals(""))) {
            BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
            if (is_nullable.equals("true"))  {
                sql4 = " alter table  " + tableName + " modify " + column_name + "  null ";
            } else {
                sql4 = " alter table  " + tableName + " modify " + column_name + "  not null ";
            }
            db.setupdateData(sql4);
        }
        return 0;
    }
    
    public int savePrimaryKeyForOracle(String databaseName, String tableName, String column_name, String isSetting, String databaseConfigId)
        throws Exception {
        String sql4 = "";
        if ((column_name != null) && (!column_name.equals(""))) {
            BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
            List<Map<String, Object>> list2 = selectTablePrimaryKeyForOracle(databaseName, tableName, databaseConfigId);
            List<String> list3 = new ArrayList<>();
            for (Map<String, Object> map : list2)  {
                list3.add((String)map.get("COLUMN_NAME"));
            }
            if (isSetting.equals("true"))  {
                list3.add(column_name);
            } else {
                list3.remove(column_name);
            }
            String tem = list3.toString();
            String primaryKey = tem.substring(1, tem.length() - 1);
            if (list2.size() > 0)  {
                String temp = (String)(list2.get(0)).get("CONSTRAINT_NAME");
                sql4 = " alter table " + tableName + " drop constraint  " + temp;
                db.setupdateData(sql4);
            }
            if (!primaryKey.equals(""))  {
                sql4 = " alter table " + tableName + " add primary key (" + primaryKey + ") ";
                db.setupdateData(sql4);
            }
        }
        return 0;
    }
    
    public List<Map<String, Object>> selectTablePrimaryKeyForOracle(String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        String sql =
            " select a.CONSTRAINT_NAME, a.COLUMN_NAME  from user_cons_columns a, user_constraints b  where a.constraint_name = b.constraint_name and b.constraint_type = 'P'  and a.table_name = '"
                + tableName + "' ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        return db.queryForList(sql);
    }
    
    public int saveDesginColumnForOracle(Map<String, String> map, String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql = " alter table " + tableName + " add  ";
        sql = sql + map.get("COLUMN_NAME") + "  ";
        sql = sql + map.get("DATA_TYPE");
        if ((map.get("CHARACTER_MAXIMUM_LENGTH") != null) && (!(map.get("CHARACTER_MAXIMUM_LENGTH")).equals(""))) {
            sql = sql + " (" + map.get("CHARACTER_MAXIMUM_LENGTH") + ") ";
        }
        if ((map.get("COLUMN_COMMENT") != null) && (!(map.get("COLUMN_COMMENT")).equals(""))) {
            sql = sql + " comment '" + map.get("COLUMN_COMMENT") + "'";
        }
        return db.setupdateData(sql);
    }
    
    public int updateTableColumnForOracle(Map<String, Object> map, String databaseName, String tableName, String columnName, String idValues, String databaseConfigId)
        throws Exception {
        if ((columnName == null) || ("".equals(columnName))) {
            throw new Exception("数据不完整,保存失败!");
        }
        if ((idValues == null) || ("".equals(idValues))) {
            throw new Exception("数据不完整,保存失败!");
        }
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String old_field_name = (String)map.get("TREESOFTPRIMARYKEY");
        String column_name = (String)map.get("COLUMN_NAME");
        String dataType = (String)map.get("DATA_TYPE");
        String character_maximum_length = (String)map.get("CHARACTER_MAXIMUM_LENGTH");
        String column_comment = (String)map.get("COLUMN_COMMENT");
        if (!old_field_name.endsWith(column_name)) {
            String sql = " ALTER TABLE " + tableName + " RENAME COLUMN " + old_field_name + " to  " + column_name;
            db.setupdateData(sql);
        }
        String sql2 = " alter table  " + tableName + " modify  " + column_name + " " + dataType;
        if ((character_maximum_length != null) && (!character_maximum_length.equals(""))) {
            sql2 = sql2 + " (" + character_maximum_length + ")";
        }
        int y = db.setupdateData(sql2);
        if ((column_comment != null) && (!column_comment.equals(""))) {
            String sql4 = "  comment on column " + tableName + "." + column_name + " is '" + column_comment + "' ";
            db.setupdateData(sql4);
        }
        return y;
    }
    
    public int deleteTableColumnForOracle(String databaseName, String tableName, String[] ids, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        int y = 0;
        for (int i = 0; i < ids.length; i++) {
            String sql = " alter table " + tableName + " drop (" + ids[i] + ")";
            y += db.setupdateData(sql);
        }
        return y;
    }
    
    public int saveRowsForOracle(Map<String, String> map, String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql = " insert into  " + tableName;
        String colums = " ";
        String values = " ";
        String columnType = "";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!entry.getKey().equals("RN"))  {
                colums = colums + entry.getKey() + ",";
                columnType = selectColumnTypeForOracle(databaseName, tableName, entry.getKey(), databaseConfigId);
                String str = entry.getValue();
                if (str.equals("")) {
                    values = values + " null,";
                } else if (columnType.equals("DATE")) {
                    values = values + " to_date('" + entry.getValue() + "','yyyy-mm-dd hh24:mi:ss'),";
                } else if (columnType.indexOf("TIMESTAMP") >= 0) {
                    values = values + " to_date('" + entry.getValue() + "','yyyy-mm-dd hh24:mi:ss'),";
                } else if (columnType.equals("NUMBER")) {
                    values = values + entry.getValue() + ",";
                } else if (columnType.equals("INTEGER")) {
                    values = values + entry.getValue() + ",";
                } else if (columnType.equals("FLOAT")) {
                    values = values + entry.getValue() + ",";
                } else if (columnType.equals("BINARY_FLOAT")) {
                    values = values + entry.getValue() + ",";
                } else if (columnType.equals("BINARY_DOUBLE")) {
                    values = values + entry.getValue() + ",";
                } else {
                    values = values + "'" + entry.getValue() + "',";
                }
            }
        }
        colums = colums.substring(0, colums.length() - 1);
        values = values.substring(0, values.length() - 1);
        sql = sql + " (" + colums + ") values (" + values + ")";
        return db.setupdateData(sql);
    }
    
    public String selectColumnTypeForOracle(String databaseName, String tableName, String column, String databaseConfigId)
        throws Exception {
        String sql = " select DATA_TYPE from user_tab_columns where table_name ='" + tableName + "' AND COLUMN_NAME ='" + column + "' ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForList(sql);
        return (String)(list.get(0)).get("DATA_TYPE");
    }
    
    public int deleteRowsNewForOracle(String databaseName, String tableName, String primary_key, List<String> condition, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        int y = 0;
        for (int i = 0; i < condition.size(); i++) {
            String whereStr = condition.get(i);
            String sql = " delete from  " + tableName + " where  1=1 " + whereStr;
            y += db.setupdateData(sql);
        }
        return y;
    }
    
    public List<Map<String, Object>> getAllDataBaseForPostgreSQL(String databaseConfigId)
        throws Exception {
        Map<String, Object> map0 = getConfig(databaseConfigId);
        String databaseName = (String)map0.get("databaseName");
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql = " select  datname as  \"SCHEMA_NAME\" from pg_database  where  datname not like 'template%'  ";
        return db.queryForList(sql);
    }
    
    public List<Map<String, Object>> getAllTablesForPostgreSQL(String dbName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        return db.queryForList("select  tablename as \"TABLE_NAME\" from pg_tables  where schemaname='public'  ");
    }
    
    public List<Map<String, Object>> getTableColumns3ForPostgreSQL(String dbName, String tableName, String databaseConfigId)
        throws Exception {
        String sql =
            " select t1.column_name as \"TREESOFTPRIMARYKEY\", t1.COLUMN_NAME as \"COLUMN_NAME\", t1.DATA_TYPE as \"COLUMN_TYPE\", t1.DATA_TYPE as \"DATA_TYPE\", character_maximum_length as \"CHARACTER_MAXIMUM_LENGTH\", t1.IS_NULLABLE as \"IS_NULLABLE\", '' as \"COLUMN_COMMENT\", CASE  WHEN t2.COLUMN_NAME IS NULL THEN ''  ELSE 'PRI'  END AS \"COLUMN_KEY\" from information_schema.columns t1  left join information_schema.constraint_column_usage t2  on t1.table_name = t2.table_name  and t1.COLUMN_NAME = t2.COLUMN_NAME where  t1.table_name='"
                + tableName + "'  ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        return db.queryForList(sql);
    }
    
    public List<Map<String, Object>> getAllViewsForPostgreSQL(String dbName, String databaseConfigId)
        throws Exception {
        String sql = "select viewname as \"TABLE_NAME\"  from pg_views  where schemaname='public'  ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        return db.queryForList(sql);
    }
    
    public List<Map<String, Object>> getAllFuntionForPostgreSQL(String dbName, String databaseConfigId)
        throws Exception {
        String sql = "select prosrc as \"ROUTINE_NAME\" from pg_proc where 1=2  ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        return db.queryForList(sql);
    }
    
    public Page<Map<String, Object>> getDataForPostgreSQL(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId)
        throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        String orderBy = page.getOrderBy();
        String order = page.getOrder();
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        tableName = "\"" + tableName + "\"";
        List<Map<String, Object>> list3 = getPrimaryKeyssForPostgreSQL(dbName, tableName, databaseConfigId);
        String tem = "";
        for (Map<String, Object> map : list3) {
            tem = tem + map.get("COLUMN_NAME") + ",";
        }
        String primaryKey = "";
        if (!tem.equals("")) {
            primaryKey = tem.substring(0, tem.length() - 1);
        }
        String sql = "select * from  " + tableName;
        String sql2 = "";
        if ((orderBy == null) || (orderBy.equals(""))) {
            sql2 = "select  *  from  " + tableName + "  LIMIT " + pageSize + " OFFSET  " + limitFrom;
        }
        else {
            sql2 = "select  *  from  " + tableName + " order by " + orderBy + " " + order + "  LIMIT " + pageSize + "  OFFSET " + limitFrom;
        }
        List<Map<String, Object>> list = db.queryForListForPostgreSQL(sql2);
        int rowCount = db.executeQueryForCountForPostgreSQL(sql);
        List<Map<String, Object>> columns = getTableColumnsForPostgreSQL(dbName, tableName, databaseConfigId);
        List<Map<String, Object>> tempList = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("field", "treeSoftPrimaryKey");
        map1.put("checkbox", Boolean.valueOf(true));
        tempList.add(map1);
        for (Map<String, Object> map : columns) {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("field", map.get("column_name"));
            map2.put("title", map.get("column_name"));
            map2.put("sortable", Boolean.valueOf(true));
            logger.info("{}", map.get("data_type"));
            if ((map.get("data_type").equals("DATETIME")) || (map.get("data_type").equals("DATE")) || (map.get("data_type").equals("date")) || (map.get("data_type").equals("timestamp")))  {
                map2.put("editor", "datetimebox");
            } else if ((map.get("data_type").equals("integer")) || (map.get("data_type").equals("float4")) || (map.get("data_type").equals("numeric")) || (map.get("data_type").equals("int4")))  {
                map2.put("editor", "numberbox");
            } else if (map.get("data_type").equals("DOUBLE"))  {
                map2.put("editor", "numberbox");
            } else if ((map.get("data_type").equals("bpchar")) || (map.get("data_type").equals("varchar")))  {
                map2.put("editor", "text");
            }
            tempList.add(map2);
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";
        page.setTotalCount(rowCount);
        page.setResult(list);
        page.setColumns(jsonfromList);
        page.setPrimaryKey(primaryKey);
        return page;
    }
    
    public Page<Map<String, Object>> executeSqlHaveResForPostgreSQL(Page<Map<String, Object>> page, String sql, String dbName, String databaseConfigId)
        throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        String sql2 = "select  *  from  (" + sql + ") t  LIMIT " + pageSize + " OFFSET  " + limitFrom;
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListForPostgreSQL(sql2);
        int rowCount = db.executeQueryForCountForPostgreSQL(sql);
        List<Map<String, Object>> columns = executeSqlForColumnsForPostgreSQL(sql, dbName, databaseConfigId);
        List<Map<String, Object>> tempList = new ArrayList<>();
        for (Map<String, Object> map : columns) {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("field", map.get("column_name"));
            map2.put("title", map.get("column_name"));
            map2.put("sortable", Boolean.valueOf(true));
            tempList.add(map2);
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";
        page.setTotalCount(rowCount);
        page.setResult(list);
        page.setColumns(jsonfromList);
        return page;
    }
    
    public int deleteRowsNewForPostgreSQL(String databaseName, String tableName, String primary_key, List<String> condition, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        int y = 0;
        for (int i = 0; i < condition.size(); i++) {
            String whereStr = condition.get(i);
            String sql = " delete from  " + tableName + " where  1=1 " + whereStr;
            y += db.setupdateData(sql);
        }
        return y;
    }
    
    public int saveRowsForPostgreSQL(Map<String, String> map, String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql = " insert into  \"" + tableName + "\"";
        int y = 0;
        String colums = " ";
        String values = " ";
        String columnType = "";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            colums = colums + "\"" + entry.getKey() + "\",";
            columnType = selectColumnTypeForPostgreSQL(databaseName, tableName, entry.getKey(), databaseConfigId);
            String str = entry.getValue();
            if (str.equals(""))  {
                values = values + " null,";
            } else if (columnType.equals("integer"))  {
                values = values + entry.getValue() + ",";
            } else if (columnType.equals("numeric"))  {
                values = values + entry.getValue() + ",";
            } else if (columnType.equals("real"))  {
                values = values + entry.getValue() + ",";
            } else {
                values = values + "'" + entry.getValue() + "',";
            }
        }
        colums = colums.substring(0, colums.length() - 1);
        values = values.substring(0, values.length() - 1);
        sql = sql + " (" + colums + ") values (" + values + ")";
        y = db.setupdateData(sql);
        return y;
    }
    
    public int saveDesginColumnForPostgreSQL(Map<String, String> map, String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        tableName = "\"" + tableName + "\"";
        String sql = " alter table " + tableName + " add  ";
        sql = sql + "\"" + map.get("COLUMN_NAME") + "\"  ";
        sql = sql + map.get("DATA_TYPE");
        if ((map.get("CHARACTER_MAXIMUM_LENGTH") != null) && (!(map.get("CHARACTER_MAXIMUM_LENGTH")).equals(""))) {
            sql = sql + " (" + map.get("CHARACTER_MAXIMUM_LENGTH") + ") ";
        }
        if ((map.get("COLUMN_COMMENT") != null) && (!(map.get("COLUMN_COMMENT")).equals(""))) {
            sql = sql + " comment '" + map.get("COLUMN_COMMENT") + "'";
        }
        return db.setupdateData(sql);
    }
    
    public int updateTableColumnForPostgreSQL(Map<String, Object> map, String databaseName, String tableName, String columnName, String idValues, String databaseConfigId)
        throws Exception {
        if ((columnName == null) || ("".equals(columnName))) {
            throw new Exception("数据不完整,保存失败!");
        }
        if ((idValues == null) || ("".equals(idValues))) {
            throw new Exception("数据不完整,保存失败!");
        }
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        tableName = "\"" + tableName + "\"";
        String old_field_name = "\"" + map.get("TREESOFTPRIMARYKEY") + "\"";
        String column_name = "\"" + map.get("COLUMN_NAME") + "\"";
        String dataType = (String)map.get("DATA_TYPE");
        String character_maximum_length = (String)map.get("CHARACTER_MAXIMUM_LENGTH");
        String column_comment = (String)map.get("COLUMN_COMMENT");
        if (!old_field_name.endsWith(column_name)) {
            String sql = " ALTER TABLE " + tableName + " RENAME COLUMN " + old_field_name + " to  " + column_name;
            db.setupdateData(sql);
        }
        String sql2 = " alter table  " + tableName + " alter column  " + column_name + " type " + dataType;
        if ((character_maximum_length != null) && (!character_maximum_length.equals(""))) {
            sql2 = sql2 + " (" + character_maximum_length + ")";
        }
        int y = db.setupdateData(sql2);
        if ((column_comment != null) && (!column_comment.equals(""))) {
            String sql4 = "  comment on column " + tableName + "." + column_name + " is '" + column_comment + "' ";
            db.setupdateData(sql4);
        }
        return y;
    }
    
    public int deleteTableColumnForPostgreSQL(String databaseName, String tableName, String[] ids, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        int y = 0;
        for (int i = 0; i < ids.length; i++) {
            String sql = " alter table " + tableName + " drop (" + ids[i] + ")";
            y += db.setupdateData(sql);
        }
        return y;
    }
    
    public int updateTableNullAbleForPostgreSQL(String databaseName, String tableName, String column_name, String is_nullable, String databaseConfigId)
        throws Exception {
        String sql4 = "";
        if ((column_name != null) && (!column_name.equals(""))) {
            BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
            if (is_nullable.equals("true"))  {
                sql4 = " alter table  " + tableName + " alter column " + column_name + " drop not null ";
            } else {
                sql4 = " alter table  " + tableName + " alter column " + column_name + " set not null ";
            }
            db.setupdateData(sql4);
        }
        return 0;
    }
    
    public int savePrimaryKeyForPostgreSQL(String databaseName, String tableName, String column_name, String isSetting, String databaseConfigId)
        throws Exception {
        String sql4 = "";
        if ((column_name != null) && (!column_name.equals(""))) {
            BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
            List<Map<String, Object>> list2 = selectTablePrimaryKeyForPostgreSQL(databaseName, tableName, databaseConfigId);
            List<String> list3 = new ArrayList<>();
            for (Map<String, Object> map : list2)  {
                list3.add((String)map.get("COLUMN_NAME"));
            }
            if (isSetting.equals("true"))  {
                list3.add(column_name);
            } else {
                list3.remove(column_name);
            }
            String tem = list3.toString();
            String primaryKey = tem.substring(1, tem.length() - 1);
            if (list2.size() > 0)  {
                String temp = (String)(list2.get(0)).get("CONSTRAINT_NAME");
                sql4 = " alter table " + tableName + " drop constraint  " + temp;
                db.setupdateData(sql4);
            }
            if (!primaryKey.equals(""))  {
                sql4 = " alter table " + tableName + " add primary key (" + primaryKey + ") ";
                db.setupdateData(sql4);
            }
        }
        return 0;
    }
    
    public List<Map<String, Object>> getPrimaryKeyssForPostgreSQL(String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        String sql =
            " select  pg_attribute.attname as \"COLUMN_NAME\" from pg_constraint  inner join pg_class  on pg_constraint.conrelid = pg_class.oid  inner join pg_attribute on pg_attribute.attrelid = pg_class.oid  and  pg_attribute.attnum = pg_constraint.conkey[1] inner join pg_type on pg_type.oid = pg_attribute.atttypid  where pg_class.relname = '"
                + tableName + "'  " + " and pg_constraint.contype='p' ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        return db.queryForList(sql);
    }
    
    public List<Map<String, Object>> getTableColumnsForPostgreSQL(String dbName, String tableName, String databaseConfigId) {
        String sql = "select  * from " + tableName + " limit 1 ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        return db.queryForColumnOnly(sql);
    }
    
    public String selectColumnTypeForPostgreSQL(String databaseName, String tableName, String column, String databaseConfigId)
        throws Exception {
        String sql = " select dataType as \"DATA_TYPE\"  from  information_schema.columns  where  table_name ='" + tableName + "' AND COLUMN_NAME ='" + column + "' ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForList(sql);
        return (String)(list.get(0)).get("DATA_TYPE");
    }
    
    public List<Map<String, Object>> selectTablePrimaryKeyForPostgreSQL(String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        String sql =
            " select pg_constraint.conname as \"CONSTRAINT_NAME\",pg_attribute.attname as \"COLUMN_NAME\",pg_type.typname as typename from pg_constraint  inner join pg_class on pg_constraint.conrelid = pg_class.oid  inner join pg_attribute on pg_attribute.attrelid = pg_class.oid  and  pg_attribute.attnum = pg_constraint.conkey[1] inner join pg_type on pg_type.oid = pg_attribute.atttypid  where pg_class.relname = '"
                + tableName + "'  and pg_constraint.contype='p' ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        return db.queryForList(sql);
    }
    
    public List<Map<String, Object>> executeSqlForColumnsForPostgreSQL(String sql, String dbName, String databaseConfigId)
        throws Exception {
        String sql2 = " select * from (" + sql + ") t limit 1; ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        return db.executeSqlForColumns(sql2);
    }
    
    public String getViewSqlForPostgreSQL(String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        String sql = " select  view_definition  from  information_schema.views  where  table_name='" + tableName + "' and table_catalog='" + databaseName + "'  ";
        String str = " ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForList(sql);
        if (list.size() == 1) {
            Map<String, Object> map = list.get(0);
            str = (String)map.get("view_definition");
        }
        return str;
    }
    
    public List<Map<String, Object>> getAllDataBaseForMSSQL(String databaseConfigId)
        throws Exception {
        String sql = " SELECT name as SCHEMA_NAME FROM sys.databases where state='0' ORDER BY name  ";
        Map<String, Object> map12 = getConfig(databaseConfigId);
        String databaseName = (String)map12.get("databaseName");
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
            list = db.queryForList(sql);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        List<Map<String, Object>> list2 = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("SCHEMA_NAME", databaseName);
        list2.add(map);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map2 = list.get(i);
            String schemaName = (String)map2.get("SCHEMA_NAME");
            if (!schemaName.equals(databaseName))  {
                list2.add(map2);
            }
        }
        return list2;
    }
    
    public List<Map<String, Object>> getAllDataBaseForHive2(String databaseConfigId)
        throws Exception {
        String sql = " show databases ";
        Map<String, Object> map12 = getConfig(databaseConfigId);
        String databaseName = (String)map12.get("databaseName");
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
            list = db.queryForList(sql);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        List<Map<String, Object>> list2 = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("SCHEMA_NAME", databaseName);
        list2.add(map);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map2 = list.get(i);
            String schemaName = (String)map2.get("database_name");
            if (!schemaName.equals(databaseName))  {
                map2.put("SCHEMA_NAME", schemaName);
                list2.add(map2);
            }
        }
        return list2;
    }
    
    public List<Map<String, Object>> getAllTablesForMSSQL(String dbName, String databaseConfigId) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
            String sql = " SELECT Name as TABLE_NAME FROM " + dbName + "..SysObjects Where XType='U' ORDER BY Name ";
            return db.queryForList(sql);
        } catch (Exception e) {
        }
        return list;
    }
    
    public List<Map<String, Object>> getTableColumns3ForMSSQL(String dbName, String tableName, String databaseConfigId)
        throws Exception {
        String sql =
            " select b.name TREESOFTPRIMARYKEY, b.name COLUMN_NAME, ISNULL( c.name +'('+  cast(b.length as varchar(10)) +')', c.name ) as  COLUMN_TYPE, c.name DATA_TYPE, b.length CHARACTER_MAXIMUM_LENGTH, case when b.isnullable=1  then 'YES' else 'NO' end as IS_NULLABLE, '' as COLUMN_COMMENT, (SELECT 'PRI' FROM sysobjects where xtype='PK' and  parent_obj=b.id and name in (  SELECT name  FROM sysindexes WHERE indid in(  SELECT indid FROM sysindexkeys WHERE id = b.id AND colid=b.colid  ))) as COLUMN_KEY  from sysobjects a,syscolumns b,systypes c  where a.id=b.id  and a.name='"
                + tableName + "' and a.xtype='U'  and b.xtype=c.xtype and c.name<>'sysname' ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForList2(sql);
        List<Map<String, Object>> tempList = new ArrayList<>();
        for (Map<String, Object> mmap : list) {
            String dataType = (String)mmap.get("DATA_TYPE");
            if (dataType.equals("nvarchar"))  {
                int leng = ((Short)mmap.get("CHARACTER_MAXIMUM_LENGTH")).shortValue();
                mmap.put("CHARACTER_MAXIMUM_LENGTH", Integer.valueOf(leng / 2));
            }
            tempList.add(mmap);
        }
        return tempList;
    }
    
    public List<Map<String, Object>> getAllViewsForMSSQL(String dbName, String databaseConfigId)
        throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            String sql = "  SELECT  NAME AS TABLE_NAME FROM  sysobjects where XTYPE ='V'  ";
            BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
            list = db.queryForList(sql);
        } catch (Exception localException) {
        }
        return list;
    }
    
    public List<Map<String, Object>> getAllViewsForHive2(String databaseName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql = " use " + databaseName;
        db.setupdateData(sql);
        String sql2 = "show views ";
        List<Map<String, Object>> list = db.queryForList(sql2);
        List<Map<String, Object>> list2 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> map2 = list.get(i);
            map.put("TABLE_NAME", map2.get("tab_name"));
            list2.add(map);
        }
        return list2;
    }
    
    public List<Map<String, Object>> getAllFuntionForMSSQL(String dbName, String databaseConfigId)
        throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            String sql = " SELECT  NAME AS ROUTINE_NAME FROM  sysobjects where XTYPE ='FN' ";
            BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
            list = db.queryForList(sql);
        } catch (Exception localException) {
        }
        return list;
    }
    
    public Page<Map<String, Object>> getDataForMSSQL(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId)
        throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        if (limitFrom > 0) {
            limitFrom--;
        }
        String orderBy = page.getOrderBy();
        String order = page.getOrder();
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        List<Map<String, Object>> list3 = getPrimaryKeyssForMSSQL(dbName, tableName, databaseConfigId);
        String tem = "";
        for (Map<String, Object> map : list3) {
            tem = tem + map.get("COLUMN_NAME") + ",";
        }
        String primaryKey = "";
        if (!tem.equals("")) {
            primaryKey = tem.substring(0, tem.length() - 1);
        }
        String sql = "select * from  " + tableName;
        String sql2 = "";
        if ((orderBy == null) || (orderBy.equals(""))) {
            sql2 = "select * from  " + tableName;
        }
        else {
            sql2 = "select * from  " + tableName + " order by " + orderBy + " " + order;
        }
        List<Map<String, Object>> list = db.queryForListPageForMSSQL(sql2, pageNo * pageSize, (pageNo - 1) * pageSize);
        int rowCount = db.executeQueryForCountForPostgreSQL(sql);
        List<Map<String, Object>> columns = getTableColumnsForMSSQL(dbName, tableName, databaseConfigId);
        List<Map<String, Object>> tempList = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("field", "treeSoftPrimaryKey");
        map1.put("checkbox", Boolean.valueOf(true));
        tempList.add(map1);
        String dataType = "";
        for (Map<String, Object> map : columns) {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("field", map.get("column_name"));
            map2.put("title", map.get("column_name"));
            map2.put("sortable", Boolean.valueOf(true));
            dataType = map.get("data_type").toString().toUpperCase();
            if ((dataType.equals("DATETIME")) || (dataType.equals("DATE")))  {
                map2.put("editor", "datetimebox");
            } else if ((dataType.equals("INT")) || (dataType.equals("SMALLINT")) || (dataType.equals("TINYINT")))  {
                map2.put("editor", "numberbox");
            } else if (dataType.equals("DOUBLE"))  {
                map2.put("editor", "numberbox");
            } else {
                map2.put("editor", "text");
            }
            tempList.add(map2);
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";
        page.setTotalCount(rowCount);
        page.setResult(list);
        page.setColumns(jsonfromList);
        page.setPrimaryKey(primaryKey);
        return page;
    }
    
    public Page<Map<String, Object>> getDataForHive2(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId)
        throws Exception {
        String orderBy = page.getOrderBy();
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        String sql_use = " use  " + dbName;
        db.setupdateData(sql_use);
        String sql2 = "";
        if ((orderBy == null) || (orderBy.equals(""))) {
            sql2 = "select * from " + tableName + " limit 20 ";
        }
        else {
            sql2 = "select * from " + tableName + " limit 20 ";
        }
        List<Map<String, Object>> list = db.queryForList(sql2);
        int rowCount = 20;
        List<Map<String, Object>> tempList = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("field", "treeSoftPrimaryKey");
        map1.put("checkbox", Boolean.valueOf(true));
        tempList.add(map1);
        if (!list.isEmpty()) {
            Map<String, Object> map2 = list.get(0);
            for (Map.Entry<String, Object> entry : map2.entrySet())  {
                Map<String, Object> map3 = new HashMap<>();
                map3.put("field", entry.getKey());
                map3.put("title", entry.getKey());
                map3.put("sortable", Boolean.valueOf(true));
                tempList.add(map3);
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";
        page.setTotalCount(rowCount);
        page.setResult(list);
        page.setColumns(jsonfromList);
        return page;
    }
    
    public Page<Map<String, Object>> executeSqlHaveResForMSSQL(Page<Map<String, Object>> page, String sql, String dbName, String databaseConfigId)
        throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        if (limitFrom > 0) {
            limitFrom--;
        }
        String sql2 = " select  * from (" + sql + ")  t1  ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListPageForMSSQL(sql2, pageNo * pageSize, (pageNo - 1) * pageSize);
        int rowCount = db.executeQueryForCountForPostgreSQL(sql);
        List<Map<String, Object>> columns = executeSqlForColumnsForMSSQL(sql, dbName, databaseConfigId);
        List<Map<String, Object>> tempList = new ArrayList<>();
        for (Map<String, Object> map : columns) {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("field", map.get("column_name"));
            map2.put("title", map.get("column_name"));
            map2.put("sortable", Boolean.valueOf(true));
            tempList.add(map2);
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";
        page.setTotalCount(rowCount);
        page.setResult(list);
        page.setColumns(jsonfromList);
        return page;
    }
    
    public Page<Map<String, Object>> executeSqlHaveResForHive2(Page<Map<String, Object>> page, String sql, String dbName, String databaseConfigId)
        throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        if (limitFrom > 0) {
            limitFrom--;
        }
        String sql2 = " select  * from (" + sql + ")  t1  ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListPageForHive2(sql2, pageNo * pageSize, (pageNo - 1) * pageSize);
        int rowCount = 20;
        List<Map<String, Object>> tempList = new ArrayList<>();
        if (!list.isEmpty()) {
            Map<String, Object> map2 = list.get(0);
            for (Map.Entry<String, Object> entry : map2.entrySet())  {
                Map<String, Object> map3 = new HashMap<>();
                map3.put("field", entry.getKey());
                map3.put("title", entry.getKey());
                map3.put("sortable", Boolean.valueOf(true));
                tempList.add(map3);
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";
        page.setTotalCount(rowCount);
        page.setResult(list);
        page.setColumns(jsonfromList);
        return page;
    }
    
    public int deleteRowsNewForMSSQL(String databaseName, String tableName, String primary_key, List<String> condition, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        int y = 0;
        for (int i = 0; i < condition.size(); i++) {
            String whereStr = condition.get(i);
            String sql = " delete from  " + tableName + " where  1=1 " + whereStr;
            y += db.setupdateData(sql);
        }
        return y;
    }
    
    public String getViewSqlForMSSQL(String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        String sql = " select  view_definition  from  information_schema.views  where  table_name='" + tableName + "' and table_catalog='" + databaseName + "'  ";
        String str = " ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForList(sql);
        if (list.size() == 1) {
            Map<String, Object> map = list.get(0);
            str = (String)map.get("view_definition");
        }
        return str;
    }
    
    public int saveRowsForMSSQL(Map<String, String> map, String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql = " insert into  " + tableName;
        int y = 0;
        String colums = " ";
        String values = " ";
        String columnType = "";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            colums = colums + entry.getKey() + ",";
            columnType = selectOneColumnTypeForMSSQL(databaseName, tableName, entry.getKey(), databaseConfigId);
            String str = entry.getValue();
            if (str.equals(""))  {
                values = values + " null,";
            } else if (columnType.equals("DATE"))  {
                values = values + " to_date('" + entry.getValue() + "','yyyy-mm-dd hh24:mi:ss'),";
            } else {
                values = values + "'" + entry.getValue() + "',";
            }
        }
        colums = colums.substring(0, colums.length() - 1);
        values = values.substring(0, values.length() - 1);
        sql = sql + " (" + colums + ") values (" + values + ")";
        y = db.setupdateData(sql);
        return y;
    }
    
    public int saveDesginColumnForMSSQL(Map<String, String> map, String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql = " alter table " + tableName + " add  ";
        sql = sql + map.get("COLUMN_NAME") + "  ";
        sql = sql + map.get("DATA_TYPE");
        if ((map.get("CHARACTER_MAXIMUM_LENGTH") != null) && (!(map.get("CHARACTER_MAXIMUM_LENGTH")).equals(""))) {
            sql = sql + " (" + map.get("CHARACTER_MAXIMUM_LENGTH") + ") ";
        }
        if ((map.get("COLUMN_COMMENT") != null) && (!(map.get("COLUMN_COMMENT")).equals(""))) {
            sql = sql + " comment '" + map.get("COLUMN_COMMENT") + "'";
        }
        return db.setupdateData(sql);
    }
    
    public int updateTableColumnForMSSQL(Map<String, Object> map, String databaseName, String tableName, String columnName, String idValues, String databaseConfigId)
        throws Exception {
        if ((columnName == null) || ("".equals(columnName))) {
            throw new Exception("数据不完整,保存失败!");
        }
        if ((idValues == null) || ("".equals(idValues))) {
            throw new Exception("数据不完整,保存失败!");
        }
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String old_field_name = (String)map.get("TREESOFTPRIMARYKEY");
        String column_name = (String)map.get("COLUMN_NAME");
        String dataType = (String)map.get("DATA_TYPE");
        String character_maximum_length = (String)map.get("CHARACTER_MAXIMUM_LENGTH");
        String column_comment = (String)map.get("COLUMN_COMMENT");
        if (!old_field_name.endsWith(column_name)) {
            String sql = " exec sp_rename '" + tableName + "." + old_field_name + "','" + column_name + "','COLUMN'";
            db.setupdateData(sql);
        }
        String sql2 = " alter table  " + tableName + " alter column " + column_name + " " + dataType;
        if ((!dataType.equals("int")) && (!dataType.equals("date")) && (character_maximum_length != null) && (!character_maximum_length.equals(""))) {
            sql2 = sql2 + " (" + character_maximum_length + ")";
        }
        int y = db.setupdateData(sql2);
        if ((column_comment != null) && (!column_comment.equals(""))) {
            String sql4 = "exec sp_addextendedproperty 'MS_Description','" + column_comment + "','user','dbo','TABLE','" + tableName + "','column','" + column_name + "' ";
            db.setupdateData(sql4);
        }
        return y;
    }
    
    public int deleteTableColumnForMSSQL(String databaseName, String tableName, String[] ids, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        int y = 0;
        for (int i = 0; i < ids.length; i++) {
            String sql = " alter table " + tableName + " drop (" + ids[i] + ")";
            y += db.setupdateData(sql);
        }
        return y;
    }
    
    public int updateTableNullAbleForMSSQL(String databaseName, String tableName, String column_name, String is_nullable, String databaseConfigId)
        throws Exception {
        String sql4 = "";
        if ((column_name != null) && (!column_name.equals(""))) {
            BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
            String column_type = selectOneColumnTypeForMSSQL(databaseName, tableName, column_name, databaseConfigId);
            if (is_nullable.equals("true"))  {
                sql4 = " alter table  " + tableName + " alter column " + column_name + " " + column_type + " " + "  null ";
            } else {
                sql4 = " alter table  " + tableName + " alter column " + column_name + " " + column_type + " " + "  not null ";
            }
            db.setupdateData(sql4);
        }
        return 0;
    }
    
    public int savePrimaryKeyForMSSQL(String databaseName, String tableName, String column_name, String isSetting, String databaseConfigId)
        throws Exception {
        String sql4 = "";
        if ((column_name != null) && (!column_name.equals(""))) {
            BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
            List<Map<String, Object>> list2 = selectTablePrimaryKeyForMSSQL(databaseName, tableName, databaseConfigId);
            List<String> list3 = new ArrayList<>();
            for (Map<String, Object> map : list2)  {
                list3.add((String)map.get("COLUMN_NAME"));
            }
            if (isSetting.equals("true"))  {
                list3.add(column_name);
            } else {
                list3.remove(column_name);
            }
            String tem = list3.toString();
            String primaryKey = tem.substring(1, tem.length() - 1);
            if (list2.size() > 0)  {
                String temp = (String)(list2.get(0)).get("CONSTRAINT_NAME");
                sql4 = " alter table " + tableName + " drop constraint  " + temp;
                db.setupdateData(sql4);
            }
            if (!primaryKey.equals(""))  {
                sql4 = " alter table " + tableName + " add primary key (" + primaryKey + ") ";
                db.setupdateData(sql4);
            }
        }
        return 0;
    }
    
    public List<Map<String, Object>> selectTablePrimaryKeyForMSSQL(String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        String sql =
            "  select  b.CONSTRAINT_NAME, b.COLUMN_NAME  from information_schema.table_constraints a  inner join information_schema.constraint_column_usage b  on a.constraint_name = b.constraint_name  where a.constraint_type = 'PRIMARY KEY' and a.table_name = '"
                + tableName + "'";
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        return db.queryForList(sql);
    }
    
    public List<Map<String, Object>> getPrimaryKeyssForMSSQL(String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        String sql =
            " select  c.name as COLUMN_NAME from sysindexes i join sysindexkeys k on i.id = k.id and i.indid = k.indid  join sysobjects o on i.id = o.id  join syscolumns c on i.id=c.id and k.colid = c.colid  where o.xtype = 'U' and exists(select 1 from sysobjects where  xtype = 'PK'  and name = i.name) and o.name='"
                + tableName + "' ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        return db.queryForList(sql);
    }
    
    public List<Map<String, Object>> getTableColumnsForMSSQL(String dbName, String tableName, String databaseConfigId) {
        String sql = "select top 1 * from " + tableName;
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        return db.queryForColumnOnly(sql);
    }
    
    public List<Map<String, Object>> executeSqlForColumnsForMSSQL(String sql, String dbName, String databaseConfigId)
        throws Exception {
        String sql2 = " select top 1 * from (" + sql + ") t  ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        return db.executeSqlForColumns(sql2);
    }
    
    public String selectOneColumnTypeForMSSQL(String databaseName, String tableName, String column_name, String databaseConfigId)
        throws Exception {
        String sql = " select  ISNULL( c.name +'('+  cast(b.length as varchar(10)) +')', c.name ) as  column_type  from sysobjects a,syscolumns b,systypes c  where a.id=b.id  and a.name='" + tableName
            + "'  and  b.name='" + column_name + "' and a.xtype='U'  and b.xtype=c.xtype ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForList(sql);
        return (String)(list.get(0)).get("column_type");
    }
    
    public boolean copyTableForMySql(String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        String sql4 = "create table " + databaseName + "." + tableName + "_" + DateUtil.getDateTimeString() + "  select * from " + databaseName + "." + tableName;
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        db.setupdateData(sql4);
        return true;
    }
    
    public boolean copyTableForOracle(String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        String sql4 = "create table " + tableName + "_" + DateUtil.getDateTimeString() + " as select * from " + tableName;
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        db.setupdateData(sql4);
        return true;
    }
    
    public boolean copyTableForPostgreSQL(String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        String sql4 = "create table \"" + tableName + "_" + DateUtil.getDateTimeString() + "\" as select * from \"" + tableName + "\"";
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        db.setupdateData(sql4);
        return true;
    }
    
    public boolean renameTableForMySql(String databaseName, String tableName, String databaseConfigId, String newTableName)
        throws Exception {
        String sql4 = " rename table " + tableName + " TO  " + newTableName;
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        db.setupdateData(sql4);
        return true;
    }
    
    public boolean renameTableForOracle(String databaseName, String tableName, String databaseConfigId, String newTableName)
        throws Exception {
        String sql4 = " alter table " + tableName + " rename to  " + newTableName;
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        db.setupdateData(sql4);
        return true;
    }
    
    public boolean renameTableForPostgreSQL(String databaseName, String tableName, String databaseConfigId, String newTableName)
        throws Exception {
        tableName = "\"" + tableName + "\"";
        newTableName = "\"" + newTableName + "\"";
        String sql4 = " alter table " + tableName + " rename to  " + newTableName;
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        db.setupdateData(sql4);
        return true;
    }
    
    public boolean renameTableForHive2(String databaseName, String tableName, String databaseConfigId, String newTableName)
        throws Exception {
        tableName = "\"" + tableName + "\"";
        newTableName = "\"" + newTableName + "\"";
        String sql4 = " alter table " + tableName + " rename to  " + newTableName;
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        db.setupdateData(sql4);
        return true;
    }

    public List<Map<String, Object>> getAllTablesForHive2(String databaseName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql = " use " + databaseName;
        db.setupdateData(sql);
        String sql2 = "show tables ";
        List<Map<String, Object>> list = new ArrayList<>();
        list = db.queryForList(sql2);
        List<Map<String, Object>> list2 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> map2 = list.get(i);
            map.put("TABLE_NAME", map2.get("tab_name"));
            list2.add(map);
        }
        return list2;
    }
    
    public Map<String, Object> getConfig(String id) {
        String sql = "select id, name, database_type as databaseType, ip, port, database_name as databaseName, user_name as userName, password, is_default from treesoft_config where id=?";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, id);
        String password = CryptoUtil.decode(map.get("password").toString());
        if (password.split("`").length > 1) {
            password = password.split("`")[1];
        }
        else {
            password = "";
        }
        map.put("password", password);
        return map;
    }
    
    public boolean deleteBackupFile(String[] ids, String path)
        throws Exception {
        for (int i = 0; i < ids.length; i++) {
            File f = new File(path + ids[i]);
            if (f.exists())  {
                f.delete();
            }
        }
        return true;
    }
    
    public boolean dropTable(String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql = " drop  table " + databaseName + "." + tableName;
        db.setupdateData(sql);
        return true;
    }
    
    public boolean dropTableForOracle(String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql = " drop  table " + tableName;
        db.setupdateData(sql);
        return true;
    }
    
    public boolean dropTableForPostgreSQL(String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        tableName = "\"" + tableName + "\"";
        String sql = " drop  table " + tableName;
        db.setupdateData(sql);
        return true;
    }
    
    public boolean dropDatabase(String databaseName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql = " drop  database " + databaseName;
        db.setupdateData(sql);
        return true;
    }
    
    public boolean clearTable(String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        tableName = "\"" + tableName + "\"";
        String sql = " delete from  " + tableName;
        db.setupdateData(sql);
        return true;
    }
    
    public boolean restoreDBFromFile(String databaseName, String fpath, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        boolean isZS = false;
        boolean isNull = false;
        boolean isDELIMITER = false;
        List<String> insertSQLList = new ArrayList<>();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fpath), "UTF-8"));
            String tempString = null;
            while ((tempString = reader.readLine()) != null)  {
                tempString = tempString.trim();
                if (tempString.equals("")) {
                    isNull = true;
                } else {
                    isNull = false;
                    if (tempString.equals("DELIMITER ;;"))
                    {
                        isDELIMITER = true;
                    }
                    else if (tempString.equals("DELIMITER ;"))
                    {
                        isDELIMITER = false;
                        try
                        {
                            db.setupdateData(sb.toString());
                        }
                        catch (Exception e)
                        {
                            logger.error(e.getMessage(), e);
                        }
                        sb.setLength(0);
                    }
                    else if (isDELIMITER)
                    {
                        sb = sb.append(tempString + " ");
                    }
                    else if (tempString.indexOf("/*") == 0)
                    {
                        isZS = true;
                    }
                    else if (tempString.indexOf("*/") >= 0)
                    {
                        isZS = false;
                    }
                    else if (!isZS)
                    {
                        if (tempString.indexOf("--") != 0)
                        {
                            if (tempString.indexOf("#") != 0)
                            {
                                if ((tempString.indexOf("INSERT INTO") != 0) && (tempString.lastIndexOf(";") != tempString.length() - 1))
                                {
                                    sb = sb.append(tempString);
                                }
                                if ((tempString.indexOf("INSERT INTO") != 0) && (tempString.lastIndexOf(";") == tempString.length() - 1))
                                {
                                    sb = sb.append(tempString);
                                    try
                                    {
                                        db.setupdateData(sb.toString());
                                    }
                                    catch (Exception e)
                                    {
                                        logger.error(e.getMessage(), e);
                                    }
                                    sb.setLength(0);
                                }
                                if ((tempString.indexOf("INSERT INTO") == 0) && (tempString.lastIndexOf(";") == tempString.length() - 1))
                                {
                                    insertSQLList.add(tempString);
                                    if (insertSQLList.size() >= 1000)
                                    {
                                        db.updateExecuteBatch(insertSQLList);
                                        insertSQLList.clear();
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (insertSQLList.size() > 0)  {
                db.updateExecuteBatch(insertSQLList);
                insertSQLList.clear();
            }
            reader.close();
        } catch (IOException e) {
            if (reader != null)  {
                try
                {
                    reader.close();
                }
                catch (IOException localIOException1) {
                }
            }
        }
        finally {
            if (reader != null)  {
                try
                {
                    reader.close();
                }
                catch (IOException localIOException2) {
                }
            }
        }
        return true;
    }
    
    public List<Map<String, Object>> viewTableMessForMySql(String dbName, String tableName, String databaseConfigId)
        throws Exception {
        String sql = " select  * from information_schema.tables where table_name='" + tableName + "' and table_schema='" + dbName + "'  ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        return db.queryForList(sql);
    }
    
    public List<Map<String, Object>> viewTableMessForMSSQL(String dbName, String tableName, String databaseConfigId)
        throws Exception {
        String sql = "  select top 1 t1.name as table_name, t1.crdate,t1.refdate, t2.*  from sysobjects t1 left join  sysindexes t2  on t1.id=t2.id  where  t1.name ='" + tableName + "'  ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        return db.queryForList(sql);
    }
    
    public List<Map<String, Object>> viewTableMessForPostgreSQL(String dbName, String tableName, String databaseConfigId)
        throws Exception {
        String sql = " select * from pg_tables  where tablename='" + tableName + "'  ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        return db.queryForList(sql);
    }
    
    public List<Map<String, Object>> viewTableMessForOracle(String dbName, String tableName, String databaseConfigId)
        throws Exception {
        String sql = "  select *  from user_tables where table_name ='" + tableName + "' ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        return db.queryForList(sql);
    }
    
    public List<Map<String, Object>> viewTableMessForHive2(String dbName, String tableName, String databaseConfigId)
        throws Exception {
        String sql = "desc formatted " + tableName + " ";
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        return db.queryForList(sql);
    }
    
    public String getTableRows(String dbName, String tableName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(dbName, databaseConfigId);
        String sql = " select count(*) as num from  " + tableName;
        List<Map<String, Object>> list = db.queryForList(sql);
        Map<String, Object> map = list.get(0);
        return String.valueOf(map.get("num"));
    }
    
    public int saveNewTable(JSONArray insertArray, String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql = " create table " + tableName + " (  ";
        String PRIMARY_KEY = "";
        for (int i = 0; i < insertArray.size(); i++) {
            Map<String, Object> map1 = (Map<String, Object>)insertArray.get(i);
            sql = sql + map1.get("COLUMN_NAME") + "  ";
            sql = sql + map1.get("DATA_TYPE") + " ";
            if (!map1.get("CHARACTER_MAXIMUM_LENGTH").equals(""))  {
                sql = sql + "(" + map1.get("CHARACTER_MAXIMUM_LENGTH") + ") ";
            }
            if (map1.get("IS_NULLABLE").equals(""))  {
                sql = sql + " NOT NULL  ";
            }
            if (!map1.get("COLUMN_COMMENT").equals(""))  {
                sql = sql + " COMMENT '" + map1.get("COLUMN_COMMENT") + "' ";
            }
            if (map1.get("COLUMN_KEY").equals("PRI"))  {
                PRIMARY_KEY = PRIMARY_KEY + map1.get("COLUMN_NAME") + ",";
            }
            if (i < insertArray.size() - 1)  {
                sql = sql + ",";
            }
        }
        if (!PRIMARY_KEY.equals("")) {
            sql = sql + ", PRIMARY KEY (" + PRIMARY_KEY.substring(0, PRIMARY_KEY.length() - 1) + ") ) ";
        }
        else {
            sql = sql + " ) ";
        }
        return db.setupdateData(sql);
    }
    
    public int saveNewTableForPostgreSQL(JSONArray insertArray, String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql = " create table \"" + tableName + "\" (  ";
        String PRIMARY_KEY = "";
        for (int i = 0; i < insertArray.size(); i++) {
            Map<String, Object> map1 = (Map<String, Object>)insertArray.get(i);
            sql = sql + "\"" + map1.get("COLUMN_NAME") + "\"  ";
            sql = sql + map1.get("DATA_TYPE") + " ";
            if (!map1.get("CHARACTER_MAXIMUM_LENGTH").equals(""))  {
                sql = sql + "(" + map1.get("CHARACTER_MAXIMUM_LENGTH") + ") ";
            }
            if (map1.get("IS_NULLABLE").equals(""))  {
                sql = sql + " NOT NULL  ";
            }
            if (!map1.get("COLUMN_COMMENT").equals(""))  {
                sql = sql + " COMMENT '" + map1.get("COLUMN_COMMENT") + "' ";
            }
            if (map1.get("COLUMN_KEY").equals("PRI"))  {
                PRIMARY_KEY = PRIMARY_KEY + map1.get("COLUMN_NAME") + ",";
            }
            if (i < insertArray.size() - 1)  {
                sql = sql + ",";
            }
        }
        if (!PRIMARY_KEY.equals("")) {
            sql = sql + ", PRIMARY KEY (" + PRIMARY_KEY.substring(0, PRIMARY_KEY.length() - 1) + ") ) ";
        }
        else {
            sql = sql + " ) ";
        }
        int y = db.setupdateData(sql);
        return y;
    }
    
    public int saveNewTableForMSSQL(JSONArray insertArray, String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql = " create table " + tableName + " (  ";
        String PRIMARY_KEY = "";
        for (int i = 0; i < insertArray.size(); i++) {
            Map<String, Object> map1 = (Map<String, Object>)insertArray.get(i);
            sql = sql + map1.get("COLUMN_NAME") + " ";
            sql = sql + map1.get("DATA_TYPE") + " ";
            if (!map1.get("CHARACTER_MAXIMUM_LENGTH").equals(""))  {
                sql = sql + "(" + map1.get("CHARACTER_MAXIMUM_LENGTH") + ") ";
            }
            if (map1.get("IS_NULLABLE").equals(""))  {
                sql = sql + " NOT NULL  ";
            }
            if (map1.get("COLUMN_KEY").equals("PRI"))  {
                PRIMARY_KEY = PRIMARY_KEY + map1.get("COLUMN_NAME") + ",";
            }
            if (i < insertArray.size() - 1)  {
                sql = sql + ",";
            }
        }
        if (!PRIMARY_KEY.equals("")) {
            sql = sql + ", PRIMARY KEY (" + PRIMARY_KEY.substring(0, PRIMARY_KEY.length() - 1) + ") ) ";
        }
        else {
            sql = sql + " ) ";
        }
        return db.setupdateData(sql);
    }
    
    public int saveNewTableForOracle(JSONArray insertArray, String databaseName, String tableName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql = " create table " + tableName + " (  ";
        String PRIMARY_KEY = "";
        List<String> commentStr = new ArrayList<>();
        for (int i = 0; i < insertArray.size(); i++) {
            Map<String, Object> map1 = (Map<String, Object>)insertArray.get(i);
            sql = sql + map1.get("COLUMN_NAME") + " ";
            sql = sql + map1.get("DATA_TYPE") + " ";
            if (!map1.get("CHARACTER_MAXIMUM_LENGTH").equals(""))  {
                sql = sql + "(" + map1.get("CHARACTER_MAXIMUM_LENGTH") + ") ";
            }
            if (map1.get("IS_NULLABLE").equals(""))  {
                sql = sql + " NOT NULL  ";
            }
            if (!map1.get("COLUMN_COMMENT").equals(""))  {
                commentStr.add(" COMMENT ON COLUMN  " + tableName + "." + map1.get("COLUMN_NAME") + " IS  '" + map1.get("COLUMN_COMMENT") + "'");
            }
            if (map1.get("COLUMN_KEY").equals("PRI"))  {
                PRIMARY_KEY = PRIMARY_KEY + map1.get("COLUMN_NAME") + ",";
            }
            if (i < insertArray.size() - 1)  {
                sql = sql + ",";
            }
        }
        if (!PRIMARY_KEY.equals("")) {
            sql = sql + ", PRIMARY KEY (" + PRIMARY_KEY.substring(0, PRIMARY_KEY.length() - 1) + ") ) ";
        }
        else {
            sql = sql + " ) ";
        }
        int y = 0;
        y = db.setupdateData(sql);
        for (int i = 0; i < commentStr.size(); i++) {
            db.setupdateData(commentStr.get(i));
        }
        return y;
    }
    
    public Map<String, Object> queryDatabaseStatus(String databaseName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql = " show global status  ";
        List<Map<String, Object>> list = db.queryForList(sql);
        Map<String, Object> map = new HashMap<>();
        String variableName = "";
        String value = "";
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> temp = list.get(i);
            variableName = (String)temp.get("Variable_name");
            value = (String)temp.get("Value");
            map.put(variableName, value);
        }
        return map;
    }
    
    public Map<String, Object> queryDatabaseStatusForPostgreSQL(String databaseName, String databaseConfigId)
        throws Exception {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mm1 = queryDatabaseSQPSForPostgreSQL(databaseName, databaseConfigId);
        map.put("SESSIONS", queryDatabaseStatusForPostgreSQLConn(databaseName, databaseConfigId));
        map.put("dbSize", queryDatabaseStatusForPostgreSQLDBSize(databaseName, databaseConfigId));
        map.put("LOCK", queryDatabaseStatusForPostgreSQLLocks(databaseName, databaseConfigId));
        map.put("version", queryDatabaseVersionForPostgreSQL(databaseName, databaseConfigId));
        map.put("tableSpaceSize", queryDatabaseTableSpaceForPostgreSQL(databaseName, databaseConfigId));
        map.putAll(mm1);
        return map;
    }
    
    public String queryDatabaseTableSpaceForPostgreSQL(String databaseName, String databaseConfigId) {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String value = "0";
        try {
            String sql = " select  pg_size_pretty(pg_tablespace_size(t1.spcname)) as tableSpaceSize from pg_tablespace t1 left join pg_database t2 on t1.oid = t2.dattablespace where t2.datname='"
                + databaseName + "' ";
            List<Map<String, Object>> list = db.queryForList(sql);
            if (!list.isEmpty())  {
                Map<String, Object> temp = list.get(0);
                value = (String)temp.get("tablespacesize");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return value;
    }
    
    public String queryDatabaseVersionForPostgreSQL(String databaseName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql = " select version() ";
        List<Map<String, Object>> list = db.queryForList(sql);
        String value = "0";
        if (!list.isEmpty()) {
            Map<String, Object> temp = list.get(0);
            value = (String)temp.get("version");
            if (value.split(",").length > 0)  {
                value = value.split(",")[0];
            }
        }
        return value;
    }
    
    public String queryDatabaseStatusForPostgreSQLConn(String databaseName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql = "select count(*) as connections from pg_stat_activity";
        List<Map<String, Object>> list = db.queryForList(sql);
        String value = "0";
        if (!list.isEmpty()) {
            Map<String, Object> temp = list.get(0);
            value = (String)temp.get("connections");
        }
        return value;
    }
    
    public String queryDatabaseStatusForPostgreSQLLocks(String databaseName, String databaseConfigId)
        throws Exception {
        String value;
        try {
            BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
            String sql = "select count(*) as locks from pg_stat_activity where waiting='t' ";
            List<Map<String, Object>> list = db.queryForList(sql);
            value = "0";
            if (!list.isEmpty())  {
                Map<String, Object> temp = list.get(0);
                value = (String)temp.get("locks");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            value = "0";
        }
        return value;
    }
    
    public String queryDatabaseStatusForPostgreSQLDBSize(String databaseName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql = " select pg_size_pretty(pg_database_size('" + databaseName + "'))";
        List<Map<String, Object>> list = db.queryForList(sql);
        String value = "0";
        if (!list.isEmpty()) {
            Map<String, Object> temp = list.get(0);
            value = (String)temp.get("pg_size_pretty");
        }
        return value;
    }
    
    public Map<String, Object> queryDatabaseSQPSForPostgreSQL(String databaseName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        Map<String, Object> map = new HashMap<>();
        String sql = "  select sum(seq_tup_read) as select,sum(n_tup_ins) as insert,sum(n_tup_upd) as update,sum(n_tup_del) as delete from pg_stat_user_tables ";
        List<Map<String, Object>> list = db.queryForList(sql);
        if (!list.isEmpty()) {
            map = list.get(0);
        }
        return map;
    }
    
    public Map<String, Object> queryDatabaseStatusForOracle(String databaseName, String databaseConfigId)
        throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("SESSIONS", queryDatabaseStatusForOracleSession(databaseName, databaseConfigId));
        map.put("HIT_RATIO", queryDatabaseStatusForOracleHitRatio(databaseName, databaseConfigId));
        map.put("HIT_RADIO", queryDatabaseStatusForOracleHitRadio(databaseName, databaseConfigId));
        map.put("LOG_BUFFER", queryDatabaseStatusForOracleLogBuffer(databaseName, databaseConfigId));
        map.put("LOCK", queryDatabaseStatusForOracleLock(databaseName, databaseConfigId));
        map.put("PHYRDS", queryDatabaseStatusForOraclePhyrds(databaseName, databaseConfigId));
        map.put("PHYWRTS", queryDatabaseStatusForOraclePhywrts(databaseName, databaseConfigId));
        return map;
    }
    
    public String queryDatabaseStatusForOracleSession(String databaseName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql = "select count(*) as SESSIONS from  v$session";
        List<Map<String, Object>> list = db.queryForList(sql);
        String value = "0";
        if (!list.isEmpty()) {
            Map<String, Object> temp = list.get(0);
            value = (String)temp.get("SESSIONS");
        }
        return value;
    }
    
    public String queryDatabaseStatusForOracleHitRatio(String databaseName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql =
            "select floor(( 1 - sum(decode(name, 'physical reads', value, 0)) /(sum(decode(name, 'db block gets', value, 0)) + sum(decode(name, 'consistent gets', value, 0)))) *100)  HIT_RATIO from v$sysstat t where name in ('physical reads', 'db block gets', 'consistent gets')";
        List<Map<String, Object>> list = db.queryForList(sql);
        String value = "0";
        if (!list.isEmpty()) {
            Map<String, Object> temp = list.get(0);
            value = (String)temp.get("HIT_RATIO");
        }
        return value;
    }
    
    public String queryDatabaseStatusForOracleHitRadio(String databaseName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql = "select  floor(sum(pinhits)/sum(pins)*100 ) AS HIT_RADIO from v$librarycache";
        List<Map<String, Object>> list = db.queryForList(sql);
        String value = "0";
        if (!list.isEmpty()) {
            Map<String, Object> temp = list.get(0);
            value = (String)temp.get("HIT_RADIO");
        }
        return value;
    }
    
    public String queryDatabaseStatusForOracleLogBuffer(String databaseName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql =
            "select floor((select value  from v$sysstat where name in( 'redo buffer allocation retries'))/ (select value  from v$sysstat where name in('redo entries' ) )) as LOG_BUFFER from dual";
        List<Map<String, Object>> list = db.queryForList(sql);
        String value = "0";
        if (!list.isEmpty()) {
            Map<String, Object> temp = list.get(0);
            value = (String)temp.get("LOG_BUFFER");
        }
        return value;
    }
    
    public String queryDatabaseStatusForOracleLock(String databaseName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql = "select count(*) as LOCKS from v$locked_object ";
        List<Map<String, Object>> list = db.queryForList(sql);
        String value = "0";
        if (!list.isEmpty()) {
            Map<String, Object> temp = list.get(0);
            value = (String)temp.get("LOCKS");
        }
        return value;
    }
    
    public String queryDatabaseStatusForOraclePhyrds(String databaseName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql = "select sum(f.phyrds) PHYRDS from v$filestat f, dba_data_files df where f.file# = df.file_id ";
        List<Map<String, Object>> list = db.queryForList(sql);
        String value = "0";
        if (!list.isEmpty()) {
            Map<String, Object> temp = list.get(0);
            value = (String)temp.get("PHYRDS");
        }
        return value;
    }
    
    public String queryDatabaseStatusForOraclePhywrts(String databaseName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql = "select sum(f.phywrts) PHYWRTS from v$filestat f, dba_data_files df where f.file# = df.file_id ";
        List<Map<String, Object>> list = db.queryForList(sql);
        String value = "0";
        if (!list.isEmpty()) {
            Map<String, Object> temp = list.get(0);
            value = (String)temp.get("PHYWRTS");
        }
        return value;
    }
    
    public List<Map<String, Object>> queryTableSpaceForOracle(String databaseName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql =
            " SELECT a.tablespace_name TABLESPACE_NAME,ROUND( free / (1024 * 1024 ),2) TABLESPACE_SIZE_FREE,ROUND( total / (1024 * 1024 ),2) TABLESPACE_SIZE, ROUND( (total - free) / (1024 * 1024  ),2) TABLESPACE_SIZE_USED FROM (SELECT tablespace_name, SUM(bytes) free FROM dba_free_space GROUP BY tablespace_name) a, (SELECT tablespace_name, SUM(bytes) total FROM dba_data_files GROUP BY tablespace_name) b WHERE a.tablespace_name = b.tablespace_name  ";
        return db.queryForList(sql);
    }
    
    public List<Map<String, Object>> monitorItemValue(String databaseName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql = " show global status  ";
        List<Map<String, Object>> list2 = new ArrayList<>();
        List<Map<String, Object>> list = db.queryForList(sql);
        String variableName = "";
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> tempMap = list.get(i);
            variableName = (String)tempMap.get("Variable_name");
            if (variableName.equals("Com_select"))  {
                tempMap.put("descript", "select数量");
            } else if (variableName.equals("Com_update"))  {
                tempMap.put("descript", "更新数量");
            } else if (variableName.equals("Open_tables"))  {
                tempMap.put("descript", "当前打开的表的数量");
            } else if (variableName.equals("Open_files"))  {
                tempMap.put("descript", "打开的文件的数目");
            } else if (variableName.equals("Opened_tables"))  {
                tempMap.put("descript", "已经打开的表的数量");
            } else if (variableName.equals("Questions"))  {
                tempMap.put("descript", "已经发送给服务器的查询的个数");
            } else {
                tempMap.put("descript", "");
            }
            list2.add(tempMap);
        }
        return list2;
    }
    
    public List<Map<String, Object>> monitorItemValueForOracle(String databaseName, String databaseConfigId)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String sql = "  select NAME \"Variable_name\", VALUE \"Value\"  from v$sysstat ";
        List<Map<String, Object>> list2 = new ArrayList<>();
        List<Map<String, Object>> list = db.queryForList(sql);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> tempMap = list.get(i);
            list2.add(tempMap);
        }
        return list2;
    }
    
    public Page<Map<String, Object>> configList(Page<Map<String, Object>> page) {
        String sql = "select * from  treesoft_config";
        int rowCount = jdbcTemplate.queryForList(sql).size();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql + "  limit ?, ?", (page.getPageNo() - 1) * page.getPageSize(), page.getPageSize());
        page.setTotalCount(rowCount);
        page.setResult(list);
        return page;
    }
    
    public List<Map<String, Object>> getAllConfigList() {
        return jdbcTemplate.queryForList("select * from  treesoft_config");
    }
    
    public boolean deleteConfig(String[] ids) {
        String sql = "delete from treesoft_config where id in (:ids)";
        return namedJdbcTemplate.update(sql, Collections.singletonMap("ids", Arrays.asList(ids))) > 0;
    }
    
    public List<Map<String, Object>> selectAllDataFromSQLForOracle(String databaseName, String databaseConfigId, String sql)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        return db.queryForList(sql);
    }
    
    public List<Map<String, Object>> selectAllDataFromSQLForPostgreSQL(String databaseName, String databaseConfigId, String sql)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        return db.queryForList(sql);
    }
    
    public List<Map<String, Object>> selectAllDataFromSQLForMSSQL(String databaseName, String databaseConfigId, String sql)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        return db.queryForList(sql);
    }
    
    public List<Map<String, Object>> selectAllDataFromSQLForHive2(String databaseName, String databaseConfigId, String sql)
        throws Exception {
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        return db.queryForList(sql);
    }

    public boolean isDate(String date) {
        Matcher mat = datePattern.matcher(date);
        return mat.matches();
    }

    public String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if ((src == null) || (src.length <= 0)) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2)  {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

}