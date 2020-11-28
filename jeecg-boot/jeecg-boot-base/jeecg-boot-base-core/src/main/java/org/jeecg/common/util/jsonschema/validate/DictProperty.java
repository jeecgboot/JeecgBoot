package org.jeecg.common.util.jsonschema.validate;

import java.util.HashMap;
import java.util.Map;

import org.jeecg.common.util.jsonschema.CommonProperty;

import com.alibaba.fastjson.JSONObject;

/**
 * 字典属性
 * @author 86729
 *
 */
public class DictProperty extends CommonProperty {
	
	private static final long serialVersionUID = 3786503639885610767L;
	
	//字典三属性
	private String dictCode;
	private String dictTable;
	private String dictText;
	
	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public String getDictTable() {
		return dictTable;
	}

	public void setDictTable(String dictTable) {
		this.dictTable = dictTable;
	}

	public String getDictText() {
		return dictText;
	}

	public void setDictText(String dictText) {
		this.dictText = dictText;
	}

	public DictProperty() {}
	
	/**
	 *  构造器
	 */
	public DictProperty(String key,String title,String dictTable,String dictCode,String dictText) {
		this.type = "string";
		this.view = "sel_search";
		this.key = key;
		this.title = title;
		this.dictCode = dictCode;
		this.dictTable= dictTable;
		this.dictText= dictText;
	}
	
	@Override
	public Map<String, Object> getPropertyJson() {
		Map<String,Object> map = new HashMap<>();
		map.put("key",getKey());
		JSONObject prop = getCommonJson();
		if(dictCode!=null) {
			prop.put("dictCode",dictCode);
		}
		if(dictTable!=null) {
			prop.put("dictTable",dictTable);
		}
		if(dictText!=null) {
			prop.put("dictText",dictText);
		}
		map.put("prop",prop);
		return map;
	}

	//TODO 重构问题：数据字典 只是字符串类的还是有存储的数值类型？只有字符串请跳过这个 只改前端
}
