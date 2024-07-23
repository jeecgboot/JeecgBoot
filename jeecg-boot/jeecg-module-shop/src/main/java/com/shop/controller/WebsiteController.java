package com.shop.controller;

import com.alibaba.fastjson.JSON;
import com.shop.common.core.annotation.OperLog;
import com.shop.common.core.web.*;
import com.shop.service.WebsiteService;
import com.shop.entity.Website;
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
 * 网站设置管理
 * Created by Panyoujie on 2021-06-06 02:14:54
 */
@Controller
@RequestMapping("/website/website")
public class WebsiteController extends BaseController {

    @Autowired
    private WebsiteService websiteService;

    @RequiresPermissions("website:website:view")
    @RequestMapping()
    public String view(Model model) {
        Website website = websiteService.getById(1);
        model.addAttribute("website", JSON.toJSONString(website));
        return "website/website.html";
    }

    /**
     * 分页查询网站设置
     */
    @RequiresPermissions("website:website:list")
    @OperLog(value = "网站设置管理", desc = "分页查询")
    @ResponseBody
    @RequestMapping("/page")
    public PageResult<Website> page(HttpServletRequest request) {
        PageParam<Website> pageParam = new PageParam<>(request);
        pageParam.remove("username");
        return new PageResult<>(websiteService.page(pageParam, pageParam.getWrapper()).getRecords(), pageParam.getTotal());
    }

    /**
     * 查询全部网站设置
     */
    @RequiresPermissions("website:website:list")
    @OperLog(value = "网站设置管理", desc = "查询全部")
    @ResponseBody
    @RequestMapping("/list")
    public JsonResult list(HttpServletRequest request) {
        PageParam<Website> pageParam = new PageParam<>(request);
        pageParam.remove("username");
        return JsonResult.ok().setData(websiteService.list(pageParam.getOrderWrapper()));
    }

    /**
     * 根据id查询网站设置
     */
    @RequiresPermissions("website:website:list")
    @OperLog(value = "网站设置管理", desc = "根据id查询")
    @ResponseBody
    @RequestMapping("/get")
    public JsonResult get() {
        return JsonResult.ok().setData(websiteService.getById(1));
    }

    /**
     * 添加网站设置
     */
    @RequiresPermissions("website:website:save")
    @OperLog(value = "网站设置管理", desc = "添加", param = false, result = true)
    @ResponseBody
    @RequestMapping("/save")
    public JsonResult save(Website website) {
        if (websiteService.save(website)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 修改网站设置
     */
    @RequiresPermissions("website:website:update")
    @OperLog(value = "网站设置管理", desc = "修改", param = false, result = true)
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(Website website) {
        if (websiteService.updateById(website)) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 删除网站设置
     */
    @RequiresPermissions("website:website:remove")
    @OperLog(value = "网站设置管理", desc = "删除", result = true)
    @ResponseBody
    @RequestMapping("/remove")
    public JsonResult remove(Integer id) {
        if (websiteService.removeById(id)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 批量添加网站设置
     */
    @RequiresPermissions("website:website:save")
    @OperLog(value = "网站设置管理", desc = "批量添加", param = false, result = true)
    @ResponseBody
    @RequestMapping("/saveBatch")
    public JsonResult saveBatch(@RequestBody List<Website> list) {
        if (websiteService.saveBatch(list)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 批量修改网站设置
     */
    @RequiresPermissions("website:website:update")
    @OperLog(value = "网站设置管理", desc = "批量修改", result = true)
    @ResponseBody
    @RequestMapping("/updateBatch")
    public JsonResult updateBatch(@RequestBody BatchParam<Website> batchParam) {
        if (batchParam.update(websiteService, "id")) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 批量删除网站设置
     */
    @RequiresPermissions("website:website:remove")
    @OperLog(value = "网站设置管理", desc = "批量删除", result = true)
    @ResponseBody
    @RequestMapping("/removeBatch")
    public JsonResult removeBatch(@RequestBody List<Integer> ids) {
        if (websiteService.removeByIds(ids)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

}
