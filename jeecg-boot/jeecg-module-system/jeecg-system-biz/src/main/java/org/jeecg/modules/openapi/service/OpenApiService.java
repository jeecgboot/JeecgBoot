package org.jeecg.modules.openapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.openapi.entity.OpenApi;

public interface OpenApiService extends IService<OpenApi> {
    OpenApi findByPath(String path);
}
