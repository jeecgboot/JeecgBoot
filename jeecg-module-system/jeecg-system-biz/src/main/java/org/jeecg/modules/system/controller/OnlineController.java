package org.jeecg.modules.system.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.dynamic.db.DynamicDBUtil;
import org.jeecg.config.LogUtil;
import org.jeecg.config.StringUtil;
import org.jeecg.config.ThreadUtil;
import org.jeecg.modules.quartz.job.AListJobUtil;
import org.jeecg.modules.system.service.ISysDictService;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Online表单、报表控制器
 */
@RestController
@RequestMapping("/onilne")
@Slf4j
public class OnlineController {

	private static final String dbKey = "jeecg-boot";

	private static Pattern bracketPat = Pattern.compile("\\((.*?)\\)");

	@Autowired
	private ISysDictService sysDictService;

	/**
	 * 查询表单默认宽度
	 * @param headId
	 * @return
	 */
	@RequestMapping(value = "/tableFieldList", method = RequestMethod.GET)
	public Result<JSONObject> selectFormField(@RequestParam(name="headId", required=true) String headId) {
		Result<JSONObject> result = new Result<JSONObject>();
		String sql = "select * from onl_table_field_v where head_id=?";
		List<Map<String, Object>> fieldList = DynamicDBUtil.findList(dbKey,sql,headId);
		JSONObject obj = new JSONObject();
		obj.put("data",fieldList);
		result.setResult(obj);
		return result;
	}

	@PostMapping("/add")
	public Result addEnhance(@RequestBody JSONObject params) throws JobExecutionException {
		String tableName = params.getString("tableName");
		JSONObject data = params.getJSONObject("record");
		addRecord(tableName, data);
		return Result.OK(params);
	}

	@PostMapping("/edit")
	public Result editEnhance(@RequestBody JSONObject params) throws JobExecutionException {
		String tableName = params.getString("tableName");
		JSONObject data = params.getJSONObject("record");
		updateRecord(tableName, data);
		return Result.OK(params);
	}

	@PostMapping("/table/edit")
	public Result editTableEnhance(@RequestBody JSONObject params) throws JobExecutionException {
		LogUtil.startTime("editTableEnhance");
		String tableName = params.getString("tableName");
		JSONObject data = params.getJSONObject("record");
		updateRecord(tableName, data);
		ThreadUtil.execute(() -> {
			DynamicDBUtil.execute(dbKey,"call update_onltable_headid()");
		});
		LogUtil.endTime("editTableEnhance");
		return Result.OK(params);
	}

	/**
	 * 保存分享链接-增强
	 * @param params
	 * @return
	 */
	@PostMapping("/alist/shares/edit")
	public Result editAListStoragesEnhance(@RequestBody JSONObject params) throws JobExecutionException {
		LogUtil.startTime("editAListStoragesEnhance");
		String tableName = params.getString("tableName");
		JSONObject data = params.getJSONObject("record");
		// 处理业务数据
		String name = data.getString("name");
		String links = data.getString("links");
		String resourceType = data.getString("resource_type");
		String driver = data.getString("driver");
		String mount_path = data.getString("mount_path");
		if (StringUtils.isBlank(driver)) {
			driver = StringUtil.getDriverNameByUrl(links);
			data.put("driver",driver);
		}
		if (StringUtils.isBlank(resourceType)) {
			List<DictModel> resourceTypeList = sysDictService.queryDictItemsByCode("alist_resource_type");
			resourceType = AListJobUtil.getResourceTypeName(resourceTypeList,name);
		}
		data.put("resource_type",resourceType);
		data.put("mount_path","/共享/"+(StringUtils.isBlank(resourceType)?driver:resourceType)+"/"+name);
		updateRecord(tableName, data);
		// 更新storages
		String query = "select * from alist_storages where mount_path=?";
		Map<String, Object> result = (Map<String, Object>) DynamicDBUtil.findOne(dbKey,query, mount_path);
		if (result != null) {
			result.put("mount_path", data.get("mount_path"));
			AListJobUtil.updateStorage(result);
		}
		LogUtil.endTime("editAListStoragesEnhance");
		return Result.OK(params);
	}

	/**
	 * 更新业务表数据
	 * @param tableName 业务表
	 * @param record 新数据
	 */
	public void updateRecord(String tableName, JSONObject record) {
		// 查询主键
		if (record.containsKey("jeecg_row_key")) {
			record.remove("jeecg_row_key");
		}
		String sql = "select f.db_field_name from onl_cgform_field f join onl_cgform_head h on h.id=f.cgform_head_id where h.table_name='"+tableName+"' and f.db_is_key=1";
		String keyName = DynamicDBUtil.getOne(dbKey,sql);
		if (StringUtils.isBlank(keyName)) {
			log.warn("updateRecord error: 未找到[{}]主键，无法更新数据", tableName);
			throw new JeecgBootException("未找到["+tableName+"]主键");
		}
		// 拼接update语句
		StringBuilder sb = new StringBuilder();
		HashMap<String,Object> param = new HashMap<>();
		sb.append("update " + tableName + " set ");
		record.keySet().forEach(key -> {
			Object val = record.get(key);
			if (val instanceof ArrayList) { // 子表更新前处理
				String query = "select f.db_field_name,f.main_table,f.main_field from onl_cgform_field f join onl_cgform_head h on h.id=f.cgform_head_id where h.table_name='"+key+"' and f.main_field !=''";
				Map<String,String> keyMap = (Map<String, String>) DynamicDBUtil.findOne(dbKey,query);
				log.info("table[{}];主键[{}];外键[{}]",key,keyMap.get("main_field"),keyMap.get("db_field_name"));
			} else {
				param.put(key,val);
				if (!key.equals(keyName)) {
					sb.append(key+"=:"+key+",");
				}
			}
		});
		sb.setLength(sb.length()-1);
		sb.append(" where "+keyName+"=:"+keyName);
		ThreadUtil.execute(() -> {
			// 更新业务表 TODO 正常执行会超时，先用线程池异步更新
			DynamicDBUtil.updateByHash(dbKey,sb.toString(),param);
		});
	}

	/**
	 * 新增记录
	 * @param tableName
	 * @param record
	 */
	public void addRecord(String tableName, JSONObject record) {
		StringBuilder sb = new StringBuilder();
		HashMap<String,Object> param = new HashMap<>();
		sb.append("insert into ").append(tableName).append(" (");
		record.keySet().forEach(key -> {
			param.put(key,record.get(key));
			sb.append(key+",");
		});
		sb.setLength(sb.length()-1);
		sb.append(") values (:").append(bracketPat.matcher(sb.toString()).group().replaceAll(",",",:")).append(")");
		ThreadUtil.execute(() -> {
			// 新增 TODO 正常执行会超时，先用线程池异步更新
			DynamicDBUtil.updateByHash(dbKey,sb.toString(),param);
		});
	}

}