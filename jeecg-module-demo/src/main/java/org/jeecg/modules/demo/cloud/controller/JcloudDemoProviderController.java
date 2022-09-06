package org.jeecg.modules.demo.cloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.demo.cloud.service.JcloudDemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 服务端提供方——feign接口
 * 【提供给system-start调用测试，看feign是否畅通】
 * @author: jeecg-boot
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class JcloudDemoProviderController {

    @Resource
    private JcloudDemoService jcloudDemoService;

    @GetMapping("/getMessage")
    public String getMessage(@RequestParam String name) {
        String msg = jcloudDemoService.getMessage(name);
        log.info(" 微服务被调用：{} ",msg);
        return msg;
    }

}
