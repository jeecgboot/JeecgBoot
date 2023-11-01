package org.jeecg.modules.im.mq.receiver;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.boot.starter.rabbitmq.core.BaseRabbiMqHandler;
import org.jeecg.boot.starter.rabbitmq.listenter.MqListener;
import org.jeecg.common.annotation.RabbitComponent;
import org.jeecg.common.base.BaseMap;
import org.jeecg.modules.im.base.constant.ConstantMQ;
import org.jeecg.modules.im.entity.UserLog;
import org.jeecg.modules.im.service.UserLogService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

import javax.annotation.Resource;

/**
 * 用户日志消费者
 * （@RabbitListener声明类上，一个类只能监听一个队列）
 */
@Slf4j
@RabbitListener(queues = ConstantMQ.USER_LOG)
@RabbitComponent(value = "mqUserLogReceiver")
public class MQUserLogReceiver extends BaseRabbiMqHandler<BaseMap> {
    @Resource
    private UserLogService userLogService;

    private static final Gson gson = new Gson();

    @RabbitHandler
    public void onMessage(BaseMap baseMap, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        super.onMessage(baseMap, deliveryTag, channel, new MqListener<BaseMap>() {
            @Override
            public void handler(BaseMap map, Channel channel) {
                try {
                    //业务处理
                    String message = map.get("msg");
                    log.info("新的用户日志, " + message);
                    UserLog userLog = gson.fromJson(message, UserLog.class);
                    boolean result = userLogService.save(userLog);
                    log.info("消费系统日志：id={}", userLog.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("消费用户日志消息异常：", e);
                }
            }
        });
    }


}