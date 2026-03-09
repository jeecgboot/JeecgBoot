package org.jeecg.modules.airag.llm.controller;

import org.jeecg.common.airag.api.IAiragBaseApi;
import org.jeecg.modules.airag.llm.service.impl.AiragBaseApiImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * airag baseAPI Controller
 *
 * @author sjlei
 * @date 2025-12-30
 */
@RestController("airagBaseApiController")
public class AiragBaseApiController implements IAiragBaseApi {

    @Autowired
    AiragBaseApiImpl airagBaseApi;

    @PostMapping("/airag/api/knowledgeWriteTextDocument")
    public String knowledgeWriteTextDocument(
            @RequestParam("knowledgeId") String knowledgeId,
            @RequestParam("title") String title,
            @RequestParam("content") String content
    ) {
        return airagBaseApi.knowledgeWriteTextDocument(knowledgeId, title, content);
    }

}
