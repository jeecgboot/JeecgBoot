package org.jeecg.modules.business.domain.api.mabang.purDoGetProvider;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.NetworkDataStream;

import java.util.NoSuchElementException;

/**
 * This stream control reception of the response of the mabang provider list API
 */
@Slf4j
public class ProviderRawStream implements NetworkDataStream<ProviderResponse> {
    /**
     * Instance's current request.
     */
    private final ProviderRequestBody toSend;
    /**
     * Response of last request.
     */
    private ProviderResponse currentResponse;

    private boolean began;

    public ProviderRawStream(ProviderRequestBody firstBody) {
        this.toSend = firstBody;
        this.currentResponse = null;
        began = false;
    }

    @Override
    public ProviderResponse attempt() {
        log.info("Begin the first request");
        this.currentResponse = new ProviderRequest(toSend).send();
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
    public boolean hasNext() throws ProviderRequestErrorException {
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
     * @throws ProviderRequestErrorException if request format is not valid.
     */
    @Override
    public ProviderResponse next() throws ProviderRequestErrorException {
        if (!hasNext())
            throw new NoSuchElementException();

        log.info("Sending request for page {}.", toSend.getPage());
        this.currentResponse = new ProviderRequest(toSend).send();
        toSend.nextPage();
        return this.currentResponse;
    }
}
