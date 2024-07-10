package org.springframework.base.system.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.base.common.web.BaseController;
import org.springframework.base.system.entity.Config;
import org.springframework.base.system.entity.IdsDto;
import org.springframework.base.system.entity.TempDto;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.service.PermissionService;
import org.springframework.base.system.utils.ComputerMonitorUtil;
import org.springframework.base.system.utils.FileUtil;
import org.springframework.base.system.utils.NetworkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
@RequestMapping({"system/permission"})
public class PermissionController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PermissionController.class);

    @Autowired
    private PermissionService permissionService;

    @Autowired
    HttpServletRequest request;

    @ResponseBody
    @RequestMapping(value = "i/allDatabaseList", produces = "application/json;charset=UTF-8")
    public List<Map<String, Object>> allDatabaseList() {
        List<Map<String, Object>> listDb = new ArrayList<>();
        HttpSession session = request.getSession(true);
        String username = (String) session.getAttribute("LOGIN_USER_NAME");
        List<Map<String, Object>> userList = permissionService.selectUserByName(username);
        String datascope = (String) userList.get(0).get("datascope");
        try {
            listDb = permissionService.getAllDataBaseById(datascope);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return listDb;
    }

    @ResponseBody
    @RequestMapping(value = {"i/allTableAndColumn/{databaseName}/{databaseConfigId}"}, method = {RequestMethod.GET})
    public List<Map<String, Object>> allTableAndColumn(@PathVariable String databaseName, @PathVariable String databaseConfigId)
            throws Exception {
        String column_name;
        String dbName = databaseName;
        List<Map<String, Object>> resultListObject = new ArrayList<>();
        List<Map<String, Object>> listTable = new ArrayList<>();
        Map<String, Object> tempObject;
        List<Map<String, Object>> listTableColumn = new ArrayList<>();
        Map<String, Object> tempMap;
        List<Map<String, Object>> listView = new ArrayList<>();
        Map<String, Object> map = permissionService.getConfig(databaseConfigId);
        String databaseType = (String) map.get("databaseType");
        try {
            if (databaseType.equals("MySql")) {
                listTable = permissionService.getAllTables(dbName, databaseConfigId);
            }
            if (databaseType.equals("MariaDB")) {
                listTable = permissionService.getAllTables(dbName, databaseConfigId);
            }
            if (databaseType.equals("Oracle")) {
                listTable = permissionService.getAllTablesForOracle(dbName, databaseConfigId);
            }
            if (databaseType.equals("PostgreSQL")) {
                listTable = permissionService.getAllTablesForPostgreSQL(dbName, databaseConfigId);
            }
            if (databaseType.equals("MSSQL")) {
                listTable = permissionService.getAllTablesForMSSQL(dbName, databaseConfigId);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Collections.emptyList();
        }

        for (int y = 0; y < listTable.size(); y++) {
            tempObject = listTable.get(y);
            String table_name = (String) tempObject.get("TABLE_NAME");
            Map<String, Object> tempTableMap = new HashMap<>();
            if (databaseType.equals("MySql")) {
                listTableColumn = permissionService.getTableColumns3(databaseName, table_name, databaseConfigId);
            }
            if (databaseType.equals("MariaDB")) {
                listTableColumn = permissionService.getTableColumns3(databaseName, table_name, databaseConfigId);
            }
            if (databaseType.equals("Oracle")) {
                listTableColumn = permissionService.getTableColumns3ForOracle(databaseName, table_name, databaseConfigId);
            }
            if (databaseType.equals("PostgreSQL")) {
                listTableColumn = permissionService.getTableColumns3ForPostgreSQL(databaseName, table_name, databaseConfigId);
            }
            if (databaseType.equals("MSSQL")) {
                listTableColumn = permissionService.getTableColumns3ForMSSQL(databaseName, table_name, databaseConfigId);
            }
            List<Map<String, Object>> tempColumnsListObject = new ArrayList<>();
            for (int z = 0; z < listTableColumn.size(); z++) {
                tempMap = listTableColumn.get(z);
                column_name = (String) tempMap.get("COLUMN_NAME");
                Map<String, Object> tempColumnsMap = new HashMap<>();
                tempColumnsMap.put("text", column_name);
                tempColumnsMap.put("displayText", column_name);
                tempColumnsListObject.add(tempColumnsMap);
            }
            tempTableMap.put("text", table_name);
            tempTableMap.put("columns", tempColumnsListObject);
            resultListObject.add(tempTableMap);
        }
        if (databaseType.equals("MySql")) {
            listView = permissionService.getAllViews(dbName, databaseConfigId);
        }
        if (databaseType.equals("MariaDB")) {
            listView = permissionService.getAllViews(dbName, databaseConfigId);
        }
        if (databaseType.equals("Oracle")) {
            listView = permissionService.getAllViewsForOracle(dbName, databaseConfigId);
        }
        if (databaseType.equals("PostgreSQL")) {
            listView = permissionService.getAllViewsForPostgreSQL(dbName, databaseConfigId);
        }
        if (databaseType.equals("MSSQL")) {
            listView = permissionService.getAllViewsForMSSQL(dbName, databaseConfigId);
        }
        for (int y = 0; y < listView.size(); y++) {
            Map<String, Object> tempViewMap = new HashMap<>();
            tempObject = listView.get(y);
            String viewName = (String) tempObject.get("TABLE_NAME");
            tempViewMap.put("text", viewName);
            tempViewMap.put("columns", new ArrayList<>());
            resultListObject.add(tempViewMap);
        }
        return resultListObject;
    }

    @ResponseBody
    @RequestMapping(value = {"i/databaseList/{databaseConfigId}"}, method = {RequestMethod.GET})
    public List<Map<String, Object>> databaseList(@PathVariable String databaseConfigId)
            throws Exception {
        List<Map<String, Object>> listAll = new ArrayList<>();
        List<Map<String, Object>> listTable = new ArrayList<>();
        List<Map<String, Object>> listView = new ArrayList<>();
        List<Map<String, Object>> listFunction = new ArrayList<>();
        Map<String, Object> map = permissionService.getConfig(databaseConfigId);
        String databaseType = (String) map.get("databaseType");
        String databaseName = (String) map.get("databaseName");
        String userName = (String) map.get("userName");
        List<Map<String, Object>> listDb = new ArrayList<>();
        try {
            if (databaseType.equals("MySql")) {
                listDb = permissionService.getAllDataBase(databaseConfigId);
            }
            if (databaseType.equals("MariaDB")) {
                listDb = permissionService.getAllDataBase(databaseConfigId);
            }
            if (databaseType.equals("Oracle")) {
                listDb = permissionService.getAllDataBaseForOracle(databaseConfigId);
            }
            if (databaseType.equals("PostgreSQL")) {
                listDb = permissionService.getAllDataBaseForPostgreSQL(databaseConfigId);
            }
            if (databaseType.equals("MSSQL")) {
                if (userName.equals("sa")) {
                    listDb = permissionService.getAllDataBaseForMSSQL(databaseConfigId);
                } else {
                    Map<String, Object> map3 = new HashMap<>();
                    map3.put("SCHEMA_NAME", databaseName);
                    listDb.add(map3);
                }
            }
            if (databaseType.equals("Hive2")) {
                listDb = permissionService.getAllDataBaseForHive2(databaseConfigId);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Collections.emptyList();
        }
        int id = 0;
        int pid = 0;
        for (int i = 0; i < listDb.size(); i++) {
            Map<String, Object> tempObject = new HashMap<>();
            Map<String, Object> map3 = listDb.get(i);
            String dbName = (String) map3.get("SCHEMA_NAME");
            tempObject.put("id", Integer.valueOf(++id));
            tempObject.put("name", dbName);
            tempObject.put("type", "db");
            tempObject.put("icon", "icon-hamburg-database");
            if (!dbName.equals(databaseName)) {
                tempObject.put("state", "closed");
            }
            listAll.add(tempObject);
            pid = id;
            Map<String, Object> tempObject2 = new HashMap<>();
            tempObject2.put("id", Integer.valueOf(++id));
            tempObject2.put("pid", Integer.valueOf(pid));
            tempObject2.put("name", "表");
            tempObject2.put("icon", "icon-berlin-billing");
            tempObject2.put("type", "direct");
            listAll.add(tempObject2);
            Map<String, Object> tempObject3 = new HashMap<>();
            tempObject3.put("id", Integer.valueOf(++id));
            tempObject3.put("pid", Integer.valueOf(pid));
            tempObject3.put("name", "视图");
            tempObject3.put("icon", "icon-berlin-address");
            tempObject3.put("type", "direct");
            listAll.add(tempObject3);
            Map<String, Object> tempObject4 = new HashMap<>();
            tempObject4.put("id", Integer.valueOf(++id));
            tempObject4.put("pid", Integer.valueOf(pid));
            tempObject4.put("name", "函数");
            tempObject4.put("icon", "icon-berlin-address");
            tempObject4.put("type", "direct");
            listAll.add(tempObject4);
            try {
                if (databaseType.equals("MySql")) {
                    listTable = permissionService.getAllTables(dbName, databaseConfigId);
                }
                if (databaseType.equals("MariaDB")) {
                    listTable = permissionService.getAllTables(dbName, databaseConfigId);
                }
                if (databaseType.equals("Oracle")) {
                    listTable = permissionService.getAllTablesForOracle(dbName, databaseConfigId);
                }
                if (databaseType.equals("PostgreSQL")) {
                    listTable = permissionService.getAllTablesForPostgreSQL(dbName, databaseConfigId);
                }
                if (databaseType.equals("MSSQL")) {
                    listTable = permissionService.getAllTablesForMSSQL(dbName, databaseConfigId);
                }
                if (databaseType.equals("Hive2")) {
                    listTable = permissionService.getAllTablesForHive2(dbName, databaseConfigId);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return Collections.emptyList();
            }
            for (int y = 0; y < listTable.size(); y++) {
                tempObject = listTable.get(y);
                String table_name = (String) tempObject.get("TABLE_NAME");
                Map<String, Object> tempObjectTable = new HashMap<>();
                tempObjectTable.put("id", Integer.valueOf(++id));
                tempObjectTable.put("pid", Integer.valueOf(pid + 1));
                tempObjectTable.put("name", table_name);
                tempObjectTable.put("icon", "icon-berlin-calendar");
                tempObjectTable.put("type", "table");
                listAll.add(tempObjectTable);
            }
            if (databaseType.equals("MySql")) {
                listView = permissionService.getAllViews(dbName, databaseConfigId);
            }
            if (databaseType.equals("MariaDB")) {
                listView = permissionService.getAllViews(dbName, databaseConfigId);
            }
            if (databaseType.equals("Oracle")) {
                listView = permissionService.getAllViewsForOracle(dbName, databaseConfigId);
            }
            if (databaseType.equals("PostgreSQL")) {
                listView = permissionService.getAllViewsForPostgreSQL(dbName, databaseConfigId);
            }
            if (databaseType.equals("MSSQL")) {
                listView = permissionService.getAllViewsForMSSQL(dbName, databaseConfigId);
            }
            if (databaseType.equals("Hive2")) {
                listView = permissionService.getAllViewsForHive2(dbName, databaseConfigId);
            }
            for (int y = 0; y < listView.size(); y++) {
                tempObject = listView.get(y);
                Map<String, Object> tempObjectView = new HashMap<>();
                tempObjectView.put("id", Integer.valueOf(++id));
                tempObjectView.put("pid", Integer.valueOf(pid + 2));
                tempObjectView.put("name", tempObject.get("TABLE_NAME"));
                tempObjectView.put("icon", "icon-berlin-library");
                tempObjectView.put("type", "view");
                listAll.add(tempObjectView);
            }
            if (databaseType.equals("MySql")) {
                listFunction = permissionService.getAllFuntion(dbName, databaseConfigId);
            }
            if (databaseType.equals("MariaDB")) {
                listFunction = permissionService.getAllFuntion(dbName, databaseConfigId);
            }
            if (databaseType.equals("Oracle")) {
                listFunction = permissionService.getAllFuntionForOracle(dbName, databaseConfigId);
            }
            if (databaseType.equals("PostgreSQL")) {
                listFunction = permissionService.getAllFuntionForPostgreSQL(dbName, databaseConfigId);
            }
            if (databaseType.equals("MSSQL")) {
                listFunction = permissionService.getAllFuntionForMSSQL(dbName, databaseConfigId);
            }
            for (int y = 0; y < listFunction.size(); y++) {
                tempObject = listFunction.get(y);
                Map<String, Object> tempObjectFunction = new HashMap<>();
                tempObjectFunction.put("id", Integer.valueOf(++id));
                tempObjectFunction.put("pid", Integer.valueOf(pid + 3));
                tempObjectFunction.put("name", tempObject.get("ROUTINE_NAME"));
                tempObjectFunction.put("icon", "icon-berlin-settings");
                tempObjectFunction.put("type", "function");
                listAll.add(tempObjectFunction);
            }
            id += 100;
        }
        request.setAttribute("databaseName", databaseName);
        return listAll;
    }

    @RequestMapping(value = {"i/tableColumns/{tableName}/{dbName}/{databaseConfigId}"}, method = {RequestMethod.GET})
    @ResponseBody
    public List<Map<String, Object>> tableColumns(@PathVariable String tableName, @PathVariable String dbName, @PathVariable String databaseConfigId) {
        List<Map<String, Object>> listAllColumn = permissionService.getTableColumns(dbName, tableName, databaseConfigId);
        List<Map<String, Object>> list2 = new ArrayList<>();
        for (int i = 0; i < listAllColumn.size(); i++) {
            Map<String, Object> map1 = listAllColumn.get(i);
            Map<String, Object> map2 = new HashMap<>();
            map2.put("column_name", map1.get("column_name"));
            map2.put("column_key", map1.get("column_key"));
            list2.add(map2);
        }
        return list2;
    }

    @RequestMapping(value = {"i/table/{tableName}/{dbName}/{databaseConfigId}"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getData(@PathVariable String tableName, @PathVariable String dbName, @PathVariable String databaseConfigId)
            throws Exception {
        Page<Map<String, Object>> page = getPage(request);
        Map<String, Object> map = permissionService.getConfig(databaseConfigId);
        String databaseType = (String) map.get("databaseType");
        Map<String, Object> result = new HashMap<>();
        String mess;
        String status;
        try {
            if (databaseType.equals("MySql")) {
                page = permissionService.getData(page, tableName, dbName, databaseConfigId);
                result.put("operator", "edit");
            }
            if (databaseType.equals("MariaDB")) {
                page = permissionService.getData(page, tableName, dbName, databaseConfigId);
                result.put("operator", "edit");
            }
            if (databaseType.equals("Oracle")) {
                page = permissionService.getDataForOracle(page, tableName, dbName, databaseConfigId);
                result.put("operator", "edit");
            }
            if (databaseType.equals("PostgreSQL")) {
                page = permissionService.getDataForPostgreSQL(page, tableName, dbName, databaseConfigId);
                result.put("operator", "edit");
            }
            if (databaseType.equals("MSSQL")) {
                page = permissionService.getDataForMSSQL(page, tableName, dbName, databaseConfigId);
                result.put("operator", "edit");
            }
            if (databaseType.equals("Hive2")) {
                page = permissionService.getDataForHive2(page, tableName, dbName, databaseConfigId);
                result.put("operator", "read");
            }
            mess = "执行完成！";
            status = "success";
            result.put("rows", page.getResult());
            result.put("total", page.getTotalCount());
            result.put("columns", page.getColumns());
            result.put("primaryKey", page.getPrimaryKey());
            result.put("totalCount", page.getTotalCount());
            result.put("mess", mess);
            result.put("status", status);
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = e.getMessage();
            status = "fail";
            result.put("mess", mess);
            result.put("status", status);
        }
        return result;
    }

    @RequestMapping(value = {"i/executeSqlTest/{databaseConfigId}"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> executeSqlTest(@PathVariable String databaseConfigId, String sql, String databaseName) {
        HttpSession session = request.getSession(true);
        String username = (String) session.getAttribute("LOGIN_USER_NAME");
        String ip = NetworkUtil.getIpAddress(request);
        Map<String, Object> map = permissionService.getConfig(databaseConfigId);
        String databaseType = (String) map.get("databaseType");
        Map<String, Object> result = new HashMap<>();
        if (databaseType.equals("MySql")) {
            if (StringUtils.startsWithAny(sql.toLowerCase(), "select", "show")) {
                result = executeSqlHaveRes(sql, databaseName, databaseConfigId);
            } else {
                result = executeSqlNotRes(sql, databaseName, databaseConfigId);
            }
        }
        if (databaseType.equals("MariaDB")) {
            if (StringUtils.startsWithAny(sql.toLowerCase(), "select", "show")) {
                result = executeSqlHaveRes(sql, databaseName, databaseConfigId);
            } else {
                result = executeSqlNotRes(sql, databaseName, databaseConfigId);
            }
        }
        if (databaseType.equals("Oracle")) {
            if (StringUtils.startsWithAny(sql.toLowerCase(), "select", "show")) {
                result = executeSqlHaveResForOracle(sql, databaseName, databaseConfigId);
            } else {
                result = executeSqlNotRes(sql, databaseName, databaseConfigId);
            }
        }
        if (databaseType.equals("PostgreSQL")) {
            if (StringUtils.startsWithAny(sql.toLowerCase(), "select", "show")) {
                result = executeSqlHaveResForPostgreSQL(sql, databaseName, databaseConfigId);
            } else {
                result = executeSqlNotRes(sql, databaseName, databaseConfigId);
            }
        }
        if (databaseType.equals("MSSQL")) {
            if (StringUtils.startsWithAny(sql.toLowerCase(), "select", "show")) {
                result = executeSqlHaveResForMSSQL(sql, databaseName, databaseConfigId);
            } else {
                result = executeSqlNotRes(sql, databaseName, databaseConfigId);
            }
        }
        if (databaseType.equals("Hive2")) {
            if (StringUtils.startsWithAny(sql.toLowerCase(), "select", "show")) {
                result = executeSqlHaveResForHive2(sql, databaseName, databaseConfigId);
            } else {
                result = executeSqlNotRes(sql, databaseName, databaseConfigId);
            }
        }
        return result;
    }

    public Map<String, Object> executeSqlHaveRes(String sql, String dbName, String databaseConfigId) {
        Map<String, Object> map = new HashMap<>();
        Page<Map<String, Object>> page = getPage(request);
        String mess;
        String status;
        Date b1 = new Date();
        try {
            page = permissionService.executeSql(page, sql, dbName, databaseConfigId);
            mess = "执行成功！";
            status = "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = e.getMessage();
            status = "fail";
        }
        Date b2 = new Date();
        long y = b2.getTime() - b1.getTime();
        map.put("rows", page.getResult());
        map.put("total", Long.valueOf(page.getTotalCount()));
        map.put("columns", page.getColumns());
        map.put("primaryKey", page.getPrimaryKey());
        map.put("tableName", page.getTableName());
        map.put("totalCount", Long.valueOf(page.getTotalCount()));
        map.put("time", Long.valueOf(y));
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    public Map<String, Object> executeSqlNotRes(String sql, String dbName, String databaseConfigId) {
        String mess;
        String status;
        StopWatch clock = new StopWatch();
        clock.start();
        int i = 0;
        try {
            i = permissionService.executeSqlNotRes(sql, dbName, databaseConfigId);
            mess = "执行成功！";
            status = "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = e.getMessage();
            status = "fail";
        }
        clock.stop();
        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", Integer.valueOf(i));
        map.put("time", clock.getTotalTimeMillis());
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"i/saveSearchHistory"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> saveSearchHistory(@RequestBody TempDto tem) {
        HttpSession session = request.getSession(true);
        String username = (String) session.getAttribute("LOGIN_USER_NAME");
        List<Map<String, Object>> userList = permissionService.selectUserByName(username);
        String userId = userList.get(0).get("id").toString();
        String id = tem.getId();
        String sql3 = tem.getSql();
        String dbName = tem.getDbName();
        String name = tem.getName();
        sql3 = sql3.replaceAll("'", "''");
        boolean bool = true;
        String mess = "";
        String status = "";
        if (StringUtils.isEmpty(id)) {
            bool = permissionService.saveSearchHistory(name, sql3, dbName, userId);
        } else {
            bool = permissionService.updateSearchHistory(id, name, sql3, dbName);
        }
        if (bool) {
            mess = "修改成功";
            status = "success";
        } else {
            mess = "保存失败";
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"i/deleteSearchHistory"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> deleteSearchHistory(@RequestBody TempDto tem) {
        String id = tem.getId();
        Map<String, Object> map = new HashMap<>();
        String mess = "";
        String status = "";
        boolean bool = true;
        if (StringUtils.isNotBlank(id)) {
            bool = permissionService.deleteSearchHistory(id);
        }
        if (bool) {
            mess = "操作成功";
            status = "success";
        } else {
            mess = "操作失败";
            status = "fail";
        }
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"i/selectSearchHistory"}, method = {RequestMethod.GET})
    @ResponseBody
    public List<Map<String, Object>> selectSearchHistory() {
        HttpSession session = request.getSession(true);
        String username = (String) session.getAttribute("LOGIN_USER_NAME");
        List<Map<String, Object>> userList = permissionService.selectUserByName(username);
        String userId = userList.get(0).get("id").toString();
        List<Map<String, Object>> list = permissionService.selectSearchHistory(userId);
        List<Map<String, Object>> list2 = new ArrayList<>();
        for (Map<String, Object> map : list) {
            String tempName = (String) map.get("name");
            map.put("name", tempName);
            map.put("pid", "0");
            map.put("icon", "icon-hamburg-zoom");
            list2.add(map);
        }
        return list2;
    }

    @RequestMapping(value = {"i/config"}, method = {RequestMethod.GET})
    public String config(Model model) {
        return "system/configList";
    }

    @RequestMapping(value = {"i/configList/{random}"}, method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> configList() {
        Page<Map<String, Object>> page = getPage(request);
        try {
            page = permissionService.configList(page);
        } catch (Exception e) {
            return getEasyUIData(page);
        }
        return getEasyUIData(page);
    }

    @RequestMapping(value = {"i/addConfigForm"}, method = {RequestMethod.GET})
    public String addConfigForm(Model model) {
        return "system/configForm";
    }

    @RequestMapping(value = {"i/editConfigForm/{id}"}, method = {RequestMethod.GET})
    public String editConfigForm(@PathVariable String id, Model model) {
        Map<String, Object> map = permissionService.getConfig(id);
        map.remove("password");
        model.addAttribute("config", map);
        return "system/configForm";
    }

    @RequestMapping(value = {"i/configUpdate"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> configUpdate(@RequestBody Config config, Model model) {
        Map<String, Object> map = new HashMap<>();
        String databaseType = config.getDatabaseType();
        String ip = config.getIp();
        String port = config.getPort();
        String dbName = StringUtils.trimToEmpty(config.getDatabaseName()).toLowerCase();
        if (StringUtils.contains(dbName, "mysql")) {
            map.put("mess", "不允许访问的资源");
            map.put("status", "fail");
            return map;
        }
        String url = "";
        if (databaseType.equals("MySql") || databaseType.equals("MariaDB")) {
            url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName;
        }
        if (databaseType.equals("Oracle")) {
            url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + dbName;
        }
        if (databaseType.equals("PostgreSQL")) {
            url = "jdbc:postgresql://" + ip + ":" + port + "/" + dbName;
        }
        if (databaseType.equals("MSSQL")) {
            url = "jdbc:sqlserver://" + ip + ":" + port + ";database=" + dbName;
        }
        config.setUrl(url);
        String mess = "";
        String status = "";
        try {
            permissionService.configUpdate(config);
            mess = "修改成功";
            status = "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = "error:" + e.getMessage();
            status = "fail";
        }
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"i/changePass"}, method = {RequestMethod.GET})
    public String changePass(Model model) {
        return "system/changePass";
    }

    @RequestMapping(value = {"i/searchHistory"}, method = {RequestMethod.GET})
    public String searchHistory(Model model) {
        return "system/searchHistory";
    }

    @RequestMapping(value = {"i/deleteRows/{databaseConfigId}"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> deleteRows(@RequestBody IdsDto tem, @PathVariable String databaseConfigId)
            throws Exception {
        String databaseName = tem.getDatabaseName();
        String tableName = tem.getTableName();
        String primary_key = tem.getPrimary_key();
        String checkedItems = tem.getCheckedItems();
        HttpSession session = request.getSession(true);
        String username = (String) session.getAttribute("LOGIN_USER_NAME");
        String ip = NetworkUtil.getIpAddress(request);
        Map<String, Object> map = permissionService.getConfig(databaseConfigId);
        String databaseType = (String) map.get("databaseType");
        List<String> condition = new ArrayList<>();
        if (checkedItems != null) {
            JSONArray deleteArray = JSONArray.parseArray(checkedItems);
            for (int i = 0; i < deleteArray.size(); i++) {
                Map<String, Object> map1 = (Map) deleteArray.get(i);
                String whereStr = "";
                if (StringUtils.isEmpty(primary_key)) {
                    for (String key : map1.keySet()) {
                        if ((map1.get(key) != null) && (!key.equals("RN"))) {
                            whereStr = whereStr + " and " + key + " = '" + map1.get(key) + "' ";
                        }
                    }
                } else {
                    String[] primaryKeys = primary_key.split(",");
                    for (String primaryKey : primaryKeys) {
                        whereStr = whereStr + " and " + primaryKey + " = '" + map1.get(primaryKey) + "' ";
                    }
                }
                condition.add(whereStr);
            }
        }
        int i = 0;
        String mess = "";
        String status = "";
        try {
            if (databaseType.equals("MySql")) {
                permissionService.deleteRowsNew(databaseName, tableName, primary_key, condition, databaseConfigId);
            }
            if (databaseType.equals("MariaDB")) {
                permissionService.deleteRowsNew(databaseName, tableName, primary_key, condition, databaseConfigId);
            }
            if (databaseType.equals("Oracle")) {
                permissionService.deleteRowsNewForOracle(databaseName, tableName, primary_key, condition, databaseConfigId);
            }
            if (databaseType.equals("PostgreSQL")) {
                permissionService.deleteRowsNewForPostgreSQL(databaseName, tableName, primary_key, condition, databaseConfigId);
            }
            if (databaseType.equals("MSSQL")) {
                permissionService.deleteRowsNewForMSSQL(databaseName, tableName, primary_key, condition, databaseConfigId);
            }
            mess = "删除成功";
            status = "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = e.getMessage();
            status = "fail";
        }
        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", i);
        result.put("mess", mess);
        result.put("status", status);
        return result;
    }

    @RequestMapping(value = {"i/saveRows"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> saveRows() {
        String databaseName = request.getParameter("databaseName");
        String tableName = request.getParameter("tableName");
        String databaseConfigId = request.getParameter("databaseConfigId");
        Map<String, Object> mapResult = new HashMap<>();
        Map<String, Object> maps = new HashMap<>();
        Map<String, String[]> map = request.getParameterMap();
        Set<Map.Entry<String, String[]>> set = map.entrySet();
        Iterator<Map.Entry<String, String[]>> it = set.iterator();
        String column = "";
        String value = "";
        String mess = "";
        String status = "";
        while (it.hasNext()) {
            Map.Entry<String, String[]> entry = it.next();
            column = entry.getKey();
            for (String i : entry.getValue()) {
                value = i;
                value = value.replaceAll("'", "''");
            }
            maps.put(column, value);
        }
        maps.remove("databaseName");
        maps.remove("tableName");
        try {
            permissionService.saveRows(maps, databaseName, tableName, databaseConfigId);
            mess = "新增成功！";
            status = "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = e.getMessage();
            status = "fail";
        }
        mapResult.put("mess", mess);
        mapResult.put("status", status);
        return mapResult;
    }

    @RequestMapping(value = {"i/editRows/{tableName}/{databaseName}/{id}/{idValues}/{databaseConfigId}"}, method = {RequestMethod.GET})
    public String editRows(@PathVariable String tableName, @PathVariable String databaseName, @PathVariable String id, @PathVariable String idValues, @PathVariable String databaseConfigId) {
        List<Map<String, Object>> listAllColumn = permissionService.getOneRowById(databaseName, tableName, id, idValues, databaseConfigId);
        List<Map<String, Object>> newList = new ArrayList<>();
        for (int i = 0; i < listAllColumn.size(); i++) {
            Map<String, Object> map3 = listAllColumn.get(i);
            String dataType = (String) map3.get("data_type");
            if (dataType.equals("VARCHAR")) {
                String columnValue = (String) map3.get("column_value");
                map3.put("column_value", htmlEscape(columnValue));
            }
            newList.add(map3);
        }
        request.setAttribute("databaseName", databaseName);
        request.setAttribute("tableName", tableName);
        request.setAttribute("listAllColumn", newList);
        request.setAttribute("id", id);
        request.setAttribute("idValues", idValues);
        return "system/editRowOne";
    }

    @RequestMapping(value = {"i/updateRows"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> updateRows(String databaseName, String tableName, String id, String idValues, String databaseConfigId) {
        String mess = "";
        String status = "";
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> maps = new HashMap<>();
        Map<String, String[]> map = request.getParameterMap();
        Set<Map.Entry<String, String[]>> set = map.entrySet();
        Iterator<Map.Entry<String, String[]>> it = set.iterator();
        String column = "";
        String value = "";
        while (it.hasNext()) {
            Map.Entry<String, String[]> entry = it.next();
            column = entry.getKey();
            for (String i : entry.getValue()) {
                value = i;
            }
            value = value.replaceAll("'", "''");
            maps.put(column, value);
        }
        maps.remove("databaseName");
        maps.remove("tableName");
        maps.remove("id");
        maps.remove("idValues");
        try {
            permissionService.updateRows(maps, databaseName, tableName, id, idValues, databaseConfigId);
            mess = " 更新成功！";
            status = "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = e.getMessage();
            status = "fail";
        }
        result.put("mess", mess);
        result.put("status", status);
        return result;
    }

    @RequestMapping(value = {"i/exportExcel"}, method = {RequestMethod.POST})
    public void exportExcel(@RequestBody String sContent, HttpServletResponse response) {
        String ss = "<html><body><table border=\"1px\">" +
                "<tr><td>8888888888888</td></tr>" +
                "</table></body></html>";
        try {
            OutputStream outputStream = response.getOutputStream();
            response.setContentType("application/msexcel; charset=UTF-8");
            response.setHeader("Content-disposition", "attachment; filename=data.xls");
            byte[] dataByteArr = ss.getBytes(StandardCharsets.UTF_8);
            outputStream.write(dataByteArr);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @RequestMapping(value = {"i/download"}, method = {RequestMethod.GET})
    public void download(HttpServletResponse response) {
        try {
            String path = "E://a.xls";
            File file = new File(path);
            String filename = file.getName();
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            response.reset();
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", String.valueOf(file.length()));
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=gb2312");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @RequestMapping(value = {"i/getViewSql/{databaseConfigId}"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getViewSql(@RequestBody IdsDto tem, @PathVariable String databaseConfigId)
            throws Exception {
        Map<String, Object> map = permissionService.getConfig(databaseConfigId);
        String databaseType = (String) map.get("databaseType");
        Map<String, Object> mapResult = new HashMap<>();
        String tableName = tem.getTableName();
        String databaseName = tem.getDatabaseName();
        String viewSql = "";
        String mess = "";
        String status = "";
        try {
            if (databaseType.equals("MySql")) {
                viewSql = permissionService.getViewSql(databaseName, tableName, databaseConfigId);
            }
            if (databaseType.equals("MariaDB")) {
                viewSql = permissionService.getViewSql(databaseName, tableName, databaseConfigId);
            }
            if (databaseType.equals("Oracle")) {
                viewSql = permissionService.getViewSqlForOracle(databaseName, tableName, databaseConfigId);
            }
            if (databaseType.equals("PostgreSQL")) {
                viewSql = permissionService.getViewSqlForPostgreSQL(databaseName, tableName, databaseConfigId);
            }
            if (databaseType.equals("MSSQL")) {
                viewSql = permissionService.getViewSqlForMSSQL(databaseName, tableName, databaseConfigId);
            }
            viewSql = viewSql.replaceAll("`", "");
            viewSql = viewSql.replaceAll(databaseName + ".", "");
            mess = "查询成功！";
            status = "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = e.getMessage();
            status = "fail";
        }
        mapResult.put("mess", mess);
        mapResult.put("status", status);
        mapResult.put("viewSql", viewSql);
        return mapResult;
    }

    public static String htmlEscape(String strData) {
        if (strData == null) {
            return "";
        }
        strData = replaceString(strData, "&", "&amp;");
        strData = replaceString(strData, "<", "&lt;");
        strData = replaceString(strData, ">", "&gt;");
        strData = replaceString(strData, "'", "&apos;");
        strData = replaceString(strData, "\"", "&quot;");
        return strData;
    }

    public static String replaceString(String strData, String regex, String replacement) {
        if (strData == null) {
            return null;
        }
        int index = strData.indexOf(regex);
        String strNew = "";
        if (index >= 0) {
            while (index >= 0) {
                strNew = strNew + strData.substring(0, index) + replacement;
                strData = strData.substring(index + regex.length());
                index = strData.indexOf(regex);
            }
            strNew = strNew + strData;
            return strNew;
        }
        return strData;
    }

    @RequestMapping(value = {"i/contribute"}, method = {RequestMethod.GET})
    public String contribute() {
        return "system/contribute";
    }

    @RequestMapping(value = {"i/help"}, method = {RequestMethod.GET})
    public String help() {
        return "system/help";
    }

    @RequestMapping(value = {"i/testConn"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> testConn(@RequestBody Config config) {
        Map<String, Object> mapResult = new HashMap<>();
        String databaseType = config.getDatabaseType();
        String databaseName = config.getDatabaseName();
        String ip = config.getIp();
        String port = config.getPort();
        String user = config.getUserName();
        String pass = config.getPassword();
        String mess = "";
        String status = "";
        boolean bl = permissionService.testConn(databaseType, databaseName, ip, port, user, pass);
        if (bl) {
            mess = "连接成功！";
            status = "success";
        } else {
            status = "fail";
        }
        mapResult.put("mess", mess);
        mapResult.put("status", status);
        return mapResult;
    }

    @RequestMapping(value = {"i/showTableData/{tableName}/{databaseName}/{databaseConfigId}/{objectType}"}, method = {RequestMethod.GET})
    public String showTableData(@PathVariable String tableName, @PathVariable String databaseName, @PathVariable String databaseConfigId, @PathVariable String objectType) {
        request.setAttribute("databaseName", databaseName);
        request.setAttribute("tableName", tableName);
        request.setAttribute("databaseConfigId", databaseConfigId);
        request.setAttribute("objectType", objectType);
        return "system/showTableData";
    }

    @RequestMapping(value = {"i/showResult/{sqlIndex}"}, method = {RequestMethod.GET})
    public String showResult(@PathVariable String sqlIndex) {
        request.setAttribute("sqlIndex", sqlIndex);
        return "system/showResult";
    }

    @RequestMapping(value = {"i/selectSqlStudy"}, method = {RequestMethod.GET})
    @ResponseBody
    public List<Map<String, Object>> selectSqlStudy() {
        return permissionService.selectSqlStudy();
    }

    @RequestMapping(value = {"i/updateRow/{tableName}/{databaseName}"}, method = {RequestMethod.GET})
    public Map<String, Object> updateRow(@PathVariable String tableName, @PathVariable String databaseName) {
        Map<String, Object> mapResult = new HashMap<>();
        String mess = "";
        String status = "";
        mess = "update成功！";
        status = "success";
        mapResult.put("mess", mess);
        mapResult.put("status", status);
        return mapResult;
    }

    @RequestMapping(value = {"i/designTable/{tableName}/{databaseName}/{databaseConfigId}"}, method = {RequestMethod.GET})
    public String designTable(@PathVariable String tableName, @PathVariable String databaseName, @PathVariable String databaseConfigId) {
        request.setAttribute("databaseName", databaseName);
        request.setAttribute("tableName", tableName);
        request.setAttribute("databaseConfigId", databaseConfigId);
        return "system/designTable";
    }

    @RequestMapping(value = {"i/treeShow/{tableName}/{databaseName}/{databaseConfigId}"}, method = {RequestMethod.GET})
    public String treeShow(@PathVariable String tableName, @PathVariable String databaseName, @PathVariable String databaseConfigId) {
        request.setAttribute("databaseName", databaseName);
        request.setAttribute("tableName", tableName);
        request.setAttribute("databaseConfigId", databaseConfigId);
        return "system/treeShow";
    }

    @RequestMapping(value = {"i/addNewTable/{databaseName}/{databaseConfigId}"}, method = {RequestMethod.GET})
    public String addNewTable(@PathVariable String databaseName, @PathVariable String databaseConfigId) {
        request.setAttribute("databaseName", databaseName);
        request.setAttribute("databaseConfigId", databaseConfigId);
        return "system/addNewTable";
    }

    @RequestMapping(value = {"i/jsonFormat"}, method = {RequestMethod.GET})
    public String jsonFormat() {
        return "system/jsonFormat";
    }

    @RequestMapping(value = {"i/designTableData/{tableName}/{databaseName}/{databaseConfigId}"}, method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> designTableData(@PathVariable String tableName, @PathVariable String databaseName, @PathVariable String databaseConfigId)
            throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> map = permissionService.getConfig(databaseConfigId);
        String databaseType = (String) map.get("databaseType");
        List<Map<String, Object>> listAllColumn = new ArrayList<>();
        if (databaseType.equals("MySql")) {
            listAllColumn = permissionService.getTableColumns3(databaseName, tableName, databaseConfigId);
            result.put("status", "success");
        }
        if (databaseType.equals("MariaDB")) {
            listAllColumn = permissionService.getTableColumns3(databaseName, tableName, databaseConfigId);
            result.put("status", "success");
        }
        if (databaseType.equals("Oracle")) {
            listAllColumn = permissionService.getTableColumns3ForOracle(databaseName, tableName, databaseConfigId);
            result.put("status", "success");
        }
        if (databaseType.equals("PostgreSQL")) {
            listAllColumn = permissionService.getTableColumns3ForPostgreSQL(databaseName, tableName, databaseConfigId);
            result.put("status", "success");
        }
        if (databaseType.equals("MSSQL")) {
            listAllColumn = permissionService.getTableColumns3ForMSSQL(databaseName, tableName, databaseConfigId);
            result.put("status", "success");
        }
        result.put("rows", listAllColumn);
        result.put("total", Integer.valueOf(listAllColumn.size()));
        return result;
    }

    @RequestMapping(value = {"i/saveData/{databaseConfigId}"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> saveData(@PathVariable String databaseConfigId)
            throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> map = permissionService.getConfig(databaseConfigId);
        String databaseType = (String) map.get("databaseType");
        String databaseName = request.getParameter("databaseName");
        String tableName = request.getParameter("tableName");
        String inserted = request.getParameter("inserted");
        String updated = request.getParameter("updated");
        String primary_key = request.getParameter("primary_key");
        HttpSession session = request.getSession(true);
        String username = (String) session.getAttribute("LOGIN_USER_NAME");
        String ip = NetworkUtil.getIpAddress(request);
        String mess = "";
        String status = "";
        String columnType = "";
        try {
            if (inserted != null) {
                JSONArray insertArray = JSONArray.parseArray(inserted);
                for (int i = 0; i < insertArray.size(); i++) {
                    Map<String, Object> map1 = (Map) insertArray.get(i);
                    Map maps = new HashMap<>();
                    for (String key : map1.keySet()) {
                        maps.put(key, map1.get(key));
                    }
                    maps.remove("treeSoftPrimaryKey");
                    if (databaseType.equals("MySql")) {
                        permissionService.saveRows(maps, databaseName, tableName, databaseConfigId);
                    }
                    if (databaseType.equals("MariaDB")) {
                        permissionService.saveRows(maps, databaseName, tableName, databaseConfigId);
                    }
                    if (databaseType.equals("Oracle")) {
                        permissionService.saveRowsForOracle(maps, databaseName, tableName, databaseConfigId);
                    }
                    if (databaseType.equals("PostgreSQL")) {
                        permissionService.saveRowsForPostgreSQL(maps, databaseName, tableName, databaseConfigId);
                    }
                    if (databaseType.equals("MSSQL")) {
                        permissionService.saveRowsForMSSQL(maps, databaseName, tableName, databaseConfigId);
                    }
                }
            }
            List<String> condition = new ArrayList<>();
            if (updated != null) {
                JSONArray updateArray = JSONArray.parseArray(updated);
                for (int i = 0; i < updateArray.size(); i++) {
                    Map<String, Object> map1 = (Map) updateArray.get(i);
                    Map<String, Object> map2 = (Map) map1.get("oldData");
                    Map<String, Object> map3 = (Map) map1.get("changesData");
                    String setStr = " set ";
                    String whereStr = " where 1=1 ";
                    if (map2.size() <= 0) {
                        break;
                    }
                    if (map3.size() <= 0) {
                        break;
                    }
                    if ((primary_key == null) || (primary_key.equals(""))) {
                        for (String key : map3.keySet()) {
                            if (map3.get(key) == null) {
                                setStr = setStr + key + " = null , ";
                            } else {
                                if (databaseType.equals("MySql")) {
                                    if (map3.get(key).equals("")) {
                                        setStr = setStr + key + " = null ,";
                                    } else {
                                        setStr = setStr + key + " = '" + map3.get(key) + "',";
                                    }
                                }
                                if (databaseType.equals("MariaDB")) {
                                    setStr = setStr + key + " = '" + map3.get(key) + "',";
                                }
                                if (databaseType.equals("Oracle")) {
                                    columnType = permissionService.selectColumnTypeForOracle(databaseName, tableName, key, databaseConfigId);
                                    if (columnType.equals("DATE")) {
                                        setStr = setStr + key + " = to_date('" + map3.get(key) + "' ,'yyyy-mm-dd hh24:mi:ss'),";
                                    } else if (columnType.indexOf("TIMESTAMP") >= 0) {
                                        setStr = setStr + key + " = to_date('" + map3.get(key) + "' ,'yyyy-mm-dd hh24:mi:ss'),";
                                    } else {
                                        setStr = setStr + key + " = '" + map3.get(key) + "',";
                                    }
                                }
                                if (databaseType.equals("PostgreSQL")) {
                                    setStr = setStr + "\"" + key + "\" = '" + map3.get(key) + "',";
                                }
                                if (databaseType.equals("MSSQL")) {
                                    setStr = setStr + key + " = '" + map3.get(key) + "',";
                                }
                            }
                        }
                        for (String key : map2.keySet()) {
                            if ((map2.get(key) != null) && (!key.equals("RN"))) {
                                if (databaseType.equals("MySql")) {
                                    columnType = permissionService.selectColumnTypeForMySql(databaseName, tableName, key, databaseConfigId);
                                }
                                if (databaseType.equals("MariaDB")) {
                                    columnType = permissionService.selectColumnTypeForMySql(databaseName, tableName, key, databaseConfigId);
                                }
                                if (databaseType.equals("Oracle")) {
                                    columnType = permissionService.selectColumnTypeForOracle(databaseName, tableName, key, databaseConfigId);
                                }
                                if (databaseType.equals("PostgreSQL")) {
                                    columnType = permissionService.selectColumnTypeForPostgreSQL(databaseName, tableName, key, databaseConfigId);
                                }
                                if (databaseType.equals("MSSQL")) {
                                    columnType = permissionService.selectColumnTypeForMSSQL(databaseName, tableName, key, databaseConfigId);
                                }
                                if (columnType.equals("DATE")) {
                                    whereStr = whereStr + " and " + key + " = to_date(' " + map2.get(key) + "','yyyy-mm-dd hh24:mi:ss') ";
                                } else if (columnType.indexOf("TIMESTAMP") >= 0) {
                                    whereStr = whereStr + " and " + key + " = to_date(' " + map2.get(key) + "','yyyy-mm-dd hh24:mi:ss') ";
                                } else if (databaseType.equals("PostgreSQL")) {
                                    whereStr = whereStr + " and \"" + key + "\" = '" + map2.get(key) + "' ";
                                } else {
                                    whereStr = whereStr + " and " + key + " = '" + map2.get(key) + "' ";
                                }
                            }
                        }
                    } else {
                        String[] primaryKeys = primary_key.split(",");
                        for (String key : map3.keySet()) {
                            if (map3.get(key) == null) {
                                setStr = setStr + key + " = null , ";
                            } else {
                                if (databaseType.equals("MySql")) {
                                    if (map3.get(key).equals("")) {
                                        setStr = setStr + key + " = null ,";
                                    } else {
                                        setStr = setStr + key + " = '" + map3.get(key) + "',";
                                    }
                                }
                                if (databaseType.equals("MariaDB")) {
                                    setStr = setStr + key + " = '" + map3.get(key) + "',";
                                }
                                if (databaseType.equals("Oracle")) {
                                    columnType = permissionService.selectColumnTypeForOracle(databaseName, tableName, key, databaseConfigId);
                                    if (columnType.equals("DATE")) {
                                        setStr = setStr + key + " = to_date('" + map3.get(key) + "' ,'yyyy-mm-dd hh24:mi:ss'),";
                                    } else if (columnType.indexOf("TIMESTAMP") >= 0) {
                                        setStr = setStr + key + " = to_date('" + map3.get(key) + "' ,'yyyy-mm-dd hh24:mi:ss'),";
                                    } else {
                                        setStr = setStr + key + " = '" + map3.get(key) + "',";
                                    }
                                }
                                if (databaseType.equals("PostgreSQL")) {
                                    setStr = setStr + "\"" + key + "\" = '" + map3.get(key) + "',";
                                }
                                if (databaseType.equals("MSSQL")) {
                                    setStr = setStr + key + " = '" + map3.get(key) + "',";
                                }
                            }
                        }
                        if (databaseType.equals("PostgreSQL")) {
                            for (String primaryKey : primaryKeys) {
                                whereStr = whereStr + " and \"" + primaryKey + "\" = '" + map2.get(primaryKey) + "' ";
                            }
                        } else {
                            for (String primaryKey : primaryKeys) {
                                whereStr = whereStr + " and " + primaryKey + " = '" + map2.get(primaryKey) + "' ";
                            }
                        }
                    }
                    setStr = setStr.substring(0, setStr.length() - 1);
                    condition.add(setStr + whereStr);
                }
                if (databaseType.equals("MySql")) {
                    permissionService.updateRowsNew(databaseName, tableName, condition, databaseConfigId);
                }
                if (databaseType.equals("MariaDB")) {
                    permissionService.updateRowsNew(databaseName, tableName, condition, databaseConfigId);
                }
                if (databaseType.equals("Oracle")) {
                    permissionService.updateRowsNewForOracle(databaseName, tableName, condition, databaseConfigId);
                }
                if (databaseType.equals("PostgreSQL")) {
                    permissionService.updateRowsNewForPostgreSQL(databaseName, tableName, condition, databaseConfigId);
                }
                if (databaseType.equals("MSSQL")) {
                    permissionService.updateRowsNewForMSSQL(databaseName, tableName, condition, databaseConfigId);
                }
            }
            mess = "保存成功！";
            status = "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = "保存出错！" + e.getMessage();
            status = "fail";
        }
        result.put("mess", mess);
        result.put("status", status);
        return result;
    }

    @RequestMapping(value = {"i/designTableUpdate/{databaseConfigId}"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> designTableUpdate(@PathVariable String databaseConfigId)
            throws Exception {
        Map<String, Object> mapResult = new HashMap<>();
        String mess = "";
        String status = "";
        Map<String, Object> map = permissionService.getConfig(databaseConfigId);
        String databaseType = (String) map.get("databaseType");
        String databaseName = request.getParameter("databaseName");
        String tableName = request.getParameter("tableName");
        String inserted = request.getParameter("inserted");
        String updated = request.getParameter("updated");
        try {
            if (inserted != null) {
                JSONArray insertArray = JSONArray.parseArray(inserted);
                for (int i = 0; i < insertArray.size(); i++) {
                    Map<String, Object> map1 = (Map) insertArray.get(i);
                    Map maps = new HashMap<>();
                    for (String key : map1.keySet()) {
                        maps.put(key, map1.get(key));
                    }
                    maps.remove("TREESOFTPRIMARYKEY");
                    if (databaseType.equals("MySql")) {
                        permissionService.saveDesginColumn(maps, databaseName, tableName, databaseConfigId);
                    }
                    if (databaseType.equals("MariaDB")) {
                        permissionService.saveDesginColumn(maps, databaseName, tableName, databaseConfigId);
                    }
                    if (databaseType.equals("Oracle")) {
                        permissionService.saveDesginColumnForOracle(maps, databaseName, tableName, databaseConfigId);
                    }
                    if (databaseType.equals("PostgreSQL")) {
                        permissionService.saveDesginColumnForPostgreSQL(maps, databaseName, tableName, databaseConfigId);
                    }
                    if (databaseType.equals("MSSQL")) {
                        permissionService.saveDesginColumnForMSSQL(maps, databaseName, tableName, databaseConfigId);
                    }
                }
            }
            if (updated != null) {
                if (databaseType.equals("MySql")) {
                    permissionService.updateTableColumn(updated, databaseName, tableName, databaseConfigId);
                }
                if (databaseType.equals("MariaDB")) {
                    permissionService.updateTableColumn(updated, databaseName, tableName, databaseConfigId);
                }
                if (databaseType.equals("Oracle")) {
                    permissionService.updateTableColumnForOracle(updated, databaseName, tableName, databaseConfigId);
                }
                if (databaseType.equals("PostgreSQL")) {
                    permissionService.updateTableColumnForPostgreSQL(updated, databaseName, tableName, databaseConfigId);
                }
                if (databaseType.equals("MSSQL")) {
                    permissionService.updateTableColumnForMSSQL(updated, databaseName, tableName, databaseConfigId);
                }
            }
            mess = "保存成功！";
            status = "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = "保存出错！" + e.getMessage();
            status = "fail";
        }
        mapResult.put("mess", mess);
        mapResult.put("status", status);
        return mapResult;
    }

    @RequestMapping(value = {"i/deleteTableColumn/{databaseConfigId}"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> deleteTableColumn(@PathVariable String databaseConfigId, @RequestBody IdsDto tem)
            throws Exception {
        String databaseName = tem.getDatabaseName();
        String tableName = tem.getTableName();
        String[] ids = tem.getIds();
        Map<String, Object> map0 = permissionService.getConfig(databaseConfigId);
        String databaseType = (String) map0.get("databaseType");
        int i = 0;
        String mess = "";
        String status = "";
        try {
            if (databaseType.equals("MySql")) {
                permissionService.deleteTableColumn(databaseName, tableName, ids, databaseConfigId);
            }
            if (databaseType.equals("MariaDB")) {
                permissionService.deleteTableColumn(databaseName, tableName, ids, databaseConfigId);
            }
            if (databaseType.equals("Oracle")) {
                permissionService.deleteTableColumnForOracle(databaseName, tableName, ids, databaseConfigId);
            }
            if (databaseType.equals("PostgreSQL")) {
                permissionService.deleteTableColumnForPostgreSQL(databaseName, tableName, ids, databaseConfigId);
            }
            if (databaseType.equals("MSSQL")) {
                permissionService.deleteTableColumnForMSSQL(databaseName, tableName, ids, databaseConfigId);
            }
            mess = "删除成功";
            status = "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = e.getMessage();
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", Integer.valueOf(i));
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"i/designTableSetNull/{databaseConfigId}"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> designTableSetNull(@PathVariable String databaseConfigId, @RequestBody IdsDto tem)
            throws Exception {
        String mess = "";
        String status = "";
        Map<String, Object> map0 = permissionService.getConfig(databaseConfigId);
        String databaseType = (String) map0.get("databaseType");
        String databaseName = tem.getDatabaseName();
        String tableName = tem.getTableName();
        String column_name = tem.getColumn_name();
        String is_nullable = tem.getIs_nullable();
        try {
            if (databaseType.equals("MySql")) {
                permissionService.updateTableNullAble(databaseName, tableName, column_name, is_nullable, databaseConfigId);
            }
            if (databaseType.equals("MariaDB")) {
                permissionService.updateTableNullAble(databaseName, tableName, column_name, is_nullable, databaseConfigId);
            }
            if (databaseType.equals("Oracle")) {
                permissionService.updateTableNullAbleForOracle(databaseName, tableName, column_name, is_nullable, databaseConfigId);
            }
            if (databaseType.equals("PostgreSQL")) {
                permissionService.updateTableNullAbleForPostgreSQL(databaseName, tableName, column_name, is_nullable, databaseConfigId);
            }
            if (databaseType.equals("MSSQL")) {
                permissionService.updateTableNullAbleForMSSQL(databaseName, tableName, column_name, is_nullable, databaseConfigId);
            }
            mess = "保存成功";
            status = "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = e.getMessage();
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"i/designTableSetPimary/{databaseConfigId}"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> designTableSetPimary(@PathVariable String databaseConfigId, @RequestBody IdsDto tem)
            throws Exception {
        String mess = "";
        String status = "";
        Map<String, Object> map0 = permissionService.getConfig(databaseConfigId);
        String databaseType = (String) map0.get("databaseType");
        String databaseName = tem.getDatabaseName();
        String tableName = tem.getTableName();
        String column_name = tem.getColumn_name();
        String column_key = tem.getColumn_key();
        try {
            if (databaseType.equals("MySql")) {
                permissionService.savePrimaryKey(databaseName, tableName, column_name, column_key, databaseConfigId);
            }
            if (databaseType.equals("MariaDB")) {
                permissionService.savePrimaryKey(databaseName, tableName, column_name, column_key, databaseConfigId);
            }
            if (databaseType.equals("Oracle")) {
                permissionService.savePrimaryKeyForOracle(databaseName, tableName, column_name, column_key, databaseConfigId);
            }
            if (databaseType.equals("PostgreSQL")) {
                permissionService.savePrimaryKeyForPostgreSQL(databaseName, tableName, column_name, column_key, databaseConfigId);
            }
            if (databaseType.equals("MSSQL")) {
                permissionService.savePrimaryKeyForMSSQL(databaseName, tableName, column_name, column_key, databaseConfigId);
            }
            mess = "保存成功";
            status = "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = e.getMessage();
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"i/upDownColumn/{databaseConfigId}"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> upDownColumn(@PathVariable String databaseConfigId, @RequestBody IdsDto tem)
            throws Exception {
        String mess = "";
        String status = "";
        Map<String, Object> map0 = permissionService.getConfig(databaseConfigId);
        String databaseType = (String) map0.get("databaseType");
        String databaseName = tem.getDatabaseName();
        String tableName = tem.getTableName();
        String column_name = tem.getColumn_name();
        String column_name2 = tem.getColumn_name2();
        try {
            if (databaseType.equals("MySql")) {
                permissionService.upDownColumn(databaseName, tableName, column_name, column_name2, databaseConfigId);
                mess = "保存成功";
                status = "success";
            }
            if (databaseType.equals("MariaDB")) {
                permissionService.upDownColumn(databaseName, tableName, column_name, column_name2, databaseConfigId);
                mess = "保存成功";
                status = "success";
            }
            if (databaseType.equals("Oracle")) {
                mess = "Oracle不支持该操作！";
                status = "success";
            }
            if (databaseType.equals("PostgreSQL")) {
                mess = "PostgreSQL不支持该操作！";
                status = "success";
            }
            if (databaseType.equals("MSSQL")) {
                mess = "SQL Server不支持该操作！";
                status = "success";
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = e.getMessage();
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    public Map<String, Object> executeSqlHaveResForOracle(String sql, String dbName, String databaseConfigId) {
        Map<String, Object> map = new HashMap<>();
        Page<Map<String, Object>> page = getPage(request);
        String mess = "";
        String status = "";
        Date b1 = new Date();
        try {
            page = permissionService.executeSqlHaveResForOracle(page, sql, dbName, databaseConfigId);
            mess = "执行成功！";
            status = "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = e.getMessage();
            status = "fail";
        }
        Date b2 = new Date();
        long y = b2.getTime() - b1.getTime();
        map.put("rows", page.getResult());
        map.put("total", Long.valueOf(page.getTotalCount()));
        map.put("columns", page.getColumns());
        map.put("primaryKey", page.getPrimaryKey());
        map.put("totalCount", Long.valueOf(page.getTotalCount()));
        map.put("time", Long.valueOf(y));
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    public Map<String, Object> executeSqlHaveResForPostgreSQL(String sql, String dbName, String databaseConfigId) {
        Map<String, Object> map = new HashMap<>();
        Page<Map<String, Object>> page = getPage(request);
        String mess = "";
        String status = "";
        Date b1 = new Date();
        try {
            page = permissionService.executeSqlHaveResForPostgreSQL(page, sql, dbName, databaseConfigId);
            mess = "执行成功！";
            status = "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = e.getMessage();
            status = "fail";
        }
        Date b2 = new Date();
        long y = b2.getTime() - b1.getTime();
        map.put("rows", page.getResult());
        map.put("total", Long.valueOf(page.getTotalCount()));
        map.put("columns", page.getColumns());
        map.put("primaryKey", page.getPrimaryKey());
        map.put("totalCount", Long.valueOf(page.getTotalCount()));
        map.put("time", Long.valueOf(y));
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    public Map<String, Object> executeSqlHaveResForMSSQL(String sql, String dbName, String databaseConfigId) {
        Map<String, Object> map = new HashMap<>();
        Page<Map<String, Object>> page = getPage(request);
        String mess = "";
        String status = "";
        Date b1 = new Date();
        try {
            page = permissionService.executeSqlHaveResForMSSQL(page, sql, dbName, databaseConfigId);
            mess = "执行成功！";
            status = "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = e.getMessage();
            status = "fail";
        }
        Date b2 = new Date();
        long y = b2.getTime() - b1.getTime();
        map.put("rows", page.getResult());
        map.put("total", Long.valueOf(page.getTotalCount()));
        map.put("columns", page.getColumns());
        map.put("primaryKey", page.getPrimaryKey());
        map.put("totalCount", Long.valueOf(page.getTotalCount()));
        map.put("time", Long.valueOf(y));
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    public Map<String, Object> executeSqlHaveResForHive2(String sql, String dbName, String databaseConfigId) {
        Map<String, Object> map = new HashMap<>();
        Page<Map<String, Object>> page = getPage(request);
        String mess = "";
        String status = "";
        Date b1 = new Date();
        try {
            page = permissionService.executeSqlHaveResForHive2(page, sql, dbName, databaseConfigId);
            mess = "执行成功！";
            status = "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = e.getMessage();
            status = "fail";
        }
        Date b2 = new Date();
        long y = b2.getTime() - b1.getTime();
        map.put("rows", page.getResult());
        map.put("total", Long.valueOf(page.getTotalCount()));
        map.put("columns", page.getColumns());
        map.put("tableName", "");
        map.put("totalCount", Long.valueOf(page.getTotalCount()));
        map.put("time", Long.valueOf(y));
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"i/backupDatabase/{databaseName}/{databaseConfigId}"}, method = {RequestMethod.GET})
    public String backupDatabase(@PathVariable String databaseName, @PathVariable String databaseConfigId) {
        request.setAttribute("databaseName", databaseName);
        request.setAttribute("databaseConfigId", databaseConfigId);
        return "system/backupDatabase";
    }

    @RequestMapping(value = {"i/monitor/{databaseName}/{databaseConfigId}"}, method = {RequestMethod.GET})
    public String monitor(@PathVariable String databaseName, @PathVariable String databaseConfigId)
            throws Exception {
        Map<String, Object> map0 = permissionService.getConfig(databaseConfigId);
        String ip = (String) map0.get("ip");
        String port = (String) map0.get("port");
        String name = (String) map0.get("name");
        String databaseType = (String) map0.get("databaseType");
        request.setAttribute("ip", ip);
        request.setAttribute("port", port);
        request.setAttribute("name", name);
        request.setAttribute("databaseType", databaseType);
        request.setAttribute("databaseConfigId", databaseConfigId);
        if (databaseType.equals("Oracle")) {
            List<Map<String, Object>> list = permissionService.queryTableSpaceForOracle(databaseName, databaseConfigId);
            String tableSpaceName = "";
            String TABLESPACE_SIZE_FREE = "";
            String TABLESPACE_SIZE_USED = "";
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                tableSpaceName = tableSpaceName + "'" + map.get("TABLESPACE_NAME") + "',";
                TABLESPACE_SIZE_FREE = TABLESPACE_SIZE_FREE + map.get("TABLESPACE_SIZE_FREE") + ",";
                TABLESPACE_SIZE_USED = TABLESPACE_SIZE_USED + map.get("TABLESPACE_SIZE_USED") + ",";
            }
            tableSpaceName = tableSpaceName.substring(0, tableSpaceName.length() - 1);
            TABLESPACE_SIZE_FREE = TABLESPACE_SIZE_FREE.substring(0, TABLESPACE_SIZE_FREE.length() - 1);
            TABLESPACE_SIZE_USED = TABLESPACE_SIZE_USED.substring(0, TABLESPACE_SIZE_USED.length() - 1);
            request.setAttribute("tableSpaceName", tableSpaceName);
            request.setAttribute("tableSpaceSizeFree", TABLESPACE_SIZE_FREE);
            request.setAttribute("tableSpaceSizeUsed", TABLESPACE_SIZE_USED);
            return "system/monitorOracle";
        }
        if (databaseType.equals("PostgreSQL")) {
            return "system/monitorPostgreSQL";
        }
        if (databaseType.equals("MySql")) {
            return "system/monitorMySql";
        }
        if (databaseType.equals("MariaDB")) {
            return "system/monitorMySql";
        }
        return "system/monitor";
    }

    @RequestMapping(value = {"i/monitorItem/{databaseName}/{databaseConfigId}"}, method = {RequestMethod.GET})
    public String monitorItem(@PathVariable String databaseName, @PathVariable String databaseConfigId) {
        request.setAttribute("databaseName", databaseName);
        request.setAttribute("databaseConfigId", databaseConfigId);
        return "system/monitorItem";
    }

    @RequestMapping({"i/queryDatabaseStatus/{databaseName}/{databaseConfigId}"})
    @ResponseBody
    public Map<String, Object> queryDatabaseStatus(@PathVariable String databaseName, @PathVariable String databaseConfigId) {
        Map<String, Object> map = new HashMap<>();
        String mess = "";
        String status = "";
        try {
            Map<String, Object> map0 = permissionService.getConfig(databaseConfigId);
            String databaseType = (String) map0.get("databaseType");
            if (databaseType.equals("MySql")) {
                map = permissionService.queryDatabaseStatus(databaseName, databaseConfigId);
                mess = "查询成功";
                status = "success";
            }
            if (databaseType.equals("MariaDB")) {
                map = permissionService.queryDatabaseStatus(databaseName, databaseConfigId);
                mess = "查询成功";
                status = "success";
            }
            if (databaseType.equals("Oracle")) {
                map = permissionService.queryDatabaseStatusForOracle(databaseName, databaseConfigId);
                mess = "查询成功";
                status = "success";
            }
            if (databaseType.equals("PostgreSQL")) {
                map = permissionService.queryDatabaseStatusForPostgreSQL(databaseName, databaseConfigId);
                mess = "查询成功";
                status = "success";
            }
            if (databaseType.equals("MSSQL")) {
                mess = "暂不支持SQL Server状态监控!";
                status = "fail";
            }
            double memUsage = ComputerMonitorUtil.getMemUsage();
            double diskUsage = ComputerMonitorUtil.getDiskUsage();
            map.put("memUsage", Double.valueOf(memUsage));
            map.put("diskUsage", Double.valueOf(diskUsage));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = e.getMessage();
            status = "fail";
        }
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping({"i/monitorItemValue/{databaseName}/{databaseConfigId}"})
    @ResponseBody
    public Map<String, Object> monitorItemValue(@PathVariable String databaseName, @PathVariable String databaseConfigId)
            throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map0 = permissionService.getConfig(databaseConfigId);
        String databaseType = (String) map0.get("databaseType");
        String mess = "";
        String status = "";
        try {
            if (databaseType.equals("MySql")) {
                list = permissionService.monitorItemValue(databaseName, databaseConfigId);
                mess = "查询成功";
                status = "success";
            }
            if (databaseType.equals("MariaDB")) {
                list = permissionService.monitorItemValue(databaseName, databaseConfigId);
                mess = "查询成功";
                status = "success";
            }
            if (databaseType.equals("Oracle")) {
                list = permissionService.monitorItemValueForOracle(databaseName, databaseConfigId);
                mess = "查询成功";
                status = "success";
            }
            if (databaseType.equals("PostgreSQL")) {
                Thread.sleep(3000L);
                mess = "暂不支持";
                status = "fail";
            }
            if (databaseType.equals("MSSQL")) {
                Thread.sleep(3000L);
                mess = "暂不支持";
                status = "fail";
            }
            mess = "查询成功";
            status = "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = e.getMessage();
            status = "fail";
            return null;
        }
        map.put("mess", mess);
        map.put("status", status);
        map.put("rows", list);
        map.put("total", Integer.valueOf(list.size()));
        return map;
    }

    @RequestMapping(value = {"i/copyTable/{databaseConfigId}"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> copyTable(@RequestBody IdsDto tem, @PathVariable String databaseConfigId)
            throws Exception {
        String databaseName = tem.getDatabaseName();
        String tableName = tem.getTableName();
        Map<String, Object> map = permissionService.getConfig(databaseConfigId);
        String databaseType = (String) map.get("databaseType");
        Map<String, Object> mapResult = new HashMap<>();
        String mess = "";
        String status = "";
        try {
            if (databaseType.equals("MySql")) {
                permissionService.copyTableForMySql(databaseName, tableName, databaseConfigId);
                Thread.sleep(3000L);
                mess = "复制表完成！";
                status = "success";
            }
            if (databaseType.equals("MariaDB")) {
                permissionService.copyTableForMySql(databaseName, tableName, databaseConfigId);
                Thread.sleep(3000L);
                mess = "复制表完成！";
                status = "success";
            }
            if (databaseType.equals("Oracle")) {
                permissionService.copyTableForOracle(databaseName, tableName, databaseConfigId);
                Thread.sleep(3000L);
                mess = "复制表完成！";
                status = "success";
            }
            if (databaseType.equals("PostgreSQL")) {
                permissionService.copyTableForPostgreSQL(databaseName, tableName, databaseConfigId);
                Thread.sleep(3000L);
                mess = "复制表完成！";
                status = "success";
            }
            if (databaseType.equals("MSSQL")) {
                mess = "暂不支持MSSQL复制表！";
                status = "fail";
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = "复制表失败！";
            status = "fail";
        }
        mapResult.put("mess", mess);
        mapResult.put("status", status);
        return mapResult;
    }

    @RequestMapping(value = {"i/renameTable/{databaseConfigId}"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> renameTable(@RequestBody IdsDto tem, @PathVariable String databaseConfigId)
            throws Exception {
        String databaseName = tem.getDatabaseName();
        String tableName = tem.getTableName();
        String newTableName = tem.getNewTableName();
        Map<String, Object> map = permissionService.getConfig(databaseConfigId);
        String databaseType = (String) map.get("databaseType");
        Map<String, Object> mapResult = new HashMap<>();
        String mess = "";
        String status = "";
        try {
            if (databaseType.equals("MySql")) {
                permissionService.renameTableForMySql(databaseName, tableName, databaseConfigId, newTableName);
                Thread.sleep(3000L);
                mess = "操作完成！";
                status = "success";
            }
            if (databaseType.equals("MariaDB")) {
                permissionService.renameTableForMySql(databaseName, tableName, databaseConfigId, newTableName);
                Thread.sleep(3000L);
                mess = "操作完成！";
                status = "success";
            }
            if (databaseType.equals("Oracle")) {
                permissionService.renameTableForOracle(databaseName, tableName, databaseConfigId, newTableName);
                Thread.sleep(3000L);
                mess = "操作完成！";
                status = "success";
            }
            if (databaseType.equals("PostgreSQL")) {
                permissionService.renameTableForPostgreSQL(databaseName, tableName, databaseConfigId, newTableName);
                Thread.sleep(3000L);
                mess = "操作完成！";
                status = "success";
            }
            if (databaseType.equals("Hive2")) {
                permissionService.renameTableForHive2(databaseName, tableName, databaseConfigId, newTableName);
                Thread.sleep(3000L);
                mess = "操作完成！";
                status = "success";
            }
            if (databaseType.equals("MSSQL")) {
                mess = "暂不支持MSSQL重命名表！";
                status = "fail";
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = "重命名表失败！";
            status = "fail";
        }
        mapResult.put("mess", mess);
        mapResult.put("status", status);
        return mapResult;
    }

    @RequestMapping(value = {"i/deleteBackupFile"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> deleteBackupFile(@RequestBody IdsDto tem) {
        String[] ids = tem.getIds();
        String mess = "";
        String status = "";
        String path = request.getSession().getServletContext().getRealPath("/") + "backup" + File.separator;
        try {
            permissionService.deleteBackupFile(ids, path);
            mess = "删除成功";
            status = "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = e.getMessage();
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"i/backupFileDownload/{fileName:.+}"}, method = {RequestMethod.GET})
    @ResponseBody
    public void backupFileDownload(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        String path = request.getSession().getServletContext().getRealPath("/") + "backup" + File.separator;
        File file = new File(path + fileName);
        response.setContentType("multipart/form-data");
        response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
        response.addHeader("Content-Length", String.valueOf(file.length()));
        try {
            FileInputStream inputStream = new FileInputStream(file);
            ServletOutputStream out = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            inputStream.close();
            out.close();
            out.flush();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @RequestMapping(value = {"i/zipFile"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> zipFile(@RequestBody IdsDto tem) {
        String path = request.getSession().getServletContext().getRealPath("/") + "backup" + File.separator;
        String mess = "";
        String status = "";
        String fileName = "";
        String[] ids = tem.getIds();
        try {
            for (int i = 0; i < ids.length; i++) {
                fileName = ids[i];
                File srcFile = new File(path + fileName);
                File zipFile = new File(path + fileName.replaceAll(".sql", "") + ".zip");
                Project prj = new Project();
                Zip zip = new Zip();
                zip.setProject(prj);
                zip.setDestFile(zipFile);
                FileSet fileSet = new FileSet();
                fileSet.setProject(prj);
                fileSet.setFile(srcFile);
                zip.addFileset(fileSet);
                zip.execute();
            }
            mess = "压缩成功";
            status = "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = "压缩文件出错";
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"i/unzipFile"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> unzip(String zipFilepath, String destDir) {
        String mess = "";
        String status = "";
        try {
            if (!new File(zipFilepath).exists()) {
                throw new RuntimeException("zip file " + zipFilepath + " does not exist.");
            }
            Project proj = new Project();
            Expand expand = new Expand();
            expand.setProject(proj);
            expand.setTaskType("unzip");
            expand.setTaskName("unzip");
            expand.setSrc(new File(zipFilepath));
            expand.setDest(new File(destDir));
            expand.execute();
            mess = "解压缩成功";
            status = "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = "解压缩文件出错";
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"i/dropTable/{databaseConfigId}"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> dropTable(@RequestBody IdsDto tem, @PathVariable String databaseConfigId)
            throws Exception {
        String mess = "";
        String status = "";
        Map<String, Object> map = new HashMap<>();
        String tableName = tem.getTableName();
        String databaseName = tem.getDatabaseName();
        HttpSession session = request.getSession(true);
        String username = (String) session.getAttribute("LOGIN_USER_NAME");
        String ip = NetworkUtil.getIpAddress(request);
        Map<String, Object> map0 = permissionService.getConfig(databaseConfigId);
        String databaseType = (String) map0.get("databaseType");
        boolean bool = true;
        try {
            if ((tableName != null) || (!"".equals(tableName))) {
                if (databaseType.equals("Oracle")) {
                    bool = permissionService.dropTableForOracle(databaseName, tableName, databaseConfigId);
                } else if (databaseType.equals("PostgreSQL")) {
                    bool = permissionService.dropTableForPostgreSQL(databaseName, tableName, databaseConfigId);
                } else {
                    bool = permissionService.dropTable(databaseName, tableName, databaseConfigId);
                }
            }
            mess = "删除成功";
            status = "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = "删除失败," + e.getMessage();
            status = "fail";
        }
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"i/dropDatabase/{databaseConfigId}"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> dropDatabase(@RequestBody IdsDto tem, @PathVariable String databaseConfigId)
            throws Exception {
        String mess = "";
        String status = "";
        Map<String, Object> map = new HashMap<>();
        String databaseName = tem.getDatabaseName();
        HttpSession session = request.getSession(true);
        String username = (String) session.getAttribute("LOGIN_USER_NAME");
        String ip = NetworkUtil.getIpAddress(request);
        boolean bool = true;
        try {
            bool = permissionService.dropDatabase(databaseName, databaseConfigId);
            mess = "删除成功";
            status = "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = "删除失败";
            status = "fail";
        }
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"i/clearTable/{databaseConfigId}"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> clearTable(@PathVariable String databaseConfigId, @RequestBody IdsDto tem)
            throws Exception {
        String mess = "";
        String status = "";
        Map<String, Object> map = new HashMap<>();
        String tableName = tem.getTableName();
        String databaseName = tem.getDatabaseName();
        HttpSession session = request.getSession(true);
        String username = (String) session.getAttribute("LOGIN_USER_NAME");
        String ip = NetworkUtil.getIpAddress(request);
        Map<String, Object> map0 = permissionService.getConfig(databaseConfigId);
        String databaseType = (String) map0.get("databaseType");
        boolean bool = true;
        try {
            if ((tableName != null) || (!"".equals(tableName))) {
                bool = permissionService.clearTable(databaseName, tableName, databaseConfigId);
            }
            mess = "删除成功";
            status = "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = "删除失败";
            status = "fail";
        }
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"i/showTableMess/{tableName}/{databaseName}/{databaseConfigId}"}, method = {RequestMethod.GET})
    public String showTableMess(@PathVariable String tableName, @PathVariable String databaseName, @PathVariable String databaseConfigId) {
        request.setAttribute("databaseName", databaseName);
        request.setAttribute("tableName", tableName);
        request.setAttribute("databaseConfigId", databaseConfigId);
        return "system/showTableMess";
    }

    @RequestMapping(value = {"i/viewTableMess/{tableName}/{databaseName}/{databaseConfigId}"}, method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> viewTableMess(@PathVariable String tableName, @PathVariable String databaseName, @PathVariable String databaseConfigId)
            throws Exception {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map0 = permissionService.getConfig(databaseConfigId);
        String databaseType = (String) map0.get("databaseType");
        List<Map<String, Object>> listAllColumn = new ArrayList<>();
        if (databaseType.equals("MySql")) {
            listAllColumn = permissionService.viewTableMessForMySql(databaseName, tableName, databaseConfigId);
        }
        if (databaseType.equals("MariaDB")) {
            listAllColumn = permissionService.viewTableMessForMySql(databaseName, tableName, databaseConfigId);
        }
        if (databaseType.equals("Oracle")) {
            listAllColumn = permissionService.viewTableMessForOracle(databaseName, tableName, databaseConfigId);
        }
        if (databaseType.equals("PostgreSQL")) {
            listAllColumn = permissionService.viewTableMessForPostgreSQL(databaseName, tableName, databaseConfigId);
        }
        if (databaseType.equals("MSSQL")) {
            listAllColumn = permissionService.viewTableMessForMSSQL(databaseName, tableName, databaseConfigId);
        }
        if (databaseType.equals("Hive2")) {
            listAllColumn = permissionService.viewTableMessForHive2(databaseName, tableName, databaseConfigId);
        }
        map.put("rows", listAllColumn);
        map.put("total", Integer.valueOf(listAllColumn.size()));
        return map;
    }

    @RequestMapping(value = {"i/saveNewTable/{databaseConfigId}"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> saveNewTable(@PathVariable String databaseConfigId)
            throws Exception {
        Map<String, Object> mapResult = new HashMap<>();
        String mess = "";
        String status = "";
        Map<String, Object> map = permissionService.getConfig(databaseConfigId);
        String databaseType = (String) map.get("databaseType");
        String databaseName = request.getParameter("databaseName");
        String tableName = request.getParameter("tableName");
        String inserted = request.getParameter("inserted");
        try {
            JSONObject jsonObject = JSONObject.parseObject(inserted);
            JSONArray insertArray = jsonObject.getJSONArray("rows");
            if (databaseType.equals("MySql")) {
                permissionService.saveNewTable(insertArray, databaseName, tableName, databaseConfigId);
            }
            if (databaseType.equals("MariaDB")) {
                permissionService.saveNewTable(insertArray, databaseName, tableName, databaseConfigId);
            }
            if (databaseType.equals("Oracle")) {
                permissionService.saveNewTableForOracle(insertArray, databaseName, tableName, databaseConfigId);
            }
            if (databaseType.equals("PostgreSQL")) {
                permissionService.saveNewTableForPostgreSQL(insertArray, databaseName, tableName, databaseConfigId);
            }
            if (databaseType.equals("MSSQL")) {
                permissionService.saveNewTableForMSSQL(insertArray, databaseName, tableName, databaseConfigId);
            }
            mess = "保存成功！";
            status = "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = "保存出错！" + e.getMessage();
            status = "fail";
        }
        mapResult.put("mess", mess);
        mapResult.put("status", status);
        return mapResult;
    }

    @RequestMapping(value = {"i/restoreDB/{databaseConfigId}"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> restoreDB(@RequestBody IdsDto tem, @PathVariable String databaseConfigId) {
        String path = request.getSession().getServletContext().getRealPath("/") + "backup" + File.separator;
        String mess = "";
        String status = "";
        String fileName = "";
        String[] ids = tem.getIds();
        try {
            for (int i = 0; i < ids.length; i++) {
                fileName = ids[i];
                File file = new File(path + fileName);
                if (fileName.indexOf(".rar") > 0) {
                    FileUtil.unRarFile(path + fileName, path + "temp" + File.separator);
                    doImportDB(path + "temp" + File.separator);
                    for (int a = 0; a < tempFileList.size(); a++) {
                        permissionService.restoreDBFromFile(tem.getDatabaseName(), tempFileList.get(a), databaseConfigId);
                    }
                }
                if (fileName.indexOf(".zip") > 0) {
                    FileUtil.unZipFiles(file, path + "temp" + File.separator);
                    doImportDB(path + "temp" + File.separator);
                    for (int a = 0; a < tempFileList.size(); a++) {
                        permissionService.restoreDBFromFile(tem.getDatabaseName(), tempFileList.get(a), databaseConfigId);
                    }
                }
                if (fileName.toUpperCase().contains(".SQL")) {
                    permissionService.restoreDBFromFile(tem.getDatabaseName(), path + fileName, databaseConfigId);
                }
            }
            mess = "数据库还原成功";
            status = "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = "数据库还原出错!";
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    List<String> tempFileList = new ArrayList<>();

    public void doImportDB(String path) {
        File file = new File(path);
        getSqlFile(file);
    }

    public void getSqlFile(File f) {
        if (f != null) {
            String fileName = f.getName();
            if (f.isDirectory()) {
                File[] fileArray = f.listFiles();
                if (fileArray != null) {
                    for (File file : fileArray) {
                        getSqlFile(file);
                    }
                }
            } else if (fileName.toUpperCase().contains(".SQL")) {
                tempFileList.add(f.getAbsolutePath());
            }
        }
    }

    @RequestMapping(value = {"i/deleteConfig"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> deleteConfig(@RequestBody IdsDto tem) {
        String[] ids = tem.getIds();
        String mess = "";
        String status = "";
        try {
            permissionService.deleteConfig(ids);
            mess = "删除成功";
            status = "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            mess = e.getMessage();
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"i/getDataBaseConfig/{databaseConfigId}"}, method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getDataBaseConfig(@PathVariable String databaseConfigId) {
        Map<String, Object> map = permissionService.getConfig(databaseConfigId);
        map.remove("password");
        return map;
    }

    @RequestMapping(value = {"i/dataSynchronize"}, method = {RequestMethod.GET})
    public String dataSynchronize(Model model) {
        return "system/dataSynchronizeList";
    }

    @RequestMapping(value = {"i/addDataSynchronizeForm"}, method = {RequestMethod.GET})
    public String addDataSynchronizeForm(Model model) {
        List<Map<String, Object>> configList = permissionService.getAllConfigList();
        model.addAttribute("configList", configList);
        return "system/dataSynchronizeForm";
    }

    @RequestMapping(value = {"i/dataSynchronizeLogForm/{dataSynchronizeId}"}, method = {RequestMethod.GET})
    public String dataSynchronizeLogForm(@PathVariable String dataSynchronizeId) {
        request.setAttribute("dataSynchronizeId", dataSynchronizeId);
        return "system/dataSynchronizeLogForm";
    }

    @RequestMapping(value = {"i/treeShowData/{tableName}/{dbName}/{databaseConfigId}"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> treeShowData(@PathVariable String tableName, @PathVariable String dbName, @PathVariable String databaseConfigId, @RequestBody Page<Map<String, Object>> page)
            throws Exception {
        Map<String, Object> map2 = new HashMap<>();
        return map2;
    }
}
