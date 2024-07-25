package com.shop.controller;

import com.shop.common.core.annotation.OperLog;
import com.shop.common.core.web.*;
import com.shop.entity.Classifys;
import com.shop.entity.Products;
import com.shop.service.ClassifysService;
import com.shop.service.ProductsService;
import com.shop.entity.Coupon;
import com.shop.service.CouponService;
import com.shop.vo.CouponVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 优惠券管理
 * 2021-06-23 07:43:23
 */
@Controller
@RequestMapping("/settings/coupon")
public class CouponController extends BaseController {
    @Autowired
    private CouponService couponService;

    @Autowired
    private ClassifysService classifysService;

    @Autowired
    private ProductsService productsService;

    @RequiresPermissions("settings:coupon:view")
    @RequestMapping()
    public String view() {
        return "settings/coupon.html";
    }

    @RequiresPermissions("settings:coupon:view")
    @RequestMapping("/add")
    public String view_add(Model model) {
        List<Classifys> classifyList = classifysService.list();
        model.addAttribute("classifyList", classifyList);
        return "settings/coupon_add.html";
    }

    /**
     * 分页查询优惠券
     */
    @RequiresPermissions("settings:coupon:list")
    @OperLog(value = "优惠券管理", desc = "分页查询")
    @ResponseBody
    @RequestMapping("/page")
    public PageResult<CouponVo> page() {
        PageParam<Coupon> pageParam = new PageParam<>(request);
        pageParam.addOrderDesc("create_time");
        List<Coupon> records = couponService.page(pageParam, pageParam.getWrapper()).getRecords();
        List<CouponVo> couponVoList = records.stream().map((coupon) -> {
            CouponVo couponVo = new CouponVo();
            BeanUtils.copyProperties(coupon, couponVo);
            Products products = productsService.getById(coupon.getProductId());
            couponVo.setProductName(products.getName());
            return couponVo;
        }).collect(Collectors.toList());
        return new PageResult<>(couponVoList, pageParam.getTotal());
    }

    /**
     * 查询全部优惠券
     */
    @RequiresPermissions("settings:coupon:list")
    @OperLog(value = "优惠券管理", desc = "查询全部")
    @ResponseBody
    @RequestMapping("/list")
    public JsonResult list() {
        PageParam<Coupon> pageParam = new PageParam<>(request);
        return JsonResult.ok().setData(couponService.list(pageParam.getOrderWrapper()));
    }

    /**
     * 根据id查询优惠券
     */
    @RequiresPermissions("settings:coupon:list")
    @OperLog(value = "优惠券管理", desc = "根据id查询")
    @ResponseBody
    @RequestMapping("/get")
    public JsonResult get(Integer id) {
        return JsonResult.ok().setData(couponService.getById(id));
    }

    /**
     * 添加优惠券
     */
    @RequiresPermissions("settings:coupon:save")
    @OperLog(value = "优惠券管理", desc = "添加", param = false, result = true)
    @ResponseBody
    @RequestMapping("/save")
    public JsonResult save(Coupon coupon) {
        if (coupon.getDiscountType() == 1 && coupon.getDiscountVal().compareTo(new BigDecimal(10)) == 1) {
            return JsonResult.error("折扣只能1-10之间的数字");
        }
        coupon.setUsername(getLoginUsername());
        if (couponService.save(coupon, getLoginUserId())) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 修改优惠券
     */
    @RequiresPermissions("settings:coupon:update")
    @OperLog(value = "优惠券管理", desc = "修改", param = false, result = true)
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(Coupon coupon) {
        if (couponService.updateById(coupon)) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 删除优惠券
     */
    @RequiresPermissions("settings:coupon:remove")
    @OperLog(value = "优惠券管理", desc = "删除", result = true)
    @ResponseBody
    @RequestMapping("/remove")
    public JsonResult remove(Integer id) {
        if (couponService.removeById(id)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 批量添加优惠券
     */
    @RequiresPermissions("settings:coupon:save")
    @OperLog(value = "优惠券管理", desc = "批量添加", param = false, result = true)
    @ResponseBody
    @RequestMapping("/saveBatch")
    public JsonResult saveBatch(@RequestBody List<Coupon> list) {
        for (Coupon coupon : list) {
            coupon.setUsername(getLoginUsername());
        }
        if (couponService.saveBatch(list)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 批量修改优惠券
     */
    @RequiresPermissions("settings:coupon:update")
    @OperLog(value = "优惠券管理", desc = "批量修改", result = true)
    @ResponseBody
    @RequestMapping("/updateBatch")
    public JsonResult updateBatch(@RequestBody BatchParam<Coupon> batchParam) {
        if (batchParam.update(couponService, "id")) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 批量删除优惠券
     */
    @RequiresPermissions("settings:coupon:remove")
    @OperLog(value = "优惠券管理", desc = "批量删除", result = true)
    @ResponseBody
    @RequestMapping("/removeBatch")
    public JsonResult removeBatch(@RequestBody List<Integer> ids) {
        if (couponService.removeByIds(ids)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

}
