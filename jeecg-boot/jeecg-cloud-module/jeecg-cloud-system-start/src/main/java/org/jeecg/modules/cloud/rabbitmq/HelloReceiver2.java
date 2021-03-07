package org.jeecg.modules.cloud.rabbitmq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.boot.starter.rabbitmq.core.BaseRabbiMqHandler;
import org.jeecg.boot.starter.rabbitmq.listenter.MqListener;
import org.jeecg.common.annotation.RabbitComponent;
import org.jeecg.common.base.BaseMap;
import org.jeecg.modules.cloud.constant.CloudConstant;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 * RabbitMq接受者2
 * （@RabbitListener声明类上，一个类只能监听一个队列）
 */
@Slf4j
@RabbitListener(queues = CloudConstant.MQ_JEECG_PLACE_ORDER)
@RabbitComponent(value = "helloReceiver2")
public class HelloReceiver2 extends BaseRabbiMqHandler<BaseMap> {

    @RabbitHandler
    public void onMessage(BaseMap baseMap, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        super.onMessage(baseMap, deliveryTag, channel, new MqListener<BaseMap>() {
            @Override
            public void handler(BaseMap map, Channel channel) {
                //业务处理
                String orderId = map.get("orderId").toString();
                log.info("MQ Receiver2，orderId : " + orderId);
            }
        });
    }

}