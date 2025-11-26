package org.jeecg.common.constant.enums;

import org.jeecg.common.util.oConvertUtils;

/**
 * UniPush 消息推送枚举
 * @author: jeecg-boot
 */
public enum UniPushTypeEnum {
    /**
     * 聊天
     */
    CHAT("chat", "聊天消息", "收到%s发来的聊天消息"),
    /**
     * 流程跳转到我的任务
     */
    BPM("bpm_task", "待办任务", "收到%s待办任务"),

    /**
     * 流程抄送任务
     */
    BPM_VIEW("bpm_cc", "知会任务", "收到%s知会任务"),
    /**
     * 系统消息
     */
    SYS_MSG("system", "系统消息", "收到一条系统通告");

    /**
     * 业务类型(chat:聊天 bpm_task:流程 bpm_cc:流程抄送)
     */
    private String type;
    /**
     * 消息标题
     */
    private String title;
    /**
     * 消息内容
     */
    private String content;

    UniPushTypeEnum(String type, String title, String content) {
        this.type = type;
        this.title = title;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title ;
    }

    public void setTitle(String openType) {
        this.title = openType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static UniPushTypeEnum getByType(String type) {
        if (oConvertUtils.isEmpty(type)) {
            return null;
        }
        for (UniPushTypeEnum val : values()) {
            if (val.getType().equals(type)) {
                return val;
            }
        }
        return null;
    }
}
