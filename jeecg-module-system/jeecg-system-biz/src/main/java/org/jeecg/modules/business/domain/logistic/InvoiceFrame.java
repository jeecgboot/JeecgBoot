package org.jeecg.modules.business.domain.logistic;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class InvoiceFrame extends DataFrame {
    /**
     * Invoice code
     */
    private final String code;

    /**
     * date of generation
     */
    private final Date createDate;

    /**
     * total amount of invoice
     */
    private final double receiveAmount;
}
