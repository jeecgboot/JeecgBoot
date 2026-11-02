package org.jeecg.modules.system.service.impl;

import java.util.*;

import org.jeecg.common.util.IpUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysRolePermission;
import org.jeecg.modules.system.entity.SysUserRole;
import org.jeecg.modules.system.mapper.SysRolePermissionMapper;
import org.jeecg.modules.system.mapper.SysUserRoleMapper;
import org.jeecg.modules.system.service.ISysRolePermissionService;
import org.jeecg.modules.system.util.ShiroAuthCacheUtil;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

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

	@Autowired
	private SysUserRoleMapper sysUserRoleMapper;
	@Autowired
	private RedisUtil redisUtil;

	@Override
	public void saveRolePermission(String roleId, String permissionIds) {
		String ip = "";
		try {
			//获取request
			HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
			//获取IP地址
			ip = IpUtils.getIpAddr(request);
		} catch (Exception e) {
			ip = "127.0.0.1";
		}
		LambdaQueryWrapper<SysRolePermission> query = new QueryWrapper<SysRolePermission>().lambda().eq(SysRolePermission::getRoleId, roleId);
		this.remove(query);
		List<SysRolePermission> list = new ArrayList<SysRolePermission>();
        String[] arr = permissionIds.split(",");
		for (String p : arr) {
			if(oConvertUtils.isNotEmpty(p)) {
				SysRolePermission rolepms = new SysRolePermission(roleId, p);
				rolepms.setOperateDate(new Date());
				rolepms.setOperateIp(ip);
				list.add(rolepms);
			}
		}
		this.saveBatch(list);
		//update-begin---author:scott ---date:20261029  for：【LHZP-1129】角色授权后清理该角色下用户Shiro权限缓存，无需重新登录即生效-----------
		clearRoleUserAuthCache(roleId);
		//update-end---author:scott ---date:20261029  for：【LHZP-1129】角色授权后清理该角色下用户Shiro权限缓存，无需重新登录即生效-----------
	}

	@Override
	public void saveRolePermission(String roleId, String permissionIds, String lastPermissionIds) {
		String ip = "";
		try {
			//获取request
			HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
			//获取IP地址
			ip = IpUtils.getIpAddr(request);
		} catch (Exception e) {
			ip = "127.0.0.1";
		}
		List<String> add = getDiff(lastPermissionIds,permissionIds);
		if(add!=null && add.size()>0) {
			List<SysRolePermission> list = new ArrayList<SysRolePermission>();
			for (String p : add) {
				if(oConvertUtils.isNotEmpty(p)) {
					SysRolePermission rolepms = new SysRolePermission(roleId, p);
					rolepms.setOperateDate(new Date());
					rolepms.setOperateIp(ip);
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
		//update-begin---author:scott ---date:20261029  for：【LHZP-1129】角色授权后清理该角色下用户Shiro权限缓存，无需重新登录即生效-----------
		clearRoleUserAuthCache(roleId);
		//update-end---author:scott ---date:20261029  for：【LHZP-1129】角色授权后清理该角色下用户Shiro权限缓存，无需重新登录即生效-----------
	}

	/**
	 * 清理角色下所有用户的Shiro权限缓存
	 * @param roleId 角色id
	 * @author scott
	 * @since 2026-10-29 【LHZP-1129】角色授权后清理该角色下用户权限缓存，无需重新登录即生效
	 */
	private void clearRoleUserAuthCache(String roleId) {
		if (oConvertUtils.isEmpty(roleId)) {
			return;
		}
		List<SysUserRole> userRoles = sysUserRoleMapper.selectList(new QueryWrapper<SysUserRole>().lambda().select(SysUserRole::getUserId).eq(SysUserRole::getRoleId, roleId));
		ShiroAuthCacheUtil.clearUserAuthCache(redisUtil, userRoles.stream().map(SysUserRole::getUserId).collect(Collectors.toSet()));
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
		Map<String, Integer> map = new HashMap(5);
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
