package org.jeecg.modules.business.domain.api.mabang.doSearchSkuList;

/**
 * This class represents error that is returned by target stock-do-search-sku-list API
 * Message will contain error details.
 */
public class SkuListRequestErrorException extends RuntimeException {
    public SkuListRequestErrorException(String msg) {
        super(msg);
    }
}
