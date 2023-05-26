package org.jeecg.modules.business.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShippingFeesEstimation {

    private final String code;

    private final String shop;

    private final Integer ordersToProcess;

    private final Integer processedOrders;

    private final BigDecimal dueForProcessedOrders;
}
