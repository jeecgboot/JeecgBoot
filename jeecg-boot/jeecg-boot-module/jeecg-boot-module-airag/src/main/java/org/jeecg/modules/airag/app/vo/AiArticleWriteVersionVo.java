package org.jeecg.modules.airag.app.vo;

import lombok.Data;

/**
* @Description: AI写作版本号
*
* @author: wangshuai
* @date: 2026/1/16 11:57
*/
@Data
public class AiArticleWriteVersionVo {

    /**
     * 当前版本号
     */
    private String version;

    /**
     * 写作内容
     */
    private String content;
}
