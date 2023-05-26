package org.jeecg.modules.business.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Entry of promotion history detail in purchase page.
 */
@Data
public class PromotionCouple {
    private final int count;
    private final BigDecimal unitAmount;
}
