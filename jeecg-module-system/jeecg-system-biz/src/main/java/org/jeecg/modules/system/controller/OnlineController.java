package org.jeecg.modules.system.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.dynamic.db.DynamicDBUtil;
import org.jeecg.modules.quartz.job.JobUtil;
import org.jeecg.modules.system.service.ISysDictService;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Online表单、报表控制器
 */
@RestController
@RequestMapping("/onilne")
@Slf4j
public class OnlineController {

	private static final String dbKey = "jeecg-boot";

	@Autowired
	private ISysDictService sysDictService;

	/**
	 * 查询表单默认宽度
	 * @param headId
	 * @return
	 */
	@RequestMapping(value = "/cgformFieldList", method = RequestMethod.GET)
	public Result<JSONObject> selectFormField(@RequestParam(name="headId", required=true) String headId) {
		Result<JSONObject> result = new Result<JSONObject>();
		String sql = "select db_field_name,db_field_txt,field_show_type,field_length,field_extend_json from onl_cgform_field where cgform_head_id=?";
		List<Map<String, Object>> fieldList = DynamicDBUtil.findList(dbKey,sql,headId);
		JSONObject obj = new JSONObject();
		obj.put("data",fieldList);
		result.setResult(obj);
		return result;
	}

	@RequestMapping(value = "/cgreportFieldList", method = RequestMethod.GET)
	public Result<JSONObject> selectReportField(@RequestParam(name="headId", required=true) String headId) {
		Result<JSONObject> result = new Result<JSONObject>();
		String sql = "select field_name,field_txt,field_width,field_type from onl_cgreport_item where cgrhead_id=?";
		List<Map<String, Object>> fieldList = DynamicDBUtil.findList(dbKey,sql,headId);
		JSONObject obj = new JSONObject();
		obj.put("data",fieldList);
		result.setResult(obj);
		return result;
	}

	/**
	 * online api增强 表单
	 * @param params
	 * @return
	 */
	@PostMapping("/alist/shares/edit")
	public Result enhanceJavaFormHttp(@RequestBody JSONObject params) throws JobExecutionException {
		String table = params.getString("tableName");
		JSONObject data = params.getJSONObject("record");
		data.remove("jeecg_row_key");
		String sql = "select f.db_field_name from onl_cgform_field f join onl_cgform_head h on h.id=f.cgform_head_id where h.table_name='"+table+"' and f.db_is_key=1";
		String keyName = DynamicDBUtil.getOne(dbKey,sql,new Object[]{});
		StringBuilder sb = new StringBuilder();
		HashMap<String,Object> param = new HashMap<>();
		String name = data.getString("name");
		String links = data.getString("links");
		String resourceType = data.getString("resource_type");
		String driver = data.getString("driver");
		String mount_path = data.getString("mount_path");
		if (StringUtils.isBlank(driver)) {
			driver = JobUtil.getDriverNameByUrl(links);
			data.put("driver",driver);
		}
		if (StringUtils.isBlank(resourceType)) {
			List<DictModel> resourceTypeList = sysDictService.queryDictItemsByCode("alist_resource_type");
			resourceType = JobUtil.getResourceTypeName(resourceTypeList,name);
		}
		data.put("resource_type",resourceType);
		data.put("mount_path","/共享/"+(StringUtils.isBlank(resourceType)?driver:resourceType)+"/"+name);
		sb.append("update "+table+" set ");
		data.keySet().forEach(key -> {
			param.put(key,data.get(key));
			if (!key.equals(keyName)) {
				sb.append(" "+key+"=:"+key+",");
			}
		});
		sb.setLength(sb.length()-1);
		sb.append(" where "+keyName+"=:"+keyName);
		DynamicDBUtil.updateByHash(dbKey,sb.toString(),param);
		// 更新storages
		sql = "select * from alist_storages where mount_path=?";
		Map<String, Object> result = (Map<String, Object>) DynamicDBUtil.findOne(dbKey,sql,mount_path);
		result.put("mount_path",data.get("mount_path"));
		JobUtil.updateStorage(result);
		return Result.OK(params);
	}

}