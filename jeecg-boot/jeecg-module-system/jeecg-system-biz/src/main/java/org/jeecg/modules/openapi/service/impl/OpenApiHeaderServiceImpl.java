package org.jeecg.modules.openapi.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.openapi.entity.OpenApiHeader;
import org.jeecg.modules.openapi.mapper.OpenApiHeaderMapper;
import org.jeecg.modules.openapi.service.OpenApiHeaderService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @date 2024/12/10 14:49
 */
@Service
public class OpenApiHeaderServiceImpl extends ServiceImpl<OpenApiHeaderMapper, OpenApiHeader> implements OpenApiHeaderService {
    @Override
    public boolean deleteByApiId(String apiId) {
        return baseMapper.delete(Wrappers.lambdaUpdate(OpenApiHeader.class).eq(OpenApiHeader::getApiId, apiId)) > 0;
    }

    @Override
    public List<OpenApiHeader> findByApiId(String apiId) {
        return baseMapper.selectList(Wrappers.lambdaQuery(OpenApiHeader.class).eq(OpenApiHeader::getApiId, apiId));
    }
}
