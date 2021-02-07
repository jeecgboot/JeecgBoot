package org.jeecg.modules.message.websocket;

import cn.hutool.core.util.ObjectUtil;
import org.jeecg.boot.starter.redis.listener.JeecgRedisListerer;
import org.jeecg.common.base.BaseMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 监听消息(采用redis发布订阅方式发送消息)
 */
@Component
public class SocketHandler implements JeecgRedisListerer {

    @Autowired
    private WebSocket webSocket;

    @Override
    public void onMessage(BaseMap map) {
        String userId = map.get("userId");
        String message = map.get("message");
        if (ObjectUtil.isNotEmpty(userId)) {
            webSocket.pushMessage(userId, message);
        } else {
            webSocket.pushMessage(message);
        }

    }
}