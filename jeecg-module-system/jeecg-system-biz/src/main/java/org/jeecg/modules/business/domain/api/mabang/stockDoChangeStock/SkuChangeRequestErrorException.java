package org.jeecg.modules.business.domain.api.mabang.stockDoChangeStock;

/**
 * This class represents error that is returned by target stock-do-change-stock API
 * Message will contain error details.
 */
public class SkuChangeRequestErrorException extends RuntimeException {
    public SkuChangeRequestErrorException(String msg) {
        super(msg);
    }
}
