package org.jeecg.cloud.demo.bus;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.boot.starter.rabbitmq.event.EventObj;
import org.jeecg.boot.starter.rabbitmq.event.JeecgBusEventHandler;
import org.jeecg.common.base.BaseMap;
import org.springframework.stereotype.Component;

/**
 * 消息处理器【发布订阅】
 */
@Slf4j
@Component("demoBusEvent")
public class DemoBusEvent implements JeecgBusEventHandler {


    @Override
    public void onMessage(EventObj obj) {
        if (ObjectUtil.isNotEmpty(obj)) {
            BaseMap baseMap = obj.getBaseMap();
            String orderId = baseMap.get("orderId");
            log.info("业务处理----订单ID:" + orderId);
        }
    }
}
