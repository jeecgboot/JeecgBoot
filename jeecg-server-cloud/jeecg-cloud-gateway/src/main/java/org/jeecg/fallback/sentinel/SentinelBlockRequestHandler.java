//package org.jeecg.fallback.sentinel;
//import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
//import com.alibaba.csp.sentinel.transport.config.TransportConfig;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.commons.util.InetUtils;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.BodyInserters;
//import org.springframework.web.reactive.function.server.ServerResponse;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import javax.annotation.PostConstruct;
//
///**
// * 自定义限流返回信息
// * @author scott
// */
//@Slf4j
//@Component
//public class SentinelBlockRequestHandler implements BlockRequestHandler {
//    @Autowired
//    private InetUtils inetUtils;
//
//    @PostConstruct
//    public void doInit() {
//        System.setProperty(TransportConfig.HEARTBEAT_CLIENT_IP, inetUtils.findFirstNonLoopbackAddress().getHostAddress());
//    }
//
//    @Override
//    public Mono<ServerResponse> handleRequest(ServerWebExchange exchange, Throwable ex) {
//        String resultString = "{\"code\":403,\"message\":\"服务开启限流保护,请稍后再试!\"}";
//        return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS).contentType(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject(resultString));
//    }
//
//
//}
