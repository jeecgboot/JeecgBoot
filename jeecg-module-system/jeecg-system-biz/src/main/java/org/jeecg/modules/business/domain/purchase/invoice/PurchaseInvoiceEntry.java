package org.jeecg.modules.business.domain.purchase.invoice;

import lombok.Getter;

import java.math.BigDecimal;

public class PurchaseInvoiceEntry {
    @Getter
    private final String erp_code;

    @Getter
    private final String sku_en_name;

    @Getter
    private final Integer quantity;

    @Getter
    private final BigDecimal totalAmount;

    public PurchaseInvoiceEntry(String erp_code, String sku_en_name, Integer quantity, BigDecimal totalAmount) {
        this.erp_code = erp_code;
        this.sku_en_name = sku_en_name;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
    }

    public BigDecimal unitPrice() {
        return totalAmount.divide(BigDecimal.valueOf(quantity));
    }


}
