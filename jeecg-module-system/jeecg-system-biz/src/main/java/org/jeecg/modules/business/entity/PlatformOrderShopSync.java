package org.jeecg.modules.business.entity;

import lombok.Data;

@Data
public class PlatformOrderShopSync {

    private final String platformOrderId;

    private final String trackingNumber;

    private final String shopifyPrefix;

    private final String shopifyToken;

    private final String postcode;
}
