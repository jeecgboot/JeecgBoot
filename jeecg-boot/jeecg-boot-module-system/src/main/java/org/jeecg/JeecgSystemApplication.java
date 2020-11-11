package org.jeecg;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@SpringBootApplication
public class JeecgSystemApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(JeecgSystemApplication.class);
    }

    public static void main(String[] args) throws UnknownHostException {
        //System.setProperty("spring.devtools.restart.enabled", "true");

        ConfigurableApplicationContext application = SpringApplication.run(JeecgSystemApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = oConvertUtils.getString(env.getProperty("server.servlet.context-path"));
        log.info("\n----------------------------------------------------------\n\t" +
                "Application Jeecg-Boot is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
                "External: \thttp://" + ip + ":" + port + path + "/\n\t" +
                "Swagger-ui: \thttp://" + ip + ":" + port + path + "/swagger-ui.html\n\t" +
                "Doc文档: \thttp://" + ip + ":" + port + path + "/doc.html\n" +
                "----------------------------------------------------------");

    }

    /**
     * tomcat-embed-jasper引用后提示jar找不到的问题
     */
    @Bean
    public TomcatServletWebServerFactory tomcatFactory() {
    	
    	TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory(){
            @Override
            protected void postProcessContext(Context context) {
                ((StandardJarScanner) context.getJarScanner()).setScanManifest(false);
            }
        };
        
        factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
        	@Override
            public void customize(Connector connector) {
        		connector.setProperty("relaxedPathChars", "\"<>[\\]^`{|}");
        		connector.setProperty("relaxedQueryChars", "\"<>[\\]^`{|}");
        	}
        });
    	
        return factory;
    }
}