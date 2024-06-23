package org.jeecg.common.constant.enums;

import org.jeecg.common.util.oConvertUtils;

/**
 * 系统公告自定义跳转方式
 * @author: jeecg-boot
 */
public enum SysAnnmentTypeEnum {
    /**
     * 邮件跳转组件
     */
    EMAIL("email", "component", "modules/eoa/email/modals/EoaEmailInForm"),
    /**
     * 流程跳转到我的任务
     */
    BPM("bpm", "url", "/bpm/task/MyTaskList"),
    
    /**
     * 流程抄送任务
     */
    BPM_VIEW("bpm_cc", "url", "/bpm/task/MyTaskList"),
    /**
     * 邀请用户跳转到个人设置
     */
    TENANT_INVITE("tenant_invite", "url", "/system/usersetting");

    /**
     * 业务类型(email:邮件 bpm:流程)
     */
    private String type;
    /**
     * 打开方式 组件：component 路由：url
     */
    private String openType;
    /**
     * 组件/路由 地址
     */
    private String openPage;

    SysAnnmentTypeEnum(String type, String openType, String openPage) {
        this.type = type;
        this.openType = openType;
        this.openPage = openPage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOpenType() {
        return openType;
    }

    public void setOpenType(String openType) {
        this.openType = openType;
    }

    public String getOpenPage() {
        return openPage;
    }

    public void setOpenPage(String openPage) {
        this.openPage = openPage;
    }

    public static SysAnnmentTypeEnum getByType(String type) {
        if (oConvertUtils.isEmpty(type)) {
            return null;
        }
        for (SysAnnmentTypeEnum val : values()) {
            if (val.getType().equals(type)) {
                return val;
            }
        }
        return null;
    }
}
