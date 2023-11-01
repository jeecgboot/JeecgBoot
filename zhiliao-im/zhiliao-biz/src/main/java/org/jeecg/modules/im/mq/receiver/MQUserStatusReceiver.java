package org.jeecg.modules.im.mq.receiver;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.boot.starter.rabbitmq.core.BaseRabbiMqHandler;
import org.jeecg.boot.starter.rabbitmq.listenter.MqListener;
import org.jeecg.common.annotation.RabbitComponent;
import org.jeecg.modules.im.base.constant.ConstantMQ;
import org.jeecg.modules.im.service.UserService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

import javax.annotation.Resource;

/**
 * 用户状态消费者
 * （@RabbitListener声明类上，一个类只能监听一个队列）
 */
@Slf4j
@RabbitListener(queues = ConstantMQ.USER_STATUS)
@RabbitComponent(value = "mqUserStatusReceiver")
public class MQUserStatusReceiver extends BaseRabbiMqHandler<String> {
    @Resource
    private UserService userService;

    @RabbitHandler
    public void onMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        super.onMessage(message, deliveryTag, channel, new MqListener<String>() {
            @Override
            public void handler(String message, Channel channel) {
                try {
                    //业务处理
                    log.info("新的用户状态, " + message);
                    String[] strs = StringUtils.split(message, ":");
                    Integer id = null;
                    try{
                        id = Integer.parseInt(strs[0]);
                    }catch (Exception e){

                    }
                    if(id!=null){
                        log.info("用户状态更新：id={}，status={}，resource={}", strs[0], strs[1], strs[2]);
                        if ("1".equals(strs[1])) {
                            userService.handleLogin(id, strs[2]);
                        } else {
                            userService.closeConnection(id, strs[2],"1".equals(strs[3]));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //是否需要将消息放回队列？
                    log.error("消费用戶状态消息异常：", e);
                }
            }
        });
    }


}