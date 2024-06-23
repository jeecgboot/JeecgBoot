//package org.jeecg.common.util.superSearch;
//
//import org.jeecg.common.util.oConvertUtils;
//
///**
// * Query 规则 常量
// * @Author Scott
// * @Date 2019年02月14日
// */
//public enum QueryRuleEnum {
//
//    /**查询规则 大于*/
//    GT(">","大于"),
//    /**查询规则 大于等于*/
//    GE(">=","大于等于"),
//    /**查询规则 小于*/
//    LT("<","小于"),
//    /**查询规则 小于等于*/
//    LE("<=","小于等于"),
//    /**查询规则 等于*/
//    EQ("=","等于"),
//    /**查询规则 不等于*/
//    NE("!=","不等于"),
//    /**查询规则 包含*/
//    IN("IN","包含"),
//    /**查询规则 全模糊*/
//    LIKE("LIKE","全模糊"),
//    /**查询规则 左模糊*/
//    LEFT_LIKE("LEFT_LIKE","左模糊"),
//    /**查询规则 右模糊*/
//    RIGHT_LIKE("RIGHT_LIKE","右模糊"),
//    /**查询规则 自定义SQL片段*/
//    SQL_RULES("EXTEND_SQL","自定义SQL片段");
//
//    private String value;
//
//    private String msg;
//
//    QueryRuleEnum(String value, String msg){
//        this.value = value;
//        this.msg = msg;
//    }
//
//    public String getValue() {
//        return value;
//    }
//
//    public void setValue(String value) {
//        this.value = value;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public static QueryRuleEnum getByValue(String value){
//    	if(oConvertUtils.isEmpty(value)) {
//    		return null;
//    	}
//        for(QueryRuleEnum val :values()){
//            if (val.getValue().equals(value)){
//                return val;
//            }
//        }
//        return  null;
//    }
//}
