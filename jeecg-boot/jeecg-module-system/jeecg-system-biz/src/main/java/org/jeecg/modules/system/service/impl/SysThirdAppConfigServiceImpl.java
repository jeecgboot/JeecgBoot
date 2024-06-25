package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.system.entity.SysThirdAppConfig;
import org.jeecg.modules.system.mapper.SysThirdAppConfigMapper;
import org.jeecg.modules.system.service.ISysThirdAppConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 第三方配置表
 * @Author: jeecg-boot
 * @Date:   2023-02-03
 * @Version: V1.0
 */
@Service
@Slf4j
public class SysThirdAppConfigServiceImpl extends ServiceImpl<SysThirdAppConfigMapper, SysThirdAppConfig> implements ISysThirdAppConfigService {

    @Autowired
    private SysThirdAppConfigMapper configMapper;

    @Override
    public List<SysThirdAppConfig> getThirdConfigListByThirdType(int tenantId) {
        return configMapper.getThirdConfigListByThirdType(tenantId);
    }

    @Override
    public SysThirdAppConfig getThirdConfigByThirdType(Integer tenantId, String thirdType) {
        return configMapper.getThirdConfigByThirdType(tenantId,thirdType);
    }

    @Override
    public List<SysThirdAppConfig> getThirdAppConfigByClientId(String clientId) {
        LambdaQueryWrapper<SysThirdAppConfig> query = new LambdaQueryWrapper<>();
        query.eq(SysThirdAppConfig::getClientId,clientId);
        List<SysThirdAppConfig> sysThirdAppConfigs = configMapper.selectList(query);
        return sysThirdAppConfigs;
    }
}
