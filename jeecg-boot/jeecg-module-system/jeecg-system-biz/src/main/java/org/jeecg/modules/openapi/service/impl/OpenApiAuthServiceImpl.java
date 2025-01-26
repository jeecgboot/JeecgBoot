package org.jeecg.modules.openapi.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.openapi.entity.OpenApiAuth;
import org.jeecg.modules.openapi.mapper.OpenApiAuthMapper;
import org.jeecg.modules.openapi.service.OpenApiAuthService;
import org.springframework.stereotype.Service;

/**
 * @date 2024/12/10 9:51
 */
@Service
public class OpenApiAuthServiceImpl extends ServiceImpl<OpenApiAuthMapper, OpenApiAuth> implements OpenApiAuthService {
    @Override
    public OpenApiAuth getByAppkey(String appkey) {
        return baseMapper.selectOne(Wrappers.lambdaUpdate(OpenApiAuth.class).eq(OpenApiAuth::getAk, appkey), false);
    }
}
