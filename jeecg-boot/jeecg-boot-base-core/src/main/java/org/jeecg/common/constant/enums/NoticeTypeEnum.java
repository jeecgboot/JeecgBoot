package org.jeecg.common.constant.enums;

/**
* @Description: 文件类型枚举类
*
* @author: wangshuai
* @date: 2025/6/26 17:29
*/
public enum NoticeTypeEnum {
    
    //VUE3专用
    NOTICE_TYPE_FILE("知识库消息","file"),
    NOTICE_TYPE_FLOW("工作流消息","flow"),
    NOTICE_TYPE_PLAN("日程消息","plan"),
    //暂时没用到
    NOTICE_TYPE_MEETING("会议消息","meeting"),
    NOTICE_TYPE_SYSTEM("系统消息","system"),
    /**
     * 协同工作
     * for [JHHB-136]【vue3】协同工作系统消息需要添加一个类型
     */
    NOTICE_TYPE_COLLABORATION("协同工作", "collab"),
    /**
     * 督办
     */
    NOTICE_TYPE_SUPERVISE("督办管理", "supe"),
    /**
     * 考勤
     */
    NOTICE_TYPE_ATTENDANCE("考勤消息", "attendance");

    /**
     * 文件类型名称
     */
    private String name;

    /**
     * 文件类型值
     */
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    NoticeTypeEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 获取聊天通知类型
     * 
     * @param value
     * @return
     */
    public static String getChatNoticeType(String value){
        return value + "Notice";
    }

    /**
     * 获取通知名称
     *
     * @param value
     * @return
     */
    public static String getNoticeNameByValue(String value){
        value = value.replace("Notice","");
        for (NoticeTypeEnum e : NoticeTypeEnum.values()) {
            if (e.getValue().equals(value)) {
                return e.getName();
            }
        }
        return "系统消息";
    }
}
