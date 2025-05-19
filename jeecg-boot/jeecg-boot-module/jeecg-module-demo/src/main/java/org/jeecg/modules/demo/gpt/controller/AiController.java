package org.jeecg.modules.demo.gpt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.chatgpt.service.AiChatService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//update-begin---author:chenrui ---date:20240110  for：[QQYUN-5509]AI生成表结构和软文------------

/**
 * @Description: chatGpt接口
 * @Author: chenrui
 * @Date: 2024/1/9 16:30
 */
@Tag(name = "AI接口")
@RestController
@RequestMapping("/test/ai")
@Slf4j
public class AiController {

    private static final String CACHE_PREFIX = "ai:resp:";

    @Autowired
    AiChatService aiChatService;


    /**
     * 通过AI生成模块表设计
     * @param descr 描述
     * @return
     * @author chenrui
     * @date 2024/1/9 20:12
     */
    @AutoLog(value = "通过AI生成模块表设计")
    @PostMapping(value = "/gen/schema/modules")
    @Operation(summary = "通过AI生成模块表设计")
    public Result<String> genSchemaModules(@RequestParam(name = "prompt", required = true) String prompt) {
        String result = aiChatService.genSchemaModules(prompt);
        return Result.ok(result);
    }

    /**
     * 通过AI生成软文
     * @param descr 描述
     * @return
     * @author chenrui
     * @date 2024/1/9 20:12
     */
    @AutoLog(value = "通过AI生成软文")
    @PostMapping(value = "/gen/article")
    @Operation(summary = "通过AI生成软文")
    public Result<String> genArticle(@RequestParam(name = "prompt", required = true) String prompt) {
        String result = aiChatService.genArticleWithMd(prompt);
        return Result.ok(result);
    }

    /**
     * 向AI提问
     * @param message
     * @return
     * @author chenrui
     * @date 2024/1/15 19:11
     */
    @AutoLog(value = "向AI提问")
    @PostMapping(value = "/completions")
    @Operation(summary = "向AI提问")
    public Result<?> completions(@RequestParam(name = "message", required = true) String message) {
        String result = aiChatService.completions(message);
        return Result.ok(result);
    }

    /**
     * 让AI生成图片
     * @param prompt
     * @return
     * @author chenrui
     * @date 2024/1/15 19:11
     */
    @AutoLog(value = "让AI生成图片")
    @PostMapping(value = "/gen/image")
    @Operation(summary = "让AI生成图片")
    public Result<?> genImage(@RequestParam(name = "prompt", required = true) String prompt) {
        String result = aiChatService.imageGenerate(prompt);
        return Result.ok(result);
    }

}
//update-end---author:chenrui ---date:20240110  for：//update-begin---author:chenrui ---date:20240110  for：[QQYUN-5509]AI生成表结构和软文------------------------
