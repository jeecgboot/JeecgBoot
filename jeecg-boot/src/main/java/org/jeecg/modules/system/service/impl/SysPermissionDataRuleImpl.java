package org.jeecg.modules.system.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysPermissionDataRule;
import org.jeecg.modules.system.mapper.SysPermissionDataRuleMapper;
import org.jeecg.modules.system.service.ISysPermissionDataRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <p>
 * 菜单权限规则  服务实现类
 * </p>
 *
 * @author huangzhilin
 * @since 2019-04-01
 */
@Service
public class SysPermissionDataRuleImpl extends ServiceImpl<SysPermissionDataRuleMapper, SysPermissionDataRule>
		implements ISysPermissionDataRuleService {

	@Autowired
	private ISysPermissionDataRuleService permRuleService;

	/**
	 * 根据菜单id查询其对应的权限数据
	 */
	@Override
	public List<SysPermissionDataRule> getPermRuleListByPermId(String permissionId) {
		LambdaQueryWrapper<SysPermissionDataRule> query = new LambdaQueryWrapper<SysPermissionDataRule>();
		query.eq(SysPermissionDataRule::getPermissionId, permissionId);
		query.orderByDesc(SysPermissionDataRule::getCreateTime);
		List<SysPermissionDataRule> permRuleList = permRuleService.list(query);
		return permRuleList;
	}

	/**
	 * 根据前端传递的权限名称和权限值参数来查询权限数据
	 */
	@Override
	public List<SysPermissionDataRule> queryPermissionRule(SysPermissionDataRule permRule) {
		QueryWrapper<SysPermissionDataRule> queryWrapper = QueryGenerator.initQueryWrapper(permRule, null);
		return permRuleService.list(queryWrapper);
	}

	@Override
	public List<SysPermissionDataRule> queryPermissionDataRules(String username,String permissionId) {
		List<String> idsList = this.baseMapper.queryDataRuleIds(username, permissionId);
		if(idsList==null || idsList.size()==0) {
			return null;
		}
		Set<String> set = new HashSet<String>();
		for (String ids : idsList) {
			if(ids==null) {
				continue;
			}
			String arr[] = ids.split(",");
			for (String id : arr) {
				if(oConvertUtils.isNotEmpty(id)) {
					set.add(id);
				}
			}
		}
		return this.baseMapper.selectList(new QueryWrapper<SysPermissionDataRule>().in("id", set));
	}

}
