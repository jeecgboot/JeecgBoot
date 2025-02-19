package org.jeecg.modules.openapi.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.openapi.entity.OpenApiParam;
import org.jeecg.modules.openapi.mapper.OpenApiParamMapper;
import org.jeecg.modules.openapi.service.OpenApiParamService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @date 2024/12/10 14:50
 */
@Service
public class OpenApiParamServiceImpl extends ServiceImpl<OpenApiParamMapper, OpenApiParam> implements OpenApiParamService {
    @Override
    public boolean deleteByApiId(String apiId) {
        return baseMapper.delete(Wrappers.lambdaUpdate(OpenApiParam.class).eq(OpenApiParam::getApiId, apiId)) > 0;

    }

    @Override
    public List<OpenApiParam> findByApiId(String apiId) {
        return baseMapper.selectList(Wrappers.lambdaQuery(OpenApiParam.class).eq(OpenApiParam::getApiId, apiId));
    }
}
