package org.jeecg.common.system.query;

import org.jeecg.common.util.oConvertUtils;

/**
 * Query 规则 常量
 * @Author Scott
 * @Date 2019年02月14日
 */
public enum QueryRuleEnum {

    /**查询规则 大于*/
    GT(">","gt","大于"),
    /**查询规则 大于等于*/
    GE(">=","ge","大于等于"),
    /**查询规则 小于*/
    LT("<","lt","小于"),
    /**查询规则 小于等于*/
    LE("<=","le","小于等于"),
    /**查询规则 等于*/
    EQ("=","eq","等于"),
    /**查询规则 不等于*/
    NE("!=","ne","不等于"),
    /**查询规则 包含*/
    IN("IN","in","包含"),
    /**查询规则 全模糊*/
    LIKE("LIKE","like","全模糊"),
    /**查询规则 不模糊包含*/
    NOT_LIKE("NOT_LIKE","not_like","不模糊包含"),
    /**查询规则 左模糊*/
    LEFT_LIKE("LEFT_LIKE","left_like","左模糊"),
    /**查询规则 右模糊*/
    RIGHT_LIKE("RIGHT_LIKE","right_like","右模糊"),
    /**查询规则 带加号等于*/
    EQ_WITH_ADD("EQWITHADD","eq_with_add","带加号等于"),
    /**查询规则 多词模糊匹配(and)*/
    LIKE_WITH_AND("LIKEWITHAND","like_with_and","多词模糊匹配————暂时未用上"),
    /**查询规则 多词模糊匹配(or)*/
    LIKE_WITH_OR("LIKEWITHOR","like_with_or","多词模糊匹配(or)"),
    /**查询规则 自定义SQL片段*/
    SQL_RULES("USE_SQL_RULES","ext","自定义SQL片段"),

    /** 查询工作表 */
    LINKAGE("LINKAGE","linkage","查询工作表"),

    // ------- 当前表单设计器内专用 -------
    /**查询规则 不以…结尾*/
    NOT_LEFT_LIKE("NOT_LEFT_LIKE","not_left_like","不以…结尾"),
    /**查询规则 不以…开头*/
    NOT_RIGHT_LIKE("NOT_RIGHT_LIKE","not_right_like","不以…开头"),
    /** 值为空 */
    EMPTY("EMPTY","empty","值为空"),
    /** 值不为空 */
    NOT_EMPTY("NOT_EMPTY","not_empty","值不为空"),
    /**查询规则 不包含*/
    NOT_IN("NOT_IN","not_in","不包含"),
    /**查询规则 多词匹配*/
    ELE_MATCH("ELE_MATCH","elemMatch","多词匹配"),
    /**查询规则 范围查询*/
    RANGE("RANGE","range","范围查询"),
    /**查询规则 不在范围内查询*/
    NOT_RANGE("NOT_RANGE","not_range","不在范围查询"),
    /** 自定义mongodb查询语句 */
    CUSTOM_MONGODB("CUSTOM_MONGODB","custom_mongodb","自定义mongodb查询语句");
    // ------- 当前表单设计器内专用 -------

    private String value;
    
    private String condition; 

    private String msg;

    QueryRuleEnum(String value, String condition, String msg){
        this.value = value;
        this.condition = condition;
        this.msg = msg;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public static QueryRuleEnum getByValue(String value){
    	if(oConvertUtils.isEmpty(value)) {
    		return null;
    	}
        for(QueryRuleEnum val :values()){
            if (val.getValue().equals(value) || val.getCondition().equalsIgnoreCase(value)){
                return val;
            }
        }
        return  null;
    }
}
