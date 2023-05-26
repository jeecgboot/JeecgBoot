package org.jeecg.modules.business.vo;

import lombok.Data;
import org.jeecg.modules.business.entity.ShippingFeesWaiver;

import java.util.Objects;

@Data
public class SkuShippingFeesWaiver {

    private final String skuId;

    private final String erpCode;

    private final ShippingFeesWaiver shippingFeesWaiver;

    public SkuShippingFeesWaiver(String skuId, String erpCode, ShippingFeesWaiver shippingFeesWaiver) {
        this.skuId = Objects.requireNonNull(skuId);
        this.erpCode = erpCode;
        this.shippingFeesWaiver = shippingFeesWaiver;
    }
}
