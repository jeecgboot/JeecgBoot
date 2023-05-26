package org.jeecg.modules.business.controller.client;

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
import org.jeecg.modules.business.entity.ClientPlatformOrderContent;
import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.entity.PlatformOrderContent;
import org.jeecg.modules.business.service.IPlatformOrderService;
import org.jeecg.modules.business.vo.SkuQuantity;
import org.jeecg.modules.business.vo.clientPlatformOrder.ClientPlatformOrderPage;
import org.jeecg.modules.business.vo.clientPlatformOrder.PurchaseConfirmation;
import org.jeecg.modules.business.vo.clientPlatformOrder.section.OrderQuantity;
import org.jeecg.modules.business.vo.clientPlatformOrder.section.OrdersStatisticData;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: API handler for any request related to platform order when request sender is a client.
 * @Author: Wenke
 * @Date: 2021-04-17
 * @Version: V1.000002
 */
@Api(tags = " platform order (client)")
@RestController
@RequestMapping("/business/clientPlatformOrder")
@Slf4j
public class ClientPlatformOrderController {
    private final IPlatformOrderService platformOrderService;

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
        List<ClientPlatformOrderPage> pageList = new ArrayList<>();
        for (PlatformOrder main : platformOrderList) {
            ClientPlatformOrderPage vo = new ClientPlatformOrderPage();
            BeanUtils.copyProperties(main, vo);
            List<ClientPlatformOrderContent> platformOrderContentList = platformOrderService.selectClientVersionByMainId(main.getId());
            vo.setPlatformOrderContentList(platformOrderContentList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "Platform order list");
        mv.addObject(NormalExcelConstants.CLASS, ClientPlatformOrderPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("Platform order data", "Export by:" + sysUser.getRealname(), "Orders"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    @Autowired
    public ClientPlatformOrderController(IPlatformOrderService platformOrderService) {
        this.platformOrderService = platformOrderService;
    }

    /**
     * Query all pending platform orders for current client.
     *
     * @return all pending platform orders of current client in a Result object
     */
    @AutoLog(value = "当前客户的待处理平台订单列表查询")
    @ApiOperation(value = "当前客户的待处理平台订单列表查询", notes = "当前客户的待处理平台订单列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<ClientPlatformOrderPage>> queryPendingPlatformOrder(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        log.info("Query for pending client platform orders");
        IPage<ClientPlatformOrderPage> page = new Page<>(pageNo, pageSize);
        platformOrderService.pendingPlatformOrderPage(page);
        return Result.OK(page);
    }

    /**
     * Query all purchasing platform orders for current client.
     *
     * @return all purchasing platform orders of current client in a Result object
     */
    @AutoLog(value = "当前客户的采购中平台订单列表查询")
    @ApiOperation(value = "当前客户的采购中平台订单列表查询", notes = "当前客户的采购中平台订单列表查询")
    @GetMapping(value = "/listPurchasing")
    public Result<IPage<ClientPlatformOrderPage>> queryPurchasingPlatformOrder(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        log.info("Query for purchasing client platform orders");
        IPage<ClientPlatformOrderPage> page = new Page<>(pageNo, pageSize);
        platformOrderService.purchasingPlatformOrderPage(page);
        return Result.OK(page);
    }

    /**
     * Query all processed platform orders for current client.
     *
     * @return all processed platform orders of current client in a Result object
     */
    @AutoLog(value = "当前客户的已发货平台订单列表查询")
    @ApiOperation(value = "当前客户的已发货平台订单列表查询", notes = "当前客户的已发货平台订单列表查询")
    @GetMapping(value = "/listProcessed")
    public Result<IPage<ClientPlatformOrderPage>> queryProcessedPlatformOrder(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        log.info("Query for processed client platform orders");
        IPage<ClientPlatformOrderPage> page = new Page<>(pageNo, pageSize);
        platformOrderService.processedPlatformOrderPage(page);
        return Result.OK(page);
    }


    /**
     * Query a platform order by its identifier.
     *
     * @param id the identifier of the platform order
     * @return {@code Result} of platform order, or {@code Result} with error message in case of failure.
     */
    @AutoLog(value = "平台订单表-通过id查询")
    @ApiOperation(value = "平台订单表-通过id查询", notes = "平台订单表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        PlatformOrder platformOrder = platformOrderService.getById(id);
        if (platformOrder == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(platformOrder);
    }

    /**
     * Query a certain platform order's content by its identifier.
     *
     * @param id the identifier of the platform order
     * @return its content
     */
    @AutoLog(value = "平台订单内容通过主表ID查询")
    @ApiOperation(value = "平台订单内容主表ID查询", notes = "平台订单内容-通主表ID查询")
    @GetMapping(value = "/queryPlatformOrderContentByMainId")
    public Result<?> queryPlatformOrderContentListByMainId(@RequestParam(name = "id") String id) {
        List<PlatformOrderContent> platformOrderContentList = platformOrderService.selectByMainId(id);
        IPage<PlatformOrderContent> page = new Page<>();
        page.setRecords(platformOrderContentList);
        page.setTotal(platformOrderContentList.size());
        return Result.OK(page);
    }


    /**
     * Compute order statistic data based on identifiers of orders.
     *
     * @param orderIds Identifiers of orders
     * @return Order statistic data in result
     */
    @AutoLog(value = "Compute order statistic data")
    @ApiOperation(
            value = "Compute Order Statistic Data",
            notes = "Compute order statistic data of platform orders indicated by its identifier."
    )
    @PostMapping(value = "/computeInfo", consumes = "application/json", produces = "application/json")
    public Result<OrdersStatisticData> queryOrdersStatisticInfo(@RequestBody List<String> orderIds) {
        log.info("Calculating statistic information for orders: {}", orderIds);
        OrdersStatisticData ordersData = platformOrderService.getPlatformOrdersStatisticData(orderIds);
        log.info("Got statistic information: {}", ordersData);
        return Result.OK(ordersData);
    }

    /**
     * Create a purchase confirmation based on some platform orders
     *
     * @param orderIds Identifiers of platform orders
     * @return Purchase confirmation data in Result
     */
    @AutoLog(value = "Place a purchase order by platform orders")
    @ApiOperation(
            value = "Place a purchase order by platform orders",
            notes = "Place a purchase order by platform orders, return purchase details to let" +
                    "client confirm information."
    )
    @PostMapping(value = "/placeOrder", consumes = "application/json", produces = "application/json")
    public Result<PurchaseConfirmation> placeOrder(@RequestBody List<String> orderIds) {
        log.info("One client place a purchase order");
        PurchaseConfirmation d = platformOrderService.confirmPurchaseByPlatformOrder(orderIds);
        log.info(d.toString());
        return Result.OK(d);
    }

    /**
     * Adjust confirmation information based on sku quantity.
     *
     * @param skuQuantities sku and its quantity
     * @return confirmation.
     */
    @PostMapping(value = "/adjustOrder", consumes = "application/json", produces = "application/json")
    public Result<PurchaseConfirmation> adjustOrder(@RequestBody List<SkuQuantity> skuQuantities) {
        log.info("One client adjust its purchase order");
        log.info("Content: {}", skuQuantities);
        PurchaseConfirmation d = platformOrderService.confirmPurchaseBySkuQuantity(skuQuantities);
        return Result.OK(d);
    }

    /**
     * Query all platform orders for current client.
     *
     * @return all platform orders of current client in a Result object
     */
    @AutoLog(value = "当前客户的平台订单数据查询")
    @ApiOperation(value = "当前客户的平台订单数据查询", notes = "当前客户的平台订单数据查询")
    @GetMapping(value = "/queryQuantities")
    public Result<OrderQuantity> queryQuantities() {
        log.info("Query order quantities of each status");
        OrderQuantity orderQuantity = platformOrderService.queryOrderQuantities();
        return Result.OK(orderQuantity);
    }

}
