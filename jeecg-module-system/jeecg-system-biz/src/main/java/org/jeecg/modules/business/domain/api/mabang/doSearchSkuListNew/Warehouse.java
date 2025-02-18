package org.jeecg.modules.business.domain.api.mabang.doSearchSkuListNew;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
 // 仓库信息,接口参数传showWarehouse才返回
public class Warehouse {
    /**仓库编号*/
    @JSONField(name = "warehouseId")
    private long warehouseId;
    /**仓位**/
    @JSONField(name = "gridCode")
    private String gridCode;
    /**库存警戒天数*/
    @JSONField(name = "stockWarningDays")
    private int stockWarningDays;
    /**商品预警天数*/
    @JSONField(name="product_warning_days")
    private int productWarningDays;
    /**仓库名称*/
    @JSONField(name = "warehouseName")
    private String warehouseName;
    /**仓库状态:1启用;2停用*/
    @JSONField(name = "status")
    private int status;
    /**仓库成本价*/
    @JSONField(name = "stockCost")
    private String stockCost;
    /**是否是默认仓库1是;2否*/
    @JSONField(name = "isDefault")
    private int isDefault;
    /**警戒库存*/
    @JSONField(name = "stockWarningQuantity")
    private int stockWarningQuantity;
    /**采购天数*/
    @JSONField(name = "purchaseDays")
    private int purchaseDays;
    @JSONField(name = "minPurchaseQuantity")
    private int minPurchaseQuantity;
    @JSONField(name = "maxPurchaseQuantity")
    private int maxPurchaseQuantity;
}
