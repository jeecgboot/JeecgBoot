package org.jeecg.modules.openapi.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.config.shiro.IgnoreAuth;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @date 2024/12/20 14:04
 */
@RestController
@RequestMapping("/openapi/demo")
public class OpenApiIndexController {

    @GetMapping("index")
    @IgnoreAuth
    public Result<Map<String, String>> index() {
        Map<String, String> result = new HashMap<>();
        result.put("first", "Hello World");
        return Result.ok(result);
    }
}
