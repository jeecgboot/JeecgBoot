package org.jeecg.modules.airag.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Description: 发送消息的入参
 * @Author: chenrui
 * @Date: 2025/2/25 11:47
 */
@NoArgsConstructor
@Data
public class ChatSendParams {

    public ChatSendParams(String content, String conversationId, String topicId, String appId) {
        this.content = content;
        this.conversationId = conversationId;
        this.topicId = topicId;
        this.appId = appId;
    }

    /**
     * 用户输入的聊天内容
     */
    private String content;

    /**
     * 对话会话ID
     */
    private String conversationId;

    /**
     * 对话主题ID（用于关联历史记录）
     */
    private String topicId;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 图片列表
     */
    private List<String> images;

    /**
     * 文件列表
     */
    private List<String> files;

    /**
     * 工作流额外入参配置
     * key: 参数field, value: 参数值
     * for [issues/8545]新建AI应用的时候只能选择没有自定义参数的AI流程
     */
    private Map<String, Object> flowInputs;

    /**
     * 是否开启网络搜索（仅千问模型支持）
     */
    private Boolean enableSearch;

    /**
     * 是否开启深度思考
     */
    private Boolean enableThink;

    /**
     * 会话类型: portal 应用门户
     */
    private String sessionType;

    /**
     * 是否开启生成绘画
     */
    private Boolean enableDraw;

    /**
     * 绘画模型的id
     */
    private String drawModelId;

    /**
     * 图片尺寸
     */
    private String imageSize;

    /**
     * 一张图片
     */
    private String imageUrl;

    /**
     * 是否保存会话
     */
    private Boolean izSaveSession;

}
