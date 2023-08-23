package org.jeecg.modules.business.domain.logistic;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.jeecg.modules.business.entity.LogisticChannelPrice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * Data set for shipping cost trial calculation.
 */
@Data
public class CostTrialCalculation {

    private final String countryCode;

    private final String logisticsChannelName;

    private final String logisticChannelCode;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private final Date effectiveDate;

    private final BigDecimal unitPrice;

    private final BigDecimal shippingCost;

    private final BigDecimal registrationCost;

    private final BigDecimal additionalCost;


    private CostTrialCalculation(String countryCode, String logisticsChannelName, String logisticChannelCode, BigDecimal unitPrice, BigDecimal shippingCost,
                                 BigDecimal registrationCost, BigDecimal additionalCost, Date effectiveDate) {
        this.countryCode = countryCode;
        this.logisticsChannelName = logisticsChannelName;
        this.logisticChannelCode = logisticChannelCode;
        this.unitPrice = unitPrice;
        this.shippingCost = shippingCost;
        this.registrationCost = registrationCost;
        this.additionalCost = additionalCost;
        this.effectiveDate = effectiveDate;
    }

    public CostTrialCalculation(LogisticChannelPrice price, int weight, String logisticsChannelName, String code) {
        this(price.getEffectiveCountry(), logisticsChannelName, code, price.getCalUnitPrice(), price.calculateShippingPrice(BigDecimal.valueOf(weight)),
                price.getRegistrationFee(), price.getAdditionalCost(), price.getEffectiveDate());
    }

    @JsonProperty("TotalCost")
    public double getTotalCost() {
        return shippingCost.add(registrationCost).add(additionalCost).setScale(2, RoundingMode.CEILING).doubleValue();
    }
}
