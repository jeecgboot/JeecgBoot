package com.bomaos.theme.controller;

import com.bomaos.common.core.annotation.OperLog;
import com.bomaos.common.core.web.*;
import com.bomaos.theme.entity.Theme;
import com.bomaos.theme.service.ThemeService;
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
 * 主题配置管理
 * Created by Panyoujie on 2021-06-28 00:36:29
 */
@Controller
@RequestMapping("/theme/theme")
public class ThemeController extends BaseController {
    @Autowired
    private ThemeService themeService;

    @RequiresPermissions("theme:theme:view")
    @RequestMapping()
    public String view() {
        return "theme/theme.html";
    }

    /**
     * 分页查询主题配置
     */
    @RequiresPermissions("theme:theme:list")
    @OperLog(value = "主题配置管理", desc = "分页查询")
    @ResponseBody
    @RequestMapping("/page")
    public PageResult<Theme> page(HttpServletRequest request) {
        PageParam<Theme> pageParam = new PageParam<>(request);
        return new PageResult<>(themeService.page(pageParam, pageParam.getWrapper()).getRecords(), pageParam.getTotal());
    }

    /**
     * 查询全部主题配置
     */
    @RequiresPermissions("theme:theme:list")
    @OperLog(value = "主题配置管理", desc = "查询全部")
    @ResponseBody
    @RequestMapping("/list")
    public JsonResult list(HttpServletRequest request) {
        PageParam<Theme> pageParam = new PageParam<>(request);
        return JsonResult.ok().setData(themeService.list(pageParam.getOrderWrapper()));
    }

    /**
     * 根据id查询主题配置
     */
    @RequiresPermissions("theme:theme:list")
    @OperLog(value = "主题配置管理", desc = "根据id查询")
    @ResponseBody
    @RequestMapping("/get")
    public JsonResult get(Integer id) {
        return JsonResult.ok().setData(themeService.getById(id));
    }

    /**
     * 添加主题配置
     */
    @RequiresPermissions("theme:theme:save")
    @OperLog(value = "主题配置管理", desc = "添加", param = false, result = true)
    @ResponseBody
    @RequestMapping("/save")
    public JsonResult save(Theme theme) {
        if (themeService.save(theme)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 修改主题配置
     */
    @RequiresPermissions("theme:theme:update")
    @OperLog(value = "主题配置管理", desc = "修改", param = false, result = true)
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(Theme theme) {
        if (themeService.updateById(theme)) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 删除主题配置
     */
    @RequiresPermissions("theme:theme:remove")
    @OperLog(value = "主题配置管理", desc = "删除", result = true)
    @ResponseBody
    @RequestMapping("/remove")
    public JsonResult remove(Integer id) {
        if (themeService.removeById(id)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 批量添加主题配置
     */
    @RequiresPermissions("theme:theme:save")
    @OperLog(value = "主题配置管理", desc = "批量添加", param = false, result = true)
    @ResponseBody
    @RequestMapping("/saveBatch")
    public JsonResult saveBatch(@RequestBody List<Theme> list) {
        if (themeService.saveBatch(list)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 批量修改主题配置
     */
    @RequiresPermissions("theme:theme:update")
    @OperLog(value = "主题配置管理", desc = "批量修改", result = true)
    @ResponseBody
    @RequestMapping("/updateBatch")
    public JsonResult updateBatch(@RequestBody BatchParam<Theme> batchParam) {
        if (batchParam.update(themeService, "id")) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 批量删除主题配置
     */
    @RequiresPermissions("theme:theme:remove")
    @OperLog(value = "主题配置管理", desc = "批量删除", result = true)
    @ResponseBody
    @RequestMapping("/removeBatch")
    public JsonResult removeBatch(@RequestBody List<Integer> ids) {
        if (themeService.removeByIds(ids)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 修改状态
     */
    @OperLog(value = "主题配置管理", desc = "修改状态", result = true)
    @RequiresPermissions("theme:theme:update")
    @ResponseBody
    @RequestMapping("/status/update")
    public JsonResult updateStates(Integer id, Integer enabled) {
        if (enabled == null || (enabled != 0 && enabled != 1)) {
            return JsonResult.error("状态值不正确");
        }

        List<Theme> list = themeService.list();
        List<Theme> themeList = list.stream().map((theme) -> {
            theme.setEnable(0);
            return theme;
        }).collect(Collectors.toList());
        themeService.updateBatchById(themeList);

        Theme theme = new Theme();
        theme.setId(id);
        theme.setEnable(enabled);
        if (themeService.updateById(theme)) {
            return JsonResult.ok("主题切换成功！");
        }
        return JsonResult.error("主题切换失败！");
    }

}
