package org.jeecg.cloud.demo.lock;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.boot.starter.lock.annotation.DistributedLock;
import org.jeecg.boot.starter.lock.client.RedissonLockClient;
import org.jeecg.boot.starter.rabbitmq.client.RabbitMqClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 分布式锁测试demo
 */
@Slf4j
@Component
public class DemoLockTest {
    @Autowired
    RedissonLockClient redissonLock;

    @Autowired
    RabbitMqClient rabbitMqClient;

    /**
     * 注解方式测试分布式锁
     */
    //@Scheduled(cron = "0/5 * * * * ?")
    @DistributedLock(lockKey = "redis-lock", expireSeconds = 11)
    public void execute() throws InterruptedException {
        log.info("执行execute任务开始，休眠三秒");
        Thread.sleep(3000);
        System.out.println("=======================业务逻辑1=============================");
        Map map = new HashMap();
        map.put("orderId", "12345");
        rabbitMqClient.sendMessage("test", map);
        //延迟10秒发送
        map.put("orderId", "555555");
        rabbitMqClient.sendMessage("test", map, 10000);
        log.info("execute任务结束，休眠三秒");
    }

    /**
     * 编码方式测试分布式锁
     */
    //@Scheduled(cron = "0/10 * * * * ?")
    public void execute2() throws InterruptedException {
        if (redissonLock.tryLock("redisson", -1, 10)) {
            log.info("执行任务execute2开始，休眠三秒");
            Thread.sleep(3000);
            System.out.println("=======================业务逻辑2=============================");
            log.info("定时execute2结束，休眠三秒");
            redissonLock.unlock("redisson");
        } else {
            log.info("execute2获取锁失败");
        }
    }


}
