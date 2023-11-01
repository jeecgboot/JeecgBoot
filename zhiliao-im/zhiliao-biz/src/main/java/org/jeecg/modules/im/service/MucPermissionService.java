package org.jeecg.modules.im.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.MucPermission;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.query_helper.QMucPermission;

/**
 * <p>
 * 群组管理员权限 服务类
 * </p>
 *
 * @author junko
 * @since 2023-03-27
 */
public interface MucPermissionService extends IService<MucPermission> {
    MucPermission findByManager(Integer managerId);
    MucPermission findDefaultOfMuc(Integer mucId);

    Integer deleteOne(Integer mucId, Integer managerId);

    Result<Object> findByUserOfMuc(Integer userId, Integer mucId);

    Result<Object> updateByCondition(QMucPermission q);
}
