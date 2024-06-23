package org.jeecg.modules.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.jeecg.modules.system.entity.SysTenant;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserTenant;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.system.vo.SysUserTenantVo;
import org.jeecg.modules.system.vo.thirdapp.JwUserDepartVo;

/**
 * @Description: sys_user_tenant_relation
 * @Author: jeecg-boot
 * @Date:   2022-12-23
 * @Version: V1.0
 */
public interface SysUserTenantMapper extends BaseMapper<SysUserTenant> {

    /**
     * 通过租户id获取数据
     * @param page
     * @param userTenantId
     * @return
     */
    List<SysUser> getPageUserList(@Param("page") Page<SysUser> page,@Param("userTenantId") Integer userTenantId,@Param("user") SysUser user);

    /**
     * 根据租户id获取用户ids
     * @param tenantId
     * @return
     */
    List<String> getUserIdsByTenantId(@Param("tenantId") Integer tenantId);

    /**
     * 通过用户id获取租户ids
     * @param userId
     * @return
     */
    List<Integer> getTenantIdsByUserId(@Param("userId") String userId);


    
    //==============================================================================================================================
    /**
     * 通过用户id获取租户列表
     * @param userId
     * @return
     */
    List<SysUserTenantVo> getTenantListByUserId(@Param("userId") String userId, @Param("userTenantStatus") List<String> userTenantStatus);
    
    /**
     * 通过状态、当前登录人的用户名，租户id，查询用户id
     * @param tenantId
     * @param statusList
     * @param username
     * @return
     */
    List<String> getUserIdsByCreateBy(@Param("tenantId") Integer tenantId, @Param("userTenantStatus")  List<String> statusList, @Param("username") String username);

    /**
     * 联查用户和租户审核状态
     * @param page
     * @param status
     * @param tenantId
     * @return
     */
    List<SysUserTenantVo> getUserTenantPageList(@Param("page") Page<SysUserTenantVo> page, @Param("status") List<String> status, @Param("user") SysUser user, @Param("tenantId") Integer tenantId);

    /**
     * 根据用户id获取租户id，没有状态值(如获取租户已经存在，只不过是被拒绝或者审批中)
     * @param userId
     * @return
     */
    List<Integer> getTenantIdsNoStatus(@Param("userId") String userId);
    //==============================================================================================================================

    /**
     * 统计一个人创建了多少个租户
     *
     * @param userId
     * @return
     */
    Integer countCreateTenantNum(String userId);

    /**
     * 取消离职
     * @param userIds
     * @param tenantId
     */
    void putCancelQuit(@Param("userIds") List<String> userIds, @Param("tenantId") Integer tenantId);

    /**
     * 判断当前用户是否已在该租户下面
     * @param userId
     * @param tenantId
     */
    Integer userTenantIzExist(@Param("userId") String userId, @Param("tenantId") int tenantId);

    /**
     * 查询未被注销的租户
     * @param userId
     * @return
     */
    List<SysTenant> getTenantNoCancel(@Param("userId") String userId);

    /**
     * 根据用户id获取我的租户
     * @param page
     * @param userId
     * @param userTenantStatus
     * @return
     */
    List<SysTenant> getTenantPageListByUserId(@Param("page") Page<SysTenant> page, @Param("userId") String userId, @Param("userTenantStatus") List<String> userTenantStatus,@Param("sysUserTenantVo") SysUserTenantVo sysUserTenantVo);

    /**
     * 同意加入租户
     * @param userId
     * @param tenantId
     */
    @Update("update sys_user_tenant set status = '1' where user_id = #{userId} and tenant_id = #{tenantId}")
    void agreeJoinTenant(@Param("userId") String userId, @Param("tenantId") Integer tenantId);

    /**
     * 拒绝加入租户
     * @param userId
     * @param tenantId
     */
    @Delete("delete from sys_user_tenant where user_id = #{userId} and tenant_id = #{tenantId}")
    void refuseJoinTenant(@Param("userId") String userId, @Param("tenantId") Integer tenantId);

    /**
     * 根据用户id和租户id获取用户租户中间表信息
     *
     * @param userId
     * @param tenantId
     * @return
     */
    @Select("select id,user_id,tenant_id,create_by,status from sys_user_tenant where user_id = #{userId} and tenant_id = #{tenantId}")
    SysUserTenant getUserTenantByTenantId(@Param("userId") String userId, @Param("tenantId") Integer tenantId);

    /**
     * 删除租户下的用户
     *
     * @param tenantIds
     */
    void deleteUserByTenantId(@Param("tenantIds") List<Integer> tenantIds);

    /**
     * 获取租户下的成员数量
     *
     * @param tenantId
     * @param tenantStatus
     * @return
     */
    Long getUserCount(Integer tenantId, String tenantStatus);
    
    /**
     * 根据租户id和名称获取用户数据
     * @param tenantId
     * @return
     */
    List<JwUserDepartVo> getUsersByTenantIdAndName(@Param("tenantId") Integer tenantId);

    /**
     * 根据多个用户id获取租户id
     *
     * @param userIds
     * @return
     */
    List<Integer> getTenantIdsByUserIds(@Param("userIds") List<String> userIds);
}
