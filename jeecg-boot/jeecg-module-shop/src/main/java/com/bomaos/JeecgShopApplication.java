package com.bomaos;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.wf.jwtp.configuration.EnableJwtPermission;

@EnableJwtPermission
@EnableAsync
@MapperScan("com.bomaos.**.mapper")
@SpringBootApplication
public class JeecgShopApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(JeecgShopApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(JeecgShopApplication.class, args);
    }

}
