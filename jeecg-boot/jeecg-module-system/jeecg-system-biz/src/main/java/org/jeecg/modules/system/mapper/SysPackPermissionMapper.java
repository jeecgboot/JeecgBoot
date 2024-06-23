package org.jeecg.modules.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysPackPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 产品包菜单关系表
 * @Author: jeecg-boot
 * @Date:   2022-12-31
 * @Version: V1.0
 */
public interface SysPackPermissionMapper extends BaseMapper<SysPackPermission> {

    /**
     * 通过产品包id获取菜单id
     * @param packId
     * @return
     */
    List<String> getPermissionsByPackId(@Param("packId") String packId);

    /**
     * 删除产品包对应的菜单权限
     *
     * @param tenantIdList
     */
    void deletePackPermByTenantIds(@Param("tenantIdList") List<Integer> tenantIdList);
    
}
