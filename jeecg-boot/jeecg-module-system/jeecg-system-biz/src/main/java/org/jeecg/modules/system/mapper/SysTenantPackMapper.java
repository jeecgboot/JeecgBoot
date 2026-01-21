package org.jeecg.modules.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.system.entity.SysTenantPack;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 租户产品包
 * @Author: jeecg-boot
 * @Date:   2022-12-31
 * @Version: V1.0
 */
public interface SysTenantPackMapper extends BaseMapper<SysTenantPack> {

    /**
     * 删除租户产品包
     *
     * @param tenantIdList
     */
    void deletePackByTenantIds(@Param("tenantIdList") List<Integer> tenantIdList);

    /**
     * 根据租户id和产品包的code获取租户套餐id
     *
     * @param tenantId
     */
    @Select("select id from sys_tenant_pack where tenant_id = #{tenantId} and (pack_code not in('superAdmin','accountAdmin','appAdmin') or pack_code is null) and iz_sysn = '1'")
    List<String> getPackIdByPackCodeAndTenantId(@Param("tenantId") Integer tenantId);

    /**
     * 是否为拥有管理用户权限【accountAdmin，superAdmin】
     * @param tenantId
     * @param userId
     * @return
     */
    @Select("select count(1) from sys_tenant_pack_user where user_id = #{userId} and tenant_id = #{tenantId} and pack_id in(select id from sys_tenant_pack where tenant_id = #{tenantId} and pack_type = 'custom' and pack_code in('accountAdmin','superAdmin'))")
    long izHaveManageUserAuth(@Param("tenantId") String tenantId,@Param("userId") String userId);
}
