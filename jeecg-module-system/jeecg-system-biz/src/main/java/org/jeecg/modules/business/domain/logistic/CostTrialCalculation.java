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

    private final BigDecimal previousUnitPrice;

    private final BigDecimal previousShippingCost;

    private final BigDecimal previousRegistrationCost;

    private final BigDecimal previousAdditionalCost;



    private CostTrialCalculation(String countryCode, String logisticsChannelName, String logisticChannelCode,
                                 BigDecimal unitPrice, BigDecimal shippingCost, BigDecimal registrationCost, BigDecimal additionalCost, Date effectiveDate,
                                 BigDecimal previousUnitPrice,BigDecimal previousShippingCost, BigDecimal previousRegistrationCost, BigDecimal previousAdditionalCost) {
        this.countryCode = countryCode;
        this.logisticsChannelName = logisticsChannelName;
        this.logisticChannelCode = logisticChannelCode;
        this.unitPrice = unitPrice;
        this.shippingCost = shippingCost;
        this.registrationCost = registrationCost;
        this.additionalCost = additionalCost;
        this.effectiveDate = effectiveDate;
        this.previousUnitPrice = previousUnitPrice;
        this.previousShippingCost = previousShippingCost;
        this.previousRegistrationCost = previousRegistrationCost;
        this.previousAdditionalCost = previousAdditionalCost;
    }

    public CostTrialCalculation(LogisticChannelPrice price, LogisticChannelPrice previousPrice,int weight, String logisticsChannelName, String code) {
        this(price.getEffectiveCountry(), logisticsChannelName, code, price.getCalUnitPrice(), price.calculateShippingPrice(BigDecimal.valueOf(weight)),
                price.getRegistrationFee(), price.getAdditionalCost(), price.getEffectiveDate(),
                previousPrice.getCalUnitPrice(), previousPrice.calculateShippingPrice(BigDecimal.valueOf(weight)), previousPrice.getRegistrationFee(), previousPrice.getAdditionalCost()
        );
    }

    @JsonProperty("TotalCost")
    public double getTotalCost() {
        return shippingCost.add(registrationCost).add(additionalCost).setScale(2, RoundingMode.CEILING).doubleValue();
    }

    @JsonProperty("CostDifference")
    public double getCostDifference() {
        double previousCost = previousShippingCost.add(previousRegistrationCost).add(previousAdditionalCost).setScale(2, RoundingMode.CEILING).doubleValue();
        BigDecimal diff = BigDecimal.valueOf((getTotalCost() - previousCost) / previousCost * 100);
        return diff.setScale(2, RoundingMode.CEILING).doubleValue();
    }
}
