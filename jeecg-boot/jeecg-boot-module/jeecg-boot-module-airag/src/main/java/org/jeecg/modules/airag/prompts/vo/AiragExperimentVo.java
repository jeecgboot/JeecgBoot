package org.jeecg.modules.airag.prompts.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Description: AiragExperimentVo
 * @Author: jeecg-boot
 * @Date:   2025-12-12
 * @Version: V1.0
 */
@Data
public class AiragExperimentVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 提示词
     */
    private String promptKey;
    /**
     * 输入内容
     */
    private String extDataId;
    /**
     * 映射关系
     */
    private Map<String,String> mappings;
}
