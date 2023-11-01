package org.jeecg.modules.im.configuration;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.im.service.XMPPService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 项目启动后初始化xmpp相关配置
 */
@Component
@Slf4j
@Order(value = 1)
public class StartupInitXmppRunner implements CommandLineRunner {
    @Resource
    private XMPPService xmppService;
    @Override
    public void run(String... args) {
        //初始化xmpp系统账号
        xmppService.initSystemNo();
    }
}