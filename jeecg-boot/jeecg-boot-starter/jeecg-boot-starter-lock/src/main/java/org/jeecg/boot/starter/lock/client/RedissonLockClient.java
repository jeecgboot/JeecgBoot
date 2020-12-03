package org.jeecg.boot.starter.lock.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.boot.starter.lock.core.RedissonManager;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁实现基于Redisson
 *
 * @author zyf
 * @date 2020-11-11
 */
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RedissonLockClient {

    RedissonManager redissonManager;

    /**
     * 获取锁
     */
    public RLock getLock(String lockKey) {
        return redissonManager.getRedisson().getLock(lockKey);
    }

    /**
     * 加锁操作
     *
     * @return boolean
     */
    public boolean tryLock(String lockName, long expireSeconds) {
        return tryLock(lockName, 0, expireSeconds);
    }


    /**
     * 加锁操作
     *
     * @return boolean
     */
    public boolean tryLock(String lockName, long waitTime, long expireSeconds) {
        RLock rLock = getLock(lockName);
        boolean getLock = false;
        try {
            getLock = rLock.tryLock(waitTime, expireSeconds, TimeUnit.SECONDS);
            if (getLock) {
                log.info("获取锁成功,lockName={}", lockName);
            } else {
                log.info("获取锁失败,lockName={}", lockName);
            }
        } catch (InterruptedException e) {
            log.error("获取式锁异常，lockName=" + lockName, e);
            getLock = false;
        }
        return getLock;
    }

    /**
     * 锁lockKey
     *
     * @param lockKey
     * @return
     */
    public RLock lock(String lockKey) {
        RLock lock = getLock(lockKey);
        lock.lock();
        return lock;
    }

    /**
     * 锁lockKey
     *
     * @param lockKey
     * @param leaseTime
     * @return
     */
    public RLock lock(String lockKey, long leaseTime) {
        RLock lock = getLock(lockKey);
        lock.lock(leaseTime, TimeUnit.SECONDS);
        return lock;
    }


    /**
     * 解锁
     *
     * @param lockName 锁名称
     */
    public void unlock(String lockName) {
        redissonManager.getRedisson().getLock(lockName).unlock();
    }


}
