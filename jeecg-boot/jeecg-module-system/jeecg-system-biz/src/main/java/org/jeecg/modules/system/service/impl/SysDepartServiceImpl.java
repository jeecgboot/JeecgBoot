package org.jeecg.modules.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.FillRuleConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.FillRuleUtil;
import org.jeecg.common.util.ImportExcelUtil;
import org.jeecg.common.util.YouBianCodeUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.mapper.*;
import org.jeecg.modules.system.model.DepartIdModel;
import org.jeecg.modules.system.model.SysDepartTreeModel;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.util.FindsDepartsChildrenUtil;
import org.jeecg.modules.system.vo.SysDepartExportVo;
import org.jeecg.modules.system.vo.lowapp.ExportDepartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门表 服务实现类
 * <p>
 * 
 * @Author Steve
 * @Since 2019-01-22
 */
@Service
public class SysDepartServiceImpl extends ServiceImpl<SysDepartMapper, SysDepart> implements ISysDepartService {

	@Autowired
	private SysUserDepartMapper userDepartMapper;
	@Autowired
	private SysDepartRoleMapper sysDepartRoleMapper;
	@Autowired
	private SysDepartPermissionMapper departPermissionMapper;
	@Autowired
	private SysDepartRolePermissionMapper departRolePermissionMapper;
	@Autowired
	private SysDepartRoleUserMapper departRoleUserMapper;
	@Autowired
	private SysUserMapper sysUserMapper;
	@Autowired
	private SysDepartMapper departMapper;

	@Override
	public List<SysDepartTreeModel> queryMyDeptTreeList(String departIds) {
		//根据部门id获取所负责部门
		LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
		String[] codeArr = this.getMyDeptParentOrgCode(departIds);
		//update-begin---author:wangshuai---date:2023-12-01---for:【QQYUN-7320】查询部门没数据，导致报错空指针---
		if(ArrayUtil.isEmpty(codeArr)){
			return null;
		}
		//update-end---author:wangshuai---date:2023-12-01---for:【QQYUN-7320】查询部门没数据，导致报错空指针---
		for(int i=0;i<codeArr.length;i++){
			query.or().likeRight(SysDepart::getOrgCode,codeArr[i]);
		}
		query.eq(SysDepart::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
		
		//------------------------------------------------------------------------------------------------
		//是否开启系统管理模块的 SASS 控制
		if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL){
			query.eq(SysDepart::getTenantId, oConvertUtils.getInt(TenantContext.getTenant(), 0));
		}
		//------------------------------------------------------------------------------------------------
		
		query.orderByAsc(SysDepart::getDepartOrder);
		//将父节点ParentId设为null
		List<SysDepart> listDepts = this.list(query);
		for(int i=0;i<codeArr.length;i++){
			for(SysDepart dept : listDepts){
				if(dept.getOrgCode().equals(codeArr[i])){
					dept.setParentId(null);
				}
			}
		}
		// 调用wrapTreeDataToTreeList方法生成树状数据
		List<SysDepartTreeModel> listResult = FindsDepartsChildrenUtil.wrapTreeDataToTreeList(listDepts);
		return listResult;
	}

	/**
	 * queryTreeList 对应 queryTreeList 查询所有的部门数据,以树结构形式响应给前端
	 */
	@Override
	//@Cacheable(value = CacheConstant.SYS_DEPARTS_CACHE)
	public List<SysDepartTreeModel> queryTreeList() {
		LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
		//------------------------------------------------------------------------------------------------
		//是否开启系统管理模块的多租户数据隔离【SAAS多租户模式】
		if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL){
			query.eq(SysDepart::getTenantId, oConvertUtils.getInt(TenantContext.getTenant(), 0));
		}   
		//------------------------------------------------------------------------------------------------
		query.eq(SysDepart::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
		query.orderByAsc(SysDepart::getDepartOrder);
		List<SysDepart> list = this.list(query);
        //update-begin---author:wangshuai ---date:20220307  for：[JTC-119]在部门管理菜单下设置部门负责人 创建用户的时候不需要处理
		//设置用户id,让前台显示
        this.setUserIdsByDepList(list);
        //update-begin---author:wangshuai ---date:20220307  for：[JTC-119]在部门管理菜单下设置部门负责人 创建用户的时候不需要处理
		// 调用wrapTreeDataToTreeList方法生成树状数据
		List<SysDepartTreeModel> listResult = FindsDepartsChildrenUtil.wrapTreeDataToTreeList(list);
		return listResult;
	}

	/**
	 * queryTreeList 根据部门id查询,前端回显调用
	 */
	@Override
	public List<SysDepartTreeModel> queryTreeList(String ids) {
		List<SysDepartTreeModel> listResult=new ArrayList<>();
		LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
		query.eq(SysDepart::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
		if(oConvertUtils.isNotEmpty(ids)){
			query.in(true,SysDepart::getId,ids.split(","));
		}
		//------------------------------------------------------------------------------------------------
		//是否开启系统管理模块的多租户数据隔离【SAAS多租户模式】
		if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL){
			query.eq(SysDepart::getTenantId, oConvertUtils.getInt(TenantContext.getTenant(), 0));
		}
		//------------------------------------------------------------------------------------------------
		query.orderByAsc(SysDepart::getDepartOrder);
		List<SysDepart> list= this.list(query);
		for (SysDepart depart : list) {
			listResult.add(new SysDepartTreeModel(depart));
		}
		return  listResult;

	}

	//@Cacheable(value = CacheConstant.SYS_DEPART_IDS_CACHE)
	@Override
	public List<DepartIdModel> queryDepartIdTreeList() {
		LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
		query.eq(SysDepart::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
		//------------------------------------------------------------------------------------------------
		//是否开启系统管理模块的多租户数据隔离【SAAS多租户模式】
		if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL){
			query.eq(SysDepart::getTenantId, oConvertUtils.getInt(TenantContext.getTenant(), 0));
		}
		//------------------------------------------------------------------------------------------------
		query.orderByAsc(SysDepart::getDepartOrder);
		List<SysDepart> list = this.list(query);
		// 调用wrapTreeDataToTreeList方法生成树状数据
		List<DepartIdModel> listResult = FindsDepartsChildrenUtil.wrapTreeDataToDepartIdTreeList(list);
		return listResult;
	}

	/**
	 * saveDepartData 对应 add 保存用户在页面添加的新的部门对象数据
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveDepartData(SysDepart sysDepart, String username) {
		if (sysDepart != null && username != null) {
			//update-begin---author:wangshuai ---date:20230216  for：[QQYUN-4163]给部门表加个是否有子节点------------
			if (oConvertUtils.isEmpty(sysDepart.getParentId())) {
				sysDepart.setParentId("");
			}else{
				//将父部门的设成不是叶子结点
				departMapper.setMainLeaf(sysDepart.getParentId(),CommonConstant.NOT_LEAF);
			}
			//update-end---author:wangshuai ---date:20230216  for：[QQYUN-4163]给部门表加个是否有子节点------------
			//String s = UUID.randomUUID().toString().replace("-", "");
			sysDepart.setId(IdWorker.getIdStr(sysDepart));
			// 先判断该对象有无父级ID,有则意味着不是最高级,否则意味着是最高级
			// 获取父级ID
			String parentId = sysDepart.getParentId();
			//update-begin--Author:baihailong  Date:20191209 for：部门编码规则生成器做成公用配置
			JSONObject formData = new JSONObject();
			formData.put("parentId",parentId);
			String[] codeArray = (String[]) FillRuleUtil.executeRule(FillRuleConstant.DEPART,formData);
			//update-end--Author:baihailong  Date:20191209 for：部门编码规则生成器做成公用配置
			sysDepart.setOrgCode(codeArray[0]);
			String orgType = codeArray[1];
			sysDepart.setOrgType(String.valueOf(orgType));
			sysDepart.setCreateTime(new Date());
			sysDepart.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
			//新添加的部门是叶子节点
			sysDepart.setIzLeaf(CommonConstant.IS_LEAF);
			// 【QQYUN-7172】数据库默认值兼容
			if (oConvertUtils.isEmpty(sysDepart.getOrgCategory())) {
				if (oConvertUtils.isEmpty(sysDepart.getParentId())) {
					sysDepart.setOrgCategory("1");
				} else {
					sysDepart.setOrgCategory("2");
				}
			}
			this.save(sysDepart);
            //update-begin---author:wangshuai ---date:20220307  for：[JTC-119]在部门管理菜单下设置部门负责人 创建用户的时候不需要处理
			//新增部门的时候新增负责部门
            if(oConvertUtils.isNotEmpty(sysDepart.getDirectorUserIds())){
			    this.addDepartByUserIds(sysDepart,sysDepart.getDirectorUserIds());
            }
            //update-end---author:wangshuai ---date:20220307  for：[JTC-119]在部门管理菜单下设置部门负责人 创建用户的时候不需要处理
         }

	}
	
	/**
	 * saveDepartData 的调用方法,生成部门编码和部门类型（作废逻辑）
	 * @deprecated
	 * @param parentId
	 * @return
	 */
	private String[] generateOrgCode(String parentId) {	
		//update-begin--Author:Steve  Date:20190201 for：组织机构添加数据代码调整
				LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
				LambdaQueryWrapper<SysDepart> query1 = new LambdaQueryWrapper<SysDepart>();
				String[] strArray = new String[2];
		        // 创建一个List集合,存储查询返回的所有SysDepart对象
		        List<SysDepart> departList = new ArrayList<>();
				// 定义新编码字符串
				String newOrgCode = "";
				// 定义旧编码字符串
				String oldOrgCode = "";
				// 定义部门类型
				String orgType = "";
				// 如果是最高级,则查询出同级的org_code, 调用工具类生成编码并返回
				if (StringUtil.isNullOrEmpty(parentId)) {
					// 线判断数据库中的表是否为空,空则直接返回初始编码
					query1.eq(SysDepart::getParentId, "").or().isNull(SysDepart::getParentId);
					query1.orderByDesc(SysDepart::getOrgCode);
					departList = this.list(query1);
					if(departList == null || departList.size() == 0) {
						strArray[0] = YouBianCodeUtil.getNextYouBianCode(null);
						strArray[1] = "1";
						return strArray;
					}else {
					SysDepart depart = departList.get(0);
					oldOrgCode = depart.getOrgCode();
					orgType = depart.getOrgType();
					newOrgCode = YouBianCodeUtil.getNextYouBianCode(oldOrgCode);
					}
				} else { // 反之则查询出所有同级的部门,获取结果后有两种情况,有同级和没有同级
					// 封装查询同级的条件
					query.eq(SysDepart::getParentId, parentId);
					// 降序排序
					query.orderByDesc(SysDepart::getOrgCode);
					// 查询出同级部门的集合
					List<SysDepart> parentList = this.list(query);
					// 查询出父级部门
					SysDepart depart = this.getById(parentId);
					// 获取父级部门的Code
					String parentCode = depart.getOrgCode();
					// 根据父级部门类型算出当前部门的类型
					orgType = String.valueOf(Integer.valueOf(depart.getOrgType()) + 1);
					// 处理同级部门为null的情况
					if (parentList == null || parentList.size() == 0) {
						// 直接生成当前的部门编码并返回
						newOrgCode = YouBianCodeUtil.getSubYouBianCode(parentCode, null);
					} else { //处理有同级部门的情况
						// 获取同级部门的编码,利用工具类
						String subCode = parentList.get(0).getOrgCode();
						// 返回生成的当前部门编码
						newOrgCode = YouBianCodeUtil.getSubYouBianCode(parentCode, subCode);
					}
				}
				// 返回最终封装了部门编码和部门类型的数组
				strArray[0] = newOrgCode;
				strArray[1] = orgType;
				return strArray;
		//update-end--Author:Steve  Date:20190201 for：组织机构添加数据代码调整
	} 

	
	/**
	 * removeDepartDataById 对应 delete方法 根据ID删除相关部门数据
	 * 
	 */
	/*
	 * @Override
	 * 
	 * @Transactional public boolean removeDepartDataById(String id) {
	 * System.out.println("要删除的ID 为=============================>>>>>"+id); boolean
	 * flag = this.removeById(id); return flag; }
	 */

	/**
	 * updateDepartDataById 对应 edit 根据部门主键来更新对应的部门数据
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean updateDepartDataById(SysDepart sysDepart, String username) {
		if (sysDepart != null && username != null) {
			sysDepart.setUpdateTime(new Date());
			sysDepart.setUpdateBy(username);
			this.updateById(sysDepart);
            //update-begin---author:wangshuai ---date:20220307  for：[JTC-119]在部门管理菜单下设置部门负责人 创建用户的时候不需要处理
			//修改部门管理的时候，修改负责部门
            this.updateChargeDepart(sysDepart);
            //update-begin---author:wangshuai ---date:20220307  for：[JTC-119]在部门管理菜单下设置部门负责人 创建用户的时候不需要处理
			return true;
		} else {
			return false;
		}

	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBatchWithChildren(List<String> ids) {
		//存放子级的id
		List<String> idList = new ArrayList<String>();
		//存放父级的id
		List<String> parentIdList = new ArrayList<>();
		for(String id: ids) {
			idList.add(id);
			//此步骤是为了删除子级
			this.checkChildrenExists(id, idList);
			//update-begin---author:wangshuai ---date:20230712  for：【QQYUN-5757】批量删除部门时未正确置为叶子节点 ------------
			SysDepart depart = this.getDepartById(id);
			if (oConvertUtils.isNotEmpty(depart.getParentId())) {
				if (!parentIdList.contains(depart.getParentId())) {
					parentIdList.add(depart.getParentId());
				}
			}
			//update-end---author:wangshuai ---date:20230712  for：【QQYUN-5757】批量删除部门时未正确置为叶子节点 ------------
		}
		this.removeByIds(idList);
		//update-begin---author:wangshuai ---date:20230712  for：【QQYUN-5757】批量删除部门时未正确置为叶子节点 ------------
		//再删除前需要获取父级id，不然会一直为空
		this.setParentDepartIzLeaf(parentIdList);
		//update-end---author:wangshuai ---date:20230712  for：【QQYUN-5757】批量删除部门时未正确置为叶子节点 ------------
		//根据部门id获取部门角色id
		List<String> roleIdList = new ArrayList<>();
		LambdaQueryWrapper<SysDepartRole> query = new LambdaQueryWrapper<>();
		query.select(SysDepartRole::getId).in(SysDepartRole::getDepartId, idList);
		List<SysDepartRole> depRoleList = sysDepartRoleMapper.selectList(query);
		for(SysDepartRole deptRole : depRoleList){
			roleIdList.add(deptRole.getId());
		}
		//根据部门id删除用户与部门关系
		userDepartMapper.delete(new LambdaQueryWrapper<SysUserDepart>().in(SysUserDepart::getDepId,idList));
		//根据部门id删除部门授权
		departPermissionMapper.delete(new LambdaQueryWrapper<SysDepartPermission>().in(SysDepartPermission::getDepartId,idList));
		//根据部门id删除部门角色
		sysDepartRoleMapper.delete(new LambdaQueryWrapper<SysDepartRole>().in(SysDepartRole::getDepartId,idList));
		if(roleIdList != null && roleIdList.size()>0){
			//根据角色id删除部门角色授权
			departRolePermissionMapper.delete(new LambdaQueryWrapper<SysDepartRolePermission>().in(SysDepartRolePermission::getRoleId,roleIdList));
			//根据角色id删除部门角色用户信息
			departRoleUserMapper.delete(new LambdaQueryWrapper<SysDepartRoleUser>().in(SysDepartRoleUser::getDroleId,roleIdList));
		}
	}

	@Override
	public List<String> getSubDepIdsByDepId(String departId) {
		return this.baseMapper.getSubDepIdsByDepId(departId);
	}

	@Override
	public List<String> getMySubDepIdsByDepId(String departIds) {
		//根据部门id获取所负责部门
		String[] codeArr = this.getMyDeptParentOrgCode(departIds);
		if(codeArr==null || codeArr.length==0){
			return null;
		}
		return this.baseMapper.getSubDepIdsByOrgCodes(codeArr);
	}

	/**
	 * <p>
	 * 根据关键字搜索相关的部门数据
	 * </p>
	 */
	@Override
	public List<SysDepartTreeModel> searchByKeyWord(String keyWord,String myDeptSearch,String departIds) {
		LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
		List<SysDepartTreeModel> newList = new ArrayList<>();
		//myDeptSearch不为空时为我的部门搜索，只搜索所负责部门
		if(!StringUtil.isNullOrEmpty(myDeptSearch)){
			//departIds 为空普通用户或没有管理部门
			if(StringUtil.isNullOrEmpty(departIds)){
				return newList;
			}
			//根据部门id获取所负责部门
			String[] codeArr = this.getMyDeptParentOrgCode(departIds);
			//update-begin-author:taoyan date:20220104 for:/issues/3311 当用户属于两个部门的时候，且这两个部门没有上下级关系，我的部门-部门名称查询条件模糊搜索失效！
			if (codeArr != null && codeArr.length > 0) {
				query.nested(i -> {
					for (String s : codeArr) {
						i.or().likeRight(SysDepart::getOrgCode, s);
					}
				});
			}
			//update-end-author:taoyan date:20220104 for:/issues/3311 当用户属于两个部门的时候，且这两个部门没有上下级关系，我的部门-部门名称查询条件模糊搜索失效！
			query.eq(SysDepart::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
		}
		query.like(SysDepart::getDepartName, keyWord);
		//update-begin--Author:huangzhilin  Date:20140417 for：[bugfree号]组织机构搜索回显优化--------------------
		SysDepartTreeModel model = new SysDepartTreeModel();
		List<SysDepart> departList = this.list(query);
		if(departList.size() > 0) {
			for(SysDepart depart : departList) {
				model = new SysDepartTreeModel(depart);
				model.setChildren(null);
	    //update-end--Author:huangzhilin  Date:20140417 for：[bugfree号]组织机构搜索功回显优化----------------------
				newList.add(model);
			}
			return newList;
		}
		return null;
	}

	/**
	 * 根据部门id删除并且删除其可能存在的子级任何部门
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean delete(String id) {
		List<String> idList = new ArrayList<>();
		idList.add(id);
		this.checkChildrenExists(id, idList);
		//清空部门树内存
		//FindsDepartsChildrenUtil.clearDepartIdModel();
		boolean ok = this.removeByIds(idList);
		//根据部门id获取部门角色id
		List<String> roleIdList = new ArrayList<>();
		LambdaQueryWrapper<SysDepartRole> query = new LambdaQueryWrapper<>();
		query.select(SysDepartRole::getId).in(SysDepartRole::getDepartId, idList);
		List<SysDepartRole> depRoleList = sysDepartRoleMapper.selectList(query);
		for(SysDepartRole deptRole : depRoleList){
			roleIdList.add(deptRole.getId());
		}
		//根据部门id删除用户与部门关系
		userDepartMapper.delete(new LambdaQueryWrapper<SysUserDepart>().in(SysUserDepart::getDepId,idList));
		//根据部门id删除部门授权
		departPermissionMapper.delete(new LambdaQueryWrapper<SysDepartPermission>().in(SysDepartPermission::getDepartId,idList));
		//根据部门id删除部门角色
		sysDepartRoleMapper.delete(new LambdaQueryWrapper<SysDepartRole>().in(SysDepartRole::getDepartId,idList));
		if(roleIdList != null && roleIdList.size()>0){
			//根据角色id删除部门角色授权
			departRolePermissionMapper.delete(new LambdaQueryWrapper<SysDepartRolePermission>().in(SysDepartRolePermission::getRoleId,roleIdList));
			//根据角色id删除部门角色用户信息
			departRoleUserMapper.delete(new LambdaQueryWrapper<SysDepartRoleUser>().in(SysDepartRoleUser::getDroleId,roleIdList));
		}
		return ok;
	}
	
	/**
	 * delete 方法调用
	 * @param id
	 * @param idList
	 */
	private void checkChildrenExists(String id, List<String> idList) {	
		LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
		query.eq(SysDepart::getParentId,id);
		List<SysDepart> departList = this.list(query);
		if(departList != null && departList.size() > 0) {
			for(SysDepart depart : departList) {
				idList.add(depart.getId());
				this.checkChildrenExists(depart.getId(), idList);
			}
		}
	}

	@Override
	public List<SysDepart> queryUserDeparts(String userId) {
		return baseMapper.queryUserDeparts(userId);
	}

	@Override
	public List<SysDepart> queryDepartsByUsername(String username) {
		return baseMapper.queryDepartsByUsername(username);
	}
	
	@Override
	public List<String> queryDepartsByUserId(String userId) {
		List<String> list = baseMapper.queryDepartsByUserId(userId);
		return list;
	}

	/**
	 * 根据用户所负责部门ids获取父级部门编码
	 * @param departIds
	 * @return
	 */
	private String[] getMyDeptParentOrgCode(String departIds){
		//根据部门id查询所负责部门
		LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
		query.eq(SysDepart::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
		if(oConvertUtils.isNotEmpty(departIds)){
			query.in(SysDepart::getId, Arrays.asList(departIds.split(",")));
		}
		
		//------------------------------------------------------------------------------------------------
		//是否开启系统管理模块的多租户数据隔离【SAAS多租户模式】
		if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL){
			query.eq(SysDepart::getTenantId, oConvertUtils.getInt(TenantContext.getTenant(), 0));
		}
		//------------------------------------------------------------------------------------------------
		query.orderByAsc(SysDepart::getOrgCode);
		List<SysDepart> list = this.list(query);
		//查找根部门
		if(list == null || list.size()==0){
			return null;
		}
		String orgCode = this.getMyDeptParentNode(list);
		String[] codeArr = orgCode.split(",");
		return codeArr;
	}

	/**
	 * 获取负责部门父节点
	 * @param list
	 * @return
	 */
	private String getMyDeptParentNode(List<SysDepart> list){
		Map<String,String> map = new HashMap(5);
		//1.先将同一公司归类
		for(SysDepart dept : list){
			String code = dept.getOrgCode().substring(0,3);
			if(map.containsKey(code)){
				String mapCode = map.get(code)+","+dept.getOrgCode();
				map.put(code,mapCode);
			}else{
				map.put(code,dept.getOrgCode());
			}
		}
		StringBuffer parentOrgCode = new StringBuffer();
		//2.获取同一公司的根节点
		for(String str : map.values()){
			String[] arrStr = str.split(",");
			parentOrgCode.append(",").append(this.getMinLengthNode(arrStr));
		}
		return parentOrgCode.substring(1);
	}

	/**
	 * 获取同一公司中部门编码长度最小的部门
	 * @param str
	 * @return
	 */
	private String getMinLengthNode(String[] str){
		int min =str[0].length();
		StringBuilder orgCodeBuilder = new StringBuilder(str[0]);
		for(int i =1;i<str.length;i++){
			if(str[i].length()<=min){
				min = str[i].length();
                orgCodeBuilder.append(SymbolConstant.COMMA).append(str[i]);
			}
		}
		return orgCodeBuilder.toString();
	}
    /**
     * 获取部门树信息根据关键字
     * @param keyWord
     * @return
     */
    @Override
    public List<SysDepartTreeModel> queryTreeByKeyWord(String keyWord) {
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
        query.eq(SysDepart::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
        query.orderByAsc(SysDepart::getDepartOrder);
        List<SysDepart> list = this.list(query);
        // 调用wrapTreeDataToTreeList方法生成树状数据
        List<SysDepartTreeModel> listResult = FindsDepartsChildrenUtil.wrapTreeDataToTreeList(list);
        List<SysDepartTreeModel> treelist =new ArrayList<>();
        if(StringUtils.isNotBlank(keyWord)){
            this.getTreeByKeyWord(keyWord,listResult,treelist);
        }else{
            return listResult;
        }
        return treelist;
    }

	/**
	 * 根据parentId查询部门树
	 * @param parentId
	 * @param ids 前端回显传递
	 * @param primaryKey 主键字段（id或者orgCode）
	 * @return
	 */
	@Override
	public List<SysDepartTreeModel> queryTreeListByPid(String parentId,String ids, String primaryKey) {
		Consumer<LambdaQueryWrapper<SysDepart>> square = i -> {
			if (oConvertUtils.isNotEmpty(ids)) {
				if (CommonConstant.DEPART_KEY_ORG_CODE.equals(primaryKey)) {
					i.in(SysDepart::getOrgCode, ids.split(SymbolConstant.COMMA));
				} else {
					i.in(SysDepart::getId, ids.split(SymbolConstant.COMMA));
				}
			} else {
				if(oConvertUtils.isEmpty(parentId)){
					i.and(q->q.isNull(true,SysDepart::getParentId).or().eq(true,SysDepart::getParentId,""));
				}else{
					i.eq(true,SysDepart::getParentId,parentId);
				}
			}
		};
		LambdaQueryWrapper<SysDepart> lqw=new LambdaQueryWrapper<>();
		//------------------------------------------------------------------------------------------------
		//是否开启系统管理模块的 SASS 控制
		if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL){
			lqw.eq(SysDepart::getTenantId, oConvertUtils.getInt(TenantContext.getTenant(), 0));
		}
		//------------------------------------------------------------------------------------------------
		lqw.eq(true,SysDepart::getDelFlag,CommonConstant.DEL_FLAG_0.toString());
		lqw.func(square);
        //update-begin---author:wangshuai ---date:20220527  for：[VUEN-1143]排序不对，vue3和2应该都有问题，应该按照升序排------------
		lqw.orderByAsc(SysDepart::getDepartOrder);
        //update-end---author:wangshuai ---date:20220527  for：[VUEN-1143]排序不对，vue3和2应该都有问题，应该按照升序排--------------
		List<SysDepart> list = list(lqw);
        //update-begin---author:wangshuai ---date:20220316  for：[JTC-119]在部门管理菜单下设置部门负责人 创建用户的时候不需要处理
        //设置用户id,让前台显示
        this.setUserIdsByDepList(list);
        //update-end---author:wangshuai ---date:20220316  for：[JTC-119]在部门管理菜单下设置部门负责人 创建用户的时候不需要处理
		List<SysDepartTreeModel> records = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			SysDepart depart = list.get(i);
            SysDepartTreeModel treeModel = new SysDepartTreeModel(depart);
            //TODO 异步树加载key拼接__+时间戳,以便于每次展开节点会刷新数据
			//treeModel.setKey(treeModel.getKey()+"__"+System.currentTimeMillis());
            records.add(treeModel);
        }
		return records;
	}

	@Override
	public JSONObject queryAllParentIdByDepartId(String departId) {
		JSONObject result = new JSONObject();
		for (String id : departId.split(SymbolConstant.COMMA)) {
			JSONObject all = this.queryAllParentId("id", id);
			result.put(id, all);
		}
		return result;
	}

	@Override
	public JSONObject queryAllParentIdByOrgCode(String orgCode) {
		JSONObject result = new JSONObject();
		for (String code : orgCode.split(SymbolConstant.COMMA)) {
			JSONObject all = this.queryAllParentId("org_code", code);
			result.put(code, all);
		}
		return result;
	}

	/**
	 * 查询某个部门的所有父ID信息
	 *
	 * @param fieldName 字段名
	 * @param value     值
	 */
	private JSONObject queryAllParentId(String fieldName, String value) {
		JSONObject data = new JSONObject();
		// 父ID集合，有序
		data.put("parentIds", new JSONArray());
		// 父ID的部门数据，key是id，value是数据
		data.put("parentMap", new JSONObject());
		this.queryAllParentIdRecursion(fieldName, value, data);
		return data;
	}

	/**
	 * 递归调用查询父部门接口
	 */
	private void queryAllParentIdRecursion(String fieldName, String value, JSONObject data) {
		QueryWrapper<SysDepart> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(fieldName, value);
		SysDepart depart = super.getOne(queryWrapper);
		if (depart != null) {
			data.getJSONArray("parentIds").add(0, depart.getId());
			data.getJSONObject("parentMap").put(depart.getId(), depart);
			if (oConvertUtils.isNotEmpty(depart.getParentId())) {
				this.queryAllParentIdRecursion("id", depart.getParentId(), data);
			}
		}
	}

	@Override
	public SysDepart queryCompByOrgCode(String orgCode) {
		int length = YouBianCodeUtil.ZHANWEI_LENGTH;
		String compyOrgCode = orgCode.substring(0,length);
		return this.baseMapper.queryCompByOrgCode(compyOrgCode);
	}
	/**
	 * 根据id查询下级部门
	 * @param pid
	 * @return
	 */
	@Override
	public List<SysDepart> queryDeptByPid(String pid) {
		return this.baseMapper.queryDeptByPid(pid);
	}
	/**
     * 根据关键字筛选部门信息
     * @param keyWord
     * @return
     */
    public void getTreeByKeyWord(String keyWord,List<SysDepartTreeModel> allResult,List<SysDepartTreeModel>  newResult){
        for (SysDepartTreeModel model:allResult) {
            if (model.getDepartName().contains(keyWord)){
                newResult.add(model);
                continue;
            }else if(model.getChildren()!=null){
                getTreeByKeyWord(keyWord,model.getChildren(),newResult);
            }
        }
    }
    
    //update-begin---author:wangshuai ---date:20200308  for：[JTC-119]在部门管理菜单下设置部门负责人，新增方法添加部门负责人、删除负责部门负责人、查询部门对应的负责人
    /**
     * 通过用户id设置负责部门
     * @param sysDepart SysDepart部门对象
     * @param userIds 多个负责用户id
     */
    public void addDepartByUserIds(SysDepart sysDepart, String userIds) {
        //获取部门id,保存到用户
        String departId = sysDepart.getId();
        //循环用户id
        String[] userIdArray = userIds.split(",");
        for (String userId:userIdArray) {
            //查询用户表增加负责部门
            SysUser sysUser = sysUserMapper.selectById(userId);
            //如果部门id不为空，那么就需要拼接
            if(oConvertUtils.isNotEmpty(sysUser.getDepartIds())){
                if(!sysUser.getDepartIds().contains(departId)) {
                    sysUser.setDepartIds(sysUser.getDepartIds() + "," + departId);
                }
            }else{
                sysUser.setDepartIds(departId);
            }
            //设置身份为上级
            sysUser.setUserIdentity(CommonConstant.USER_IDENTITY_2);
            //跟新用户表
            sysUserMapper.updateById(sysUser);
            //判断当前用户是否包含所属部门
            List<SysUserDepart> userDepartList = userDepartMapper.getUserDepartByUid(userId);
            boolean isExistDepId = userDepartList.stream().anyMatch(item -> departId.equals(item.getDepId()));
            //如果不存在需要设置所属部门
            if(!isExistDepId){
                userDepartMapper.insert(new SysUserDepart(userId,departId));
            }
        }
    }
    
    /**
     * 修改用户负责部门
     * @param sysDepart SysDepart对象
     */
    private void updateChargeDepart(SysDepart sysDepart) {
        //新的用户id
        String directorIds = sysDepart.getDirectorUserIds();
        //旧的用户id（数据库中存在的）
        String oldDirectorIds = sysDepart.getOldDirectorUserIds();
        String departId = sysDepart.getId();
        //如果用户id为空,那么用户的负责部门id应该去除
        if(oConvertUtils.isEmpty(directorIds)){
            this.deleteChargeDepId(departId,null);
        }else if(oConvertUtils.isNotEmpty(directorIds) && oConvertUtils.isEmpty(oldDirectorIds)){
            //如果用户id不为空但是用户原来负责部门的用户id为空
            this.addDepartByUserIds(sysDepart,directorIds);
        }else{
            //都不为空，需要比较，进行添加或删除
            //找到新的负责部门用户id与原来负责部门的用户id，进行删除
            List<String> userIdList = Arrays.stream(oldDirectorIds.split(",")).filter(item -> !directorIds.contains(item)).collect(Collectors.toList());
            for (String userId:userIdList){
                this.deleteChargeDepId(departId,userId);
            }
            //找到原来负责部门的用户id与新的负责部门用户id，进行新增
            String addUserIds = Arrays.stream(directorIds.split(",")).filter(item -> !oldDirectorIds.contains(item)).collect(Collectors.joining(","));
            if(oConvertUtils.isNotEmpty(addUserIds)){
                this.addDepartByUserIds(sysDepart,addUserIds); 
            }
        }
    }

    /**
     * 删除用户负责部门
     * @param departId 部门id
     * @param userId 用户id
     */
    private void deleteChargeDepId(String departId,String userId){
        //先查询负责部门的用户id,因为负责部门的id使用逗号拼接起来的
        LambdaQueryWrapper<SysUser> query = new LambdaQueryWrapper<>();
        query.like(SysUser::getDepartIds,departId);
        //删除全部的情况下用户id不存在
        if(oConvertUtils.isNotEmpty(userId)){
            query.eq(SysUser::getId,userId); 
        }
        List<SysUser> userList = sysUserMapper.selectList(query);
        for (SysUser sysUser:userList) {
            //将不存在的部门id删除掉
            String departIds = sysUser.getDepartIds();
            List<String> list = new ArrayList<>(Arrays.asList(departIds.split(",")));
            list.remove(departId);
            //删除之后再将新的id用逗号拼接起来进行更新
            String newDepartIds = String.join(",",list);
            sysUser.setDepartIds(newDepartIds);
            sysUserMapper.updateById(sysUser);
        }
    }

    /**
     * 通过部门集合为部门设置用户id，用于前台展示
     * @param departList 部门集合
     */
    private void setUserIdsByDepList(List<SysDepart> departList) {
        //查询负责部门不为空的情况
        LambdaQueryWrapper<SysUser> query  = new LambdaQueryWrapper<>();
        query.isNotNull(SysUser::getDepartIds);
        List<SysUser> users = sysUserMapper.selectList(query);
        Map<String,Object> map = new HashMap(5);
        //先循环一遍找到不同的负责部门id
        for (SysUser user:users) {
            String departIds = user.getDepartIds();
            String[] departIdArray = departIds.split(",");
            for (String departId:departIdArray) {
                //mao中包含部门key，负责用户直接拼接
                if(map.containsKey(departId)){
                    String userIds = map.get(departId) + "," + user.getId();
                    map.put(departId,userIds);
                }else{
                    map.put(departId,user.getId());  
                }
            }
        }
        //循环部门集合找到部门id对应的负责用户
        for (SysDepart sysDepart:departList) {
            if(map.containsKey(sysDepart.getId())){
                sysDepart.setDirectorUserIds(map.get(sysDepart.getId()).toString()); 
            }
        }
    }
    //update-end---author:wangshuai ---date:20200308  for：[JTC-119]在部门管理菜单下设置部门负责人，新增方法添加部门负责人、删除负责部门负责人、查询部门对应的负责人

    /**
     * 获取我的部门已加入的公司
     * @return
     */
    @Override
    public List<SysDepart> getMyDepartList() {
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = user.getId();
        //字典code集合
        List<String> list = new ArrayList<>();
        //查询我加入的部门
        List<SysDepart> sysDepartList = this.baseMapper.queryUserDeparts(userId);
        for (SysDepart sysDepart : sysDepartList) {
            //获取一级部门编码
            String orgCode = sysDepart.getOrgCode();
            if (YouBianCodeUtil.ZHANWEI_LENGTH <= orgCode.length()) {
                int length = YouBianCodeUtil.ZHANWEI_LENGTH;
                String companyOrgCode = orgCode.substring(0, length);
                list.add(companyOrgCode);
            }
        }
        //字典code集合不为空
        if (oConvertUtils.isNotEmpty(list)) {
            //查询一级部门的数据
            LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
            query.select(SysDepart::getDepartName, SysDepart::getId, SysDepart::getOrgCode);
            query.eq(SysDepart::getDelFlag, String.valueOf(CommonConstant.DEL_FLAG_0));
            query.in(SysDepart::getOrgCode, list);
            return this.baseMapper.selectList(query);
        }
        return null;
    }

	@Override
	public void deleteDepart(String id) {
    	//删除部门设置父级的叶子结点
		this.setIzLeaf(id);
		this.delete(id);
		//删除部门用户关系表
		LambdaQueryWrapper<SysUserDepart> query = new LambdaQueryWrapper<SysUserDepart>()
				.eq(SysUserDepart::getDepId, id);
		this.userDepartMapper.delete(query);
	}

	@Override
	public List<SysDepartTreeModel> queryBookDepTreeSync(String parentId, Integer tenantId, String departName) {
		List<SysDepart> list = departMapper.queryBookDepTreeSync(parentId,tenantId,departName);
		List<SysDepartTreeModel> records = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			SysDepart depart = list.get(i);
			SysDepartTreeModel treeModel = new SysDepartTreeModel(depart);
			records.add(treeModel);
		}
		return records;
	}

	@Override
	public SysDepart getDepartById(String id) {
		return departMapper.getDepartById(id);
	}

	@Override
	public IPage<SysDepart> getMaxCodeDepart(Page<SysDepart> page, String parentId) {
		return page.setRecords(departMapper.getMaxCodeDepart(page,parentId));
	}

	@Override
	public void updateIzLeaf(String id, Integer izLeaf) {
		departMapper.setMainLeaf(id,izLeaf);
	}

	/**
	 * 设置父级节点是否存在叶子结点
	 * @param id
	 */
	private void setIzLeaf(String id) {
		SysDepart depart = this.getDepartById(id);
		String parentId = depart.getParentId();
		if(oConvertUtils.isNotEmpty(parentId)){
			Long count = this.count(new QueryWrapper<SysDepart>().lambda().eq(SysDepart::getParentId, parentId));
			if(count == 1){
				//若父节点无其他子节点，则该父节点是叶子节点
				departMapper.setMainLeaf(parentId, CommonConstant.IS_LEAF);
			}
		}
	}

	//========================begin 零代码下部门与人员导出 ==================================================================

	@Override
	public List<ExportDepartVo> getExcelDepart(int tenantId) {
		//获取父级部门
		List<ExportDepartVo> parentDepart = departMapper.getDepartList("",tenantId);
		//子部门
		List<ExportDepartVo> childrenDepart = new ArrayList<>();
		//把一级部门名称放在里面
		List<ExportDepartVo> exportDepartVoList = new ArrayList<>();
		//存放部门一级id避免重复
		List<String> departIdList = new ArrayList<>();
		for (ExportDepartVo departVo:parentDepart) {
			departIdList.add(departVo.getId());
			departVo.setDepartNameUrl(departVo.getDepartName());
			exportDepartVoList.add(departVo);
			//创建路径
			List<String> path = new ArrayList<>();
			path.add(departVo.getDepartName());
			//创建子部门路径
			findPath(departVo, path, tenantId,childrenDepart,departIdList);
			path.clear();
		}
		exportDepartVoList.addAll(childrenDepart);
		childrenDepart.clear();
		departIdList.clear();
		return exportDepartVoList;
	}

	/**
	 * 寻找部门路径
	 * @param departVo 部门vo
	 * @param path 部门路径
	 * @param tenantId 租户id
	 * @param childrenDepart 子部门
	 * @param departIdList 部门id集合
	 */
	private void findPath(ExportDepartVo departVo, List<String> path,Integer tenantId,List<ExportDepartVo> childrenDepart,List<String> departIdList) {
		//获取租户id和部门父id获取的部门数据
		List<ExportDepartVo> departList = departMapper.getDepartList(departVo.getId(), tenantId);
		//部门为空判断
		if (departList == null || departList.size() <= 0) {
			if(!departIdList.contains(departVo.getId())){
				departVo.setDepartNameUrl(String.join(SymbolConstant.SINGLE_SLASH,path));
				childrenDepart.add(departVo);
			}
			return;
		}

		for (int i = 0; i < departList.size(); i++) {
			ExportDepartVo exportDepartVo = departList.get(i);
			//存放子级路径
			List<String> cPath = new ArrayList<>();
			cPath.addAll(path);
			cPath.add(exportDepartVo.getDepartName());
			if(!departIdList.contains(departVo.getId())){
				departIdList.add(departVo.getId());
				departVo.setDepartNameUrl(String.join(SymbolConstant.SINGLE_SLASH,path));
				childrenDepart.add(departVo);
			}
			findPath(exportDepartVo,cPath ,tenantId, childrenDepart,departIdList);
		}
	}
	//========================end 零代码下部门与人员导出 ==================================================================

	//========================begin 零代码下部门与人员导入 ==================================================================
	@Override
	public void importExcel(List<ExportDepartVo> listSysDeparts, List<String> errorMessageList) {
		int num = 0;
		int tenantId = oConvertUtils.getInt(TenantContext.getTenant(), 0);
		
		//部门路径排序
		Collections.sort(listSysDeparts, new Comparator<ExportDepartVo>() {
			@Override
			public int compare(ExportDepartVo o1, ExportDepartVo o2) {
				if(oConvertUtils.isNotEmpty(o1.getDepartNameUrl()) && oConvertUtils.isNotEmpty(o2.getDepartNameUrl())){
					int oldLength = o1.getDepartNameUrl().split(SymbolConstant.SINGLE_SLASH).length;
					int newLength = o2.getDepartNameUrl().split(SymbolConstant.SINGLE_SLASH).length;
					return oldLength - newLength;
				}else{
					return 0;
				}
			}
		});
		//存放部门数据的map
		Map<String,SysDepart> departMap = new HashMap<>();
		//循环第二遍导入数据
		for (ExportDepartVo exportDepartVo : listSysDeparts) {
			SysDepart sysDepart = new SysDepart();
			// orgCode编码长度
			int codeLength = YouBianCodeUtil.ZHANWEI_LENGTH;
			Boolean izExport = false;
			try {
				izExport = this.addDepartByName(exportDepartVo.getDepartNameUrl(),exportDepartVo.getDepartName(),sysDepart,errorMessageList,tenantId,departMap,num);
			} catch (Exception e) {
				//没有查找到parentDept
			}
			//没有错误的时候才会导入数据
			if(izExport){
				sysDepart.setOrgType(sysDepart.getOrgCode().length()/codeLength+"");
				sysDepart.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
				sysDepart.setOrgCategory("1");
				sysDepart.setTenantId(tenantId);
				ImportExcelUtil.importDateSaveOne(sysDepart, ISysDepartService.class, errorMessageList, num, CommonConstant.SQL_INDEX_UNIQ_DEPART_ORG_CODE);
				departMap.put(exportDepartVo.getDepartNameUrl(),sysDepart);
			}
			num++;
		}
	}

	/**
	 * 添加部门
	 * @param departNameUrl 部门路径
	 * @param departName 部门名称
	 * @param sysDepart 部门类
	 * @param errorMessageList 错误集合
	 * @param tenantId 租户id
	 * @param departMap 部门数组。避免存在部门信息再次查询 key 存放部门路径 value 存放部门对象
	 * @param num 判断第几行有错误信息
	 */
	private Boolean addDepartByName(String departNameUrl,String departName,SysDepart sysDepart,List<String> errorMessageList,Integer tenantId,Map<String,SysDepart> departMap, int num) {
		int lineNumber = num + 1;
		if(oConvertUtils.isEmpty(departNameUrl) && oConvertUtils.isEmpty(departName)){
			//部门路径为空
			errorMessageList.add("第 " + lineNumber + " 行：记录部门路径或者部门名称为空禁止导入");
			return false;
		}
		//获取部门名称路径
		String name = "";
		if(departNameUrl.contains(SymbolConstant.SINGLE_SLASH)){
			//获取分割的部门名称
			name = departNameUrl.substring(departNameUrl.lastIndexOf(SymbolConstant.SINGLE_SLASH)+1);
		}else{
			name = departNameUrl;
		}
		
		if(!name.equals(departName)){
			//部门名称已存在
			errorMessageList.add("第 " + lineNumber + " 行：记录部门路径:”"+departNameUrl+"“"+"和部门名称：“"+departName+"“不一致，请检查！");
			return false;
		}else{
			String parentId = "";
			//判断是否包含“/”
			if(departNameUrl.contains(SymbolConstant.SINGLE_SLASH)){
				//获取最后一个斜杠之前的路径
				String departNames = departNameUrl.substring(0,departNameUrl.lastIndexOf(SymbolConstant.SINGLE_SLASH));
				//判断是否已经包含部门路径
				if(departMap.containsKey(departNames)){
					SysDepart depart = departMap.get(departNames);
					if(null != depart){
						parentId = depart.getId();
					}
				}else{
					//分割斜杠路径，查看数据库中是否存在此路径
					String[] departNameUrls = departNameUrl.split(SymbolConstant.SINGLE_SLASH);
					String departUrlName = departNameUrls[0];
					//判断是否为最后一位
					int count = 0;
					SysDepart depart  = new SysDepart();
					depart.setId("");
					String parentIdByName = this.getDepartListByName(departUrlName,tenantId,depart,departNameUrls,count,departNameUrls.length-1,name,departMap);
					//如果parentId不为空
					if(oConvertUtils.isNotEmpty(parentIdByName)){
						parentId = parentIdByName;
					}else{
						//部门名称已存在
						errorMessageList.add("第 " + lineNumber + " 行：记录部门名称“"+departName+"”上级不存在，请检查！");
						return false;
					}
				}
			}
			//查询部门名称是否已存在
			SysDepart parentDept = null;
			//update-begin---author:wangshuai ---date:20230721  for：一个租户部门名称可能有多个------------
			List<SysDepart> sysDepartList = departMapper.getDepartByName(departName,tenantId,parentId);
			if(CollectionUtil.isNotEmpty(sysDepartList)){
				parentDept = sysDepartList.get(0);
			}
			//update-end---author:wangshuai ---date:20230721  for：一个租户部门名称可能有多个------------
			if(null != parentDept) {
				//部门名称已存在
				errorMessageList.add("第 " + lineNumber + " 行：记录部门名称“"+departName+"”已存在，请检查！");
				return false;
			}else{
				Page<SysDepart> page = new Page<>(1,1);
				//需要获取父级id，查看父级是否已经存在
				//获取一级部门的最大orgCode
				List<SysDepart> records = departMapper.getMaxCodeDepart(page, parentId);
				String newOrgCode = "";
				if(CollectionUtil.isNotEmpty(records)){
					newOrgCode = YouBianCodeUtil.getNextYouBianCode(records.get(0).getOrgCode());
				}else{
					//查询父id
					if(oConvertUtils.isNotEmpty(parentId)){
						SysDepart departById = departMapper.getDepartById(parentId);
						newOrgCode = YouBianCodeUtil.getSubYouBianCode(departById.getOrgCode(), null);
					}else{
						newOrgCode = YouBianCodeUtil.getNextYouBianCode(null);
					}
				}
				if(oConvertUtils.isNotEmpty(parentId)){
					this.updateIzLeaf(parentId,CommonConstant.NOT_LEAF);
					sysDepart.setParentId(parentId);
				}
				sysDepart.setOrgCode(newOrgCode);
				sysDepart.setDepartName(departName);
				return true;
			}
	
		}
	}

	/**
	 * 获取部门名称url（下级）
	 * @param departName 部门名称
	 * @param tenantId 租户id
	 * @param sysDepart 部门对象
	 * @param count 部门路径下标
	 * @param departNameUrls 部门路径
	 * @param departNum 部门路径的数量
	 * @param name 部门路径的数量
	 * @param departMap 存放部门的数据 key 存放部门路径 value 存放部门对象
	 */
	private String getDepartListByName(String departName, Integer tenantId, SysDepart sysDepart,String[] departNameUrls, int count, int departNum,String name,Map<String,SysDepart> departMap) {
		//递归查找下一级
		//update-begin---author:wangshuai ---date:20230721  for：一个租户部门名称可能有多个------------
		SysDepart parentDept = null;
		List<SysDepart> departList = departMapper.getDepartByName(departName,tenantId,sysDepart.getId());
		if(CollectionUtil.isNotEmpty(departList)){
			parentDept = departList.get(0);
		}
		//update-end---author:wangshuai ---date:20230721  for：一个租户部门名称可能有多个------------
		//判断是否包含/
		if(oConvertUtils.isNotEmpty(name)){
			name = name + SymbolConstant.SINGLE_SLASH + departName;
		}else{
			name = departName;
		}
		if(null != parentDept){
			//如果名称路径key不再在，添加一个，避免再次查询
			if(!departMap.containsKey(name)){
				departMap.put(name,parentDept);
			}
			//查询出来的部门名称和部门路径中的部门名称作比较，如果不存在直接返回空
			if(parentDept.getDepartName().equals(departNameUrls[count])){
				count = count + 1;
				//数量和部门数量相等说明已经到最后一位了，直接返回部门id
				if(count == departNum){
					return parentDept.getId();
				}else{
					return this.getDepartListByName(departNameUrls[count],tenantId,parentDept,departNameUrls,count,departNum,name,departMap);
				}
			}else{
				return "";
			}
		}else{
			return "";
		}
	}
	//========================end 零代码下部门与人员导入 ==================================================================

	/**
	 * 清空部门id
	 *
	 * @param parentIdList
	 */
	private void setParentDepartIzLeaf(List<String> parentIdList) {
		if (CollectionUtil.isNotEmpty(parentIdList)) {
			for (String parentId : parentIdList) {
				//查询父级id没有子级的时候跟新为叶子节点
				LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
				query.eq(SysDepart::getParentId, parentId);
				Long count = departMapper.selectCount(query);
				//当子级都不存在时，设置当前部门为叶子节点
				if (count == 0) {
					departMapper.setMainLeaf(parentId, CommonConstant.IS_LEAF);
				}
			}
		}
	}

	//========================begin 系统下部门与人员导入 ==================================================================
	/**
	 * 系统部门导出
	 * @param tenantId
	 * @return
	 */
	@Override
	public List<SysDepartExportVo> getExportDepart(Integer tenantId) {
		//获取父级部门
		List<SysDepartExportVo> parentDepart = departMapper.getSysDepartList("", tenantId);
		//子部门
		List<SysDepartExportVo> childrenDepart = new ArrayList<>();
		//把一级部门名称放在里面
		List<SysDepartExportVo> exportDepartVoList = new ArrayList<>();
		//存放部门一级id避免重复
		List<String> departIdList = new ArrayList<>();
		for (SysDepartExportVo sysDepart : parentDepart) {
			//step 1.添加第一级部门
			departIdList.add(sysDepart.getId());
			sysDepart.setDepartNameUrl(sysDepart.getDepartName());
			exportDepartVoList.add(sysDepart);
			//step 2.添加自己部门路径，用/分离
			//创建路径
			List<String> path = new ArrayList<>();
			path.add(sysDepart.getDepartName());
			//创建子部门路径
			findSysDepartPath(sysDepart, path, tenantId, childrenDepart, departIdList);
			path.clear();
		}
		exportDepartVoList.addAll(childrenDepart);
		childrenDepart.clear();
		departIdList.clear();
		return exportDepartVoList;
	}

	/**
	 * 系统部门导入
	 * @param listSysDeparts
	 * @param errorMessageList
	 */
	@Override
	public void importSysDepart(List<SysDepartExportVo> listSysDeparts, List<String> errorMessageList) {
		int num = 0;
		int tenantId = 0;
		if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
			tenantId = oConvertUtils.getInt(TenantContext.getTenant(), 0);
		}
		//部门路径排序
		Collections.sort(listSysDeparts, new Comparator<SysDepartExportVo>() {
			@Override
			public int compare(SysDepartExportVo o1, SysDepartExportVo o2) {
				if(oConvertUtils.isNotEmpty(o1.getDepartNameUrl()) && oConvertUtils.isNotEmpty(o2.getDepartNameUrl())){
					int oldLength = o1.getDepartNameUrl().split(SymbolConstant.SINGLE_SLASH).length;
					int newLength = o2.getDepartNameUrl().split(SymbolConstant.SINGLE_SLASH).length;
					return oldLength - newLength;
				}else{
					return 0;
				}
			}
		});
		//存放部门数据的map
		Map<String,SysDepart> departMap = new HashMap<>();
		// orgCode编码长度
		int codeLength = YouBianCodeUtil.ZHANWEI_LENGTH;
		//循环第二遍导入数据
		for (SysDepartExportVo departExportVo : listSysDeparts) {
			SysDepart sysDepart = new SysDepart();
			boolean izExport = false;
			try {
				izExport = this.addDepartByName(departExportVo.getDepartNameUrl(),departExportVo.getDepartName(),sysDepart,errorMessageList,tenantId,departMap,num);
			} catch (Exception e) {
				//没有查找到parentDept
			}
			//没有错误的时候才会导入数据
			if(izExport){
				if(oConvertUtils.isNotEmpty(departExportVo.getOrgCode())){
					SysDepart depart = this.baseMapper.queryCompByOrgCode(departExportVo.getOrgCode());
					if(null != depart){
						if(oConvertUtils.isNotEmpty(sysDepart.getParentId())){
							//更新上级部门为叶子节点
							this.updateIzLeaf(sysDepart.getParentId(),CommonConstant.IS_LEAF);
						}
						//部门名称已存在
						errorMessageList.add("第 " + num + " 行：记录部门名称“"+departExportVo.getDepartName()+"”部门编码重复，请检查！");
						continue;
					}
					String departNameUrl = departExportVo.getDepartNameUrl();
					//包含/说明是多级
					if(departNameUrl.contains(SymbolConstant.SINGLE_SLASH)){
						//判断添加部门的规则是否和生成的一致
						if(!sysDepart.getOrgCode().equals(departExportVo.getOrgCode())){
							if(oConvertUtils.isNotEmpty(sysDepart.getParentId())){
								//更新上级部门为叶子节点
								this.updateIzLeaf(sysDepart.getParentId(),CommonConstant.IS_LEAF);
							}
							//部门名称已存在
							errorMessageList.add("第 " + num + " 行：记录部门名称“"+departExportVo.getDepartName()+"”部门编码规则不匹配，请检查！");
							continue;
						}
					}
					sysDepart.setOrgCode(departExportVo.getOrgCode());
					if(oConvertUtils.isNotEmpty(sysDepart.getParentId())){
					    //上级
						sysDepart.setOrgType("2");
					}else{
						//下级
						sysDepart.setOrgType("1");
					}
				}else{
					sysDepart.setOrgType(sysDepart.getOrgCode().length()/codeLength+"");
				}
				sysDepart.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
				sysDepart.setDepartNameEn(departExportVo.getDepartNameEn());
				sysDepart.setDepartOrder(departExportVo.getDepartOrder());
				sysDepart.setOrgCategory(oConvertUtils.getString(departExportVo.getOrgCategory(),"1"));
				sysDepart.setMobile(departExportVo.getMobile());
				sysDepart.setFax(departExportVo.getFax());
				sysDepart.setAddress(departExportVo.getAddress());
				sysDepart.setMemo(departExportVo.getMemo());
				ImportExcelUtil.importDateSaveOne(sysDepart, ISysDepartService.class, errorMessageList, num, CommonConstant.SQL_INDEX_UNIQ_DEPART_ORG_CODE);
				departMap.put(departExportVo.getDepartNameUrl(),sysDepart);
			}
			num++;
		}
	}

	/**
	 * 寻找部门路径
	 *
	 * @param departVo       部门vo
	 * @param path           部门路径
	 * @param tenantId       租户id
	 * @param childrenDepart 子部门
	 * @param departIdList   部门id集合
	 */
	private void findSysDepartPath(SysDepartExportVo departVo, List<String> path, Integer tenantId, List<SysDepartExportVo> childrenDepart, List<String> departIdList) {
		//step 1.查询子部门的数据
		//获取租户id和部门父id获取的部门数据
		List<SysDepartExportVo> departList = departMapper.getSysDepartList(departVo.getId(), tenantId);
		//部门为空判断
		if (departList == null || departList.size() <= 0) {
			//判断最后一个子部门是否已拼接
			if (!departIdList.contains(departVo.getId())) {
				departVo.setDepartNameUrl(String.join(SymbolConstant.SINGLE_SLASH, path));
				childrenDepart.add(departVo);
			}
			return;
		}

		for (SysDepartExportVo exportDepartVo : departList) {
			//存放子级路径
			List<String> cPath = new ArrayList<>(path);
			cPath.add(exportDepartVo.getDepartName());
			//step 2.拼接子部门路径
			if (!departIdList.contains(departVo.getId())) {
				departIdList.add(departVo.getId());
				departVo.setDepartNameUrl(String.join(SymbolConstant.SINGLE_SLASH, path));
				childrenDepart.add(departVo);
			}
			//step 3.递归查询子路径，直到找不到为止
			findSysDepartPath(exportDepartVo, cPath, tenantId, childrenDepart, departIdList);
		}
	}
	//========================end 系统下部门与人员导入 ==================================================================
}
