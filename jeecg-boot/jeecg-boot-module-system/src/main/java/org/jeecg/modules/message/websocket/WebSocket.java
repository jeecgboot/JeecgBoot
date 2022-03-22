package org.jeecg.modules.message.websocket;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.base.BaseMap;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.modules.redis.client.JeecgRedisClient;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.SpringContextHolder;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.impl.SysUserServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Author scott
 * @Date 2019/11/29 9:41
 * @Description: 此注解相当于设置访问URL
 */
@Component
@Slf4j
@ServerEndpoint("/websocket/{userId}") //此注解相当于设置访问URL
public class WebSocket {

    private Session session;

    private String userId;

    private static final String REDIS_TOPIC_NAME = "socketHandler";

    @Resource
    private JeecgRedisClient jeecgRedisClient;

    private RedisUtil redisUtil;

    private SysUserServiceImpl sysUserService;

    /**
     * 缓存 webSocket连接到单机服务class中（整体方案支持集群）
     */
    private static CopyOnWriteArraySet<WebSocket> webSockets = new CopyOnWriteArraySet<>();
    private static Map<String, Session> sessionPool = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") String userId) {
        try {
            sysUserService = SpringContextHolder.getBean(SysUserServiceImpl.class);
            redisUtil = SpringContextHolder.getBean(RedisUtil.class);
            this.session = session;
            this.userId = userId;

            //设置传输大小
            session.setMaxBinaryMessageBufferSize(1024 * 32);
            session.setMaxTextMessageBufferSize(1024 * 32);

            //验证登录状态-从redis取
            if (redisUtil.hasKey(CacheConstant.SYS_USERS_CACHE + "::" + Optional
                    .ofNullable(sysUserService.getById(userId))
                    .map(SysUser::getUsername)
                    .orElse("null"))) {
                webSockets.add(this);
                sessionPool.put(userId, session);
                log.info("【websocket消息】有新的连接，总数为:" + webSockets.size());
            } else {
                CloseReason closeReason = new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "非法的连接！");
                this.session.close(closeReason);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @OnClose
    public void onClose() {
        try {
            webSockets.remove(this);
            sessionPool.remove(this.userId);
            log.info("【websocket消息】连接断开，总数为:" + webSockets.size());
        } catch (Exception e) {
            log.info(e.getMessage());
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
                synchronized (session) {
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
        /*下面原本的内容还有心跳，
        但现在是通过定时任务 每t时间发送，前端2t时间没收到就主动断开即可
        定时任务就在job里面，同时是没有启动的，要启动一下在界面的-系统监控-定时任务配置一下就好了。
        * */
        log.info("【websocket消息】收到客户端消息:" + message);
        //根据业务来，这里就简单的群发一下
        pushMessage(message);
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
