package org.jeecg.modules.cloud.feign.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.cloud.feign.feign.JeecgTestClient;
import org.jeecg.modules.cloud.feign.feign.JeecgTestClient2;
import org.jeecg.starter.cloud.feign.impl.JeecgFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/test")
@Api(tags = "feign测试")
public class JeecgTestFeignTest {

    @Autowired
    private JeecgFeignService jeecgFeignService;

    @Autowired
    private JeecgTestClient jeecgTestClient;


    @GetMapping("getMessage")
    @ApiOperation(value = "测试feign", notes = "测试feign")
    public Result<String> getMessage() {
        return jeecgTestClient.getMessage("jeecg-boot");
    }

    @GetMapping("getMessage2")
    @ApiOperation(value = "测试动态feign", notes = "测试动态feign")
    public Result<String> getMessage2() {
        JeecgTestClient2 jeecgTestClient = jeecgFeignService.newInstance(JeecgTestClient2.class, "jeecg-demo");
        return jeecgTestClient.getMessage("jeecg-boot2");
    }
}
