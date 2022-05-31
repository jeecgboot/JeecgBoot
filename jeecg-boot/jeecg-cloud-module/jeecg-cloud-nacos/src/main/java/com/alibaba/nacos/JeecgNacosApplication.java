package com.alibaba.nacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * Nacos 启动类
 *
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

    /**
     * 默认跳转首页
     *
     * @param model
     * @return
     */
    @GetMapping("/")
    public String index(Model model, HttpServletResponse response) {
        // 视图重定向 - 跳转
        return "/nacos";
    }
}
