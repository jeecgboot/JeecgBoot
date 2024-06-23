package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.entity.SysTenant;
import org.jeecg.modules.system.entity.SysTenantPackUser;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.vo.tenant.TenantDepartAuthInfo;
import org.jeecg.modules.system.vo.tenant.TenantPackModel;
import org.jeecg.modules.system.vo.tenant.TenantPackUser;
import org.jeecg.modules.system.vo.tenant.TenantPackUserCount;

import java.util.Collection;
import java.util.List;

/**
 * @Description: 租户service接口
 * @author: jeecg-boot
 */
public interface ISysTenantService extends IService<SysTenant> {

    /**
     * 查询有效的租户
     *
     * @param idList
     * @return
     */
    List<SysTenant> queryEffectiveTenant(Collection<Integer> idList);

    /**
     * 返回某个租户被多少个用户引用了
     *
     * @param id
     * @return
     */
    Long countUserLinkTenant(String id);

    /**
     * 根据ID删除租户，会判断是否已被引用
     *
     * @param id
     * @return
     */
    boolean removeTenantById(String id);

    /**
     * 邀请用户加入租户,通过手机号
     * @param ids
     * @param phone
     */
    void invitationUserJoin(String ids, String phone);

    /**
     * 请离用户（租户）
     * @param userIds
     * @param tenantId
     */
    void leaveTenant(String userIds, String tenantId);

    /**
     * 添加租户，并将创建的用户加入关系表
     * @param sysTenant
     * @param userId
     */
    Integer saveTenantJoinUser(SysTenant sysTenant, String userId);

    /**
     * 保存租户
     * @param sysTenant
     */
    void saveTenant(SysTenant sysTenant);

    /**
     * 通过门牌号加入租户
     * @param sysTenant
     * @param userId
     */
    Integer joinTenantByHouseNumber(SysTenant sysTenant, String userId);

    /**
     * 统计一个人创建了多少个租户
     * 
     * @param userId
     * @return
     */
    Integer countCreateTenantNum(String userId);

    /**
     * 获取租户回收站的数据
     * @param page
     * @param sysTenant
     * @return
     */
    IPage<SysTenant> getRecycleBinPageList(Page<SysTenant> page, SysTenant sysTenant);

    /**
     * 彻底删除租户
     * @param ids
     */
    void deleteTenantLogic(String ids);

    /**
     * 还原租户
     * @param ids
     */
    void revertTenantLogic(String ids);

    /**
     * 退出租户
     * @param userId
     * @param userId
     * @param username
     */
    void exitUserTenant(String userId, String username, String tenantId);

    /**
     * 变更租户拥有者
     * @param userId
     * @param tenantId
     */
    void changeOwenUserTenant(String userId, String tenantId);

    /**
     * 邀请用户到租户。通过手机号匹配
     * @param phone
     * @param departId
     * @return
     */
    Result<String> invitationUser(String phone, String departId);

    /**
     * 进入应用组织页面 查询用户是否有 超级管理员的权限
     * @param tenantId
     * @return
     */
    TenantDepartAuthInfo getTenantDepartAuthInfo(Integer tenantId);


    /**
     * 获取 租户产品包-3个默认admin的人员数量
     * @param tenantId
     * @return
     */
    List<TenantPackUserCount> queryTenantPackUserCount(Integer tenantId);

    /**
     * 查询租户产品包信息
     * @param model
     * @return
     */
    TenantPackModel queryTenantPack(TenantPackModel model);

    /**
     * 添加多个用户和产品包的关系数据
     * @param sysTenantPackUser
     */
    void addBatchTenantPackUser(SysTenantPackUser sysTenantPackUser);

    /**
     * 添加用户和产品包的关系数据 带日志记录的
     * @param sysTenantPackUser
     */
    void addTenantPackUser(SysTenantPackUser sysTenantPackUser);

    /**
     * 移除用户和产品包的关系数据 带日志记录的
     * @param sysTenantPackUser
     */
    void deleteTenantPackUser(SysTenantPackUser sysTenantPackUser);


    /**
     * 查询申请的用户列表
     */
    List<TenantPackUser> getTenantPackApplyUsers(Integer tenantId);

    /**
     * 个人 申请成为管理员
     * @param sysTenantPackUser
     */
    void doApplyTenantPackUser(SysTenantPackUser sysTenantPackUser);

    /**
     * 申请通过 成为管理员
     * @param sysTenantPackUser
     */
    void passApply(SysTenantPackUser sysTenantPackUser);

    /**
     * 拒绝申请 成为管理员
     * @param sysTenantPackUser
     */
    void deleteApply(SysTenantPackUser sysTenantPackUser);

    /**
     * 产品包用户列表
     * @param tenantId
     * @param packId
     * @param status
     * @param page
     * @return
     */
    IPage<TenantPackUser> queryTenantPackUserList(String tenantId, String packId, Integer status, Page<TenantPackUser> page);

    /**
     * 查看是否已经申请过了管理员
     * @return
     */
    Long getApplySuperAdminCount();

    /**
     * 发送同意或者拒绝消息
     * 
     * @param user 
     * @param content 
     */
    void sendMsgForAgreeAndRefuseJoin(SysUser user, String content);

    /**
     * 根据密码删除当前用户
     * 
     * @param sysUser
     * @param tenantId
     */
    void deleteUserByPassword(SysUser sysUser, Integer tenantId);

    /**
     * 根据用户id获取租户信息
     * @param userId
     * @return
     */
    List<SysTenant> getTenantListByUserId(String userId);
}
