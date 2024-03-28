package org.jeecg.modules.business.domain.api.mabang.stockGetStockQuantity;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.NetworkDataStream;

import java.util.NoSuchElementException;

/**
 * This stream control reception of the response of the mabang order list API
 */
@Slf4j
public class SkuStockRawStream implements NetworkDataStream<SkuStockResponse> {
    /**
     * Instance's current request.
     */
    private final SkuStockRequestBody toSend;
    /**
     * Response of last request.
     */
    private SkuStockResponse currentResponse;

    private boolean began;

    public SkuStockRawStream(SkuStockRequestBody firstBody) {
        this.toSend = firstBody;
        this.currentResponse = null;
        began = false;
    }

    @Override
    public SkuStockResponse attempt() {
        log.info("Begin the first request");
        this.currentResponse = new SkuStockRequest(toSend).send();
        if (currentResponse.getData().isEmpty()) {
            return null;
        }
        began = true;
        toSend.nextPage();
        return currentResponse;
    }

    /**
     * Check whether there are still order left, network communication is done here.
     *
     * @return true if there are, otherwise false.
     */
    @Override
    public boolean hasNext() {
        if (!began) {
            throw new IllegalStateException("Calling hasNext before begin");
        }
        // no page left, false
        log.info("No page left, end");
        return false;
    }

    /**
     * Get next Order.
     *
     * @return next order.
     * @throws NoSuchElementException         if data is already empty.
     */
    @Override
    public SkuStockResponse next() {
        if (!hasNext())
            throw new NoSuchElementException();

        log.info("Sending request for page {}/{}.", toSend.getPage(), toSend.getTotal() == null ? "?" : toSend.getTotalPages());
        this.currentResponse = new SkuStockRequest(toSend).send();
        toSend.nextPage();
        return this.currentResponse;
    }
}
