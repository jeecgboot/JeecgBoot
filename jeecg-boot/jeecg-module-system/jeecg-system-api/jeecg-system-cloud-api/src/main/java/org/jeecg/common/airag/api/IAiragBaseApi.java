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
     * 知识库写入文本文档（支持自定义分段策略）
     *
     * @param knowledgeId   知识库ID
     * @param title         文档标题
     * @param content       文档内容
     * @param segmentConfig 【可选】分段策略配置JSON
     * @return 新增的文档ID
     * @author sjlei
     * @date 2025-12-30
     */
    @PostMapping("/airag/api/knowledgeWriteTextDocument")
    String knowledgeWriteTextDocument(
            @RequestParam("knowledgeId") String knowledgeId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "segmentConfig", required = false) String segmentConfig
    );

    /**
     * 知识库写入文件文档（支持自定义分段策略）
     *
     * @param knowledgeId   知识库ID
     * @param title         文档标题
     * @param filePath      文件URL
     * @param segmentConfig 【可选】分段策略配置JSON
     * @return 新增的文档ID
     */
    @PostMapping("/airag/api/knowledgeWriteFileDocument")
    String knowledgeWriteFileDocument(
            @RequestParam("knowledgeId") String knowledgeId,
            @RequestParam("title") String title,
            @RequestParam("filePath") String filePath,
            @RequestParam(value = "segmentConfig", required = false) String segmentConfig
    );

    /**
     * 批量查询知识库文档向量化状态
     */
    @PostMapping("/airag/api/checkKnowledgeDocsVectorizeStatus")
    String checkKnowledgeDocsVectorizeStatus(@RequestParam("documentIds") String documentIds);

    /**
     * 读取会话变量
     */
    @PostMapping("/airag/api/getChatVariable")
    String getChatVariable(
            @RequestParam("appId") String appId,
            @RequestParam("username") String username,
            @RequestParam("name") String name
    );

    /**
     * 设置会话变量
     */
    @PostMapping("/airag/api/setChatVariable")
    void setChatVariable(
            @RequestParam("appId") String appId,
            @RequestParam("username") String username,
            @RequestParam("name") String name,
            @RequestParam("value") String value
    );

    /**
     * 根据应用ID查询记忆库ID
     */
    @PostMapping("/airag/api/getMemoryIdByAppId")
    String getMemoryIdByAppId(@RequestParam("appId") String appId);

    /**
     * 根据提示词ID查询提示词内容
     */
    @PostMapping("/airag/api/getPromptContent")
    String getPromptContent(@RequestParam("promptId") String promptId);

    /**
     * 解析本地、MinIO或OSS文件内容。
     */
    @PostMapping("/airag/api/parseFileContent")
    String parseFileContent(@RequestParam("filePath") String filePath);

}
