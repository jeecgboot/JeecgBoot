package org.jeecg.modules.openapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.openapi.entity.OpenApiHeader;

import java.util.List;

/**
 * @date 2024/12/10 14:48
 */
public interface OpenApiHeaderService extends IService<OpenApiHeader> {

    boolean deleteByApiId(String apiId);

    List<OpenApiHeader> findByApiId(String apiId);
}
