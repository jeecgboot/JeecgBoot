package org.jeecg;

import org.jeecg.loader.DynamicRouteLoader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.Resource;

/**
 * @author jeecg
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class JeecgGatewayApplication  implements CommandLineRunner {

    @Resource
    private DynamicRouteLoader dynamicRouteLoader;

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(JeecgGatewayApplication.class, args);
        String userName = applicationContext.getEnvironment().getProperty("jeecg.test");
        System.err.println("user name :" +userName);
    }

    /**
     * 容器初始化后加载路由
     * @param strings
     */
    @Override
    public void run(String... strings) {
        dynamicRouteLoader.refresh(null);
    }
}
