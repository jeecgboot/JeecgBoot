package org.jeecg.modules.business.domain.job;

import java.util.LinkedHashMap;
import java.util.Map;
/**
 * Extends {@code LinkedHashMap} into a fixed sized ordered cache
 * for allocating and tracking limited resources in <em>intervals</em>. It also
 * tracks the allocation rate as a moving average.
 * <p>
 * The {@code IntervalWindow} is created with a cache that consists of the
 * present <em>interval</em> and at least one past <em>interval</em>.
 * As the number of cached <em>intervals</em> exceed the windows size, they are
 * removed and the moving average updated. The allocation method is
 * thread-safe to ensure over allocation is avoided.
 *
 * @author martinb
 * @since 2015-08-17
 */
public class IntervalWindow extends LinkedHashMap<Long, Integer>
{
    /**
     * Serial
     */
    private static final long serialVersionUID = 201508171315L;

    /**
     * The upper execution limit per interval
     */
    private final int INTERVAL_LIMIT;

    /**
     * Number of intervals to track
     */
    private final int INTERVAL_WINDOW_SIZE;

    /**
     * The current interval being filled.
     */
    private long currentInterval = 0;

    /**
     * The moving total of slots used in the window
     */
    private int slotsUsedInWindow = 0;

    /**
     * The minimum interval index that can be considered.
     */
    private long minimumInterval = 0;


    /**
     * Returns the value in the map, or a default.
     * Implemented in JSE8
     *
     * @param key
     * @param defaultValue
     * @return the value
     */
    private final int getOrDefault(Long key,
                                   Integer defaultValue)
    {
        if (get(key) != null)
        {
            return get(key);
        }
        return defaultValue;
    }


    /**
     * Decreases the running total by the number of slots used in the
     * interval leaving the moving window.
     * <p>
     * The value in map is the number of free slots left in the interval.
     *
     * @see java.util.LinkedHashMap#removeEldestEntry(java.util.Map.Entry)
     */
    protected boolean removeEldestEntry(Map.Entry<Long, Integer> eldest)
    {
        if (INTERVAL_WINDOW_SIZE < size())
        {
            slotsUsedInWindow -= (INTERVAL_LIMIT - eldest.getValue());
            minimumInterval = eldest.getKey();
            return true;
        }
        return false;
    }


    /**
     * Tries to allocate a slot in the given interval within the rate limit.
     *
     * @param interval
     *            the interval
     * @return true is a slot was allocated in the interval
     */
    public boolean allocateSlot(long interval)
    {
        boolean isSlotAllocated = false;
        int freeSlots = 0; // Free slots in the interval

        if (interval > minimumInterval)
            /*
             * Cheap range check is OK
             */
        {
            synchronized (this)
                /*
                 * Synchronize allocate on this object to ensure that cache is consistent
                 */
            {
                if ((freeSlots = getOrDefault(interval,
                        INTERVAL_LIMIT)) > 0)
                    /*
                     * There are free slots in this interval to execute this thread
                     * Break out of the loop and return.
                     */
                {
                    if (currentInterval > 0 && currentInterval != interval)
                        /*
                         * Update the running total of slots used in window
                         * with past values only once past the first interval.
                         */
                    {
                        slotsUsedInWindow +=
                                INTERVAL_LIMIT
                                        - getOrDefault(currentInterval,
                                        0);
                    }

                    put(currentInterval = interval, freeSlots - 1); // Maximum is RATE_LIMIT - 1
                    isSlotAllocated = true;
                } // if
            } // synchronized
        } // if

        return isSlotAllocated;
    }


    /**
     * Returns the moving average number of slots allocated for work during
     * the present window but excluding the currently filling interval
     *
     * @return the average number of slots used
     */
    public float getAverageSlotUsed()
    {
        return slotsUsedInWindow / (INTERVAL_WINDOW_SIZE - 1);
    }


    /**
     * Check window size parameters for range.
     *
     * @param intervalWindowSize
     *            the proposed window size
     * @return the window size
     */
    private static int checkWindowSize(int intervalWindowSize)
    {
        if (intervalWindowSize < 2)
        {
            throw new IllegalArgumentException(
                    "Interval Window Size cannot be smaller than 2");
        }
        return intervalWindowSize;

    }


    /**
     * Creates an {@code IntervalWindow} of a window size of two that limits the
     * number of successful allocations in each interval.
     *
     * @param intervalLimit
     *            the maximum number of allocations per interval.
     */
    public IntervalWindow(int intervalLimit)
    {
        super(2, 1);
        INTERVAL_WINDOW_SIZE = 2;
        INTERVAL_LIMIT = intervalLimit;
    }


    /**
     * Creates an {@code IntervalWindow} of a given window size that limits the
     * number of successful allocations in each interval.
     *
     * @param intervalLimit
     *            the maximum number of allocations per interval.
     * @param intervalWindow
     *            the number if intervals to track, must be at least two
     */
    public IntervalWindow(int intervalLimit, int intervalWindow)
    {
        super(checkWindowSize(intervalWindow),
                1);
        INTERVAL_WINDOW_SIZE = intervalWindow;
        INTERVAL_LIMIT = intervalLimit;
    }

}