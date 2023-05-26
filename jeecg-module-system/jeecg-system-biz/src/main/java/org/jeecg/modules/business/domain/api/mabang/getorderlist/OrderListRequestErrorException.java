package org.jeecg.modules.business.domain.api.mabang.getorderlist;

/**
 * This class represents error that is returned by target get-order-list API
 * Message will contain error details.
 */
public class OrderListRequestErrorException extends RuntimeException {
    public OrderListRequestErrorException(String msg) {
        super(msg);
    }
}
