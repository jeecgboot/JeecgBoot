package org.jeecg.modules.business.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * For purchase invoice.
 */
@Data
public class PromotionDetail {
    private final int count;
    private final BigDecimal unitAmount;
    private final String name;
}
