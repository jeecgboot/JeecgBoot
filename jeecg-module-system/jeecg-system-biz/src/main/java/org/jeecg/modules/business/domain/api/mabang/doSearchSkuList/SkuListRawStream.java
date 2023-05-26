package org.jeecg.modules.business.domain.api.mabang.doSearchSkuList;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.mabang.doSearchSkuList.*;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.NetworkDataStream;

import java.util.NoSuchElementException;

/**
 * This stream control reception of the response of the mabang order list API
 */
@Slf4j
public class SkuListRawStream implements NetworkDataStream<SkuListResponse> {
    /**
     * Instance's current request.
     */
    private final SkuListRequestBody toSend;
    /**
     * Response of last request.
     */
    private SkuListResponse currentResponse;

    private boolean began;

    public SkuListRawStream(SkuListRequestBody firstBody) {
        this.toSend = firstBody;
        this.currentResponse = null;
        began = false;
    }

    @Override
    public SkuListResponse attempt() {
        log.info("Begin the first request");
        this.currentResponse = new SkuListRequest(toSend).send();
        if (currentResponse.getDataCount() == 0) {
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
    public boolean hasNext() throws SkuListRequestErrorException {
        if (!began) {
            throw new IllegalStateException("Calling hasNext before begin");
        }
        // still has page left, true
        if (toSend.getPage() <= currentResponse.getTotalPage()) {
            log.info("page: {}/{}, has next", toSend.getPage(), currentResponse.getTotalPage());
            return true;
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
     * @throws SkuListRequestErrorException if request format is not valid.
     */
    @Override
    public SkuListResponse next() throws SkuListRequestErrorException {
        if (!hasNext())
            throw new NoSuchElementException();

        log.info("Sending request for page {}.", toSend.getPage());
        this.currentResponse = new SkuListRequest(toSend).send();
        toSend.nextPage();
        return this.currentResponse;
    }
}
