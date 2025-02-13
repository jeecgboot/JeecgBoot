package org.jeecg.modules.openapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.openapi.entity.OpenApiParam;

import java.util.List;

/**
 * @date 2024/12/10 14:49
 */
public interface OpenApiParamService extends IService<OpenApiParam> {
    boolean deleteByApiId(String apiId);

    List<OpenApiParam> findByApiId(String apiId);
}
