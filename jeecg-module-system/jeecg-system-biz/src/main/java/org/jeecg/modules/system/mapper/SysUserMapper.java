package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.system.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.system.model.SysUserSysDepartModel;
import org.jeecg.modules.system.vo.SysUserDepVo;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
	/**
	  * 通过用户账号查询用户信息
	 * @param username
	 * @return
	 */
	public SysUser getUserByName(@Param("username") String username);
	
	/**
	  * 通过用户账号查询用户Id
	 * @param username
	 * @return
	 */
	public String getUserIdByName(@Param("username") String username);

	/**
	 *  根据部门Id查询用户信息
	 * @param page
	 * @param departId
     * @param username 用户登录账户
	 * @return
	 */
	IPage<SysUser> getUserByDepId(Page page, @Param("departId") String departId, @Param("username") String username);

	/**
	 * 根据部门和子部门下的所有用户账号
	 *
	 * @param orgCode 部门编码
	 * @return
	 */
	List<String> getUserAccountsByDepCode(@Param("orgCode") String orgCode);

	/**
	 *  根据用户Ids,查询用户所属部门名称信息
	 * @param userIds
	 * @return
	 */
	List<SysUserDepVo> getDepNamesByUserIds(@Param("userIds")List<String> userIds);

	/**
	 *  根据部门Ids,查询部门下用户信息
	 * @param page
	 * @param departIds
     * @param username 用户登录账户
	 * @return
	 */
	IPage<SysUser> getUserByDepIds(Page page, @Param("departIds") List<String> departIds, @Param("username") String username);

	/**
	 * 根据角色Id查询用户信息
	 * @param page
	 * @param roleId 角色id
     * @param username 用户登录账户
	 * @return
	 */
	IPage<SysUser> getUserByRoleId(Page page, @Param("roleId") String roleId, @Param("username") String username);
	
	/**
	 * 根据用户名设置部门ID
	 * @param username
	 * @param orgCode
	 */
	void updateUserDepart(@Param("username") String username,@Param("orgCode") String orgCode, @Param("loginTenantId") Integer loginTenantId);
	
	/**
	 * 根据手机号查询用户信息
	 * @param phone
	 * @return
	 */
	public SysUser getUserByPhone(@Param("phone") String phone);
	
	
	/**
	 * 根据邮箱查询用户信息
	 * @param email
	 * @return
	 */
	public SysUser getUserByEmail(@Param("email")String email);

	/**
	 * 根据 orgCode 查询用户，包括子部门下的用户
	 *
	 * @param page 分页对象, xml中可以从里面进行取值,传递参数 Page 即自动分页,必须放在第一位(你可以继承Page实现自己的分页对象)
	 * @param orgCode
	 * @param userParams 用户查询条件，可为空
	 * @return
	 */
	List<SysUserSysDepartModel> getUserByOrgCode(IPage page, @Param("orgCode") String orgCode, @Param("userParams") SysUser userParams);


    /**
     * 查询 getUserByOrgCode 的Total
     *
     * @param orgCode
     * @param userParams 用户查询条件，可为空
     * @return
     */
    Integer getUserByOrgCodeTotal(@Param("orgCode") String orgCode, @Param("userParams") SysUser userParams);

    /**
     * 批量删除角色与用户关系
     * @Author scott
     * @Date 2019/12/13 16:10
     * @param roleIdArray
     */
	void deleteBathRoleUserRelation(@Param("roleIdArray") String[] roleIdArray);

    /**
     * 批量删除角色与权限关系
     * @Author scott
     * @Date 2019/12/13 16:10
     * @param roleIdArray
     */
	void deleteBathRolePermissionRelation(@Param("roleIdArray") String[] roleIdArray);

	/**
	 * 查询被逻辑删除的用户
     * @param wrapper
     * @return List<SysUser>
	 */
	List<SysUser> selectLogicDeleted(@Param(Constants.WRAPPER) Wrapper<SysUser> wrapper);

	/**
	 * 还原被逻辑删除的用户
     * @param userIds 用户id
     * @param entity
     * @return int
	 */
	int revertLogicDeleted(@Param("userIds") List<String> userIds, @Param("entity") SysUser entity);

	/**
	 * 彻底删除被逻辑删除的用户
     * @param userIds 多个用户id
     * @return int
	 */
	int deleteLogicDeleted(@Param("userIds") List<String> userIds);

    /**
     * 更新空字符串为null【此写法有sql注入风险，禁止随便用】
     * @param fieldName
     * @return int
     */
    @Deprecated
    int updateNullByEmptyString(@Param("fieldName") String fieldName);
    
	/**
	 *  根据部门Ids,查询部门下用户信息
	 * @param departIds
     * @param username 用户账户名称
	 * @return
	 */
	List<SysUser> queryByDepIds(@Param("departIds")List<String> departIds,@Param("username") String username);

	/**
	 * 获取用户信息
	 * @param page
	 * @param roleId
	 * @param keyword
	 * @param userIdList
	 * @return
	 */
	IPage<SysUser> selectUserListByRoleId(Page<SysUser> page,  @Param("roleId") String roleId,  @Param("keyword") String keyword,  @Param("tenantId") Integer tenantId, @Param("excludeUserIdList") List<String> excludeUserIdList);

    /**
     * 更新刪除状态和离职状态
     * @param userIds  存放用户id集合
     * @param sysUser
     * @return boolean
     */
    void updateStatusAndFlag(@Param("userIds") List<String> userIds, @Param("sysUser") SysUser sysUser);

	/**
	 * 获取租户下的离职列表信息
	 * @param tenantId
	 * @return
	 */
	List<SysUser> getTenantQuitList(@Param("tenantId") Integer tenantId);
	
	/**
	 * 获取租户下的有效用户ids
	 * @param tenantId
	 * @return
	 */
	List<String> getTenantUserIdList(@Param("tenantId") Integer tenantId);
	
	/**
	 * 根据部门id和租户id获取用户数据 
	 * @param departIds
	 * @param tenantId
	 * @return
	 */
	List<SysUser> getUserByDepartsTenantId(@Param("departIds") List<String> departIds,@Param("tenantId") Integer tenantId);

	/**
	 * 根据用户名和手机号获取用户
	 * @param phone
	 * @param username
	 * @return
	 */
	@Select("select id,phone from sys_user where phone = #{phone} and username = #{username}")
    SysUser getUserByNameAndPhone(@Param("phone") String phone, @Param("username") String username);
}
