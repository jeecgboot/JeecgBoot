package org.jeecg.modules.business.domain.api.mabang.doSearchSkuList;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.NetworkDataStream;
import org.jeecg.modules.business.domain.api.mabang.doSearchSkuList.SkuData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class provide stream of order.
 */
@Slf4j
public class SkuListStream implements NetworkDataStream<SkuData> {

    private final NetworkDataStream<SkuListResponse> rawStream;

    private List<SkuData> skus;

    private int index;

    private boolean began;

    /**
     * Flag of current data is already empty,
     * either currentOrders is null or currentIndex arrives at the end.
     * In both case, we should call next() of the rawStream.
     */
    private boolean empty;


    public SkuListStream(NetworkDataStream<SkuListResponse> rawStream) {
        this.rawStream = rawStream;
        skus = null;
        this.index = 0;
        this.empty = true;
        this.began = false;
    }
    @Override
    public List<SkuData> all() {
        SkuData firstElement = attempt();
        if (firstElement == null) {
            return Collections.emptyList();
        }

        ArrayList<SkuData> res = new ArrayList<>();
        if (firstElement.getStatus().equals(SkuStatus.Normal)) {
            res.add(firstElement);
        }
        while (hasNext()) {
            SkuData nextSku = next();
            if(nextSku.getStatus().equals(SkuStatus.Normal)) {
                res.add(nextSku);
            }
        }
        return res;
    }
    @Override
    public SkuData attempt() {
        began = true;
        log.info("Attempting for the first request");
        SkuListResponse response = rawStream.attempt();
        if (response == null) {
            log.info("No response");
            return null;
        }
        if (response.getDataCount() == 0) {
            log.info("Response with empty data");
            return null;
        }
        skus = response.getData().toJavaList(SkuData.class);
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
    public SkuData next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Stream is empty!");
        }
        if (empty) {
            skus = this.rawStream.next().getData().toJavaList(SkuData.class);
            empty = false;
            index = 0;
        }
        log.debug("Return data at {}", index);
        SkuData res = skus.get(index);
        index++;
        return res;
    }
}
