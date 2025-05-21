package org.jeecg.modules.business.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.codehaus.jettison.json.JSONException;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.business.domain.api.mabang.dochangeorder.ChangeOrderResponse;
import org.jeecg.modules.business.domain.api.mabang.dochangeorder.ChangeWarehouseRequest;
import org.jeecg.modules.business.domain.api.mabang.dochangeorder.ChangeWarehouseRequestBody;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.*;
import org.jeecg.modules.business.domain.api.shouman.JsonOrderCreationRequestBody;
import org.jeecg.modules.business.domain.api.shouman.OrderCreationRequest;
import org.jeecg.modules.business.domain.api.shouman.OrderCreationRequestBody;
import org.jeecg.modules.business.domain.api.shouman.ShoumanOrderRequest;
import org.jeecg.modules.business.domain.job.ThrottlingExecutorService;
import org.jeecg.modules.business.entity.Client;
import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.entity.PlatformOrderContent;
import org.jeecg.modules.business.entity.Shouman.ShoumanOrder;
import org.jeecg.modules.business.entity.ShoumanOrderContent;
import org.jeecg.modules.business.mapper.PlatformOrderContentMapper;
import org.jeecg.modules.business.mapper.PlatformOrderMapper;
import org.jeecg.modules.business.service.*;
import org.jeecg.modules.business.vo.*;
import org.jeecg.modules.message.service.ISysMessageService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.jeecg.modules.business.entity.Invoice.InvoicingMethod.PRESHIPPING;
import static org.jeecg.modules.business.vo.PlatformOrderOperation.Action.*;

/**
 * @Description: 平台订单表
 * @Author: jeecg-boot
 * @Date: 2021-04-08
 * @Version: V1.0
 */
@Api(tags = "平台订单表")
@RestController
@RequestMapping("/business/platformOrder")
@Slf4j
public class PlatformOrderController {
    @Autowired
    private IClientService clientService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private IPlatformOrderService platformOrderService;
    @Autowired
    private PlatformOrderMapper platformOrderMapper;

    @Autowired
    private PlatformOrderContentMapper platformOrderContentMapper;

    @Autowired
    private IShoumanOrderService shoumanOrderService;

    @Autowired
    private IPlatformOrderMabangService platformOrderMabangService;
    @Autowired
    private IShopService shopService;
    @Autowired
    private ISecurityService securityService;
    @Autowired
    private ISysMessageService sysMessageService;

    @Autowired
    private Environment env;
    @Autowired
    private FreeMarkerConfigurer freemarkerConfigurer;

    private static final Integer DEFAULT_NUMBER_OF_THREADS = 2;
    private static final Integer MABANG_API_RATE_LIMIT_PER_MINUTE = 10;

    /**
     * Fetchs all orders with erp_status = 1, no logicistic channel and product available
     *
     * @param platformOrder
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "平台订单有货未交运-分页列表查询")
    @ApiOperation(value = "平台订单有货未交运-分页列表查询", notes = "平台订单有货未交运-分页列表查询")
    @GetMapping(value = "/errorList")
    public Result<?> queryPageErrorList(PlatformOrder platformOrder, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<PlatformOrder> queryWrapper = QueryGenerator.initQueryWrapper(platformOrder, req.getParameterMap());
        LambdaQueryWrapper<PlatformOrder> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.in(PlatformOrder::getErpStatus, OrderStatus.Pending.getCode());
        lambdaQueryWrapper.in(PlatformOrder::getProductAvailable, 1);
        lambdaQueryWrapper.in(PlatformOrder::getLogisticChannelName, "");
        Page<PlatformOrder> page = new Page<>(pageNo, pageSize);
        IPage<PlatformOrder> pageList = platformOrderService.page(page, lambdaQueryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param platformOrder
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "平台订单表-分页列表查询")
    @ApiOperation(value = "平台订单表-分页列表查询", notes = "平台订单表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(PlatformOrder platformOrder, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<PlatformOrder> queryWrapper = QueryGenerator.initQueryWrapper(platformOrder, req.getParameterMap());
        Page<PlatformOrder> page = new Page<>(pageNo, pageSize);
        IPage<PlatformOrder> pageList = platformOrderService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param platformOrderPage
     * @return
     */
    @AutoLog(value = "平台订单表-添加")
    @ApiOperation(value = "平台订单表-添加", notes = "平台订单表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody PlatformOrderPage platformOrderPage) {
        PlatformOrder platformOrder = new PlatformOrder();
        BeanUtils.copyProperties(platformOrderPage, platformOrder);
        platformOrderService.saveMain(platformOrder, platformOrderPage.getPlatformOrderContentList());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param platformOrderPage
     * @return
     */
    @AutoLog(value = "平台订单表-编辑")
    @ApiOperation(value = "平台订单表-编辑", notes = "平台订单表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody PlatformOrderPage platformOrderPage) {
        PlatformOrder platformOrder = new PlatformOrder();
        BeanUtils.copyProperties(platformOrderPage, platformOrder);
        PlatformOrder platformOrderEntity = platformOrderService.getById(platformOrder.getId());
        if (platformOrderEntity == null) {
            return Result.error("未找到对应数据");
        }
        platformOrderService.updateMain(platformOrder, platformOrderPage.getPlatformOrderContentList());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "平台订单表-通过id删除")
    @ApiOperation(value = "平台订单表-通过id删除", notes = "平台订单表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        platformOrderService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "平台订单表-批量删除")
    @ApiOperation(value = "平台订单表-批量删除", notes = "平台订单表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.platformOrderService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "平台订单表-通过id查询")
    @ApiOperation(value = "平台订单表-通过id查询", notes = "平台订单表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        PlatformOrder platformOrder = platformOrderService.getById(id);
        if (platformOrder == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(platformOrder);

    }

    /**
     * Get the order content of the order specified by its identifier
     *
     * @param id the identifier of the pla order
     * @return the order content in a result object
     */
    @AutoLog(value = "Query order contents by order's identifier")
    @ApiOperation(value = "Order content query", notes = "Query order contents by order's identifier")
    @GetMapping(value = "/queryPlatformOrderContentByMainId")
    public Result<?> queryPlatformOrderContentListByMainId(@RequestParam(name = "id") String id) {
        List<PlatformOrderContentFront> platformOrderContentList = platformOrderService.selectByMainIdAndSkuSync(id);
        IPage<PlatformOrderContentFront> page = new Page<>();
        page.setRecords(platformOrderContentList);
        page.setTotal(platformOrderContentList.size());
        return Result.OK(page);
    }

    /**
     * Export pending orders that have products in stock, but no logistic channel to a excel file
     *
     * @param request       a request that contains the condition
     * @param platformOrder a model and view
     * @return
     */
    @RequestMapping(value = "/exportErrorXls")
    public ModelAndView exportErrorXls(HttpServletRequest request, PlatformOrder platformOrder) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        // On récupère la liste de ID des lignes selectionnées
        String selections = request.getParameter("selections");
        // Step.1 On fait une requête SQL
        QueryWrapper<PlatformOrder> queryWrapper = QueryGenerator.initQueryWrapper(platformOrder, request.getParameterMap());
        LambdaQueryWrapper<PlatformOrder> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.in(PlatformOrder::getErpStatus, OrderStatus.Pending.getCode()); // On récupère commandes avec un statut 1
        lambdaQueryWrapper.in(PlatformOrder::getProductAvailable, 1); // produits de la commande en stock
        lambdaQueryWrapper.in(PlatformOrder::getLogisticChannelName, ""); // sans ligne de logistique
        lambdaQueryWrapper.inSql(PlatformOrder::getId, "SELECT po.id FROM platform_order po");

        //Step.2 获取导出数据
        List<PlatformOrder> queryList = platformOrderMapper.selectList(lambdaQueryWrapper);
        List<PlatformOrder> platformOrderList;
        if (oConvertUtils.isEmpty(selections)) {
            platformOrderList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            // on récupère les commandes correspondants aux ID de la selection
            platformOrderList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "有货未配单订表列表");
        mv.addObject(NormalExcelConstants.CLASS, PlatformOrder.class); // modèle à partir du quel on va créer l'excel
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("有货未配单订表数据", "导出人:" + sysUser.getRealname(), "有货未配单订表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, platformOrderList);
        return mv;
    }

    /**
     * Export data filtered by conditions to a excel file
     *
     * @param request       a request that contains the condition
     * @param platformOrder a model and view
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PlatformOrder platformOrder) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<PlatformOrder> queryWrapper = QueryGenerator.initQueryWrapper(platformOrder, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<PlatformOrder> queryList = platformOrderService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<PlatformOrder> platformOrderList;
        if (oConvertUtils.isEmpty(selections)) {
            platformOrderList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            platformOrderList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<PlatformOrderPage> pageList = new ArrayList<>();
        for (PlatformOrder main : platformOrderList) {
            PlatformOrderPage vo = new PlatformOrderPage();
            BeanUtils.copyProperties(main, vo);
            List<PlatformOrderContent> platformOrderContentList = platformOrderService.selectByMainId(main.getId());
            vo.setPlatformOrderContentList(platformOrderContentList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "平台订单表列表");
        mv.addObject(NormalExcelConstants.CLASS, PlatformOrderPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("平台订单表数据", "导出人:" + sysUser.getRealname(), "平台订单表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * Import data from excel file
     *
     * @param request request containing excel file
     * @return Result object that contains success/fail message.
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                Map<PlatformOrder, List<PlatformOrderContent>> orderMap = new HashMap<>();
                List<PlatformOrderPage> list = ExcelImportUtil.importExcel(file.getInputStream(), PlatformOrderPage.class, params);
                for (PlatformOrderPage page : list) {
                    PlatformOrder po = new PlatformOrder();
                    BeanUtils.copyProperties(page, po);
                    orderMap.put(po, page.getPlatformOrderContentList());
                }
                platformOrderService.saveBatch(orderMap);
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
     * @return
     */
    @GetMapping("/monthOrderQuantity")
    public Result<List<PlatformOrderQuantity>> monthOrderNumber() {
        List<PlatformOrderQuantity> res = platformOrderService.monthOrderNumber();
        return Result.OK(res);
    }

    /**
     * Get all orders by shop with erp status 1, 2 and 3
     *
     * @return
     */
    @GetMapping("/ordersByShop")
    public Result<List<PlatformOrderOption>> ordersByShop(@RequestParam("shopID") String shopID) {
        List<PlatformOrderOption> res = platformOrderService.ordersByShop(shopID);
        return Result.OK(res);
    }

    @PostMapping("/orderManagement")
    public Result<?> orderManagement(@RequestBody List<PlatformOrderOperation> orderOperations) throws IOException, JSONException {
        boolean isEmployee = securityService.checkIsEmployee();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Client client;
        if (!isEmployee) {
            client = clientService.getCurrentClient();
            if (client == null) {
                return Result.error(500, "Client not found. Please contact administrator.");
            }
            String shopId = orderOperations.get(0).getShopId();
            List<String> shopIds = shopService.listIdByClient(client.getId());
            if (!shopIds.contains(shopId)) {
                return Result.error(500, "You are not allowed to perform this operation.");
            }
        } else {
            client = clientService.getByShopId(orderOperations.get(0).getShopId());
        }

        long suspendCount = 0, cancelCount = 0, editCount = 0;
        List<PlatformOrderOperation> ordersToSuspend = orderOperations.stream().filter(orderOperation -> orderOperation.getAction().equalsIgnoreCase(SUSPEND.getValue())).collect(Collectors.toList());
        List<PlatformOrderOperation> ordersToCancel = orderOperations.stream().filter(orderOperation -> orderOperation.getAction().equalsIgnoreCase(CANCEL.getValue())).collect(Collectors.toList());
        List<PlatformOrderOperation> ordersToEdit = orderOperations.stream().filter(orderOperation -> orderOperation.getAction().equalsIgnoreCase(EDIT.getValue())).collect(Collectors.toList());
        for (PlatformOrderOperation orderOperation : ordersToSuspend) {
            suspendCount += orderOperation.getOrderIds().split(",").length;
        }
        for (PlatformOrderOperation orderOperation : ordersToCancel) {
            cancelCount += orderOperation.getOrderIds().split(",").length;
        }
        editCount = ordersToEdit.size();
        log.info("{} Orders to suspend: {}", suspendCount, ordersToSuspend);
        log.info("{} Orders to cancel: {}", cancelCount, ordersToCancel);
        log.info("{} Orders to edit: {}", editCount, ordersToEdit);

        Responses cancelResponses = new Responses();
        Responses suspendResponses = new Responses();
        Responses editResponses = new Responses();

        ExecutorService executor = ThrottlingExecutorService.createExecutorService(DEFAULT_NUMBER_OF_THREADS, MABANG_API_RATE_LIMIT_PER_MINUTE, TimeUnit.MINUTES);
        // Cancel orders
        List<CompletableFuture<Responses>> futuresCancel = ordersToCancel.stream().map(orderOperation -> CompletableFuture.supplyAsync(() -> platformOrderMabangService.cancelOrders(orderOperation), executor)).collect(Collectors.toList());
        List<Responses> cancelResults = futuresCancel.stream().map(CompletableFuture::join).collect(Collectors.toList());
        cancelResults.forEach(res -> {
            cancelResponses.getSuccesses().addAll(res.getSuccesses());
            cancelResponses.getFailures().addAll(res.getFailures());
        });
        log.info("{}/{} orders cancelled successfully.", cancelResponses.getSuccesses().size(), cancelCount);
        log.info("Failed to cancel orders: {}", cancelResponses.getFailures());


        // Suspend orders
        List<CompletableFuture<Responses>> futuresSuspend = ordersToSuspend.stream().map(orderOperation -> CompletableFuture.supplyAsync(() -> platformOrderMabangService.suspendOrder(orderOperation), executor)).collect(Collectors.toList());
        List<Responses> suspendResults = futuresSuspend.stream().map(CompletableFuture::join).collect(Collectors.toList());
        suspendResults.forEach(res -> {
            suspendResponses.getSuccesses().addAll(res.getSuccesses());
            suspendResponses.getFailures().addAll(res.getFailures());
        });
        log.info("{}/{} orders suspended successfully.", suspendResponses.getSuccesses().size(), suspendCount);

        // Edit orders
        // First we clear logistic channel if order has one
        List<List<String>> editOrderIds = Lists.partition(ordersToEdit.stream().map(PlatformOrderOperation::getOrderIds).collect(Collectors.toList()), 10);
        List<Order> ordersWithTrackingNumber = new ArrayList<>();

        List<OrderListRequestBody> requests = new ArrayList<>();
        for (List<String> platformOrderIdList : editOrderIds) {
            requests.add(new OrderListRequestBody().setPlatformOrderIds(platformOrderIdList));
        }
        List<Order> mabangOrders = platformOrderMabangService.getOrdersFromMabang(requests, executor);
        for (Order mabangOrder : mabangOrders) {
            if (mabangOrder.getTrackingNumber() == null) {
                continue;
            }
            if (!mabangOrder.getTrackingNumber().isEmpty()) {
                ordersWithTrackingNumber.add(mabangOrder);
            }
        }
        platformOrderMabangService.clearLogisticChannel(ordersWithTrackingNumber, executor);

        List<CompletableFuture<Responses>> futuresEdit = ordersToEdit.stream().map(orderOperation -> CompletableFuture.supplyAsync(() -> {
            ChangeWarehouseRequestBody body = new ChangeWarehouseRequestBody(orderOperation);
            ChangeWarehouseRequest request = new ChangeWarehouseRequest(body);
            ChangeOrderResponse response = request.send();
            Responses r = new Responses();
            if (response.success()) r.addSuccess(orderOperation.getOrderIds());
            else r.addFailure(orderOperation.getOrderIds());
            return r;
        }, executor)).collect(Collectors.toList());
        List<Responses> editResults = futuresEdit.stream().map(CompletableFuture::join).collect(Collectors.toList());
        editResults.forEach(res -> {
            editResponses.getSuccesses().addAll(res.getSuccesses());
            editResponses.getFailures().addAll(res.getFailures());
        });
        log.info("{}/{} orders edited successfully.", editResponses.getSuccesses().size(), editCount);
        executor.shutdown();

        JSONObject result = new JSONObject();
        result.put("cancelResult", cancelResponses);
        result.put("suspendResult", suspendResponses);
        result.put("editResult", editResponses);

        // send mail
        String subject = "[" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "] Orders management report";
        String destEmail = sysUser.getEmail();
        Properties prop = emailService.getMailSender();
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("firstname", client.getFirstName());
        templateModel.put("lastname", client.getSurname());
        if (cancelCount > 0) {
            templateModel.put("cancelSuccessCount", cancelResponses.getSuccesses().size() + "/" + cancelCount);
            templateModel.put("cancelSuccesses", cancelResponses.getSuccesses());
            templateModel.put("cancelFailures", cancelResponses.getFailures());
        }
        if (suspendCount > 0) {
            templateModel.put("suspendSuccessCount", suspendResponses.getSuccesses().size() + "/" + suspendCount);
            templateModel.put("suspendSuccesses", suspendResponses.getSuccesses());
            templateModel.put("suspendFailures", suspendResponses.getFailures());
        }
        if (editCount > 0) {
            templateModel.put("editSuccessCount", editResponses.getSuccesses().size() + "/" + editCount);
            templateModel.put("editSuccesses", editResponses.getSuccesses());
            templateModel.put("editFailures", editResponses.getFailures());
        }

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(env.getProperty("spring.mail.username"), env.getProperty("spring.mail.password"));
            }
        });
        try {
            freemarkerConfigurer = emailService.freemarkerClassLoaderConfig();
            Template freemarkerTemplate = freemarkerConfigurer.getConfiguration().getTemplate("client/orderManagementNotification.ftl");
            String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
            emailService.sendSimpleMessage(destEmail, subject, htmlBody, session);
            log.info("Mail sent successfully !");
        } catch (TemplateException | MessagingException e) {
            throw new RuntimeException(e);
        }

        // sync orders from Mabang
        List<String> poIdsSuccesses = new ArrayList<>();
        poIdsSuccesses.addAll(cancelResponses.getSuccesses());
        poIdsSuccesses.addAll(suspendResponses.getSuccesses());
        poIdsSuccesses.addAll(editResponses.getSuccesses());
        platformOrderMabangService.syncOrdersFromMabang(poIdsSuccesses);

        return Result.OK(result);
    }

    @GetMapping("/recipientInfo")
    public Result<?> getRecipientInfo(@RequestParam("orderId") String platformOrderId) {

        OrderListRequestBody body = new OrderListRequestBody().setPlatformOrderIds(Collections.singletonList(platformOrderId));
        OrderListRawStream rawStream = new OrderListRawStream(body);
        OrderListStream stream = new OrderListStream(rawStream);
        List<Order> orders = stream.all();
        if (orders.isEmpty()) {
            return Result.error(400, "Order not found.");
        }
        Order order = orders.get(0);
        JSONObject recipient = new JSONObject();
        recipient.put("recipient", order.getRecipient());
        recipient.put("phone", order.getPhone1());
        recipient.put("street1", order.getAddress());
        recipient.put("street2", order.getAddress2());
        return Result.OK(recipient);
    }

    /**
     * Fetches all potential Shouman orders
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @AutoLog(value = "潜在首曼订单-分页列表查询")
    @ApiOperation(value = "潜在首曼订单-分页列表查询", notes = "潜在首曼订单-分页列表查询")
    @GetMapping(value = "/shouman/list")
    public Result<?> queryPageShoumanList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, @RequestParam(name = "column") String column, @RequestParam(name = "order") String order) {
        Page<PlatformOrderPage> page = new Page<>(pageNo, pageSize);
        platformOrderService.pagePotentialShoumanOrders(page, CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, column), order);
        return Result.OK(page);
    }

    /**
     * Generates remarks of a Shouman eligible order
     *
     * @param platformOrderId
     * @return
     */
    @AutoLog(value = "潜在首曼订单-订单备注预览")
    @ApiOperation(value = "潜在首曼订单-订单备注预览", notes = "潜在首曼订单-订单备注预览")
    @GetMapping(value = "/shouman/preview")
    public Result<?> generateShoumanRemarkForOrder(@RequestParam(name = "platformOrderId") String platformOrderId) {
        List<ShoumanOrderContent> shoumanOrderContents = platformOrderContentMapper.searchShoumanOrderContentByPlatformOrderId(platformOrderId);
        OrderCreationRequestBody requestBody = new OrderCreationRequestBody(shoumanOrderContents);
        return Result.OK(requestBody.parameters().get("outboundInfos"));
    }

    /**
     * Create/update Shouman Orders
     *
     * @param shoumanOrderRequests
     * @return
     */
    @AutoLog(value = "首曼订单-生成或更新首曼订单")
    @ApiOperation(value = "首曼订单-生成或更新首曼订单", notes = "首曼订单-生成或更新首曼订单")
    @Transactional
    @RequestMapping(value = "/shouman/create", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> generateShoumanOrder(@RequestBody List<ShoumanOrderRequest> shoumanOrderRequests) {
        List<ShoumanOrder> shoumanOrders = new ArrayList<>();
        for (ShoumanOrderRequest shoumanOrderRequest : shoumanOrderRequests) {
            List<ShoumanOrderContent> shoumanOrderContents = platformOrderContentMapper.searchShoumanOrderContentByPlatformOrderId(shoumanOrderRequest.getPlatformOrderId());
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            OrderCreationRequestBody requestBody = new OrderCreationRequestBody(shoumanOrderContents);
            ShoumanOrder shoumanOrder = new ShoumanOrder();
            JSONObject jsonToUpdate = requestBody.parameters();
            // Override remarks if present
            if (shoumanOrderRequest.getRemarks() != null) {
                jsonToUpdate.put("outboundInfos", shoumanOrderRequest.getRemarks());
            }
            shoumanOrder.setOrderJson(jsonToUpdate.toJSONString());
            shoumanOrder.setPlatformOrderId(shoumanOrderRequest.getPlatformOrderId());
            shoumanOrder.setCreateBy(sysUser.getUsername());
            shoumanOrders.add(shoumanOrder);
        }
        try {
            shoumanOrderService.saveBatch(shoumanOrders);

            log.info("{} Shouman orders to send to ShoumanAPI", shoumanOrders.size());

            log.info("Started building and sending Shouman order requests");
            for (ShoumanOrder shoumanOrder : shoumanOrders) {
                String platformOrderId = shoumanOrder.getPlatformOrderId();

                log.info("Started building Shouman Order request {}", platformOrderId);
                OrderCreationRequest request = new OrderCreationRequest(new JsonOrderCreationRequestBody(shoumanOrder.getOrderJson()));
                log.info("Finished building Shouman Order request {}", platformOrderId);

                log.info("Started sending Shouman Order request {}", platformOrderId);
                String resultString = request.rawSend(shoumanOrder).getBody();
                log.info("Finished sending Shouman Order request {}", platformOrderId);

                JSONObject json = JSON.parseObject(resultString);
                Object status = json.get("status");
                if (status != null) {
                    if (((Integer) status) == 1) {
                        log.info("Shouman Order {} ended with success", platformOrderId);
                        shoumanOrder.setSuccess(status.toString());
                    } else {
                        log.info("Shouman Order {} failed", platformOrderId);
                    }
                }
            }
            log.info("Finished building and sending Shouman order requests");

            log.info("Started updating Shouman Orders in DB");
            shoumanOrderService.updateBatchById(shoumanOrders);
            log.info("Finished updating Shouman Orders in DB");

            return Result.OK("订单已发送成功，请前往首曼系统确认");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/editOrdersRemark")
    public Result<?> editOrdersRemark(@RequestBody InvoiceOrdersEditParam param) {
        String userId = ((LoginUser) SecurityUtils.getSubject().getPrincipal()).getId();
        if(param.getInvoicingMethod() == PRESHIPPING) {
            ResponsesWithMsg<String> mabangResponses = platformOrderMabangService.editOrdersRemark(param.getInvoiceNumber());
            if(!mabangResponses.getFailures().isEmpty())
                sysMessageService.sendProgress(userId, HttpStatus.BAD_REQUEST.value(),mabangResponses.getFailures(), "websocket.mabang.editOrdersRemarkError", "editOrdersRemark");
            else sysMessageService.sendProgress(userId, HttpStatus.OK.value(), null,"websocket.mabang.editOrdersRemarkSuccess", "editOrdersRemark");
            return Result.OK(mabangResponses);
        }
        sysMessageService.pushProgress(userId, "Method not supported");
        return Result.error(HttpStatus.NOT_FOUND.value(), "Invoicing method not supported");
    }
}