package org.jeecg.modules.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
import org.jeecg.common.constant.enums.DepartCategoryEnum;
import org.jeecg.common.exception.JeecgBootBizTipException;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.*;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.mapper.*;
import org.jeecg.modules.system.model.DepartIdModel;
import org.jeecg.modules.system.model.SysDepartTreeModel;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.util.FindsDepartsChildrenUtil;
import org.jeecg.modules.system.vo.*;
import org.jeecg.modules.system.vo.lowapp.ExportDepartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    @Autowired
    private SysPositionMapper sysPositionMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SysUserDepPostMapper sysUserDepPostMapper;
    
	@Override
	public List<SysDepartTreeModel> queryMyDeptTreeList(String departIds) {
		//根据部门id获取所负责部门
		LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
		String[] codeArr = this.getMyDeptParentOrgCode(departIds);
		// 代码逻辑说明: 【QQYUN-7320】查询部门没数据，导致报错空指针---
		if(ArrayUtil.isEmpty(codeArr)){
			return null;
		}
		for(int i=0;i<codeArr.length;i++){
			query.or().likeRight(SysDepart::getOrgCode,codeArr[i]);
		}
		query.eq(SysDepart::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
        query.ne(SysDepart::getOrgCategory,DepartCategoryEnum.DEPART_CATEGORY_POST.getValue());
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
        // 代码逻辑说明: 【QQYUN-13427】部门选择组件修改:需要过滤掉岗位 只保留 公司 子公司 部门---
        query.ne(SysDepart::getOrgCategory, DepartCategoryEnum.DEPART_CATEGORY_POST.getValue());
		query.orderByAsc(SysDepart::getDepartOrder);
		List<SysDepart> list = this.list(query);
		//设置用户id,让前台显示
        this.setUserIdsByDepList(list);
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
        // 代码逻辑说明: 【QQYUN-13427】部门选择组件修改:需要过滤掉岗位 只保留 公司 子公司 部门---
         query.ne(SysDepart::getOrgCategory,DepartCategoryEnum.DEPART_CATEGORY_POST.getValue());
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
			// 代码逻辑说明: [QQYUN-4163]给部门表加个是否有子节点------------
			if (oConvertUtils.isEmpty(sysDepart.getParentId())) {
				sysDepart.setParentId("");
			}else{
				//将父部门的设成不是叶子结点
				departMapper.setMainLeaf(sysDepart.getParentId(),CommonConstant.NOT_LEAF);
			}
			//String s = UUID.randomUUID().toString().replace("-", "");
			sysDepart.setId(IdWorker.getIdStr(sysDepart));
			// 先判断该对象有无父级ID,有则意味着不是最高级,否则意味着是最高级
			// 获取父级ID
			String parentId = sysDepart.getParentId();
			// 代码逻辑说明: 部门编码规则生成器做成公用配置
			JSONObject formData = new JSONObject();
			formData.put("parentId",parentId);
			String[] codeArray = (String[]) FillRuleUtil.executeRule(FillRuleConstant.DEPART,formData);
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
			//新增部门的时候新增负责部门
            if(oConvertUtils.isNotEmpty(sysDepart.getDirectorUserIds())){
			    this.addDepartByUserIds(sysDepart,sysDepart.getDirectorUserIds());
            }
         }

	}
	
	/**
	 * saveDepartData 的调用方法,生成部门编码和部门类型（作废逻辑）
	 * @deprecated
	 * @param parentId
	 * @return
	 */
	private String[] generateOrgCode(String parentId) {	
				// 代码逻辑说明: 组织机构添加数据代码调整
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
            //验证部门类型
            this.verifyOrgCategory(sysDepart);
			this.updateById(sysDepart);
			//修改部门管理的时候，修改负责部门
            this.updateChargeDepart(sysDepart);
            //redis清除缓存key
            redisUtil.removeAll(CommonConstant.DEPART_NAME_REDIS_KEY_PRE);
			return true;
		} else {
			return false;
		}

	}

    /**
     * 验证部门类型
     *
     * @param sysDepart
     */
    private void verifyOrgCategory(SysDepart sysDepart) {
        //update-begin---author:wangshuai---date:2025-08-21---for: 当部门类型为岗位的时候，需要查看是否存在下级，存在下级无法变更为岗位---
        //如果是岗位的情况下，不能存在子级
        if (oConvertUtils.isNotEmpty(sysDepart.getOrgCategory()) && DepartCategoryEnum.DEPART_CATEGORY_POST.getValue().equals(sysDepart.getOrgCategory())) {
            long count = this.count(new QueryWrapper<SysDepart>().lambda().eq(SysDepart::getParentId, sysDepart.getId()));
            if (count > 0) {
                throw new JeecgBootBizTipException("当前子公司/部门下存在子级，无法变更为岗位!");
            }
        }
        //如果是子公司的情况下，则上级不能为部门或者岗位
        if (oConvertUtils.isNotEmpty(sysDepart.getOrgCategory()) && DepartCategoryEnum.DEPART_CATEGORY_SUB_COMPANY.getValue().equals(sysDepart.getOrgCategory())
                && oConvertUtils.isNotEmpty(sysDepart.getParentId())) {
            LambdaQueryWrapper<SysDepart> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysDepart::getId, sysDepart.getParentId());
            queryWrapper.in(SysDepart::getOrgCategory, DepartCategoryEnum.DEPART_CATEGORY_POST.getValue(), DepartCategoryEnum.DEPART_CATEGORY_DEPART.getValue());
            long count = this.count(queryWrapper);
            if (count > 0) {
                throw new JeecgBootBizTipException("当前父级为部门或岗位，无法变更为子公司!");
            }
        }
        //如果是部门的情况下，下级不能为子公司或者公司
        if (oConvertUtils.isNotEmpty(sysDepart.getOrgCategory()) && DepartCategoryEnum.DEPART_CATEGORY_DEPART.getValue().equals(sysDepart.getOrgCategory())) {
            LambdaQueryWrapper<SysDepart> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysDepart::getParentId, sysDepart.getId());
            queryWrapper.in(SysDepart::getOrgCategory, DepartCategoryEnum.DEPART_CATEGORY_COMPANY.getValue(), DepartCategoryEnum.DEPART_CATEGORY_SUB_COMPANY.getValue());
            long count = this.count(queryWrapper);
            if (count > 0) {
                throw new JeecgBootBizTipException("当前子级存在子公司，无法变更为部门!");
            }
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
			// 代码逻辑说明: 【QQYUN-5757】批量删除部门时未正确置为叶子节点 ------------
			SysDepart depart = this.getDepartById(id);
			if (oConvertUtils.isNotEmpty(depart.getParentId())) {
				if (!parentIdList.contains(depart.getParentId())) {
					parentIdList.add(depart.getParentId());
				}
			}
		}
		this.removeByIds(idList);
		//再删除前需要获取父级id，不然会一直为空
		this.setParentDepartIzLeaf(parentIdList);
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
        //删除岗位信息
        this.deleteDepartPostByDepIds(idList);
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
	public List<SysDepartTreeModel> searchByKeyWord(String keyWord, String myDeptSearch, String departIds, String orgCategory, String depIds) {
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
			// 代码逻辑说明: /issues/3311 当用户属于两个部门的时候，且这两个部门没有上下级关系，我的部门-部门名称查询条件模糊搜索失效！
			if (codeArr != null && codeArr.length > 0) {
				query.nested(i -> {
					for (String s : codeArr) {
						i.or().likeRight(SysDepart::getOrgCode, s);
					}
				});
			}
			query.eq(SysDepart::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
		}
		query.like(SysDepart::getDepartName, keyWord);
        //需要根据部门类型进行数据筛选
        if(oConvertUtils.isNotEmpty(orgCategory)){
            query.in(SysDepart::getOrgCategory, Arrays.asList(orgCategory.split(SymbolConstant.COMMA)));
        }else{
            query.ne(SysDepart::getOrgCategory,DepartCategoryEnum.DEPART_CATEGORY_POST.getValue());
        }
        //如果前端传过来的部门id不为空的时候，说明是系统用户根据所属部门选择主岗位或者兼职岗位，需要进行数据过滤
        if(oConvertUtils.isNotEmpty(depIds)){
            List<String> codeList = baseMapper.getDepCodeByDepIds(Arrays.asList(depIds.split(SymbolConstant.COMMA)));
            if(CollectionUtil.isNotEmpty(codeList)){
                query.and(i -> {
                    for (String code : codeList) {
                        i.or().likeRight(SysDepart::getOrgCode,code);
                    }
                });
            }
        }
        // 代码逻辑说明: [bugfree号]组织机构搜索回显优化--------------------
		SysDepartTreeModel model = new SysDepartTreeModel();
		List<SysDepart> departList = this.list(query);
		if(departList.size() > 0) {
			for(SysDepart depart : departList) {
				model = new SysDepartTreeModel(depart);
				model.setChildren(null);
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
		List<SysDepart> sysDeparts = baseMapper.queryUserDeparts(userId);
		sysDeparts.stream()
			.filter(depart -> oConvertUtils.isNotEmpty(depart) &&
					oConvertUtils.isNotEmpty(depart.getOrgCode()))
			.forEach(depart -> {
				String orgCategory = depart.getOrgCategory();
				if(DepartCategoryEnum.DEPART_CATEGORY_DEPART.getValue().equalsIgnoreCase(orgCategory)){
					String departPathName = this.getDepartPathNameByOrgCode(depart.getOrgCode(), "");
					depart.setDepartPathName(departPathName);
				}
			});
		return sysDeparts;
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
        // 代码逻辑说明: 【QQYUN-13427】部门选择组件修改:需要过滤掉岗位 只保留 公司 子公司 部门---
        lqw.ne(SysDepart::getOrgCategory,DepartCategoryEnum.DEPART_CATEGORY_POST.getValue());
		lqw.func(square);
        // 代码逻辑说明: [VUEN-1143]排序不对，vue3和2应该都有问题，应该按照升序排------------
		lqw.orderByAsc(SysDepart::getDepartOrder);
		List<SysDepart> list = list(lqw);
        //设置用户id,让前台显示
        this.setUserIdsByDepList(list);
		List<SysDepartTreeModel> records = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			SysDepart depart = list.get(i);
            // 代码逻辑说明: 【QQYUN-13427】部门选择组件修改:需要过滤掉岗位 只保留 公司 子公司 部门---
            long count = getNoDepartPostCount(depart.getId());
            if(count == 0){
                depart.setIzLeaf(CommonConstant.IS_LEAF);
            }
            SysDepartTreeModel treeModel = new SysDepartTreeModel(depart);
            //TODO 异步树加载key拼接__+时间戳,以便于每次展开节点会刷新数据
			//treeModel.setKey(treeModel.getKey()+"__"+System.currentTimeMillis());
            records.add(treeModel);
        }
		return records;
	}

    /**
     * 获取部门数量
     * @param departId
     * @return
     */
    private long getNoDepartPostCount(String departId) {
        LambdaQueryWrapper<SysDepart> queryNoPosition = new LambdaQueryWrapper<>();
        queryNoPosition.ne(SysDepart::getOrgCategory,DepartCategoryEnum.DEPART_CATEGORY_POST.getValue());
        queryNoPosition.eq(SysDepart::getParentId,departId);
        return this.count(queryNoPosition);
    }

    /**
     * 部门管理异步树
     *
     * @param parentId
     * @param ids
     * @param primaryKey
     * @param departIds
     * @return
     */
    @Override
    public List<SysDepartTreeModel> queryDepartAndPostTreeSync(String parentId, String ids, String primaryKey, 
															   String departIds, String orgName) {
        Consumer<LambdaQueryWrapper<SysDepart>> square = i -> {
            if (oConvertUtils.isNotEmpty(ids)) {
                if (CommonConstant.DEPART_KEY_ORG_CODE.equals(primaryKey)) {
                    i.in(SysDepart::getOrgCode, ids.split(SymbolConstant.COMMA));
                } else {
                    i.in(SysDepart::getId, ids.split(SymbolConstant.COMMA));
                }
            } else {
                if(oConvertUtils.isEmpty(parentId)){
                    // 代码逻辑说明: 如果前端传过来的部门id不为空的时候，说明是系统用户根据所属部门选择主岗位或者兼职岗位---
                    if(oConvertUtils.isNotEmpty(departIds)){
                        i.in(SysDepart::getId,Arrays.asList(departIds.split(SymbolConstant.COMMA)));
                    }else{
						if(oConvertUtils.isEmpty(orgName)){
                        	i.and(q->q.isNull(true,SysDepart::getParentId).or().eq(true,SysDepart::getParentId,""));
						}else{
							i.like(SysDepart::getDepartName, orgName);
						}
                    }
                }else{
                    i.eq(true,SysDepart::getParentId,parentId);
                }
            }
        };
        LambdaQueryWrapper<SysDepart> lqw=new LambdaQueryWrapper<>();
        //是否开启系统管理模块的 SASS 控制
        if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL){
            lqw.eq(SysDepart::getTenantId, oConvertUtils.getInt(TenantContext.getTenant(), 0));
        }
        lqw.eq(true,SysDepart::getDelFlag,CommonConstant.DEL_FLAG_0.toString());
        lqw.func(square);
        lqw.orderByAsc(SysDepart::getDepartOrder);
        List<SysDepart> list = list(lqw);
        //设置用户id,让前台显示
        this.setUserIdsByDepList(list);
        List<String> departIdList = new ArrayList<>();
        //如果前端传过来的部门id不为空的时候，说明是系统用户根据所属部门选择主岗位或者兼职岗位
        if(oConvertUtils.isNotEmpty(departIds) && oConvertUtils.isEmpty(parentId)){
            departIdList = list.stream().map(SysDepart::getId).toList();
        }
        List<SysDepartTreeModel> records = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            SysDepart depart = list.get(i);
            //如果部门id和父级部门id再同一列的时候，不用添加到树结构里面去了
            if(oConvertUtils.isNotEmpty(departIds) && oConvertUtils.isEmpty(parentId)
               && departIdList.contains(depart.getParentId())){
                continue;
            }
            SysDepartTreeModel treeModel = new SysDepartTreeModel(depart);
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
			// 代码逻辑说明: 一个租户部门名称可能有多个------------
			List<SysDepart> sysDepartList = departMapper.getDepartByName(departName,tenantId,parentId);
			if(CollectionUtil.isNotEmpty(sysDepartList)){
				parentDept = sysDepartList.get(0);
			}
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
		// 代码逻辑说明: 一个租户部门名称可能有多个------------
		SysDepart parentDept = null;
		List<SysDepart> departList = departMapper.getDepartByName(departName,tenantId,sysDepart.getId());
		if(CollectionUtil.isNotEmpty(departList)){
			parentDept = departList.get(0);
		}
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
	 * @param idList 需要查询部门sql的id集合
	 * @return
	 */
	@Override
	public List<SysDepartExportVo> getExportDepart(Integer tenantId, List<String> idList) {
        String parentId = "";
        if(CollectionUtil.isEmpty(idList)){
            //-1代表父级部门为空的数据
            parentId = "-1";
        }
		//获取父级部门
		List<SysDepartExportVo> parentDepart = departMapper.getSysDepartList(parentId, tenantId, idList);
		//子部门
		List<SysDepartExportVo> childrenDepart = new ArrayList<>();
		//把一级部门名称放在里面
		List<SysDepartExportVo> exportDepartVoList = new ArrayList<>();
		//存放部门一级id避免重复
		List<String> departIdList = new ArrayList<>();
		for (SysDepartExportVo sysDepart : parentDepart) {
            if(CollectionUtil.isNotEmpty(departIdList) && departIdList.contains(sysDepart.getId())){
                continue;
            }
			//step 1.添加第一级部门
			departIdList.add(sysDepart.getId());
			sysDepart.setDepartNameUrl(sysDepart.getDepartName());
			exportDepartVoList.add(sysDepart);
			//step 2.添加自己部门路径，用/分离
			//创建路径
			List<String> path = new ArrayList<>();
			path.add(sysDepart.getDepartName());
			//创建子级部门路径
            // 代码逻辑说明: 【JHHB-222】导出，选中最顶级部门，只能导出选中的部门---
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
                sysDepart.setPositionId(departExportVo.getPositionId());
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
		List<SysDepartExportVo> departList = departMapper.getSysDepartList(departVo.getId(), tenantId, null);
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
            // 代码逻辑说明: 【JHHB-222】导出，选中最顶级部门，只能导出选中的部门---
			findSysDepartPath(exportDepartVo, cPath, tenantId, childrenDepart, departIdList);
        }
	}
	//========================end 系统下部门与人员导入 ==================================================================


    //=========================begin 部门岗位改造 ==================================================================
    @Override
    public List<SysPositionSelectTreeVo> getPositionByDepartId(String parentId, String departId, String positionId) {
        //step1 根据职级获取当前岗位的级别
        SysPosition sysPosition = sysPositionMapper.selectById(positionId);
        if(null == sysPosition){
            return null;
        }
        Integer postLevel = sysPosition.getPostLevel();
        //先获取上级部门的信息
        SysDepart sysDepart = baseMapper.getDepartById(parentId);
        //step2 如果是总公司 即数据为空的时候，则说明没有上级领导了
        if (null == sysDepart) {
            return null;
        }
        
        //可能是老数据
        if(null == postLevel){
            throw new JeecgBootBizTipException("当前选择职级的职务等级为空，请前往职务管理进行修改！");
        }
        return this.getParentDepartPosition(sysDepart, postLevel, departId);
    }
    
    /*
     * 获取上级部门岗位 或者当前部门下级别高的
     *
     * @param sysDepart
     * @param postLevel
     * @param id
     * @return
     */
    private List<SysPositionSelectTreeVo> getParentDepartPosition(SysDepart sysDepart, Integer postLevel, String id) {
        //step1 先获取上级部门下的数据
        //已经存在的code
        List<String> existCodeList = new ArrayList<>();
        List<SysPositionSelectTreeVo> departPosition = getDepartPosition(sysDepart, postLevel, id, existCodeList);
        //step2 获取上级部门信息，一直获取到子公司或者总公司为止
        //父级id不为空并且当前部门不是子公司或者总公司，则需要寻上顶级公司
        // 代码逻辑说明: 【JHHB-501】三级子公司的董事长岗位，存在向一级公司总经理汇报的职级关系，现在无法配置---
        String parentId = sysDepart.getParentId();
        SysDepart depart = departMapper.getDepartById(parentId);
        if(null != depart){
            //获取长度
            int codeNum = YouBianCodeUtil.ZHANWEI_LENGTH;
            List<String> codeList = getCodeHierarchy(depart.getOrgCode(), codeNum);
            if(null != codeList && codeList.size() > 1){
                //需要将当前和上级部门的排除掉
                existCodeList.add(codeList.get(codeList.size() - 1));
                //从上向下找存在子级的code
                List<SysPositionSelectTreeVo> parentDepartPost = this.getParentDepartPost(codeList, existCodeList);
                //当前父级部门下存在职级必当前高的需要同事渲染
                if (CollectionUtil.isNotEmpty(departPosition)) {
                    departPosition.addAll(parentDepartPost);
                    return buildTree(departPosition);
                }else if(CollectionUtil.isNotEmpty(parentDepartPost)){
                    return buildTree(parentDepartPost);
                }
            }
        }
        // 代码逻辑说明: 【JHHB-501】三级子公司的董事长岗位，存在向一级公司总经理汇报的职级关系，现在无法配置---
        if(CollectionUtil.isNotEmpty(departPosition)){
            return getSuperiorCompany(departPosition);
        }
        return null;
    }

    /**
     * 获取上级公司
     *
     * @param departPosition
     */
    private List<SysPositionSelectTreeVo> getSuperiorCompany(List<SysPositionSelectTreeVo> departPosition) {
        String parentId = departPosition.get(0).getParentId();
        SysDepart depart = baseMapper.getDepartById(parentId);
        if (null == depart) {
            return departPosition;
        }
        List<SysPositionSelectTreeVo> childrenList = new ArrayList<>();
        SysPositionSelectTreeVo childrenTreeModel = new SysPositionSelectTreeVo(depart);
        childrenTreeModel.setChildren(departPosition);
        childrenList.add(childrenTreeModel);
        if (DepartCategoryEnum.DEPART_CATEGORY_COMPANY.getValue().equals(depart.getOrgCategory()) || DepartCategoryEnum.DEPART_CATEGORY_SUB_COMPANY.getValue().equals(depart.getOrgCategory())) {
            return childrenList;
        } else {
            return this.getSuperiorCompany(childrenList);
        }
    }

    /**
     * 获取父级部门下的一级岗位
     *
     * @param codeList
     * @param existCodeList 已存在的部门编码
     */
    private List<SysPositionSelectTreeVo> getParentDepartPost(List<String> codeList, List<String> existCodeList) {
        List<SysPositionSelectTreeVo> list = new ArrayList<>();
        for (String orgCode : codeList){
            if(existCodeList.contains(orgCode)){
                continue;
            }
            //当前部门
            SysDepart depart = departMapper.queryDepartByOrgCode(orgCode);
            SysPositionSelectTreeVo sysPositionSelectTreeVo = new SysPositionSelectTreeVo(depart);
            sysPositionSelectTreeVo.setLeaf(false);
            list.add(sysPositionSelectTreeVo);
            //查找当前部门下一级部门存在岗位的部门信息
            List<SysDepart> departByParentId = departMapper.getDepartByParentId(depart.getId());
            List<String> parentIds = new ArrayList<>();
            for (SysDepart sysDepart : departByParentId) {
                list.add(new SysPositionSelectTreeVo(sysDepart));
                // 代码逻辑说明: 上级岗位太慢，sql优化---
                parentIds.add(sysDepart.getId());
            }
            // 代码逻辑说明: 上级岗位太慢，sql优化---
            if(CollectionUtil.isNotEmpty(parentIds)){
                //根据父级id获取部门岗位信息
                List<SysDepart> departPositionList = departMapper.getDepartPositionByParentIds(parentIds);
                if(CollectionUtil.isNotEmpty(departPositionList)){
                    List<SysPositionSelectTreeVo> sysDepartTreeModels = sysDepartToTreeModel(departPositionList);
                    list.addAll(sysDepartTreeModels);
                }
            }
        }
        return list; 
    }

    /**
     * 获取部门职务
     *
     * @param sysDepart
     * @param postLevel
     * @param existCodeList
     */
    private List<SysPositionSelectTreeVo> getDepartPosition(SysDepart sysDepart, Integer postLevel, String id, List<String> existCodeList) {
        //step1 获取部门下的所有部门
        String parentId = sysDepart.getParentId();
        List<SysDepart> departList = baseMapper.getDepartByParentId(parentId);
        List<SysPositionSelectTreeVo> treeModels = new ArrayList<>();
        for (int i = 0; i < departList.size(); i++) {
            SysDepart depart = departList.get(i);
            existCodeList.add(depart.getOrgCode());
            //如果是叶子节点说明没有岗位直接跳出循环
            if (depart.getIzLeaf() == 1) {
                if (DepartCategoryEnum.DEPART_CATEGORY_POST.getValue().equals(depart.getOrgCategory())) {
                    SysPositionSelectTreeVo sysDepartTreeModel = new SysPositionSelectTreeVo(depart);
                    treeModels.add(sysDepartTreeModel);
                }
                continue;
            }
            //step2 查找子部门下大于当前职别的数据
            List<SysDepart> departParentPosition = baseMapper.getDepartPositionByParentId(depart.getId(), postLevel, id);
            if (CollectionUtil.isNotEmpty(departParentPosition)) {
                List<SysPositionSelectTreeVo> sysDepartTreeModels = sysDepartToTreeModel(departParentPosition);
                SysPositionSelectTreeVo parentDepartTree = new SysPositionSelectTreeVo(depart);
                parentDepartTree.setChildren(sysDepartTreeModels);
                treeModels.add(parentDepartTree);
            }
        }
        return treeModels;
    }

    /**
     * 将SysDepart中的属性转到SysDepartTreeModel中
     *
     * @return
     */
    private List<SysPositionSelectTreeVo> sysDepartToTreeModel(List<SysDepart> sysDeparts) {
        List<SysPositionSelectTreeVo> records = new ArrayList<>();
        for (int i = 0; i < sysDeparts.size(); i++) {
            SysDepart depart = sysDeparts.get(i);
            SysPositionSelectTreeVo treeModel = new SysPositionSelectTreeVo(depart);
            records.add(treeModel);
        }
        return records;
    }

    /**
     * 获取公司或者子公司的id
     *
     * @param parentDepartId
     * @return
     */
    private String getCompanyDepartId(String parentDepartId) {
        SysDepart sysDepart = baseMapper.getDepartById(parentDepartId);
        if (sysDepart != null) {
            if (DepartCategoryEnum.DEPART_CATEGORY_COMPANY.getValue().equals(sysDepart.getOrgCategory()) || DepartCategoryEnum.DEPART_CATEGORY_SUB_COMPANY.getValue().equals(sysDepart.getOrgCategory())) {
                return sysDepart.getId();
            }
            //如果不是公司或者子公司的时候，需要递归查询
            if (oConvertUtils.isNotEmpty(sysDepart.getParentId())) {
                return getCompanyDepartId(sysDepart.getParentId());
            } else {
                return parentDepartId;
            }
        } else {
            return "";
        }
    }

    @Override
    public List<SysPositionSelectTreeVo> getRankRelation(String departId) {
        //记录当前部门 key为部门id,value为部门名称
        Map<String, String> departNameMap = new HashMap<>(5);
        //step1 根据id查询部门信息
        SysDepartPositionVo sysDepartPosition = baseMapper.getDepartPostByDepartId(departId);
        if (null == sysDepartPosition) {
            throw new JeecgBootBizTipException("当前所选部门数据为空");
        }
        List<SysPositionSelectTreeVo> selectTreeVos = new ArrayList<>();
        //step2 查看是否有子级部门，存在递归查询职位
        if (!CommonConstant.IS_LEAF.equals(sysDepartPosition.getIzLeaf())) {
            //获取子级职位根据部门编码
            this.getChildrenDepartPositionByOrgCode(selectTreeVos, departNameMap, sysDepartPosition);
            return buildTree(selectTreeVos);
        }
        return new ArrayList<>();
    }

    /**
     * 获取子级职位根据部门编码
     *
     * @param selectTreeVos
     * @param departNameMap
     * @param sysDepartPosition
     */
    private void getChildrenDepartPositionByOrgCode(List<SysPositionSelectTreeVo> selectTreeVos, Map<String, String> departNameMap, SysDepartPositionVo sysDepartPosition) {
        String orgCode = sysDepartPosition.getOrgCode();
        //step1 根据父级id获取子级部门信息
        List<SysDepartPositionVo> positionList = baseMapper.getDepartPostByOrgCode(orgCode + "%");
        if (CollectionUtil.isNotEmpty(positionList)) {
            for (SysDepartPositionVo position : positionList) {
                //初始化map
                if (departNameMap == null) {
                    departNameMap = new HashMap<>(5);
                }
                SysDepart depart = baseMapper.getDepartById(position.getParentId());
                if(null != depart){
                    position.setDepartName(depart.getDepartName());
                }
                if(oConvertUtils.isNotEmpty(position.getDepPostParentId())){
                    LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
                    query.eq(SysDepart::getId,position.getDepPostParentId());
                    query.likeRight(SysDepart::getOrgCode,orgCode);
                    Long count = baseMapper.selectCount(query);
                    if(null== count || count == 0){
                        position.setDepPostParentId(null);
                    }
                }
                departNameMap.put(position.getParentId(), position.getPositionName());
                //查看是否为部门岗位，不是则不需要处理
                SysPositionSelectTreeVo treeVo = new SysPositionSelectTreeVo(position);
                selectTreeVos.add(treeVo);
            }
        }
    }


    /**
     * 构建树形结构，只返回没有父级的一级节点
     */
    public static List<SysPositionSelectTreeVo> buildTree(List<SysPositionSelectTreeVo> nodes) {
        // 1. 去重：根据ID去重，保留第一个
        Map<String, SysPositionSelectTreeVo> uniqueNodes = nodes.stream()
                .filter(Objects::nonNull)
                .filter(node -> node.getId() != null && !node.getId().trim().isEmpty())
                .collect(Collectors.toMap(
                        SysPositionSelectTreeVo::getId,
                        node -> node,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
        // 2. 初始化所有节点的children列表
        uniqueNodes.values().forEach(node -> {
            if (node.getChildren() == null) {
                node.setChildren(new ArrayList<>());
            }
        });
        // 3. 构建树形结构
        List<SysPositionSelectTreeVo> rootNodes = new ArrayList<>();
        for (SysPositionSelectTreeVo node : uniqueNodes.values()) {
            String parentId = node.getParentId();

            if (parentId == null || parentId.trim().isEmpty()) {
                // 根节点
                rootNodes.add(node);
            } else {
                // 子节点，查找父节点
                SysPositionSelectTreeVo parent = uniqueNodes.get(parentId);
                if (parent != null) {
                    parent.getChildren().add(node);
                } else {
                    // 父节点不存在，当作根节点处理
                    rootNodes.add(node);
                }
            }
        }
        return rootNodes;
    }

    //=========================end 部门岗位改造 ==================================================================

    @Override
    public String getDepartPathNameByOrgCode(String orgCode, String depId) {
        //部门id为空需要查询当前部门下的编码
        if(oConvertUtils.isNotEmpty(depId)){
            SysDepart departById = baseMapper.getDepartById(depId);
            if(null != departById){
                orgCode = departById.getOrgCode();
            }
        }
        if(oConvertUtils.isEmpty(orgCode)){
            return "";
        }
        //从redis 获取不为空直接返回
        Object departName  = redisUtil.get(CommonConstant.DEPART_NAME_REDIS_KEY_PRE + orgCode);
        if(null != departName){
            return String.valueOf(departName);
        }
        //获取长度
        int codeNum = YouBianCodeUtil.ZHANWEI_LENGTH;
        List<String> list = this.getCodeHierarchy(orgCode, codeNum);
        //根据部门编码查询公司和子公司的数据
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
        query.in(SysDepart::getOrgCode, list);
        query.orderByAsc(SysDepart::getOrgCode);
        List<SysDepart> sysDepartList = departMapper.selectList(query);
        if(!CollectionUtils.isEmpty(sysDepartList)){
            //获取部门名称拼接返回给前台
            // 代码逻辑说明: 【JHHB-631】【部门管理】存在缩写使用缩写来显示---
            List<String> departNameList = sysDepartList.stream().map(item-> oConvertUtils.getString(item.getDepartNameAbbr(),item.getDepartName())).toList();
            String departNames = String.join("/", departNameList);
            redisUtil.set(CommonConstant.DEPART_NAME_REDIS_KEY_PRE + orgCode,departNames);
            return departNames;
        }
        return "";
    }
    
    /**
     * 获取编码及其所有上级编码
     * 
     * @param code 完整编码，如 "A01A01A01"
     * @param fixedLength 固定位数，如 3
     * @return 包含所有上级编码的列表，如 ['A01','A01A01','A01A01A01']
     */
    public List<String> getCodeHierarchy(String code, int fixedLength) {
        List<String> hierarchy = new ArrayList<>();
        if (code == null || code.isEmpty() || fixedLength <= 0) {
            return hierarchy;
        }
        // 检查编码长度是否能被固定位数整除
        if (code.length() % fixedLength != 0) {
            throw new IllegalArgumentException("编码长度必须能被固定位数整除");
        }
        // 按固定位数分割并生成所有上级编码
        for (int i = fixedLength; i <= code.length(); i += fixedLength) {
            hierarchy.add(code.substring(0, i));
        }
        return hierarchy;
    }

    /**
     * 根据多个部门id删除主岗位和兼职岗位
     * 
     * @param idList
     */
    private void deleteDepartPostByDepIds(List<String> idList) {
        //更新用户主岗位位空，使用LambdaUpdateWrapper，避免为空时受全局 updateStrategy 影响导致误更新
        LambdaUpdateWrapper<SysUser> userQuery = new LambdaUpdateWrapper<>();
        userQuery.in(SysUser::getMainDepPostId, idList);
        userQuery.set(SysUser::getMainDepPostId, null);
        sysUserMapper.update(userQuery);
        //删除兼职岗位
        LambdaQueryWrapper<SysUserDepPost> postQuery = new LambdaQueryWrapper<>();
        postQuery.in(SysUserDepPost::getDepId, idList);
        sysUserDepPostMapper.delete(postQuery);
        //redis清除缓存key
        redisUtil.removeAll(CommonConstant.DEPART_NAME_REDIS_KEY_PRE);
    }

    /**
     * 根据部门id获取部门下的岗位id
     *
     * @param depIds 当前选择的公司、子公司、部门id
     * @return
     */
    @Override
    public String getDepPostIdByDepId(String depIds) {
        if (oConvertUtils.isEmpty(depIds)) {
            return "";
        }
        //step1 先根据部门id获取编码
        List<SysUserDepVo> departIdList = departMapper.getDepartByIds(Arrays.asList(depIds.split(SymbolConstant.COMMA)));
        if (CollectionUtil.isNotEmpty(departIdList)) {
            //step2 根据部门编码查询岗位id
            LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
            query.select(SysDepart::getId);
            departIdList.forEach(item -> {
                query.or(lq-> lq.likeRight(SysDepart::getOrgCode, item.getOrgCode()));
            });
            query.eq(SysDepart::getOrgCategory, DepartCategoryEnum.DEPART_CATEGORY_POST.getValue());
            List<SysDepart> departList = departMapper.selectList(query);
            //step3 返回部门id
            if (CollectionUtil.isNotEmpty(departList)) {
                return departList.stream().map(SysDepart::getId).collect(Collectors.joining(SymbolConstant.COMMA));
            }
        }
        return "";
    }

    /**
     * 变更部门位置
     * 
     * @param changeDepartVo
     * @return orgCode 部门id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateChangeDepart(SysChangeDepartVo changeDepartVo) {
        String dragId = changeDepartVo.getDragId();
        // 1. 获取被拖拽的部门
        SysDepart dragDept = baseMapper.getDepartById(dragId);
        if (null == dragDept) {
            throw new JeecgBootBizTipException("被拖拽的部门不存在");
        }
        // 2. 获取目标部门
        String dropId = changeDepartVo.getDropId();
        SysDepart targetDept = baseMapper.getDepartById(dropId);
        if (null == targetDept) {
            throw new JeecgBootBizTipException("目标部门不存在");
        }
        //3. 验证拖拽操作是否合法
        validateDragOperation(dragDept, targetDept, changeDepartVo.getDropPosition());
        //4. 根据dropPosition调整部门顺序
        Integer dropPosition = changeDepartVo.getDropPosition();
        switch (dropPosition) {
            case -1:
                // 拖拽到上方
                moveToAbove(dragDept, targetDept);
                break;
            case 0:
                // 拖拽到内部（作为子部门）
                moveAsChild(dragDept, targetDept);
                break;
            case 1:
                //拖拽到下方
                moveToBelow(dragDept, targetDept, changeDepartVo.getSort());
                break;
            default:
                throw new RuntimeException("无效的拖拽位置");
        }
        //5. 清空缓存
        redisUtil.removeAll(CommonConstant.DEPART_NAME_REDIS_KEY_PRE);
    }

    /**
     * 验证拖拽操作是否合法
     *
     * @param dragDept     被拖拽的部门
     * @param targetDept   目标部门
     * @param dropPosition 拖拽位置
     */
    private void validateDragOperation(SysDepart dragDept, SysDepart targetDept, Integer dropPosition) {
        // 禁止拖拽到自身
        if (dragDept.getId().equals(targetDept.getId())) {
            throw new RuntimeException("不能拖拽到自身");
        }
        // 禁止拖拽到自身子部门
        if (isDescendant(dragDept, targetDept.getId())) {
            throw new RuntimeException("不能拖拽到自身子部门");
        }
        //公司岗位判断
        String orgCategory = targetDept.getOrgCategory();
        String oldOrgCategory = dragDept.getOrgCategory();
        //部门为公司
        if(0 != dropPosition && DepartCategoryEnum.DEPART_CATEGORY_COMPANY.getValue().equals(orgCategory)){
            //当前部门不能为子公司、部门和岗位
            if(!DepartCategoryEnum.DEPART_CATEGORY_COMPANY.getValue().equals(oldOrgCategory)){
                throw new JeecgBootBizTipException("当前部门类型为【"+DepartCategoryEnum.getNameByValue(oldOrgCategory)+"】,不允许移动到公司");
            }
        }
        //部门为岗位不允许移入
        if(0 == dropPosition && DepartCategoryEnum.DEPART_CATEGORY_POST.getValue().equals(orgCategory)) {
            throw new JeecgBootBizTipException("岗位不允许存在子级");
        }
        //公司不能做为子级
        if(oConvertUtils.isNotEmpty(targetDept.getParentId()) && DepartCategoryEnum.DEPART_CATEGORY_COMPANY.getValue().equals(oldOrgCategory)){
            throw new JeecgBootBizTipException("公司不允许作为子级");
        }
    }

    /**
     * 判断目标部门是否是被拖拽部门的子部门
     */
    private boolean isDescendant(SysDepart dragDept, String targetId) {
        List<SysDepart> children = departMapper.getDepartByParentId(dragDept.getId());
        for (SysDepart child : children) {
            if (child.getId().equals(targetId)) {
                return true;
            }
            if (isDescendant(child, targetId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 拖拽到上方：将部门移动到目标部门上方(只有最上级 即公司才会走这个逻辑)
     * @param dragDept   被拖拽的部门
     * @param targetDept 目标部门
     */
    private void moveToAbove(SysDepart dragDept, SysDepart targetDept) {
        // 获取目标部门同级的所有部门
        List<SysDepart> siblings = departMapper.getDepartNoParent();
        // 计算新的排序值
        Integer newDepartOrder = targetDept.getDepartOrder();
        // 更新被拖拽部门的排序值
        dragDept.setDepartOrder(newDepartOrder);
        // 更新被拖拽部门的排序值
        dragDept.setDepartOrder(0);
        if(CollectionUtil.isNotEmpty(siblings)){
            // 计算新的排序值
            this.computingSort(siblings,0,dragDept.getId());
            // 保存所有更新的部门
            this.updateBatchById(siblings);
        }
        departMapper.updateById(dragDept);
    }

    /**
     * 拖拽到下方：将部门移动到目标部门下方
     *
     * @param dragDept   被拖拽的部门
     * @param targetDept 目标部门
     * @param sort 排序
     */
    private void moveToBelow(SysDepart dragDept, SysDepart targetDept, Integer sort) {
        String parentId = targetDept.getParentId();
        List<SysDepart> siblings = null;
        if(oConvertUtils.isNotEmpty(parentId)){
            // 获取目标部门同级的所有部门
            siblings = departMapper.getDepartByParentId(parentId);
        }else{
            siblings = departMapper.getDepartNoParent(); 
        }
        String oldParentId = dragDept.getParentId();
        //判断父级部门id是否相同，不同则更新为目标部门的父部门id
        if(oConvertUtils.isNotEmpty(dragDept.getParentId()) &&
                oConvertUtils.isNotEmpty(parentId) &&
                !dragDept.getParentId().equals(parentId)){
            String oldOrgCode = dragDept.getOrgCode();
            //设置父级id和部门code
            this.setDepartParentAndOrgCode(dragDept, parentId);
            //修改子级的部门编码
            this.updateChildOrgCode(dragDept.getOrgCode(), oldOrgCode);
        }
        // 更新被拖拽部门的排序值
        dragDept.setDepartOrder(sort);
        if(CollectionUtil.isNotEmpty(siblings)){
            // 计算新的排序值
            this.computingSort(siblings,sort,dragDept.getId());
            // 保存所有更新的部门
            this.updateBatchById(siblings);
        }
        departMapper.updateById(dragDept);
        if(oConvertUtils.isNotEmpty(oldParentId)){
            long count = departMapper.countByParentId(oldParentId);
            if(count == 0){
                this.updateIzLeaf(oldParentId,CommonConstant.IS_LEAF);
            }
        }
    }
    
    /**
     * 拖拽到内部：将部门移动到目标部门内部（作为子部门）
     */
    private void moveAsChild(SysDepart dragDept, SysDepart targetDept) {
        // 更新父部门ID
        String parentId = targetDept.getId();
        String oldParentId = dragDept.getParentId();
        // 获取目标部门同级的所有部门
        List<SysDepart> siblings = departMapper.getDepartByParentId(parentId);
        //判断父级部门id是否相同，不同则更新为目标部门的父部门id
        if(oConvertUtils.isNotEmpty(dragDept.getParentId()) &&
                oConvertUtils.isNotEmpty(parentId) &&
                !dragDept.getParentId().equals(parentId)){
            String oldOrgCode = dragDept.getOrgCode();
            //设置父级id和部门code
            this.setDepartParentAndOrgCode(dragDept, parentId);
            //修改子级的部门编码
            this.updateChildOrgCode(dragDept.getOrgCode(), oldOrgCode);
        }
        //内部排序为0
        Integer sort = 0;
        // 设置新的排序值
        dragDept.setDepartOrder(sort);
        if(CollectionUtil.isNotEmpty(siblings)){
            // 计算新的排序值
            this.computingSort(siblings,sort,dragDept.getId());
            // 保存所有更新的部门
            this.updateBatchById(siblings);
        }
        departMapper.updateById(dragDept);
        this.updateIzLeaf(parentId, CommonConstant.NOT_LEAF);
        if(oConvertUtils.isNotEmpty(oldParentId)){
            long count = departMapper.countByParentId(oldParentId);
            if(count == 0){
                this.updateIzLeaf(oldParentId,CommonConstant.IS_LEAF);
            }
        }
    }

    /**
     * 计算排序值
     *
     * @param siblings
     * @param sort
     * @param id
     */
    private void computingSort(List<SysDepart> siblings, Integer sort, String id) {
        for (int i = 0; i < siblings.size(); i++) {
            SysDepart depart = siblings.get(i);
            if(id.equals(depart.getId())){
                continue;
            }
            //如果当前循环的sort大等于传入的sort值 则需要+1
            if(i >= sort){
                depart.setDepartOrder(sort + 1);
                sort++;
            } else {
                depart.setDepartOrder(i);
            }
        }
    }

    /**
     * 设置被拖拽部门的父级id和部门编码
     *
     * @param dragDept 被拖拽的部门
     * @param parentId 目标部门的父级id
     */
    private void setDepartParentAndOrgCode(SysDepart dragDept, String parentId) {
        // 更新父部门ID（与目标部门相同）
        dragDept.setParentId(parentId);
        Page<SysDepart> page = new Page<>(1, 1);
        //需要获取父级id，查看父级是否已经存在
        //获取一级部门的最大orgCode
        List<SysDepart> records = departMapper.getMaxCodeDepart(page, parentId);
        String newOrgCode = "";
        if (CollectionUtil.isNotEmpty(records)) {
            newOrgCode = YouBianCodeUtil.getNextYouBianCode(records.get(0).getOrgCode());
        } else {
            //查询父id
            if (oConvertUtils.isNotEmpty(parentId)) {
                SysDepart departById = departMapper.getDepartById(parentId);
                newOrgCode = YouBianCodeUtil.getSubYouBianCode(departById.getOrgCode(), null);
            } else {
                newOrgCode = YouBianCodeUtil.getNextYouBianCode(null);
            }
        }
        dragDept.setOrgCode(newOrgCode);
    }

    /**
     * 修改子级的部门编码
     *
     * @param newOrgCode 当前父级新的部门编码
     * @param oldOrgCode 当前父级旧的部门编码
     */
    private void updateChildOrgCode(String newOrgCode, String oldOrgCode) {
        //查询当前部门下的所有子级部门
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
        query.likeRight(SysDepart::getOrgCode, oldOrgCode);
        query.orderByAsc(SysDepart::getDepartOrder);
        query.orderByDesc(SysDepart::getCreateTime);
        query.select(SysDepart::getId, SysDepart::getOrgCode);
        List<SysDepart> childDeparts = departMapper.selectList(query);
        if (CollectionUtil.isNotEmpty(childDeparts)) {
            for (SysDepart depart : childDeparts) {
                String orgCode = depart.getOrgCode();
                if (orgCode.startsWith(oldOrgCode)) {
                    orgCode = newOrgCode + orgCode.substring(oldOrgCode.length());
                }
                depart.setOrgCode(orgCode);
            }
        }
        this.updateBatchById(childDeparts);
    }

    /**
     * 获取部门负责人
     *
     * @param departId
     * @param page
     * @return
     */
    @Override
    public IPage<SysUser> getDepartmentHead(String departId, Page<SysUser> page) {
        List<SysUser> departmentHead = departMapper.getDepartmentHead(page, departId);
        if(CollectionUtil.isNotEmpty(departmentHead)){
            departmentHead.forEach(item->{
                //兼职岗位
                List<String> depPostList = sysUserDepPostMapper.getDepPostByUserId(item.getId());
                if(CollectionUtil.isNotEmpty(depPostList)){
                    item.setOtherDepPostId(StringUtils.join(depPostList.toArray(), SymbolConstant.COMMA));
                }
            });
        }
        return page.setRecords(departmentHead);
    }
}
