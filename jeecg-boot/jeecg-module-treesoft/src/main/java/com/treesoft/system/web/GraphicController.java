package com.treesoft.system.web;

import com.treesoft.system.service.PermissionService;
import com.treesoft.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping({"system/graphic"})
public class GraphicController extends BaseController {
    @Autowired
    HttpServletRequest request;

    @Autowired
    private PermissionService permissionService;

    @RequestMapping(value = {"graphicPage/{databaseName}/{databaseConfigId}/{temp}"}, method = {RequestMethod.GET})
    public String config(@PathVariable String databaseName, @PathVariable String databaseConfigId, @PathVariable String temp) {
        request.setAttribute("databaseName", databaseName);
        request.setAttribute("databaseConfigId", databaseConfigId);
        request.setAttribute("temp", temp);
        return "system/graphicPage";
    }

    @RequestMapping(value = {"getViewData/{databaseConfigId}"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getViewData(@PathVariable String databaseConfigId, String sql, String databaseName, String limitForm, String pageSize)
            throws Exception {
        Map<String, Object> map = permissionService.getConfig(databaseConfigId);
        String databaseType = (String) map.get("databaseType");
        String mess;
        String status;
        List<Map<String, Object>> dataList = new ArrayList<>();
        try {
            if (databaseType.equals("MySql")) {
                sql = "select * from (" + sql + ") tab limit " + limitForm + "," + pageSize;
                dataList = permissionService.selectAllDataFromSQLForMysql(databaseName, databaseConfigId, sql);
            }
            if (databaseType.equals("MariaDB")) {
                sql = "select * from (" + sql + ") tab limit " + limitForm + "," + pageSize;
                dataList = permissionService.selectAllDataFromSQLForMysql(databaseName, databaseConfigId, sql);
            }
            if (databaseType.equals("Oracle")) {
                dataList = permissionService.selectAllDataFromSQLForOracle(databaseName, databaseConfigId, sql);
            }
            if (databaseType.equals("PostgreSQL")) {
                dataList = permissionService.selectAllDataFromSQLForPostgreSQL(databaseName, databaseConfigId, sql);
            }
            if (databaseType.equals("MSSQL")) {
                dataList = permissionService.selectAllDataFromSQLForMSSQL(databaseName, databaseConfigId, sql);
            }
            if (databaseType.equals("Hive2")) {
                dataList = permissionService.selectAllDataFromSQLForHive2(databaseName, databaseConfigId, sql);
            }
            mess = "查询数据成功";
            status = "success";
        } catch (Exception e) {
            mess = "查询数据出错!" + e.getMessage();
            status = "fail";
        }
        Map<String, Object> result = new HashMap<>();
        result.put("mess", mess);
        result.put("status", status);
        result.put("rows", dataList);
        return result;
    }
}
