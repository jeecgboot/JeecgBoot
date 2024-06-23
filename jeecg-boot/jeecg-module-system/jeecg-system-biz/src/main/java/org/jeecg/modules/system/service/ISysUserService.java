package org.jeecg.modules.system.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.system.vo.SysUserCacheInfo;
import org.jeecg.modules.system.entity.SysRoleIndex;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.model.SysUserSysDepartModel;
import org.jeecg.modules.system.vo.lowapp.DepartAndUserInfo;
import org.jeecg.modules.system.vo.lowapp.UpdateDepartInfo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
public interface ISysUserService extends IService<SysUser> {

	/**
	 * 查询用户数据列表
	 * 
	 * @param req
	 * @param queryWrapper
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	Result<IPage<SysUser>> queryPageList(HttpServletRequest req, QueryWrapper<SysUser> queryWrapper, Integer pageSize, Integer pageNo);
	
	/**
	 * 重置密码
	 *
	 * @param username
	 * @param oldpassword
	 * @param newpassword
	 * @param confirmpassword
	 * @return
	 */
	public Result<?> resetPassword(String username, String oldpassword, String newpassword, String confirmpassword);

	/**
	 * 修改密码
	 *
	 * @param sysUser
	 * @return
	 */
	public Result<?> changePassword(SysUser sysUser);

	/**
	 * 删除用户
	 * @param userId
	 * @return
	 */
	public boolean deleteUser(String userId);

	/**
	 * 批量删除用户
	 * @param userIds
	 * @return
	 */
	public boolean deleteBatchUsers(String userIds);

    /**
     * 根据用户名查询
     * @param username 用户名
     * @return SysUser
     */
	public SysUser getUserByName(String username);
	
	/**
	 * 添加用户和用户角色关系
	 * @param user
	 * @param roles
	 */
	public void addUserWithRole(SysUser user, String roles);
	
	
	/**
	 * 修改用户和用户角色关系
	 * @param user
	 * @param roles
	 */
	public void editUserWithRole(SysUser user, String roles);

	/**
	 * 获取用户的授权角色
	 * @param username
	 * @return
	 */
	public List<String> getRole(String username);

	/**
	 * 获取根据登录用户的角色获取动态首页
	 *
	 * @param username
	 * @param version 前端UI版本
	 * @return
	 */
	public SysRoleIndex getDynamicIndexByUserRole(String username, String version);
	
	/**
	  * 查询用户信息包括 部门信息
	 * @param username
	 * @return
	 */
	@Deprecated
	public SysUserCacheInfo getCacheUser(String username);

	/**
	 * 根据部门Id查询
	 * @param page
     * @param departId 部门id
     * @param username 用户账户名称
	 * @return
	 */
	public IPage<SysUser> getUserByDepId(Page<SysUser> page, String departId, String username);

	/**
	 * 根据部门Ids查询
	 * @param page
     * @param departIds  部门id集合
     * @param username 用户账户名称
	 * @return
	 */
	public IPage<SysUser> getUserByDepIds(Page<SysUser> page, List<String> departIds, String username);

	/**
	 * 根据 userIds查询，查询用户所属部门的名称（多个部门名逗号隔开）
	 * @param userIds
	 * @return
	 */
	public Map<String,String> getDepNamesByUserIds(List<String> userIds);

    /**
     * 根据部门 Id 和 QueryWrapper 查询
     *
     * @param page
     * @param departId
     * @param queryWrapper
     * @return
     */
    //update-begin-author:taoyan date:2022-9-13 for: VUEN-2245【漏洞】发现新漏洞待处理20220906 ----sql注入 方法没有使用，注掉
    // public IPage<SysUser> getUserByDepartIdAndQueryWrapper(Page<SysUser> page, String departId, QueryWrapper<SysUser> queryWrapper);
	//update-end-author:taoyan date:2022-9-13 for: VUEN-2245【漏洞】发现新漏洞待处理20220906 ----sql注入 方法没有使用，注掉

	/**
	 * 根据 orgCode 查询用户，包括子部门下的用户
	 *
	 * @param orgCode
	 * @param userParams 用户查询条件，可为空
	 * @param page 分页参数
	 * @return
	 */
	IPage<SysUserSysDepartModel> queryUserByOrgCode(String orgCode, SysUser userParams, IPage page);

	/**
	 * 根据角色Id查询
	 * @param page
     * @param roleId 角色id
     * @param username 用户账户名称
	 * @return
	 */
	public IPage<SysUser> getUserByRoleId(Page<SysUser> page, String roleId, String username);

	/**
	 * 通过用户名获取用户角色集合
	 *
	 * @param username 用户名
	 * @return 角色集合
	 */
	Set<String> getUserRolesSet(String username);
	
	/**
	 * 通过用户名获取用户角色集合
	 *
	 * @param userId 用户id
	 * @return 角色集合
	 */
	Set<String> getUserRoleSetById(String userId);

	/**
	 * 通过用户名获取用户权限集合
	 *
	 * @param userId 用户id
	 * @return 权限集合
	 */
	Set<String> getUserPermissionsSet(String userId);
	
	/**
	 * 根据用户名设置部门ID
	 * @param username
	 * @param orgCode
	 */
	void updateUserDepart(String username,String orgCode,Integer loginTenantId);
	
	/**
	 * 根据手机号获取用户名和密码
     * @param phone 手机号
     * @return SysUser
	 */
	public SysUser getUserByPhone(String phone);


	/**
	 * 根据邮箱获取用户
     * @param email 邮箱
     * @return SysUser
     */
	public SysUser getUserByEmail(String email);


	/**
	 * 添加用户和用户部门关系
	 * @param user
	 * @param selectedParts
	 */
	void addUserWithDepart(SysUser user, String selectedParts);

	/**
	 * 编辑用户和用户部门关系
	 * @param user
	 * @param departs
	 */
	void editUserWithDepart(SysUser user, String departs);
	
	/**
	   * 校验用户是否有效
	 * @param sysUser
	 * @return
	 */
	Result checkUserIsEffective(SysUser sysUser);

	/**
	 * 查询被逻辑删除的用户
     * @return List<SysUser>
	 */
	List<SysUser> queryLogicDeleted();

	/**
	 * 查询被逻辑删除的用户（可拼装查询条件）
     * @param wrapper
     * @return List<SysUser>
	 */
	List<SysUser> queryLogicDeleted(LambdaQueryWrapper<SysUser> wrapper);

	/**
	 * 还原被逻辑删除的用户
     * @param userIds  存放用户id集合
     * @param updateEntity
     * @return boolean
	 */
	boolean revertLogicDeleted(List<String> userIds, SysUser updateEntity);

	/**
	 * 彻底删除被逻辑删除的用户
     * @param userIds 存放用户id集合
     * @return boolean
	 */
	boolean removeLogicDeleted(List<String> userIds);

    /**
     * 更新手机号、邮箱空字符串为 null
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    boolean updateNullPhoneEmail();

	/**
	 * 保存第三方用户信息
	 * @param sysUser
	 */
	void saveThirdUser(SysUser sysUser);

	/**
	 * 根据部门Ids查询
	 * @param departIds 部门id集合
     * @param username 用户账户名称
	 * @return
	 */
	List<SysUser> queryByDepIds(List<String> departIds, String username);

	/**
	 * 保存用户
	 * @param user 用户
	 * @param selectedRoles 选择的角色id，多个以逗号隔开
	 * @param selectedDeparts 选择的部门id，多个以逗号隔开
	 * @param relTenantIds 多个租户id
	 */
	void saveUser(SysUser user, String selectedRoles, String selectedDeparts, String relTenantIds);

	/**
	 * 编辑用户
	 * @param user 用户
	 * @param roles 选择的角色id，多个以逗号隔开
	 * @param departs 选择的部门id，多个以逗号隔开
	 * @param relTenantIds 多个租户id
	 */
	void editUser(SysUser user, String roles, String departs, String relTenantIds);

	/**
     * userId转为username
     * @param userIdList
     * @return List<String>
     */
	List<String> userIdToUsername(Collection<String> userIdList);


	/**
	 * 获取用户信息 字段信息是加密后的 【加密用户信息】
	 * @param username
	 * @return
	 */
	LoginUser getEncodeUserInfo(String username);

    /**
     * 用户离职
     * @param username
     */
    void userQuit(String username);

    /**
     * 获取离职人员列表
	 * @param tenantId 租户id
     * @return
     */
    List<SysUser> getQuitList(Integer tenantId);

    /**
     * 更新刪除状态和离职状态
     * @param userIds  存放用户id集合
     * @param sysUser
     * @return boolean
     */
    void updateStatusAndFlag(List<String> userIds, SysUser sysUser);

	/**
	 * 设置登录租户
	 * @param sysUser
	 * @return
	 */
	Result<JSONObject> setLoginTenant(SysUser sysUser, JSONObject obj, String username, Result<JSONObject> result);

	//--- author:taoyan date:20221231 for: QQYUN-3515【应用】应用下的组织机构管理功能，细节实现 ---
	/**
	 * 批量编辑用户信息
	 * @param json
	 */
	void batchEditUsers(JSONObject json);

	/**
	 * 根据关键词查询用户和部门
	 * @param keyword
	 * @return
	 */
	DepartAndUserInfo searchByKeyword(String keyword);

	/**
	 * 查询 部门修改的信息
	 * @param departId
	 * @return
	 */
	UpdateDepartInfo getUpdateDepartInfo(String departId);

	/**
	 * 修改部门相关信息
	 * @param updateDepartInfo
	 */
	void doUpdateDepartInfo(UpdateDepartInfo updateDepartInfo);

	/**
	 * 设置负责人 取消负责人
	 * @param json
	 */
	void changeDepartChargePerson(JSONObject json);
	//--- author:taoyan date:20221231 for: QQYUN-3515【应用】应用下的组织机构管理功能，细节实现 ---
	
	/**
	 * 编辑租户用户
	 * @param sysUser
	 * @param tenantId
	 * @param departs
	 */
	void editTenantUser(SysUser sysUser, String tenantId, String departs, String roles);

	/**
	 * 修改用户账号状态
	 * @param id 账号id
	 * @param status 账号状态
	 */
	void updateStatus(String id, String status);

	/**
	 * 导出应用下的用户Excel
	 * @param request
	 * @return
	 */
	ModelAndView exportAppUser(HttpServletRequest request);

	/**
	 * 导入应用下的用户
	 * @param request
	 * @return
	 */
	Result<?> importAppUser(HttpServletRequest request);

	/**
	 * 验证用户是否为管理员
	 * @param ids
	 */
	void checkUserAdminRejectDel(String ids);

	/**
	 * 修改手机号
	 * 
	 * @param json
	 * @param username
	 */
    void changePhone(JSONObject json, String username);

	/**
	 * 发送短信验证码
	 * 
	 * @param jsonObject
	 * @param username 用户名
	 * @param ipAddress ip地址
	 */
	void sendChangePhoneSms(JSONObject jsonObject, String username, String ipAddress);
}
