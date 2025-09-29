package org.jeecg.modules.business.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FeesEstimationPerOrder {

    private String code;

    private String shop;

    private BigDecimal shippingEstimation;

    private BigDecimal purchaseEstimation;

    private String errorMessage;

    private String orderId;
}
