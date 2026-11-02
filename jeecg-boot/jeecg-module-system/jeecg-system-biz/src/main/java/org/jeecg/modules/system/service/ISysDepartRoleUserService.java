package org.jeecg.modules.system.service;

import org.jeecg.modules.system.entity.SysDepartRoleUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 部门角色人员信息
 * @Author: jeecg-boot
 * @Date:   2020-02-13
 * @Version: V1.0
 */
public interface ISysDepartRoleUserService extends IService<SysDepartRoleUser> {

    /**
     * 添加用户与部门关联
     * @param userId 用户id
     * @param newRoleId 新的角色id
     * @param oldRoleId 旧的角色id
     */
    void deptRoleUserAdd(String userId,String newRoleId,String oldRoleId);

    /**
     * 取消用户与部门关联，删除关联关系
     * @param userIds
     * @param depId
     */
    void removeDeptRoleUser(List<String> userIds,String depId);

    /**
     * 清理部门角色下所有用户的Shiro权限缓存
     * @param roleId 部门角色id
     * @author scott
     * @since 2026-10-29 【LHZP-1129】部门角色授权后清理相关用户权限缓存，无需重新登录即生效
     */
    void clearDeptRoleUserAuthCache(String roleId);
}
