package org.jeecg.common.util.jsonschema.validate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecg.common.util.jsonschema.CommonProperty;

import com.alibaba.fastjson.JSONObject;

public class PopupProperty extends CommonProperty {
	
	private static final long serialVersionUID = -3200493311633999539L;
	
	private String code;
	
	private String destFields;
	
	private String orgFields;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDestFields() {
		return destFields;
	}

	public void setDestFields(String destFields) {
		this.destFields = destFields;
	}

	public String getOrgFields() {
		return orgFields;
	}

	public void setOrgFields(String orgFields) {
		this.orgFields = orgFields;
	}

	public PopupProperty() {}
	
	public PopupProperty(String key,String title,String code,String destFields,String orgFields) {
		this.view = "popup";
		this.type = "string";
		this.key = key;
		this.title = title;
		this.code = code;
		this.destFields=destFields;
		this.orgFields=orgFields;
	}
	

	@Override
	public Map<String, Object> getPropertyJson() {
		Map<String,Object> map = new HashMap<>();
		map.put("key",getKey());
		JSONObject prop = getCommonJson();
		if(code!=null) {
			prop.put("code",code);
		}
		if(destFields!=null) {
			prop.put("destFields",destFields);
		}
		if(orgFields!=null) {
			prop.put("orgFields",orgFields);
		}
		map.put("prop",prop);
		return map;
	}

}
