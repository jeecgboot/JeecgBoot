package org.jeecg.common.airag.api;

/**
 * airag baseAPI
 *
 * @author sjlei
 * @date 2025-12-30
 */
public interface IAiragBaseApi {

    /**
     * 知识库写入文本文档（支持自定义分段策略）
     *
     * @param knowledgeId   知识库ID
     * @param title         文档标题
     * @param content       文档内容
     * @param segmentConfig 【可选】分段策略配置JSON，包含 segmentStrategy/separator/customSeparator/maxSegment/overlap/textRules
     * @return 新增的文档ID
     */
    String knowledgeWriteTextDocument(String knowledgeId, String title, String content, String segmentConfig);

    /**
     * 知识库写入文件文档（支持自定义分段策略）
     *
     * @param knowledgeId   知识库ID
     * @param title         文档标题
     * @param filePath      文件URL（与知识库文档功能 metadata.filePath 字段含义一致）
     * @param segmentConfig 【可选】分段策略配置JSON
     * @return 新增的文档ID
     */
    String knowledgeWriteFileDocument(String knowledgeId, String title, String filePath, String segmentConfig);

    /**
     * 批量查询知识库文档向量化状态
     *
     * @param documentIds 文档ID列表（英文逗号拼接字符串）
     * @return 状态码：COMPLETED / COMPLETED_WITH_FAIL / PROCESSING / PROCESSING_WITH_FAIL
     */
    String checkKnowledgeDocsVectorizeStatus(String documentIds);

    /**
     * 读取会话变量
     *
     * @param appId    应用ID
     * @param username 用户名
     * @param name     变量名
     * @return 变量值，不存在时返回null
     */
    String getChatVariable(String appId, String username, String name);

    /**
     * 设置会话变量
     *
     * @param appId    应用ID
     * @param username 用户名
     * @param name     变量名
     * @param value    变量值
     */
    void setChatVariable(String appId, String username, String name, String value);

    /**
     * 根据应用ID查询记忆库ID
     * 当应用开启了记忆功能(izOpenMemory=1)时返回memoryId，否则返回null
     *
     * @param appId 应用ID
     * @return 记忆库ID，未开启记忆功能时返回null
     */
    String getMemoryIdByAppId(String appId);

    /**
     * 根据提示词ID查询提示词内容
     * 供 LLM 节点关联模式在运行时动态加载提示词内容
     *
     * @param promptId 提示词表主键ID
     * @return 提示词内容，提示词不存在时返回null
     */
    String getPromptContent(String promptId);

    /**
     * 解析本地、MinIO或OSS文件内容。
     */
    String parseFileContent(String filePath);

}
