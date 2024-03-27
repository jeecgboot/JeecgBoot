package org.jeecg.modules.business.domain.api.mabang.stockGetStockQuantity;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.NetworkDataStream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class provide stream of order.
 */
@Slf4j
public class SkuStockStream implements NetworkDataStream<SkuStockData> {

    private final NetworkDataStream<SkuStockResponse> rawStream;

    private List<SkuStockData> skus;

    private int index;

    private boolean began;

    /**
     * Flag of current data is already empty,
     * either currentOrders is null or currentIndex arrives at the end.
     * In both case, we should call next() of the rawStream.
     */
    private boolean empty;


    public SkuStockStream(NetworkDataStream<SkuStockResponse> rawStream) {
        this.rawStream = rawStream;
        skus = null;
        this.index = 0;
        this.empty = true;
        this.began = false;
    }
    @Override
    public List<SkuStockData> all() {
        SkuStockData firstElement = attempt();
        if (firstElement == null) {
            return Collections.emptyList();
        }

        ArrayList<SkuStockData> res = new ArrayList<>();
        res.add(firstElement);
        while (hasNext()) {
            SkuStockData nextSku = next();
            res.add(nextSku);
        }
        return res;
    }
    @Override
    public SkuStockData attempt() {
        began = true;
        log.info("Attempting for the first request");
        SkuStockResponse response = rawStream.attempt();
        if (response == null) {
            log.info("No response");
            return null;
        }
        if (response.getData().isEmpty()) {
            log.info("Response with empty data");
            return null;
        }
        skus = response.getData().toJavaList(SkuStockData.class);
        index = 1;
        log.info("Returned the first element");
        empty = index >= skus.size();
        return skus.get(0);
    }

    @Override
    public boolean hasNext() {
        // the first time
        if (!began) {
            throw new IllegalStateException("Calling hasNext before begin");
        }

        // Current data is not yet empty
        if (index < skus.size()) {
            log.debug("Current order list is not empty yet");
            return true;
        }

        /* Current data is empty */
        this.empty = true;
        log.debug("Current order list is already empty,");
        // and raw stream is empty too.
        if (!rawStream.hasNext()) {
            log.debug("and source stream is empty too, hasNext: false");
            return false;
        }
        // but raw stream not empty.
        else {
            log.debug("but source stream still has data, hasNext: true");
            return true;
        }
    }

    @Override
    public SkuStockData next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Stream is empty!");
        }
        if (empty) {
            skus = this.rawStream.next().getData().toJavaList(SkuStockData.class);
            empty = false;
            index = 0;
        }
        log.debug("Return data at {}", index);
        SkuStockData res = skus.get(index);
        index++;
        return res;
    }
}
