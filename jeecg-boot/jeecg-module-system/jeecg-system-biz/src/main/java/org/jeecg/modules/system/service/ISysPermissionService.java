package org.jeecg.modules.system.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.system.entity.SysPermission;
import org.jeecg.modules.system.model.TreeModel;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
public interface ISysPermissionService extends IService<SysPermission> {
    /**
     * 切换vue3菜单
     */
	public void switchVue3Menu();
	
    /**
     * 通过父id查询菜单
     * @param parentId 父id
     * @return
     */
	public List<TreeModel> queryListByParentId(String parentId);
	
	/**
     * 真实删除
     * @param id 菜单id
     * @throws JeecgBootException
     */
	public void deletePermission(String id) throws JeecgBootException;
	/**
     * 逻辑删除
     * @param id 菜单id
     * @throws JeecgBootException
     */
	public void deletePermissionLogical(String id) throws JeecgBootException;

    /**
     * 添加菜单
     * @param sysPermission SysPermission对象
     * @throws JeecgBootException
     */
	public void addPermission(SysPermission sysPermission) throws JeecgBootException;

    /**
     * 编辑菜单
     * @param sysPermission SysPermission对象
     * @throws JeecgBootException
     */
	public void editPermission(SysPermission sysPermission) throws JeecgBootException;

    /**
     * 获取登录用户拥有的权限
     * @param username 用户名
     * @return
     */
	public List<SysPermission> queryByUser(String username);
	
	/**
	 * 根据permissionId删除其关联的SysPermissionDataRule表中的数据
	 * 
	 * @param id
	 * @return
	 */
	public void deletePermRuleByPermId(String id);
	
	/**
	  * 查询出带有特殊符号的菜单地址的集合
	 * @return
	 */
	public List<String> queryPermissionUrlWithStar();

	/**
	 * 判断用户否拥有权限
	 * @param username
	 * @param sysPermission
	 * @return
	 */
	public boolean hasPermission(String username, SysPermission sysPermission);

	/**
	 * 根据用户和请求地址判断是否有此权限
	 * @param username
	 * @param url
	 * @return
	 */
	public boolean hasPermission(String username, String url);

	/**
	 * 查询部门权限数据
	 * @param departId
	 * @return
	 */
	List<SysPermission> queryDepartPermissionList(String departId);

	/**
	 * 检测地址是否存在(聚合路由的情况下允许使用子菜单路径作为父菜单的路由地址)
	 * @param id
	 * @param url
	 * @param alwaysShow 是否是聚合路由
	 * @return
	 */
	 boolean checkPermDuplication(String id, String url,Boolean alwaysShow);
}
