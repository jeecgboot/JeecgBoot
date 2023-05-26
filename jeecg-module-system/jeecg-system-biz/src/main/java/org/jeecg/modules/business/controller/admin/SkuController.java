package org.jeecg.modules.business.controller.admin;

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
import org.jeecg.modules.business.entity.SkuDeclaredValue;
import org.jeecg.modules.business.entity.SkuPrice;
import org.jeecg.modules.business.service.IShippingDiscountService;
import org.jeecg.modules.business.service.ISkuDeclaredValueService;
import org.jeecg.modules.business.service.ISkuPriceService;
import org.jeecg.modules.business.service.ISkuService;
import org.jeecg.modules.business.vo.SkuName;
import org.jeecg.modules.business.vo.SkuPage;
import org.jeecg.modules.business.vo.SkuUpdate;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: SKU表
 * @Author: jeecg-boot
 * @Date: 2021-06-28
 * @Version: V1.1
 */
@Api(tags = "SKU表")
@RestController
@RequestMapping("/business/sku")
@Slf4j
public class SkuController {
    @Autowired
    private ISkuService skuService;
    @Autowired
    private ISkuPriceService skuPriceService;
    @Autowired
    private IShippingDiscountService shippingDiscountService;
    @Autowired
    private ISkuDeclaredValueService skuDeclaredValueService;

    /**
     * 分页列表查询
     *
     * @param sku
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "SKU表-分页列表查询")
    @ApiOperation(value = "SKU表-分页列表查询", notes = "SKU表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Sku sku,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<Sku> queryWrapper = QueryGenerator.initQueryWrapper(sku, req.getParameterMap());
        Page<Sku> page = new Page<Sku>(pageNo, pageSize);
        IPage<Sku> pageList = skuService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param skuPage
     * @return
     */
    @AutoLog(value = "SKU表-添加")
    @ApiOperation(value = "SKU表-添加", notes = "SKU表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SkuPage skuPage) {
        Sku sku = new Sku();
        BeanUtils.copyProperties(skuPage, sku);
        skuService.saveMain(sku, skuPage.getSkuPriceList(), skuPage.getShippingDiscountList(), skuPage.getSkuDeclaredValueList());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param skuPage
     * @return
     */
    @AutoLog(value = "SKU表-编辑")
    @ApiOperation(value = "SKU表-编辑", notes = "SKU表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SkuPage skuPage) {
        Sku sku = new Sku();
        BeanUtils.copyProperties(skuPage, sku);
        Sku skuEntity = skuService.getById(sku.getId());
        if (skuEntity == null) {
            return Result.error("未找到对应数据");
        }
        skuService.updateMain(sku, skuPage.getSkuPriceList(), skuPage.getShippingDiscountList(), skuPage.getSkuDeclaredValueList());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "SKU表-通过id删除")
    @ApiOperation(value = "SKU表-通过id删除", notes = "SKU表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        skuService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "SKU表-批量删除")
    @ApiOperation(value = "SKU表-批量删除", notes = "SKU表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.skuService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
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
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "SKU申报价格-通过主表ID查询")
    @ApiOperation(value = "SKU申报价格-通过主表ID查询", notes = "SKU申报价格-通过主表ID查询")
    @GetMapping(value = "/querySkuDeclaredValueByMainId")
    public Result<?> querySkuDeclaredValueListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<SkuDeclaredValue> skuDeclaredValueList = skuDeclaredValueService.selectByMainId(id);
        IPage<SkuDeclaredValue> page = new Page<>();
        page.setRecords(skuDeclaredValueList);
        page.setTotal(skuDeclaredValueList.size());
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
            List<SkuDeclaredValue> skuDeclaredValueList = skuDeclaredValueService.selectByMainId(main.getId());
            vo.setSkuDeclaredValueList(skuDeclaredValueList);
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

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<SkuPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SkuPage.class, params);
                for (SkuPage page : list) {
                    Sku po = new Sku();
                    BeanUtils.copyProperties(page, po);
                    skuService.saveMain(po, page.getSkuPriceList(), page.getShippingDiscountList(), page.getSkuDeclaredValueList());
                }
                return Result.OK("文件导入成功！数据行数:" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.OK("文件导入失败！");
    }

    /**
     * 通过excel批量更新库存（马帮导出）
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/updateStock", method = RequestMethod.POST)
    public Result<?> updateStock(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<SkuUpdate> list = ExcelImportUtil.importExcel(file.getInputStream(), SkuUpdate.class, params);
                for (SkuUpdate sku : list) {
                    sku.setUpdateBy(sysUser.getUsername());
                    sku.setUpdateTime(new Date());
                }
                skuService.batchUpdateSku(list);
                return Result.OK("文件导入成功！数据行数:" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.OK("文件导入失败！");
    }

    @GetMapping("/all")
    public Result<List<SkuName>> allSku() {
        return Result.OK(
                skuService.all().stream()
                        .sorted(Comparator.comparing(SkuName::getErpCode))
                        .collect(Collectors.toList())
        );
    }

}
