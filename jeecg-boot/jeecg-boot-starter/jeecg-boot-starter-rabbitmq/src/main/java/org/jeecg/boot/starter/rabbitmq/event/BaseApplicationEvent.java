package org.jeecg.boot.starter.rabbitmq.event;

import cn.hutool.core.util.ObjectUtil;

import org.jeecg.common.util.SpringContextHolder;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 监听远程事件,并分发消息到业务模块消息处理器
 */
@Component
public class BaseApplicationEvent implements ApplicationListener<JeecgRemoteApplicationEvent> {

    @Override
    public void onApplicationEvent(JeecgRemoteApplicationEvent jeecgRemoteApplicationEvent) {
        EventObj eventObj = jeecgRemoteApplicationEvent.getEventObj();
        if (ObjectUtil.isNotEmpty(eventObj)) {
            //获取业务模块消息处理器
            JeecgBusEventHandler busEventHandler = SpringContextHolder.getHandler(eventObj.getHandlerName(), JeecgBusEventHandler.class);
            if (ObjectUtil.isNotEmpty(busEventHandler)) {
                //通知业务模块
                busEventHandler.onMessage(eventObj);
            }
        }
    }

}
