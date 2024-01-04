/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.csp.sentinel.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import com.alibaba.csp.sentinel.init.InitExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * Sentinel dashboard application.
 *
 * @author Carpenter Lee
 */
@SpringBootApplication
@Slf4j
public class JeecgSentinelApplication {

    public static void main(String[] args) {
        System.setProperty("csp.sentinel.app.type", "1");
        triggerSentinelInit();
        ConfigurableApplicationContext application = SpringApplication.run(JeecgSentinelApplication.class, args);
        Environment env = application.getEnvironment();
        // 目前jeecg-sentinel 1.8.3 版本存在alibaba-sentinel 1.8.3版本 启动nacos数据源导致配置不生效的问题，以下为临时处理办法
        System.getProperties().setProperty("sentinel.dashboard.auth.username", env.getProperty("sentinel.dashboard.auth.username"));
        System.getProperties().setProperty("sentinel.dashboard.auth.password", env.getProperty("sentinel.dashboard.auth.password"));
        String port = env.getProperty("server.port");
        log.info("\n----------------------------------------------------------\n\t" +
                "Application SentinelDashboard is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port  + "/\n\t" +
                "----------------------------------------------------------");
    }

    private static void triggerSentinelInit() {
        new Thread(() -> InitExecutor.doInit()).start();
    }
}
