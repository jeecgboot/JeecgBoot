package org.jeecg.modules.business.domain.api.mabang.skuShippingQty;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("skuWarehouseStockData")
public class SkuWarehouseStockData {
    /**
     * 仓库编号
     */
    @JSONField(name="warehouseId")
    private String warehouseId;
    /**
     * 仓库名称
     */
    @JSONField(name="warehouseName")
    private String warehouseName;
    /**
     * 库存总数
     */
    @JSONField(name="stockQuantity")
    private int stockQuantity;
    /**
     * 未发货数量
     */
    @JSONField(name="waitingQuantity")
    private int waitingQuantity;
    /**
     * 采购在途数量
     */
    @JSONField(name="shippingQuantity")
    private int shippingQuantity;
}
