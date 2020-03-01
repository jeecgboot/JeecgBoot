package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
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
	 *  根据部门Id查询用户信息
	 * @param page
	 * @param departId
	 * @return
	 */
	IPage<SysUser> getUserByDepId(Page page, @Param("departId") String departId, @Param("username") String username);

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
	 * @return
	 */
	IPage<SysUser> getUserByDepIds(Page page, @Param("departIds") List<String> departIds, @Param("username") String username);

	/**
	 * 根据角色Id查询用户信息
	 * @param page
	 * @param
	 * @return
	 */
	IPage<SysUser> getUserByRoleId(Page page, @Param("roleId") String roleId, @Param("username") String username);
	
	/**
	 * 根据用户名设置部门ID
	 * @param username
	 * @param departId
	 */
	void updateUserDepart(@Param("username") String username,@Param("orgCode") String orgCode);
	
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
     * @Author scott
     * @Date 2019/12/13 16:10
     * @Description: 批量删除角色与用户关系
     */
	void deleteBathRoleUserRelation(@Param("roleIdArray") String[] roleIdArray);

    /**
     * @Author scott
     * @Date 2019/12/13 16:10
     * @Description: 批量删除角色与权限关系
     */
	void deleteBathRolePermissionRelation(@Param("roleIdArray") String[] roleIdArray);

	/**
	 * 查询被逻辑删除的用户
	 */
	List<SysUser> selectLogicDeleted(@Param(Constants.WRAPPER) Wrapper<SysUser> wrapper);

	/**
	 * 还原被逻辑删除的用户
	 */
	int revertLogicDeleted(@Param("userIds") String userIds, @Param("entity") SysUser entity);

	/**
	 * 彻底删除被逻辑删除的用户
	 */
	int deleteLogicDeleted(@Param("userIds") String userIds);

}
