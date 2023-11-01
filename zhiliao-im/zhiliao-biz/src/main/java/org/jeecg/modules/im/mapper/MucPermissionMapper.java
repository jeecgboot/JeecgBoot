package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.im.entity.MucPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 群组管理员权限 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2023-03-27
 */
@Mapper
public interface MucPermissionMapper extends BaseMapper<MucPermission> {
    MucPermission findByManager(Integer managerId);
    MucPermission findDefaultOfMuc(Integer mucId);

    Integer deleteOne(Integer mucId, Integer managerId);
}
