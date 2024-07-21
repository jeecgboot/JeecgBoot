package com.bomaos.common.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bomaos.common.core.annotation.OperLog;
import com.bomaos.common.core.exception.BusinessException;
import com.bomaos.common.core.web.BaseController;
import com.bomaos.common.core.web.JsonResult;
import com.bomaos.common.system.entity.Menu;
import com.bomaos.common.system.entity.RoleMenu;
import com.bomaos.common.system.service.MenuService;
import com.bomaos.common.system.service.RoleMenuService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色菜单管理
 * Created by Panyoujie on 2018-12-24 16:10
 */
@Controller
@RequestMapping("/sys/role/menu")
public class RoleMenuController extends BaseController {
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private MenuService menuService;

    /**
     * 查询角色菜单
     */
    @OperLog(value = "角色管理", desc = "查询角色菜单")
    @RequiresPermissions("sys:role:list")
    @ResponseBody
    @RequestMapping("/list")
    public JsonResult list(Integer roleId) {
        List<Menu> menus = menuService.list(new QueryWrapper<Menu>().orderByAsc("sort_number"));
        List<RoleMenu> roleMenus = roleMenuService.list(new QueryWrapper<RoleMenu>().eq("role_id", roleId));
        for (Menu menu : menus) {
            menu.setOpen(true);
            menu.setChecked(false);
            for (RoleMenu roleMenu : roleMenus) {
                if (menu.getMenuId().equals(roleMenu.getMenuId())) {
                    menu.setChecked(true);
                    break;
                }
            }
        }
        return JsonResult.ok().setData(menus);
    }

    /**
     * 添加角色菜单
     */
    @OperLog(value = "角色管理", desc = "添加角色菜单")
    @RequiresPermissions("sys:role:update")
    @ResponseBody
    @RequestMapping("/save")
    public JsonResult addRoleAuth(Integer roleId, Integer menuId) {
        RoleMenu roleMenu = new RoleMenu();
        roleMenu.setRoleId(roleId);
        roleMenu.setMenuId(menuId);
        if (roleMenuService.save(roleMenu)) {
            return JsonResult.ok();
        }
        return JsonResult.error();
    }

    /**
     * 移除角色菜单
     */
    @OperLog(value = "角色管理", desc = "移除角色菜单")
    @RequiresPermissions("sys:role:update")
    @ResponseBody
    @RequestMapping("/remove")
    public JsonResult removeRoleAuth(Integer roleId, Integer menuId) {
        if (roleMenuService.remove(new UpdateWrapper<RoleMenu>()
                .eq("role_id", roleId).eq("menuId", menuId))) {
            return JsonResult.ok();
        }
        return JsonResult.error();
    }

    /**
     * 批量修改角色菜单
     */
    @OperLog(value = "角色管理", desc = "修改角色菜单")
    @RequiresPermissions("sys:role:update")
    @Transactional
    @ResponseBody
    @RequestMapping("/update/{id}")
    public JsonResult setRoleAuth(@PathVariable("id") Integer roleId, @RequestBody List<Integer> menuIds) {
        roleMenuService.remove(new UpdateWrapper<RoleMenu>().eq("role_id", roleId));
        if (menuIds.size() > 0) {
            List<RoleMenu> roleMenuList = new ArrayList<>();
            for (Integer menuId : menuIds) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                roleMenuList.add(roleMenu);
            }
            if (roleMenuService.saveBatch(roleMenuList)) {
                return JsonResult.ok("保存成功");
            } else {
                throw new BusinessException("操作失败");
            }
        }
        return JsonResult.ok("保存成功");
    }

}
