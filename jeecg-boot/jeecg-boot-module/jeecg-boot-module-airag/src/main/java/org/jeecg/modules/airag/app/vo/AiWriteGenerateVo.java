package org.jeecg.modules.airag.app.vo;

import lombok.Data;

/**
* @Description: ai写作生成实体类
*
* @author: wangshuai
* @date: 2026/1/12 15:59
*/
@Data
public class AiWriteGenerateVo {

    /**
     * 写作类型
     */
    private String activeMode;

    /**
     * 写作内容提示
     */
    private String prompt;

    /**
     * 原文
     */
    private String originalContent;

    /**
     * 长度
     */
    private String length;

    /**
     * 格式
     */
    private String format;

    /**
     * 语气
     */
    private String tone;

    /**
     * 语言
     */
    private String language;
}
