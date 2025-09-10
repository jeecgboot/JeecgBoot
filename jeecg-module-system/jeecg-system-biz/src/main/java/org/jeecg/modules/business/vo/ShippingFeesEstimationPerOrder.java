package org.jeecg.modules.business.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShippingFeesEstimationPerOrder {

    private String code;

    private String shop;

    private BigDecimal amount;

    private String isCompleteInvoice;

    private String errorMessage;

    private String orderId;

    public ShippingFeesEstimationPerOrder(@JsonProperty("code") String code,
                                          @JsonProperty("shop")String shop,
                                          @JsonProperty("amount")BigDecimal amount,
                                          @JsonProperty("isCompleteInvoice")String isCompleteInvoice,
                                          @JsonProperty(value = "errorMessage")String errorMessage,
                                          @JsonProperty("orderId") String orderId) {
        this.code = code;
        this.shop = shop;
        this.isCompleteInvoice = isCompleteInvoice;
        this.errorMessage = errorMessage;
        this.amount = amount;
        this.orderId = orderId;
    }
}
