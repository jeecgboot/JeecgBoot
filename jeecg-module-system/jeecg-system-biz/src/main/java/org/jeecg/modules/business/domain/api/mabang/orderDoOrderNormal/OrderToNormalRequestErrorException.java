package org.jeecg.modules.business.domain.api.mabang.orderDoOrderNormal;

/**
 * This class represents error that is returned by target order-do-order-abnormal API
 * Message will contain error details.
 */
public class OrderToNormalRequestErrorException extends RuntimeException {
    public OrderToNormalRequestErrorException(String msg) {
        super(msg);
    }
}
