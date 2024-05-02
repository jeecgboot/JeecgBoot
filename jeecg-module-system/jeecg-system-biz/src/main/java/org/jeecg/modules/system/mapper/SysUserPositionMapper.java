package org.jeecg.modules.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserPosition;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.vo.SysUserPositionVo;

/**
 * @Description: 用户职位关系表
 * @Author: jeecg-boot
 * @Date:   2023-02-14
 * @Version: V1.0
 */
public interface SysUserPositionMapper extends BaseMapper<SysUserPosition> {

    /**
     * 获取职位用户列表
     * @param page
     * @param positionId
     * @return
     */
    List<SysUser> getPositionUserList(@Param("page") Page<SysUser> page, @Param("positionId") String positionId);

    /**
     * 获取成员是否存在职位中
     * @param userId
     * @param positionId
     * @return
     */
    @Select("SELECT count(*) FROM sys_user_position WHERE user_id = #{userId} and position_id = #{positionId}")
    Long getUserPositionCount(@Param("userId") String userId, @Param("positionId") String positionId);

    /**
     * 通过职位id删除用户职位关系表
     * @param positionId
     */
    @Delete("DELETE FROM sys_user_position WHERE position_id = #{positionId} ")
    void removeByPositionId(@Param("positionId") String positionId);

    /**
     * 职位列表移除成员
     * @param userIdList
     * @param positionId
     */
    void removePositionUser(@Param("userIdList") List<String> userIdList, @Param("positionId") String positionId);

    /**
     * 根据用户id查询职位id
     * @param userId
     * @return
     */
    List<String> getPositionIdByUserId(@Param("userId") String userId);


    /**
     * 根据用户ID和租户ID获取职位id
     * @param userId
     * @param tenantId
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    List<String> getPositionIdByUserTenantId(@Param("userId")String userId, @Param("tenantId")Integer tenantId);

    /**
     * 根据用户id获取用户职位
     * @param userIdList
     * @param tenantId
     * @return
     */
    List<SysUserPositionVo> getPositionIdByUsersTenantId(@Param("userIdList") List<SysUser> userIdList, @Param("tenantId") Integer tenantId);

    /**
     * 根据职位名称和租户id，删除用户职位关系表
     * @param positionNames
     * @param tenantId
     * @param userId
     */
    void deleteUserPosByNameAndTenantId(@Param("positionNames") List<String> positionNames, @Param("tenantId") Integer tenantId, @Param("userId") String userId);
}
