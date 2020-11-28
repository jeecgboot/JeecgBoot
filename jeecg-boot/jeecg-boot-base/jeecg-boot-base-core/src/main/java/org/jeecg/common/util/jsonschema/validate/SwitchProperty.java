package org.jeecg.common.util.jsonschema.validate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.util.jsonschema.CommonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * 开关 属性
 */
public class SwitchProperty extends CommonProperty {

	//扩展参数配置信息
	private String extendStr;

	public SwitchProperty() {}

	/**
	 *  构造器
	 */
	public SwitchProperty(String key, String title, String extendStr) {
		this.type = "string";
		this.view = "switch";
		this.key = key;
		this.title = title;
		this.extendStr = extendStr;
	}
	
	@Override
	public Map<String, Object> getPropertyJson() {
		Map<String,Object> map = new HashMap<>();
		map.put("key",getKey());
		JSONObject prop = getCommonJson();
		JSONArray array = new JSONArray();
		if(extendStr!=null) {
			array = JSONArray.parseArray(extendStr);
			prop.put("extendOption",array);
		}
		map.put("prop",prop);
		return map;
	}

	//TODO 重构问题：数据字典 只是字符串类的还是有存储的数值类型？只有字符串请跳过这个 只改前端
}
