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
 * @author scott
 * @since 2018-12-21
 */
public interface ISysPermissionService extends IService<SysPermission> {
	
	public List<TreeModel> queryListByParentId(String parentId);
	
	/**真实删除*/
	public void deletePermission(String id) throws JeecgBootException;
	/**逻辑删除*/
	public void deletePermissionLogical(String id) throws JeecgBootException;
	
	public void addPermission(SysPermission sysPermission) throws JeecgBootException;
	
	public void editPermission(SysPermission sysPermission) throws JeecgBootException;
	
	public List<SysPermission> queryByUser(String username);
}
