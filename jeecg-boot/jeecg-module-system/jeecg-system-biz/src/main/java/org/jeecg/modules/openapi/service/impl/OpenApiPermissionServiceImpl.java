package org.jeecg.modules.openapi.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.openapi.entity.OpenApi;
import org.jeecg.modules.openapi.entity.OpenApiPermission;
import org.jeecg.modules.openapi.mapper.OpenApiPermissionMapper;
import org.jeecg.modules.openapi.service.OpenApiPermissionService;
import org.jeecg.modules.openapi.service.OpenApiService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @date 2024/12/19 17:44
 */
@Service
public class OpenApiPermissionServiceImpl extends ServiceImpl<OpenApiPermissionMapper, OpenApiPermission> implements OpenApiPermissionService {
    @Resource
    private OpenApiService openApiService;
    @Override
    public List<OpenApiPermission> findByAuthId(String authId) {
        return baseMapper.selectList(Wrappers.lambdaQuery(OpenApiPermission.class).eq(OpenApiPermission::getApiAuthId, authId));
    }

    @Override
    public Result<?> getOpenApi(String apiAuthId) {
        List<OpenApi> openApis = openApiService.list();
        if (CollectionUtil.isEmpty(openApis)) {
            return Result.error("接口不存在");
        }
        List<OpenApiPermission> openApiPermissions = baseMapper.selectList(Wrappers.<OpenApiPermission>lambdaQuery().eq(OpenApiPermission::getApiAuthId, apiAuthId));
        if (CollectionUtil.isNotEmpty(openApiPermissions)) {
            Map<String, OpenApi> openApiMap = openApis.stream().collect(Collectors.toMap(OpenApi::getId, o -> o));
            for (OpenApiPermission openApiPermission : openApiPermissions) {
                OpenApi openApi = openApiMap.get(openApiPermission.getApiId());
                if (openApi!=null) {
                    openApi.setIfCheckBox("1");
                }
            }
        }
        return Result.ok(openApis);
    }

    @Override
    public void add(OpenApiPermission openApiPermission) {
        this.remove(Wrappers.<OpenApiPermission>lambdaQuery().eq(OpenApiPermission::getApiAuthId, openApiPermission.getApiAuthId()));
        List<String> list = Arrays.asList(openApiPermission.getApiId().split(","));
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(l->{
                if (StrUtil.isNotEmpty(l)){
                    OpenApiPermission saveApiPermission = new OpenApiPermission();
                    saveApiPermission.setApiId(l);
                    saveApiPermission.setApiAuthId(openApiPermission.getApiAuthId());
                    this.save(saveApiPermission);
                }
            });
        }
    }
}
