package com.vone.mq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JeecgVmqApplication {

    public static void main(String[] args) {
        SpringApplication.run(JeecgVmqApplication.class, args);
    }

}

