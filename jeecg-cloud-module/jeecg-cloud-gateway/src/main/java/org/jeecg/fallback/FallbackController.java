//package org.jeecg.fallback;
//
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import reactor.core.publisher.Mono;
//
///**
// * 响应超时熔断处理器【升级springboot2.6.6后，此类作废】
// *
// * @author zyf
// */
//@RestController
//public class FallbackController {
//
//    /**
//     * 全局熔断处理
//     * @return
//     */
//    @RequestMapping("/fallback")
//    public Mono<String> fallback() {
//        return Mono.just("访问超时，请稍后再试!");
//    }
//
//    /**
//     * demo熔断处理
//     * @return
//     */
//    @RequestMapping("/demo/fallback")
//    public Mono<String> fallback2() {
//        return Mono.just("访问超时，请稍后再试!");
//    }
//}
