package org.jeecg.modules.airag;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.service.tool.ToolExecutor;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.airag.llm.handler.JeecgToolsProvider;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.system.controller.SysUserController;
import org.jeecg.modules.system.entity.SysRole;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * for [QQYUN-13565]【AI助手】新增创建用户和查询用户的工具扩展
 * @Description: jeecg llm工具提供者
 * @Author: chenrui
 * @Date: 2025/8/26 18:06
 */
@Component
public class JeecgBizToolsProvider implements JeecgToolsProvider {

    @Autowired
    SysUserController sysUserController;

    @Autowired
    SysUserMapper userMapper;

    @Autowired
    private BaseCommonService baseCommonService;

    @Autowired
    private org.jeecg.modules.system.service.ISysRoleService sysRoleService;

    @Autowired
    private org.jeecg.modules.system.service.ISysUserRoleService sysUserRoleService;

    @Autowired
    private org.jeecg.modules.system.service.ISysUserService sysUserService;

    public Map<ToolSpecification, ToolExecutor> getDefaultTools(){
        Map<ToolSpecification, ToolExecutor> tools = new HashMap<>();
        JeecgLlmTools userTool = queryUserTool();
        tools.put(userTool.getToolSpecification(), userTool.getToolExecutor());
        JeecgLlmTools addUser = addUserTool();
        tools.put(addUser.getToolSpecification(), addUser.getToolExecutor());
        // 新增：查询所有角色
        JeecgLlmTools queryRoles = queryAllRolesTool();
        tools.put(queryRoles.getToolSpecification(), queryRoles.getToolExecutor());
        // 新增：给用户授予角色
        JeecgLlmTools grantRoles = grantUserRolesTool();
        tools.put(grantRoles.getToolSpecification(), grantRoles.getToolExecutor());
        return tools;
    }

    /**
     * 添加用户
     * @return
     * @author chenrui
     * @date 2025/8/27 09:51
     */
    private JeecgLlmTools addUserTool(){
        ToolSpecification toolSpecification = ToolSpecification.builder()
                .name("add_user")
                .description("添加用户,返回添加结果;" +
                        "\n\n - 缺少必要字段时,请向用户索要." +
                        "\n\n - 你应该提前判断用户的输入是否合法,比如用户名是否符合规范,手机号和邮箱是否正确等." +
                        "\n\n - 提前使用用户名查询用户是否存在,如果存在则不能添加." +
                        "\n\n - 添加成功后返回成功消息,如果失败则返回失败原因." +
                        "\n\n - 用户名,邮箱,手机号均要求唯一,提前通过查询用户工具确认唯一性." )
                .parameters(
                        JsonObjectSchema.builder()
                                .addStringProperty("username", "用户名,必填,只允许使用字母、数字、下划线，且必须以字母开头,唯一")
                                .addStringProperty("password", "用户密码,必填")
                                .addStringProperty("realname", "真实姓名,必填")
                                //.addStringProperty("email", "邮箱,必填,唯一")
                                .addStringProperty("phone", "手机号,必填,唯一")
                                .required("username","password","realname","workNo","email","phone")
                                .build()
                )
                .build();
        ToolExecutor toolExecutor = (toolExecutionRequest, memoryId) -> {
            JSONObject arguments = JSONObject.parseObject(toolExecutionRequest.arguments());
            arguments.put("confirmPassword",arguments.get("password"));
            arguments.put("userIdentity",1);
            arguments.put("activitiSync",1);
            arguments.put("departIds","");
            String selectedRoles = arguments.getString("selectedroles");
            String selectedDeparts = arguments.getString("selecteddeparts");
            String msg = "添加用户失败";
            try {
                SysUser user = JSON.parseObject(arguments.toJSONString(), SysUser.class);
                user.setCreateTime(new Date());//设置创建时间
                String salt = oConvertUtils.randomGen(8);
                user.setSalt(salt);
                String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), salt);
                user.setPassword(passwordEncode);
                user.setStatus(1);
                user.setDelFlag(CommonConstant.DEL_FLAG_0);
                //用户表字段org_code不能在这里设置他的值
                user.setOrgCode(null);
                // 保存用户走一个service 保证事务
                //获取租户ids
                String relTenantIds = arguments.getString("relTenantIds");
                sysUserService.saveUser(user, selectedRoles, selectedDeparts, relTenantIds, false);
                baseCommonService.addLog("添加用户，username： " +user.getUsername() ,CommonConstant.LOG_TYPE_2, 2);
                msg = "添加用户成功";
                // 用户变更，触发同步工作流
            } catch (Exception e) {
                msg = "添加用户失败";
            }
            return msg;
        };
        return new JeecgLlmTools(toolSpecification,toolExecutor);
    }

    /**
     * 查询用户信息
     *
     * @return 用户列表JSON字符串
     * @author chenrui
     * @date 2025/8/26 18:52
     */
    private JeecgLlmTools queryUserTool() {
        ToolSpecification toolSpecification = ToolSpecification.builder()
                .name("query_user_by_name")
                .description("查询用户详细信息，返回json数组。支持用户名、真实姓名、邮箱、手机号 多字段组合查询，用户名、真实姓名、邮箱、手机号均为模糊查询。无条件则返回全部用户。")
                .parameters(
                        JsonObjectSchema.builder()
                                .addStringProperty("username", "用户名")
                                .addStringProperty("realname", "真实姓名")
                                .addStringProperty("email", "电子邮件")
                                .addStringProperty("phone", "手机号")
                                .build()
                )
                .build();
        ToolExecutor toolExecutor = (toolExecutionRequest, memoryId) -> {
            SysUser args = JSONObject.parseObject(toolExecutionRequest.arguments(), SysUser.class);
            QueryWrapper<SysUser> qw = new QueryWrapper<>();
            if (StringUtils.isNotBlank(args.getUsername())) {
                qw.like("username", args.getUsername());
            }
            if (StringUtils.isNotBlank(args.getRealname())) {
                qw.like("realname", args.getRealname());
            }
            if (StringUtils.isNotBlank(args.getEmail())) {
                qw.like("email", args.getEmail());
            }
            if (StringUtils.isNotBlank(args.getPhone())) {
                qw.like("phone", args.getPhone());
            }
            if (StringUtils.isNotBlank(args.getWorkNo())) {
                qw.eq("work_no", args.getWorkNo());
            }
            qw.eq("del_flag", 0);
            List<SysUser> users = userMapper.selectList(qw);
            users.forEach(u -> { u.setPassword(null); u.setSalt(null); });
            return JSONObject.toJSONString(users);
        };
        return new JeecgLlmTools(toolSpecification, toolExecutor);
    }

    /**
     * 查询所有角色
     * @return
     * @author chenrui
     * @date 2025/8/27 09:52
     */
    private JeecgLlmTools queryAllRolesTool() {
        ToolSpecification spec = ToolSpecification.builder()
                .name("query_all_roles")
                .description("查询所有角色，返回json数组。包含字段：id、roleName、roleCode；默认按创建时间/排序号规则由后端决定。")
                .parameters(
                        JsonObjectSchema.builder()
                                .addStringProperty("roleName", "角色姓名")
                                .addStringProperty("roleCode", "角色编码")
                                .build()
                )
                .build();
        ToolExecutor exec = (toolExecutionRequest, memoryId) -> {
            // 做租户隔离查询（若开启）
            SysRole sysRole = JSONObject.parseObject(toolExecutionRequest.arguments(), SysRole.class);
            QueryWrapper<SysRole> qw = Wrappers.query();
            if (StringUtils.isNotBlank(sysRole.getRoleName())) {
                qw.like("role_name", sysRole.getRoleName());
            }
            if (StringUtils.isNotBlank(sysRole.getRoleCode())) {
                qw.like("role_code", sysRole.getRoleCode());
            }
            // 未删除
            List<org.jeecg.modules.system.entity.SysRole> roles = sysRoleService.list(qw);
            // 仅返回核心字段
            JSONArray arr = new JSONArray();
            for (org.jeecg.modules.system.entity.SysRole r : roles) {
                JSONObject o = new JSONObject();
                o.put("id", r.getId());
                o.put("roleName", r.getRoleName());
                o.put("roleCode", r.getRoleCode());
                arr.add(o);
            }
            return arr.toJSONString();
        };
        return new JeecgLlmTools(spec, exec);
    }

    /**
     * 给用户授予角色
     * @return
     * @author chenrui
     * @date 2025/8/27 09:52
     */
    private JeecgLlmTools grantUserRolesTool() {
        ToolSpecification spec = ToolSpecification.builder()
                .name("grant_user_roles")
                .description("给用户授予角色，支持一次授予多个角色；如果关系已存在则跳过。返回授予结果统计。")
                .parameters(
                        JsonObjectSchema.builder()
                                .addStringProperty("userId", "用户ID，必填")
                                .addStringProperty("roleIds", "角色ID列表，必填，使用英文逗号分隔")
                                .required("userId","roleIds")
                                .build()
                )
                .build();
        ToolExecutor exec = (toolExecutionRequest, memoryId) -> {
            JSONObject args = JSONObject.parseObject(toolExecutionRequest.arguments());
            String userId = args.getString("userId");
            String roleIdsStr = args.getString("roleIds");
            if (org.apache.commons.lang3.StringUtils.isAnyBlank(userId, roleIdsStr)) {
                return "参数缺失：userId 或 roleIds";
            }
            org.jeecg.modules.system.entity.SysUser user = sysUserService.getById(userId);
            if (user == null) {
                return "用户不存在：" + userId;
            }
            String[] roleIds = roleIdsStr.split(",");
            int added = 0, existed = 0, invalid = 0;
            for (String roleId : roleIds) {
                roleId = roleId.trim();
                if (roleId.isEmpty()) continue;
                org.jeecg.modules.system.entity.SysRole role = sysRoleService.getById(roleId);
                if (role == null) { invalid++; continue; }
                com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<org.jeecg.modules.system.entity.SysUserRole> q = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
                q.eq("role_id", roleId).eq("user_id", userId);
                org.jeecg.modules.system.entity.SysUserRole one = sysUserRoleService.getOne(q);
                if (one == null) {
                    org.jeecg.modules.system.entity.SysUserRole rel = new org.jeecg.modules.system.entity.SysUserRole(userId, roleId);
                    boolean ok = sysUserRoleService.save(rel);
                    if (ok) { added++; } else { invalid++; }
                } else {
                    existed++;
                }
            }
            return String.format("授予完成：新增%d，已存在%d，无效/失败%d", added, existed, invalid);
        };
        return new JeecgLlmTools(spec, exec);
    }
}
