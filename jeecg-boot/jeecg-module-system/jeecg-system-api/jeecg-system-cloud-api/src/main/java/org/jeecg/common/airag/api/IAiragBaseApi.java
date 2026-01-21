package org.jeecg.common.airag.api;

import org.jeecg.common.airag.api.fallback.AiragBaseApiFallback;
import org.jeecg.common.constant.ServiceNameConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * airag baseAPI
 *
 * @author sjlei
 * @date 2025-12-30
 */
@Component
@FeignClient(contextId = "airagBaseRemoteApi", value = ServiceNameConstants.SERVICE_SYSTEM, fallbackFactory = AiragBaseApiFallback.class)
@ConditionalOnMissingClass("org.jeecg.modules.airag.llm.service.impl.AiragBaseApiImpl")
public interface IAiragBaseApi {

    /**
     * 知识库写入文本文档
     *
     * @param knowledgeId 知识库ID
     * @param title       文档标题
     * @param content     文档内容
     * @return 新增的文档ID
     * @author sjlei
     * @date 2025-12-30
     */
    @PostMapping("/airag/api/knowledgeWriteTextDocument")
    String knowledgeWriteTextDocument(
            @RequestParam("knowledgeId") String knowledgeId,
            @RequestParam("title") String title,
            @RequestParam("content") String content
    );

}
