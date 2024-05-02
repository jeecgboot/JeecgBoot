package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.system.entity.SysTenant;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserTenant;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.vo.SysUserTenantVo;

import java.util.List;

/**
 * @Description: sys_user_tenant_relation
 * @Author: jeecg-boot
 * @Date:   2022-12-23
 * @Version: V1.0
 */
public interface ISysUserTenantService extends IService<SysUserTenant> {

    /**
     * 通过租户id获取数据
     * @param page
     * @param userTenantId
     * @param user
     * @return
     */
    Page<SysUser> getPageUserList(Page<SysUser> page, Integer userTenantId, SysUser user);

    /**
     * 设置租户id
     * @param records
     * @return
     */
    List<SysUser> setUserTenantIds(List<SysUser> records);

    /**
     * 获取租户id获取用户ids
     * @param tenantId
     * @return
     */
    List<String> getUserIdsByTenantId(Integer tenantId);

    /**
     * 通过用户id获取租户ids
     * @param userId
     * @return
     */
    List<Integer> getTenantIdsByUserId(String userId);
    
    /**
     * 通过用户id获取租户列表
     * @param userId
     * @param userTenantStatus
     * @return
     */
    List<SysUserTenantVo> getTenantListByUserId(String userId, List<String>  userTenantStatus);
    
    /**
     * 更新用户租户状态
     * @param id
     * @param tenantId
     * @param userTenantStatus
     */
    void updateUserTenantStatus(String id, String tenantId, String userTenantStatus);

    /**
     * 联查用户和租户审核状态
     * @param page
     * @param status 租户用户状态，默认为1正常
     * @param user
     * @return
     */
    IPage<SysUserTenantVo> getUserTenantPageList(Page<SysUserTenantVo> page, List<String> status, SysUser user, Integer tenantId);

    /**
     * 取消离职
     * @param userIds
     * @param tenantId
     */
    void putCancelQuit(List<String> userIds, Integer tenantId);

    /**
     * 验证用户是否已存在
     * @param userId
     * @param tenantId
     * @return
     */
    Integer userTenantIzExist(String userId, Integer tenantId);

    /**
     * 根据用户id获取我的租户
     *
     * @param page
     * @param userId
     * @param userTenantStatus
     * @param sysUserTenantVo
     * @return
     */
    IPage<SysTenant> getTenantPageListByUserId(Page<SysTenant> page, String userId, List<String> userTenantStatus,SysUserTenantVo sysUserTenantVo);

    /**
     * 同意加入租户
     * @param userId
     * @param tenantId
     */
    void agreeJoinTenant(String userId, Integer tenantId);

    /**
     * 拒绝加入租户
     * @param userId
     * @param tenantId
     */
    void refuseJoinTenant(String userId, Integer tenantId);

    /**
     * 根据用户id和租户id获取用户租户中间表信息
     * @param userId
     * @param tenantId
     * @return
     */
    SysUserTenant getUserTenantByTenantId(String userId, Integer tenantId);

    /**
     * 获取租户下的成员数量
     * @param tenantId
     * @param tenantStatus
     * @return
     */
    Long getUserCount(Integer tenantId, String tenantStatus);
}
