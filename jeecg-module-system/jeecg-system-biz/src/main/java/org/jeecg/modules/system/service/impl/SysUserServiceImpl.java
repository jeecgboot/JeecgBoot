package org.jeecg.modules.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.FillRuleConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.constant.enums.RoleIndexConfigEnum;
import org.jeecg.common.desensitization.annotation.SensitiveEncode;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.system.vo.SysUserCacheInfo;
import org.jeecg.common.util.*;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.mapper.*;
import org.jeecg.modules.system.model.SysUserSysDepartModel;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.system.vo.SysUserDepVo;
import org.jeecg.modules.system.vo.UserAvatar;
import org.jeecg.modules.system.vo.lowapp.DepartAndUserInfo;
import org.jeecg.modules.system.vo.lowapp.DepartInfo;
import org.jeecg.modules.system.vo.lowapp.UpdateDepartInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @Author: scott
 * @Date: 2018-12-20
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
	
	@Autowired
	private SysUserMapper userMapper;
	@Autowired
	private SysPermissionMapper sysPermissionMapper;
	@Autowired
	private SysUserRoleMapper sysUserRoleMapper;
	@Autowired
	private SysUserDepartMapper sysUserDepartMapper;
	@Autowired
	private SysDepartMapper sysDepartMapper;
	@Autowired
	private SysRoleMapper sysRoleMapper;
	@Autowired
	private SysDepartRoleUserMapper departRoleUserMapper;
	@Autowired
	private SysDepartRoleMapper sysDepartRoleMapper;
	@Resource
	private BaseCommonService baseCommonService;
	@Autowired
	private SysThirdAccountMapper sysThirdAccountMapper;
	@Autowired
	ThirdAppWechatEnterpriseServiceImpl wechatEnterpriseService;
	@Autowired
	ThirdAppDingtalkServiceImpl dingtalkService;
	@Autowired
	SysRoleIndexMapper sysRoleIndexMapper;
	@Autowired
	SysTenantMapper sysTenantMapper;
	@Autowired
    private SysUserTenantMapper relationMapper;
	@Autowired
    private SysUserTenantMapper userTenantMapper;
	
	@Override
	public Result<IPage<SysUser>> queryPageList(HttpServletRequest req, QueryWrapper<SysUser> queryWrapper, Integer pageSize, Integer pageNo) {
		Result<IPage<SysUser>> result = new Result<IPage<SysUser>>();
		//update-begin-Author:wangshuai--Date:20211119--for:【vue3】通过部门id查询用户，通过code查询id
		//部门ID
		String departId = req.getParameter("departId");
		if (oConvertUtils.isNotEmpty(departId)) {
			LambdaQueryWrapper<SysUserDepart> query = new LambdaQueryWrapper<>();
			query.eq(SysUserDepart::getDepId, departId);
			List<SysUserDepart> list = sysUserDepartMapper.selectList(query);
			List<String> userIds = list.stream().map(SysUserDepart::getUserId).collect(Collectors.toList());
			//update-begin---author:wangshuai ---date:20220322  for：[issues/I4XTYB]查询用户时，当部门id 下没有分配用户时接口报错------------
			if (oConvertUtils.listIsNotEmpty(userIds)) {
				queryWrapper.in("id", userIds);
			} else {
				return Result.OK();
			}
			//update-end---author:wangshuai ---date:20220322  for：[issues/I4XTYB]查询用户时，当部门id 下没有分配用户时接口报错------------
		}
		//用户ID
		String code = req.getParameter("code");
		if (oConvertUtils.isNotEmpty(code)) {
			queryWrapper.in("id", Arrays.asList(code.split(",")));
			pageSize = code.split(",").length;
		}
		//update-end-Author:wangshuai--Date:20211119--for:【vue3】通过部门id查询用户，通过code查询id

		//update-begin-author:taoyan--date:20220104--for: JTC-372 【用户冻结问题】 online授权、用户组件，选择用户都能看到被冻结的用户
		String status = req.getParameter("status");
		if (oConvertUtils.isNotEmpty(status)) {
			queryWrapper.eq("status", Integer.parseInt(status));
		}
		//update-end-author:taoyan--date:20220104--for: JTC-372 【用户冻结问题】 online授权、用户组件，选择用户都能看到被冻结的用户

		//TODO 外部模拟登陆临时账号，列表不显示
		queryWrapper.ne("username", "_reserve_user_external");
		Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);
		IPage<SysUser> pageList = this.page(page, queryWrapper);

		//批量查询用户的所属部门
		//step.1 先拿到全部的 useids
		//step.2 通过 useids，一次性查询用户的所属部门名字
		List<String> userIds = pageList.getRecords().stream().map(SysUser::getId).collect(Collectors.toList());
		if (userIds != null && userIds.size() > 0) {
			Map<String, String> useDepNames = this.getDepNamesByUserIds(userIds);
			pageList.getRecords().forEach(item -> {
				item.setOrgCodeTxt(useDepNames.get(item.getId()));
				//查询用户的租户ids
				List<Integer> list = userTenantMapper.getTenantIdsByUserId(item.getId());
				if (oConvertUtils.isNotEmpty(list)) {
					item.setRelTenantIds(StringUtils.join(list.toArray(), SymbolConstant.COMMA));
				} else {
					item.setRelTenantIds("");
				}
			});
		}

		result.setSuccess(true);
		result.setResult(pageList);
		//log.info(pageList.toString());
		return result;
	}


    @Override
    @CacheEvict(value = {CacheConstant.SYS_USERS_CACHE}, allEntries = true)
    public Result<?> resetPassword(String username, String oldpassword, String newpassword, String confirmpassword) {
        SysUser user = userMapper.getUserByName(username);
        String passwordEncode = PasswordUtil.encrypt(username, oldpassword, user.getSalt());
        if (!user.getPassword().equals(passwordEncode)) {
            return Result.error("旧密码输入错误!");
        }
        if (oConvertUtils.isEmpty(newpassword)) {
            return Result.error("新密码不允许为空!");
        }
        if (!newpassword.equals(confirmpassword)) {
            return Result.error("两次输入密码不一致!");
        }
        String password = PasswordUtil.encrypt(username, newpassword, user.getSalt());
        this.userMapper.update(new SysUser().setPassword(password), new LambdaQueryWrapper<SysUser>().eq(SysUser::getId, user.getId()));
        return Result.ok("密码重置成功!");
    }

    @Override
    @CacheEvict(value = {CacheConstant.SYS_USERS_CACHE}, allEntries = true)
    public Result<?> changePassword(SysUser sysUser) {
        String salt = oConvertUtils.randomGen(8);
        sysUser.setSalt(salt);
        String password = sysUser.getPassword();
        String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), password, salt);
        sysUser.setPassword(passwordEncode);
        this.userMapper.updateById(sysUser);
        return Result.ok("密码修改成功!");
    }

    @Override
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteUser(String userId) {
		//1.删除用户
		this.removeById(userId);
		return false;
	}

	@Override
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteBatchUsers(String userIds) {
		//1.删除用户
		this.removeByIds(Arrays.asList(userIds.split(",")));
		return false;
	}

	@Override
	public SysUser getUserByName(String username) {
		SysUser sysUser = userMapper.getUserByName(username);
		//查询用户的租户ids
		if(sysUser!=null){
			List<Integer> list = userTenantMapper.getTenantIdsByUserId(sysUser.getId());
			if (oConvertUtils.isNotEmpty(list)) {
				sysUser.setRelTenantIds(StringUtils.join(list.toArray(), SymbolConstant.COMMA));
			} else {
				sysUser.setRelTenantIds("");
			}
		}
		return sysUser;
	}
	
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addUserWithRole(SysUser user, String roles) {
		this.save(user);
		if(oConvertUtils.isNotEmpty(roles)) {
			String[] arr = roles.split(",");
			for (String roleId : arr) {
				SysUserRole userRole = new SysUserRole(user.getId(), roleId);
				sysUserRoleMapper.insert(userRole);
			}
		}
	}

	@Override
	@CacheEvict(value= {CacheConstant.SYS_USERS_CACHE}, allEntries=true)
	@Transactional(rollbackFor = Exception.class)
	public void editUserWithRole(SysUser user, String roles) {
		this.updateById(user);
		//先删后加
		sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, user.getId()));
		if(oConvertUtils.isNotEmpty(roles)) {
			String[] arr = roles.split(",");
			for (String roleId : arr) {
				SysUserRole userRole = new SysUserRole(user.getId(), roleId);
				sysUserRoleMapper.insert(userRole);
			}
		}
	}


	@Override
	public List<String> getRole(String username) {
		return sysUserRoleMapper.getRoleByUserName(username);
	}

	/**
	 * 获取动态首页路由配置
	 * @param username
	 * @param version
	 * @return
	 */
	@Override
	public SysRoleIndex getDynamicIndexByUserRole(String username,String version) {
		List<String> roles = sysUserRoleMapper.getRoleByUserName(username);
		String componentUrl = RoleIndexConfigEnum.getIndexByRoles(roles);
		SysRoleIndex roleIndex = new SysRoleIndex(componentUrl);
		//只有 X-Version=v3 的时候，才读取sys_role_index表获取角色首页配置
		if (oConvertUtils.isNotEmpty(version) && roles!=null && roles.size()>0) {
			LambdaQueryWrapper<SysRoleIndex> routeIndexQuery = new LambdaQueryWrapper();
			//用户所有角色
			routeIndexQuery.in(SysRoleIndex::getRoleCode, roles);
			//角色首页状态0：未开启  1：开启
			routeIndexQuery.eq(SysRoleIndex::getStatus, CommonConstant.STATUS_1);
			//优先级正序排序
			routeIndexQuery.orderByAsc(SysRoleIndex::getPriority);
			List<SysRoleIndex> list = sysRoleIndexMapper.selectList(routeIndexQuery);
			if (null != list && list.size() > 0) {
				roleIndex = list.get(0);
			}
		}
		
		//如果componentUrl为空，则返回空
		if(oConvertUtils.isEmpty(roleIndex.getComponent())){
			return null;
		}
		return roleIndex;
	}

	/**
	 * 通过用户名获取用户角色集合
	 * @param username 用户名
     * @return 角色集合
	 */
	@Override
	public Set<String> getUserRolesSet(String username) {
		// 查询用户拥有的角色集合
		List<String> roles = sysUserRoleMapper.getRoleByUserName(username);
		log.info("-------通过数据库读取用户拥有的角色Rules------username： " + username + ",Roles size: " + (roles == null ? 0 : roles.size()));
		return new HashSet<>(roles);
	}

	/**
	 * 通过用户名获取用户权限集合
	 *
	 * @param username 用户名
	 * @return 权限集合
	 */
	@Override
	public Set<String> getUserPermissionsSet(String username) {
		Set<String> permissionSet = new HashSet<>();
		List<SysPermission> permissionList = sysPermissionMapper.queryByUser(username);
		for (SysPermission po : permissionList) {
//			// TODO URL规则有问题？
//			if (oConvertUtils.isNotEmpty(po.getUrl())) {
//				permissionSet.add(po.getUrl());
//			}
			if (oConvertUtils.isNotEmpty(po.getPerms())) {
				permissionSet.add(po.getPerms());
			}
		}
		log.info("-------通过数据库读取用户拥有的权限Perms------username： "+ username+",Perms size: "+ (permissionSet==null?0:permissionSet.size()) );
		return permissionSet;
	}

	/**
	 * 升级SpringBoot2.6.6,不允许循环依赖
	 * @author:qinfeng
	 * @update: 2022-04-07
	 * @param username
	 * @return
	 */
	@Override
	public SysUserCacheInfo getCacheUser(String username) {
		SysUserCacheInfo info = new SysUserCacheInfo();
		info.setOneDepart(true);
		if(oConvertUtils.isEmpty(username)) {
			return null;
		}

		//查询用户信息
		SysUser sysUser = userMapper.getUserByName(username);
		if(sysUser!=null) {
			info.setSysUserCode(sysUser.getUsername());
			info.setSysUserName(sysUser.getRealname());
			info.setSysOrgCode(sysUser.getOrgCode());
		}
		
		//多部门支持in查询
		List<SysDepart> list = sysDepartMapper.queryUserDeparts(sysUser.getId());
		List<String> sysMultiOrgCode = new ArrayList<String>();
		if(list==null || list.size()==0) {
			//当前用户无部门
			//sysMultiOrgCode.add("0");
		}else if(list.size()==1) {
			sysMultiOrgCode.add(list.get(0).getOrgCode());
		}else {
			info.setOneDepart(false);
			for (SysDepart dpt : list) {
				sysMultiOrgCode.add(dpt.getOrgCode());
			}
		}
		info.setSysMultiOrgCode(sysMultiOrgCode);
		
		return info;
	}

    /**
     * 根据部门Id查询
     * @param page
     * @param departId 部门id
     * @param username 用户账户名称
     * @return
     */
	@Override
	public IPage<SysUser> getUserByDepId(Page<SysUser> page, String departId,String username) {
		return userMapper.getUserByDepId(page, departId,username);
	}

	@Override
	public IPage<SysUser> getUserByDepIds(Page<SysUser> page, List<String> departIds, String username) {
		return userMapper.getUserByDepIds(page, departIds,username);
	}

	@Override
	public Map<String, String> getDepNamesByUserIds(List<String> userIds) {
		List<SysUserDepVo> list = this.baseMapper.getDepNamesByUserIds(userIds);

		Map<String, String> res = new HashMap(5);
		list.forEach(item -> {
					if (res.get(item.getUserId()) == null) {
						res.put(item.getUserId(), item.getDepartName());
					} else {
						res.put(item.getUserId(), res.get(item.getUserId()) + "," + item.getDepartName());
					}
				}
		);
		return res;
	}

	//update-begin-author:taoyan date:2022-9-13 for: VUEN-2245【漏洞】发现新漏洞待处理20220906 ----sql注入  方法没有使用，注掉
/*	@Override
	public IPage<SysUser> getUserByDepartIdAndQueryWrapper(Page<SysUser> page, String departId, QueryWrapper<SysUser> queryWrapper) {
		LambdaQueryWrapper<SysUser> lambdaQueryWrapper = queryWrapper.lambda();

		lambdaQueryWrapper.eq(SysUser::getDelFlag, CommonConstant.DEL_FLAG_0);
        lambdaQueryWrapper.inSql(SysUser::getId, "SELECT user_id FROM sys_user_depart WHERE dep_id = '" + departId + "'");

        return userMapper.selectPage(page, lambdaQueryWrapper);
	}*/
	//update-end-author:taoyan date:2022-9-13 for: VUEN-2245【漏洞】发现新漏洞待处理20220906 ----sql注入 方法没有使用，注掉

	@Override
	public IPage<SysUserSysDepartModel> queryUserByOrgCode(String orgCode, SysUser userParams, IPage page) {
		List<SysUserSysDepartModel> list = baseMapper.getUserByOrgCode(page, orgCode, userParams);
		Integer total = baseMapper.getUserByOrgCodeTotal(orgCode, userParams);

		IPage<SysUserSysDepartModel> result = new Page<>(page.getCurrent(), page.getSize(), total);
		result.setRecords(list);

		return result;
	}

    /**
     * 根据角色Id查询
     * @param page
     * @param roleId 角色id
     * @param username 用户账户名称
     * @return
     */
	@Override
	public IPage<SysUser> getUserByRoleId(Page<SysUser> page, String roleId, String username) {
		return userMapper.getUserByRoleId(page,roleId,username);
	}


	@Override
	@CacheEvict(value= {CacheConstant.SYS_USERS_CACHE}, key="#username")
	public void updateUserDepart(String username,String orgCode,Integer loginTenantId) {
		baseMapper.updateUserDepart(username, orgCode,loginTenantId);
	}


	@Override
	public SysUser getUserByPhone(String phone) {
		return userMapper.getUserByPhone(phone);
	}


	@Override
	public SysUser getUserByEmail(String email) {
		return userMapper.getUserByEmail(email);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addUserWithDepart(SysUser user, String selectedParts) {
//		this.save(user);  //保存角色的时候已经添加过一次了
		if(oConvertUtils.isNotEmpty(selectedParts)) {
			String[] arr = selectedParts.split(",");
			for (String deaprtId : arr) {
				SysUserDepart userDeaprt = new SysUserDepart(user.getId(), deaprtId);
				sysUserDepartMapper.insert(userDeaprt);
			}
		}
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
	public void editUserWithDepart(SysUser user, String departs) {
        //更新角色的时候已经更新了一次了，可以再跟新一次
		this.updateById(user);
		String[] arr = {};
		if(oConvertUtils.isNotEmpty(departs)){
			arr = departs.split(",");
		}
		//查询已关联部门
		List<SysUserDepart> userDepartList = sysUserDepartMapper.selectList(new QueryWrapper<SysUserDepart>().lambda().eq(SysUserDepart::getUserId, user.getId()));
		if(userDepartList != null && userDepartList.size()>0){
			for(SysUserDepart depart : userDepartList ){
				//修改已关联部门删除部门用户角色关系
				if(!Arrays.asList(arr).contains(depart.getDepId())){
					List<SysDepartRole> sysDepartRoleList = sysDepartRoleMapper.selectList(
							new QueryWrapper<SysDepartRole>().lambda().eq(SysDepartRole::getDepartId,depart.getDepId()));
					List<String> roleIds = sysDepartRoleList.stream().map(SysDepartRole::getId).collect(Collectors.toList());
					if(roleIds != null && roleIds.size()>0){
						departRoleUserMapper.delete(new QueryWrapper<SysDepartRoleUser>().lambda().eq(SysDepartRoleUser::getUserId, user.getId())
								.in(SysDepartRoleUser::getDroleId,roleIds));
					}
				}
			}
		}
		//先删后加
		sysUserDepartMapper.delete(new QueryWrapper<SysUserDepart>().lambda().eq(SysUserDepart::getUserId, user.getId()));
		if(oConvertUtils.isNotEmpty(departs)) {
			for (String departId : arr) {
				SysUserDepart userDepart = new SysUserDepart(user.getId(), departId);
				sysUserDepartMapper.insert(userDepart);
			}
		}
	}


	/**
	   * 校验用户是否有效
	 * @param sysUser
	 * @return
	 */
	@Override
	public Result<?> checkUserIsEffective(SysUser sysUser) {
		Result<?> result = new Result<Object>();
		//情况1：根据用户信息查询，该用户不存在
		if (sysUser == null) {
			result.error500("该用户不存在，请注册");
			baseCommonService.addLog("用户登录失败，用户不存在！", CommonConstant.LOG_TYPE_1, null);
			return result;
		}
		//情况2：根据用户信息查询，该用户已注销
		//update-begin---author:王帅   Date:20200601  for：if条件永远为falsebug------------
		if (CommonConstant.DEL_FLAG_1.equals(sysUser.getDelFlag())) {
		//update-end---author:王帅   Date:20200601  for：if条件永远为falsebug------------
			baseCommonService.addLog("用户登录失败，用户名:" + sysUser.getUsername() + "已注销！", CommonConstant.LOG_TYPE_1, null);
			result.error500("该用户已注销");
			return result;
		}
		//情况3：根据用户信息查询，该用户已冻结
		if (CommonConstant.USER_FREEZE.equals(sysUser.getStatus())) {
			baseCommonService.addLog("用户登录失败，用户名:" + sysUser.getUsername() + "已冻结！", CommonConstant.LOG_TYPE_1, null);
			result.error500("该用户已冻结");
			return result;
		}
		return result;
	}

	@Override
	public List<SysUser> queryLogicDeleted() {
        //update-begin---author:wangshuai ---date:20221116  for：回收站查询未离职的------------
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(SysUser::getStatus, CommonConstant.USER_QUIT);
        return this.queryLogicDeleted(wrapper);
        //update-end---author:wangshuai ---date:20221116  for：回收站查询未离职的--------------
    }

	@Override
	public List<SysUser> queryLogicDeleted(LambdaQueryWrapper<SysUser> wrapper) {
		if (wrapper == null) {
			wrapper = new LambdaQueryWrapper<>();
		}
		wrapper.eq(SysUser::getDelFlag, CommonConstant.DEL_FLAG_1);
		return userMapper.selectLogicDeleted(wrapper);
	}

	@Override
	@CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
	public boolean revertLogicDeleted(List<String> userIds, SysUser updateEntity) {
		return userMapper.revertLogicDeleted(userIds, updateEntity) > 0;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeLogicDeleted(List<String> userIds) {
		// 1. 删除用户
		int line = userMapper.deleteLogicDeleted(userIds);
		// 2. 删除用户部门关系
		line += sysUserDepartMapper.delete(new LambdaQueryWrapper<SysUserDepart>().in(SysUserDepart::getUserId, userIds));
		//3. 删除用户角色关系
		line += sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().in(SysUserRole::getUserId, userIds));
		//4.同步删除第三方App的用户
		try {
			dingtalkService.removeThirdAppUser(userIds);
			wechatEnterpriseService.removeThirdAppUser(userIds);
		} catch (Exception e) {
			log.error("同步删除第三方App的用户失败：", e);
		}
		//5. 删除第三方用户表（因为第4步需要用到第三方用户表，所以在他之后删）
		line += sysThirdAccountMapper.delete(new LambdaQueryWrapper<SysThirdAccount>().in(SysThirdAccount::getSysUserId, userIds));

		//6. 删除租户用户中间表的数据
		line += userTenantMapper.delete(new LambdaQueryWrapper<SysUserTenant>().in(SysUserTenant::getUserId,userIds));
		
		return line != 0;
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateNullPhoneEmail() {
        userMapper.updateNullByEmptyString("email");
        userMapper.updateNullByEmptyString("phone");
        return true;
    }

	@Override
	public void saveThirdUser(SysUser sysUser) {
		//保存用户
		String userid = UUIDGenerator.generate();
		sysUser.setId(userid);
		baseMapper.insert(sysUser);
		//获取第三方角色
		SysRole sysRole = sysRoleMapper.selectOne(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleCode, "third_role"));
		//保存用户角色
		SysUserRole userRole = new SysUserRole();
		userRole.setRoleId(sysRole.getId());
		userRole.setUserId(userid);
		sysUserRoleMapper.insert(userRole);
	}

	@Override
	public List<SysUser> queryByDepIds(List<String> departIds, String username) {
		return userMapper.queryByDepIds(departIds,username);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveUser(SysUser user, String selectedRoles, String selectedDeparts, String relTenantIds) {
		//step.1 保存用户
		this.save(user);
		//获取用户保存前台传过来的租户id并添加到租户
        this.saveUserTenant(user.getId(),relTenantIds);
		//step.2 保存角色
		if(oConvertUtils.isNotEmpty(selectedRoles)) {
			String[] arr = selectedRoles.split(",");
			for (String roleId : arr) {
				SysUserRole userRole = new SysUserRole(user.getId(), roleId);
				sysUserRoleMapper.insert(userRole);
			}
		}

		//update-begin---author:wangshuai ---date:20230112  for：用户创建的时候增加临时角色 test------------
		//开启租户saas模式
		if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
			String testRoleId = "ee8626f80f7c2619917b6236f3a7f02b";
			//如果前台没有传递角色或者传过来的劫色没有临时角色,那么默认临时角色 test
			if (oConvertUtils.isEmpty(selectedRoles) || !selectedRoles.contains(testRoleId)) {
				SysUserRole userRole = new SysUserRole(user.getId(), testRoleId);
				sysUserRoleMapper.insert(userRole);
			}
		}
		//update-end---author:wangshuai ---date:20230112  for：用户创建的时候增加临时角色 test------------
		
		//step.3 保存所属部门
		if(oConvertUtils.isNotEmpty(selectedDeparts)) {
			String[] arr = selectedDeparts.split(",");
			for (String deaprtId : arr) {
				SysUserDepart userDeaprt = new SysUserDepart(user.getId(), deaprtId);
				sysUserDepartMapper.insert(userDeaprt);
			}
		}

		//触发入职流程
		LoginUser userInfo=new LoginUser();
		BeanUtils.copyProperties(user,userInfo);
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
	public void editUser(SysUser user, String roles, String departs, String relTenantIds) {
		//获取用户编辑前台传过来的租户id
        this.editUserTenants(user.getId(),relTenantIds);
		//step.1 修改用户基础信息
		this.updateById(user);
		//step.2 修改角色
		//处理用户角色 先删后加
		sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, user.getId()));
		if(oConvertUtils.isNotEmpty(roles)) {
			String[] arr = roles.split(",");
			for (String roleId : arr) {
				SysUserRole userRole = new SysUserRole(user.getId(), roleId);
				sysUserRoleMapper.insert(userRole);
			}
		}

		//step.3 修改部门
		String[] arr = {};
		if(oConvertUtils.isNotEmpty(departs)){
			arr = departs.split(",");
		}
		//查询已关联部门
		List<SysUserDepart> userDepartList = sysUserDepartMapper.selectList(new QueryWrapper<SysUserDepart>().lambda().eq(SysUserDepart::getUserId, user.getId()));
		if(userDepartList != null && userDepartList.size()>0){
			for(SysUserDepart depart : userDepartList ){
				//修改已关联部门删除部门用户角色关系
				if(!Arrays.asList(arr).contains(depart.getDepId())){
					List<SysDepartRole> sysDepartRoleList = sysDepartRoleMapper.selectList(
							new QueryWrapper<SysDepartRole>().lambda().eq(SysDepartRole::getDepartId,depart.getDepId()));
					List<String> roleIds = sysDepartRoleList.stream().map(SysDepartRole::getId).collect(Collectors.toList());
					if(roleIds != null && roleIds.size()>0){
						departRoleUserMapper.delete(new QueryWrapper<SysDepartRoleUser>().lambda().eq(SysDepartRoleUser::getUserId, user.getId())
								.in(SysDepartRoleUser::getDroleId,roleIds));
					}
				}
			}
		}
		//先删后加
		sysUserDepartMapper.delete(new QueryWrapper<SysUserDepart>().lambda().eq(SysUserDepart::getUserId, user.getId()));
		if(oConvertUtils.isNotEmpty(departs)) {
			for (String departId : arr) {
				SysUserDepart userDepart = new SysUserDepart(user.getId(), departId);
				sysUserDepartMapper.insert(userDepart);
			}
		}
		//step.4 修改手机号和邮箱
		// 更新手机号、邮箱空字符串为 null
		userMapper.updateNullByEmptyString("email");
		userMapper.updateNullByEmptyString("phone");

	}

	@Override
	public List<String> userIdToUsername(Collection<String> userIdList) {
		LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.in(SysUser::getId, userIdList);
		List<SysUser> userList = super.list(queryWrapper);
		return userList.stream().map(SysUser::getUsername).collect(Collectors.toList());
	}

	@Override
	@Cacheable(cacheNames=CacheConstant.SYS_USERS_CACHE, key="#username")
	@SensitiveEncode
	public LoginUser getEncodeUserInfo(String username){
		if(oConvertUtils.isEmpty(username)) {
			return null;
		}
		LoginUser loginUser = new LoginUser();
		SysUser sysUser = userMapper.getUserByName(username);
		//查询用户的租户ids
		this.setUserTenantIds(sysUser);
		if(sysUser==null) {
			return null;
		}
		BeanUtils.copyProperties(sysUser, loginUser);
		return loginUser;
	}

    @Override
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
    @Transactional(rollbackFor = Exception.class)
    public void userQuit(String username) {
        SysUser sysUser = userMapper.getUserByName(username);
        if(null == sysUser){
            throw new JeecgBootException("离职失败，该用户已不存在");
        }
		//update-begin---author:wangshuai ---date:20230111  for：[QQYUN-3951]租户用户离职重构------------
		int tenantId = oConvertUtils.getInt(TenantContext.getTenant(), 0);
		//更新用户租户表的状态为离职状态
		if(tenantId==0){
			throw new JeecgBootException("离职失败，租户不存在");
		}
		LambdaQueryWrapper<SysUserTenant> query = new LambdaQueryWrapper<>();
		query.eq(SysUserTenant::getUserId,sysUser.getId());
		query.eq(SysUserTenant::getTenantId,tenantId);
		SysUserTenant userTenant = new SysUserTenant();
		userTenant.setStatus(CommonConstant.USER_TENANT_QUIT);
		userTenantMapper.update(userTenant,query);
		//update-end---author:wangshuai ---date:20230111  for：[QQYUN-3951]租户用户离职重构------------
		//触发离职流程
		LoginUser userInfo=new LoginUser();
		BeanUtils.copyProperties(sysUser,userInfo);
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
    }

    @Override
    public List<SysUser> getQuitList(Integer tenantId) {
        return userMapper.getTenantQuitList(tenantId);
    }

    @Override
    public void updateStatusAndFlag(List<String> userIds, SysUser sysUser) {
        userMapper.updateStatusAndFlag(userIds,sysUser);
    }

	/**
	 * 设置登录租户
	 * @param sysUser
	 * @return
	 */
	@Override
	public Result<JSONObject>  setLoginTenant(SysUser sysUser, JSONObject obj, String username, Result<JSONObject> result){
		// update-begin--Author:sunjianlei Date:20210802 for：获取用户租户信息
		//用户有哪些租户
		List<SysTenant> tenantList = null;
        //update-begin---author:wangshuai ---date:20221223  for：[QQYUN-3371]租户逻辑改造，改成关系表------------
        List<Integer> tenantIdList = relationMapper.getTenantIdsNoStatus(sysUser.getId());
        if (null!=tenantIdList && tenantIdList.size()>0) {
		//update-end---author:wangshuai ---date:20221223  for：[QQYUN-3371]租户逻辑改造，改成关系表--------------
			//-------------------------------------------------------------------------------------
			//查询有效的租户集合
			LambdaQueryWrapper<SysTenant> queryWrapper = new LambdaQueryWrapper<>();
			queryWrapper.in(SysTenant::getId, tenantIdList);
			queryWrapper.eq(SysTenant::getStatus, Integer.valueOf(CommonConstant.STATUS_1));
			tenantList = sysTenantMapper.selectList(queryWrapper);
			//-------------------------------------------------------------------------------------
			
			if (tenantList.size() == 0) {
				return result.error500("与该用户关联的租户均已被冻结，无法登录！");
			} else {
				obj.put("tenantList", tenantList);
			}
		}
		// update-end--Author:sunjianlei Date:20210802 for：获取用户租户信息


		//登录会话租户ID，有效性重置
		if (tenantList != null && tenantList.size() > 0) {
			if (tenantList.size() == 1) {
				sysUser.setLoginTenantId(tenantList.get(0).getId());
			} else {
				List<SysTenant> listAfterFilter = tenantList.stream().filter(s -> s.getId().equals(sysUser.getLoginTenantId())).collect(Collectors.toList());
				if (listAfterFilter == null || listAfterFilter.size() == 0) {
					//如果上次登录租户ID，在用户拥有的租户集合里面没有了，则随机取用户拥有的第一个租户ID
					sysUser.setLoginTenantId(tenantList.get(0).getId());
				}
			}
		} else {
			//无租户的时候，设置为 0
			sysUser.setLoginTenantId(0);
		}
		//设置用户登录缓存租户
		this.updateUserDepart(username, null,sysUser.getLoginTenantId());
		log.info(" 登录接口用户的租户ID = {}", sysUser.getLoginTenantId());
		return null;
	}


    /**
     * 获取租户id
     * @param sysUser
     */
    private void setUserTenantIds(SysUser sysUser) {
		if(ObjectUtils.isNotEmpty(sysUser)) {
			List<Integer> list  = relationMapper.getTenantIdsNoStatus(sysUser.getId());
			if(null!=list && list.size()>0){
				sysUser.setRelTenantIds(StringUtils.join(list.toArray(), ","));
			}else{
				sysUser.setRelTenantIds("");
			}
		}
    }

    /**
     * 保存租户
     * @param userId
     * @param relTenantIds
     */
    private void saveUserTenant(String userId, String relTenantIds) {
        if (oConvertUtils.isNotEmpty(relTenantIds)) {
            String[] tenantIds = relTenantIds.split(SymbolConstant.COMMA);
            for (String tenantId : tenantIds) {
                SysUserTenant relation = new SysUserTenant();
                relation.setUserId(userId);
                relation.setTenantId(Integer.valueOf(tenantId));
                relation.setStatus(CommonConstant.STATUS_1);
                relationMapper.insert(relation);
            }
        }else{
			//是否开启系统管理模块的多租户数据隔离【SAAS多租户模式】
			if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
				SysUserTenant relation = new SysUserTenant();
				relation.setUserId(userId);
				String tenantId = oConvertUtils.getString(TenantContext.getTenant(), "0");
				relation.setTenantId(Integer.valueOf(tenantId));
				relation.setStatus(CommonConstant.STATUS_1);
				relationMapper.insert(relation);
			}
		}
    }

    /**
     * 编辑租户
     * @param userId
     * @param relTenantIds
     */
    private void editUserTenants(String userId, String relTenantIds) {
        LambdaQueryWrapper<SysUserTenant> query = new LambdaQueryWrapper<>();
        query.eq(SysUserTenant::getUserId, userId);
        //数据库的租户id
		List<Integer> oldTenantIds = relationMapper.getTenantIdsNoStatus(userId);
        //如果传过来的租户id为空，那么就删除租户
        if (oConvertUtils.isEmpty(relTenantIds)) {
            this.deleteTenantByUserId(userId, null);
        } else if (oConvertUtils.isNotEmpty(relTenantIds) && oConvertUtils.isEmpty(oldTenantIds)) {
            //如果传过来的租户id不为空但是数据库的租户id为空，那么就新增
            this.saveUserTenant(userId, relTenantIds);
        } else {
			//都不为空，需要比较，进行添加或删除
			if(oConvertUtils.isNotEmpty(oldTenantIds)){
				//找到新的租户id与原来的租户id不同之处，进行删除
				List<Integer> tenantIdList = oldTenantIds.stream().filter(item -> !relTenantIds.contains(item.toString())).collect(Collectors.toList());
				for (Integer tenantId : tenantIdList) {
					this.deleteTenantByUserId(userId, tenantId);
				}
				//找到原来租户的用户id与新的租户id不同之处，进行新增
				String tenantIds = Arrays.stream(relTenantIds.split(SymbolConstant.COMMA)).filter(item -> !oldTenantIds.contains(Integer.valueOf(item))).collect(Collectors.joining(","));
				this.saveUserTenant(userId, tenantIds);
			}
        }
    }

    /**
     * 删除租户通过用户id
     * @param tenantId
     * @param userId
     */
    private void deleteTenantByUserId(String userId,Integer tenantId){
        LambdaQueryWrapper<SysUserTenant> query = new LambdaQueryWrapper<>();
        query.eq(SysUserTenant::getUserId, userId);
        if(oConvertUtils.isNotEmpty(tenantId)){
            query.eq(SysUserTenant::getTenantId, tenantId);
        }
        relationMapper.delete(query);
    }



	@Override
	public void batchEditUsers(JSONObject json) {
		String userIds = json.getString("userIds");
		List<String> idList = JSONArray.parseArray(userIds, String.class);
		//部门
		String selecteddeparts = json.getString("selecteddeparts");
		//职位
		String post = json.getString("post");
		//工作地点? 没有这个字段
		String workAddress = json.getString("workAddress");
		if(oConvertUtils.isNotEmpty(post)) {
			LambdaUpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<SysUser>().lambda()
					.in(SysUser::getId, idList)
					.set(SysUser::getPost, post);
			this.update(updateWrapper);
		}
		if(oConvertUtils.isNotEmpty(selecteddeparts)) {
			//查询当前租户的部门列表
			Integer currentTenantId = oConvertUtils.getInt(TenantContext.getTenant(), 0);
			LambdaQueryWrapper<SysDepart> departQuery = new LambdaQueryWrapper<SysDepart>()
					.eq(SysDepart::getTenantId, currentTenantId);
			List<SysDepart> departList = sysDepartMapper.selectList(departQuery);
			if(departList==null || departList.size()==0){
				log.error("batchEditUsers 根据租户ID没有找到部门>"+currentTenantId);
				return;
			}
			List<String> departIdList = new ArrayList<String>();
			for(SysDepart depart: departList){
				if(depart!=null){
					String id = depart.getId();
					if(oConvertUtils.isNotEmpty(id)){
						departIdList.add(id);
					}
				}
			}
			//删除人员的部门关联
			LambdaQueryWrapper<SysUserDepart> query = new LambdaQueryWrapper<SysUserDepart>()
					.in(SysUserDepart::getUserId, idList)
					.in(SysUserDepart::getDepId, departIdList);
			sysUserDepartMapper.delete(query);
			
			String[] arr = selecteddeparts.split(",");
			
			//再新增
			for (String deaprtId : arr) {
				for(String userId: idList){
					SysUserDepart userDepart = new SysUserDepart(userId, deaprtId);
					sysUserDepartMapper.insert(userDepart);
				}
			}
		}
	}

	@Override
	public DepartAndUserInfo searchByKeyword(String keyword) {
		DepartAndUserInfo departAndUserInfo = new DepartAndUserInfo();
		if(oConvertUtils.isNotEmpty(keyword)){
			LambdaQueryWrapper<SysUser> query1 = new LambdaQueryWrapper<SysUser>()
					.like(SysUser::getRealname, keyword);
			String str = oConvertUtils.getString(TenantContext.getTenant(), "0");
			Integer tenantId = Integer.valueOf(str);
			if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL){
				List<String> userIds = userTenantMapper.getUserIdsByTenantId(tenantId);
				if (oConvertUtils.listIsNotEmpty(userIds)) {
					query1.in(SysUser::getId, userIds);
				}else{
					query1.eq(SysUser::getId, "");
				}
			}
			List<SysUser> list1 = this.baseMapper.selectList(query1);
			if(list1!=null && list1.size()>0){
				List<UserAvatar> userList = list1.stream().map(v -> new UserAvatar(v)).collect(Collectors.toList());
				departAndUserInfo.setUserList(userList);
			}

			LambdaQueryWrapper<SysDepart> query2 = new LambdaQueryWrapper<SysDepart>()
					.like(SysDepart::getDepartName, keyword);
			if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL){
				query2.eq(SysDepart::getTenantId, tenantId);
			}
			List<SysDepart> list2 = sysDepartMapper.selectList(query2);
			if(list2!=null && list2.size()>0){
				List<DepartInfo> departList = new ArrayList<>();
				for(SysDepart depart: list2){
					List<String> orgName = new ArrayList<>();
					List<String> orgId = new ArrayList<>();
					getParentDepart(depart, orgName, orgId);
					DepartInfo departInfo = new DepartInfo();
					departInfo.setId(depart.getId());
					departInfo.setOrgId(orgId);
					departInfo.setOrgName(orgName);
					departList.add(departInfo);
				}
				departAndUserInfo.setDepartList(departList);
			}
		}
		return departAndUserInfo;
	}

	@Override
	public UpdateDepartInfo getUpdateDepartInfo(String departId) {
		SysDepart depart = sysDepartMapper.selectById(departId);
		if(depart!=null){
			UpdateDepartInfo info = new UpdateDepartInfo(depart);
			List<SysDepart> subList = sysDepartMapper.queryDeptByPid(departId);
			if(subList!=null && subList.size()>0){
				info.setHasSub(true);
			}
			//获取部门负责人信息
			LambdaQueryWrapper<SysUser> query = new LambdaQueryWrapper<SysUser>()
					.eq(SysUser::getUserIdentity, 2)
					.like(SysUser::getDepartIds, depart.getId());
			List<SysUser> userList = this.baseMapper.selectList(query);
			if(userList!=null && userList.size()>0){
				List<String> idList = userList.stream().map(i -> i.getId()).collect(Collectors.toList());
				info.setChargePersonList(idList);
			}
			return info;
		}
		return null;
	}

	@Override
	public void doUpdateDepartInfo(UpdateDepartInfo info) {
		String departId = info.getDepartId();
		SysDepart depart = sysDepartMapper.selectById(departId);
		if(depart!=null){
			//修改部门信息-上级和部门名称
			if(!depart.getParentId().equals(info.getParentId())){
				String pid = info.getParentId();
				SysDepart parentDepart = sysDepartMapper.selectById(pid);
				if(parentDepart!=null){
					String orgCode = getNextOrgCode(pid);
					depart.setOrgCode(orgCode);
					depart.setParentId(pid);
				}
			}
			depart.setDepartName(info.getDepartName());
			sysDepartMapper.updateById(depart);
			//先查询这个部门的负责人
			List<SysUser> departChargeUsers = queryDepartChargePersons(departId);
			List<String> departChargeUserIdList = departChargeUsers.stream().map(i -> i.getId()).collect(Collectors.toList());
			//修改部门负责人
			List<String> userIdList = info.getChargePersonList();
			if(userIdList!=null && userIdList.size()>0){
				for(String userId: userIdList){
					SysUser user = this.baseMapper.selectById(userId);
					if(user!=null){
						departChargeUserIdList.remove(user.getId());
						user.setUserIdentity(2);
						String departIds = user.getDepartIds();
						if(oConvertUtils.isEmpty(departIds)){
							user.setDepartIds(departId);
						}else{
							List<String> list = new ArrayList<String>(Arrays.asList(departIds.split(",")));
							if(list.indexOf(departId)>=0){
								continue;
							}else{
								list.add(departId);
								String newDepartIds = String.join(",", list);
								user.setDepartIds(newDepartIds);
							}
						}
						this.baseMapper.updateById(user);
					}
				}
				//删除
				for(String chargeUserId: departChargeUserIdList){
					for(SysUser chargeUser: departChargeUsers){
						if(chargeUser.getId().equals(chargeUserId)){
							String departIds = chargeUser.getDepartIds();
							List<String> list = new ArrayList<String>(Arrays.asList(departIds.split(",")));
							list.remove(departId);
							String newDepartIds = String.join(",", list);
							chargeUser.setDepartIds(newDepartIds);
							this.baseMapper.updateById(chargeUser);
							break;
						}
					}
				}
				
			}
		}
	}

	private List<SysUser> queryDepartChargePersons(String departId){
    	List<SysUser> result = new ArrayList<>();
    	LambdaQueryWrapper<SysUserDepart> query1 = new LambdaQueryWrapper<SysUserDepart>()
				.eq(SysUserDepart::getDepId, departId);
    	List<SysUserDepart> list1 = sysUserDepartMapper.selectList(query1);
    	if(list1!=null && list1.size()>0){
			List<String> userIdList = list1.stream().map(item -> item.getUserId()).collect(Collectors.toList());
			LambdaQueryWrapper<SysUser> query2 = new LambdaQueryWrapper<SysUser>()
					.in(SysUser::getId, userIdList);
			List<SysUser> userList = this.baseMapper.selectList(query2);
			if(userList!=null && userList.size()>0){
				for(SysUser user: userList){
					Integer identity = user.getUserIdentity();
					String deps = user.getDepartIds();
					if(identity!=null && identity==2){
						if(oConvertUtils.isNotEmpty(deps)){
							if(deps.indexOf(departId)>=0){
								result.add(user);
							}
						}
					}
				}
			}
		}
    	return result;
	}
	
	/**
	 * 变更父级部门 修改编码
	 * @param parentId
	 * @return
	 */
	private String getNextOrgCode(String parentId){
		JSONObject formData = new JSONObject();
		formData.put("parentId",parentId);
		String[] codeArray = (String[]) FillRuleUtil.executeRule(FillRuleConstant.DEPART, formData);
		return codeArray[0];
	}

	@Override
	public void changeDepartChargePerson(JSONObject json) {
		String userId = json.getString("userId");
		String departId = json.getString("departId");
		boolean status  = json.getBoolean("status");
		SysUser user = this.getById(userId);
		if(user!=null){
			String ids = user.getDepartIds();
			if(status==true){
				//设置部门负责人
				if(oConvertUtils.isEmpty(ids)){
					user.setDepartIds(departId);
				}else{
					List<String> list = new ArrayList<String>(Arrays.asList(ids.split(",")));
					if(list.indexOf(departId)>=0){
						//啥也不干
					}else{
						list.add(departId);
						String newIds = String.join(",", list);
						user.setDepartIds(newIds);
					}
				}
			}else{
				// 取消负责人
				if(oConvertUtils.isNotEmpty(ids)){
					List<String> list = new ArrayList<String>();
					for(String temp: ids.split(",")){
						if(oConvertUtils.isEmpty(temp)){
							continue;
						}
						if(!temp.equals(departId)){
							list.add(temp);
						}
					}
					String newIds = "";
					if(list.size()>0){
						newIds = String.join(",", list);
					}
					user.setDepartIds(newIds);
				}
			}
			this.updateById(user);
		}
	}

	/**
	 * 找上级部门
	 * @param depart
	 * @param orgName
	 * @param orgId
	 */
	private void getParentDepart(SysDepart depart,List<String> orgName,List<String> orgId){
		String pid = depart.getParentId();
		orgName.add(0, depart.getDepartName());
		orgId.add(0, depart.getId());
		if(oConvertUtils.isNotEmpty(pid)){
			SysDepart temp = sysDepartMapper.selectById(pid);
			getParentDepart(temp, orgName, orgId);
		}
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
	public void editTenantUser(SysUser sysUser, String tenantId, String departs, String roles) {
		SysUser user = new SysUser();
		user.setWorkNo(sysUser.getWorkNo());
		user.setPost(sysUser.getPost());
		user.setId(sysUser.getId());
		this.updateById(user);
		//修改租户用户下的部门
		this.updateTenantDepart(user, tenantId, departs);
	}
	
	/**
	 * 修改租户下的部门
	 * @param departs
	 */
	public void updateTenantDepart(SysUser user, String tenantId, String departs) {
		List<String> departList = new ArrayList<>();
		if (oConvertUtils.isNotEmpty(departs)) {
			//获取当前租户下的部门id,根据前台
			departList = sysUserDepartMapper.getTenantDepart(Arrays.asList(departs.split(SymbolConstant.COMMA)), tenantId);
		}
		//查询当前租户下部门和用户已关联的部门
		List<SysUserDepart> userDepartList = sysUserDepartMapper.getTenantUserDepart(user.getId(), tenantId);
		if (userDepartList != null && userDepartList.size() > 0 && departList.size() > 0) {
			for (SysUserDepart depart : userDepartList) {
				//修改已关联部门删除部门用户角色关系
				if (!departList.contains(depart.getDepId())) {
					List<SysDepartRole> sysDepartRoleList = sysDepartRoleMapper.selectList(
							new QueryWrapper<SysDepartRole>().lambda().eq(SysDepartRole::getDepartId, depart.getDepId()));
					List<String> roleIds = sysDepartRoleList.stream().map(SysDepartRole::getId).collect(Collectors.toList());
					if (roleIds.size() > 0) {
						departRoleUserMapper.delete(new QueryWrapper<SysDepartRoleUser>().lambda().eq(SysDepartRoleUser::getUserId, user.getId())
								.in(SysDepartRoleUser::getDroleId, roleIds));
					}
				}
			}
		}
		if (departList.size() > 0) {
			//删除用户下的部门
			sysUserDepartMapper.deleteUserDepart(user.getId(), tenantId);
			for (String departId : departList) {
				//添加部门
				SysUserDepart userDepart = new SysUserDepart(user.getId(), departId);
				sysUserDepartMapper.insert(userDepart);
			}
		}
	}
}
