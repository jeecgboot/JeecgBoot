package org.jeecg.modules.business.domain.job;

import static java.lang.Integer.MAX_VALUE;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A {@code ExecutorService} that throttles the amount of work released
 * for execution per time period.
 *
 * @author martinb
 * @since 2015-08-17
 */
public class ThrottlingExecutorService extends ThreadPoolExecutor {
    /**
     * The interval window cache
     */
    private final IntervalWindow INTERVAL_WINDOW;

    /**
     * The rate limit interval in milliseconds
     */
    private final long RATE_INTERVAL_MILLISECONDS;


    /**
     * Caching, dynamic rate limiting {@code ExecutorService}
     *
     * @param rateLimit
     *            the rate limit
     * @param unit
     *            the rate limit time unit
     */
    private ThrottlingExecutorService(
            int rateLimit,
            TimeUnit unit)
    {
        /*
         * Create a CACHING ExecutorService
         */
        super(0, MAX_VALUE,
                60L, SECONDS,
                new SynchronousQueue<Runnable>());

        INTERVAL_WINDOW = new IntervalWindow(rateLimit);
        RATE_INTERVAL_MILLISECONDS = unit.toMillis(1);
    }


    /**
     * Fixed size rate limiting {@code ExecutorService}
     *
     * @param parallelism
     * @param rateLimit
     * @param unit
     */
    private ThrottlingExecutorService(int parallelism,
                                      int rateLimit,
                                      TimeUnit unit)
    {
        /*
         * Create a FIXED ExecutorService
         */
        super(parallelism, parallelism, 0, MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());

        INTERVAL_WINDOW = new IntervalWindow(rateLimit);
        RATE_INTERVAL_MILLISECONDS = unit.toMillis(1);
    }


    /**
     * Produces a throttling ExecutorService
     * <p>
     * Evaluates the parameters and generates an appropriate ExecutorService
     *
     * @param parallelism
     *            how many threads
     * @param rateLimit
     *            work per time unit
     * @param unit
     *            the time unit
     * @return the ExecutorService
     */
    public static ExecutorService createExecutorService(int parallelism,
                                                        int rateLimit,
                                                        TimeUnit unit)
    {
        if (parallelism > 0)
            /*
             * Fixed ExecutorService
             */
        {
            return new ThrottlingExecutorService(parallelism,
                    rateLimit > 0 ? rateLimit : MAX_VALUE,
                    unit);
        }
        else
            /*
             * Caching ExecutorService
             */
        {
            return new ThrottlingExecutorService(
                    rateLimit > 0 ? rateLimit : MAX_VALUE,
                    unit);
        }
    }


    /**
     * Throttles the execution before executing the task to achieve the desired
     * rate.
     *
     * @see java.util.concurrent.ThreadPoolExecutor#execute(java.lang.Runnable)
     */
    @Override
    public void execute(final Runnable task)
    {
        throttle();
        super.execute(task);
    }


    /**
     * Throttles if the thread can not be allocated in the current time
     * interval,
     * forcing it to wait to the next interval.
     */
    private void throttle()
    {
        long interval = 0; // The interval index
        long milliTime = System.currentTimeMillis(); // The current time
        long offset = milliTime % RATE_INTERVAL_MILLISECONDS; // Interval offset

        while (!INTERVAL_WINDOW.allocateSlot(
                (interval = (milliTime + offset) / RATE_INTERVAL_MILLISECONDS)))
            /*
             * Cannot allocate free slots in this interval.
             * Calculate the required pause to get to the next interval and sleep
             */
        {
            int pause = (int) (((interval + 1)
                    * RATE_INTERVAL_MILLISECONDS)
                    - milliTime + offset);
            try
                /*
                 * Try to sleep the thread for a pause of nanoseconds
                 */
            {
                Thread.sleep(pause);
            }
            catch (InterruptedException e)
            {
            }

            milliTime = System.currentTimeMillis();

        } // while
    }
}
