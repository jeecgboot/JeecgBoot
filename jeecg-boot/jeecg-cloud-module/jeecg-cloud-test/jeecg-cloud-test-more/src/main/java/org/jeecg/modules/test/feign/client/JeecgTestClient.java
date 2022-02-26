package org.jeecg.modules.test.feign.client;

import org.jeecg.common.api.vo.Result;

import org.jeecg.config.FeignConfig;
import org.jeecg.modules.test.constant.CloudConstant;
import org.jeecg.modules.test.feign.factory.JeecgTestClientFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 常规feign接口定义
 */
@FeignClient(value = CloudConstant.SERVER_NAME_JEECGDEMO, configuration = FeignConfig.class,fallbackFactory = JeecgTestClientFactory.class)
@Component
public interface JeecgTestClient {

    @PostMapping(value = "/test/getMessage")
    Result<Object> getMessage(@RequestParam(value = "name",required = false) String name);
}
