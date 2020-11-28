package org.jeecg.common.util.jsonschema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonschemaUtil {
	
	/**
	 * 生成JsonSchema
	 * 
	 * @param descrip
	 * @param propertyList
	 * @return
	 */
	public static JSONObject getJsonSchema(JsonSchemaDescrip descrip, List<CommonProperty> propertyList) {
		JSONObject obj = new JSONObject();
		obj.put("$schema", descrip.get$schema());
		obj.put("type", descrip.getType());
		obj.put("title", descrip.getTitle());

		List<String> requiredArr = descrip.getRequired();
		obj.put("required", requiredArr);

		JSONObject properties = new JSONObject();
		for (CommonProperty commonProperty : propertyList) {
			Map<String, Object> map = commonProperty.getPropertyJson();
			properties.put(map.get("key").toString(), map.get("prop"));
		}
		obj.put("properties", properties);
		//鬼知道这里为什么报错 org.jeecg.modules.system.model.DictModel cannot be cast to org.jeecg.modules.system.model.DictModel
		//log.info("---JSONSchema--->"+obj.toJSONString());
		return obj;
	}
	
	/**
	 * 生成JsonSchema 用于子对象
	 * @param title 子对象描述
	 * @param requiredArr 子对象必填属性名集合
	 * @param propertyList 子对象属性集合
	 * @return
	 */
	public static JSONObject getSubJsonSchema(String title,List<String> requiredArr,List<CommonProperty> propertyList) {
		JSONObject obj = new JSONObject();
		obj.put("type", "object");
		obj.put("view", "tab");
		obj.put("title", title);

		if(requiredArr==null) {
			requiredArr = new ArrayList<String>();
		}
		obj.put("required", requiredArr);

		JSONObject properties = new JSONObject();
		for (CommonProperty commonProperty : propertyList) {
			Map<String, Object> map = commonProperty.getPropertyJson();
			properties.put(map.get("key").toString(), map.get("prop"));
		}
		obj.put("properties", properties);
		//log.info("---JSONSchema--->"+obj.toString());
		return obj;
	}

}
