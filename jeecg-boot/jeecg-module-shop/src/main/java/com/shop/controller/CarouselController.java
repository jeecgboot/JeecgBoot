package com.shop.controller;

import com.shop.common.core.annotation.OperLog;
import com.shop.common.core.web.*;
import com.shop.entity.Carousel;
import com.shop.service.CarouselService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 轮播图管理管理
 * 2021-11-10 02:54:31
 */
@Controller
@RequestMapping("/content/carousel")
public class CarouselController extends BaseController {
    @Autowired
    private CarouselService carouselService;

    @RequiresPermissions("content:carousel:view")
    @RequestMapping()
    public String view() {
        return "content/carousel.html";
    }

    /**
     * 分页查询轮播图管理
     */
    @RequiresPermissions("content:carousel:list")
    @OperLog(value = "轮播图管理管理", desc = "分页查询")
    @ResponseBody
    @RequestMapping("/page")
    public PageResult<Carousel> page() {
        PageParam<Carousel> pageParam = new PageParam<>(request);
        return new PageResult<>(carouselService.page(pageParam, pageParam.getWrapper()).getRecords(), pageParam.getTotal());
    }

    /**
     * 查询全部轮播图管理
     */
    @RequiresPermissions("content:carousel:list")
    @OperLog(value = "轮播图管理管理", desc = "查询全部")
    @ResponseBody
    @RequestMapping("/list")
    public JsonResult list() {
        PageParam<Carousel> pageParam = new PageParam<>(request);
        return JsonResult.ok().setData(carouselService.list(pageParam.getOrderWrapper()));
    }

    /**
     * 根据id查询轮播图管理
     */
    @RequiresPermissions("content:carousel:list")
    @OperLog(value = "轮播图管理管理", desc = "根据id查询")
    @ResponseBody
    @RequestMapping("/get")
    public JsonResult get(Integer id) {
        return JsonResult.ok().setData(carouselService.getById(id));
    }

    /**
     * 添加轮播图管理
     */
    @RequiresPermissions("content:carousel:save")
    @OperLog(value = "轮播图管理管理", desc = "添加", param = false, result = true)
    @ResponseBody
    @RequestMapping("/save")
    public JsonResult save(Carousel carousel) {
        carousel.setCreateTime(new Date());
        carousel.setUpdateTime(new Date());
        carousel.setUsername(getLoginUsername());
        if (carouselService.save(carousel)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 修改轮播图管理
     */
    @RequiresPermissions("content:carousel:update")
    @OperLog(value = "轮播图管理管理", desc = "修改", param = false, result = true)
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(Carousel carousel) {
        if (carouselService.updateById(carousel)) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 删除轮播图管理
     */
    @RequiresPermissions("content:carousel:remove")
    @OperLog(value = "轮播图管理管理", desc = "删除", result = true)
    @ResponseBody
    @RequestMapping("/remove")
    public JsonResult remove(Integer id) {
        if (carouselService.removeById(id)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 批量添加轮播图管理
     */
    @RequiresPermissions("content:carousel:save")
    @OperLog(value = "轮播图管理管理", desc = "批量添加", param = false, result = true)
    @ResponseBody
    @RequestMapping("/saveBatch")
    public JsonResult saveBatch(@RequestBody List<Carousel> list) {
        for (Carousel carousel : list) {
            carousel.setUsername(getLoginUsername());
        }
        if (carouselService.saveBatch(list)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 批量修改轮播图管理
     */
    @RequiresPermissions("content:carousel:update")
    @OperLog(value = "轮播图管理管理", desc = "批量修改", result = true)
    @ResponseBody
    @RequestMapping("/updateBatch")
    public JsonResult updateBatch(@RequestBody BatchParam<Carousel> batchParam) {
        if (batchParam.update(carouselService, "id")) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 批量删除轮播图管理
     */
    @RequiresPermissions("content:carousel:remove")
    @OperLog(value = "轮播图管理管理", desc = "批量删除", result = true)
    @ResponseBody
    @RequestMapping("/removeBatch")
    public JsonResult removeBatch(@RequestBody List<Integer> ids) {
        if (carouselService.removeByIds(ids)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 修改状态
     */
    @OperLog(value = "轮播图管理", desc = "修改状态", result = true)
    @RequiresPermissions("content:carousel:update")
    @ResponseBody
    @RequestMapping("/status/update")
    public JsonResult updateStates(Integer id, Integer status) {
        if (status == null || (status != 0 && status != 1)) {
            return JsonResult.error("状态值不正确");
        }
        Carousel carousel = new Carousel();
        carousel.setId(id);
        carousel.setEnabled(status);
        if (carouselService.updateById(carousel)) {
            return JsonResult.ok("启用成功");
        }
        return JsonResult.error("启用失败");
    }

}
