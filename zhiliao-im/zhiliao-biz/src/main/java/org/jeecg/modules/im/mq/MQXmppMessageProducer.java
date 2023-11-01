package org.jeecg.modules.im.mq;

import com.google.gson.GsonBuilder;
import org.jeecg.boot.starter.rabbitmq.client.RabbitMqClient;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.base.BaseMap;
import org.jeecg.modules.im.base.xmpp.MessageBean;
import org.jeecg.modules.im.base.constant.ConstantMQ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * XMPP消息生产者
 */
@Slf4j
@Component
public class MQXmppMessageProducer {
    @Autowired
    private RabbitMqClient rabbitMqClient;

    public Result<String> send(MessageBean messageBean) {
        GsonBuilder builder = new GsonBuilder();
        String msg = builder.create().toJson(messageBean);
        Map map = new BaseMap();
        map.put("msg", msg);
        rabbitMqClient.sendMessage(ConstantMQ.XMPP_MESSAGE,map);
        return Result.OK("发送成功");
    }
}