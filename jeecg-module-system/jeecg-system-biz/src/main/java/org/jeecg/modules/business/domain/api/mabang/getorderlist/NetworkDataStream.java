package org.jeecg.modules.business.domain.api.mabang.getorderlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * To use class that implements this interface, one should call sequentially
 * 1. begin()
 * 2. hasNext()
 * 3. next()
 * and repeat step 2 and 3 util all the data is work out.
 *
 * @param <E> type of the data.
 */
public interface NetworkDataStream<E> {
    /**
     * Start the first query and return the data if it is available.
     *
     * @return the if there are data to read, otherwise null.
     */
    E attempt();

    /**
     * To see whether there are still data available.
     * <p>
     * The implementation should guarantee that multiple call on this function before a calling on next()
     * return the same result and the state of the class remains the same.
     *
     * @return true if there are, false otherwise.
     */
    boolean hasNext();

    /**
     * Read the data.
     *
     * @return the data
     */
    E next();

    /**
     * Retrieve all data by only one call, this is a shortcut.
     *
     * @return all data in a list, in case of empty data, "Collections.emptyList()" will be returned.
     */
    default List<E> all() {
        E firstElement = attempt();
        if (firstElement == null) {
            return Collections.emptyList();
        }

        ArrayList<E> res = new ArrayList<>();
        res.add(firstElement);
        while (hasNext()) {
            res.add(next());
        }
        return res;
    }
}
