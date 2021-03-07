package org.jeecg.modules.cloud.ebus;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.boot.starter.rabbitmq.event.EventObj;
import org.jeecg.boot.starter.rabbitmq.event.JeecgBusEventHandler;
import org.jeecg.common.base.BaseMap;
import org.jeecg.modules.cloud.constant.CloudConstant;
import org.springframework.stereotype.Component;

/**
 * 消息处理器【发布订阅】
 */
@Slf4j
@Component(CloudConstant.MQ_DEMO_BUS_EVENT)
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
