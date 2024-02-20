package org.jeecg.modules.business.domain.api.mabang.purDoGetProvider;

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
public class ProviderStream implements NetworkDataStream<ProviderData> {

    private final ProviderRawStream rawStream;

    private List<ProviderData> providers;

    private int index;

    private boolean began;

    /**
     * Flag of current data is already empty,
     * either currentOrders is null or currentIndex arrives at the end.
     * In both case, we should call next() of the rawStream.
     */
    private boolean empty;


    public ProviderStream(ProviderRawStream rawStream) {
        this.rawStream = rawStream;
        providers = null;
        this.index = 0;
        this.empty = true;
        this.began = false;
    }
    @Override
    public List<ProviderData> all() {
        ProviderData firstElement = attempt();
        if (firstElement == null) {
            return Collections.emptyList();
        }

        ArrayList<ProviderData> res = new ArrayList<>();
        res.add(firstElement);
        System.out.println("firstElement : " + firstElement);
        while (hasNext()) {
            ProviderData nextProvider = next();
            res.add(nextProvider);

        }
        return res;
    }
    @Override
    public ProviderData attempt() {
        began = true;
        log.info("Attempting for the first request");
        ProviderResponse response = rawStream.attempt();
        if (response == null) {
            log.info("No response");
            return null;
        }
        if (response.getDataCount() == 0) {
            log.info("Response with empty data");
            return null;
        }
        providers = response.getData().toJavaList(ProviderData.class);
        index = 1;
        log.info("Returned the first element");
        empty = index >= providers.size();
        return providers.get(0);
    }

    @Override
    public boolean hasNext() {
        // the first time
        if (!began) {
            throw new IllegalStateException("Calling hasNext before begin");
        }

        // Current data is not yet empty
        if (index < providers.size()) {
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
    public ProviderData next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Stream is empty!");
        }
        if (empty) {
            providers = this.rawStream.next().getData().toJavaList(ProviderData.class);
            empty = false;
            index = 0;
        }
        log.debug("Return data at {}", index);
        ProviderData res = providers.get(index);
        index++;
        return res;
    }
}
