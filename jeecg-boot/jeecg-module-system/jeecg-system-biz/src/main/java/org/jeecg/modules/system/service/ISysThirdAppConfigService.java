package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.models.auth.In;
import org.jeecg.modules.system.entity.SysThirdAppConfig;

import java.util.List;

/**
 * @Description: 第三方配置表
 * @Author: jeecg-boot
 * @Date:   2023-02-03
 * @Version: V1.0
 */
public interface ISysThirdAppConfigService extends IService<SysThirdAppConfig>{

    /**
     * 根据租户id获取钉钉/企业微信配置
     * @param tenantId
     * @return
     */
    List<SysThirdAppConfig> getThirdConfigListByThirdType(int tenantId);

    /**
     * 根据租户id和第三方类别获取第三方配置
     * @param tenantId
     * @param thirdType
     * @return
     */
    SysThirdAppConfig getThirdConfigByThirdType(Integer tenantId, String thirdType);

    /**
     * 根据应用key获取第三方表配置
     * @param clientId
     */
    List<SysThirdAppConfig> getThirdAppConfigByClientId(String clientId);
}
