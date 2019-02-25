package org.jeecg.modules.system.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysPermission;
import org.jeecg.modules.system.mapper.SysPermissionMapper;
import org.jeecg.modules.system.model.TreeModel;
import org.jeecg.modules.system.service.ISysPermissionService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author scott
 * @since 2018-12-21
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

	@Resource
	private SysPermissionMapper sysPermissionMapper;
	
	@Override
	public List<TreeModel> queryListByParentId(String parentId) {
		return sysPermissionMapper.queryListByParentId(parentId);
	}

	/**
	  * 真实删除
	 */
	@Override
	public void deletePermission(String id) throws JeecgBootException {
		SysPermission sysPermission = this.getById(id);
		if(sysPermission==null) {
			throw new JeecgBootException("未找到菜单信息");
		}
		String pid = sysPermission.getParentId();
		int count = this.count(new QueryWrapper<SysPermission>().lambda().eq(SysPermission::getParentId, pid));
		if(count==1) {
			//若父节点无其他子节点，则该父节点是叶子节点
			this.update(new SysPermission().setIsLeaf(1),new UpdateWrapper<SysPermission>().eq("id",pid));
		}
		sysPermissionMapper.deleteById(id);
	}
	
	/**
	  * 逻辑删除
	 */
	@Override
	public void deletePermissionLogical(String id) throws JeecgBootException {
		SysPermission sysPermission = this.getById(id);
		if(sysPermission==null) {
			throw new JeecgBootException("未找到菜单信息");
		}
		String pid = sysPermission.getParentId();
		int count = this.count(new QueryWrapper<SysPermission>().lambda().eq(SysPermission::getParentId, pid));
		if(count==1) {
			//若父节点无其他子节点，则该父节点是叶子节点
			this.update(new SysPermission().setIsLeaf(1),new UpdateWrapper<SysPermission>().eq("id",pid));
		}
		sysPermission.setDelFlag(1);
		this.updateById(sysPermission);
	}

	@Override
	public void addPermission(SysPermission sysPermission) throws JeecgBootException {
		//----------------------------------------------------------------------
		//判断是否是一级菜单，是的话清空父菜单
		if(CommonConstant.MENU_TYPE_0.equals(sysPermission.getMenuType())) {
			sysPermission.setParentId(null);
		}
		//----------------------------------------------------------------------
		String pid = sysPermission.getParentId();
		if(oConvertUtils.isNotEmpty(pid)) {
			//设置父节点不为叶子节点
			this.update(new SysPermission().setIsLeaf(0),new UpdateWrapper<SysPermission>().eq("id",pid));
		}
		sysPermission.setCreateTime(new Date());
		sysPermission.setDelFlag(0);
		sysPermission.setIsLeaf(1);
		this.save(sysPermission);
	}

	@Override
	public void editPermission(SysPermission sysPermission) throws JeecgBootException {
		SysPermission p = this.getById(sysPermission.getId());
		//TODO 该节点判断是否还有子节点
		if(p==null) {
			throw new JeecgBootException("未找到菜单信息");
		}else {
			sysPermission.setUpdateTime(new Date());
			//----------------------------------------------------------------------
			//判断是否是一级菜单，是的话清空父菜单
			if(CommonConstant.MENU_TYPE_0.equals(sysPermission.getMenuType())) {
				sysPermission.setParentId("");
			}
			//----------------------------------------------------------------------
			this.updateById(sysPermission);
			String pid = sysPermission.getParentId();
			if(oConvertUtils.isNotEmpty(pid) && !pid.equals(p.getParentId())) {
				//设置父节点不为叶子节点
				this.update(new SysPermission().setIsLeaf(0),new UpdateWrapper<SysPermission>().eq("id",pid));
			}
		}
		
	}

	@Override
	public List<SysPermission> queryByUser(String username) {
		return this.sysPermissionMapper.queryByUser(username);
	}
	
}
