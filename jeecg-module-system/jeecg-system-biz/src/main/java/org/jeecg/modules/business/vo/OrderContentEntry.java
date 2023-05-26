package org.jeecg.modules.business.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderContentEntry {
    private final int quantity;
    private final BigDecimal totalAmount;
    private final String skuID;

    public OrderContentEntry(int quantity, BigDecimal totalAmount, String skuID) {
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.skuID = skuID;
    }
}
