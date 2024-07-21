package com.bomaos.settings.controller;

import com.alibaba.fastjson.JSON;
import com.bomaos.common.core.annotation.OperLog;
import com.bomaos.common.core.web.*;
import com.bomaos.settings.entity.ShopSettings;
import com.bomaos.settings.service.ShopSettingsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 商店设置管理
 * Created by Panyoujie on 2021-07-04 03:54:31
 */
@Controller
@RequestMapping("/settings/shopSettings")
public class ShopSettingsController extends BaseController {
    @Autowired
    private ShopSettingsService shopSettingsService;

    @RequiresPermissions("settings:shopSettings:view")
    @RequestMapping()
    public String view(Model model) {
        ShopSettings shopSettings = shopSettingsService.getById(1);
        model.addAttribute("shopSettings", JSON.toJSONString(shopSettings));
        return "settings/shopSettings.html";
    }

    /**
     * 分页查询商店设置
     */
    @RequiresPermissions("settings:shopSettings:list")
    @OperLog(value = "商店设置管理", desc = "分页查询")
    @ResponseBody
    @RequestMapping("/page")
    public PageResult<ShopSettings> page(HttpServletRequest request) {
        PageParam<ShopSettings> pageParam = new PageParam<>(request);
        return new PageResult<>(shopSettingsService.page(pageParam, pageParam.getWrapper()).getRecords(), pageParam.getTotal());
    }

    /**
     * 查询全部商店设置
     */
    @RequiresPermissions("settings:shopSettings:list")
    @OperLog(value = "商店设置管理", desc = "查询全部")
    @ResponseBody
    @RequestMapping("/list")
    public JsonResult list(HttpServletRequest request) {
        PageParam<ShopSettings> pageParam = new PageParam<>(request);
        return JsonResult.ok().setData(shopSettingsService.list(pageParam.getOrderWrapper()));
    }

    /**
     * 根据id查询商店设置
     */
    @RequiresPermissions("settings:shopSettings:list")
    @OperLog(value = "商店设置管理", desc = "根据id查询")
    @ResponseBody
    @RequestMapping("/get")
    public JsonResult get() {
        return JsonResult.ok().setData(shopSettingsService.getById(1));
    }

    /**
     * 添加商店设置
     */
    @RequiresPermissions("settings:shopSettings:save")
    @OperLog(value = "商店设置管理", desc = "添加", param = false, result = true)
    @ResponseBody
    @RequestMapping("/save")
    public JsonResult save(ShopSettings shopSettings) {
        if (shopSettingsService.save(shopSettings)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 修改商店设置
     */
    @RequiresPermissions("settings:shopSettings:update")
    @OperLog(value = "商店设置管理", desc = "修改", param = false, result = true)
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(ShopSettings shopSettings) {
        if (shopSettingsService.updateById(shopSettings)) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 删除商店设置
     */
    @RequiresPermissions("settings:shopSettings:remove")
    @OperLog(value = "商店设置管理", desc = "删除", result = true)
    @ResponseBody
    @RequestMapping("/remove")
    public JsonResult remove(Integer id) {
        if (shopSettingsService.removeById(id)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 批量添加商店设置
     */
    @RequiresPermissions("settings:shopSettings:save")
    @OperLog(value = "商店设置管理", desc = "批量添加", param = false, result = true)
    @ResponseBody
    @RequestMapping("/saveBatch")
    public JsonResult saveBatch(@RequestBody List<ShopSettings> list) {
        if (shopSettingsService.saveBatch(list)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 批量修改商店设置
     */
    @RequiresPermissions("settings:shopSettings:update")
    @OperLog(value = "商店设置管理", desc = "批量修改", result = true)
    @ResponseBody
    @RequestMapping("/updateBatch")
    public JsonResult updateBatch(@RequestBody BatchParam<ShopSettings> batchParam) {
        if (batchParam.update(shopSettingsService, "id")) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 批量删除商店设置
     */
    @RequiresPermissions("settings:shopSettings:remove")
    @OperLog(value = "商店设置管理", desc = "批量删除", result = true)
    @ResponseBody
    @RequestMapping("/removeBatch")
    public JsonResult removeBatch(@RequestBody List<Integer> ids) {
        if (shopSettingsService.removeByIds(ids)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

}
