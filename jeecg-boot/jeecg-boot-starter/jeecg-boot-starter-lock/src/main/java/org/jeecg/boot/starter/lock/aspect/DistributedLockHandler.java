package org.jeecg.boot.starter.lock.aspect;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jeecg.boot.starter.lock.annotation.DistributedLock;
import org.jeecg.boot.starter.lock.client.RedissonLockClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 分布式锁解析器
 *
 * @author zyf
 * @date 2020-11-11
 */
@Slf4j
@Aspect
@Component
public class DistributedLockHandler {

    @Autowired
    RedissonLockClient redissonLock;

    /**
     * 切面环绕通知
     *
     * @param joinPoint
     * @param distributedLock
     * @return Object
     */
    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) {
        log.info("进入RedisLock环绕通知...");
        Object obj = null;

        //获取锁名称
        String lockName = getLockKey(joinPoint, distributedLock);
        if (StringUtils.isEmpty(lockName)) {
            return null;
        }
        //获取超时时间
        int expireSeconds = distributedLock.expireSeconds();
        //等待多久,n秒内获取不到锁，则直接返回
        int waitTime = distributedLock.waitTime();
        Boolean success = redissonLock.tryLock(lockName, waitTime, expireSeconds);
        if (success) {
            log.info("获取锁成功....");
            try {
                obj = joinPoint.proceed();
            } catch (Throwable throwable) {
                log.error("获取锁异常", throwable);
            } finally {
                //释放锁
                redissonLock.unlock(lockName);
                log.info("成功释放锁...");
            }
        } else {
            log.error("获取锁失败", distributedLock.failMsg());
        }
        log.info("结束RedisLock环绕通知...");
        return obj;
    }

    @SneakyThrows
    private String getLockKey(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) {
        String lockKey = distributedLock.lockKey();
        if (StringUtils.isEmpty(lockKey)) {
            int[] fieldIndexs = distributedLock.fieldIndexs();
            String[] fieldNames = distributedLock.fieldNames();
            //目标方法内的所有参数
            Object[] params = joinPoint.getArgs();
            //获取目标包名和类名
            String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
            //获取目标方法名
            String methodName = joinPoint.getSignature().getName();
            // 锁2个及2个以上参数时，fieldNames数量应与fieldIndexs一致
            if (fieldNames.length > 1 && fieldIndexs.length != fieldNames.length) {
                log.error("fieldIndexs与fieldNames数量不一致");
                return null;
            }
            // 数组为空代表锁整个方法
            if (ArrayUtils.isNotEmpty(fieldNames)) {
                StringBuffer lockParamsBuffer = new StringBuffer();
                for (int i = 0; i < fieldIndexs.length; i++) {
                    if (fieldNames.length == 0 || fieldNames[i] == null || fieldNames[i].length() == 0) {
                        lockParamsBuffer.append("." + params[fieldIndexs[i]]);
                    } else {
                        Object lockParamValue = PropertyUtils.getSimpleProperty(params[fieldIndexs[i]], fieldNames[i]);
                        lockParamsBuffer.append("." + lockParamValue);
                    }
                }
                lockKey = declaringTypeName + "." + methodName + lockParamsBuffer.toString();
            }
        }
        return lockKey;
    }
}
