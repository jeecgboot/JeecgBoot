package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.im.entity.ChannelPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 频道管理员权限 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2022-04-28
 */
@Mapper
public interface ChannelPermissionMapper extends BaseMapper<ChannelPermission> {

    ChannelPermission findDefault();
}
