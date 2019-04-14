package org.jeecg.modules.online.cgreport.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgreport.def.CgReportConstant;
import org.jeecg.modules.online.cgreport.entity.OnlCgreportHead;
import org.jeecg.modules.online.cgreport.entity.OnlCgreportItem;
import org.jeecg.modules.online.cgreport.entity.OnlCgreportParam;
import org.jeecg.modules.online.cgreport.mapper.OnlCgreportHeadMapper;
import org.jeecg.modules.online.cgreport.model.OnlCgreportModel;
import org.jeecg.modules.online.cgreport.service.IOnlCgreportHeadService;
import org.jeecg.modules.online.cgreport.service.IOnlCgreportItemService;
import org.jeecg.modules.online.cgreport.service.IOnlCgreportParamService;
import org.jeecg.modules.online.cgreport.util.SqlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 在线报表配置
 * @author: jeecg-boot
 * @date: 2019-03-08
 * @version: V1.0
 */
@Service
public class OnlCgreportHeadServiceImpl extends ServiceImpl<OnlCgreportHeadMapper, OnlCgreportHead> implements IOnlCgreportHeadService {

	@Autowired
	private IOnlCgreportParamService onlCgreportParamService;
	@Autowired
	private IOnlCgreportItemService onlCgreportItemService;

	@Autowired
	OnlCgreportHeadMapper mapper;

	/**
	 * 执行 select sql语句
	 * 
	 * @param sql
	 */
	@Override
	public Map<String, Object> executeSelectSql(String sql,Map<String,Object> params) {
		Map<String, Object> result = new HashMap<>();
		
		//1、分页查询逻辑
		Integer pageSize = oConvertUtils.getInt(params.get("pageSize"),10);
		Integer pageNo = oConvertUtils.getInt(params.get("pageNo"),1);
		String pagesql = SqlUtil.jeecgCreatePageSql(sql,null,pageNo,pageSize);
		
		//2、排序逻辑
		//TODO
		
		//TODO 页面查询条件未实现
		String countSql = SqlUtil.getCountSql(sql, null);
		Long cl = mapper.queryCountBySql(countSql);
		
		result.put("total", cl);
		result.put("records", mapper.executeSelete(pagesql));
		return result;
	}

	/** 修改全部项，包括新增、修改、删除 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Result<?> editAll(OnlCgreportModel values) {
		OnlCgreportHead head = values.getHead();

		OnlCgreportHead onlCgreportHeadEntity = super.getById(head.getId());
		if (onlCgreportHeadEntity == null) {
			return Result.error("未找到对应实体");
		}

		super.updateById(head);

		List<OnlCgreportParam> params = values.getParams();
		List<OnlCgreportItem> items = values.getItems();

		// 将param分类成 添加和修改 两个列表
		List<OnlCgreportParam> addParam = new ArrayList<OnlCgreportParam>();
		List<OnlCgreportParam> editParam = new ArrayList<OnlCgreportParam>();
		for (OnlCgreportParam param : params) {
			String id = String.valueOf(param.getId());

			if (id.length() == 32) {
				// 修改项
				editParam.add(param);

			} else {
				// 新增项
				param.setId(null);
				param.setCgrheadId(head.getId());
				addParam.add(param);
			}
		}
		onlCgreportParamService.saveBatch(addParam);

		// 使用 updateBatchById 会报错，原因未知，具体错误：
		// updateBatch Parameter 'param1' not found. Available parameters are
		// [et]

		for (OnlCgreportParam edit : editParam) {
			onlCgreportParamService.updateById(edit);
		}

		// 将item分类
		List<OnlCgreportItem> addItem = new ArrayList<OnlCgreportItem>();
		List<OnlCgreportItem> editItem = new ArrayList<OnlCgreportItem>();
		for (OnlCgreportItem item : items) {
			String id = String.valueOf(item.getId());

			if (id.length() == 32) {
				// 修改项
				editItem.add(item);

			} else {
				// 新增项
				item.setId(null);
				item.setCgrheadId(head.getId());
				addItem.add(item);
			}
		}
		onlCgreportItemService.saveBatch(addItem);

		for (OnlCgreportItem edit : editItem) {
			onlCgreportItemService.updateById(edit);
		}

		// 删除项
		onlCgreportParamService.removeByIds(values.getDeleteParamIdList());
		onlCgreportItemService.removeByIds(values.getDeleteItemIdList());

		return Result.ok("全部修改成功");

	}
	
	
	/**
	 * 动态数据源： 获取SQL解析的字段
	 */
	@Override
	public List<String> getSqlFields(String sql,String dbKey){
		List<String> fields = null;
		sql = SqlUtil.getSql(sql);
		if(StringUtils.isNotBlank(dbKey)){
			fields = this.parseSql(sql,dbKey);
		}else{
			fields = this.parseSql(sql,null);
		}
		return fields;
	}

	
	/**
	 * 解析SQL参数
	 */
	@Override
	public List<String> getSqlParams(String sql) {
		if(oConvertUtils.isEmpty(sql)){
			return null;
		}
		List<String> params = new ArrayList<String>();
		String regex = "\\$\\{\\w+\\}";
		
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(sql);
		while(m.find()){
			String whereParam = m.group();
			params.add(whereParam.substring(whereParam.indexOf("{")+1,whereParam.indexOf("}")));
		}
		return params;
	}
	
	/**
	 * 未实现动态数据源的逻辑
	 * @param sql
	 * @param dbKey
	 * @return
	 */
	private List<String> parseSql(String sql,String dbKey) {
		if(oConvertUtils.isEmpty(sql)){
			return null;
		}
		//封装分页SQL
		int page = 1;
		int rows = 1;
		String pagesql = SqlUtil.jeecgCreatePageSql(sql,null,page,rows);
		List<Map<?, ?>> result = mapper.executeSelete(pagesql);
		if(result.size()<1){
			throw new JeecgBootException("该报表sql没有数据");
		}
		Set fieldsSet= result.get(0).keySet();
		List<String> fileds = new ArrayList<String>(fieldsSet);
		return fileds;
	}
	
	
	@Override
	public Map<String, Object> queryCgReportConfig(String reportId) {
		Map<String,Object> cgReportM = new HashMap<String, Object>(0);
		Map<String,Object> mainM = mapper.queryCgReportMainConfig(reportId);
		List<Map<String,Object>> itemsM = mapper.queryCgReportItems(reportId);
		List<String> params = mapper.queryCgReportParams(reportId);
		cgReportM.put(CgReportConstant.MAIN, mainM);
		cgReportM.put(CgReportConstant.ITEMS, itemsM);
		cgReportM.put(CgReportConstant.PARAMS, params);
		return cgReportM;
	}
	
	/**
	 * 分页查询报表数据
	 */
	@Override
	public List<Map<?, ?>> queryByCgReportSql(String sql, Map params,Map paramData,
			int page, int rows) {
		String querySql = SqlUtil.getFullSql(sql,params);
		List<Map<?, ?>> result = null;
		if(paramData!=null&&paramData.size()==0){
			paramData = null;
		}
		if(page==-1 && rows==-1){
			//TODO 根据sql,paramData，转换成真正的SQL
			result = mapper.executeSelete(querySql);
		}else{
			//TODO 根据sql,paramData，转换成真正的SQL
			String pagesql = SqlUtil.jeecgCreatePageSql(sql,paramData,page,rows);
			result = mapper.executeSelete(pagesql);
		}
		return result;
	}
}
