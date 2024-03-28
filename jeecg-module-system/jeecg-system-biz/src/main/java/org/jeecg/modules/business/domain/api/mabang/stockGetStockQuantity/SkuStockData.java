package org.jeecg.modules.business.domain.api.mabang.stockGetStockQuantity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data

public class SkuStockData {
    @JSONField(name = "stockSku")
    private String stockSku;
    @JSONField(name = "stockQuantity")
    private Integer stockQuantity;
    @JSONField(name = "warehouse")
    private List<WarehouseStock> warehouse;

    public WarehouseStock getWarehouseStock(String warehouseName) {
        for (WarehouseStock stock : warehouse) {
            if (stock.getWarehouseName().equals(warehouseName)) {
                return stock;
            }
        }
        return null;
    }
}
