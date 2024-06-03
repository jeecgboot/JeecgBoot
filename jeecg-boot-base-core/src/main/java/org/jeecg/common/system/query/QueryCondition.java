package org.jeecg.common.system.query;

import java.io.Serializable;

/**
 * @Description: QueryCondition
 * @author: jeecg-boot
 */
public class QueryCondition implements Serializable {

	private static final long serialVersionUID = 4740166316629191651L;
	
	private String field;
	/** 组件的类型（例如：input、select、radio） */
	private String type;
	/**
	 * 对应的数据库字段的类型
	 * 支持：int、bigDecimal、short、long、float、double、boolean
	 */
	private String dbType;
	private String rule;
	private String val;

	public QueryCondition(String field, String type, String dbType, String rule, String val) {
		this.field = field;
		this.type = type;
		this.dbType = dbType;
		this.rule = rule;
		this.val = val;
	}
	
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	@Override
	public String toString(){
		StringBuffer sb =new StringBuffer();
		if(field == null || "".equals(field)){
			return "";
		}
		sb.append(this.field).append(" ").append(this.rule).append(" ").append(this.type).append(" ").append(this.dbType).append(" ").append(this.val);
		return sb.toString();
	}
}
