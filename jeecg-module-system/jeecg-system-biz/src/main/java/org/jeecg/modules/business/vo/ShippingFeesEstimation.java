package org.jeecg.modules.business.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ShippingFeesEstimation {

    private String code;

    private String shop;

    private Integer ordersToProcess;

    private Integer processedOrders;

    private BigDecimal dueForProcessedOrders;

    private String isCompleteInvoice;

    private String errorMessage;

    private List<String> orderIds;

    public ShippingFeesEstimation(@JsonProperty("code") String code, @JsonProperty("shop")String shop,
                                  @JsonProperty("ordersToProcess")Integer ordersToProcess, @JsonProperty("processedOrders")Integer processedOrders,
                                  @JsonProperty("dueForProcessedOrders")BigDecimal dueForProcessedOrders,
                                  @JsonProperty("isCompleteInvoice")String isCompleteInvoice,
                                  @JsonProperty(value = "errorMessage")String errorMessage,
                                  @JsonProperty("orderIds")List<String> orderIds) {
        this.code = code;
        this.shop = shop;
        this.ordersToProcess = ordersToProcess;
        this.processedOrders = processedOrders;
        this.dueForProcessedOrders = dueForProcessedOrders;
        this.isCompleteInvoice = isCompleteInvoice;
        this.errorMessage = errorMessage;
        this.orderIds = orderIds;
    }
}
