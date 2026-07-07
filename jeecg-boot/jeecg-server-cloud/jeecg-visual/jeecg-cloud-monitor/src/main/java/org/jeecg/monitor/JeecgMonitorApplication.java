package org.jeecg.monitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author scott
 */
@SpringBootApplication
@EnableAdminServer
public class JeecgMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(JeecgMonitorApplication.class, args);
    }
}
