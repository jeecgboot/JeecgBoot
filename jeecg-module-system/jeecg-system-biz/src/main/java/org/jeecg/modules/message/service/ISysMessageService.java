package org.jeecg.modules.message.service;

import org.jeecg.common.system.base.service.JeecgService;
import org.jeecg.modules.message.entity.SysMessage;

import java.util.Map;

/**
 * @Description: 消息
 * @Author: jeecg-boot
 * @Date:  2019-04-09
 * @Version: V1.0
 */
public interface ISysMessageService extends JeecgService<SysMessage> {

    void pushProgress(String userId, String message);

    void pushProgress(String userId, String message, Map<String, Object> data);

    <T> void sendProgress(String userId, int code, T data, String message, String task);
}
