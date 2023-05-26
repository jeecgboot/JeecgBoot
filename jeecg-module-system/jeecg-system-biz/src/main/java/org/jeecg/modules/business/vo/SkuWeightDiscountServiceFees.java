package org.jeecg.modules.business.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

@Data
public class SkuWeightDiscountServiceFees {

    private final String skuId;

    private final String erpCode;

    private final Integer weight;

    private final BigDecimal discount;

    private final BigDecimal serviceFees;

    public SkuWeightDiscountServiceFees(String skuId, String erpCode, Integer weight, BigDecimal discount, BigDecimal serviceFee) {
        this.skuId = Objects.requireNonNull(skuId);
        this.erpCode = erpCode;
        this.weight = weight;
        this.discount = discount;
        this.serviceFees = serviceFee;
    }

    @Override
    public String toString() {
        return String.format(
                "ID: %s, ERP: %s, weight: %s, discount: %s, service fees: %s",
                skuId, erpCode, weight, discount, serviceFees
        );
    }
}
