package com.xxl.job.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * @author xuxueli 2018-10-28 00:38:13
 */
@SpringBootApplication
@Slf4j
public class XxlJobAdminApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext application = SpringApplication.run(XxlJobAdminApplication.class, args);
		Environment env = application.getEnvironment();
		String port = env.getProperty("server.port");
		String path = env.getProperty("server.servlet.context-path");
		log.info("\n----------------------------------------------------------\n\t" +
				"Application XxlJobAdmin is running! Access URLs:\n\t" +
				"Local: \t\thttp://localhost:" + port + path + "/\n\t" +
				"----------------------------------------------------------");
	}

}