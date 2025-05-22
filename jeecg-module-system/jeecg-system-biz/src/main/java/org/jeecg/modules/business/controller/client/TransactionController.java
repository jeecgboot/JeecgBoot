package org.jeecg.modules.business.controller.client;

import cn.hutool.core.date.DateTime;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.dto.message.TemplateMessageDTO;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.business.domain.shippingInvoice.ShippingInvoiceFactory;
import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.entity.PlatformOrderContent;
import org.jeecg.modules.business.entity.SkuPrice;
import org.jeecg.modules.business.mapper.*;
import org.jeecg.modules.business.service.*;
import org.jeecg.modules.business.vo.Estimation;
import org.jeecg.modules.business.vo.ShippingFeesEstimation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
    private ISkuPriceService skuPriceService;
    @Autowired
    PlatformOrderContentMapper platformOrderContentMapper;
    @Autowired
    ExchangeRatesMapper exchangeRatesMapper;
    @Autowired
    private ISysBaseAPI ISysBaseApi;
    @Autowired
    private ShippingInvoiceFactory factory;
    @Autowired
    Environment env;

    private final String SECTION_START = "<section><ul>";
    private final String SECTION_END = "</ul></section>";
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
        List<PlatformOrder> shippingOrders = platformOrderService.findUninvoicedShippingOrdersByShopForClient(shopIds, Arrays.asList(1,2,3));
        List<String> orderIds = shippingOrders.stream().map(PlatformOrder::getId).collect(Collectors.toList());
        Date startDate = null;
        Date endDate = null;
        List<ShippingFeesEstimation> shippingFeesEstimations;
        BigDecimal shippingFeesEstimation = BigDecimal.ZERO;
        String internalCode = "";
        int ordersToProcess = 0;
        int processedOrders = 0;
        if(!shippingOrders.isEmpty()) {
            startDate = shippingOrders.stream().map(PlatformOrder::getOrderTime).min(Date::compareTo).get();
            endDate = shippingOrders.stream().map(PlatformOrder::getOrderTime).max(Date::compareTo).get();
            List<String> shippingOrderIds = shippingOrders.stream().map(PlatformOrder::getId).collect(Collectors.toList());
            log.info("Estimating shipping fees for {}", shippingOrderIds.size());
            shippingFeesEstimations = factory.getEstimations(clientId, shippingOrderIds, errorMessages);
            for (ShippingFeesEstimation estimation : shippingFeesEstimations) {
                shippingFeesEstimation = shippingFeesEstimation.add(estimation.getDueForProcessedOrders());
                ordersToProcess += estimation.getOrdersToProcess();
                processedOrders += estimation.getProcessedOrders();
            }
            internalCode = shippingFeesEstimations.get(0).getCode();
        }

        // purchase estimation
        List<PlatformOrder> purchaseOrders = platformOrderService.fetchUninvoicedPurchaseOrdersByShopForClient(shopIds, Arrays.asList(1,2,3));
        BigDecimal purchaseEstimation = BigDecimal.ZERO;

        if(shippingOrders.isEmpty() && purchaseOrders.isEmpty()) {
            return Result.OK("No order to invoice.");
        }

        boolean isCompleteInvoiceReady = true;
        if(!purchaseOrders.isEmpty()) {
            Date purchaseStartDate = purchaseOrders.stream().map(PlatformOrder::getOrderTime).min(Date::compareTo).get();
            Date purchaseEndDate = purchaseOrders.stream().map(PlatformOrder::getOrderTime).max(Date::compareTo).get();
            if(startDate == null)
                startDate = purchaseStartDate;
            else
                startDate = startDate.before(purchaseStartDate) ? startDate : purchaseStartDate;
            if(endDate == null)
                endDate = purchaseEndDate;
            else
                endDate = endDate.after(purchaseEndDate) ? endDate : purchaseEndDate;

            List<String> purchaseOrderIds = purchaseOrders.stream().map(PlatformOrder::getId).collect(Collectors.toList());
            List<PlatformOrderContent> orderContents = platformOrderContentMapper.fetchOrderContent(purchaseOrderIds);
            List<String> skuIds = orderContents.stream().map(PlatformOrderContent::getSkuId).collect(Collectors.toList());
            List<SkuPrice> skuPrices = platformOrderContentMapper.searchSkuPrice(skuIds);
            BigDecimal exchangeRateEurToRmb = exchangeRatesMapper.getLatestExchangeRate("EUR", "RMB");
            if (skuPrices.size() != skuIds.size()) {
                isCompleteInvoiceReady = false;
                errorMessages.add("Some sku prices are missing.");
            }
            for (PlatformOrderContent content : orderContents) {
                for (SkuPrice skuPrice : skuPrices) {
                    if (content.getSkuId().equals(skuPrice.getSkuId())) {
                        purchaseEstimation = purchaseEstimation.add(skuPriceService.getPrice(skuPrice, content.getQuantity(), exchangeRateEurToRmb));
                    }
                }
            }
        }
        if(shippingFeesEstimation.compareTo(BigDecimal.ZERO) == 0 && purchaseEstimation.compareTo(BigDecimal.ZERO) == 0) {
            return Result.OK("No estimation found.");
        }
        if(!currency.equals("EUR")) {
            BigDecimal exchangeRate = exchangeRatesMapper.getLatestExchangeRate("EUR", currency);

            purchaseEstimation = purchaseEstimation.multiply(exchangeRate).setScale(2, RoundingMode.CEILING);
            shippingFeesEstimation = shippingFeesEstimation.multiply(exchangeRate).setScale(2, RoundingMode.CEILING);

        }
        log.info("Purchase Fee {} : {}", currency, purchaseEstimation);
        log.info("Shipping Fee {} : {}", currency, shippingFeesEstimation);
        // system notification
        String errors = SECTION_START;
        int max_entries = 100;
        int current_page = 0;
        int total_page = (int) Math.ceil((double) errorMessages.size() /max_entries);
        for(int i = 1; i <= errorMessages.size(); i++) {
            if(i%max_entries == 1) {
                errors = SECTION_START;
                current_page++;
            }
            errors = errors.concat("<li>" + i + " : " + errorMessages.get(i-1) +"</li>");
            if(i%max_entries==0 || i == errorMessages.size()) {
                errors = errors.concat(SECTION_END);
                Map<String, String> param = new HashMap<>();
                param.put("nb_entries", String.valueOf(errorMessages.size()));
                param.put("errors", errors);
                param.put("current_page", String.valueOf(current_page));
                param.put("total_page", String.valueOf(total_page));
                TemplateMessageDTO message = new TemplateMessageDTO("admin", "admin", "Expenses Overview Errors", param, "expenses_overview_errors");
                ISysBaseApi.sendTemplateAnnouncement(message);
            }
        }
        return Result.ok(new Estimation(internalCode, ordersToProcess, processedOrders, shippingFeesEstimation, purchaseEstimation, currency, errorMessages,null, shopIds, new DateTime(startDate).toString(), new DateTime(endDate).toString(), isCompleteInvoiceReady, orderIds));
    }
}
