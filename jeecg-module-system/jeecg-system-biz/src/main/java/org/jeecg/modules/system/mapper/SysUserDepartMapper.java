package org.jeecg.modules.system.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserDepart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 用户部门mapper接口
 * @author: jeecg-boot
 */
public interface SysUserDepartMapper extends BaseMapper<SysUserDepart>{

    /**
     * 通过用户id查询部门用户
     * @param userId 用户id
     * @return List<SysUserDepart>
     */
	List<SysUserDepart> getUserDepartByUid(@Param("userId") String userId);

	/**
	 *  查询指定部门下的用户 并且支持用户真实姓名模糊查询
	 * @param orgCode
	 * @param realname
	 * @return
	 */
	List<SysUser> queryDepartUserList(@Param("orgCode") String orgCode, @Param("realname") String realname);

	/**
	 * 根据部门查询部门用户
	 * @param page
	 * @param orgCode
	 * @param username
	 * @param realname
	 * @return
	 */
	IPage<SysUser> queryDepartUserPageList(Page<SysUser> page, @Param("orgCode") String orgCode, @Param("username") String username, @Param("realname") String realname);

    /**
     * 获取用户信息
     * @param page
     * @param orgCode
     * @param keyword
     * @return
     */
    IPage<SysUser> getUserInformation(Page<SysUser> page,  @Param("orgCode") String orgCode,  @Param("keyword") String keyword,@Param("userId") String userId);


	/**
	 * 获取用户信息
	 * @param page
	 * @param orgCode
	 * @param keyword
	 * @return
	 */
	IPage<SysUser> getProcessUserList(Page<SysUser> page,  @Param("orgCode") String orgCode,  @Param("keyword") String keyword,  @Param("tenantId") Integer tenantId, @Param("excludeUserIdList") List<String> excludeUserIdList);

	/**
	 * 获取租户下的部门通过前台传过来的部门id
	 * @param departIds
	 * @param tenantId
	 * @return
	 */
    List<String> getTenantDepart(@Param("departIds") List<String> departIds, @Param("tenantId") String tenantId);

	/**
	 * 根据当前租户和用户id查询用户部门数据
	 * @param userId
	 * @param tenantId
	 * @return
	 */
	List<SysUserDepart> getTenantUserDepart(@Param("userId") String userId, @Param("tenantId") String tenantId);

	/**
	 * 根据用户id和租户id,删除用户部门数据
	 * @param userId
	 * @param tenantId
	 */
	void deleteUserDepart(@Param("userId") String userId, @Param("tenantId") String tenantId);

	/**
	 * 通过部门id和租户id获取用户
	 * @param departId
	 * @param tenantId
	 * @return
	 */
    List<SysUser> getUsersByDepartTenantId(@Param("departId") String departId, @Param("tenantId") Integer tenantId);

	/**
	 * 根据用户id和部门id获取数量,用于查看用户是否存在用户部门关系表中
	 * @param userId
	 * @param departId
	 * @return
	 */
	@Select("SELECT COUNT(*) FROM sys_user_depart WHERE user_id = #{userId} AND dep_id = #{departId}")
    Long getCountByDepartIdAndUserId(String userId, String departId);
}
