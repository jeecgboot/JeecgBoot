package org.jeecg.modules.business.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.CaseFormat;
import freemarker.template.Template;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.mongoService.SkuMongoService;
import org.jeecg.modules.business.service.*;
import org.jeecg.modules.business.vo.*;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static org.jeecg.common.util.SqlInjectionUtil.specialFilterContentForDictSql;

/**
 * @Description: SKU表
 * @Author: jeecg-boot
 * @Date: 2021-06-28
 * @Version: V1.1
 */
@Api(tags = "SKU表")
@RestController
@RequestMapping("/sku")
@Slf4j
public class SkuController {
    @Autowired
    private IShopService shopService;
    @Autowired
    private ISkuService skuService;
    @Autowired
    private ISkuWeightService skuWeightService;
    @Autowired
    private ISkuPriceService skuPriceService;
    @Autowired
    private IShippingDiscountService shippingDiscountService;
    @Autowired
    private ISkuDeclaredValueService skuDeclaredValueService;
    @Autowired
    private ISkuListMabangService skuListMabangService;
    @Autowired
    private SkuMongoService skuMongoService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private FreeMarkerConfigurer freemarkerConfigurer;
    @Autowired
    Environment env;

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

    @GetMapping("/listWithFilters")
    public Result<?> listWithFilters(@RequestParam(name = "clientId", required = false) String clientId,
                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(name = "pageSize", defaultValue = "50") Integer pageSize,
                                  @RequestParam(name = "column", defaultValue = "erp_code") String column,
                                  @RequestParam(name = "order", defaultValue = "ASC") String order,
                                  @RequestParam(name = "erpCodes", required = false) String erpCodes,
                                  @RequestParam(name = "zhNames", required = false) String zhNames,
                                  @RequestParam(name = "enNames", required = false) String enNames,
                                     ServletRequest servletRequest) {
        String parsedColumn = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, column.replace("_dictText", ""));
        String parsedOrder = order.toUpperCase();
        if(!parsedOrder.equals("ASC") && !parsedOrder.equals("DESC")) {
            return Result.error("Error 400 Bad Request");
        }
        try {
            specialFilterContentForDictSql(parsedColumn);
        } catch (RuntimeException e) {
            return Result.error("Error 400 Bad Request");
        }
        List<SkuOrderPage> skuOrdersPage;
        int total;
        if(erpCodes != null || zhNames != null || enNames != null) {
            List<String> erpCodeList = erpCodes == null ? null : Arrays.asList(erpCodes.split(","));
            List<String> zhNameList = zhNames == null ? null : Arrays.asList(zhNames.split(","));
            List<String> enNameList = enNames == null ? null : Arrays.asList(enNames.split(","));
            if(clientId != null) {
                total = skuService.countAllClientSkusWithFilters(clientId, erpCodeList, zhNameList, enNameList);
                skuOrdersPage = skuService.fetchSkusByClientWithFilters(clientId, pageNo, pageSize, parsedColumn, parsedOrder, erpCodeList, zhNameList, enNameList);
            } else {
                total = skuService.countAllSkuWeightsWithFilters(erpCodeList, zhNameList, enNameList);
                skuOrdersPage = skuService.fetchSkuWeightsWithFilters(pageNo, pageSize, parsedColumn, parsedOrder, erpCodeList, zhNameList, enNameList);
            }
        }
        else {
            if(clientId != null) {
                total = skuService.countAllClientSkus(clientId);
                skuOrdersPage = skuService.fetchSkusByClient(clientId, pageNo, pageSize, parsedColumn, parsedOrder);
            } else {
                total = skuService.countAllSkus();
                skuOrdersPage = skuService.fetchSkuWeights(pageNo, pageSize, parsedColumn, parsedOrder);
            }
        }
        IPage<SkuOrderPage> page = new Page<>();
        page.setRecords(skuOrdersPage);
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        page.setTotal(total);
        return Result.OK(page);
    }

    @GetMapping("/listAllWithFilters")
    public Result<?> listAllWithFilters(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "50") Integer pageSize,
            @RequestParam(name = "column", defaultValue = "erp_code") String column,
            @RequestParam(name = "order", defaultValue = "ASC") String order,
            @RequestParam(name = "erpCodes", required = false) String erpCodes,
            @RequestParam(name = "zhNames", required = false) String zhNames,
            @RequestParam(name = "enNames", required = false) String enNames,
            ServletRequest servletRequest) {

        String parsedColumn = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, column.replace("_dictText", ""));
        String parsedOrder = order.toUpperCase();
        if (!parsedOrder.equals("ASC") && !parsedOrder.equals("DESC")) {
            return Result.error("Error 400 Bad Request");
        }

        try {
            specialFilterContentForDictSql(parsedColumn);
        } catch (RuntimeException e) {
            return Result.error("Error 400 Bad Request");
        }

        List<SkuOrderPage> skuOrdersPage;
        int total;

        List<String> erpCodeList = erpCodes == null ? null : Arrays.asList(erpCodes.split(","));
        List<String> zhNameList = zhNames == null ? null : Arrays.asList(zhNames.split(","));
        List<String> enNameList = enNames == null ? null : Arrays.asList(enNames.split(","));

        // Count and fetch ALL records (not just latest)
        total = skuService.countAllSkuWeightHistoryWithFilters(erpCodeList, zhNameList, enNameList);
        skuOrdersPage = skuService.fetchAllSkuWeightsWithFilters(pageNo, pageSize, parsedColumn, parsedOrder, erpCodeList, zhNameList, enNameList);

        IPage<SkuOrderPage> page = new Page<>();
        page.setRecords(skuOrdersPage);
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        page.setTotal(total);
        return Result.OK(page);
    }
    @GetMapping("/listAllSelectableSkuIds")
    public Result<?> listAllSelectableSkuIds(@RequestParam(name = "clientId") String clientId,
                                             @RequestParam(name = "erpCodes", required = false) String erpCodes,
                                             @RequestParam(name = "zhNames", required = false) String zhNames,
                                             @RequestParam(name = "enNames", required = false) String enNames
    ) {
        List<SkuOrderPage> selectableSkuIds;
        if(erpCodes != null || zhNames != null || enNames != null) {
            List<String> erpCodeList = erpCodes == null ? null : Arrays.asList(erpCodes.split(","));
            List<String> zhNameList = zhNames == null ? null : Arrays.asList(zhNames.split(","));
            List<String> enNameList = enNames == null ? null : Arrays.asList(enNames.split(","));
            selectableSkuIds = skuService.listSelectableSkuIdsWithFilters(clientId, erpCodeList, zhNameList, enNameList);
        } else {
            selectableSkuIds = skuService.listSelectableSkuIds(clientId);
        }
        return Result.OK(selectableSkuIds);
    }
    @GetMapping("/searchExistingSkuByKeywords")
    public Result<?> searchExistingSkuByKeywords(@RequestParam("keywords") String keywords) {
        String parsedKeywords = keywords.trim().replaceAll("[{}=$]", "");
        if(parsedKeywords.isEmpty()) {
            return Result.OK(new ArrayList<>());
        }
        return Result.OK(skuMongoService.textSearch(parsedKeywords));
    }

    @PostMapping("/createMabangSku")
    public Result<?> createMabangSku(@RequestBody List<SkuOrderPage> skuList) {
        log.info("Request to create {} new skus in Mabang", skuList.size());
        skuList.forEach(sku -> {
            if(sku.getShippingDiscount() == null) {
                sku.setShippingDiscount(BigDecimal.ZERO);
            } else {
                BigDecimal oldValue = sku.getShippingDiscount().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                sku.setShippingDiscount(BigDecimal.ONE.subtract(oldValue));
            }
            sku.setStatus(3);
        });
        return Result.OK(skuListMabangService.publishSkuToMabang(skuList));
    }
    @RequestMapping(value = "/exportNewMabangSkusXls")
    public ModelAndView exportXls(@RequestParam Map<String, String> params) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<NewSkuPage> skuList = parseSkuList(params);
        log.info("Exporting new skus to excel ...");
        skuList.forEach(sku -> {
            if(sku.getShippingDiscount() == null) {
                sku.setShippingDiscount(BigDecimal.ONE);
            } else {
                BigDecimal oldValue = sku.getShippingDiscount().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                sku.setShippingDiscount(BigDecimal.ONE.subtract(oldValue));
            }
            sku.setStatus(3);
        });

        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "新增库存SKU表");
        mv.addObject(NormalExcelConstants.CLASS, NewSkuPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("新增库存SKU表数据", "导出人:" + sysUser.getRealname(), "新增库存SKU表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, skuList);
        return mv;
    }
    private List<NewSkuPage> parseSkuList(Map<String, String> params) {
        // Group parameters by "selections[index]"
        Map<Integer, Map<String, String>> grouped = new HashMap<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (key.startsWith("selections[")) {
                int indexStart = key.indexOf("[") + 1;
                int indexEnd = key.indexOf("]");
                int index = Integer.parseInt(key.substring(indexStart, indexEnd));

                String field = key.substring(key.indexOf("][") + 2, key.length() - 1);
                grouped.computeIfAbsent(index, k -> new HashMap<>()).put(field, value);
            }
        }

        // Convert maps to SkuOrderPage objects
        return grouped.values().stream()
                .map(this::mapToSkuOrderPage)
                .collect(Collectors.toList());
    }

    private NewSkuPage mapToSkuOrderPage(Map<String, String> map) {
        NewSkuPage sku = new NewSkuPage();
        sku.setMabangSku(map.get("id"));
        sku.setZhName(map.get("zhName"));
        sku.setEnName(map.get("enName"));
        sku.setDeclareEname(map.get("declareEname"));
        sku.setDeclareName(map.get("declareName"));
        sku.setErpCode(map.get("erpCode"));
        sku.setWeight(map.get("weight") != null ? Integer.parseInt(map.get("weight")) : null);
        sku.setStatus(map.get("status") != null ? Integer.parseInt(map.get("status")) : 3);
        sku.setSensitiveAttribute(map.get("sensitiveAttribute"));
        sku.setIsGift(map.get("isGift") != null ? Integer.parseInt(map.get("isGift")) : 2);
        sku.setSkuPrice(map.get("skuPrice") != null ? new BigDecimal(map.get("skuPrice")) : BigDecimal.ZERO);
        sku.setDeclaredValue(map.get("declaredValue") != null ? new BigDecimal(map.get("declaredValue")) : null);
        sku.setShippingDiscount(map.get("shippingDiscount") != null ? new BigDecimal(map.get("shippingDiscount")) : BigDecimal.ZERO);
        sku.setServiceFee(map.get("serviceFee") != null ? new BigDecimal(map.get("serviceFee")) : BigDecimal.ZERO);
        sku.setWarehouse(map.get("warehouse"));
        sku.setSupplier(map.get("supplier"));
        sku.setSupplierLink(map.get("supplierLink"));
        sku.setImageSource(map.get("imageSource"));
        sku.setLabelData(map.get("labelData"));
        return sku;
    }
    @PostMapping("/syncSkus")
    public Result<?> syncSkus(@RequestBody List<String> erpCodes) {
        Map<Sku, String> newSkusNeedTreatmentMap;

        try {
            newSkusNeedTreatmentMap = skuListMabangService.skuSyncUpsert(erpCodes);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        skuListMabangService.updateSkuId();

        // here we send system notification with the list of new skus that need extra treatment
        if (newSkusNeedTreatmentMap.isEmpty()) {
            return Result.ok();
        }
        Properties prop = emailService.getMailSender();
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(env.getProperty("spring.mail.username"), env.getProperty("spring.mail.password"));
            }
        });

        String subject = "Association of Sku to Client failed while creating new Sku";
        String destEmail = env.getProperty("company.jessy.email");
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("operation", "créés");
        templateModel.put("skusMap", newSkusNeedTreatmentMap);
        try {
            freemarkerConfigurer = emailService.freemarkerClassLoaderConfig();
            Template template = freemarkerConfigurer.getConfiguration().getTemplate("admin/unknownClientForSku.ftl");
            String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateModel);
            emailService.sendSimpleMessage(destEmail, subject, htmlBody, session);
            log.info("Mail sent successfully");
        } catch (Exception e) {
            log.error("Error sending mail: {}", e.getMessage());
        }
        return Result.OK();
    }

    @PostMapping("/syncSkuQty")
    public Result<?> syncSkuQty(@RequestBody List<String> erpCodes) {
        log.info("Syncing sku stock for SKUs : {}", erpCodes);
        skuListMabangService.mabangSkuStockUpdate(erpCodes);
        return Result.OK();
    }

    @GetMapping("/unpairedSkus")
    public Result<?> unpairedSkus(@RequestParam(name = "shop") String shopCode,
                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(name = "pageSize", defaultValue = "50") Integer pageSize,
                                  @RequestParam(name = "skus[]", required = false) List<String> skuNames
    ) {
        if(skuNames == null)
            skuNames = new ArrayList<>();
        String shopId = shopService.getIdByCode(shopCode);
        if (shopId == null) return Result.error(404, "Shop not found");
        List<SkuOrderPage> unpairedSkus = skuListMabangService.unpairedSkus(shopId, skuNames);

        return Result.OK(unpairedSkus);
    }

    @GetMapping(value = "/latestSkuCounter")
    public Result<?> latestSkuCounter(@RequestParam(name= "userCode") String userCode,
                                      @RequestParam(name= "clientCode") String clientCode,
                                      @RequestParam(name= "date") String date) {
        return Result.OK(skuService.latestSkuCounter(userCode, clientCode, date));
    }

    @GetMapping(value = "/compare")
    public Result<?> compareClientSkuWithMabang(@RequestParam(name="clientId") String clientId,
                                                @RequestParam(name="erpStatuses[]") List<String> erpStatuses) {
        Map<String, Sku> clientSkus = skuService.listInUninvoicedOrders(clientId, erpStatuses);
        if(clientSkus.isEmpty()) {
            log.info("No skus to compare");
            return Result.OK();
        }
        skuListMabangService.compareClientSkusWithMabang(clientSkus);

        return Result.OK();
    }
}
