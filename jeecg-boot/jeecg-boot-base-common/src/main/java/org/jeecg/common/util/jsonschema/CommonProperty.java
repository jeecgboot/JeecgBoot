package org.jeecg.common.util.jsonschema;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jeecg.common.system.vo.DictModel;

import com.alibaba.fastjson.JSONObject;

/**
 * 验证通用属性
 */
public abstract class CommonProperty implements Serializable{

	private static final long serialVersionUID = -426159949502493187L;


	protected String key;
	
	
	/**
	 * <p>此关键字的值必须是字符串或数组。如果它是一个数组，那么数组的元素必须是字符串，并且必须是唯一的。
	 * <p>字符串值必须是六种基本类型之一（“null”，“boolean”，“object”，“array”，“number”或“string”），或“integer”，它匹配任何数字，零分数部分。
	 * <p>当且仅当实例位于为此关键字列出的任何集合中时，实例才会验证。
     *
	 */
	protected String type;
	
	/**
	 * 对应JsonSchema的enum
	 * <p>该关键字的值必须是一个数组。这个数组应该至少有一个元素。数组中的元素应该是唯一的。如果实例的值等于此关键字的数组值中的某个元素，则实例将对此关键字成功验证。
	 * 数组中的元素可以是任何值，包括null
	 * 
	 *  {
	 *   "type": "string",
	 *   "enum": ["1", "2", "3"] 需要的话可以通过这个include转一下
	 *	}
	 */
	protected List<DictModel> include;
	
	/**
	 * 对应JsonSchema的const
	 * <p>此关键字的值可以是任何类型，包括null。
	 *	如果实例的值等于关键字的值，则实例将针对此关键字成功验证。
	 */
	protected Object constant;
	
	//三个自定义 属性
	protected String view;// 展示类型
	protected String title;//数据库字段备注
	protected Integer order;//字段显示排序
	
	protected boolean disabled;//是否禁用

    protected String defVal; // 字段默认值

    public String getDefVal() {
        return defVal;
    }

    public void setDefVal(String defVal) {
        this.defVal = defVal;
    }

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<DictModel> getInclude() {
		return include;
	}

	public void setInclude(List<DictModel> include) {
		this.include = include;
	}

	public Object getConstant() {
		return constant;
	}

	public void setConstant(Object constant) {
		this.constant = constant;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	/**
	 * 返回一个map有两个key
	 * <P>key ---> Property JSON的key
	 * <P>prop --> JSON object
	 * @return
	 */
	public abstract Map<String,Object> getPropertyJson();
	
    public JSONObject getCommonJson() {
		JSONObject json = new JSONObject();
		json.put("type", type);
		if(include!=null && include.size()>0) {
			json.put("enum", include);
		}
		if(constant!=null) {
			json.put("const", constant);
		}
		if(title!=null) {
			json.put("title", title);
		}
		if(order!=null) {
			json.put("order", order);
		}
		if(view==null) {
			json.put("view", "input");
		}else {
			json.put("view", view);
		}
		if(disabled) {
			String str = "{\"widgetattrs\":{\"disabled\":true}}";
			JSONObject ui = JSONObject.parseObject(str);
			json.put("ui", ui);
		}
        if (StringUtils.isNotBlank(defVal)) {
            json.put("defVal", defVal);
        }
		return json;
	}
	

}
