package org.jeecg.modules.test.feign.controller;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.boot.starter.rabbitmq.client.RabbitMqClient;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.test.feign.client.JeecgTestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 微服务单元测试
 * @author: zyf
 * @date: 2022/04/21
 */
@Slf4j
@RestController
@RequestMapping("/sys/test")
@Api(tags = "【微服务】单元测试")
public class JeecgTestFeignController {

    @Autowired
    private JeecgTestClient jeecgTestClient;

    /**
     * 熔断： fallbackFactory优先于 @SentinelResource
     *
     * @param name
     * @return
     */
    @GetMapping("/getMessage")
    @ApiOperation(value = "测试feign调用demo服务1", notes = "测试feign @SentinelResource熔断写法 | 测试熔断关闭jeecg-demo服务")
    @SentinelResource(value = "test_more_getMessage", fallback = "getDefaultUser")
    public Result<String> getMessage(@RequestParam(value = "name", required = false) String name) {
        log.info("---------Feign fallbackFactory优先级高于@SentinelResource-----------------");
        String resultMsg = jeecgTestClient.getMessage(" I am jeecg-system 服务节点，呼叫 jeecg-demo!");
        return Result.OK(null, resultMsg);
    }

    /**
     * 测试方法：关闭demo服务，访问请求 http://127.0.0.1:9999/sys/test/getMessage
     *
     * @param name
     * @return
     */
    @GetMapping("/getMessage2")
    @ApiOperation(value = "测试feign调用demo服务2", notes = "测试feign fallbackFactory熔断写法 | 测试熔断关闭jeecg-demo服务")
    public Result<String> getMessage2(@RequestParam(value = "name", required = false) String name) {
        log.info("---------测试 Feign fallbackFactory-----------------");
        String resultMsg = jeecgTestClient.getMessage(" I am jeecg-system 服务节点，呼叫 jeecg-demo!");
        return Result.OK(null, resultMsg);
    }


    @GetMapping("/fallback")
    @ApiOperation(value = "测试熔断", notes = "测试熔断")
    @SentinelResource(value = "test_more_fallback", fallback = "getDefaultUser")
    public Result<Object> test(@RequestParam(value = "name", required = false) String name) {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("name param is empty");
        }
        return Result.OK();
    }

    /**
     * 熔断，默认回调函数
     *
     * @param name
     * @return
     */
    public Result<Object> getDefaultUser(String name) {
        log.info("熔断，默认回调函数");
        return Result.error(null, "访问超时, 自定义 @SentinelResource Fallback");
    }
}
