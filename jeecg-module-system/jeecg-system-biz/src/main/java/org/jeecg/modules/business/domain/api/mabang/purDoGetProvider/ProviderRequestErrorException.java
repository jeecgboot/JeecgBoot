package org.jeecg.modules.business.domain.api.mabang.purDoGetProvider;

/**
 * This class represents error that is returned by target stock-do-search-sku-list API
 * Message will contain error details.
 */
public class ProviderRequestErrorException extends RuntimeException {
    public ProviderRequestErrorException(String msg) {
        super(msg);
    }
}
