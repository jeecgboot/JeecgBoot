package org.jeecg.modules.airag.llm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import dev.langchain4j.data.message.ChatMessage;
import org.jeecg.modules.airag.llm.entity.AiragModel;

import java.util.List;

/**
 * @Description: AiRag模型配置
 * @Author: jeecg-boot
 * @Date:   2025-02-14
 * @Version: V1.0
 */
public interface IAiragModelService extends IService<AiragModel> {

}
