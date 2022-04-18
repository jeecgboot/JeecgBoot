package org.jeecg.modules.message.websocket;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.base.BaseMap;
import org.jeecg.common.constant.CommonSendStatus;
import org.jeecg.common.modules.redis.listener.JeecgRedisListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 监听消息(采用redis发布订阅方式发送消息)
 * @author: jeecg-boot
 */
@Slf4j
@Component
public class SocketHandler implements JeecgRedisListener {

    @Autowired
    private WebSocket webSocket;

    @Override
    public void onMessage(BaseMap map) {
        log.info("【SocketHandler消息】Redis Listerer:" + map.toString());

        String userId = map.get("userId");
        String message = map.get("message");
        if (ObjectUtil.isNotEmpty(userId)) {
            webSocket.pushMessage(userId, message);
            //app端消息推送
            webSocket.pushMessage(userId+CommonSendStatus.APP_SESSION_SUFFIX, message);
        } else {
            webSocket.pushMessage(message);
        }

    }
}