package org.jeecg.modules.business.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShippingFeesEstimationClient {
    private final String clientId;

    private final String code;

    private final Integer ordersToProcess;

    private final Integer processedOrders;

    private final BigDecimal dueForProcessedOrders;

    private final String isCompleteInvoice;

    private final Integer hasErrors;
}
