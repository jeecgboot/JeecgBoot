package org.jeecg.modules.business.vo.clientPlatformOrder.section;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Each row of client platform order page in confirmation page
 */
@Data
public class PurchaseDetail {
    private final String skuId;

    private final String erpCode;

    private final String imageSource;

    private final String nameEn;

    private final String nameZh;

    private final int quantity;

    private final BigDecimal price;

    private final BigDecimal total;

    public PurchaseDetail(String skuId, String erpCode, String imageSource, String nameEn, String nameZh, int quantity, BigDecimal price) {
        this.skuId = skuId;
        this.erpCode = erpCode;
        this.imageSource = imageSource;
        this.nameEn = nameEn;
        this.nameZh = nameZh;
        this.quantity = quantity;
        this.price = price;
        this.total = new BigDecimal(quantity).multiply(price);
    }
}
