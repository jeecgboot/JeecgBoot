package org.jeecg.modules.openapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.openapi.entity.OpenApiAuth;

/**
 * @date 2024/12/10 9:50
 */
public interface OpenApiAuthService extends IService<OpenApiAuth> {
    OpenApiAuth getByAppkey(String appkey);
}
