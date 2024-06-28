package org.jeecg.modules.business.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Data
public class Estimation {
    private String code;
    private Integer ordersToProcess;
    private Integer processedOrders;
    private BigDecimal shippingFeesEstimation;
    private BigDecimal purchaseEstimation;
    private BigDecimal totalEstimation;
    private String currency;
    private List<String> errorMessages;
    private String shop = "";
    private List<String> shopIds;
    private String startDate;
    private String endDate;
    private boolean isCompleteInvoiceReady;
    private List<String> orderIds;
    public Estimation(@JsonProperty("code") String code,
                      @JsonProperty("ordersToProcess") Integer ordersToProcess,
                      @JsonProperty("processedOrders") Integer processedOrders,
                      @JsonProperty("shippingFeesEstimation") BigDecimal shippingFeesEstimation,
                      @JsonProperty("purchaseEstimation") BigDecimal purchaseEstimation,
                      @JsonProperty("currency") String currency,
                      @JsonProperty("errorMessages") List<String> errorMessages,
                      @JsonProperty("shop") String shop,
                      @JsonProperty("shopIds") List<String> shopIds,
                      @JsonProperty("startDate") String startDate,
                      @JsonProperty("endDate") String endDate,
                      @JsonProperty("isCompleteInvoiceReady") boolean isCompleteInvoiceReady,
                      @JsonProperty("orderIds") List<String> orderIds
    ) {
        this.code = code;
        this.ordersToProcess = ordersToProcess;
        this.processedOrders = processedOrders;
        this.currency = currency;
        this.purchaseEstimation = purchaseEstimation;
        this.shippingFeesEstimation = shippingFeesEstimation;
        this.errorMessages = errorMessages;
        this.shop = shop;
        this.shopIds = shopIds;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isCompleteInvoiceReady = isCompleteInvoiceReady;
        this.orderIds = orderIds;
        totalEstimation = shippingFeesEstimation.add(purchaseEstimation).setScale(2, RoundingMode.CEILING);
    }
}
