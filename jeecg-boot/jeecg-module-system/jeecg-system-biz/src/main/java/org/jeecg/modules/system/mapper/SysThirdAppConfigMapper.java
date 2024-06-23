package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.system.entity.SysThirdAppConfig;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Description: 第三方配置表
 * @Author: jeecg-boot
 * @Date:   2023-02-03
 * @Version: V1.0
 */
public interface SysThirdAppConfigMapper  extends BaseMapper<SysThirdAppConfig> {

    /**
     * 根据租户id获取钉钉/企业微信配置
     * @param tenantId
     * @return
     */
    List<SysThirdAppConfig> getThirdConfigListByThirdType(@Param("tenantId") int tenantId);

    /**
     * 根据租户id和第三方类别获取第三方配置
     * @param tenantId
     * @param thirdType
     * @return
     */
    SysThirdAppConfig getThirdConfigByThirdType(@Param("tenantId") int tenantId, @Param("thirdType") String thirdType);
}
