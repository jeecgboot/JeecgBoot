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

    public Estimation(@JsonProperty("shippingFeesEstimation") BigDecimal shippingFeesEstimation,
                      @JsonProperty("purchaseEstimation") BigDecimal purchaseEstimation,
                      @JsonProperty("currency") String currency,
                      @JsonProperty("errorMessages") List<String> errorMessages) {
        this.currency = currency;
        this.purchaseEstimation = purchaseEstimation;
        this.shippingFeesEstimation = shippingFeesEstimation;
        this.errorMessages = errorMessages;
        totalEstimation = shippingFeesEstimation.add(purchaseEstimation).setScale(2, RoundingMode.CEILING);
    }
}
