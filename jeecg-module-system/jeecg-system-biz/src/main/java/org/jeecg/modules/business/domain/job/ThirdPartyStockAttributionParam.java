package org.jeecg.modules.business.domain.job;

import lombok.Data;

@Data
public class ThirdPartyStockAttributionParam {
    private String platformOrderId;
    private Integer quantity;
    private String skuId;
    private String sku;
    private String warehouseName;
}