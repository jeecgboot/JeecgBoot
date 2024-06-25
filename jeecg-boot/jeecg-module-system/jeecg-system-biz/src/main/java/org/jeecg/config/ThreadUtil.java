package org.jeecg.config;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class ThreadUtil {
    private static final ExecutorService cachedThreadPool = new ThreadPoolExecutor(20, 200, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

    /**
     * 静态方法异步调用
     *
     * @param method
     */
    public static void execute(Runnable method) {
        log.info("ThreadUtil -- execute: {}", Thread.currentThread().getName());
        cachedThreadPool.execute(method);
    }

    public static Future<?> submit(Runnable method) {
        log.info("ThreadUtil -- execute: {}", Thread.currentThread().getName());
        return cachedThreadPool.submit(method);
    }

    public static <T> Future<T> submit(Runnable method, T result) {
        log.info("ThreadUtil -- execute: {}", Thread.currentThread().getName());
        return cachedThreadPool.submit(method, result);
    }

    public static <T> Future<T> submit(Callable<T> method) {
        log.info("ThreadUtil -- execute: {}", Thread.currentThread().getName());
        return cachedThreadPool.submit(method);
    }
}
