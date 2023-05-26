package org.jeecg.modules.business.controller.client.Inventory;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.business.entity.ShippingDiscount;
import org.jeecg.modules.business.entity.Sku;
import org.jeecg.modules.business.entity.SkuPrice;
import org.jeecg.modules.business.service.IShippingDiscountService;
import org.jeecg.modules.business.service.ISkuPriceService;
import org.jeecg.modules.business.service.ISkuService;
import org.jeecg.modules.business.vo.SkuPage;
import org.jeecg.modules.business.vo.inventory.InventoryRecord;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: SKU表
 * @Author: jeecg-boot
 * @Date: 2021-05-08
 * @Version: V1.0
 */
@Api(tags = "SKU表")
@RestController
@RequestMapping("/business/inventory/client")
@Slf4j
public class InventoryController {
    @Autowired
    private ISkuService skuService;
    @Autowired
    private ISkuPriceService skuPriceService;
    @Autowired
    private IShippingDiscountService shippingDiscountService;

    /**
     * 分页列表查询
     *
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "SKU表-分页列表查询")
    @ApiOperation(value = "SKU表-分页列表查询", notes = "SKU表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<Page<InventoryRecord>> queryPageList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                       HttpServletRequest req) {
        Page<InventoryRecord> page = new Page<>(pageNo, pageSize);
        skuService.fillPageForCurrentClient(page);
        return Result.OK(page);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "SKU表-通过id查询")
    @ApiOperation(value = "SKU表-通过id查询", notes = "SKU表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Sku sku = skuService.getById(id);
        if (sku == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(sku);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "SKU价格表-通过主表ID查询")
    @ApiOperation(value = "SKU价格表-通过主表ID查询", notes = "SKU价格表-通过主表ID查询")
    @GetMapping(value = "/querySkuPriceByMainId")
    public Result<?> querySkuPriceListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<SkuPrice> skuPriceList = skuPriceService.selectByMainId(id);
        IPage<SkuPrice> page = new Page<>();
        page.setRecords(skuPriceList);
        page.setTotal(skuPriceList.size());
        return Result.OK(page);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "SKU物流折扣-通过主表ID查询")
    @ApiOperation(value = "SKU物流折扣-通过主表ID查询", notes = "SKU物流折扣-通过主表ID查询")
    @GetMapping(value = "/queryShippingDiscountByMainId")
    public Result<?> queryShippingDiscountListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<ShippingDiscount> shippingDiscountList = shippingDiscountService.selectByMainId(id);
        IPage<ShippingDiscount> page = new Page<>();
        page.setRecords(shippingDiscountList);
        page.setTotal(shippingDiscountList.size());
        return Result.OK(page);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param sku
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Sku sku) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<Sku> queryWrapper = QueryGenerator.initQueryWrapper(sku, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<Sku> queryList = skuService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<Sku> skuList = new ArrayList<Sku>();
        if (oConvertUtils.isEmpty(selections)) {
            skuList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            skuList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<SkuPage> pageList = new ArrayList<SkuPage>();
        for (Sku main : skuList) {
            SkuPage vo = new SkuPage();
            BeanUtils.copyProperties(main, vo);
            List<SkuPrice> skuPriceList = skuPriceService.selectByMainId(main.getId());
            vo.setSkuPriceList(skuPriceList);
            List<ShippingDiscount> shippingDiscountList = shippingDiscountService.selectByMainId(main.getId());
            vo.setShippingDiscountList(shippingDiscountList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "SKU表列表");
        mv.addObject(NormalExcelConstants.CLASS, SkuPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("SKU表数据", "导出人:" + sysUser.getRealname(), "SKU表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }
}
