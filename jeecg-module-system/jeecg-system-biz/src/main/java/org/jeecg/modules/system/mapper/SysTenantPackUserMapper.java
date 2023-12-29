package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
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
    Long izHaveBuyAuth(@Param("userId") String userId, @Param("tenantId") String tenantId);
}
