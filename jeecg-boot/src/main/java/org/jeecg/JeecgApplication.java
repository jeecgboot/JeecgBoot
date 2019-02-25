package org.jeecg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class JeecgApplication {

    public static void main(String[] args) {
    	System.setProperty("spring.devtools.restart.enabled", "true");
    	SpringApplication.run(JeecgApplication.class, args);
    }
}