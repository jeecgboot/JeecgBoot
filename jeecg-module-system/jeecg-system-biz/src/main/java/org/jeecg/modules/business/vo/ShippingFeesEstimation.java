package org.jeecg.modules.business.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShippingFeesEstimation {

    private String code;

    private String shop;

    private Integer ordersToProcess;

    private Integer processedOrders;

    private BigDecimal dueForProcessedOrders;

    private String isCompleteInvoice;

    private String errorMessage;

    public ShippingFeesEstimation(@JsonProperty("code") String code, @JsonProperty("shop")String shop,
                                  @JsonProperty("ordersToProcess")Integer ordersToProcess, @JsonProperty("processedOrders")Integer processedOrders,
                                  @JsonProperty("dueForProcessedOrders")BigDecimal dueForProcessedOrders,
                                  @JsonProperty("isCompleteInvoice")String isCompleteInvoice,
                                  @JsonProperty(value = "errorMessage")String errorMessage) {
        this.code = code;
        this.shop = shop;
        this.ordersToProcess = ordersToProcess;
        this.processedOrders = processedOrders;
        this.dueForProcessedOrders = dueForProcessedOrders;
        this.isCompleteInvoice = isCompleteInvoice;
        this.errorMessage = errorMessage;
    }
}
