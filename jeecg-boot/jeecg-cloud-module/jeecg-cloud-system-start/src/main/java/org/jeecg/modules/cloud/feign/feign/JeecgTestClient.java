package org.jeecg.modules.cloud.feign.feign;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.cloud.constant.CloudConstant;
import org.jeecg.modules.cloud.feign.feign.fallback.JeecgTestClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 常规feign接口定义
 */
@FeignClient(value = CloudConstant.SERVER_NAME_JEECGDEMO, fallbackFactory = JeecgTestClientFallback.class)
@Component
public interface JeecgTestClient {

    @GetMapping(value = "/test/getMessage")
    Result<String> getMessage(@RequestParam("name") String name);
}
