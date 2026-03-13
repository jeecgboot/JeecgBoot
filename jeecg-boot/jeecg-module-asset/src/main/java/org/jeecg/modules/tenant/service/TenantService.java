package org.jeecg.modules.tenant.service;

import java.util.List;

/**
 * Project：jeecg-boot
 * Created by：lc5198
 * Date：2026/2/10 14:25
 */
public interface TenantService {
    List<String> getRoleIdByUserId(String userId, int tenantId);
}
