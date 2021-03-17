package org.jeecg.boot.starter.lock.client;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁实现基于Redisson
 *
 * @author zyf
 * @date 2020-11-11
 */
@Slf4j
@Component
public class RedissonLockClient {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取锁
     */
    public RLock getLock(String lockKey) {
        return redissonClient.getLock(lockKey);
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


    public boolean fairLock(String lockKey, TimeUnit unit, int leaseTime) {
        RLock fairLock = redissonClient.getFairLock(lockKey);
        try {
            boolean existKey = existKey(lockKey);
            // 已经存在了，就直接返回
            if (existKey) {
                return false;
            }
            return fairLock.tryLock(3, leaseTime, unit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existKey(String key) {
        return redisTemplate.hasKey(key);
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
        try {
            redissonClient.getLock(lockName).unlock();
        } catch (Exception e) {
            log.error("解锁异常，lockName=" + lockName, e);
        }
    }


}
