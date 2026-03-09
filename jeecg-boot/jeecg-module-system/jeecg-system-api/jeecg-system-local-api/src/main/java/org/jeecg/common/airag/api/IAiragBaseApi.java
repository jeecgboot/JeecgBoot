package org.jeecg.common.airag.api;

/**
 * airag baseAPI
 *
 * @author sjlei
 * @date 2025-12-30
 */
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
    String knowledgeWriteTextDocument(String knowledgeId, String title, String content);

}
