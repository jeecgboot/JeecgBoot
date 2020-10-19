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
 * @Date: Created in 14:09 2020/10/16
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableAutoConfiguration(exclude={org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
public class JeecgMerckApplication {

    public static void main(String[] args) throws UnknownHostException {
        SpringApplication.run(JeecgMerckApplication.class, args);
    }
}
