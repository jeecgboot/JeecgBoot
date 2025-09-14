package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.system.entity.SysTenantPack;
import org.jeecg.modules.system.entity.SysTenantPackUser;

import java.util.List;

/**
 * @Description: 租户产品包用户关系
 * @Author: jeecg-boot
 * @Date:   2023-02-16
 * @Version: V1.0
 */
public interface SysTenantPackUserMapper extends BaseMapper<SysTenantPackUser> {


    /**
     * 查询租户下 特定角色的人员列表
     * @param tenantId
     * @param packCodeList
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    List<String> queryTenantPackUserNameList(@Param("tenantId") Integer tenantId, @Param("packCodeList") List<String> packCodeList); 

    /**
     * 判断当前用户在该租户下是否拥有管理员的权限
     * @param userId
     * @param tenantId
     * @return
     */
    Long izHaveBuyAuth(@Param("userId") String userId, @Param("tenantId") Integer tenantId);

    /**
     * 根据租户id 删除租户产品包下的 用户
     * @param tenantId
     */
    void deletePackUserByTenantId(@Param("tenantId") Integer tenantId, @Param("userIds") List<String> userIds);

    /**
     * 根据多个租户id 删除租户产品包下的 用户
     * @param
     */
    void deletePackUserByTenantIds(@Param("tenantIds") List<Integer> tenantIds);

    /**
     * 根据用户id和租户id获取当前租户用户下的产品包id
     *
     * @param tenantId
     * @param userId
     * @return
     */
    @Select("select pack_id from sys_tenant_pack_user where tenant_id = #{tenantId} and user_id = #{userId}")
    List<String> getPackIdByTenantIdAndUserId(@Param("tenantId") Integer tenantId, @Param("userId") String userId);
    
    /**
     * 根据租户id获取用户的产品包列表
     * 
     * @param tenantId
     * @return
     */
    @Select("select id,pack_name,pack_code,pack_type from sys_tenant_pack where tenant_id = #{tenantId}")
    List<SysTenantPack> getPackListByTenantId(@Param("tenantId") Integer tenantId);
}
