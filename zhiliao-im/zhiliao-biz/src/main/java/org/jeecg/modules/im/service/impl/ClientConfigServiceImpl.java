package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.im.entity.ClientConfig;
import org.jeecg.modules.im.mapper.ClientConfigMapper;
import org.jeecg.modules.im.service.ClientConfigService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 应用配置 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-02-03
 */
@Service
public class ClientConfigServiceImpl extends BaseServiceImpl<ClientConfigMapper, ClientConfig> implements ClientConfigService {

    @Autowired
    private ClientConfigMapper clientConfigMapper;
    @Override
    public ClientConfig get() {
        return clientConfigMapper.get();
    }
}
