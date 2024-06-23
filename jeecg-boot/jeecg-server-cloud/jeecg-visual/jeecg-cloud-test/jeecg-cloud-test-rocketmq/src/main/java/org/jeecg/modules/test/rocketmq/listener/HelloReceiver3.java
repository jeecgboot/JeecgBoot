package org.jeecg.modules.test.rocketmq.listener;//package org.jeecg.modules.cloud.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.jeecg.common.base.BaseMap;
import org.jeecg.modules.test.rocketmq.constant.CloudConstant;
import org.springframework.stereotype.Component;

/**
 * 定义接收者（可以定义N个接受者，消息会均匀的发送到N个接收者中）
 *
 * RabbitMq接受者3【我是处理人3】
 * （@RabbitListener声明类方法上，一个类可以多监听多个队列）
 * @author: zyf
 * @date: 2022/04/21
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = CloudConstant.MQ_JEECG_PLACE_ORDER, consumerGroup = "helloReceiver3")
public class HelloReceiver3 implements RocketMQListener<BaseMap> {

    public void onMessage(BaseMap baseMap) {
        log.info("helloReceiver3接收消息：" + baseMap);
    }

}