package org.jeecg.modules.test.lock;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.boot.starter.lock.annotation.JLock;
import org.jeecg.boot.starter.lock.client.RedissonLockClient;
import org.jeecg.boot.starter.rabbitmq.client.RabbitMqClient;
import org.jeecg.common.base.BaseMap;
import org.jeecg.modules.test.constant.CloudConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 分布式锁测试demo
 * @author: zyf
 * @date: 2022/04/21
 */
@Slf4j
@Component
public class DemoLockTest {
    @Autowired
    RedissonLockClient redissonLock;
//    @Autowired
//    RabbitMqClient rabbitMqClient;

    /**
     * 测试方法：
     *    @Scheduled(cron = "0/5 * * * * ?") 表示每5秒执行一次
     *    @JLock(lockKey = CloudConstant.REDISSON_DEMO_LOCK_KEY1)分布式锁，10秒钟才释放
     *    结果：每10秒钟输出一次 “执行 分布式锁 业务逻辑1” 就说明锁成功了
     *
     * 测试分布式锁【注解方式】
     */
    @Scheduled(cron = "0/5 * * * * ?")
    @JLock(lockKey = CloudConstant.REDISSON_DEMO_LOCK_KEY1)
    public void execute() throws InterruptedException {
        log.info("执行execute任务开始，休眠十秒开始，当前系统时间戳（秒）："+ System.currentTimeMillis()/1000);
        Thread.sleep(10000);
        log.info("========执行 分布式锁 业务逻辑1=============");
//        Map map = new BaseMap();
//        map.put("orderId", "BJ0001");
//        rabbitMqClient.sendMessage(CloudConstant.MQ_JEECG_PLACE_ORDER, map);
//        //延迟10秒发送
//        map.put("orderId", "NJ0002");
//        rabbitMqClient.sendMessage(CloudConstant.MQ_JEECG_PLACE_ORDER, map, 10000);

        log.info("execute任务结束，休眠十秒完成，当前系统时间戳（秒）："+ System.currentTimeMillis()/1000);
    }


    /**
     * 测试分布式锁【编码方式】
     * @Scheduled(cron = "0/5 * * * * ?")
     */
    public void execute2() throws InterruptedException {
        int expireSeconds=6000;
        if (redissonLock.tryLock(CloudConstant.REDISSON_DEMO_LOCK_KEY2, -1, expireSeconds)) {
            log.info("执行任务execute2开始，休眠十秒");
            Thread.sleep(10000);
           log.info("=============业务逻辑2===================");
            log.info("定时execute2结束，休眠十秒");

            redissonLock.unlock(CloudConstant.REDISSON_DEMO_LOCK_KEY2);
        } else {
            log.info("execute2获取锁失败");
        }
    }

}
