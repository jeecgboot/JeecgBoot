package org.jeecg.boot.starter.lock.aspect;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.jeecg.boot.starter.lock.annotation.JLock;
import org.jeecg.boot.starter.lock.enums.LockModel;
import org.redisson.RedissonMultiLock;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * 分布式锁解析器
 *
 * @author zyf
 * @date 2020-11-11
 */
@Slf4j
@Aspect
@Component
public class DistributedLockHandler extends BaseAspect{


    @Autowired(required = false)
    private RedissonClient redissonClient;

    /**
     * 切面环绕通知
     *
     * @param joinPoint
     * @param jLock
     * @return Object
     */
    @SneakyThrows
    @Around("@annotation(jLock)")
    public Object around(ProceedingJoinPoint joinPoint, JLock jLock) {
        Object obj = null;
        log.info("进入RedisLock环绕通知...");
        RLock rLock = getLock(joinPoint, jLock);
        boolean res = false;
        //获取超时时间
        long expireSeconds = jLock.expireSeconds();
        //等待多久,n秒内获取不到锁，则直接返回
        long waitTime = jLock.waitTime();
        //执行aop
        if (rLock != null) {
            try {
                if (waitTime == -1) {
                    res = true;
                    //一直等待加锁
                    rLock.lock(expireSeconds, TimeUnit.MILLISECONDS);
                } else {
                    res = rLock.tryLock(waitTime, expireSeconds, TimeUnit.MILLISECONDS);
                }
                if (res) {
                    obj = joinPoint.proceed();
                } else {
                    log.error("获取锁异常");
                }
            } finally {
                if (res) {
                    rLock.unlock();
                }
            }
        }
        log.info("结束RedisLock环绕通知...");
        return obj;
    }

    @SneakyThrows
    private RLock getLock(ProceedingJoinPoint joinPoint, JLock jLock) {
        String[] keys = jLock.lockKey();
        if (keys.length == 0) {
            throw new RuntimeException("keys不能为空");
        }
        String[] parameterNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(((MethodSignature) joinPoint.getSignature()).getMethod());
        Object[] args = joinPoint.getArgs();

        LockModel lockModel = jLock.lockModel();
        if (!lockModel.equals(LockModel.MULTIPLE) && !lockModel.equals(LockModel.REDLOCK) && keys.length > 1) {
            throw new RuntimeException("参数有多个,锁模式为->" + lockModel.name() + ".无法锁定");
        }
        RLock rLock = null;
        String keyConstant = jLock.keyConstant();
        if (lockModel.equals(LockModel.AUTO)) {
            if (keys.length > 1) {
                lockModel = LockModel.REDLOCK;
            } else {
                lockModel = LockModel.REENTRANT;
            }
        }
        switch (lockModel) {
            case FAIR:
                rLock = redissonClient.getFairLock(getValueBySpEL(keys[0], parameterNames, args, keyConstant).get(0));
                break;
            case REDLOCK:
                List<RLock> rLocks = new ArrayList<>();
                for (String key : keys) {
                    List<String> valueBySpEL = getValueBySpEL(key, parameterNames, args, keyConstant);
                    for (String s : valueBySpEL) {
                        rLocks.add(redissonClient.getLock(s));
                    }
                }
                RLock[] locks = new RLock[rLocks.size()];
                int index = 0;
                for (RLock r : rLocks) {
                    locks[index++] = r;
                }
                rLock = new RedissonRedLock(locks);
                break;
            case MULTIPLE:
                rLocks = new ArrayList<>();

                for (String key : keys) {
                    List<String> valueBySpEL = getValueBySpEL(key, parameterNames, args, keyConstant);
                    for (String s : valueBySpEL) {
                        rLocks.add(redissonClient.getLock(s));
                    }
                }
                locks = new RLock[rLocks.size()];
                index = 0;
                for (RLock r : rLocks) {
                    locks[index++] = r;
                }
                rLock = new RedissonMultiLock(locks);
                break;
            case REENTRANT:
                List<String> valueBySpEL = getValueBySpEL(keys[0], parameterNames, args, keyConstant);
                //如果spel表达式是数组或者LIST 则使用红锁
                if (valueBySpEL.size() == 1) {
                    rLock = redissonClient.getLock(valueBySpEL.get(0));
                } else {
                    locks = new RLock[valueBySpEL.size()];
                    index = 0;
                    for (String s : valueBySpEL) {
                        locks[index++] = redissonClient.getLock(s);
                    }
                    rLock = new RedissonRedLock(locks);
                }
                break;
            case READ:
                rLock = redissonClient.getReadWriteLock(getValueBySpEL(keys[0], parameterNames, args, keyConstant).get(0)).readLock();
                break;
            case WRITE:
                rLock = redissonClient.getReadWriteLock(getValueBySpEL(keys[0], parameterNames, args, keyConstant).get(0)).writeLock();
                break;
        }
        return rLock;
    }
}
