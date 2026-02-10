package org.jeecg.modules.tenant.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Project：jeecg-boot
 * Created by：lc5198
 * Date：2026/2/10 13:21
 */
@Repository
public interface TenantMapper {
    List<String> getRoleIdByUserId(@Param("userId") String userId, @Param("tenantId") int tenantId);
}
