package com.shop.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shop.common.core.annotation.OperLog;
import com.shop.common.core.web.*;
import com.shop.entity.Classifys;
import com.shop.entity.Products;
import com.shop.service.ClassifysService;
import com.shop.service.ProductsService;
import com.shop.vo.ClassifysVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分类管理
 * Created by Panyoujie on 2021-03-27 20:22:00
 */
@Controller
@RequestMapping("/products/classifys")
public class ClassifysController extends BaseController {
    @Autowired
    private ClassifysService classifysService;

    @Autowired
    private ProductsService productsService;

    @RequiresPermissions("products:classifys:view")
    @RequestMapping()
    public String view() {
        return "products/classifys.html";
    }

    /**
     * 分页查询分类
     */
    @RequiresPermissions("products:classifys:list")
    @OperLog(value = "分类管理", desc = "分页查询")
    @ResponseBody
    @RequestMapping("/page")
    public PageResult<ClassifysVo> page(HttpServletRequest request) {
        PageParam<Classifys> pageParam = new PageParam<>(request);
        pageParam.addOrderAsc("sort");

        List<Classifys> records = classifysService.page(pageParam, pageParam.getWrapper()).getRecords();
        List<ClassifysVo> classifysVoList = records.stream().map((classifys) -> {
            ClassifysVo classifysVo = new ClassifysVo();
            BeanUtils.copyProperties(classifys, classifysVo);
            // 获取商品的数量
            long count = productsService.count(new QueryWrapper<Products>().eq("classify_id", classifys.getId()));
            classifysVo.setProductsMember(count);
            return classifysVo;
        }).collect(Collectors.toList());

        return new PageResult<>(classifysVoList, pageParam.getTotal());
    }

    /**
     * 查询全部分类
     */
    @RequiresPermissions("products:classifys:list")
    @OperLog(value = "分类管理", desc = "查询全部")
    @ResponseBody
    @RequestMapping("/list")
    public JsonResult list(HttpServletRequest request) {
        PageParam<Classifys> pageParam = new PageParam<>(request);
        return JsonResult.ok().setData(classifysService.list(pageParam.getOrderWrapper()));
    }

    /**
     * 根据id查询分类
     */
    @RequiresPermissions("products:classifys:list")
    @OperLog(value = "分类管理", desc = "根据id查询")
    @ResponseBody
    @RequestMapping("/get")
    public JsonResult get(Integer id) {
        return JsonResult.ok().setData(classifysService.getById(id));
    }

    /**
     * 添加分类
     */
    @RequiresPermissions("products:classifys:save")
    @OperLog(value = "分类管理", desc = "添加", param = false, result = true)
    @ResponseBody
    @RequestMapping("/save")
    public JsonResult save(Classifys classifys) {
        classifys.setCreatedAt(new Date());
        classifys.setUpdatedAt(new Date());
        classifys.setUsername(getLoginUsername());
        if (classifysService.save(classifys)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 修改分类
     */
    @RequiresPermissions("products:classifys:update")
    @OperLog(value = "分类管理", desc = "修改", param = false, result = true)
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(Classifys classifys) {
        if (classifysService.updateById(classifys)) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 删除分类
     */
    @RequiresPermissions("products:classifys:remove")
    @OperLog(value = "分类管理", desc = "删除", result = true)
    @ResponseBody
    @RequestMapping("/remove")
    public JsonResult remove(Integer id) {

        long count = productsService.count(new QueryWrapper<Products>().eq("classify_id", id));
        if (count >= 1) {
            return JsonResult.error("该分类有商品存在，不允许删除");
        }

        if (classifysService.removeById(id)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 批量添加分类
     */
    @RequiresPermissions("products:classifys:save")
    @OperLog(value = "分类管理", desc = "批量添加", param = false, result = true)
    @ResponseBody
    @RequestMapping("/saveBatch")
    public JsonResult saveBatch(@RequestBody List<Classifys> list) {
        for (Classifys classifys : list) {
            classifys.setUsername(getLoginUsername());
        }
        if (classifysService.saveBatch(list)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 批量修改分类
     */
    @RequiresPermissions("products:classifys:update")
    @OperLog(value = "分类管理", desc = "批量修改", result = true)
    @ResponseBody
    @RequestMapping("/updateBatch")
    public JsonResult updateBatch(@RequestBody BatchParam<Classifys> batchParam) {
        if (batchParam.update(classifysService, "id")) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 批量删除分类
     */
    @RequiresPermissions("products:classifys:remove")
    @OperLog(value = "分类管理", desc = "批量删除", result = true)
    @ResponseBody
    @RequestMapping("/removeBatch")
    public JsonResult removeBatch(@RequestBody List<Integer> ids) {
        if (classifysService.removeByIds(ids)) {
            return JsonResult.ok("删除分类成功");
        }
        return JsonResult.error("删除分类失败！");
    }

    /**
     * 修改商品状态
     */
    @OperLog(value = "分类列表管理", desc = "修改状态", result = true)
    @RequiresPermissions("products:classifys:update")
    @ResponseBody
    @RequestMapping("/status/update")
    public JsonResult updateStates(Integer id, Integer status) {
        if (status == null || (status != 0 && status != 1)) {
            return JsonResult.error("状态值不正确");
        }
        Classifys classifys = new Classifys();
        classifys.setId(id);
        classifys.setStatus(status);
        if (classifysService.updateById(classifys)) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

}
