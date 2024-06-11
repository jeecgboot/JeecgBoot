package org.jeecg.modules.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.FillRuleConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.constant.enums.DySmsEnum;
import org.jeecg.common.constant.enums.MessageTypeEnum;
import org.jeecg.common.constant.enums.RoleIndexConfigEnum;
import org.jeecg.common.constant.enums.SysAnnmentTypeEnum;
import org.jeecg.common.desensitization.annotation.SensitiveEncode;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.system.vo.SysUserCacheInfo;
import org.jeecg.common.util.*;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.message.handle.impl.SystemSendMsgHandle;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.mapper.*;
import org.jeecg.modules.system.model.SysUserSysDepartModel;
import org.jeecg.modules.system.service.ISysRoleIndexService;
import org.jeecg.modules.system.service.ISysThirdAccountService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.system.vo.SysUserDepVo;
import org.jeecg.modules.system.vo.SysUserPositionVo;
import org.jeecg.modules.system.vo.UserAvatar;
import org.jeecg.modules.system.vo.lowapp.AppExportUserVo;
import org.jeecg.modules.system.vo.lowapp.DepartAndUserInfo;
import org.jeecg.modules.system.vo.lowapp.DepartInfo;
import org.jeecg.modules.system.vo.lowapp.UpdateDepartInfo;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
    ISysRoleIndexService sysRoleIndexService;
	@Autowired
    SysTenantMapper sysTenantMapper;
	@Autowired
    private SysUserTenantMapper relationMapper;
	@Autowired
    private SysUserTenantMapper userTenantMapper;
	@Autowired
	private SysUserPositionMapper sysUserPositionMapper;
	@Autowired
	private SysPositionMapper sysPositionMapper;
	@Autowired
	private SystemSendMsgHandle systemSendMsgHandle;
	
	@Autowired
	private ISysThirdAccountService sysThirdAccountService;

	@Autowired
	private RedisUtil redisUtil;
	
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

		//update-begin---author:wangshuai---date:2024-03-08---for:【QQYUN-8110】在线通讯录支持设置权限(只能看分配的技术支持)---
		String tenantId = TokenUtils.getTenantIdByRequest(req);
		String lowAppId = TokenUtils.getLowAppIdByRequest(req);
//		Object bean = ResourceUtil.getImplementationClass(DataEnhanceEnum.getClassPath(tenantId,lowAppId));
//		if(null != bean){
//			UserFilterEnhance userEnhanceService = (UserFilterEnhance) bean;
//			LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
//			List<String> userIds = userEnhanceService.getUserIds(sysUser.getId());
//			if(CollectionUtil.isNotEmpty(userIds)){
//				queryWrapper.in("id", userIds);
//			}
//		}
		//update-end---author:wangshuai---date:2024-03-08---for:【QQYUN-8110】在线通讯录支持设置权限(只能看分配的技术支持)---
		
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
				Integer posTenantId = null;
				if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
					posTenantId = oConvertUtils.getInt(TenantContext.getTenant(), 0);;		
				}
				//查询用户职位关系表(获取租户下面的)
				//update-begin---author:wangshuai---date:2023-11-15---for:【QQYUN-7028】用户职务保存后未回显---
				List<String> positionList =  sysUserPositionMapper.getPositionIdByUserTenantId(item.getId(),posTenantId);
				//update-end---author:wangshuai---date:2023-11-15---for:【QQYUN-7028】用户职务保存后未回显---
				//update-end---author:wangshuai ---date:20230228  for：[QQYUN-4354]加入更多字段：当前加入时间应该取当前租户的/职位也是当前租户下的------------
				item.setPost(CommonUtils.getSplitText(positionList, SymbolConstant.COMMA));
				
				//update-begin---author:wangshuai---date:2023-10-08---for:【QQYUN-6668】钉钉部门和用户同步，我怎么知道哪些用户是双向绑定成功的---
				//是否根据租户隔离(敲敲云用户列表专用，用于展示是否同步钉钉)
				if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
					//查询账号表是否已同步钉钉
					LambdaQueryWrapper<SysThirdAccount> query = new LambdaQueryWrapper<>();
					query.eq(SysThirdAccount::getSysUserId,item.getId());
					query.eq(SysThirdAccount::getTenantId, tenantId);
					//目前只有同步钉钉
					query.eq(SysThirdAccount::getThirdType, MessageTypeEnum.DD.getType());
					//不为空代表已同步钉钉
					List<SysThirdAccount> account = sysThirdAccountService.list(query);
					if(CollectionUtil.isNotEmpty(account)){
						item.setIzBindThird(true);
					}
				}
				//update-end---author:wangshuai---date:2023-10-08---for:【QQYUN-6668】钉钉部门和用户同步，我怎么知道哪些用户是双向绑定成功的---
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
		//update-begin---author:wangshuai---date:2024-01-16---for:【QQYUN-7974】admin用户禁止删除---
		//1.验证当前用户是管理员账号 admin
		//验证用户是否为管理员
		this.checkUserAdminRejectDel(userId);
		//update-end---author:wangshuai---date:2024-01-16---for:【QQYUN-7974】admin用户禁止删除---
		
		//2.删除用户
		this.removeById(userId);
		return false;
	}

	@Override
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteBatchUsers(String userIds) {
		//1.验证当前用户是管理员账号 admin
		this.checkUserAdminRejectDel(userIds);
		//2.删除用户
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
	 *
	 * @param username
	 * @param version
	 * @return
	 */
	@Override
	public SysRoleIndex getDynamicIndexByUserRole(String username, String version) {
		List<String> roles = sysUserRoleMapper.getRoleByUserName(username);
		String componentUrl = RoleIndexConfigEnum.getIndexByRoles(roles);
		SysRoleIndex roleIndex = new SysRoleIndex(componentUrl);
		boolean isV3 = CommonConstant.VERSION_V3.equals(version);
		//只有 X-Version=v3 的时候，才读取sys_role_index表获取角色首页配置
		if (isV3 && CollectionUtils.isNotEmpty(roles)) {
			LambdaQueryWrapper<SysRoleIndex> routeIndexQuery = new LambdaQueryWrapper<>();
			//用户所有角色
			routeIndexQuery.in(SysRoleIndex::getRoleCode, roles);
			//角色首页状态0：未开启  1：开启
			routeIndexQuery.eq(SysRoleIndex::getStatus, CommonConstant.STATUS_1);
			//优先级正序排序
			routeIndexQuery.orderByAsc(SysRoleIndex::getPriority);
			List<SysRoleIndex> list = sysRoleIndexService.list(routeIndexQuery);
			if (CollectionUtils.isNotEmpty(list)) {
				roleIndex = list.get(0);
			}
		}

		if (oConvertUtils.isEmpty(roleIndex.getComponent())) {
			if (isV3) {
				// 如果角色没有配置首页，则使用默认首页
				return sysRoleIndexService.queryDefaultIndex();
			} else {
				// 非v3返回null
				return null;
			}
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
	 * 通过用户名获取用户角色集合
	 * @param userId 用户ID
     * @return 角色集合
	 */
	@Override
	public Set<String> getUserRoleSetById(String userId) {
		// 查询用户拥有的角色集合
		List<String> roles = sysUserRoleMapper.getRoleCodeByUserId(userId);
		log.info("-------通过数据库读取用户拥有的角色Rules------userId： " + userId + ",Roles size: " + (roles == null ? 0 : roles.size()));
		return new HashSet<>(roles);
	}

	/**
	 * 通过用户名获取用户权限集合
	 *
	 * @param userId 用户ID
	 * @return 权限集合
	 */
	@Override
	public Set<String> getUserPermissionsSet(String userId) {
		Set<String> permissionSet = new HashSet<>();
		List<SysPermission> permissionList = sysPermissionMapper.queryByUser(userId);
		//================= begin 开启租户的时候 如果没有test角色，默认加入test角色================
		if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
			if (permissionList == null) {
				permissionList = new ArrayList<>();
			}
			List<SysPermission> testRoleList = sysPermissionMapper.queryPermissionByTestRoleId();
			permissionList.addAll(testRoleList);
		}
		//================= end 开启租户的时候 如果没有test角色，默认加入test角色================
		for (SysPermission po : permissionList) {
//			// TODO URL规则有问题？
//			if (oConvertUtils.isNotEmpty(po.getUrl())) {
//				permissionSet.add(po.getUrl());
//			}
			if (oConvertUtils.isNotEmpty(po.getPerms())) {
				permissionSet.add(po.getPerms());
			}
		}
		log.info("-------通过数据库读取用户拥有的权限Perms------userId： "+ userId+",Perms size: "+ (permissionSet==null?0:permissionSet.size()) );
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
	public IPage<SysUser> getUserByDepId(Page<SysUser> page, String departId, String username) {
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
		//根据部门orgCode查询部门，需要将职位id进行传递
		for (SysUserSysDepartModel model:list) {
			List<String> positionList = sysUserPositionMapper.getPositionIdByUserId(model.getId());
			model.setPost(CommonUtils.getSplitText(positionList, SymbolConstant.COMMA));
		}
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
		//update-begin---author:wangshuai ---date:20230220  for：[QQYUN-3980]组织管理中 职位功能 职位表加租户id 加职位-用户关联表------------
		IPage<SysUser> userRoleList = userMapper.getUserByRoleId(page, roleId, username);
		List<SysUser> records = userRoleList.getRecords();
		if (null != records && records.size() > 0) {
			List<String> userIds = records.stream().map(SysUser::getId).collect(Collectors.toList());
			Map<String, String> useDepNames = this.getDepNamesByUserIds(userIds);
			for (SysUser sysUser : userRoleList.getRecords()) {
				//设置部门
				sysUser.setOrgCodeTxt(useDepNames.get(sysUser.getId()));
				//设置用户职位id
				this.userPositionId(sysUser);
			}
		}
		return userRoleList;
		//update-end---author:wangshuai ---date:20230220  for：[QQYUN-3980]组织管理中 职位功能 职位表加租户id 加职位-用户关联表------------
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
		
		//step.3 保存所属部门
		if(oConvertUtils.isNotEmpty(selectedDeparts)) {
			String[] arr = selectedDeparts.split(",");
			for (String deaprtId : arr) {
				SysUserDepart userDeaprt = new SysUserDepart(user.getId(), deaprtId);
				sysUserDepartMapper.insert(userDeaprt);
			}
		}

		//step.4 保存职位
		this.saveUserPosition(user.getId(),user.getPost());
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

		//step.5 修改职位
		this.editUserPosition(user.getId(),user.getPost());
	}

	@Override
	public List<String> userIdToUsername(Collection<String> userIdList) {
		LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.in(SysUser::getId, userIdList);
		List<SysUser> userList = super.list(queryWrapper);
		return userList.stream().map(SysUser::getUsername).collect(Collectors.toList());
	}

	@Override
	@Cacheable(cacheNames= CacheConstant.SYS_USERS_CACHE, key="#username")
	@SensitiveEncode
	public LoginUser getEncodeUserInfo(String username){
		if(oConvertUtils.isEmpty(username)) {
			return null;
		}
		LoginUser loginUser = new LoginUser();
		SysUser sysUser = userMapper.getUserByName(username);
		//查询用户的租户ids
		this.setUserTenantIds(sysUser);
		//设置职位id
		this.userPositionId(sysUser);
		if(sysUser==null) {
			return null;
		}
		BeanUtils.copyProperties(sysUser, loginUser);
		// 查询当前登录用户的部门id
		loginUser.setOrgId(this.getDepartIdByOrCode(sysUser.getOrgCode()));
		// 查询当前登录用户的角色code（多个逗号分割）
		loginUser.setRoleCode(this.getJoinRoleCodeByUserId(sysUser.getId()));
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
	public Result<JSONObject> setLoginTenant(SysUser sysUser, JSONObject obj, String username, Result<JSONObject> result){
		// update-begin--Author:sunjianlei Date:20210802 for：获取用户租户信息
		//用户有哪些租户
//		List<SysTenant> tenantList = null;
        //update-begin---author:wangshuai ---date:20221223  for：[QQYUN-3371]租户逻辑改造，改成关系表------------
		//update-begin---author:wangshuai ---date:20230427  for：【QQYUN-5270】名下租户全部退出后，再次登录出现租户冻结------------
		List<SysTenant> tenantList = relationMapper.getTenantNoCancel(sysUser.getId());
		obj.put("tenantList", tenantList);
		//update-end---author:wangshuai ---date:20230427  for：【QQYUN-5270】名下租户全部退出后，再次登录出现租户冻结------------
//		if (null!=tenantIdList && tenantIdList.size()>0) {
//			//update-end---author:wangshuai ---date:20221223  for：[QQYUN-3371]租户逻辑改造，改成关系表--------------
//			//-------------------------------------------------------------------------------------
//			//查询有效的租户集合
//			LambdaQueryWrapper<SysTenant> queryWrapper = new LambdaQueryWrapper<>();
//			queryWrapper.in(SysTenant::getId, tenantIdList);
//			queryWrapper.eq(SysTenant::getStatus, Integer.valueOf(CommonConstant.STATUS_1));
//			tenantList = sysTenantMapper.selectList(queryWrapper);
//			//-------------------------------------------------------------------------------------
//
//			if (tenantList.size() == 0) {
//				return result.error500("与该用户关联的租户均已被冻结，无法登录！");
//			} else {
//				obj.put("tenantList", tenantList);
//			}
//		}
        //update-end---author:wangshuai ---date:20221223  for：[QQYUN-3371]租户逻辑改造，改成关系表--------------
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
		if(sysUser.getLoginTenantId()!=null){
			//登录的时候需要手工设置下会话中的租户ID,不然登录接口无法通过租户隔离查询到数据
			TenantContext.setTenant(sysUser.getLoginTenantId()+"");
		}
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
                
				LambdaQueryWrapper sysUserTenantQueryWrapper = new LambdaQueryWrapper<SysUserTenant>()
						.eq(SysUserTenant::getUserId, userId)
						.eq(SysUserTenant::getTenantId,Integer.valueOf(tenantId));
				SysUserTenant tenantPresent = relationMapper.selectOne(sysUserTenantQueryWrapper);
				if (tenantPresent != null) {
					tenantPresent.setStatus(CommonConstant.STATUS_1);
					relationMapper.updateById(tenantPresent);
				}else{
					relationMapper.insert(relation);
				}
            }
        }else{
			//是否开启系统管理模块的多租户数据隔离【SAAS多租户模式】
			if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
				//update-begin---author:wangshuai ---date:20230220  for：判断当前用户是否在当前租户里面，如果不存在在新增------------
				String tenantId = TenantContext.getTenant();
				if(oConvertUtils.isNotEmpty(tenantId)){
					Integer count = relationMapper.userTenantIzExist(userId, Integer.parseInt(tenantId));
					if(count == 0){
						SysUserTenant relation = new SysUserTenant();
						relation.setUserId(userId);
						relation.setTenantId(Integer.parseInt(tenantId));
						relation.setStatus(CommonConstant.STATUS_1);
						relationMapper.insert(relation);
					}
				}
				//update-end---author:wangshuai ---date:20230220  for：判断当前用户是否在当前租户里面，如果不存在在新增------------
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
		List<Integer> oldTenantIds = relationMapper.getTenantIdsByUserId(userId);
        //如果传过来的租户id为空，那么就删除租户
        if (oConvertUtils.isEmpty(relTenantIds) && CollectionUtils.isNotEmpty(oldTenantIds)) {
            this.deleteTenantByUserId(userId, null);
        } else if (oConvertUtils.isNotEmpty(relTenantIds) && CollectionUtils.isEmpty(oldTenantIds)) {
            //如果传过来的租户id不为空但是数据库的租户id为空，那么就新增
            this.saveUserTenant(userId, relTenantIds);
        } else {
			//都不为空，需要比较，进行添加或删除
			if(oConvertUtils.isNotEmpty(relTenantIds) && CollectionUtils.isNotEmpty(oldTenantIds)){
				//找到新的租户id与原来的租户id不同之处，进行删除
				String[] relTenantIdArray = relTenantIds.split(SymbolConstant.COMMA);
				List<String> relTenantIdList = Arrays.asList(relTenantIdArray);
				
				List<Integer> deleteTenantIdList = oldTenantIds.stream().filter(item -> !relTenantIdList.contains(item.toString())).collect(Collectors.toList());
				for (Integer tenantId : deleteTenantIdList) {
					this.deleteTenantByUserId(userId, tenantId);
				}
				//找到原来租户的用户id与新的租户id不同之处，进行新增
				String tenantIds = relTenantIdList.stream().filter(item -> !oldTenantIds.contains(Integer.valueOf(item))).collect(Collectors.joining(","));
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
		//批量修改用户职位
		if(oConvertUtils.isNotEmpty(post)) {
			//update-begin---author:wangshuai ---date:20230220  for：[QQYUN-3980]组织管理中 职位功能 职位表加租户id 加职位-用户关联表------------
			//修改职位用户关系表
			for (String userId:idList) {
				this.editUserPosition(userId,post);
			}
			//update-end---author:wangshuai ---date:20230220  for：[QQYUN-3980]组织管理中 职位功能 职位表加租户id 加职位-用户关联表------------
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
			//update-begin---author:wangshuai ---date:20230303  for：部门负责人不能被删除------------
				this.removeDepartmentManager(departChargeUserIdList,departChargeUsers,departId);
			}else{
				if(CollectionUtil.isNotEmpty(departChargeUsers)){
					//前端传过来用户列表id为空，说明数据库的负责部门人员均需要删除
					this.removeDepartmentManager(departChargeUserIdList,departChargeUsers,departId);
				}
			//update-end---author:wangshuai ---date:20230303  for：部门负责人不能被删除------------
				
			}
		}
	}

	private List<SysUser> queryDepartChargePersons(String departId){
    	List<SysUser> result = new ArrayList<>();
		//update-begin---author:wangshuai ---date:20230303  for：部门负责人不能被删除------------
		LambdaQueryWrapper<SysUser> userQuery = new LambdaQueryWrapper<>();
		userQuery.like(SysUser::getDepartIds,departId);
		List<SysUser> userList = userMapper.selectList(userQuery);
		if(userList!=null && userList.size()>0){
			for(SysUser user: userList){
				Integer identity = user.getUserIdentity();
				String deps = user.getDepartIds();
				if(identity!=null && identity==2){
					if(oConvertUtils.isNotEmpty(deps)){
						if(deps.indexOf(departId)>=0){
							result.add(user);
						}
		//update-end---author:wangshuai ---date:20230303  for：部门负责人不能被删除------------
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
					//设置为上级
					user.setUserIdentity(CommonConstant.USER_IDENTITY_2);
					user.setDepartIds(departId);
				}else{
					List<String> list = new ArrayList<String>(Arrays.asList(ids.split(",")));
					if(list.indexOf(departId)>=0){
						//啥也不干
					}else{
						list.add(departId);
						String newIds = String.join(",", list);
						//设置为上级
						user.setUserIdentity(CommonConstant.USER_IDENTITY_2);
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
					}else{
						//负责部门为空时，说明已经是普通用户
						user.setUserIdentity(CommonConstant.USER_IDENTITY_1);
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
	private void getParentDepart(SysDepart depart, List<String> orgName, List<String> orgId){
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
		user.setId(sysUser.getId());
		this.updateById(user);
		//update-begin---author:wangshuai ---date:20230424  for：【QQYUN-5251】人员与部门：部门删除不掉------------
		if(oConvertUtils.isEmpty(departs)){
			//直接删除用户下的的租户部门
			sysUserDepartMapper.deleteUserDepart(user.getId(),tenantId);
		}else{
			//修改租户用户下的部门
			this.updateTenantDepart(user, tenantId, departs);
		}
		//update-end---author:wangshuai ---date:20230424  for：【QQYUN-5251】人员与部门：部门删除不掉------------
		//修改用户下的职位
		this.editUserPosition(sysUser.getId(),sysUser.getPost());
	}

	/**
	 * 修改账号状态
	 * @param id 账号id
	 * @param status 账号状态
	 */
	@Override
	@CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
	public void updateStatus(String id, String status) {
		userMapper.update(new SysUser().setStatus(Integer.parseInt(status)),
				new UpdateWrapper<SysUser>().lambda().eq(SysUser::getId,id));
	}

	/**
	 * 修改租户下的部门
	 * @param departs
	 */
	public void updateTenantDepart(SysUser user, String tenantId, String departs) {
		List<String> departList = new ArrayList<>();
		long startTime = System.currentTimeMillis();
		if (oConvertUtils.isNotEmpty(departs)) {
			//获取当前租户下的部门id,根据前台
			departList = sysUserDepartMapper.getTenantDepart(Arrays.asList(departs.split(SymbolConstant.COMMA)), tenantId);
		}
		long endTime = System.currentTimeMillis();
		System.out.println("查询用户部门用时：" + (endTime - startTime) + "ms");
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
		long endTime1 = System.currentTimeMillis();
		System.out.println("修改部门角色用时：" + (endTime1 - startTime) + "ms");
		
		if (departList.size() > 0) {
			//删除用户下的部门
			sysUserDepartMapper.deleteUserDepart(user.getId(), tenantId);
			for (String departId : departList) {
				//添加部门
				SysUserDepart userDepart = new SysUserDepart(user.getId(), departId);
				sysUserDepartMapper.insert(userDepart);
			}
		}
		long endTime2 = System.currentTimeMillis();
		System.out.println("修改用户部门用时：" + (endTime2 - startTime) + "ms");
	}

	/**
	 * 保存用户职位
	 *
	 * @param userId
	 * @param positionIds
	 */
	private void saveUserPosition(String userId, String positionIds) {
		if (oConvertUtils.isNotEmpty(positionIds)) {
			String[] positionIdArray = positionIds.split(SymbolConstant.COMMA);
			for (String postId : positionIdArray) {
				SysUserPosition userPosition = new SysUserPosition();
				userPosition.setUserId(userId);
				userPosition.setPositionId(postId);
				sysUserPositionMapper.insert(userPosition);
			}
		}
	}

	/**
	 * 编辑用户职位
	 *
	 * @param userId
	 * @param positionIds
	 */
	private void editUserPosition(String userId, String positionIds) {
		//先删除
		LambdaQueryWrapper<SysUserPosition> query = new LambdaQueryWrapper<>();
		query.eq(SysUserPosition::getUserId, userId);
		sysUserPositionMapper.delete(query);
		//后新增数据
		this.saveUserPosition(userId, positionIds);
	}

	/**
	 * 设置用户职位id(已逗号拼接起来)
	 * @param sysUser
	 */
	private void userPositionId(SysUser sysUser) {
		if(null != sysUser){
			List<String> positionList = sysUserPositionMapper.getPositionIdByUserId(sysUser.getId());
			sysUser.setPost(CommonUtils.getSplitText(positionList, SymbolConstant.COMMA));
		}
	}

	/**
	 * 查询用户当前登录部门的id
	 *
	 * @param orgCode
	 */
	private @Nullable String getDepartIdByOrCode(String orgCode) {
		if (oConvertUtils.isEmpty(orgCode)) {
			return null;
		}
		LambdaQueryWrapper<SysDepart> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(SysDepart::getOrgCode, orgCode);
		queryWrapper.select(SysDepart::getId);
		SysDepart depart = sysDepartMapper.selectOne(queryWrapper);
		if (depart == null || oConvertUtils.isEmpty(depart.getId())) {
			return null;
		}
		return depart.getId();
	}

	/**
	 * 查询用户的角色code（多个逗号分割）
	 *
	 * @param userId
	 */
	private @Nullable String getJoinRoleCodeByUserId(String userId) {
		if (oConvertUtils.isEmpty(userId)) {
			return null;
		}
		// 判断是否开启saas模式，根据租户id过滤
		Integer tenantId = null;
		if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
			// 开启了但是没有租户ID，默认-1，使其查询不到任何数据
			tenantId = oConvertUtils.getInt(TenantContext.getTenant(), -1);
		}
		List<SysRole> roleList = sysRoleMapper.getRoleCodeListByUserId(userId, tenantId);
		if (CollectionUtils.isEmpty(roleList)) {
			return null;
		}
		return roleList.stream().map(SysRole::getRoleCode).collect(Collectors.joining(SymbolConstant.COMMA));
	}

	/**
	 * 移除部门负责人
	 * @param departChargeUserIdList
	 * @param departChargeUsers
	 * @param departId
	 */
	private void removeDepartmentManager(List<String> departChargeUserIdList, List<SysUser> departChargeUsers, String departId){
		//移除部门负责人
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

	//======================================= begin 用户与部门 用户列表导出 =========================================
	@Override
	public ModelAndView exportAppUser(HttpServletRequest request) {
		Integer tenantId = oConvertUtils.getInt(TenantContext.getTenant());
		// Step.1 组装查询条件，导出选中的部门id数据
		String departIds = request.getParameter("departIds");
		List<String> list = new ArrayList<>();
		if(oConvertUtils.isNotEmpty(departIds)){
			list = Arrays.asList(departIds.split(SymbolConstant.COMMA));
		}
		//查询用户数据
		List<SysUser> userList = userMapper.getUserByDepartsTenantId(list, tenantId);
		//获取部门名称
		List<SysUserDepVo> userDepVos = sysDepartMapper.getUserDepartByTenantUserId(userList, tenantId);
		//获取职位
		List<SysUserPositionVo> positionVos = sysUserPositionMapper.getPositionIdByUsersTenantId(userList, tenantId);
		// step2 根据用户id进行分类
		//循环用户数据将数据整合导出
		List<AppExportUserVo> exportUserVoList = new ArrayList<>();
		for (SysUser sysUser : userList) {
			AppExportUserVo exportUserVo = new AppExportUserVo();
			BeanUtils.copyProperties(sysUser, exportUserVo);
			String departNames = userDepVos.stream().filter(item -> item.getUserId().equals(sysUser.getId()))
					.map(SysUserDepVo::getDepartName).collect(Collectors.joining(SymbolConstant.SEMICOLON));
			exportUserVo.setDepart(departNames);
			String posNames = positionVos.stream().filter(item -> item.getUserId().equals(sysUser.getId())).map(SysUserPositionVo::getName).collect(Collectors.joining(SymbolConstant.SEMICOLON));
			exportUserVo.setPosition(posNames);
			exportUserVoList.add(exportUserVo);
		}
		//step3 封装导出excel参数
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		//导出文件名称
		mv.addObject(NormalExcelConstants.FILE_NAME, "用户列表");
		mv.addObject(NormalExcelConstants.CLASS, AppExportUserVo.class);
		LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		ExportParams exportParams = new ExportParams("导入规则：\n" +
				"1、存在用户编号时，数据会根据用户编号进行匹配，匹配成功后只会更新职位和工号;\n" +
				"2、不存在用户编号时，支持手机号、邮箱、姓名、部们、职位、工号导入,其中手机号必填;\n" +
				"3、上下级部门用英文字符 / 连接，如 财务部/财务一部，多个部门或者职位用英文字符 ; 进行连接,如 财务部;研发部", "导出人:" + user.getRealname(), "导出信息");
		mv.addObject(NormalExcelConstants.PARAMS, exportParams);
		mv.addObject(NormalExcelConstants.DATA_LIST, exportUserVoList);
		return mv;
	}

	//======================================= end 用户与部门 用户列表导出 =========================================

	//======================================= begin 用户与部门 用户列表导入 =========================================
	@Override
	public Result<?> importAppUser(HttpServletRequest request) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		Integer tenantId = oConvertUtils.getInt(TenantContext.getTenant());
		SysTenant sysTenant = sysTenantMapper.selectById(tenantId);
		// 错误信息
		List<String> errorMessage = new ArrayList<>();
		int successLines = 0, errorLines = 0;
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile file = entity.getValue();
			ImportParams params = new ImportParams();
			params.setTitleRows(2);
			params.setHeadRows(1);
			params.setNeedSave(true);
			//存放职位的map;key为名称 value为职位id。避免多次导入和查询
			Map<String, String> positionMap = new HashMap<>();
			//存放部门的map;key为名称 value为SysDepart对象。避免多次导入和查询
			Map<String, SysDepart> departMap = new HashMap<>();
			try {
				List<AppExportUserVo> listSysUsers = ExcelImportUtil.importExcel(file.getInputStream(), AppExportUserVo.class, params);
				for (int i = 0; i < listSysUsers.size(); i++) {
					//记录现在是多少行
					int lineNumber = i + 1;
					//记录是编辑还是添加
					boolean isEdit = false;
					AppExportUserVo sysUserExcel = listSysUsers.get(i);
					String id = sysUserExcel.getId();
					String workNo = sysUserExcel.getWorkNo();
					String email = sysUserExcel.getEmail();
					String phone = sysUserExcel.getPhone();
					String realname = sysUserExcel.getRealname();
					String depart = sysUserExcel.getDepart();
					String position = sysUserExcel.getPosition();
					SysUser sysUser = new SysUser();
					//判断id是否存在，如果存在的话就是更新
					if (oConvertUtils.isNotEmpty(id)) {
						SysUser user = userMapper.selectById(id);
						if (null == user) {
							errorLines++;
							errorMessage.add("第 " + lineNumber + " 行：用户不存在，请查看编号是否已修改，忽略导入。");
							continue;
						}
						isEdit = true;
						sysUser.setId(id);
					} else {
						//处理租户中是否已存在，用户是否已存在，已存在的用户直接更新
						isEdit = false;
					}
					if (oConvertUtils.isNotEmpty(workNo)) {
						sysUser.setWorkNo(workNo);
					}
					try {
						if (isEdit) {
							userMapper.updateById(sysUser);
						} else {
							if (oConvertUtils.isEmpty(phone)) {
								errorMessage.add("第 " + lineNumber + " 行：手机号为空，忽略导入。");
								errorLines++;
								continue;
							}
							SysUser userByPhone = userMapper.getUserByPhone(phone);
							if (null != userByPhone) {
								//查看看是否已经存在此租户中，存在禁止导入，否则直接更新即可
								Integer tenantCount = userTenantMapper.userTenantIzExist(userByPhone.getId(), tenantId);
								if (tenantCount > 0) {
									errorMessage.add("第 " + lineNumber + " 行：成员已存在该组织中，如果列表中不存在，请确认该成员是否在审核中或者已离职，忽略导入。");
									errorLines++;
									continue;
								}
								sysUser.setId(userByPhone.getId());
								userMapper.updateById(sysUser);
								this.addUserTenant(sysUser.getId(), tenantId, userByPhone.getUsername(),sysTenant.getName());
							} else {
								// 密码默认为 “租户门牌号+手机号”
								String password = sysTenant.getHouseNumber()+phone;
								String salt = oConvertUtils.randomGen(8);
								sysUser.setSalt(salt);
								// 密码加密加盐
								String passwordEncode = PasswordUtil.encrypt(phone, password, salt);
								sysUser.setPassword(passwordEncode);
								sysUser.setUsername(phone);
								sysUser.setRealname(oConvertUtils.getString(realname,phone));
								sysUser.setEmail(email);
								sysUser.setPhone(phone);
								sysUser.setStatus(CommonConstant.DEL_FLAG_1);
								sysUser.setDelFlag(CommonConstant.DEL_FLAG_0);
								sysUser.setCreateTime(new Date());
								userMapper.insert(sysUser);
								this.addUserTenant(sysUser.getId(), tenantId, sysUser.getUsername(),sysTenant.getName());
							}
						}
						//新增或编辑职位
						if (oConvertUtils.isNotEmpty(position)) {
							this.addOrEditPosition(sysUser.getId(), position, isEdit, tenantId, positionMap);
						}
						//新增的时候才可以添加部门
						if (!isEdit) {
							//新增或编辑部门
							this.addOrEditDepart(sysUser.getId(), depart, tenantId, departMap);
						}
						successLines++;
					} catch (Exception e) {
						errorLines++;
						String message = e.getMessage().toLowerCase();

						// 通过索引名判断出错信息
						if (message.contains(CommonConstant.SQL_INDEX_UNIQ_SYS_USER_USERNAME)) {
							errorMessage.add("第 " + lineNumber + " 行：用户名已经存在，忽略导入。");
						} else if (message.contains(CommonConstant.SQL_INDEX_UNIQ_SYS_USER_WORK_NO)) {
							errorMessage.add("第 " + lineNumber + " 行：工号已经存在，忽略导入。");
						} else if (message.contains(CommonConstant.SQL_INDEX_UNIQ_SYS_USER_PHONE)) {
							errorMessage.add("第 " + lineNumber + " 行：手机号已经存在，忽略导入。");
						} else if (message.contains(CommonConstant.SQL_INDEX_UNIQ_SYS_USER_EMAIL)) {
							errorMessage.add("第 " + lineNumber + " 行：电子邮件已经存在，忽略导入。");
						} else if (message.contains(CommonConstant.SQL_INDEX_UNIQ_SYS_USER)) {
							errorMessage.add("第 " + lineNumber + " 行：违反表唯一性约束。");
						} else {
							errorMessage.add("第 " + lineNumber + " 行：未知错误，忽略导入");
							log.error(e.getMessage(), e);
						}
					}
				}
			} catch (Exception e) {
				errorMessage.add("发生异常：" + e.getMessage());
				log.error(e.getMessage(), e);
			} finally {
				try {
					file.getInputStream().close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		try {
			return ImportExcelUtil.imporReturnRes(errorLines, successLines, errorMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 新增或者编辑职位
	 *
	 * @param userId      用户id
	 * @param position    职位名称 已/拼接
	 * @param isEdit      新增或编辑
	 * @param positionMap 职位map key为name，value为职位id
	 */
	private void addOrEditPosition(String userId, String position, Boolean isEdit, Integer tenantId, Map<String, String> positionMap) {
		Page<SysPosition> page = new Page<>(1, 1);
		String[] positions = position.split(SymbolConstant.SEMICOLON);
		List<String> positionList = Arrays.asList(positions);
		positionList = positionList.stream().distinct().collect(Collectors.toList());
		//删除当前租户下的职位，根据职位名称、租户id、用户id
		sysUserPositionMapper.deleteUserPosByNameAndTenantId(positionList, tenantId, userId);
		//循环需要添加或修改的数据
		for (String pos : positionList) {
			String posId = "";
			if (positionMap.containsKey(pos)) {
				posId = positionMap.get(pos);
			} else {
				List<String> namePage = sysPositionMapper.getPositionIdByName(pos, tenantId, page);
				if (CollectionUtil.isNotEmpty(namePage)) {
					posId = namePage.get(0);
					positionMap.put(pos, posId);
				}
			}

			//职位id不为空直接新增
			if (oConvertUtils.isNotEmpty(posId)) {
				this.addSysUserPosition(userId, posId);
				continue;
			}

			//不是编辑的情况下职位才会新增
			if (!isEdit) {
				//新增职位和用户职位关系
				SysPosition sysPosition = new SysPosition();
				sysPosition.setName(pos);
				sysPosition.setCode(RandomUtil.randomString(10));
				sysPosition.setTenantId(tenantId);
				sysPositionMapper.insert(sysPosition);
				positionMap.put(pos, sysPosition.getId());
				this.addSysUserPosition(userId, sysPosition.getId());
			}
		}
	}

	/**
	 * 添加用户职位
	 */
	private void addSysUserPosition(String userId, String positionId) {
		Long count = sysUserPositionMapper.getUserPositionCount(userId, positionId);
		if(count == 0){
			SysUserPosition userPosition = new SysUserPosition();
			userPosition.setUserId(userId);
			userPosition.setPositionId(positionId);
			sysUserPositionMapper.insert(userPosition);
		}
	}

	/**
	 * 新增或编辑部门
	 *
	 * @param userId    用户id
	 * @param depart    部门名称
	 * @param tenantId  租户id
	 * @param departMap 存放部门的map;key为名称 value为SysDepart对象。
	 */
	private void addOrEditDepart(String userId, String depart, Integer tenantId, Map<String, SysDepart> departMap) {
		//批量将部门和用户信息建立关联关系
		if (StringUtils.isNotEmpty(depart)) {
			Page<SysDepart> page = new Page<>(1, 1);
			//多个部门分离开
			String[] departNames = depart.split(SymbolConstant.SEMICOLON);
			List<String> departNameList = Arrays.asList(departNames);
			departNameList = departNameList.stream().distinct().collect(Collectors.toList());
			//部门id
			String parentId = "";
			for (String departName : departNameList) {
				String[] names = departName.split(SymbolConstant.SINGLE_SLASH);
				//部门名称拼接
				String nameStr = "";
				for (int i = 0; i < names.length; i++) {
					String name = names[i];
					//拼接name
					if (oConvertUtils.isNotEmpty(nameStr)) {
						nameStr = nameStr + SymbolConstant.SINGLE_SLASH + name;
					} else {
						nameStr = name;
					}
					SysDepart sysDepart = null;
					//判断map中是否存在该部门名称
					if (departMap.containsKey(nameStr)) {
						sysDepart = departMap.get(nameStr);
					} else {
						//不存在需要去查询
						List<SysDepart> departPageByName = sysDepartMapper.getDepartPageByName(page, name, tenantId, parentId);
						//部门为空需要新增部门
						if (CollectionUtil.isEmpty(departPageByName)) {
							JSONObject formData = new JSONObject();
							formData.put("parentId", parentId);
							String[] codeArray = (String[]) FillRuleUtil.executeRule(FillRuleConstant.DEPART, formData);
							sysDepart = new SysDepart();
							sysDepart.setParentId(parentId);
							sysDepart.setOrgCode(codeArray[0]);
							sysDepart.setOrgType(codeArray[1]);
							sysDepart.setTenantId(tenantId);
							sysDepart.setDepartName(name);
							sysDepart.setIzLeaf(CommonConstant.IS_LEAF);
							sysDepart.setDelFlag(String.valueOf(CommonConstant.DEL_FLAG_0));
							sysDepart.setStatus(CommonConstant.STATUS_1);
							sysDepartMapper.insert(sysDepart);
						} else {
							sysDepart = departPageByName.get(0);
						}
						//父级id不为空那么就将父级部门改成不是叶子节点
						if (oConvertUtils.isNotEmpty(parentId)) {
							sysDepartMapper.setMainLeaf(parentId, CommonConstant.NOT_LEAF);
						}
						parentId = sysDepart.getId();
						departMap.put(nameStr, sysDepart);
					}
					//最后一位新增部门用户关系表
					if (i == names.length - 1) {
						Long count = sysUserDepartMapper.getCountByDepartIdAndUserId(userId, sysDepart.getId());
						if(count == 0){
							SysUserDepart userDepart = new SysUserDepart(userId, sysDepart.getId());
							sysUserDepartMapper.insert(userDepart);
						}
					}
				}
			}
		}

	}

	/**
	 * 添加用户租户
	 *
	 * @param userId
	 * @param tenantId
	 * @param invitedUsername 被邀请人的账号
	 * @param tenantName 租户名称 
	 */
	private void addUserTenant(String userId, Integer tenantId, String invitedUsername, String tenantName) {
		SysUserTenant userTenant = new SysUserTenant();
		userTenant.setTenantId(tenantId);
		userTenant.setUserId(userId);
		userTenant.setStatus(CommonConstant.USER_TENANT_INVITE);
		userTenantMapper.insert(userTenant);
		//update-begin---author:wangshuai ---date:20230710  for：【QQYUN-5731】导入用户时，没有提醒------------
		//发送系统消息通知
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		MessageDTO messageDTO = new MessageDTO();
		String title = sysUser.getRealname() + " 邀请您加入 " + tenantName + "。";
		messageDTO.setTitle(title);
		Map<String, Object> data = new HashMap<>();
		//update-begin---author:wangshuai---date:2024-03-11---for:【QQYUN-8425】用户导入成功后 消息提醒 跳转至同意页面---
		data.put(CommonConstant.NOTICE_MSG_BUS_TYPE, SysAnnmentTypeEnum.TENANT_INVITE.getType());
		//update-end---author:wangshuai---date:2024-03-11---for:【QQYUN-8425】用户导入成功后 消息提醒 跳转至同意页面---
		messageDTO.setData(data);
		messageDTO.setContent(title);
		messageDTO.setToUser(invitedUsername);
		messageDTO.setFromUser("system");
		systemSendMsgHandle.sendMessage(messageDTO);
		//update-end---author:wangshuai ---date:20230710  for：【QQYUN-5731】导入用户时，没有提醒------------
	}
	//======================================= end 用户与部门 用户列表导入 =========================================
	
	@Override
	public void checkUserAdminRejectDel(String userIds) {
		LambdaQueryWrapper<SysUser> query = new LambdaQueryWrapper<>();
		query.in(SysUser::getId,Arrays.asList(userIds.split(SymbolConstant.COMMA)));
		query.eq(SysUser::getUsername,"admin");
		Long adminRoleCount = this.baseMapper.selectCount(query);
		//大于0说明存在管理员用户，不允许删除
		if(adminRoleCount>0){
			throw new JeecgBootException("admin用户，不允许删除！");
		}
	}

	@Override
	public void changePhone(JSONObject json, String username) {
		String smscode = json.getString("smscode");
		String phone = json.getString("phone");
		String type = json.getString("type");
		if(oConvertUtils.isEmpty(phone)){
			throw new JeecgBootException("请填写原手机号！");
		}
		if(oConvertUtils.isEmpty(smscode)){
			throw new JeecgBootException("请填写验证码！");
		}
		//step1 验证原手机号是否和当前用户匹配
		SysUser sysUser = userMapper.getUserByNameAndPhone(phone,username);
		if (null == sysUser){
			throw new JeecgBootException("原手机号不匹配，无法修改密码！");
		}
		//step2 根据类型判断是验证原手机号的验证码还是新手机号的验证码
		//验证原手机号
		if(CommonConstant.VERIFY_ORIGINAL_PHONE.equals(type)){
			this.verifyPhone(phone, smscode);
		}else if(CommonConstant.UPDATE_PHONE.equals(type)){
			//修改手机号
			String newPhone = json.getString("newPhone");
			//需要验证新手机号和原手机号是否一致，一致不让修改
			if(newPhone.equals(phone)){
				throw new JeecgBootException("新手机号与原手机号一致，无法修改！");
			}
			this.verifyPhone(newPhone, smscode);
			//step3 新手机号验证码验证成功之后即可修改手机号
			sysUser.setPhone(newPhone);
			userMapper.updateById(sysUser);
		}
	}
	
	/**
	 * 验证手机号
	 *
	 * @param phone
	 * @param smsCode
	 * @return
	 */
	public void verifyPhone(String phone, String smsCode){
		String phoneKey = CommonConstant.CHANGE_PHONE_REDIS_KEY_PRE + phone;
		Object phoneCode = redisUtil.get(phoneKey);
		if(null == phoneCode){
			throw new JeecgBootException("验证码失效，请重新发送验证码！");
		}
		if(!smsCode.equals(phoneCode.toString())) {
			throw new JeecgBootException("短信验证码不匹配！");
		}
		//验证完成之后清空手机验证码
		redisUtil.removeAll(phoneKey);
	}
	
	@Override
	public void sendChangePhoneSms(JSONObject jsonObject, String username, String ipAddress) {
		String type = jsonObject.getString("type");
		String phone = jsonObject.getString("phone");
		if(oConvertUtils.isEmpty(phone)){
			throw new JeecgBootException("请填写手机号！");
		}
		//step1 根据类型判断是发送旧手机号验证码还是新的手机号验证码
		if(CommonConstant.VERIFY_ORIGINAL_PHONE.equals(type)){
			//step2 旧手机号验证码需要验证手机号是否匹配
			SysUser sysUser = userMapper.getUserByNameAndPhone(phone, username);
			if(null == sysUser){
				throw new JeecgBootException("旧手机号不匹配，无法修改手机号！");
			}
		}else if(CommonConstant.UPDATE_PHONE.equals(type)){
			//step3 新手机号需要验证手机号码是否已注册过
			SysUser userByPhone = userMapper.getUserByPhone(phone);
			if(null != userByPhone){
				throw new JeecgBootException("手机号已被注册，请尝试其他手机号！");
			}
		}
		//step4 发送短信验证码
		this.sendPhoneSms(phone, ipAddress);
	}

	/**
	 * 发送短信验证码
	 * @param phone
	 */
	private void sendPhoneSms(String phone, String clientIp) {
		String redisKey = CommonConstant.CHANGE_PHONE_REDIS_KEY_PRE+phone;
		Object object = redisUtil.get(redisKey);

		if (object != null) {
			throw new JeecgBootException("验证码10分钟内，仍然有效！");
		}

		//增加 check防止恶意刷短信接口
		if(!DySmsLimit.canSendSms(clientIp)){
			log.warn("--------[警告] IP地址:{}, 短信接口请求太多-------", clientIp);
			throw new JeecgBootException("短信接口请求太多，请稍后再试！", CommonConstant.PHONE_SMS_FAIL_CODE);
		}
		
		//随机数
		String captcha = RandomUtil.randomNumbers(6);
		JSONObject obj = new JSONObject();
		obj.put("code", captcha);
		try {
			boolean sendSmsSuccess = DySmsHelper.sendSms(phone, obj, DySmsEnum.LOGIN_TEMPLATE_CODE);
			if(!sendSmsSuccess){
				throw new JeecgBootException("短信验证码发送失败,请稍后重试！");
			}
			//验证码10分钟内有效
			redisUtil.set(redisKey, captcha, 600);
		} catch (ClientException e) {
			log.error(e.getMessage(),e);
			throw new JeecgBootException("短信接口未配置，请联系管理员！");
		}
	}
}
