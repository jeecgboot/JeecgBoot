package org.jeecg.modules.im.service.impl;

import org.jeecg.modules.im.entity.SysConfig;
import org.jeecg.modules.im.mapper.SysConfigMapper;
import org.jeecg.modules.im.service.SysConfigService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统配置 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-02-03
 */
@Service
public class SysConfigServiceImpl extends BaseServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {

    @Autowired
    private SysConfigMapper sysConfigMapper;
    @Override
    public SysConfig get() {
        return sysConfigMapper.get();
    }
}
