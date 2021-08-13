package com.alibaba.nacos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Nacos 启动类
 * 引用的nacos console 源码运行，简化开发
 * 生产建议从官网下载最新版配置运行
 * @author zyf
 */
@SpringBootApplication(scanBasePackages = "com.alibaba.nacos")
@ServletComponentScan
@EnableScheduling
public class JeecgNacosApplication {

    /**
     * 是否单机模式启动
     */
    private static String standalone = "true";

    /**
     * 是否开启鉴权
     */
    private static String enabled = "false";

    public static void main(String[] args) {
        System.setProperty("nacos.standalone", standalone);
        System.setProperty("nacos.core.auth.enabled", enabled);
        System.setProperty("server.tomcat.basedir","logs");
        //自定义启动端口号
        System.setProperty("server.port","8848");
        SpringApplication.run(JeecgNacosApplication.class, args);
    }
}
