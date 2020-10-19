package org.jeecg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.net.UnknownHostException;

/**
 * @Author: songchao
 * @Description:
 * @Date: Created in 16:10 2020/10/16
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableAutoConfiguration(exclude={org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
public class JeecgHroopApplication {
    public static void main(String[] args) throws UnknownHostException {
        SpringApplication.run(JeecgHroopApplication.class, args);
    }
}
