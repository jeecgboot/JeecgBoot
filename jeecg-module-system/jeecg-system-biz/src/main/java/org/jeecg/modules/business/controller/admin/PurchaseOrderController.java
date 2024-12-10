package org.jeecg.modules.business.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
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
import org.jeecg.modules.business.controller.UserException;
import org.jeecg.modules.business.domain.job.ThrottlingExecutorService;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.service.*;
import org.jeecg.modules.business.vo.*;
import org.jeecg.modules.business.vo.clientPlatformOrder.PurchaseConfirmation;
import org.jeecg.modules.business.vo.clientPlatformOrder.section.ClientInfo;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @Description: API Handler related admin purchase order
 * @Author: Wenke
 * @Date: 2021-04-24
 * @Version: V1.0
 */
@Api(tags = "Administrator side purchase order API")
@RestController
@RequestMapping("/purchaseOrder")
@Slf4j
public class PurchaseOrderController {
    @Autowired
    private IBalanceService balanceService;
    @Autowired
    private IClientCategoryService clientCategoryService;
    @Autowired
    private IPurchaseOrderService purchaseOrderService;
    @Autowired
    private IPurchaseOrderSkuService purchaseOrderSkuService;
    @Autowired
    private ISkuPromotionHistoryService skuPromotionHistoryService;
    @Autowired
    private ISkuService skuService;
    @Autowired
    private IImportedInventoryService importedInventoryService;
    @Autowired
    private IClientService clientService;
    @Autowired
    private IPlatformOrderService platformOrderService;
    @Autowired
    private IShippingInvoiceService shippingInvoiceService;
    @Autowired private IProviderMabangService providerMabangService;

    private static final Integer DEFAULT_NUMBER_OF_THREADS = 2;
    private static final Integer MABANG_API_RATE_LIMIT_PER_MINUTE = 10;
    /**
     * Page query for purchase order
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @AutoLog(value = "商品采购订单-分页列表查询")
    @ApiOperation(value = "商品采购订单-分页列表查询", notes = "商品采购订单-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   @RequestParam(name = "clientId", required = false) String clientId) {
        Page<PurchaseOrderPage> page = new Page<>(pageNo, pageSize);
        purchaseOrderService.setPageForList(page, clientId);
        return Result.OK(page);
    }
    /**
     *   添加
     *
     * @param purchaseOrder
     * @return
     */
    @AutoLog(value = "商品采购订单-添加")
    @ApiOperation(value="商品采购订单-添加", notes="商品采购订单-添加")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody PurchaseOrder purchaseOrder) {
        purchaseOrderService.save(purchaseOrder);
        return Result.OK("sys.api.entryAddSuccess");
    }
    /**
     *   添加
     *
     * @param purchaseOrderPage
     * @return
     */
    @Transactional
    @AutoLog(value = "商品采购订单-添加")
    @ApiOperation(value="商品采购订单-添加", notes="商品采购订单-添加")
    @PostMapping(value = "/addPurchaseAndOrder")
    public Result<?> addPurchaseAndOrder( @RequestBody PurchaseOrderPage purchaseOrderPage) {
        Client client = clientService.getById(purchaseOrderPage.getClientId());
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        BeanUtils.copyProperties(purchaseOrderPage, purchaseOrder);
        purchaseOrder.setPaymentDocumentString(new String(purchaseOrderPage.getPaymentDocument()));
        purchaseOrder.setInventoryDocumentString(new String(purchaseOrderPage.getInventoryDocument()));
        String purchaseID = UUID.randomUUID().toString();
        purchaseOrder.setId(purchaseID);
        purchaseOrderService.save(purchaseOrder);

        String clientCategory = clientCategoryService.getClientCategoryByClientId(client.getId());
        if(clientCategory.equals(ClientCategory.CategoryName.CONFIRMED.getName()) || clientCategory.equals(ClientCategory.CategoryName.VIP.getName())) {
            balanceService.updateBalance(purchaseOrder.getClientId(), purchaseOrder.getInvoiceNumber(), "purchase");
        }

        // No need to attribute purchase order to platform orders, probably just buying stock
        if(purchaseOrderPage.getPlatformOrderId() == null) {
            return Result.OK("sys.api.entryAddSuccess");
        }
        List<String> platformOrderIds = Arrays.stream(purchaseOrderPage.getPlatformOrderId().split(","))
                .map(String::trim)
                .collect(Collectors.toList());
        log.info("Attributing purchase order to platform orders: {}", platformOrderIds);
        List<PlatformOrder> platformOrders = platformOrderService.selectByPlatformOrderIds(platformOrderIds);
        Map<String, List<String>> platformOrderIdUpdateMap = new HashMap<>();
        if(!platformOrders.isEmpty()) {
            log.info("Platform orders found for attribution: {}", platformOrders.stream().map(PlatformOrder::getPlatformOrderId).collect(Collectors.toList()));
            for(PlatformOrder po : platformOrders) {
                po.setPurchaseInvoiceNumber(purchaseOrder.getInvoiceNumber());
                platformOrderIds.remove(po.getPlatformOrderId());
                if(platformOrderIdUpdateMap.get("success") != null)
                    platformOrderIdUpdateMap.get("success").add(po.getPlatformOrderId());
                else
                    platformOrderIdUpdateMap.put("success", new ArrayList<>(Collections.singletonList(po.getPlatformOrderId())));
            }
            platformOrderService.updateBatchById(platformOrders);
        }
        if(!platformOrderIds.isEmpty()) {
            log.error("Platform orders not found: {}", platformOrderIds);
            platformOrderIdUpdateMap.put("fail", platformOrderIds);
        }
        return Result.OK("sys.api.entryAddSuccess", platformOrderIdUpdateMap);
    }
//    /**
//     * 编辑
//     *
//     * @param purchaseOrderPage
//     * @return
//     */
//    @AutoLog(value = "商品采购订单-编辑")
//    @ApiOperation(value = "商品采购订单-编辑", notes = "商品采购订单-编辑")
//    @PostMapping(value = "/edit")
//    public Result<?> edit(@RequestBody PurchaseOrderPage purchaseOrderPage) {
//        PurchaseOrder purchaseOrder = new PurchaseOrder();
//        BeanUtils.copyProperties(purchaseOrderPage, purchaseOrder);
//        PurchaseOrder purchaseOrderEntity = purchaseOrderService.getById(purchaseOrder.getId());
//        if (purchaseOrderEntity == null) {
//            return Result.error("未找到对应数据");
//        }
//        purchaseOrderService.updateMain(purchaseOrder, purchaseOrderPage.getPurchaseOrderSkuList(), null);
//        return Result.OK("编辑成功!");
//    }
    /**
     *  编辑
     *
     * @param purchaseOrder
     * @return
     */
    @AutoLog(value = "purchase_order-编辑")
    @ApiOperation(value="purchase_order-编辑", notes="purchase_order-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody PurchaseOrder purchaseOrder) {
        purchaseOrderService.updateById(purchaseOrder);
        return Result.OK("sys.api.entryEditSuccess");
    }
    /**
     *  编辑
     *
     * @param purchaseOrderPage
     * @return
     */
    @Transactional
    @AutoLog(value = "purchase_order-编辑")
    @ApiOperation(value="purchase_order-编辑", notes="purchase_order-编辑")
    @RequestMapping(value = "/editPurchaseAndOrder", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<?> editPurchaseAndOrder(@RequestBody PurchaseOrderPage purchaseOrderPage) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        BeanUtils.copyProperties(purchaseOrderPage, purchaseOrder);
        purchaseOrder.setPaymentDocumentString(new String(purchaseOrderPage.getPaymentDocument()));
        purchaseOrder.setInventoryDocumentString(new String(purchaseOrderPage.getInventoryDocument()));
        if(purchaseOrderPage.getPlatformOrderId() == null) {
            purchaseOrderService.updateById(purchaseOrder);
            return Result.OK("sys.api.entryEditSuccess");
        }
        List<String> platformOrderIds = new ArrayList<>(Arrays.asList(purchaseOrderPage.getPlatformOrderId().split(",")));
        log.info("Editing purchase order and attributing it to orders : {}", platformOrderIds);
        log.info("Removing previous attribution to orders");
        platformOrderService.removePurchaseInvoiceNumber(purchaseOrder.getInvoiceNumber(), purchaseOrder.getClientId());
        List<PlatformOrder> platformOrders = platformOrderService.selectByPlatformOrderIds(platformOrderIds);
        log.info("Platform orders found for attribution : {}", platformOrders.stream().map(PlatformOrder::getPlatformOrderId).collect(Collectors.toList()));
        Map<String, List<String>> platformOrderIdUpdateMap = new HashMap<>();
        if(!platformOrders.isEmpty()) {
            for(PlatformOrder po : platformOrders) {
                po.setPurchaseInvoiceNumber(purchaseOrder.getInvoiceNumber());
                platformOrderIds.remove(po.getPlatformOrderId());
                if(platformOrderIdUpdateMap.get("success") != null)
                    platformOrderIdUpdateMap.get("success").add(po.getPlatformOrderId());
                else
                    platformOrderIdUpdateMap.put("success", new ArrayList<>(Collections.singletonList(po.getPlatformOrderId())));
            }
            platformOrderService.updateBatchById(platformOrders);
        }
        if(!platformOrderIds.isEmpty()) {
            log.error("Platform orders not found: {}", platformOrderIds);
            platformOrderIdUpdateMap.put("fail", platformOrderIds);
        }
        purchaseOrderService.updateById(purchaseOrder);
        return Result.OK("sys.api.entryEditSuccess", platformOrderIdUpdateMap);
    }

    /**
     * 通过id删除
     * not used, use cancelInvoice instead
     * @param id
     * @return
     */
    @AutoLog(value = "商品采购订单-通过id删除")
    @ApiOperation(value = "商品采购订单-通过id删除", notes = "商品采购订单-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        PurchaseOrder po = purchaseOrderService.getById(id);
        if(po.getInventoryDocumentString() != null && !po.getInventoryDocumentString().isEmpty())
            shippingInvoiceService.deleteAttachmentFile(po.getInventoryDocumentString());
        if(po.getPaymentDocumentString() != null && !po.getPaymentDocumentString().isEmpty())
            shippingInvoiceService.deleteAttachmentFile(po.getPaymentDocumentString());
        purchaseOrderService.delMain(id);
        return Result.OK("sys.api.entryDeleteSuccess");
    }

    /**
     * 批量删除
     * not used, use cancelBatchInvoice instead
     * @param ids
     * @return
     */
    @AutoLog(value = "商品采购订单-批量删除")
    @ApiOperation(value = "商品采购订单-批量删除", notes = "商品采购订单-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        List<PurchaseOrder> purchaseOrders = purchaseOrderService.listByIds(Arrays.asList(ids.split(",")));
        for(PurchaseOrder po : purchaseOrders) {
            if(po.getInventoryDocumentString() != null && !po.getInventoryDocumentString().isEmpty())
                shippingInvoiceService.deleteAttachmentFile(po.getInventoryDocumentString());
            if(po.getPaymentDocumentString() != null && !po.getPaymentDocumentString().isEmpty())
                shippingInvoiceService.deleteAttachmentFile(po.getPaymentDocumentString());
        }
        this.purchaseOrderService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("sys.api.entryBatchDeleteSuccess");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "商品采购订单-通过id查询")
    @ApiOperation(value = "商品采购订单-通过id查询", notes = "商品采购订单-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        PurchaseOrder purchaseOrder = purchaseOrderService.getById(id);
        if (purchaseOrder == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(purchaseOrder);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "商品采购订单SKU-通过主表ID查询")
    @ApiOperation(value = "商品采购订单SKU-通过主表ID查询", notes = "商品采购订单SKU-通过主表ID查询")
    @GetMapping(value = "/queryPurchaseOrderSkuByMainId")
    public Result<?> queryPurchaseOrderSkuListByMainId(@RequestParam(name = "id", required = true) String id) {
        log.info("query content by id: {}", id);
        List<PurchaseOrderSku> purchaseOrderSkuList = purchaseOrderSkuService.selectByMainId(id);
        IPage<PurchaseOrderSku> page = new Page<>();
        page.setRecords(purchaseOrderSkuList);
        page.setTotal(purchaseOrderSkuList.size());
        return Result.OK(page);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "SKU采购折扣历史-通过主表ID查询")
    @ApiOperation(value = "SKU采购折扣历史-通过主表ID查询", notes = "SKU采购折扣历史-通过主表ID查询")
    @GetMapping(value = "/querySkuPromotionHistoryByMainId")
    public Result<?> querySkuPromotionHistoryListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<PromotionCouple> skuPromotionHistoryList = skuPromotionHistoryService.selectByMainId(id);
        IPage<PromotionCouple> page = new Page<>();
        page.setRecords(skuPromotionHistoryList);
        page.setTotal(skuPromotionHistoryList.size());
        return Result.OK(page);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param purchaseOrder
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PurchaseOrder purchaseOrder) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<PurchaseOrder> queryWrapper = QueryGenerator.initQueryWrapper(purchaseOrder, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<PurchaseOrder> queryList = purchaseOrderService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<PurchaseOrder> purchaseOrderList;
        if (oConvertUtils.isEmpty(selections)) {
            purchaseOrderList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            purchaseOrderList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<PurchaseOrderPage> pageList = new ArrayList<>();
        for (PurchaseOrder main : purchaseOrderList) {
            PurchaseOrderPage vo = new PurchaseOrderPage();
            BeanUtils.copyProperties(main, vo);
            List<PurchaseOrderSku> purchaseOrderSkuList = purchaseOrderSkuService.selectByMainId(main.getId());
            vo.setPurchaseOrderSkuList(purchaseOrderSkuList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "商品采购订单列表");
        mv.addObject(NormalExcelConstants.CLASS, PurchaseOrderPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("商品采购订单数据", "导出人:" + sysUser.getRealname(), "商品采购订单"));
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
                List<PurchaseOrderPage> list = ExcelImportUtil.importExcel(file.getInputStream(), PurchaseOrderPage.class, params);
                for (PurchaseOrderPage page : list) {
                    PurchaseOrder po = new PurchaseOrder();
                    BeanUtils.copyProperties(page, po);
                    purchaseOrderService.saveMain(po, page.getPurchaseOrderSkuList(), null);
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
     * Download payment document of the purchase by document's name,
     * The file will be write back in binary format.
     *
     * @param filename document's name
     */
    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public void downloadFile(@RequestParam String filename, HttpServletResponse response) throws IOException {
        byte[] out = purchaseOrderService.downloadPaymentDocumentOfPurchase(filename);
        response.setContentType("application/octet-stream;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.getOutputStream().write(out);
    }


    /**
     * Upload a payment file for a certain purchase order.
     * The parameter of the POST request should contain <b>purchase ID and file</b> entry.
     *
     * @param request the post request
     * @return error or success message
     * @throws IOException IO error when saving the file.
     */
    @AutoLog(value = "Upload payment document")
    @ApiOperation(value = "Upload payment document", notes = "Upload payment document")
    @PostMapping(value = "/uploadPaymentFile", consumes = {"multipart/form-data"})
    public Result<?> uploadPaymentFile(HttpServletRequest request) throws IOException {
        String purchaseID = request.getParameter("purchaseID");
        if (purchaseID == null) {
            return Result.error("Missing value: purchaseID");
        }
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");// 获取上传文件对象
        if (file == null) {
            return Result.error("Missing file.");
        }
        purchaseOrderService.savePaymentDocumentForPurchase(purchaseID, file);
        return Result.OK("Payment file upload success");
    }

    @PostMapping(value = "/confirmPayment")
    public Result<?> confirmPayment(@RequestBody Map<String, String> request) {
        String purchaseID = request.get("purchaseID");
        log.info("Request to confirm payment for purchase : {}", purchaseID);
        purchaseOrderService.confirmPayment(purchaseID);
        return Result.OK();
    }

    @PostMapping(value = "/confirmPurchase")
    public Result<?> confirmPurchase(@RequestBody Map<String, String> request) {
        String purchaseID = request.get("purchaseID");
        log.info("Request to confirm purchase : {}", purchaseID);
        purchaseOrderService.confirmPurchase(purchaseID);
        return Result.OK();
    }


    /**
     * @param purchaseID purchaseID
     */
    @RequestMapping(value = "/invoiceMeta", method = RequestMethod.GET)
    public InvoiceMetaData getInvoiceMetaData(@RequestParam String purchaseID, HttpServletResponse response) throws IOException, URISyntaxException, UserException {
        return purchaseOrderService.makeInvoice(purchaseID);
    }

    /**
     * @param invoiceCode invoiceCode
     */
    @RequestMapping(value = "/downloadInvoice", method = RequestMethod.GET)
    public byte[] downloadInvoiceFile(@RequestParam String invoiceCode) throws IOException, URISyntaxException {
        return purchaseOrderService.getInvoiceByte(invoiceCode);
    }

    /**
     * 通过excel导入采购清单
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/admin/importInventory", method = RequestMethod.POST)
    public Result<?> importInventory(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setNeedSave(true);
            String clientId = request.getParameter("clientId");
            if (clientId == null) {
                return Result.error("Missing value: clientId");
            }
            try {
                List<InventoryImport> list = ExcelImportUtil.importExcel(file.getInputStream(), InventoryImport.class, params);
                Map<String, Integer> skuQuantityMap = list.stream()
                        .collect(Collectors.toMap(InventoryImport::getErpCode, InventoryImport::getQuantity));
                Set<String> erpCodes = skuQuantityMap.keySet();
                Map<String, String> erpCodeToIdMap = skuService.selectByErpCode(erpCodes).stream()
                        .collect(Collectors.toMap(Sku::getErpCode, Sku::getId));
                List<SkuQuantity> skuQuantities = skuQuantityMap.entrySet()
                        .stream()
                        .map(entry -> new SkuQuantity(erpCodeToIdMap.get(entry.getKey()), entry.getValue()))
                        .collect(Collectors.toList());
                List<ImportedInventory> importedInventoryList = new ArrayList<>();
                for (SkuQuantity skuQuantity : skuQuantities) {
                    ImportedInventory importedInventory = new ImportedInventory();
                    importedInventory.setId(IdWorker.getIdStr(importedInventory));
                    importedInventory.setCreateBy(sysUser.getUsername());
                    importedInventory.setCreateTime(new Date());
                    importedInventory.setClientId(clientId);
                    importedInventory.setSkuId(skuQuantity.getID());
                    importedInventory.setQuantity(skuQuantity.getQuantity());
                    importedInventoryList.add(importedInventory);
                }
                importedInventoryService.saveBatch(importedInventoryList);
                return Result.OK("采购清单导入成功！数据行数:" + list.size());
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
     * 加载当前客户采购清单
     *
     * @param clientId
     * @return
     */
    @AutoLog(value = "商品采购清单-通过客户id查询")
    @RequestMapping(value = "/admin/loadInventory")
    public Result<?> loadInventory(@RequestParam(name = "id") String clientId) throws UserException {
        Client client = clientService.getById(clientId);
        ClientInfo clientInfo = new ClientInfo(client);
        List<ImportedInventory> importedInventories = importedInventoryService.selectByClientId(clientId);
        Map<String, Integer> skuQuantitiesMap = importedInventories.stream()
                .collect(Collectors.toMap(ImportedInventory::getSkuId, ImportedInventory::getQuantity));
        List<SkuQuantity> skuQuantities = new ArrayList<>();
        skuQuantitiesMap.forEach((s, integer) -> skuQuantities.add(new SkuQuantity(s, integer)));
        PurchaseConfirmation d = platformOrderService.confirmPurchaseBySkuQuantity(clientInfo, skuQuantities);
        return Result.OK(d);
    }

    @GetMapping(value = "/createMabangPurchaseOrder")
    public Result<?> createMabangPurchaseOrder(@RequestParam("invoiceNumbers") List<String> invoiceNumbers) {
        log.info("Creating purchase order to Mabang for invoices : {} ", invoiceNumbers);
        ExecutorService throttlingExecutorService = ThrottlingExecutorService.createExecutorService(DEFAULT_NUMBER_OF_THREADS,
                MABANG_API_RATE_LIMIT_PER_MINUTE, TimeUnit.MINUTES);
        // providersHistory lists the providers that have already been processed, if the current provider is in the list and has been processed within the last 10 seconds, the thread will sleep for 10 seconds
        AtomicReference<Map<String, LocalDateTime>> providersHistory = new AtomicReference<>(new HashMap<>());
        List<CompletableFuture<String>> future = invoiceNumbers.stream()
                .map(invoiceNumber -> CompletableFuture.supplyAsync(() -> {
                    log.info("Invoice number : {}", invoiceNumber);
                    List<SkuQuantity> skuQuantities = purchaseOrderService.getSkuQuantityByInvoiceNumber(invoiceNumber);
                    if(skuQuantities.isEmpty()) {
                        return null;
                    }
                    Map<String, Integer> skuQuantityMap = skuQuantities.stream()
                            .collect(Collectors.toMap(SkuQuantity::getErpCode, SkuQuantity::getQuantity));
                    skuQuantityMap.forEach((s, integer) -> log.info("SKU: {} Quantity: {}", s, integer));
                    Map<String, Integer> skuQtyNotEmptyMap = skuQuantityMap.entrySet().stream()
                            .filter(entry -> entry.getValue() > 0)
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                    InvoiceMetaData metaData = purchaseOrderService.getMetaDataFromInvoiceNumbers(invoiceNumber);
                    List<String> errors = providerMabangService.addPurchaseOrderToMabang(skuQtyNotEmptyMap, metaData, providersHistory);
                    return errors.isEmpty() ? invoiceNumber : errors.toString();
                },throttlingExecutorService))
                .collect(Collectors.toList());

        List<String> results = future.stream().map(CompletableFuture::join).collect(Collectors.toList());
        long nbSuccesses = results.stream().filter(PurchaseOrderController::isInvoiceNumber).count();
        log.info("{}/{} purchase order requests have succeeded.", nbSuccesses, invoiceNumbers.size());

        Map<String, List<String>> data = new HashMap<>();

        List<String> failedInvoices = new ArrayList<>();
        List<String> successInvoices = new ArrayList<>();

        results.forEach(result -> {
            if(isInvoiceNumber(result)) {
                successInvoices.add(result);
            } else {
                failedInvoices.add(result);
            }
        });

        data.put("fail", failedInvoices);
        data.put("success", successInvoices);
        return Result.OK(data);
    }
    public static boolean isInvoiceNumber(String invoiceNumber) {
        return invoiceNumber.matches("^[0-9]{4}-[0-9]{2}-[127][0-9]{3}$");
    }
}
