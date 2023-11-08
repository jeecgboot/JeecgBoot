package org.jeecg.modules.system.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.Md5Util;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.JeecgBaseConfig;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.model.SysPermissionTree;
import org.jeecg.modules.system.model.TreeModel;
import org.jeecg.modules.system.service.*;
import org.jeecg.modules.system.util.PermissionDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单权限表 前端控制器
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
@Slf4j
@RestController
@RequestMapping("/sys/permission")
public class SysPermissionController {

	@Autowired
	private ISysPermissionService sysPermissionService;

	@Autowired
	private ISysRolePermissionService sysRolePermissionService;

	@Autowired
	private ISysPermissionDataRuleService sysPermissionDataRuleService;

	@Autowired
	private ISysDepartPermissionService sysDepartPermissionService;

	@Autowired
	private ISysUserService sysUserService;

	@Autowired
	private JeecgBaseConfig jeecgBaseConfig;

	@Autowired
    private BaseCommonService baseCommonService;

	@Autowired
	private ISysRoleIndexService sysRoleIndexService;

    /**
     * 子菜单
     */
	private static final String CHILDREN = "children";

	/**
	 * 加载数据节点
	 *
	 * @return
	 */
	//@RequiresPermissions("system:permission:list")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<List<SysPermissionTree>> list(SysPermission sysPermission, HttpServletRequest req) {
        long start = System.currentTimeMillis();
		Result<List<SysPermissionTree>> result = new Result<>();
		try {
			LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<SysPermission>();
			query.eq(SysPermission::getDelFlag, CommonConstant.DEL_FLAG_0);
			query.orderByAsc(SysPermission::getSortNo);
			
			//支持通过菜单名字，模糊查询
			if(oConvertUtils.isNotEmpty(sysPermission.getName())){
				query.like(SysPermission::getName, sysPermission.getName());
			}
			List<SysPermission> list = sysPermissionService.list(query);
			List<SysPermissionTree> treeList = new ArrayList<>();

			//如果有菜单名查询条件，则平铺数据 不做上下级
			if(oConvertUtils.isNotEmpty(sysPermission.getName())){
				if(list!=null && list.size()>0){
					treeList = list.stream().map(e -> {
						e.setLeaf(true);
						return new SysPermissionTree(e);
					}).collect(Collectors.toList());
				}
			}else{
				getTreeList(treeList, list, null);
			}
			result.setResult(treeList);
			result.setSuccess(true);
            log.info("======获取全部菜单数据=====耗时:" + (System.currentTimeMillis() - start) + "毫秒");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	/*update_begin author:wuxianquan date:20190908 for:先查询一级菜单，当用户点击展开菜单时加载子菜单 */
	/**
	 * 系统菜单列表(一级菜单)
	 *
	 * @return
	 */
	@RequestMapping(value = "/getSystemMenuList", method = RequestMethod.GET)
	public Result<List<SysPermissionTree>> getSystemMenuList() {
        long start = System.currentTimeMillis();
		Result<List<SysPermissionTree>> result = new Result<>();
		try {
			LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<SysPermission>();
			query.eq(SysPermission::getMenuType,CommonConstant.MENU_TYPE_0);
			query.eq(SysPermission::getDelFlag, CommonConstant.DEL_FLAG_0);
			query.orderByAsc(SysPermission::getSortNo);
			List<SysPermission> list = sysPermissionService.list(query);
			List<SysPermissionTree> sysPermissionTreeList = new ArrayList<SysPermissionTree>();
			for(SysPermission sysPermission : list){
				SysPermissionTree sysPermissionTree = new SysPermissionTree(sysPermission);
				sysPermissionTreeList.add(sysPermissionTree);
			}
			result.setResult(sysPermissionTreeList);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
        log.info("======获取一级菜单数据=====耗时:" + (System.currentTimeMillis() - start) + "毫秒");
		return result;
	}

	/**
	 * 查询子菜单
	 * @param parentId
	 * @return
	 */
	@RequestMapping(value = "/getSystemSubmenu", method = RequestMethod.GET)
	public Result<List<SysPermissionTree>> getSystemSubmenu(@RequestParam("parentId") String parentId){
		Result<List<SysPermissionTree>> result = new Result<>();
		try{
			LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<SysPermission>();
			query.eq(SysPermission::getParentId,parentId);
			query.eq(SysPermission::getDelFlag, CommonConstant.DEL_FLAG_0);
			query.orderByAsc(SysPermission::getSortNo);
			List<SysPermission> list = sysPermissionService.list(query);
			List<SysPermissionTree> sysPermissionTreeList = new ArrayList<SysPermissionTree>();
			for(SysPermission sysPermission : list){
				SysPermissionTree sysPermissionTree = new SysPermissionTree(sysPermission);
				sysPermissionTreeList.add(sysPermissionTree);
			}
			result.setResult(sysPermissionTreeList);
			result.setSuccess(true);
		}catch (Exception e){
			log.error(e.getMessage(), e);
		}
		return result;
	}
	/*update_end author:wuxianquan date:20190908 for:先查询一级菜单，当用户点击展开菜单时加载子菜单 */

	// update_begin author:sunjianlei date:20200108 for: 新增批量根据父ID查询子级菜单的接口 -------------
	/**
	 * 查询子菜单
	 *
	 * @param parentIds 父ID（多个采用半角逗号分割）
	 * @return 返回 key-value 的 Map
	 */
	@GetMapping("/getSystemSubmenuBatch")
	public Result getSystemSubmenuBatch(@RequestParam("parentIds") String parentIds) {
		try {
			LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<>();
			List<String> parentIdList = Arrays.asList(parentIds.split(","));
			query.in(SysPermission::getParentId, parentIdList);
			query.eq(SysPermission::getDelFlag, CommonConstant.DEL_FLAG_0);
			query.orderByAsc(SysPermission::getSortNo);
			List<SysPermission> list = sysPermissionService.list(query);
			Map<String, List<SysPermissionTree>> listMap = new HashMap(5);
			for (SysPermission item : list) {
				String pid = item.getParentId();
				if (parentIdList.contains(pid)) {
					List<SysPermissionTree> mapList = listMap.get(pid);
					if (mapList == null) {
						mapList = new ArrayList<>();
					}
					mapList.add(new SysPermissionTree(item));
					listMap.put(pid, mapList);
				}
			}
			return Result.ok(listMap);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Result.error("批量查询子菜单失败：" + e.getMessage());
		}
	}
	// update_end author:sunjianlei date:20200108 for: 新增批量根据父ID查询子级菜单的接口 -------------

//	/**
//	 * 查询用户拥有的菜单权限和按钮权限（根据用户账号）
//	 * 
//	 * @return
//	 */
//	@RequestMapping(value = "/queryByUser", method = RequestMethod.GET)
//	public Result<JSONArray> queryByUser(HttpServletRequest req) {
//		Result<JSONArray> result = new Result<>();
//		try {
//			String username = req.getParameter("username");
//			List<SysPermission> metaList = sysPermissionService.queryByUser(username);
//			JSONArray jsonArray = new JSONArray();
//			this.getPermissionJsonArray(jsonArray, metaList, null);
//			result.setResult(jsonArray);
//			result.success("查询成功");
//		} catch (Exception e) {
//			result.error500("查询失败:" + e.getMessage());
//			log.error(e.getMessage(), e);
//		}
//		return result;
//	}

	/**
	 * 查询用户拥有的菜单权限和按钮权限
	 *
	 * @return
	 */
	@RequestMapping(value = "/getUserPermissionByToken", method = RequestMethod.GET)
	//@DynamicTable(value = DynamicTableConstant.SYS_ROLE_INDEX)
	public Result<?> getUserPermissionByToken(HttpServletRequest request) {
		Result<JSONObject> result = new Result<JSONObject>();
		try {
			//直接获取当前用户不适用前端token
			LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
			if (oConvertUtils.isEmpty(loginUser)) {
				return Result.error("请登录系统！");
			}
			List<SysPermission> metaList = sysPermissionService.queryByUser(loginUser.getUsername());
			//添加首页路由
			//update-begin-author:taoyan date:20200211 for: TASK #3368 【路由缓存】首页的缓存设置有问题，需要根据后台的路由配置来实现是否缓存
			if(!PermissionDataUtil.hasIndexPage(metaList)){
				SysPermission indexMenu = sysPermissionService.list(new LambdaQueryWrapper<SysPermission>().eq(SysPermission::getName,"首页")).get(0);
				metaList.add(0,indexMenu);
			}
			//update-end-author:taoyan date:20200211 for: TASK #3368 【路由缓存】首页的缓存设置有问题，需要根据后台的路由配置来实现是否缓存

			//update-begin--Author:zyf Date:20220425  for:自定义首页地址 LOWCOD-1578
			String version = request.getHeader(CommonConstant.VERSION);
			//update-begin---author:liusq ---date:2022-06-29  for：接口返回值修改，同步修改这里的判断逻辑-----------
			SysRoleIndex roleIndex= sysUserService.getDynamicIndexByUserRole(loginUser.getUsername(),version);
			//update-end---author:liusq ---date:2022-06-29  for：接口返回值修改，同步修改这里的判断逻辑-----------
			//update-end--Author:zyf  Date:20220425  for：自定义首页地址 LOWCOD-1578

			if(roleIndex!=null){
				List<SysPermission> menus = metaList.stream().filter(sysPermission -> "首页".equals(sysPermission.getName())).collect(Collectors.toList());
				//update-begin---author:liusq ---date:2022-06-29  for：设置自定义首页地址和组件----------
				String component = roleIndex.getComponent();
				String routeUrl = roleIndex.getUrl();
				boolean route = roleIndex.isRoute();
				if(oConvertUtils.isNotEmpty(routeUrl)){
					menus.get(0).setComponent(component);
					menus.get(0).setRoute(route);
					menus.get(0).setUrl(routeUrl);
				}else{
					menus.get(0).setComponent(component);
				}
				//update-end---author:liusq ---date:2022-06-29  for：设置自定义首页地址和组件-----------
			}
			
			JSONObject json = new JSONObject();
			JSONArray menujsonArray = new JSONArray();
			this.getPermissionJsonArray(menujsonArray, metaList, null);
			//一级菜单下的子菜单全部是隐藏路由，则一级菜单不显示
			this.handleFirstLevelMenuHidden(menujsonArray);

			JSONArray authjsonArray = new JSONArray();
			this.getAuthJsonArray(authjsonArray, metaList);
			//查询所有的权限
			LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<SysPermission>();
			query.eq(SysPermission::getDelFlag, CommonConstant.DEL_FLAG_0);
			query.eq(SysPermission::getMenuType, CommonConstant.MENU_TYPE_2);
			//query.eq(SysPermission::getStatus, "1");
			List<SysPermission> allAuthList = sysPermissionService.list(query);
			JSONArray allauthjsonArray = new JSONArray();
			this.getAllAuthJsonArray(allauthjsonArray, allAuthList);
			//路由菜单
			json.put("menu", menujsonArray);
			//按钮权限（用户拥有的权限集合）
			json.put("auth", authjsonArray);
			//全部权限配置集合（按钮权限，访问权限）
			json.put("allAuth", allauthjsonArray);
			//数据源安全模式
			json.put("sysSafeMode", jeecgBaseConfig.getFirewall()!=null? jeecgBaseConfig.getFirewall().getDataSourceSafe(): false);
			result.setResult(json);
		} catch (Exception e) {
			result.error500("查询失败:" + e.getMessage());  
			log.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 【vue3专用】获取
	 * 1、查询用户拥有的按钮/表单访问权限
	 * 2、所有权限 (菜单权限配置)
	 * 3、系统安全模式 (开启则online报表的数据源必填)
	 */
	@RequestMapping(value = "/getPermCode", method = RequestMethod.GET)
	public Result<?> getPermCode() {
		try {
			// 直接获取当前用户
			LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
			if (oConvertUtils.isEmpty(loginUser)) {
				return Result.error("请登录系统！");
			}
			// 获取当前用户的权限集合
			List<SysPermission> metaList = sysPermissionService.queryByUser(loginUser.getUsername());
            // 按钮权限（用户拥有的权限集合）
            List<String> codeList = metaList.stream()
                    .filter((permission) -> CommonConstant.MENU_TYPE_2.equals(permission.getMenuType()) && CommonConstant.STATUS_1.equals(permission.getStatus()))
                    .collect(ArrayList::new, (list, permission) -> list.add(permission.getPerms()), ArrayList::addAll);
            //
			JSONArray authArray = new JSONArray();
			this.getAuthJsonArray(authArray, metaList);
			// 查询所有的权限
			LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<>();
			query.eq(SysPermission::getDelFlag, CommonConstant.DEL_FLAG_0);
			query.eq(SysPermission::getMenuType, CommonConstant.MENU_TYPE_2);
			List<SysPermission> allAuthList = sysPermissionService.list(query);
			JSONArray allAuthArray = new JSONArray();
			this.getAllAuthJsonArray(allAuthArray, allAuthList);
			JSONObject result = new JSONObject();
            // 所拥有的权限编码
			result.put("codeList", codeList);
			//按钮权限（用户拥有的权限集合）
			result.put("auth", authArray);
			//全部权限配置集合（按钮权限，访问权限）
			result.put("allAuth", allAuthArray);
            //数据源安全模式
			result.put("sysSafeMode", jeecgBaseConfig.getFirewall()!=null? jeecgBaseConfig.getFirewall().getDataSourceSafe(): null);
            return Result.OK(result);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
            return Result.error("查询失败:" + e.getMessage());
		}
	}

	/**
	  * 添加菜单
	 * @param permission
	 * @return
	 */
    @RequiresPermissions("system:permission:add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Result<SysPermission> add(@RequestBody SysPermission permission) {
		Result<SysPermission> result = new Result<SysPermission>();
		try {
			permission = PermissionDataUtil.intelligentProcessData(permission);
			sysPermissionService.addPermission(permission);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	  * 编辑菜单
	 * @param permission
	 * @return
	 */
    @RequiresPermissions("system:permission:edit")
	@RequestMapping(value = "/edit", method = { RequestMethod.PUT, RequestMethod.POST })
	public Result<SysPermission> edit(@RequestBody SysPermission permission) {
		Result<SysPermission> result = new Result<>();
		try {
			permission = PermissionDataUtil.intelligentProcessData(permission);
			sysPermissionService.editPermission(permission);
			result.success("修改成功！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	 * 检测菜单路径是否存在
	 * @param id
	 * @param url
	 * @return
	 */
	@RequestMapping(value = "/checkPermDuplication", method = RequestMethod.GET)
	public Result<String> checkPermDuplication(@RequestParam(name = "id", required = false) String id,@RequestParam(name = "url") String url,@RequestParam(name = "alwaysShow") Boolean alwaysShow) {
		Result<String> result = new Result<>();
		try {
			boolean check=sysPermissionService.checkPermDuplication(id,url,alwaysShow);
			if(check){
				return Result.ok("该值可用！");
			}
			return Result.error("访问路径不允许重复，请重定义！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	  * 删除菜单
	 * @param id
	 * @return
	 */
    @RequiresPermissions("system:permission:delete")
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public Result<SysPermission> delete(@RequestParam(name = "id", required = true) String id) {
		Result<SysPermission> result = new Result<>();
		try {
			sysPermissionService.deletePermission(id);
			result.success("删除成功!");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500(e.getMessage());
		}
		return result;
	}

	/**
	  * 批量删除菜单
	 * @param ids
	 * @return
	 */
    @RequiresPermissions("system:permission:deleteBatch")
	@RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
	public Result<SysPermission> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		Result<SysPermission> result = new Result<>();
		try {
            String[] arr = ids.split(",");
			for (String id : arr) {
				if (oConvertUtils.isNotEmpty(id)) {
					try {
						sysPermissionService.deletePermission(id);
					} catch (JeecgBootException e) {
						if(e.getMessage()!=null && e.getMessage().contains("未找到菜单信息")){
							log.warn(e.getMessage());
						}else{
							throw e;
						}
					}
				}
			}
			result.success("删除成功!");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("删除失败!");
		}
		return result;
	}

	/**
	 * 获取全部的权限树
	 *
	 * @return
	 */
	@RequestMapping(value = "/queryTreeList", method = RequestMethod.GET)
	public Result<Map<String, Object>> queryTreeList() {
		Result<Map<String, Object>> result = new Result<>();
		// 全部权限ids
		List<String> ids = new ArrayList<>();
		try {
			LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<SysPermission>();
			query.eq(SysPermission::getDelFlag, CommonConstant.DEL_FLAG_0);
			query.orderByAsc(SysPermission::getSortNo);
			List<SysPermission> list = sysPermissionService.list(query);
			for (SysPermission sysPer : list) {
				ids.add(sysPer.getId());
			}
			List<TreeModel> treeList = new ArrayList<>();
			getTreeModelList(treeList, list, null);

			Map<String, Object> resMap = new HashMap<String, Object>(5);
            // 全部树节点数据
			resMap.put("treeList", treeList);
            // 全部树ids
			resMap.put("ids", ids);
			result.setResult(resMap);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 异步加载数据节点 [接口是废的,没有用到]
	 *
	 * @return
	 */
	@RequestMapping(value = "/queryListAsync", method = RequestMethod.GET)
	public Result<List<TreeModel>> queryAsync(@RequestParam(name = "pid", required = false) String parentId) {
		Result<List<TreeModel>> result = new Result<>();
		try {
			List<TreeModel> list = sysPermissionService.queryListByParentId(parentId);
			if (list == null || list.size() <= 0) {
				result.error500("未找到角色信息");
			} else {
				result.setResult(list);
				result.setSuccess(true);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return result;
	}

	/**
	 * 查询角色授权
	 *
	 * @return
	 */
	@RequestMapping(value = "/queryRolePermission", method = RequestMethod.GET)
	public Result<List<String>> queryRolePermission(@RequestParam(name = "roleId", required = true) String roleId) {
		Result<List<String>> result = new Result<>();
		try {
			List<SysRolePermission> list = sysRolePermissionService.list(new QueryWrapper<SysRolePermission>().lambda().eq(SysRolePermission::getRoleId, roleId));
			result.setResult(list.stream().map(sysRolePermission -> String.valueOf(sysRolePermission.getPermissionId())).collect(Collectors.toList()));
			result.setSuccess(true);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 保存角色授权
	 *
	 * @return
	 */
	@RequestMapping(value = "/saveRolePermission", method = RequestMethod.POST)
    @RequiresPermissions("system:permission:saveRole")
	public Result<String> saveRolePermission(@RequestBody JSONObject json) {
		long start = System.currentTimeMillis();
		Result<String> result = new Result<>();
		try {
			String roleId = json.getString("roleId");
			String permissionIds = json.getString("permissionIds");
			String lastPermissionIds = json.getString("lastpermissionIds");
			this.sysRolePermissionService.saveRolePermission(roleId, permissionIds, lastPermissionIds);
			//update-begin---author:wangshuai ---date:20220316  for：[VUEN-234]用户管理角色授权添加敏感日志------------
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
			baseCommonService.addLog("修改角色ID: "+roleId+" 的权限配置，操作人： " +loginUser.getUsername() ,CommonConstant.LOG_TYPE_2, 2);
            //update-end---author:wangshuai ---date:20220316  for：[VUEN-234]用户管理角色授权添加敏感日志------------
			result.success("保存成功！");
			log.info("======角色授权成功=====耗时:" + (System.currentTimeMillis() - start) + "毫秒");
		} catch (Exception e) {
			result.error500("授权失败！");
			log.error(e.getMessage(), e);
		}
		return result;
	}

	private void getTreeList(List<SysPermissionTree> treeList, List<SysPermission> metaList, SysPermissionTree temp) {
		for (SysPermission permission : metaList) {
			String tempPid = permission.getParentId();
			SysPermissionTree tree = new SysPermissionTree(permission);
			if (temp == null && oConvertUtils.isEmpty(tempPid)) {
				treeList.add(tree);
				if (!tree.getIsLeaf()) {
					getTreeList(treeList, metaList, tree);
				}
			} else if (temp != null && tempPid != null && tempPid.equals(temp.getId())) {
				temp.getChildren().add(tree);
				if (!tree.getIsLeaf()) {
					getTreeList(treeList, metaList, tree);
				}
			}

		}
	}

	private void getTreeModelList(List<TreeModel> treeList, List<SysPermission> metaList, TreeModel temp) {
		for (SysPermission permission : metaList) {
			String tempPid = permission.getParentId();
			TreeModel tree = new TreeModel(permission);
			if (temp == null && oConvertUtils.isEmpty(tempPid)) {
				treeList.add(tree);
				if (!tree.getIsLeaf()) {
					getTreeModelList(treeList, metaList, tree);
				}
			} else if (temp != null && tempPid != null && tempPid.equals(temp.getKey())) {
				temp.getChildren().add(tree);
				if (!tree.getIsLeaf()) {
					getTreeModelList(treeList, metaList, tree);
				}
			}

		}
	}

	/**
	 * 一级菜单的子菜单全部是隐藏路由，则一级菜单不显示
	 * @param jsonArray
	 */
	private void handleFirstLevelMenuHidden(JSONArray jsonArray) {
		jsonArray = jsonArray.stream().map(obj -> {
			JSONObject returnObj = new JSONObject();
			JSONObject jsonObj = (JSONObject)obj;
			if(jsonObj.containsKey(CHILDREN)){
				JSONArray childrens = jsonObj.getJSONArray(CHILDREN);
                childrens = childrens.stream().filter(arrObj -> !"true".equals(((JSONObject) arrObj).getString("hidden"))).collect(Collectors.toCollection(JSONArray::new));
                if(childrens==null || childrens.size()==0){
                    jsonObj.put("hidden",true);

                    //vue3版本兼容代码
                    JSONObject meta = new JSONObject();
                    meta.put("hideMenu",true);
                    jsonObj.put("meta", meta);
                }
			}
			return returnObj;
		}).collect(Collectors.toCollection(JSONArray::new));
	}


	/**
	  *  获取权限JSON数组
	 * @param jsonArray
	 * @param allList
	 */
	private void getAllAuthJsonArray(JSONArray jsonArray,List<SysPermission> allList) {
		JSONObject json = null;
		for (SysPermission permission : allList) {
			json = new JSONObject();
			json.put("action", permission.getPerms());
			json.put("status", permission.getStatus());
			//1显示2禁用
			json.put("type", permission.getPermsType());
			json.put("describe", permission.getName());
			jsonArray.add(json);
		}
	}

	/**
	  *  获取权限JSON数组
	 * @param jsonArray
	 * @param metaList
	 */
	private void getAuthJsonArray(JSONArray jsonArray,List<SysPermission> metaList) {
		for (SysPermission permission : metaList) {
			if(permission.getMenuType()==null) {
				continue;
			}
			JSONObject json = null;
			if(permission.getMenuType().equals(CommonConstant.MENU_TYPE_2) &&CommonConstant.STATUS_1.equals(permission.getStatus())) {
				json = new JSONObject();
				json.put("action", permission.getPerms());
				json.put("type", permission.getPermsType());
				json.put("describe", permission.getName());
				jsonArray.add(json);
			}
		}
	}
	/**
	  *  获取菜单JSON数组
	 * @param jsonArray
	 * @param metaList
	 * @param parentJson
	 */
	private void getPermissionJsonArray(JSONArray jsonArray, List<SysPermission> metaList, JSONObject parentJson) {
		for (SysPermission permission : metaList) {
			if (permission.getMenuType() == null) {
				continue;
			}
			String tempPid = permission.getParentId();
			JSONObject json = getPermissionJsonObject(permission);
			if(json==null) {
				continue;
			}
			if (parentJson == null && oConvertUtils.isEmpty(tempPid)) {
				jsonArray.add(json);
				if (!permission.isLeaf()) {
					getPermissionJsonArray(jsonArray, metaList, json);
				}
			} else if (parentJson != null && oConvertUtils.isNotEmpty(tempPid) && tempPid.equals(parentJson.getString("id"))) {
				// 类型( 0：一级菜单 1：子菜单 2：按钮 )
				if (permission.getMenuType().equals(CommonConstant.MENU_TYPE_2)) {
					JSONObject metaJson = parentJson.getJSONObject("meta");
					if (metaJson.containsKey("permissionList")) {
						metaJson.getJSONArray("permissionList").add(json);
					} else {
						JSONArray permissionList = new JSONArray();
						permissionList.add(json);
						metaJson.put("permissionList", permissionList);
					}
					// 类型( 0：一级菜单 1：子菜单 2：按钮 )
				} else if (permission.getMenuType().equals(CommonConstant.MENU_TYPE_1) || permission.getMenuType().equals(CommonConstant.MENU_TYPE_0)) {
					if (parentJson.containsKey("children")) {
						parentJson.getJSONArray("children").add(json);
					} else {
						JSONArray children = new JSONArray();
						children.add(json);
						parentJson.put("children", children);
					}

					if (!permission.isLeaf()) {
						getPermissionJsonArray(jsonArray, metaList, json);
					}
				}
			}

		}
	}

	/**
	 * 根据菜单配置生成路由json
	 * @param permission
	 * @return
	 */
		private JSONObject getPermissionJsonObject(SysPermission permission) {
		JSONObject json = new JSONObject();
		// 类型(0：一级菜单 1：子菜单 2：按钮)
		if (permission.getMenuType().equals(CommonConstant.MENU_TYPE_2)) {
			//json.put("action", permission.getPerms());
			//json.put("type", permission.getPermsType());
			//json.put("describe", permission.getName());
			return null;
		} else if (permission.getMenuType().equals(CommonConstant.MENU_TYPE_0) || permission.getMenuType().equals(CommonConstant.MENU_TYPE_1)) {
			json.put("id", permission.getId());
			if (permission.isRoute()) {
                //表示生成路由
				json.put("route", "1");
			} else {
                //表示不生成路由
				json.put("route", "0");
			}

			if (isWwwHttpUrl(permission.getUrl())) {
				json.put("path", Md5Util.md5Encode(permission.getUrl(), "utf-8"));
			} else {
				json.put("path", permission.getUrl());
			}

			// 重要规则：路由name (通过URL生成路由name,路由name供前端开发，页面跳转使用)
			if (oConvertUtils.isNotEmpty(permission.getComponentName())) {
				json.put("name", permission.getComponentName());
			} else {
				json.put("name", urlToRouteName(permission.getUrl()));
			}

			JSONObject meta = new JSONObject();
			// 是否隐藏路由，默认都是显示的
			if (permission.isHidden()) {
				json.put("hidden", true);
                //vue3版本兼容代码
                meta.put("hideMenu",true);
			}
			// 聚合路由
			if (permission.isAlwaysShow()) {
				json.put("alwaysShow", true);
			}
			json.put("component", permission.getComponent());
			// 由用户设置是否缓存页面 用布尔值
			if (permission.isKeepAlive()) {
				meta.put("keepAlive", true);
			} else {
				meta.put("keepAlive", false);
			}

			/*update_begin author:wuxianquan date:20190908 for:往菜单信息里添加外链菜单打开方式 */
			//外链菜单打开方式
			if (permission.isInternalOrExternal()) {
				meta.put("internalOrExternal", true);
			} else {
				meta.put("internalOrExternal", false);
			}
			/* update_end author:wuxianquan date:20190908 for: 往菜单信息里添加外链菜单打开方式*/

			meta.put("title", permission.getName());

			//update-begin--Author:scott  Date:20201015 for：路由缓存问题，关闭了tab页时再打开就不刷新 #842
			String component = permission.getComponent();
			if(oConvertUtils.isNotEmpty(permission.getComponentName()) || oConvertUtils.isNotEmpty(component)){
				meta.put("componentName", oConvertUtils.getString(permission.getComponentName(),component.substring(component.lastIndexOf("/")+1)));
			}
			//update-end--Author:scott  Date:20201015 for：路由缓存问题，关闭了tab页时再打开就不刷新 #842

			if (oConvertUtils.isEmpty(permission.getParentId())) {
				// 一级菜单跳转地址
				json.put("redirect", permission.getRedirect());
				if (oConvertUtils.isNotEmpty(permission.getIcon())) {
					meta.put("icon", permission.getIcon());
				}
			} else {
				if (oConvertUtils.isNotEmpty(permission.getIcon())) {
					meta.put("icon", permission.getIcon());
				}
			}
			if (isWwwHttpUrl(permission.getUrl())) {
				meta.put("url", permission.getUrl());
			}
			// update-begin--Author:sunjianlei  Date:20210918 for：新增适配vue3项目的隐藏tab功能
			if (permission.isHideTab()) {
				meta.put("hideTab", true);
			}
			// update-end--Author:sunjianlei  Date:20210918 for：新增适配vue3项目的隐藏tab功能
			json.put("meta", meta);
		}

		return json;
	}

	/**
	 * 判断是否外网URL 例如： http://localhost:8080/jeecg-boot/swagger-ui.html#/ 支持特殊格式： {{
	 * window._CONFIG['domianURL'] }}/druid/ {{ JS代码片段 }}，前台解析会自动执行JS代码片段
	 *
	 * @return
	 */
	private boolean isWwwHttpUrl(String url) {
        boolean flag = url != null && (url.startsWith(CommonConstant.HTTP_PROTOCOL) || url.startsWith(CommonConstant.HTTPS_PROTOCOL) || url.startsWith(SymbolConstant.DOUBLE_LEFT_CURLY_BRACKET));
        if (flag) {
			return true;
		}
		return false;
	}

	/**
	 * 通过URL生成路由name（去掉URL前缀斜杠，替换内容中的斜杠‘/’为-） 举例： URL = /isystem/role RouteName =
	 * isystem-role
	 *
	 * @return
	 */
	private String urlToRouteName(String url) {
		if (oConvertUtils.isNotEmpty(url)) {
			if (url.startsWith(SymbolConstant.SINGLE_SLASH)) {
				url = url.substring(1);
			}
			url = url.replace("/", "-");

			// 特殊标记
			url = url.replace(":", "@");
			return url;
		} else {
			return null;
		}
	}

	/**
	 * 根据菜单id来获取其对应的权限数据
	 *
	 * @param sysPermissionDataRule
	 * @return
	 */
	@RequestMapping(value = "/getPermRuleListByPermId", method = RequestMethod.GET)
	public Result<List<SysPermissionDataRule>> getPermRuleListByPermId(SysPermissionDataRule sysPermissionDataRule) {
		List<SysPermissionDataRule> permRuleList = sysPermissionDataRuleService.getPermRuleListByPermId(sysPermissionDataRule.getPermissionId());
		Result<List<SysPermissionDataRule>> result = new Result<>();
		result.setSuccess(true);
		result.setResult(permRuleList);
		return result;
	}

	/**
	 * 添加菜单权限数据
	 *
	 * @param sysPermissionDataRule
	 * @return
	 */
    @RequiresPermissions("system:permission:addRule")
	@RequestMapping(value = "/addPermissionRule", method = RequestMethod.POST)
	public Result<SysPermissionDataRule> addPermissionRule(@RequestBody SysPermissionDataRule sysPermissionDataRule) {
		Result<SysPermissionDataRule> result = new Result<SysPermissionDataRule>();
		try {
			sysPermissionDataRule.setCreateTime(new Date());
			sysPermissionDataRuleService.savePermissionDataRule(sysPermissionDataRule);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}

    @RequiresPermissions("system:permission:editRule")
	@RequestMapping(value = "/editPermissionRule", method = { RequestMethod.PUT, RequestMethod.POST })
	public Result<SysPermissionDataRule> editPermissionRule(@RequestBody SysPermissionDataRule sysPermissionDataRule) {
		Result<SysPermissionDataRule> result = new Result<SysPermissionDataRule>();
		try {
			sysPermissionDataRuleService.saveOrUpdate(sysPermissionDataRule);
			result.success("更新成功！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	 * 删除菜单权限数据
	 *
	 * @param id
	 * @return
	 */
    @RequiresPermissions("system:permission:deleteRule")
	@RequestMapping(value = "/deletePermissionRule", method = RequestMethod.DELETE)
	public Result<SysPermissionDataRule> deletePermissionRule(@RequestParam(name = "id", required = true) String id) {
		Result<SysPermissionDataRule> result = new Result<SysPermissionDataRule>();
		try {
			sysPermissionDataRuleService.deletePermissionDataRule(id);
			result.success("删除成功！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	 * 查询菜单权限数据
	 *
	 * @param sysPermissionDataRule
	 * @return
	 */
	@RequestMapping(value = "/queryPermissionRule", method = RequestMethod.GET)
	public Result<List<SysPermissionDataRule>> queryPermissionRule(SysPermissionDataRule sysPermissionDataRule) {
		Result<List<SysPermissionDataRule>> result = new Result<>();
		try {
			List<SysPermissionDataRule> permRuleList = sysPermissionDataRuleService.queryPermissionRule(sysPermissionDataRule);
			result.setResult(permRuleList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	 * 部门权限表
	 * @param departId
	 * @return
	 */
	@RequestMapping(value = "/queryDepartPermission", method = RequestMethod.GET)
	public Result<List<String>> queryDepartPermission(@RequestParam(name = "departId", required = true) String departId) {
		Result<List<String>> result = new Result<>();
		try {
			List<SysDepartPermission> list = sysDepartPermissionService.list(new QueryWrapper<SysDepartPermission>().lambda().eq(SysDepartPermission::getDepartId, departId));
			result.setResult(list.stream().map(sysDepartPermission -> String.valueOf(sysDepartPermission.getPermissionId())).collect(Collectors.toList()));
			result.setSuccess(true);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 保存部门授权
	 *
	 * @return
	 */
	@RequestMapping(value = "/saveDepartPermission", method = RequestMethod.POST)
    @RequiresPermissions("system:permission:saveDepart")
	public Result<String> saveDepartPermission(@RequestBody JSONObject json) {
		long start = System.currentTimeMillis();
		Result<String> result = new Result<>();
		try {
			String departId = json.getString("departId");
			String permissionIds = json.getString("permissionIds");
			String lastPermissionIds = json.getString("lastpermissionIds");
			this.sysDepartPermissionService.saveDepartPermission(departId, permissionIds, lastPermissionIds);
			result.success("保存成功！");
			log.info("======部门授权成功=====耗时:" + (System.currentTimeMillis() - start) + "毫秒");
		} catch (Exception e) {
			result.error500("授权失败！");
			log.error(e.getMessage(), e);
		}
		return result;
	}

}
