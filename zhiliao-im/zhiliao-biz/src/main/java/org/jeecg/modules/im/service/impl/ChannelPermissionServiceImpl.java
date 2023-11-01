package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.im.entity.ChannelPermission;
import org.jeecg.modules.im.mapper.ChannelPermissionMapper;
import org.jeecg.modules.im.service.ChannelPermissionService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 频道管理员权限 服务实现类
 * </p>
 *
 * @author junko
 * @since 2022-04-28
 */
@Service
public class ChannelPermissionServiceImpl extends BaseServiceImpl<ChannelPermissionMapper, ChannelPermission> implements ChannelPermissionService {

    @Autowired
    private ChannelPermissionMapper channelPermissionMapper;
    @Override
    public ChannelPermission findDefault() {
        ChannelPermission config = channelPermissionMapper.findDefault();
        if(config==null){
            config = new ChannelPermission();
            if(!save(config)){
                return null;
            }
        }
        return config;
    }
}
