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
}
