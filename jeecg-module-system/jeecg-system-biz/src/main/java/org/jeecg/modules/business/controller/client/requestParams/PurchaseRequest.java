package org.jeecg.modules.business.controller.client.requestParams;

import lombok.Data;
import org.jeecg.modules.business.vo.SkuQuantity;

import java.util.List;


@Data
public class PurchaseRequest {

    private final List<SkuQuantity> skuQuantity;

    private final List<String> platformOrderIDList;
}
