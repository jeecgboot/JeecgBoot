package org.jeecg.modules.business.domain.api.mabang.getorderlist;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Necessary order item data from mabang side.
 */
@Data
public class OrderItem {

    /**
     * Erp code of sku in DB.
     */
    @JSONField(name = "stockSku")
    private String erpCode;

    @JSONField(deserialize = false)
    private String platformOrderId;

    @JSONField(name = "quantity")
    private int quantity;

    @JSONField(name = "originOrderId")
    private String originOrderId;

    /**
     * 1 = 有货
     * 2 = 缺货
     * 3 = 缺货
     */
    @JSONField(name = "hasGoods")
    private String hasGoods;

    private String productAvailable;

    /**
     * Status :
     * 2 = Normal
     * 4 = Removed
     */
    @JSONField(name = "status")
    private String status;

    private String erpStatus;

    /**
     * Resolve order item's status by the following rule :
     * If the item's status = 4 (removed), then the item is obsolete, otherwise the item will inherit the order's status
     * @param orderStatus Status code of the order
     */
    public void resolveStatus(String orderStatus) {
        if (this.status.equals("4")) {
            this.erpStatus = OrderStatus.Obsolete.getCode();
        } else {
            this.erpStatus = orderStatus;
        }
    }

    public void resolveProductAvailability() {
        if (hasGoods.equalsIgnoreCase("2")) {
            productAvailable = "0";
        } else {
            productAvailable = "1";
        }
    }
}
