package org.jeecg.modules.airag.llm.controller;

import org.jeecg.common.airag.api.IAiragBaseApi;
import org.jeecg.modules.airag.api.AiragBaseApiImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * airag baseAPI Controller
 * <p>
 * 仅供微服务 Feign 内部调用。单体模式下为进程内调用，无需对外暴露，
 * 故只在类路径存在 jeecg-boot-starter-cloud（微服务模式）时加载（2026-10-14）
 *
 * @author sjlei
 * @date 2025-12-30
 */
@RestController("airagBaseApiController")
@ConditionalOnClass(name = "org.jeecg.config.JeecgCloudBaseConfig")
public class AiragBaseApiController implements IAiragBaseApi {

    @Autowired
    AiragBaseApiImpl airagBaseApi;

    @PostMapping("/airag/api/knowledgeWriteTextDocument")
    public String knowledgeWriteTextDocument(
            @RequestParam("knowledgeId") String knowledgeId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "segmentConfig", required = false) String segmentConfig
    ) {
        return airagBaseApi.knowledgeWriteTextDocument(knowledgeId, title, content, segmentConfig);
    }

    @PostMapping("/airag/api/knowledgeWriteFileDocument")
    public String knowledgeWriteFileDocument(
            @RequestParam("knowledgeId") String knowledgeId,
            @RequestParam("title") String title,
            @RequestParam("filePath") String filePath,
            @RequestParam(value = "segmentConfig", required = false) String segmentConfig
    ) {
        return airagBaseApi.knowledgeWriteFileDocument(knowledgeId, title, filePath, segmentConfig);
    }

    @PostMapping("/airag/api/checkKnowledgeDocsVectorizeStatus")
    public String checkKnowledgeDocsVectorizeStatus(@RequestParam("documentIds") String documentIds) {
        return airagBaseApi.checkKnowledgeDocsVectorizeStatus(documentIds);
    }

    @PostMapping("/airag/api/getChatVariable")
    public String getChatVariable(
            @RequestParam("appId") String appId,
            @RequestParam("username") String username,
            @RequestParam("name") String name
    ) {
        return airagBaseApi.getChatVariable(appId, username, name);
    }

    @PostMapping("/airag/api/setChatVariable")
    public void setChatVariable(
            @RequestParam("appId") String appId,
            @RequestParam("username") String username,
            @RequestParam("name") String name,
            @RequestParam("value") String value
    ) {
        airagBaseApi.setChatVariable(appId, username, name, value);
    }

    @PostMapping("/airag/api/getMemoryIdByAppId")
    public String getMemoryIdByAppId(@RequestParam("appId") String appId) {
        return airagBaseApi.getMemoryIdByAppId(appId);
    }

    @PostMapping("/airag/api/getPromptContent")
    public String getPromptContent(@RequestParam("promptId") String promptId) {
        return airagBaseApi.getPromptContent(promptId);
    }

}
