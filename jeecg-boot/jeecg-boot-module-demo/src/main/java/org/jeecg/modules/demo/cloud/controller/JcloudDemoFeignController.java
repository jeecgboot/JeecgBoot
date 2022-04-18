//package org.jeecg.modules.demo.cloud.controller;
//
//import com.alibaba.csp.sentinel.annotation.SentinelResource;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.jeecg.common.api.vo.Result;
//import org.jeecg.common.system.api.ISysBaseAPI;
//import org.jeecg.common.system.vo.DictModel;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import javax.annotation.Resource;
//import java.util.List;
//
///**
// *
// */
//@Slf4j
//@Api(tags = "【微服务】单元测试")
//@RestController
//@RequestMapping("/test")
//public class JcloudDemoFeignController {
//    @Resource
//    private ISysBaseAPI sysBaseApi;
////    @Autowired
////    private ErpHelloApi erpHelloApi;
//
//    /**
//     * 测试
//     *
//     * @return
//     */
//    @GetMapping("/callSystem")
//    //@SentinelResource(value = "remoteDict",fallback = "getDefaultHandler")
//    @ApiOperation(value = "通过feign调用system服务", notes = "测试jeecg-demo服务，是否通过fegin调用system服务接口")
//    public Result getRemoteDict() {
//        List<DictModel> list = sysBaseApi.queryAllDict();
//        return Result.OK(list);
//    }
//
//
////    /**
////     * 测试调用 erp 微服务接口
////     * 【如何测试：通过archetype生成微服务模块，快速集成测试】
////     *  http://doc.jeecg.com/2194069
////     * @return
////     */
////    @GetMapping("/callErp")
////    @ApiOperation(value = "测试feign erp", notes = "测试feign erp")
////    public Result callErp() {
////        log.info("call erp 服务");
////        String res = erpHelloApi.callHello();
////        return Result.OK(res);
////    }
//
//    /**
//     * 熔断，默认回调函数
//     *
//     * @return
//     */
//    public Result<Object> getDefaultHandler() {
//        log.info("测试JcloudDemoController-remoteDict 熔断降级");
//        return Result.error("测试JcloudDemoController-remoteDict 熔断降级");
//    }
//
//}
