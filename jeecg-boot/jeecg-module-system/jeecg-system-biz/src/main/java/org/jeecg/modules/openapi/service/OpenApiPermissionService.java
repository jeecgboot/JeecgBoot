package org.jeecg.modules.openapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.openapi.entity.OpenApiPermission;

import java.util.List;

/**
 * @date 2024/12/19 17:44
 */
public interface OpenApiPermissionService extends IService<OpenApiPermission> {
    List<OpenApiPermission> findByAuthId(String authId);

    Result<?> getOpenApi(String apiAuthId);

    void add(OpenApiPermission openApiPermission);
}
