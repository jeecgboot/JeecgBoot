package org.jeecg.boot.starter.rabbitmq.event;

import lombok.Data;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;

/**
 * 自定义网关刷新远程事件
 *
 * @author : zyf
 * @date :2020-11-10
 */
@Data
public class JeecgRemoteApplicationEvent extends RemoteApplicationEvent {

    private JeecgRemoteApplicationEvent() {
    }

    private EventObj eventObj;

    public JeecgRemoteApplicationEvent(EventObj source, String originService, String destinationService) {
        super(source, originService, destinationService);
        this.eventObj = source;
    }

    public JeecgRemoteApplicationEvent(EventObj source, String originService) {
        super(source, originService, "");
        this.eventObj = source;
    }
}
