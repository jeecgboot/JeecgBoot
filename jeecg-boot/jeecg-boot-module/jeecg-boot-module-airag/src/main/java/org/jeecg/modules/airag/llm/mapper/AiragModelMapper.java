package org.jeecg.modules.airag.llm.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.airag.llm.entity.AiragModel;

/**
 * @Description: AiRag模型配置
 * @Author: jeecg-boot
 * @Date:   2025-02-14
 * @Version: V1.0
 */
public interface AiragModelMapper extends BaseMapper<AiragModel> {

    /**
     * 根据ID查询模型信息(忽略租户)
     * for [QQYUN-12113]分享之后的聊天，应用、模型、知识库不根据租户查询
     * @param id
     * @return
     * @author chenrui
     * @date 2025/4/21 15:24
     */
    @InterceptorIgnore(tenantLine = "true")
    AiragModel getByIdIgnoreTenant(String id);
}
