package org.jeecg.modules.im.service;

import org.jeecg.modules.im.entity.ChannelPermission;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 频道管理员权限 服务类
 * </p>
 *
 * @author junko
 * @since 2022-04-28
 */
public interface ChannelPermissionService extends IService<ChannelPermission> {
    ChannelPermission findDefault();
}
