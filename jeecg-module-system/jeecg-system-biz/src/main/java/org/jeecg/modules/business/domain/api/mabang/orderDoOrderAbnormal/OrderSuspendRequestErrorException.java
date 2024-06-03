package org.jeecg.modules.business.domain.api.mabang.orderDoOrderAbnormal;

/**
 * This class represents error that is returned by target order-do-order-abnormal API
 * Message will contain error details.
 */
public class OrderSuspendRequestErrorException extends RuntimeException {
    public OrderSuspendRequestErrorException(String msg) {
        super(msg);
    }
}
