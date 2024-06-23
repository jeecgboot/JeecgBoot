package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.system.entity.SysTenant;
import org.jeecg.modules.system.vo.tenant.TenantPackUser;
import org.jeecg.modules.system.vo.tenant.TenantPackUserCount;
import org.jeecg.modules.system.vo.tenant.UserDepart;
import org.jeecg.modules.system.vo.tenant.UserPosition;

import java.util.List;

/**
 * @Description: 租户mapper接口
 * @author: jeecg-boot
 */
public interface SysTenantMapper extends BaseMapper<SysTenant> {

    /**
     * 获取最大值id
     */
    @Select("select MAX(id) id FROM sys_tenant")
    Integer getMaxTenantId();
    
    /**
     * 获取租户回收站的数据假删除
     * @param page
     * @param sysTenant
     * @return
     */
    List<SysTenant> getRecycleBinPageList(@Param("page") Page<SysTenant> page, @Param("sysTenant") SysTenant sysTenant);
    
    /**
     * 彻底删除租户
     * @param tenantId
     */
    Integer deleteByTenantId(@Param("tenantIds") List<Integer> tenantId);

    /**
     * 租户还原
     * @param list
     * @return
     */
    Integer revertTenantLogic(@Param("tenantIds")List<Integer> list);

    /**
     * 用于统计 租户产品包的人员数量
     * @param tenantId
     * @return
     */
    List<TenantPackUserCount> queryTenantPackUserCount(@Param("tenantId") Integer tenantId);

    /**
     * 查询人员是不是租户产品包的 超级管理员
     * @param tenantId
     * @param userId
     * @return
     */
    Integer querySuperAdminCount(@Param("tenantId") Integer tenantId, @Param("userId") String userId);

    /**
     * 查询人员的产品包编码
     * @param tenantId
     * @param userId
     * @return
     */
    List<String> queryUserPackCode(@Param("tenantId") Integer tenantId, @Param("userId") String userId);

    /**
     * 查询产品包关联的用户列表
     * @param tenantId
     * @param packId
     * @param packUserStatus
     * @return
     */
    List<TenantPackUser> queryPackUserList(@Param("tenantId") Integer tenantId, @Param("packId") String packId, @Param("packUserStatus") Integer packUserStatus);


    /**
     * 根据用户ID 查询部门
     * @param userIdList
     * @return
     */
    List<UserDepart> queryUserDepartList(@Param("userIdList") List<String> userIdList);

    /**
     * 根据用户ID 查询职位
     * @param userIdList
     * @return
     */
    List<UserPosition> queryUserPositionList(@Param("userIdList") List<String> userIdList);

    /**
     * 查询产品包关联的用户列表
     * @param page
     * @param tenantId
     * @param packId
     * @param status
     * @return
     */
    List<TenantPackUser> queryTenantPackUserList(@Param("page") Page<TenantPackUser> page, @Param("tenantId") String tenantId, @Param("packId") String packId, @Param("status") Integer status);


    /**
     * 根据租户ID 查询租户
     * @param id
     * @return
     */
    @Select("select * from sys_tenant where id = #{id}")
    SysTenant querySysTenant(@Param("id") Integer id);

    /**
     * 查看是否已经申请过了超级管理员
     * @param userId
     * @param tenantId
     * @return
     */
    Long getApplySuperAdminCount(@Param("userId") String userId, @Param("tenantId") Integer tenantId);

    /**
     * 租户是否存在
     * @param tenantId
     * @return
     */
    @Select("select count(1) from sys_tenant where id = #{tenantId} and del_flag = 0")
    Long tenantIzExist(@Param("tenantId") Integer tenantId);

    /**
     * 根据用户id获取租户
     * @param userId
     * @return
     */
    List<SysTenant> getTenantListByUserId(@Param("userId") String userId);
}
