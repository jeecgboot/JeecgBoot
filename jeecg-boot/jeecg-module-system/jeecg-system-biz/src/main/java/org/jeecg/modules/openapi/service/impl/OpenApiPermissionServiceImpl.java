package org.jeecg.modules.openapi.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.openapi.entity.OpenApiPermission;
import org.jeecg.modules.openapi.mapper.OpenApiPermissionMapper;
import org.jeecg.modules.openapi.service.OpenApiPermissionService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @date 2024/12/19 17:44
 */
@Service
public class OpenApiPermissionServiceImpl extends ServiceImpl<OpenApiPermissionMapper, OpenApiPermission> implements OpenApiPermissionService {
    @Override
    public List<OpenApiPermission> findByAuthId(String authId) {
        return baseMapper.selectList(Wrappers.lambdaQuery(OpenApiPermission.class).eq(OpenApiPermission::getApiAuthId, authId));
    }
}
