package org.jeecg.modules.airag.llm.consts;

import java.util.regex.Pattern;

/**
 * @Description: airag模型常量类
 * @Author: chenrui
 * @Date: 2025/2/12 17:35
 */
public class LLMConsts {


    /**
     * 正则表达式:是否是网页
     */
    public static final Pattern WEB_PATTERN = Pattern.compile("^(http|https)://.*");

    /**
     * 状态:启用
     */
    public static final String STATUS_ENABLE = "enable";
    /**
     * 状态:禁用
     */
    public static final String STATUS_DISABLE = "disable";


    /**
     * 模型类型:向量
     */
    public static final String MODEL_TYPE_EMBED = "EMBED";

    /**
     * 模型类型:聊天
     */
    public static final String MODEL_TYPE_LLM = "LLM";

    /**
     * 知识库:文档状态:草稿
     */
    public static final String KNOWLEDGE_DOC_STATUS_DRAFT = "draft";
    /**
     * 知识库:文档状态:构建中
     */
    public static final String KNOWLEDGE_DOC_STATUS_BUILDING = "building";
    /**
     * 知识库:文档状态:构建完成
     */
    public static final String KNOWLEDGE_DOC_STATUS_COMPLETE = "complete";


    /**
     * 知识库:文档类型:文本
     */
    public static final String KNOWLEDGE_DOC_TYPE_TEXT = "text";
    /**
     * 知识库:文档类型:文件
     */
    public static final String KNOWLEDGE_DOC_TYPE_FILE = "file";
    /**
     * 知识库:文档类型:网页
     */
    public static final String KNOWLEDGE_DOC_TYPE_WEB = "web";

    /**
     * 知识库:文档元数据:文件路径
     */
    public static final String KNOWLEDGE_DOC_METADATA_FILEPATH = "filePath";

    /**
     * 知识库:文档元数据:资源路径
     */
    public static final String KNOWLEDGE_DOC_METADATA_SOURCES_PATH = "sourcesPath";

}
