package org.jeecg.modules.init;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.modules.system.service.ISysGatewayRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @desc: 启动程序，初始化路由配置
 * @author: flyme
 */
@Slf4j
@Component
public class SystemInitListener implements ApplicationListener<ApplicationReadyEvent>, Ordered {


    @Autowired
    private ISysGatewayRouteService sysGatewayRouteService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {

        log.info(" 服务已启动，准备初始化路由配置 ###################");
        if (applicationReadyEvent.getApplicationContext().getDisplayName().indexOf("AnnotationConfigServletWebServerApplicationContext") > -1) {
            sysGatewayRouteService.addRoute2Redis(CacheConstant.GATEWAY_ROUTES);
        }

    }

    @Override
    public int getOrder() {
        return 1;
    }
}
