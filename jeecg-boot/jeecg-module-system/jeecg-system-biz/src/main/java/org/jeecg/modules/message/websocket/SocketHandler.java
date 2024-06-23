package org.jeecg.modules.message.websocket;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.base.BaseMap;
import org.jeecg.common.constant.CommonSendStatus;
import org.jeecg.common.modules.redis.listener.JeecgRedisListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 监听消息(通过redis发布订阅，推送消息)
 * 此方案：解决集群部署的问题，多实例节点（也就是发送消息端先发送消息到redis中，每个服务节点收到redis消息，再触发具体的ws推送）
 * @author: jeecg-boot
 */
@Slf4j
@Component(WebSocket.REDIS_TOPIC_NAME)
public class SocketHandler implements JeecgRedisListener {

    @Autowired
    private WebSocket webSocket;

    @Override
    public void onMessage(BaseMap map) {
        log.debug("【Redis发布订阅模式】redis Listener: {}，参数：{}",WebSocket.REDIS_TOPIC_NAME, map.toString());

        String userId = map.get("userId");
        String message = map.get("message");
        if (ObjectUtil.isNotEmpty(userId)) {
            //pc端消息推送具体人
            webSocket.pushMessage(userId, message);
            //app端消息推送具体人
            webSocket.pushMessage(userId+CommonSendStatus.APP_SESSION_SUFFIX, message);
        } else {
            //推送全部
            webSocket.pushMessage(message);
        }

    }
}