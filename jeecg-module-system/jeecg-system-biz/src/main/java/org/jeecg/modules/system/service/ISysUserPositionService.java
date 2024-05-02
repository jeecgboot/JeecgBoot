package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserPosition;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * @Description: 用户职位关系表
 * @Author: jeecg-boot
 * @Date:   2023-02-14
 * @Version: V1.0
 */
public interface ISysUserPositionService extends IService<SysUserPosition> {

    /**
     * 获取职位用户列表
     * @param page
     * @param positionId
     * @return
     */
    IPage<SysUser> getPositionUserList(Page<SysUser> page, String positionId);

    /**
     * 添加成员到用户职位关系表
     * @param userIds
     * @param positionId
     */
    void saveUserPosition(String userIds, String positionId);

    /**
     * 通过职位id删除用户职位关系表
     * @param positionId
     */
    void removeByPositionId(String positionId);

    /**
     * 移除成员
     * @param userIds
     * @param positionId
     */
    void removePositionUser(String userIds, String positionId);
}
