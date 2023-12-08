package org.jeecg.modules.business.domain.api.mabang.skuShippingQty;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName("skuShippingQtyData")
public class SkuShippingQtyData {

    @JSONField(name="stockSku")
    private String stockSku;
    @JSONField(name="stockQuantity")
    private Integer stockQuantity;
    @JSONField(name="warehouse")
    private JSONArray rawData;

    private List<SkuWarehouseStockData> skuWarehouseStockDataList;
    private int shippingQuantity = 0;
    private int virtualQuantity = 0;
    private int stockAfterDistribution;

    public String toString() {
        return "StockSku : " + this.stockSku +
                "\nStockQuantity : " + this.stockQuantity;
    }

    public void jsonToData() {
        this.skuWarehouseStockDataList = rawData.toJavaList(SkuWarehouseStockData.class);
    }

    public void calculateShippingQuantity() {
        if(skuWarehouseStockDataList == null || skuWarehouseStockDataList.isEmpty()) {
            return;
        }
        for (SkuWarehouseStockData skuWarehouseStockData : skuWarehouseStockDataList) {
            shippingQuantity += skuWarehouseStockData.getShippingQuantity();
        }
    }
    public void calculateVirtualQuantity() {
        if(skuWarehouseStockDataList == null || skuWarehouseStockDataList.isEmpty()) {
            return;
        }
        virtualQuantity = stockAfterDistribution + shippingQuantity;
    }
}
