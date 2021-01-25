package org.jeecg.config.init;

import org.apache.catalina.Context;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: TomcatFactoryConfig
 * @author: scott
 * @date: 2021年01月25日 11:40
 */
@Configuration
public class TomcatFactoryConfig {
    /**
     * tomcat-embed-jasper引用后提示jar找不到的问题
     */
    @Bean
    public TomcatServletWebServerFactory tomcatFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                ((StandardJarScanner) context.getJarScanner()).setScanManifest(false);
            }
        };
        factory.addConnectorCustomizers(connector -> {
            connector.setProperty("relaxedPathChars", "[]{}");
            connector.setProperty("relaxedQueryChars", "[]{}");
        });
        return factory;
    }
}
