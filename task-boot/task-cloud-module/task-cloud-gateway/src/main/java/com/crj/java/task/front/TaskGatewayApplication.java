package com.crj.java.task.front;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableDiscoveryClient
public class TaskGatewayApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(TaskGatewayApplication.class, args);
        String userName = applicationContext.getEnvironment().getProperty("task.test");
        System.err.println("user name :" +userName);
    }
}
