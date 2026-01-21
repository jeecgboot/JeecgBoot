package org.jeecg.modules.airag.llm.consts;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
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
     * 模型类型: 图像生成
     */
    public static final String MODEL_TYPE_IMAGE = "IMAGE";

    /**
     * 向量模型：默认维度
     */
    public static final Integer EMBED_MODEL_DEFAULT_DIMENSION = 1536;

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
     * 知识库:文档状态:构建失败
     */
    public static final String KNOWLEDGE_DOC_STATUS_FAILED = "failed";

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

    /**
     * DEEPSEEK推理模型
     */
    public static final String DEEPSEEK_REASONER = "deepseek-reasoner";

    /**
     * 知识库类型：知识库
     */
    public static final String KNOWLEDGE_TYPE_KNOWLEDGE = "knowledge";
    
    /**
     * 知识库类型：记忆库
     */
    public static final String KNOWLEDGE_TYPE_MEMORY = "memory";

    /**
     * 支持文件的后缀
     */
    public static final Set<String> CHAT_FILE_EXT_WHITELIST = new HashSet<>(Arrays.asList("txt", "pdf", "docx", "doc", "pptx", "ppt", "xlsx", "xls", "md"));

    /**
     * 文件内容最大长度
     */
    public static final int CHAT_FILE_TEXT_MAX_LENGTH = 20000;

    /**
     * 上传文件对打数量
     */
    public static final int CHAT_FILE_MAX_COUNT = 3;

}
