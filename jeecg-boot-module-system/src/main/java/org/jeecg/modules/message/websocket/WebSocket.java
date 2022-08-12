package org.jeecg.modules.message.websocket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.annotation.Resource;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.jeecg.common.base.BaseMap;
import org.jeecg.common.constant.WebsocketConst;
import org.jeecg.common.modules.redis.client.JeecgRedisClient;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author scott
 * @Date 2019/11/29 9:41
 * @Description: 此注解相当于设置访问URL
 */
@Component
@Slf4j
@ServerEndpoint("/websocket/{userId}")
public class WebSocket {

    private Session session;

    /**
     * 用户ID
     */
    private String userId;

    private static final String REDIS_TOPIC_NAME = "socketHandler";

    @Resource
    private JeecgRedisClient jeecgRedisClient;

    /**
     * 缓存 webSocket连接到单机服务class中（整体方案支持集群）
     */
    private static CopyOnWriteArraySet<WebSocket> webSockets = new CopyOnWriteArraySet<>();
    /**
     * 线程安全Map
     */
    private static ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") String userId) {
        try {
            //TODO 通过header中获取token，进行check
            this.session = session;
            this.userId = userId;
            webSockets.add(this);
            sessionPool.put(userId, session);
            log.info("【websocket消息】有新的连接，总数为:" + webSockets.size());
        } catch (Exception e) {
        }
    }

    @OnClose
    public void onClose() {
        try {
            webSockets.remove(this);
            sessionPool.remove(this.userId);
            log.info("【websocket消息】连接断开，总数为:" + webSockets.size());
        } catch (Exception e) {
        }
    }


    /**
     * 服务端推送消息
     *
     * @param userId
     * @param message
     */
    public void pushMessage(String userId, String message) {
        Session session = sessionPool.get(userId);
        if (session != null && session.isOpen()) {
            try {
                //update-begin-author:taoyan date:20211012 for: websocket报错 https://gitee.com/jeecg/jeecg-boot/issues/I4C0MU
                synchronized (session){
                    log.info("【websocket消息】 单点消息:" + message);
                    session.getBasicRemote().sendText(message);
                }
                //update-end-author:taoyan date:20211012 for: websocket报错 https://gitee.com/jeecg/jeecg-boot/issues/I4C0MU
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 服务器端推送消息
     */
    public void pushMessage(String message) {
        try {
            webSockets.forEach(ws -> ws.session.getAsyncRemote().sendText(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnMessage
    public void onMessage(String message) {
        //todo 现在有个定时任务刷，应该去掉
        log.debug("【websocket消息】收到客户端消息:" + message);
        JSONObject obj = new JSONObject();
        //业务类型
        obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_CHECK);
        //消息内容
        obj.put(WebsocketConst.MSG_TXT, "心跳响应");
        //update-begin-author:taoyan date:20220308 for: 消息通知长连接启动心跳机制，后端代码小bug #3473
        for (WebSocket webSocket : webSockets) {
            webSocket.pushMessage(obj.toJSONString());
        }
        //update-end-author:taoyan date:20220308 for: 消息通知长连接启动心跳机制，后端代码小bug #3473
    }

    /**
     * 后台发送消息到redis
     *
     * @param message
     */
    public void sendMessage(String message) {
        log.info("【websocket消息】广播消息:" + message);
        BaseMap baseMap = new BaseMap();
        baseMap.put("userId", "");
        baseMap.put("message", message);
        jeecgRedisClient.sendMessage(REDIS_TOPIC_NAME, baseMap);
    }

    /**
     * 此为单点消息
     *
     * @param userId
     * @param message
     */
    public void sendMessage(String userId, String message) {
        BaseMap baseMap = new BaseMap();
        baseMap.put("userId", userId);
        baseMap.put("message", message);
        jeecgRedisClient.sendMessage(REDIS_TOPIC_NAME, baseMap);
    }

    /**
     * 此为单点消息(多人)
     *
     * @param userIds
     * @param message
     */
    public void sendMessage(String[] userIds, String message) {
        for (String userId : userIds) {
            sendMessage(userId, message);
        }
    }

}