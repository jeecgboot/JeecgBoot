package org.jeecg.modules.airag.app.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.airag.app.entity.AiragApp;

/**
 * @Description: AI应用
 * @Author: jeecg-boot
 * @Date:   2025-02-26
 * @Version: V1.0
 */
public interface AiragAppMapper extends BaseMapper<AiragApp> {

    /**
     * 根据ID查询app信息(忽略租户)
     * @param id
     * @return
     * @author chenrui
     * @date 2025/4/21 16:03
     */
    @InterceptorIgnore(tenantLine = "true")
    AiragApp getByIdIgnoreTenant(String id);

}
