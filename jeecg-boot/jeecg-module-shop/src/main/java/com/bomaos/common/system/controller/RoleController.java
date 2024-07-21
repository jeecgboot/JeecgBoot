package com.bomaos.common.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bomaos.common.core.annotation.OperLog;
import com.bomaos.common.core.utils.CoreUtil;
import com.bomaos.common.core.web.BaseController;
import com.bomaos.common.core.web.JsonResult;
import com.bomaos.common.core.web.PageParam;
import com.bomaos.common.core.web.PageResult;
import com.bomaos.common.system.entity.Role;
import com.bomaos.common.system.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色管理
 * Created by Panyoujie on 2018-12-24 16:10
 */
@Controller
@RequestMapping("/sys/role")
public class RoleController extends BaseController {
    @Autowired
    private RoleService roleService;

    @RequiresPermissions("sys:role:view")
    @RequestMapping()
    public String view() {
        return "system/role.html";
    }

    /**
     * 分页查询角色
     */
    @OperLog(value = "角色管理", desc = "分页查询")
    @RequiresPermissions("sys:role:list")
    @ResponseBody
    @RequestMapping("/page")
    public PageResult<Role> page(HttpServletRequest request) {
        PageParam<Role> pageParam = new PageParam<>(request);
        return new PageResult<>(roleService.page(pageParam, pageParam.getWrapper()).getRecords(), pageParam.getTotal());
    }

    /**
     * 查询全部角色
     */
    @OperLog(value = "角色管理", desc = "查询全部")
    @RequiresPermissions("sys:role:list")
    @ResponseBody
    @RequestMapping("/list")
    public JsonResult list(HttpServletRequest request) {
        PageParam<Role> pageParam = new PageParam<>(request);
        return JsonResult.ok().setData(roleService.list(pageParam.getOrderWrapper()));
    }

    /**
     * 根据id查询角色
     */
    @OperLog(value = "角色管理", desc = "根据id查询")
    @RequiresPermissions("sys:role:list")
    @ResponseBody
    @RequestMapping("/get")
    public JsonResult get(Integer id) {
        return JsonResult.ok().setData(roleService.getById(id));
    }

    /**
     * 添加角色
     */
    @OperLog(value = "角色管理", desc = "添加", param = false, result = true)
    @RequiresPermissions("sys:role:save")
    @ResponseBody
    @RequestMapping("/save")
    public JsonResult save(Role role) {
        if (roleService.count(new QueryWrapper<Role>().eq("role_code", role.getRoleCode())) > 0) {
            return JsonResult.error("角色标识已存在");
        }
        if (roleService.count(new QueryWrapper<Role>().eq("role_name", role.getRoleName())) > 0) {
            return JsonResult.error("角色名称已存在");
        }
        if (roleService.save(role)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 修改角色
     */
    @OperLog(value = "角色管理", desc = "修改", param = false, result = true)
    @RequiresPermissions("sys:role:update")
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(Role role) {
        if (role.getRoleCode() != null && roleService.count(new QueryWrapper<Role>().eq("role_code", role.getRoleCode())
                .ne("role_id", role.getRoleId())) > 0) {
            return JsonResult.error("角色标识已存在");
        }
        if (role.getRoleName() != null && roleService.count(new QueryWrapper<Role>().eq("role_name", role.getRoleName())
                .ne("role_id", role.getRoleId())) > 0) {
            return JsonResult.error("角色名称已存在");
        }
        if (roleService.updateById(role)) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 删除角色
     */
    @OperLog(value = "角色管理", desc = "删除", result = true)
    @RequiresPermissions("sys:role:remove")
    @ResponseBody
    @RequestMapping("/remove")
    public JsonResult remove(Integer id) {
        if (roleService.removeById(id)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 批量添加角色
     */
    @OperLog(value = "角色管理", desc = "批量添加", param = false, result = true)
    @RequiresPermissions("sys:role:save")
    @ResponseBody
    @RequestMapping("/saveBatch")
    public JsonResult saveBatch(@RequestBody List<Role> list) {
        // 对集合本身进行非空和重复校验
        StringBuilder sb = new StringBuilder();
        sb.append(CoreUtil.listCheckBlank(list, "roleCode", "角色标识"));
        sb.append(CoreUtil.listCheckBlank(list, "roleName", "角色名称"));
        sb.append(CoreUtil.listCheckRepeat(list, "roleCode", "角色标识"));
        sb.append(CoreUtil.listCheckRepeat(list, "roleName", "角色名称"));
        if (sb.length() != 0) return JsonResult.error(sb.toString());
        // 数据库层面校验
        if (roleService.count(new QueryWrapper<Role>().in("role_code",
                list.stream().map(Role::getRoleCode).collect(Collectors.toList()))) > 0) {
            return JsonResult.error("角色标识已存在");
        }
        if (roleService.count(new QueryWrapper<Role>().in("role_name",
                list.stream().map(Role::getRoleName).collect(Collectors.toList()))) > 0) {
            return JsonResult.error("角色名称已存在");
        }
        if (roleService.saveBatch(list)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 批量删除角色
     */
    @OperLog(value = "角色管理", desc = "批量删除", result = true)
    @RequiresPermissions("sys:role:remove")
    @ResponseBody
    @RequestMapping("/removeBatch")
    public JsonResult removeBatch(@RequestBody List<Integer> ids) {
        if (roleService.removeByIds(ids)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

}
