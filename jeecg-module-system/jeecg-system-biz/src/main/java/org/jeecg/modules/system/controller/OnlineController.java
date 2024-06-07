package org.jeecg.modules.system.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.dynamic.db.DynamicDBUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

	/**
	 * 查询表单默认宽度
	 * @param headId
	 * @return
	 */
	@RequestMapping(value = "/cgformFieldList", method = RequestMethod.GET)
	public Result<JSONObject> selectDepart(@RequestParam(name="headId", required=true) String headId) {
		Result<JSONObject> result = new Result<JSONObject>();
		String sql = "select db_field_name,db_field_txt,field_show_type,field_length,field_extend_json from onl_cgform_field where cgform_head_id=?";
		List<Map<String, Object>> fieldList = DynamicDBUtil.findList(dbKey,sql,headId);
		JSONObject obj = new JSONObject();
		obj.put("data",fieldList);
		result.setResult(obj);
		return result;
	}

}