package org.jeecg.modules.demo.mock.vxe.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.VXESocketConst;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * vxe WebSocket，用于实现实时无痕刷新的功能
 */
@Slf4j
@Component
@ServerEndpoint("/vxeSocket/{userId}/{pageId}")
public class VXESocket {

    /**
     * 当前 session
     */
    private Session session;
    /**
     * 当前用户id
     */
    private String userId;
    /**
     * 页面id，用于标识同一用户，不同页面的数据
     */
    private String pageId;
    /**
     * 当前socket唯一id
     */
    private String socketId;

    /**
     * 用户连接池，包含单个用户的所有socket连接；
     * 因为一个用户可能打开多个页面，多个页面就会有多个连接；
     * key是userId，value是Map对象；子Map的key是pageId，value是VXESocket对象
     */
    private static Map<String, Map<String, VXESocket>> userPool = new HashMap<>();
    /**
     * 连接池，包含所有WebSocket连接；
     * key是socketId，value是VXESocket对象
     */
    private static Map<String, VXESocket> socketPool = new HashMap<>();

    /**
     * 获取某个用户所有的页面
     */
    public static Map<String, VXESocket> getUserPool(String userId) {
        return userPool.computeIfAbsent(userId, k -> new HashMap<>());
    }

    /**
     * 向当前用户发送消息
     *
     * @param message 消息内容
     */
    public void sendMessage(String message) {
        try {
            this.session.getAsyncRemote().sendText(message);
        } catch (Exception e) {
            log.error("【vxeSocket】消息发送失败：" + e.getMessage());
        }
    }

    /**
     * 封装消息json
     *
     * @param data 消息内容
     */
    public static String packageMessage(String type, Object data) {
        JSONObject message = new JSONObject();
        message.put(VXESocketConst.TYPE, type);
        message.put(VXESocketConst.DATA, data);
        return message.toJSONString();
    }

    /**
     * 向指定用户的所有页面发送消息
     *
     * @param userId  接收消息的用户ID
     * @param message 消息内容
     */
    public static void sendMessageTo(String userId, String message) {
        Collection<VXESocket> values = getUserPool(userId).values();
        if (values.size() > 0) {
            for (VXESocket socketItem : values) {
                socketItem.sendMessage(message);
            }
        } else {
            log.warn("【vxeSocket】消息发送失败：userId\"" + userId + "\"不存在或未在线！");
        }
    }

    /**
     * 向指定用户的指定页面发送消息
     *
     * @param userId  接收消息的用户ID
     * @param message 消息内容
     */
    public static void sendMessageTo(String userId, String pageId, String message) {
        VXESocket socketItem = getUserPool(userId).get(pageId);
        if (socketItem != null) {
            socketItem.sendMessage(message);
        } else {
            log.warn("【vxeSocket】消息发送失败：userId\"" + userId + "\"的pageId\"" + pageId + "\"不存在或未在线！");
        }
    }

    /**
     * 向多个用户的所有页面发送消息
     *
     * @param userIds 接收消息的用户ID数组
     * @param message 消息内容
     */
    public static void sendMessageTo(String[] userIds, String message) {
        for (String userId : userIds) {
            VXESocket.sendMessageTo(userId, message);
        }
    }

    /**
     * 向所有用户的所有页面发送消息
     *
     * @param message 消息内容
     */
    public static void sendMessageToAll(String message) {
        for (VXESocket socketItem : socketPool.values()) {
            socketItem.sendMessage(message);
        }
    }

    /**
     * websocket 开启连接
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId, @PathParam("pageId") String pageId) {
        try {
            this.userId = userId;
            this.pageId = pageId;
            this.socketId = userId + pageId;
            this.session = session;

            socketPool.put(this.socketId, this);
            getUserPool(userId).put(this.pageId, this);

            log.info("【vxeSocket】有新的连接，总数为:" + socketPool.size());
        } catch (Exception ignored) {
        }
    }

    /**
     * websocket 断开连接
     */
    @OnClose
    public void onClose() {
        try {
            socketPool.remove(this.socketId);
            getUserPool(this.userId).remove(this.pageId);

            log.info("【vxeSocket】连接断开，总数为:" + socketPool.size());
        } catch (Exception ignored) {
        }
    }

    /**
     * websocket 收到消息
     */
    @OnMessage
    public void onMessage(String message) {
        // log.info("【vxeSocket】onMessage:" + message);
        JSONObject json;
        try {
            json = JSON.parseObject(message);
        } catch (Exception e) {
            log.warn("【vxeSocket】收到不合法的消息:" + message);
            return;
        }
        String type = json.getString(VXESocketConst.TYPE);
        switch (type) {
            // 心跳检测
            case VXESocketConst.TYPE_HB:
                this.sendMessage(VXESocket.packageMessage(type, true));
                break;
            // 更新form数据
            case VXESocketConst.TYPE_UVT:
                this.handleUpdateForm(json);
                break;
            default:
                log.warn("【vxeSocket】收到不识别的消息类型:" + type);
                break;
        }


    }

    /**
     * 处理 UpdateForm 事件
     */
    private void handleUpdateForm(JSONObject json) {
        // 将事件转发给所有人
        JSONObject data = json.getJSONObject(VXESocketConst.DATA);
        VXESocket.sendMessageToAll(VXESocket.packageMessage(VXESocketConst.TYPE_UVT, data));
    }

}
