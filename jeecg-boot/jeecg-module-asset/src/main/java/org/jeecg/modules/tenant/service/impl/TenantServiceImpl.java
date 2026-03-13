package org.jeecg.modules.tenant.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.tenant.mapper.TenantMapper;
import org.jeecg.modules.tenant.service.TenantService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Project：jeecg-boot
 * Created by：lc5198
 * Date：2026/2/10 14:25
 */
@Slf4j
@Service
public class TenantServiceImpl implements TenantService {
    private final TenantMapper tenantMapper;

    public TenantServiceImpl(TenantMapper tenantMapper) {
        this.tenantMapper = tenantMapper;
    }

    @Override
    public List<String> getRoleIdByUserId(String userId, int tenantId) {
        return tenantMapper.getRoleIdByUserId(userId, tenantId);
    }
}
