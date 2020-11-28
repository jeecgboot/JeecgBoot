package org.jeecg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class JeecgGatewayApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(JeecgGatewayApplication.class, args);
        String userName = applicationContext.getEnvironment().getProperty("jeecg.test");
        System.err.println("user name :" +userName);
    }
}
