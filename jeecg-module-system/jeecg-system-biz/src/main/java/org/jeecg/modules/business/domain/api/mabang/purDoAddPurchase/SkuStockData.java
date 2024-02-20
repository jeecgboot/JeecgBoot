package org.jeecg.modules.business.domain.api.mabang.purDoAddPurchase;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SkuStockData {
    /**库存sku*/
    private String stockSku;
    /**采购价格*/
    private BigDecimal price;
    /**采购量*/
    private Integer purchaseNum;
    /**商品备注*/
    private String goodremark;
    private String provider;
}
