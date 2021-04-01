package org.jeecg.boot.starter.lock.aspect;

/**
 * @author zyf
 */

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jeecg.boot.starter.lock.annotation.JRepeat;
import org.jeecg.boot.starter.lock.client.RedissonLockClient;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 防止重复提交分布式锁拦截器
 *
 * @author 2019年6月18日
 */
@Aspect
@Component
public class RepeatSubmitAspect extends BaseAspect{

    @Resource
    private RedissonLockClient redissonLockClient;

    /***
     * 定义controller切入点拦截规则，拦截JRepeat注解的业务方法
     */
    @Pointcut("@annotation(jRepeat)")
    public void pointCut(JRepeat jRepeat) {
    }

    /**
     * AOP分布式锁拦截
     *
     * @param joinPoint
     * @return
     * @throws Exception
     */
    @Around("pointCut(jRepeat)")
    public Object repeatSubmit(ProceedingJoinPoint joinPoint,JRepeat jRepeat) throws Throwable {
        String[] parameterNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(((MethodSignature) joinPoint.getSignature()).getMethod());
        if (Objects.nonNull(jRepeat)) {
            // 获取参数
            Object[] args = joinPoint.getArgs();
            // 进行一些参数的处理，比如获取订单号，操作人id等
            StringBuffer lockKeyBuffer = new StringBuffer();
            String key =getValueBySpEL(jRepeat.lockKey(), parameterNames, args,"RepeatSubmit").get(0);
            // 公平加锁，lockTime后锁自动释放
            boolean isLocked = false;
            try {
                isLocked = redissonLockClient.fairLock(key, TimeUnit.SECONDS, jRepeat.lockTime());
                // 如果成功获取到锁就继续执行
                if (isLocked) {
                    // 执行进程
                    return joinPoint.proceed();
                } else {
                    // 未获取到锁
                    throw new Exception("请勿重复提交");
                }
            } finally {
                // 如果锁还存在，在方法执行完成后，释放锁
                if (isLocked) {
                    redissonLockClient.unlock(key);
                }
            }
        }

        return joinPoint.proceed();
    }


}
