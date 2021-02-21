//package org.jeecg.modules.demo.cloud.controller;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.jeecg.common.api.vo.Result;
//import org.jeecg.common.system.api.ISysBaseAPI;
//import org.jeecg.common.system.vo.DictModel;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//import java.util.List;
//
///**
// *
// */
//@Slf4j
//@Api(tags = "Cloud示例")
//@RestController
//@RequestMapping("/test")
//public class JcloudDemoController {
//
//
//    @Resource
//    private ISysBaseAPI sysBaseAPI;
//
//    /**
//     * 测试
//     *
//     * @return
//     */
//    @GetMapping("/remote")
//    @ApiOperation(value = "测试feign", notes = "测试feign")
//    public Result remoteDict() {
////        try{
////            //睡5秒，网关Hystrix3秒超时，会触发熔断降级操作
////            Thread.sleep(5000);
////        }catch (Exception e){
////            e.printStackTrace();
////        }
//        List<DictModel> list = sysBaseAPI.queryAllDict();
//        return Result.OK(list);
//    }
//
//
//}
