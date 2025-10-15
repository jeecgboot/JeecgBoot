//package org.jeecg.config.init;
//
//import io.undertow.UndertowOptions;
//import io.undertow.server.DefaultByteBufferPool;
//import io.undertow.server.handlers.BlockingHandler;
//import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
//import org.jeecg.modules.monitor.actuator.undertow.CustomUndertowMetricsHandler;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
//import org.springframework.boot.web.server.WebServerFactoryCustomizer;
//import org.springframework.context.annotation.Configuration;
//
///**
// * Undertow配置
// *
// * 解决启动提示： WARN  io.undertow.websockets.jsr:68 - UT026010: Buffer pool was not set on WebSocketDeploymentInfo, the default pool will be used
// */
//@Configuration
//public class UndertowConfiguration implements WebServerFactoryCustomizer<UndertowServletWebServerFactory> {
//
//    /**
//     * 自定义undertow监控指标工具类
//     * for [QQYUN-11902]tomcat 替换undertow 这里的功能还没修改
//     */
//    @Autowired
//    private CustomUndertowMetricsHandler customUndertowMetricsHandler;
//
//    @Override
//    public void customize(UndertowServletWebServerFactory factory) {
//        // 设置 Undertow 服务器参数（底层网络配置）
//        factory.addBuilderCustomizers(builder -> {
//            builder.setServerOption(UndertowOptions.MAX_HEADER_SIZE, 65536);      // header 最大64KB
//            builder.setServerOption(UndertowOptions.MAX_PARAMETERS, 10000);       // 最大参数数
//        });
//        factory.addDeploymentInfoCustomizers(deploymentInfo -> {
//
//            WebSocketDeploymentInfo webSocketDeploymentInfo = new WebSocketDeploymentInfo();
//
//            // 设置合理的参数
//            webSocketDeploymentInfo.setBuffers(new DefaultByteBufferPool(true, 8192));
//
//            deploymentInfo.addServletContextAttribute("io.undertow.websockets.jsr.WebSocketDeploymentInfo", webSocketDeploymentInfo);
//
//            // 添加自定义 监控 handler
//            deploymentInfo.addInitialHandlerChainWrapper(next -> new BlockingHandler(customUndertowMetricsHandler.wrap(next)));
//        });
//    }
//}