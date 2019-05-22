package org.jeecg.modules.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysRolePermission;
import org.jeecg.modules.system.mapper.SysRolePermissionMapper;
import org.jeecg.modules.system.service.ISysRolePermissionService;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色权限表 服务实现类
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
@Service
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> implements ISysRolePermissionService {

	@Override
	@CacheEvict(value="loginUser_cacheRules", allEntries=true)
	public void saveRolePermission(String roleId, String permissionIds) {
		LambdaQueryWrapper<SysRolePermission> query = new QueryWrapper<SysRolePermission>().lambda().eq(SysRolePermission::getRoleId, roleId);
		this.remove(query);
		List<SysRolePermission> list = new ArrayList<SysRolePermission>();
		String arr[] = permissionIds.split(",");
		for (String p : arr) {
			if(oConvertUtils.isNotEmpty(p)) {
				SysRolePermission rolepms = new SysRolePermission(roleId, p);
				list.add(rolepms);
			}
		}
		this.saveBatch(list);
	}

	@Override
	@CacheEvict(value="loginUser_cacheRules", allEntries=true)
	public void saveRolePermission(String roleId, String permissionIds, String lastPermissionIds) {
		List<String> add = getDiff(lastPermissionIds,permissionIds);
		if(add!=null && add.size()>0) {
			List<SysRolePermission> list = new ArrayList<SysRolePermission>();
			for (String p : add) {
				if(oConvertUtils.isNotEmpty(p)) {
					SysRolePermission rolepms = new SysRolePermission(roleId, p);
					list.add(rolepms);
				}
			}
			this.saveBatch(list);
		}
		
		List<String> delete = getDiff(permissionIds,lastPermissionIds);
		if(delete!=null && delete.size()>0) {
			for (String permissionId : delete) {
				this.remove(new QueryWrapper<SysRolePermission>().lambda().eq(SysRolePermission::getRoleId, roleId).eq(SysRolePermission::getPermissionId, permissionId));
			}
		}
	}
	
	/**
	 * 从diff中找出main中没有的元素
	 * @param main
	 * @param diff
	 * @return
	 */
	private List<String> getDiff(String main,String diff){
		if(oConvertUtils.isEmpty(diff)) {
			return null;
		}
		if(oConvertUtils.isEmpty(main)) {
			return Arrays.asList(diff.split(","));
		}
		
		String[] mainArr = main.split(",");
		String[] diffArr = diff.split(",");
		Map<String, Integer> map = new HashMap<>();
		for (String string : mainArr) {
			map.put(string, 1);
		}
		List<String> res = new ArrayList<String>();
		for (String key : diffArr) {
			if(oConvertUtils.isNotEmpty(key) && !map.containsKey(key)) {
				res.add(key);
			}
		}
		return res;
	}

}
