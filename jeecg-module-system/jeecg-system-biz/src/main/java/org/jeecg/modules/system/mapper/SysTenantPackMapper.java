package org.jeecg.modules.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
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
}
