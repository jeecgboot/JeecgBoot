package org.jeecg.modules.business.controller.client;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.business.domain.shippingInvoice.ShippingInvoiceFactory;
import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.mapper.*;
import org.jeecg.modules.business.service.*;
import org.jeecg.modules.business.vo.ShippingFeesEstimation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "Transaction")
@RestController
@RequestMapping("/transaction")
@Slf4j
public class TransactionController {
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private IPlatformOrderService platformOrderService;
    @Autowired
    private IShopService shopService;
    @Autowired
    ShippingInvoiceMapper shippingInvoiceMapper;
    @Autowired
    PlatformOrderMapper platformOrderMapper;
    @Autowired
    ClientMapper clientMapper;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    LogisticChannelPriceMapper logisticChannelPriceMapper;
    @Autowired
    LogisticChannelMapper logisticChannelMapper;
    @Autowired
    IPlatformOrderContentService platformOrderContentService;
    @Autowired
    ISkuDeclaredValueService skuDeclaredValueService;
    @Autowired
    FactureDetailMapper factureDetailMapper;
    @Autowired
    CountryService countryService;
    @Autowired
    IPurchaseOrderService purchaseOrderService;
    @Autowired
    PurchaseOrderContentMapper purchaseOrderContentMapper;
    @Autowired
    SkuPromotionHistoryMapper skuPromotionHistoryMapper;
    @Autowired
    ExchangeRatesMapper exchangeRatesMapper;
    @Autowired
    ISavRefundWithDetailService savRefundWithDetailService;
    @Autowired
    ISavRefundService savRefundService;
    @GetMapping(value="/list")
    public Result<?> list() {
        return Result.ok(transactionMapper.list());
    }
    @GetMapping(value="/listByClient")
    public Result<?> listByClientId(@RequestParam("clientId") String clientId) {
        return Result.ok(transactionMapper.listByClientId(clientId));
    }
    @GetMapping(value="/debit")
    public Result<?> debit(@RequestParam("clientId") String clientId) {
        List<String> errorMessages = new ArrayList<>();
        List<String> shopIds = shopService.listIdByClient(clientId);
        List<PlatformOrder> orders = platformOrderService.findUninvoicedOrdersByShopForClient(shopIds, Arrays.asList(1,2,3));
        List<String> orderIds = orders.stream().map(PlatformOrder::getId).collect(Collectors.toList());
        System.out.println("Orders size : " + orderIds.size());
        System.out.println("Orders : " + orderIds);
        ShippingInvoiceFactory factory = new ShippingInvoiceFactory(
                platformOrderService, clientMapper, shopMapper, logisticChannelMapper, logisticChannelPriceMapper,
                platformOrderContentService, skuDeclaredValueService, countryService, exchangeRatesMapper,
                purchaseOrderService, purchaseOrderContentMapper, skuPromotionHistoryMapper, savRefundService, savRefundWithDetailService);
        List<ShippingFeesEstimation> estimations = factory.getEstimations(clientId, orderIds, errorMessages);
        System.out.println("Estimation size : " + estimations.size());
        for(ShippingFeesEstimation estimation: estimations) {
            System.out.println("estimation : " + estimation.getDueForProcessedOrders());
        }
        return Result.ok(estimations);
    }
}
