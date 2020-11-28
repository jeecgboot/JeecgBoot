package org.jeecg.boot.starter.rabbitmq.event;

import lombok.Data;
import org.jeecg.common.base.BaseMap;

import java.io.Serializable;

/**
 * 远程事件数据对象
 */
@Data
public class EventObj implements Serializable {
    /**
     * 数据对象
     */
    private BaseMap baseMap;
    /**
     * 自定义业务模块消息处理器beanName
     */
    private String handlerName;
}
