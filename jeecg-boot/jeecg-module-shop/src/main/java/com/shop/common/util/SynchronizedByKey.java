package com.shop.common.util;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * author Panyoujie
 * date   2022-03-23
 * 回调幂等处理
 * 以及并发的支持
 */
@Service
public class SynchronizedByKey implements SynchronizedByKeyService {

    Map<String, ReentrantLock> computeIfAbsent = new ConcurrentHashMap<>();

    /**
     * 主处理方法
     *
     * @param key      传入的订单ID
     * @param runnable
     */
    public void exec(String key, Runnable runnable) {
        ReentrantLock lock4Key = null;
        ReentrantLock reentrantLock;
        do {
            if (lock4Key != null) {
                lock4Key.unlock();
            }
            lock4Key = computeIfAbsent.computeIfAbsent(key, k -> new ReentrantLock());
            lock4Key.lock();
            reentrantLock = computeIfAbsent.get(key);
        } while (reentrantLock == null || lock4Key != reentrantLock);

        try {
            runnable.run();
        } finally {
            if (lock4Key.getQueueLength() == 0) {
                computeIfAbsent.remove(key);
            }
            lock4Key.unlock();
        }
    }
}
