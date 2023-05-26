package org.jeecg.modules.business.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Proportion of logistic expense by a certain condition
 */
@Data
public class LogisticExpenseProportion {

    /**
     * ID of the condition
     */
    private final String id;

    /**
     * Name of the condition
     */
    private final String name;

    /**
     * expense
     */
    private final BigDecimal expense;
}
