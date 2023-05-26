package org.jeecg.modules.business.vo.clientPurchaseOrder;

import com.alibaba.fastjson.annotation.JSONCreator;
import lombok.Data;

@Data
public class PurchaseDemand {
    private final String skuId;
    private final int quantity;

}
