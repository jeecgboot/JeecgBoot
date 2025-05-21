package org.jeecg.modules.message.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.system.base.service.impl.JeecgServiceImpl;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.mapper.SysMessageMapper;
import org.jeecg.modules.message.service.ISysMessageService;
import org.jeecg.modules.message.websocket.WebSocketSender;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description: 消息
 * @Author: jeecg-boot
 * @Date:  2019-04-09
 * @Version: V1.0
 */
@Service
public class SysMessageServiceImpl extends JeecgServiceImpl<SysMessageMapper, SysMessage> implements ISysMessageService {
    @Override
    public void pushProgress(String userId, String message) {
        pushProgress(userId, message, null);
    }
    @Override
    public void pushProgress(String userId, String message, Map<String, Object> data) {
        JSONObject msg = new JSONObject();
        msg.put("cmd", "user");
        msg.put("msgTxt", message);
        if (data != null) {
            msg.put("data", data);
        }
        WebSocketSender.sendToUser(userId, msg.toJSONString());
    }
    @Override
    public <T> void sendProgress(String userId, int code, T data, String message, String task) {
        JSONObject msg = new JSONObject();
        msg.put("task", task);
        msg.put("code", code);
        msg.put("msgTxt", message);
        if(data != null) {
            msg.put("data", data);
        }
        WebSocketSender.sendToUser(userId, msg.toJSONString());
    }
}
