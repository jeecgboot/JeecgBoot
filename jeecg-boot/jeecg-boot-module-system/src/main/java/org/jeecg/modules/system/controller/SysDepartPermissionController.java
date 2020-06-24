package org.jeecg.modules.system.controller;

import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysDepartPermission;
import org.jeecg.modules.system.entity.SysDepartRolePermission;
import org.jeecg.modules.system.entity.SysPermission;
import org.jeecg.modules.system.entity.SysPermissionDataRule;
import org.jeecg.modules.system.model.TreeModel;
import org.jeecg.modules.system.service.ISysDepartPermissionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.system.service.ISysDepartRolePermissionService;
import org.jeecg.modules.system.service.ISysPermissionDataRuleService;
import org.jeecg.modules.system.service.ISysPermissionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 部门权限表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Slf4j
@Api(tags="部门权限表")
@RestController
@RequestMapping("/sys/sysDepartPermission")
public class SysDepartPermissionController extends JeecgController<SysDepartPermission, ISysDepartPermissionService> {
	@Autowired
	private ISysDepartPermissionService sysDepartPermissionService;

	 @Autowired
	 private ISysPermissionDataRuleService sysPermissionDataRuleService;

	 @Autowired
	 private ISysPermissionService sysPermissionService;

	 @Autowired
	 private ISysDepartRolePermissionService sysDepartRolePermissionService;

	/**
	 * 分页列表查询
	 *
	 * @param sysDepartPermission
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="部门权限表-分页列表查询", notes="部门权限表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SysDepartPermission sysDepartPermission,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SysDepartPermission> queryWrapper = QueryGenerator.initQueryWrapper(sysDepartPermission, req.getParameterMap());
		Page<SysDepartPermission> page = new Page<SysDepartPermission>(pageNo, pageSize);
		IPage<SysDepartPermission> pageList = sysDepartPermissionService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param sysDepartPermission
	 * @return
	 */
	@ApiOperation(value="部门权限表-添加", notes="部门权限表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SysDepartPermission sysDepartPermission) {
		sysDepartPermissionService.save(sysDepartPermission);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param sysDepartPermission
	 * @return
	 */
	@ApiOperation(value="部门权限表-编辑", notes="部门权限表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SysDepartPermission sysDepartPermission) {
		sysDepartPermissionService.updateById(sysDepartPermission);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="部门权限表-通过id删除", notes="部门权限表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		sysDepartPermissionService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@ApiOperation(value="部门权限表-批量删除", notes="部门权限表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysDepartPermissionService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="部门权限表-通过id查询", notes="部门权限表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SysDepartPermission sysDepartPermission = sysDepartPermissionService.getById(id);
		return Result.ok(sysDepartPermission);
	}

	/**
	* 导出excel
	*
	* @param request
	* @param sysDepartPermission
	*/
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, SysDepartPermission sysDepartPermission) {
	  return super.exportXls(request, sysDepartPermission, SysDepartPermission.class, "部门权限表");
	}

	/**
	* 通过excel导入数据
	*
	* @param request
	* @param response
	* @return
	*/
	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
	  return super.importExcel(request, response, SysDepartPermission.class);
	}

	/**
	* 部门管理授权查询数据规则数据
	*/
	@GetMapping(value = "/datarule/{permissionId}/{departId}")
	public Result<?> loadDatarule(@PathVariable("permissionId") String permissionId,@PathVariable("departId") String departId) {
		List<SysPermissionDataRule> list = sysPermissionDataRuleService.getPermRuleListByPermId(permissionId);
		if(list==null || list.size()==0) {
			return Result.error("未找到权限配置信息");
		}else {
			Map<String,Object> map = new HashMap<>();
			map.put("datarule", list);
			LambdaQueryWrapper<SysDepartPermission> query = new LambdaQueryWrapper<SysDepartPermission>()
				 .eq(SysDepartPermission::getPermissionId, permissionId)
				 .eq(SysDepartPermission::getDepartId,departId);
			SysDepartPermission sysDepartPermission = sysDepartPermissionService.getOne(query);
			if(sysDepartPermission==null) {
			 //return Result.error("未找到角色菜单配置信息");
			}else {
				String drChecked = sysDepartPermission.getDataRuleIds();
				if(oConvertUtils.isNotEmpty(drChecked)) {
					map.put("drChecked", drChecked.endsWith(",")?drChecked.substring(0, drChecked.length()-1):drChecked);
				}
			}
			return Result.ok(map);
			//TODO 以后按钮权限的查询也走这个请求 无非在map中多加两个key
		}
	}

	/**
	* 保存数据规则至部门菜单关联表
	*/
	@PostMapping(value = "/datarule")
	public Result<?> saveDatarule(@RequestBody JSONObject jsonObject) {
		try {
			String permissionId = jsonObject.getString("permissionId");
			String departId = jsonObject.getString("departId");
			String dataRuleIds = jsonObject.getString("dataRuleIds");
			log.info("保存数据规则>>"+"菜单ID:"+permissionId+"部门ID:"+ departId+"数据权限ID:"+dataRuleIds);
			LambdaQueryWrapper<SysDepartPermission> query = new LambdaQueryWrapper<SysDepartPermission>()
				 .eq(SysDepartPermission::getPermissionId, permissionId)
				 .eq(SysDepartPermission::getDepartId,departId);
			SysDepartPermission sysDepartPermission = sysDepartPermissionService.getOne(query);
			if(sysDepartPermission==null) {
				return Result.error("请先保存部门菜单权限!");
			}else {
				sysDepartPermission.setDataRuleIds(dataRuleIds);
			 	this.sysDepartPermissionService.updateById(sysDepartPermission);
			}
		} catch (Exception e) {
		 	log.error("SysDepartPermissionController.saveDatarule()发生异常：" + e.getMessage(),e);
		 	return Result.error("保存失败");
		}
		return Result.ok("保存成功!");
	}

	 /**
	  * 查询角色授权
	  *
	  * @return
	  */
	 @RequestMapping(value = "/queryDeptRolePermission", method = RequestMethod.GET)
	 public Result<List<String>> queryDeptRolePermission(@RequestParam(name = "roleId", required = true) String roleId) {
		 Result<List<String>> result = new Result<>();
		 try {
			 List<SysDepartRolePermission> list = sysDepartRolePermissionService.list(new QueryWrapper<SysDepartRolePermission>().lambda().eq(SysDepartRolePermission::getRoleId, roleId));
			 result.setResult(list.stream().map(SysDepartRolePermission -> String.valueOf(SysDepartRolePermission.getPermissionId())).collect(Collectors.toList()));
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
	 @RequestMapping(value = "/saveDeptRolePermission", method = RequestMethod.POST)
	 public Result<String> saveDeptRolePermission(@RequestBody JSONObject json) {
		 long start = System.currentTimeMillis();
		 Result<String> result = new Result<>();
		 try {
			 String roleId = json.getString("roleId");
			 String permissionIds = json.getString("permissionIds");
			 String lastPermissionIds = json.getString("lastpermissionIds");
			 this.sysDepartRolePermissionService.saveDeptRolePermission(roleId, permissionIds, lastPermissionIds);
			 result.success("保存成功！");
			 log.info("======部门角色授权成功=====耗时:" + (System.currentTimeMillis() - start) + "毫秒");
		 } catch (Exception e) {
			 result.error500("授权失败！");
			 log.error(e.getMessage(), e);
		 }
		 return result;
	 }

	 /**
	  * 用户角色授权功能，查询菜单权限树
	  * @param request
	  * @return
	  */
	 @RequestMapping(value = "/queryTreeListForDeptRole", method = RequestMethod.GET)
	 public Result<Map<String,Object>> queryTreeListForDeptRole(@RequestParam(name="departId",required=true) String departId,HttpServletRequest request) {
		 Result<Map<String,Object>> result = new Result<>();
		 //全部权限ids
		 List<String> ids = new ArrayList<>();
		 try {
			 LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<SysPermission>();
			 query.eq(SysPermission::getDelFlag, CommonConstant.DEL_FLAG_0);
			 query.orderByAsc(SysPermission::getSortNo);
			 query.inSql(SysPermission::getId,"select permission_id  from sys_depart_permission where depart_id='"+departId+"'");
			 List<SysPermission> list = sysPermissionService.list(query);
			 for(SysPermission sysPer : list) {
				 ids.add(sysPer.getId());
			 }
			 List<TreeModel> treeList = new ArrayList<>();
			 getTreeModelList(treeList, list, null);
			 Map<String,Object> resMap = new HashMap<String,Object>();
			 resMap.put("treeList", treeList); //全部树节点数据
			 resMap.put("ids", ids);//全部树ids
			 result.setResult(resMap);
			 result.setSuccess(true);
		 } catch (Exception e) {
			 log.error(e.getMessage(), e);
		 }
		 return result;
	 }

	 private void getTreeModelList(List<TreeModel> treeList, List<SysPermission> metaList, TreeModel temp) {
		 for (SysPermission permission : metaList) {
			 String tempPid = permission.getParentId();
			 TreeModel tree = new TreeModel(permission.getId(), tempPid, permission.getName(),permission.getRuleFlag(), permission.isLeaf());
			 if(temp==null && oConvertUtils.isEmpty(tempPid)) {
				 treeList.add(tree);
				 if(!tree.getIsLeaf()) {
					 getTreeModelList(treeList, metaList, tree);
				 }
			 }else if(temp!=null && tempPid!=null && tempPid.equals(temp.getKey())){
				 temp.getChildren().add(tree);
				 if(!tree.getIsLeaf()) {
					 getTreeModelList(treeList, metaList, tree);
				 }
			 }

		 }
	 }

}
