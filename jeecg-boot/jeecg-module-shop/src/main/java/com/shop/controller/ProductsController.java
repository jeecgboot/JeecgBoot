package com.shop.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shop.entity.Cards;
import com.shop.entity.Classifys;
import com.shop.entity.Products;
import com.shop.service.CardsService;
import com.shop.common.core.annotation.OperLog;
import com.shop.common.core.utils.StringUtil;
import com.shop.common.core.web.*;
import com.shop.service.ClassifysService;
import com.shop.service.ProductsService;
import com.shop.vo.ProductsVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品管理
 * Created by Panyoujie on 2021-03-27 20:22:00
 */
@Controller
@RequestMapping("/products/products")
public class ProductsController extends BaseController {

    @Autowired
    private ProductsService productsService;

    @Autowired
    private ClassifysService classifysService;

    @Autowired
    private CardsService cardsService;

    @RequiresPermissions("products:products:view")
    @RequestMapping()
    public String view(Model model) {
        List<Classifys> classifysList = classifysService.listAll(null);
        model.addAttribute("classifysList", classifysList);
        return "products/products.html";
    }

    @RequiresPermissions("products:products:view")
    @RequestMapping("/addProduct")
    public String addView(Model model) {
        List<Classifys> classifysList = classifysService.listAll(null);
        model.addAttribute("classifysList", classifysList);
        return "products/add-product.html";
    }

    @RequiresPermissions("products:products:view")
    @RequestMapping("/editProduct/{productId}")
    public String editView(Model model, @PathVariable("productId") Integer productId) {
        List<Classifys> classifysList = classifysService.listAll(null);
        model.addAttribute("classifysList", classifysList);

        Products products = productsService.getById(productId);
        model.addAttribute("products", JSON.toJSONString(products));
        model.addAttribute("productId", productId);
        return "products/edit-product.html";
    }

    /**
     * 分页查询商品
     */
    @RequiresPermissions("products:products:list")
    @OperLog(value = "商品管理", desc = "分页查询")
    @ResponseBody
    @RequestMapping("/page")
    public PageResult<ProductsVo> page() {
        PageParam<Products> pageParam = new PageParam<>(request);
        pageParam.addOrderAsc("sort");
        List<Products> records = productsService.page(pageParam, pageParam.getWrapper()).getRecords();
        List<ProductsVo> productsVoList = records.stream().map((products) -> {
            ProductsVo productsVo = new ProductsVo();
            BeanUtils.copyProperties(products, productsVo);
            // 查出所属分类
            Classifys classifys = classifysService.getOne(new QueryWrapper<Classifys>().eq("id", products.getClassifyId()));
            productsVo.setClassifyName(classifys.getName());

            // 查出商品的卡密数量
            long count = cardsService.count(new QueryWrapper<Cards>().eq("product_id", products.getId()).eq("status", 0).eq("sell_type", 0));
            productsVo.setCardMember(count);

            long sellCount = cardsService.count(new QueryWrapper<Cards>().eq("product_id", products.getId()).eq("status", 1).eq("sell_type", 0));
            productsVo.setSellCardMember(sellCount);

            if (products.getSellType() == 1) {
                Cards cards = cardsService.getOne(new QueryWrapper<Cards>().eq("product_id", products.getId()).eq("sell_type", 1));
                if (!ObjectUtils.isEmpty(cards)) {
                    productsVo.setCardMember(cards.getNumber().longValue());
                    productsVo.setSellCardMember(cards.getSellNumber().longValue());
                } else {
                    productsVo.setCardMember(0L);
                    productsVo.setSellCardMember(0L);
                }
            }

            productsVo.setPrice(products.getPrice().toString());
            return productsVo;
        }).collect(Collectors.toList());

        return new PageResult<>(productsVoList, pageParam.getTotal());
    }

    /**
     * 查询全部商品
     */
    @RequiresPermissions("products:products:list")
    @OperLog(value = "商品管理", desc = "查询全部")
    @ResponseBody
    @RequestMapping("/list")
    public JsonResult list(HttpServletRequest request) {
        PageParam<Products> pageParam = new PageParam<>(request);
        return JsonResult.ok().setData(productsService.list(pageParam.getOrderWrapper()));
    }

    /**
     * 工具分类ID查询商品
     */
    @RequiresPermissions("products:products:list")
    @OperLog(value = "商品管理", desc = "查询全部")
    @ResponseBody
    @RequestMapping("/getProductList")
    public JsonResult getProductList(Integer id) {
        List<Products> productsList = productsService.list(new QueryWrapper<Products>().eq("classify_id", id));
        return JsonResult.ok("查询商品成功！").setData(productsList);
    }

    /**
     * 根据id查询商品
     */
    @RequiresPermissions("products:products:list")
    @OperLog(value = "商品管理", desc = "根据id查询")
    @ResponseBody
    @RequestMapping("/get")
    public JsonResult get(Integer id) {
        return JsonResult.ok().setData(productsService.getById(id));
    }

    /**
     * 添加商品
     */
    @RequiresPermissions("products:products:save")
    @OperLog(value = "商品管理", desc = "添加", param = false, result = true)
    @ResponseBody
    @RequestMapping("/save")
    public JsonResult save(Products products) {
        products.setUsername(getLoginUsername());
        if (products.getShipType() == null) {
            return JsonResult.error("发货模式不能为空！");
        }

        products.setCreatedAt(new Date());
        products.setUpdatedAt(new Date());
        products.setDeletedAt(new Date());
        products.setLink(StringUtil.getRandomString(16));
        if (productsService.save(products)) {
            return JsonResult.ok("添加商品成功");
        }
        return JsonResult.error("添加商品失败");
    }

    /**
     * 修改商品
     */
    @RequiresPermissions("products:products:update")
    @OperLog(value = "商品管理", desc = "修改", param = false, result = true)
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(Products products) {
        if (productsService.updateById(products)) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 删除商品
     */
    @RequiresPermissions("products:products:remove")
    @OperLog(value = "商品管理", desc = "删除", result = true)
    @ResponseBody
    @RequestMapping("/remove")
    public JsonResult remove(Integer id) {
        long count = cardsService.count(new QueryWrapper<Cards>().eq("product_id", id));
        if (count >= 1) {
            return JsonResult.error("该商品下存在卡密，不允许删除");
        }
        if (productsService.removeById(id)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 批量添加商品
     */
    @RequiresPermissions("products:products:save")
    @OperLog(value = "商品管理", desc = "批量添加", param = false, result = true)
    @ResponseBody
    @RequestMapping("/saveBatch")
    public JsonResult saveBatch(@RequestBody List<Products> list) {
        for (Products products : list) {
            products.setUsername(getLoginUsername());
        }
        if (productsService.saveBatch(list)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 批量修改商品
     */
    @RequiresPermissions("products:products:update")
    @OperLog(value = "商品管理", desc = "批量修改", result = true)
    @ResponseBody
    @RequestMapping("/updateBatch")
    public JsonResult updateBatch(@RequestBody BatchParam<Products> batchParam) {
        if (batchParam.update(productsService, "id")) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 批量删除商品
     */
    @RequiresPermissions("products:products:remove")
    @OperLog(value = "商品管理", desc = "批量删除", result = true)
    @ResponseBody
    @RequestMapping("/removeBatch")
    public JsonResult removeBatch(@RequestBody List<Integer> ids) {
        if (productsService.removeByIds(ids)) {
            return JsonResult.ok("删除商品成功");
        }
        return JsonResult.error("删除商品失败");
    }

    /**
     * 修改商品状态
     */
    @OperLog(value = "分类列表管理", desc = "修改状态", result = true)
    @RequiresPermissions("products:products:update")
    @ResponseBody
    @RequestMapping("/status/update")
    public JsonResult updateStates(Integer id, Integer status) {
        if (status == null || (status != 0 && status != 1)) {
            return JsonResult.error("状态值不正确");
        }
        Products products = new Products();
        products.setId(id);
        products.setStatus(status);
        if (productsService.updateById(products)) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

}
