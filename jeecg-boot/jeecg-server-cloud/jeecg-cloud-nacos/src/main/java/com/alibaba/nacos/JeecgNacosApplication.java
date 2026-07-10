package com.alibaba.nacos;

import com.alibaba.nacos.console.NacosConsole;
import com.alibaba.nacos.core.listener.startup.NacosStartUp;
import com.alibaba.nacos.core.listener.startup.NacosStartUpManager;
import com.alibaba.nacos.sys.env.Constants;
import com.alibaba.nacos.sys.env.DeploymentType;
import com.alibaba.nacos.sys.env.EnvUtil;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import static org.springframework.boot.context.logging.LoggingApplicationListener.CONFIG_PROPERTY;
import static org.springframework.core.io.ResourceLoader.CLASSPATH_URL_PREFIX;

/**
 * Nacos 3.x 启动类（三阶段启动）
 *
 * @author jeecg
 */
public class JeecgNacosApplication {

	public static void main(String[] args) {
		System.setProperty("nacos.standalone", "true");
		System.setProperty(CONFIG_PROPERTY, CLASSPATH_URL_PREFIX + "logback-spring.xml");

		String type = System.getProperty(Constants.NACOS_DEPLOYMENT_TYPE, Constants.NACOS_DEPLOYMENT_TYPE_MERGED);
		DeploymentType deploymentType = DeploymentType.getType(type);
		EnvUtil.setDeploymentType(deploymentType);

		// Start Core Context
		NacosStartUpManager.start(NacosStartUp.CORE_START_UP_PHASE);
		ConfigurableApplicationContext coreContext = new SpringApplicationBuilder(NacosServerBasicApplication.class)
			.web(WebApplicationType.NONE)
			.run(args);

		// Start Console Context 先启动：让 IDEA Spring Boot Services 面板识别到的端口是 18080（管理界面），
		// 而不是 NacosServerWebApplication 的 8848（API 端口），与用户实际访问的管理界面地址保持一致
		NacosStartUpManager.start(NacosStartUp.CONSOLE_START_UP_PHASE);
		ConfigurableApplicationContext consoleContext = new SpringApplicationBuilder(NacosConsole.class)
			.parent(coreContext)
			.run(args);

		// Start Server Web Context 后启动：保持 NacosServerWebApplication 监听 8848 不变（接口不变）
		NacosStartUpManager.start(NacosStartUp.WEB_START_UP_PHASE);
		new SpringApplicationBuilder(NacosServerWebApplication.class)
			.parent(coreContext)
			.run(args);

		String consolePort = consoleContext.getEnvironment().getProperty("server.port", "18080");
		String consolePath = consoleContext.getEnvironment().getProperty("server.servlet.context-path", "/nacos");
		System.out.println(" ----------------------------------------------------------");
		System.out.println("  Nacos 3.x 启动成功！");
		System.out.println("  控制台地址：	http://localhost:" + consolePort + consolePath);
		System.out.println("  服务器地址：	http://localhost:8848/nacos");
		System.out.println("---------------------------------------------------------- ");
	}

}
