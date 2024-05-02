package org.jeecg.modules.system.service;


import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserDepart;
import org.jeecg.modules.system.model.DepartIdModel;


import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * SysUserDpeart用户组织机构service
 * </p>
 * @Author ZhiLin
 *
 */
public interface ISysUserDepartService extends IService<SysUserDepart> {
	

	/**
	 * 根据指定用户id查询部门信息
	 * @param userId
	 * @return
	 */
	List<DepartIdModel> queryDepartIdsOfUser(String userId);
	

	/**
	 * 根据部门id查询用户信息
	 * @param depId
	 * @return
	 */
	List<SysUser> queryUserByDepId(String depId);
  	/**
	 * 根据部门code，查询当前部门和下级部门的用户信息
     * @param depCode 部门code
     * @param realname 真实姓名
     * @return List<SysUser>
	 */
	List<SysUser> queryUserByDepCode(String depCode,String realname);

	/**
	 * 用户组件数据查询
	 * @param departId
	 * @param username
	 * @param pageSize
	 * @param pageNo
     * @param realname
     * @param id
     * @param isMultiTranslate 是否多字段翻译
	 * @return
	 */
	IPage<SysUser> queryDepartUserPageList(String departId, String username, String realname, int pageSize, int pageNo,String id,String isMultiTranslate);

    /**
     * 获取用户信息
	 * @param tenantId
     * @param departId
     * @param keyword
     * @param pageSize
     * @param pageNo
     * @return
     */
    IPage<SysUser> getUserInformation(Integer tenantId, String departId, String keyword, Integer pageSize, Integer pageNo);

	/**
	 * 获取用户信息
	 * @param tenantId
	 * @param departId
	 * @param roleId
	 * @param keyword
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	IPage<SysUser> getUserInformation(Integer tenantId,String departId,String roleId, String keyword, Integer pageSize, Integer pageNo, String excludeUserIdList);

	/**
	 * 通过部门id和租户id获取多个用户
	 * @param departId
	 * @param tenantId
	 * @return
	 */
	List<SysUser> getUsersByDepartTenantId(String departId,Integer tenantId);
}
