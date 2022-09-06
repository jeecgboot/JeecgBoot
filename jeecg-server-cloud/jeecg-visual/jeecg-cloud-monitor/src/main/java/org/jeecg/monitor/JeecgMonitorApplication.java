package org.jeecg.monitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 监控服务
 * @author zyf
 * @date: 2022/4/21 10:55
 */
@SpringBootApplication
@EnableAdminServer
public class JeecgMonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(JeecgMonitorApplication.class);
    }
}