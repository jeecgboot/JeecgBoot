package org.jeecg.modules.mcp;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: chenrui
 * @Date: 2025/8/21 17:10
 */
@Service("userMcpService")
public class UserMcpTool {

    @Autowired
    ISysBaseAPI sysBaseAPI;

    @Autowired
    ISysDepartService sysDepartmentService;

    @Tool(name = "query_user_by_name", description = "通过通过用户名查询用户信息,返回用户信息列表")
    public String queryUserByUsername(@ToolParam(description = "用户名,多个可以使用逗号分隔", required = true) String username) {
        if (username == null || username.isEmpty()) {
            return "Username cannot be null or empty";
        }
        List<JSONObject> users = sysBaseAPI.queryUsersByUsernames(username);
        return JSONObject.toJSONString(users);
    }

    @Tool(name = "add_depart", description = "新增部门信息,返回操作结果")
    public String saveDepartData(@ToolParam(description = "部门信息,departName(部门名称,必填),orgCategory(机构类别,必填, 1=公司，2=组织机构，3=岗位),parentId(父部门:父级部门的id,非必填)", required = true) SysDepart sysDepart){
        try {
            sysDepartmentService.saveDepartData(sysDepart, "mcpService");
        } catch (Exception e) {
            return "创建部门失败,原因:"+e.getMessage();
        }
        return "创建部门成功";
    }

}
