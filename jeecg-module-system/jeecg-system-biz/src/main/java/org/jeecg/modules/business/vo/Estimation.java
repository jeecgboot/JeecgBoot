package org.jeecg.modules.business.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Data
public class Estimation {
    private BigDecimal shippingFeesEstimation;
    private BigDecimal purchaseEstimation;
    private BigDecimal totalEstimation;
    private String currency;
    private List<String> errorMessages;
    private List<String> shopIds;
    private String startDate;
    private String endDate;
    private boolean isCompleteInvoiceReady;

    public Estimation(@JsonProperty("shippingFeesEstimation") BigDecimal shippingFeesEstimation,
                      @JsonProperty("purchaseEstimation") BigDecimal purchaseEstimation,
                      @JsonProperty("currency") String currency,
                      @JsonProperty("errorMessages") List<String> errorMessages,
                      @JsonProperty("orderIds") List<String> shopIds,
                      @JsonProperty("startDate") String startDate,
                      @JsonProperty("endDate") String endDate,
                      @JsonProperty("isCompleteInvoiceReady") boolean isCompleteInvoiceReady) {
        this.currency = currency;
        this.purchaseEstimation = purchaseEstimation;
        this.shippingFeesEstimation = shippingFeesEstimation;
        this.errorMessages = errorMessages;
        this.shopIds = shopIds;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isCompleteInvoiceReady = isCompleteInvoiceReady;
        totalEstimation = shippingFeesEstimation.add(purchaseEstimation).setScale(2, RoundingMode.CEILING);
    }
}
