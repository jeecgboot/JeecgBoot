package org.jeecg.modules.airag.prompts.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.airag.prompts.entity.AiragExtData;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.airag.prompts.vo.AiragDebugVo;

import java.util.List;

/**
 * @Description: airag_ext_data
 * @Author: jeecg-boot
 * @Date:   2025-12-12
 * @Version: V1.0
 */
public interface IAiragExtDataService extends IService<AiragExtData> {

    Result debugEvaluator(AiragDebugVo debugVo);

    List<AiragExtData> queryTrackById(String id);
}
