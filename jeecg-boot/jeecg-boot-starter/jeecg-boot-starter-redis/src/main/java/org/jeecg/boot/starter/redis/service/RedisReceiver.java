package org.jeecg.boot.starter.redis.service;


import cn.hutool.core.util.ObjectUtil;
import lombok.Data;
import org.jeecg.boot.starter.redis.listener.JeecgRedisListerer;
import org.jeecg.common.base.BaseMap;
import org.jeecg.common.constant.GlobalConstants;
import org.jeecg.common.util.SpringContextHolder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
public class RedisReceiver {


    /**
     * 接受消息并调用业务逻辑处理器
     *
     * @param params
     */
    public void onMessage(BaseMap params) {
        Object handlerName = params.get(GlobalConstants.HANDLER_NAME);
        JeecgRedisListerer messageListener = SpringContextHolder.getHandler(handlerName.toString(), JeecgRedisListerer.class);
        if (ObjectUtil.isNotEmpty(messageListener)) {
            messageListener.onMessage(params);
        }
    }

}
