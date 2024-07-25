package com.shop.controller;

import com.shop.common.core.annotation.OperLog;
import com.shop.common.core.web.*;
import com.shop.entity.Menu;
import com.shop.service.MenuService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 菜单管理
 * 2018-12-24 16:10
 */
@Controller
@RequestMapping("/sys/menu")
public class MenuController extends BaseController {
    @Autowired
    private MenuService menuService;

    @RequiresPermissions("sys:menu:view")
    @RequestMapping()
    public String view() {
        return "system/menu.html";
    }

    /**
     * 分页查询菜单
     */
    @OperLog(value = "菜单管理", desc = "分页查询")
    @RequiresPermissions("sys:menu:list")
    @ResponseBody
    @RequestMapping("/page")
    public PageResult<Menu> page() {
        PageParam<Menu> pageParam = new PageParam<>(request);
        pageParam.remove("username");
        pageParam.setDefaultOrder(new String[]{"sort_number"}, null);
        return menuService.listPage(pageParam);
    }

    /**
     * 查询全部菜单
     */
    @OperLog(value = "菜单管理", desc = "查询全部")
    @RequiresPermissions("sys:menu:list")
    @ResponseBody
    @RequestMapping("/list")
    public JsonResult list() {
        PageParam<Menu> pageParam = new PageParam<>(request);
        pageParam.remove("username");
        pageParam.setDefaultOrder(new String[]{"sort_number"}, null);
        return JsonResult.ok().setData(menuService.list(pageParam.getOrderWrapper()));
    }

    /**
     * 根据id查询菜单
     */
    @OperLog(value = "菜单管理", desc = "根据id查询")
    @RequiresPermissions("sys:menu:list")
    @ResponseBody
    @RequestMapping("/get")
    public JsonResult get(Integer id) {
        return JsonResult.ok().setData(menuService.getById(id));
    }

    /**
     * 添加菜单
     */
    @OperLog(value = "菜单管理", desc = "添加", param = false, result = true)
    @RequiresPermissions("sys:menu:save")
    @ResponseBody
    @RequestMapping("/save")
    public JsonResult save(Menu menu) {
        if (menuService.save(menu)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 修改菜单
     */
    @OperLog(value = "菜单管理", desc = "修改", param = false, result = true)
    @RequiresPermissions("sys:menu:update")
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(Menu menu) {
        if (menuService.updateById(menu)) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 删除菜单
     */
    @OperLog(value = "菜单管理", desc = "删除", result = true)
    @RequiresPermissions("sys:menu:remove")
    @ResponseBody
    @RequestMapping("/remove")
    public JsonResult remove(Integer id) {
        if (menuService.removeById(id)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 批量添加菜单
     */
    @OperLog(value = "菜单管理", desc = "批量添加", param = false, result = true)
    @RequiresPermissions("sys:menu:save")
    @ResponseBody
    @RequestMapping("/saveBatch")
    public JsonResult saveBatch(@RequestBody List<Menu> menuList) {
        if (menuService.saveBatch(menuList)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 批量修改菜单
     */
    @OperLog(value = "菜单管理", desc = "批量修改", result = true)
    @RequiresPermissions("sys:menu:update")
    @ResponseBody
    @RequestMapping("/updateBatch")
    public JsonResult updateBatch(@RequestBody BatchParam<Menu> batchParam) {
        if (batchParam.update(menuService, "menu_id")) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 批量删除菜单
     */
    @OperLog(value = "菜单管理", desc = "批量删除", result = true)
    @RequiresPermissions("sys:menu:remove")
    @ResponseBody
    @RequestMapping("/removeBatch")
    public JsonResult removeBatch(@RequestBody List<Integer> ids) {
        if (menuService.removeByIds(ids)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

}
