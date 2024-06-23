package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.system.entity.SysRole;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @Author scott
 * @since 2018-12-19
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {
    /**
     * 查询全部的角色（不做租户隔离）
     * @param page
     * @param role
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    List<SysRole> listAllSysRole(@Param("page") Page<SysRole> page, @Param("role") SysRole role);

    /**
     * 查询角色是否存在不做租户隔离
     *
     * @param roleCode
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    SysRole getRoleNoTenant(@Param("roleCode") String roleCode);

    /**
     * 根据用户id查询用户拥有的角色Code
     *
     * @param userId
     * @param tenantId
     * @return
     */
    List<SysRole> getRoleCodeListByUserId(@Param("userId") String userId, @Param("tenantId") Integer tenantId);

    /**
     * 删除角色与用户关系
     * @Author scott
     * @Date 2019/12/13 16:12
     * @param roleId
     */
    @Delete("delete from sys_user_role where role_id = #{roleId}")
    void deleteRoleUserRelation(@Param("roleId") String roleId);


    /**
     * 删除角色与权限关系
     * @Author scott
     * @param roleId
     * @Date 2019/12/13 16:12
     */
    @Delete("delete from sys_role_permission where role_id = #{roleId}")
    void deleteRolePermissionRelation(@Param("roleId") String roleId);

    /**
     * 根据角色id和当前租户判断当前角色是否存在这个租户中
     * @param id
     * @return
     */
    @Select("select count(*) from sys_role where id=#{id} and tenant_id=#{tenantId}")
    Long getRoleCountByTenantId(@Param("id") String id, @Param("tenantId") Integer tenantId);
}
