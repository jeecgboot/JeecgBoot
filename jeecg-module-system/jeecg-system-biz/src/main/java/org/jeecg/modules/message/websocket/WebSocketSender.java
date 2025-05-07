package org.jeecg.modules.message.websocket;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.base.BaseMap;
import org.jeecg.common.modules.redis.client.JeecgRedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * WebSocket 发送器工具类（使用 redis 推送）
 */
@Component
public class WebSocketSender {

    private static JeecgRedisClient jeecgRedisClient;

    @Autowired
    public void setRedisClient(JeecgRedisClient client) {
        WebSocketSender.jeecgRedisClient = client;
    }

    public static void sendToUser(String userId, String message) {
        BaseMap baseMap = new BaseMap();
        baseMap.put("userId", userId);
        baseMap.put("message", message);
        jeecgRedisClient.sendMessage(WebSocket.REDIS_TOPIC_NAME, baseMap);
    }

    public static void sendJsonToUser(String userId, String cmd, String msgTxt) {
        JSONObject json = new JSONObject();
        json.put("cmd", cmd);
        json.put("msgTxt", msgTxt);
        sendToUser(userId, json.toJSONString());
    }
}
