package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.im.entity.MucConfig;
import org.jeecg.modules.im.mapper.MucConfigMapper;
import org.jeecg.modules.im.service.MucConfigService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 群组默认配置 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-27
 */
@Service
public class MucConfigServiceImpl extends BaseServiceImpl<MucConfigMapper, MucConfig> implements MucConfigService {

    @Autowired
    private MucConfigMapper mucConfigMapper;
    @Override
    public MucConfig findDefault() {
        MucConfig config = mucConfigMapper.findDefault();
        if(config==null){
            config = new MucConfig();
            if(!save(config)){
                return null;
            }
        }
        return config;
    }

    @Override
    public MucConfig findByMuc(Integer mucId) {
        return mucConfigMapper.findByMuc(mucId);
    }
}
