package org.jeecg;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.config.SystemProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@EnableSwagger2
@SpringBootApplication
@EnableAutoConfiguration
public class JeecgApplication {


  @Value(value = "${spring.application.name}")
  private String name;
  @Value(value = "${server.port}")
  private String port;
  @Value(value = "${server.servlet.context-path}")
  private String path;

  public static void main(String[] args) throws UnknownHostException {
    //System.setProperty("spring.devtools.restart.enabled", "true");

    ConfigurableApplicationContext application = SpringApplication.run(JeecgApplication.class, args);
    Environment env = application.getEnvironment();
    String name = env.getProperty("spring.application.name");
    String ip = InetAddress.getLocalHost().getHostAddress();
    String port = env.getProperty("server.port");
    String path = env.getProperty("server.servlet.context-path");
    log.info("\n----------------------------------------------------------\n\t" +
        "Application '" + name + "' is running! Access URLs:\n\t" +
        "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
        "External: \thttp://" + ip + ":" + port + path + "/\n\t" +
        "swagger-ui: http://" + ip + ":" + port + path + "/swagger-ui.html\n\t" +
        "Doc: \t\thttp://" + ip + ":" + port + path + "/doc.html\n" +
        "----------------------------------------------------------");

  }
}