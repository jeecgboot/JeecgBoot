package org.jeecg.modules.test.feign.controller;


import org.apache.commons.lang3.StringUtils;
import org.jeecg.boot.starter.rabbitmq.client.RabbitMqClient;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.test.feign.client.JeecgTestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.annotation.SentinelResource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/sys/test")
@Api(tags = "【微服务】单元测试")
public class JeecgTestFeignController {

   // @Autowired
    //private JeecgFeignService jeecgFeignService;

    @Autowired
    private JeecgTestClient jeecgTestClient;

    @Autowired
    private RabbitMqClient rabbitMqClient;

    @PostMapping("getMessage")
    @ApiOperation(value = "测试feign", notes = "测试feign")
    @SentinelResource(value = "fallback",fallback = "getDefaultUser")
    public Result<Object> getMessage(@RequestParam(value = "name",required = false) String name) {
        return jeecgTestClient.getMessage("fegin——jeecg-boot2");
    }


//    @GetMapping("getMessage2")
//    @ApiOperation(value = "测试动态feign", notes = "测试动态feign")
//    public Result<String> getMessage2() {
//        JeecgTestClientDyn myClientDyn = jeecgFeignService.newInstance(JeecgTestClientDyn.class, CloudConstant.SERVER_NAME_JEECGDEMO);
//        return myClientDyn.getMessage("动态fegin——jeecg-boot2");
//    }

    @PostMapping("test")
    @ApiOperation(value = "测试熔断", notes = "测试熔断")
    @SentinelResource(value = "fallback",fallback = "getDefaultUser")
    public Result<Object> test(@RequestParam(value = "name",required = false) String name) {
        if(StringUtils.isEmpty(name)){
            throw new IllegalArgumentException("name param is empty");
        }

        return Result.OK();
    }
    public Result<Object> getDefaultUser(String name) {
        System.out.println("熔断，默认回调函数");
        return Result.OK("访问超时");
    }
}
