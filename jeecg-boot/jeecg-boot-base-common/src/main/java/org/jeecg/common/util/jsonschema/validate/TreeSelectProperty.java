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
public class TreeSelectProperty extends CommonProperty {
	
	private static final long serialVersionUID = 3786503639885610767L;
	
	private String dict;//表名,文本,id
	private String pidField;//父级字段 默认pid
	private String pidValue;//父级节点的值 暂时没用到 默认为0
	private String hasChildField;
	private String textField;//树形下拉保存text值的字段名
	
	public String getDict() {
		return dict;
	}

	public void setDict(String dict) {
		this.dict = dict;
	}

	public String getPidField() {
		return pidField;
	}

	public void setPidField(String pidField) {
		this.pidField = pidField;
	}

	public String getPidValue() {
		return pidValue;
	}

	public void setPidValue(String pidValue) {
		this.pidValue = pidValue;
	}
	
	public String getHasChildField() {
		return hasChildField;
	}

	public void setHasChildField(String hasChildField) {
		this.hasChildField = hasChildField;
	}

	public TreeSelectProperty() {}

	public String getTextField() {
		return textField;
	}

	public void setTextField(String textField) {
		this.textField = textField;
	}

	/**
	 *  构造器 构造普通树形下拉
	 */
	public TreeSelectProperty(String key,String title,String dict,String pidField,String pidValue) {
		this.type = "string";
		this.view = "sel_tree";
		this.key = key;
		this.title = title;
		this.dict = dict;
		this.pidField= pidField;
		this.pidValue= pidValue;
	}

	/**
	 * 分类字典下拉专用
	 * @param key
	 * @param title
	 * @param pidValue
	 */
	public TreeSelectProperty(String key,String title,String pidValue) {
		this.type = "string";
		this.view = "cat_tree";
		this.key = key;
		this.title = title;
		this.pidValue = pidValue;
	}

	/**
	 * 分类字典 支持存储text 下拉专用
	 * @param key
	 * @param title
	 * @param pidValue
	 * @param textField
	 */
	public TreeSelectProperty(String key,String title,String pidValue,String textField) {
		this(key,title,pidValue);
		this.textField = textField;
	}
	
	@Override
	public Map<String, Object> getPropertyJson() {
		Map<String,Object> map = new HashMap<>();
		map.put("key",getKey());
		JSONObject prop = getCommonJson();
		if(dict!=null) {
			prop.put("dict",dict);
		}
		if(pidField!=null) {
			prop.put("pidField",pidField);
		}
		if(pidValue!=null) {
			prop.put("pidValue",pidValue);
		}
		if(textField!=null) {
			prop.put("textField",textField);
		}
		if(hasChildField!=null) {
			prop.put("hasChildField",hasChildField);
		}
		map.put("prop",prop);
		return map;
	}

}
