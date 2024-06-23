package org.jeecg.common.constant.enums;

import org.jeecg.common.util.oConvertUtils;

/**
 * 邮件html模板配置地址美剧
 *
 * @author: liusq
 * @Date: 2023-10-13
 */
public enum EmailTemplateEnum {
    /**
     * 流程催办
     */
    BPM_CUIBAN_EMAIL("bpm_cuiban_email", "/templates/email/bpm_cuiban_email.ftl"),
    /**
     * 流程新任务
     */
    BPM_NEW_TASK_EMAIL("bpm_new_task_email", "/templates/email/bpm_new_task_email.ftl"),
    /**
     * 表单新增记录
     */
    DESFORM_NEW_DATA_EMAIL("desform_new_data_email", "/templates/email/desform_new_data_email.ftl");

    /**
     * 模板名称
     */
    private String name;
    /**
     * 模板地址
     */
    private String url;

    EmailTemplateEnum(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static EmailTemplateEnum getByName(String name) {
        if (oConvertUtils.isEmpty(name)) {
            return null;
        }
        for (EmailTemplateEnum val : values()) {
            if (val.getName().equals(name)) {
                return val;
            }
        }
        return null;
    }
}
