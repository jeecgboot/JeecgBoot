package org.jeecg.modules.airag.prompts.service;

import jakarta.servlet.http.HttpServletRequest;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.airag.prompts.entity.AiragPrompts;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.airag.prompts.vo.AiragExperimentVo;

/**
 * @Description: airag_prompts
 * @Author: jeecg-boot
 * @Date:   2025-12-12
 * @Version: V1.0
 */
public interface IAiragPromptsService extends IService<AiragPrompts> {

    Result<?> promptExperiment(AiragExperimentVo experimentVo, HttpServletRequest request);
}
