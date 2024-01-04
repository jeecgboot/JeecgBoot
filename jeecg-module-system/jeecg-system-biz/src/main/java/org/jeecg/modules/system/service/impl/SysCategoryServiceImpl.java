package org.jeecg.modules.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.constant.FillRuleConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.FillRuleUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysCategory;
import org.jeecg.modules.system.mapper.SysCategoryMapper;
import org.jeecg.modules.system.model.TreeSelectModel;
import org.jeecg.modules.system.service.ISysCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 分类字典
 * @Author: jeecg-boot
 * @Date:   2019-05-29
 * @Version: V1.0
 */
@Service
public class SysCategoryServiceImpl extends ServiceImpl<SysCategoryMapper, SysCategory> implements ISysCategoryService {

	@Override
	public void addSysCategory(SysCategory sysCategory) {
		String categoryCode = "";
		String categoryPid = ISysCategoryService.ROOT_PID_VALUE;
		String parentCode = null;
		if(oConvertUtils.isNotEmpty(sysCategory.getPid())){
			categoryPid = sysCategory.getPid();

			//PID 不是根节点 说明需要设置父节点 hasChild 为1
			if(!ISysCategoryService.ROOT_PID_VALUE.equals(categoryPid)){
				SysCategory parent = baseMapper.selectById(categoryPid);
				parentCode = parent.getCode();
				if(parent!=null && !ISysCategoryService.HAS_CHILD.equals(parent.getHasChild())){
					parent.setHasChild(ISysCategoryService.HAS_CHILD);
					baseMapper.updateById(parent);
				}
			}
		}
		//update-begin--Author:baihailong  Date:20191209 for：分类字典编码规则生成器做成公用配置
		JSONObject formData = new JSONObject();
		formData.put("pid",categoryPid);
		categoryCode = (String) FillRuleUtil.executeRule(FillRuleConstant.CATEGORY,formData);
		//update-end--Author:baihailong  Date:20191209 for：分类字典编码规则生成器做成公用配置
		sysCategory.setCode(categoryCode);
		sysCategory.setPid(categoryPid);
		baseMapper.insert(sysCategory);
	}
	
	@Override
	public void updateSysCategory(SysCategory sysCategory) {
		if(oConvertUtils.isEmpty(sysCategory.getPid())){
			sysCategory.setPid(ISysCategoryService.ROOT_PID_VALUE);
		}else{
			//如果当前节点父ID不为空 则设置父节点的hasChild 为1
			SysCategory parent = baseMapper.selectById(sysCategory.getPid());
			if(parent!=null && !ISysCategoryService.HAS_CHILD.equals(parent.getHasChild())){
				parent.setHasChild(ISysCategoryService.HAS_CHILD);
				baseMapper.updateById(parent);
			}
		}
		baseMapper.updateById(sysCategory);
	}

	@Override
	public List<TreeSelectModel> queryListByCode(String pcode) throws JeecgBootException{
		String pid = ROOT_PID_VALUE;
		if(oConvertUtils.isNotEmpty(pcode)) {
			List<SysCategory> list = baseMapper.selectList(new LambdaQueryWrapper<SysCategory>().eq(SysCategory::getCode, pcode));
			if(list==null || list.size() ==0) {
				throw new JeecgBootException("该编码【"+pcode+"】不存在，请核实!");
			}
			if(list.size()>1) {
				throw new JeecgBootException("该编码【"+pcode+"】存在多个，请核实!");
			}
			pid = list.get(0).getId();
		}
		return baseMapper.queryListByPid(pid,null);
	}

	@Override
	public List<TreeSelectModel> queryListByPid(String pid) {
		if(oConvertUtils.isEmpty(pid)) {
			pid = ROOT_PID_VALUE;
		}
		return baseMapper.queryListByPid(pid,null);
	}

	@Override
	public List<TreeSelectModel> queryListByPid(String pid, Map<String, String> condition) {
		if(oConvertUtils.isEmpty(pid)) {
			pid = ROOT_PID_VALUE;
		}
		return baseMapper.queryListByPid(pid,condition);
	}

	@Override
	public String queryIdByCode(String code) {
		return baseMapper.queryIdByCode(code);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteSysCategory(String ids) {
		String allIds = this.queryTreeChildIds(ids);
		String pids = this.queryTreePids(ids);
		//1.删除时将节点下所有子节点一并删除
		this.baseMapper.deleteBatchIds(Arrays.asList(allIds.split(",")));
		//2.将父节点中已经没有下级的节点，修改为没有子节点
		if(oConvertUtils.isNotEmpty(pids)){
			LambdaUpdateWrapper<SysCategory> updateWrapper = new UpdateWrapper<SysCategory>()
					.lambda()
					.in(SysCategory::getId,Arrays.asList(pids.split(",")))
					.set(SysCategory::getHasChild,"0");
			this.update(updateWrapper);
		}
	}

	/**
	 * 查询节点下所有子节点
	 * @param ids
	 * @return
	 */
	private String queryTreeChildIds(String ids) {
		//获取id数组
		String[] idArr = ids.split(",");
		StringBuffer sb = new StringBuffer();
		for (String pidVal : idArr) {
			if(pidVal != null){
				if(!sb.toString().contains(pidVal)){
					if(sb.toString().length() > 0){
						sb.append(",");
					}
					sb.append(pidVal);
					this.getTreeChildIds(pidVal,sb);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 查询需修改标识的父节点ids
	 * @param ids
	 * @return
	 */
	private String queryTreePids(String ids) {
		StringBuffer sb = new StringBuffer();
		//获取id数组
		String[] idArr = ids.split(",");
		for (String id : idArr) {
			if(id != null){
				SysCategory category = this.baseMapper.selectById(id);
				//根据id查询pid值
				String metaPid = category.getPid();
				//查询此节点上一级是否还有其他子节点
				LambdaQueryWrapper<SysCategory> queryWrapper = new LambdaQueryWrapper<>();
				queryWrapper.eq(SysCategory::getPid,metaPid);
				queryWrapper.notIn(SysCategory::getId,Arrays.asList(idArr));
				List<SysCategory> dataList = this.baseMapper.selectList(queryWrapper);
                boolean flag = (dataList == null || dataList.size()==0) && !Arrays.asList(idArr).contains(metaPid)
                        && !sb.toString().contains(metaPid);
                if(flag){
					//如果当前节点原本有子节点 现在木有了，更新状态
					sb.append(metaPid).append(",");
				}
			}
		}
		if(sb.toString().endsWith(SymbolConstant.COMMA)){
			sb = sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * 递归 根据父id获取子节点id
	 * @param pidVal
	 * @param sb
	 * @return
	 */
	private StringBuffer getTreeChildIds(String pidVal,StringBuffer sb){
		LambdaQueryWrapper<SysCategory> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(SysCategory::getPid,pidVal);
		List<SysCategory> dataList = baseMapper.selectList(queryWrapper);
		if(dataList != null && dataList.size()>0){
			for(SysCategory category : dataList) {
				if(!sb.toString().contains(category.getId())){
					sb.append(",").append(category.getId());
				}
				this.getTreeChildIds(category.getId(), sb);
			}
		}
		return sb;
	}

	@Override
	public List<String> loadDictItem(String ids) {
		return this.loadDictItem(ids, true);
	}

	@Override
	public List<String> loadDictItem(String ids, boolean delNotExist) {
		String[] idArray = ids.split(",");
		LambdaQueryWrapper<SysCategory> query = new LambdaQueryWrapper<>();
		query.in(SysCategory::getId, Arrays.asList(idArray));
		// 查询数据
		List<SysCategory> list = super.list(query);
		// 取出name并返回
		List<String> textList;
		// update-begin--author:sunjianlei--date:20210514--for：新增delNotExist参数，设为false不删除数据库里不存在的key ----
		if (delNotExist) {
			textList = list.stream().map(SysCategory::getName).collect(Collectors.toList());
		} else {
			textList = new ArrayList<>();
			for (String id : idArray) {
				List<SysCategory> res = list.stream().filter(i -> id.equals(i.getId())).collect(Collectors.toList());
				textList.add(res.size() > 0 ? res.get(0).getName() : id);
			}
		}
		// update-end--author:sunjianlei--date:20210514--for：新增delNotExist参数，设为false不删除数据库里不存在的key ----
		return textList;
	}

	@Override
	public List<String> loadDictItemByNames(String names, boolean delNotExist) {
		List<String> nameList = Arrays.asList(names.split(SymbolConstant.COMMA));
		LambdaQueryWrapper<SysCategory> query = new LambdaQueryWrapper<>();
		query.select(SysCategory::getId, SysCategory::getName);
		query.in(SysCategory::getName, nameList);
		// 查询数据
		List<SysCategory> list = super.list(query);
		// 取出id并返回
		return nameList.stream().map(name -> {
			SysCategory res = list.stream().filter(i -> name.equals(i.getName())).findFirst().orElse(null);
			if (res == null) {
				return delNotExist ? null : name;
			}
			return res.getId();
		}).filter(Objects::nonNull).collect(Collectors.toList());
	}

}
