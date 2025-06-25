package org.jeecg.modules.airag.ocr.entity;

import lombok.Data;

/**
* @Description: OCR识别实体类
*
* @author: wangshuai
* @date: 2025/4/16 17:01
*/
@Data
public class AiOcr {

    /**
     * 编号
     */
    private String id;
    
    /**
     * 标题
     */
    private String title;
    
    /**
     * 提示词
     */
    private String prompt;
    
}
