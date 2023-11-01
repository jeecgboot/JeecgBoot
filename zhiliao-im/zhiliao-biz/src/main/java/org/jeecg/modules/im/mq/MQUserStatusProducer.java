package org.jeecg.modules.im.mq;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.boot.starter.rabbitmq.client.RabbitMqClient;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.base.BaseMap;
import org.jeecg.modules.im.base.constant.ConstantMQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 用户状态
 */
@Slf4j
@Component
public class MQUserStatusProducer {

    @Autowired
    RabbitMqClient rabbitMqClient;

    public Result<String> send(JSONObject jsonObject) {
        GsonBuilder builder = new GsonBuilder();
        String msg = builder.create().toJson(jsonObject);
        Map map = new BaseMap();
        map.put("msg", msg);
        rabbitMqClient.sendMessage(ConstantMQ.USER_STATUS,map);
        return Result.OK("发送成功");
    }
}
