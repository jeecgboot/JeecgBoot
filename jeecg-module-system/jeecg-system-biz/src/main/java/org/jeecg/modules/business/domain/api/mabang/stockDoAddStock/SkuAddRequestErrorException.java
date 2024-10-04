package org.jeecg.modules.business.domain.api.mabang.stockDoAddStock;

/**
 * This class represents error that is returned by target stock-do-add-stock API
 * Message will contain error details.
 */
public class SkuAddRequestErrorException extends RuntimeException {
    public SkuAddRequestErrorException(String msg) {
        super(msg);
    }
}
