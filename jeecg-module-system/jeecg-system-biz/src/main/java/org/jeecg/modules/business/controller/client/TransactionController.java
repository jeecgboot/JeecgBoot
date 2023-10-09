package org.jeecg.modules.business.controller.client;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.business.domain.shippingInvoice.ShippingInvoiceFactory;
import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.entity.PlatformOrderContent;
import org.jeecg.modules.business.entity.SkuPrice;
import org.jeecg.modules.business.mapper.*;
import org.jeecg.modules.business.service.*;
import org.jeecg.modules.business.vo.Estimation;
import org.jeecg.modules.business.vo.ShippingFeesEstimation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
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
    PlatformOrderContentMapper platformOrderContentMapper;
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
    @GetMapping(value="/listByClientAndCurrency")
    public Result<?> listByClientId(@RequestParam("clientId") String clientId, @RequestParam("currency") String currency) {
        return Result.ok(transactionMapper.listByClientIdAndCurrency(clientId, currency));
    }
    @GetMapping(value="/debit")
    public Result<?> debit(@RequestParam("clientId") String clientId, @RequestParam("currency") String currency) {
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
        List<ShippingFeesEstimation> shippingFeesEstimations = factory.getEstimations(clientId, orderIds, errorMessages);
        System.out.println("Estimation size : " + shippingFeesEstimations.size());
        for(ShippingFeesEstimation estimation: shippingFeesEstimations) {
            System.out.println("estimation : " + estimation.getDueForProcessedOrders());
        }
        // purchase estimation
        List<String> estimationOrderIds = new ArrayList<>();
        BigDecimal shippingFeesEstimation = BigDecimal.ZERO;
        for(ShippingFeesEstimation estimation: shippingFeesEstimations) {
            estimationOrderIds.addAll(estimation.getOrderIds());
            shippingFeesEstimation = shippingFeesEstimation.add(estimation.getDueForProcessedOrders());
        }
        System.out.println("Estimation order ids : " + estimationOrderIds);
        List<PlatformOrderContent> orderContents = platformOrderContentMapper.fetchOrderContent(estimationOrderIds);
        List<String> skuIds = orderContents.stream().map(PlatformOrderContent::getSkuId).collect(Collectors.toList());
        List<SkuPrice> skuPrices = platformOrderContentMapper.searchSkuPrice(skuIds);
        BigDecimal exchangeRateEurToRmb = exchangeRatesMapper.getLatestExchangeRate("EUR", "RMB");
        BigDecimal purchaseEstimation = BigDecimal.ZERO;
        for(PlatformOrderContent content : orderContents){
            for (SkuPrice skuPrice : skuPrices) {
                if(content.getSkuId().equals(skuPrice.getSkuId())) {
                    purchaseEstimation = purchaseEstimation.add(skuPrice.getPrice(content.getQuantity(), exchangeRateEurToRmb));
                }
            }
        }
        if(!currency.equals("EUR")) {
            BigDecimal exchangeRate = exchangeRatesMapper.getLatestExchangeRate("EUR", currency);
            System.out.println("Exchange rate : " + exchangeRate);
            System.out.println("Purchase Fee : " + purchaseEstimation);
            System.out.println("Shipping Fee : " + shippingFeesEstimation);

            purchaseEstimation = purchaseEstimation.multiply(exchangeRate).setScale(2, RoundingMode.CEILING);
            shippingFeesEstimation = shippingFeesEstimation.multiply(exchangeRate).setScale(2, RoundingMode.CEILING);

            System.out.println("Purchase Fee " + currency + " : " + purchaseEstimation);
            System.out.println("Shipping Fee " + currency + " : " + shippingFeesEstimation);
        }
        return Result.ok(new Estimation(shippingFeesEstimation, purchaseEstimation, currency, errorMessages));
    }
}
