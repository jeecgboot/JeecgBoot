package org.jeecg.modules.business.domain.api.mabang.stockGetStockQuantity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class WarehouseStock {
    /**仓库编号**/
    @JSONField(name = "warehouseId")
    private String warehouseId;
    /**仓库名称**/
    @JSONField(name = "warehouseName")
    private String warehouseName;
    /**库存总数**/
    @JSONField(name = "stockQuantity")
    private Integer stockQuantity;
    /**未发货数量 qty in platform orders**/
    @JSONField(name = "waitingQuantity")
    private Integer waitingQuantity;
    /**调拨在途**/
    @JSONField(name = "allotShippingQuantity")
    private Integer allotShippingQuantity;
    /**采购在途数量 qty in mabang purchase orders**/
    @JSONField(name = "shippingQuantity")
    private Integer shippingQuantity;
    /**加工在途量**/
    @JSONField(name = "processingQuantity")
    private Integer processingQuantity;
    /**fba未发货量**/
    @JSONField(name = "fbaWaitingQuantity")
    private Integer fbaWaitingQuantity;
    /**仓位**/
    @JSONField(name = "gridCode")
    private String gridCode;
}
