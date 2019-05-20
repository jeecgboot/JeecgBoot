package org.jeecg.common.util.jsonschema.validate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.jsonschema.CommonProperty;

import com.alibaba.fastjson.JSONObject;

public class NumberProperty extends CommonProperty {

	private static final long serialVersionUID = -558615331436437200L;
	
	/**
	 * 倍数
	 * 验证实例是否为此数值的倍数
	 * “multipleOf”的值必须是一个数字，严格大于0。
	 */
	private Integer multipleOf;
	
	/**
	 * 小于等于
	 * “maximum”的值必须是一个数字，表示数字实例的包含上限。
	 *	如果实例是数字，则仅当实例小于或等于“最大”时，此关键字才会生效。
	 */
	private Integer maxinum;
	
	/**
	 * 小于
	 * “exclusiveMaximum”的值必须是数字，表示数字实例的独占上限。
     * 如果实例是数字，则实例仅在其值严格小于（不等于）“exclusiveMaximum”时才有效。
	 */
	private Integer exclusiveMaximum;
	
	/**
	 * 大于等于
	 */
	private Integer minimum;
	
	/**
	 * 大于等于
	 */
	private Integer exclusiveMinimum;

	public Integer getMultipleOf() {
		return multipleOf;
	}

	public void setMultipleOf(Integer multipleOf) {
		this.multipleOf = multipleOf;
	}

	public Integer getMaxinum() {
		return maxinum;
	}

	public void setMaxinum(Integer maxinum) {
		this.maxinum = maxinum;
	}

	public Integer getExclusiveMaximum() {
		return exclusiveMaximum;
	}

	public void setExclusiveMaximum(Integer exclusiveMaximum) {
		this.exclusiveMaximum = exclusiveMaximum;
	}

	public Integer getMinimum() {
		return minimum;
	}

	public void setMinimum(Integer minimum) {
		this.minimum = minimum;
	}

	public Integer getExclusiveMinimum() {
		return exclusiveMinimum;
	}

	public void setExclusiveMinimum(Integer exclusiveMinimum) {
		this.exclusiveMinimum = exclusiveMinimum;
	}

	public NumberProperty() {}
	
	/**
	  *  构造器
	 * @param key 字段名
	 * @param title 字段备注 
	 * @param type number和integer
	 */
	public NumberProperty(String key,String title,String type) {
		this.key = key;
		this.type = type;
		this.title = title;
		this.view = "number";
	}
	
	/**
	  * 列表类型的走这个构造器  字典里存储的都是字符串 没法走这个构造器
	 * @param key
	 * @param type
	 * @param view list-checkbox-radio
	 * @param include
	 */
	public NumberProperty(String key,String title,String view,List<DictModel> include) {
		this.type = "integer";
		this.key = key;
		this.view = view;
		this.title = title;
		this.include = include;
	}
	
	@Override
	public Map<String,Object> getPropertyJson() {
		Map<String,Object> map = new HashMap<>();
		map.put("key",getKey());
		JSONObject prop = getCommonJson();
		if(multipleOf!=null) {
			prop.put("multipleOf",multipleOf);
		}
		if(maxinum!=null) {
			prop.put("maxinum",maxinum);
		}
		if(exclusiveMaximum!=null) {
			prop.put("exclusiveMaximum",exclusiveMaximum);
		}
		if(minimum!=null) {
			prop.put("minimum",minimum);
		}
		if(exclusiveMinimum!=null) {
			prop.put("exclusiveMinimum",exclusiveMinimum);
		}
		map.put("prop",prop);
		return map;
	}
}
