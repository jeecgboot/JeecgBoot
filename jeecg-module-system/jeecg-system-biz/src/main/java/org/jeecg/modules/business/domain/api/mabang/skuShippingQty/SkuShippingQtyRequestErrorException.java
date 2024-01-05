package org.jeecg.modules.business.domain.api.mabang.skuShippingQty;

/**
 * This class represents error that is returned by target stock-do-search-sku-list API
 * Message will contain error details.
 */
public class SkuShippingQtyRequestErrorException extends RuntimeException {
    public SkuShippingQtyRequestErrorException(String msg) {
        super(msg);
    }
}
