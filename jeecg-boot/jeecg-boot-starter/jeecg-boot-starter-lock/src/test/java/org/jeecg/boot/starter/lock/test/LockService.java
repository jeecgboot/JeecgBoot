package org.jeecg.boot.starter.lock.test;

import org.jeecg.boot.starter.lock.annotation.JLock;
import org.jeecg.boot.starter.lock.annotation.JRepeat;
import org.jeecg.boot.starter.lock.annotation.LockConstant;
import org.jeecg.boot.starter.lock.client.RedissonLockClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LockService {

    @Resource
    private RedissonLockClient redissonLockClient;

    int n = 10;

    /**
     * 模拟秒杀(注解方式)
     */
    @JLock(lockKey = "#productId", expireSeconds = 5000)
    public void seckill(String productId) {
        if (n <= 0) {
            System.out.println("活动已结束,请下次再来");
            return;
        }
        System.out.println(Thread.currentThread().getName() + ":秒杀到了商品");
        System.out.println(--n);
    }

    /**
     * 模拟秒杀(编程方式)
     */
    public void seckill2(String productId) {
        redissonLockClient.tryLock(productId, 5000);
        if (n <= 0) {
            System.out.println("活动已结束,请下次再来");
            return;
        }
        System.out.println(Thread.currentThread().getName() + ":秒杀到了商品");
        System.out.println(--n);
        redissonLockClient.unlock(productId);
    }


    /**
     * 测试重复提交
     */
    @JRepeat(lockKey = "#name", lockTime = 5)
    public void reSubmit(String name) {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("提交成功" + name);
    }
}
