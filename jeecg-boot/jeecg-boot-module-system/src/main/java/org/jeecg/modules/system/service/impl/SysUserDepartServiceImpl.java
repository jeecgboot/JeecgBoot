package org.jeecg.modules.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserDepart;
import org.jeecg.modules.system.mapper.SysUserDepartMapper;
import org.jeecg.modules.system.model.DepartIdModel;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysUserDepartService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <P>
 * 用户部门表实现类
 * <p/>
 * @Author ZhiLin
 *@since 2019-02-22
 */
@Service
public class SysUserDepartServiceImpl extends ServiceImpl<SysUserDepartMapper, SysUserDepart> implements ISysUserDepartService {
	@Autowired
	private ISysDepartService sysDepartService;
	@Autowired
	private ISysUserService sysUserService;
	

	/**
	 * 根据用户id查询部门信息
	 */
	@Override
	public List<DepartIdModel> queryDepartIdsOfUser(String userId) {
		LambdaQueryWrapper<SysUserDepart> queryUDep = new LambdaQueryWrapper<SysUserDepart>();
		LambdaQueryWrapper<SysDepart> queryDep = new LambdaQueryWrapper<SysDepart>();
		try {
			queryUDep.eq(SysUserDepart::getUserId, userId);
			List<String> depIdList = new ArrayList<>();
			List<DepartIdModel> depIdModelList = new ArrayList<>();
			List<SysUserDepart> userDepList = this.list(queryUDep);
			if(userDepList != null && userDepList.size() > 0) {
			for(SysUserDepart userDepart : userDepList) {
					depIdList.add(userDepart.getDepId());
				}
			queryDep.in(SysDepart::getId, depIdList);
			List<SysDepart> depList = sysDepartService.list(queryDep);
			if(depList != null || depList.size() > 0) {
				for(SysDepart depart : depList) {
					depIdModelList.add(new DepartIdModel().convertByUserDepart(depart));
				}
			}
			return depIdModelList;
			}
		}catch(Exception e) {
			e.fillInStackTrace();
		}
		return null;
		
		
	}


	/**
	 * 根据部门id查询用户信息
	 */
	@Override
	public List<SysUser> queryUserByDepId(String depId) {
		LambdaQueryWrapper<SysUserDepart> queryUDep = new LambdaQueryWrapper<SysUserDepart>();
		queryUDep.eq(SysUserDepart::getDepId, depId);
		List<String> userIdList = new ArrayList<>();
		List<SysUserDepart> uDepList = this.list(queryUDep);
		if(uDepList != null && uDepList.size() > 0) {
			for(SysUserDepart uDep : uDepList) {
				userIdList.add(uDep.getUserId());
			}
			List<SysUser> userList = (List<SysUser>) sysUserService.listByIds(userIdList);
			//update-begin-author:taoyan date:201905047 for:接口调用查询返回结果不能返回密码相关信息
			for (SysUser sysUser : userList) {
				sysUser.setSalt("");
				sysUser.setPassword("");
			}
			//update-end-author:taoyan date:201905047 for:接口调用查询返回结果不能返回密码相关信息
			return userList;
		}
		return new ArrayList<SysUser>();
	}
	
}
