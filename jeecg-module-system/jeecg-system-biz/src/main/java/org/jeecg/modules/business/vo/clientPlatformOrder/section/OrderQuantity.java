package org.jeecg.modules.business.vo.clientPlatformOrder.section;


import lombok.Data;

/**
 * This class is used to display order quantities of each status
 */
@Data
public class OrderQuantity {

    private final Integer processed;
    private final Integer pending;
    private final Integer purchasing;

    public OrderQuantity() {
        this.pending = 0;
        this.purchasing = 0;
        this.processed = 0;
    }
}
