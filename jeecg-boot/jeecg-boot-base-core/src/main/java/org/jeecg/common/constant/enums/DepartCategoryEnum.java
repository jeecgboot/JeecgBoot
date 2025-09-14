package org.jeecg.common.constant.enums;

import org.jeecg.common.util.oConvertUtils;

/**
* @Description: 部门类型枚举类
*
* @author: wangshuai
* @date: 2025/8/19 21:37
*/
public enum DepartCategoryEnum {
    
    DEPART_CATEGORY_COMPANY("部门类型：公司","公司","1"),
    DEPART_CATEGORY_DEPART("部门类型：部门","部门","2"),
    DEPART_CATEGORY_POST("部门类型：岗位","岗位","3"),
    DEPART_CATEGORY_SUB_COMPANY("部门类型：子公司","子公司","4");

    DepartCategoryEnum(String described, String name, String value) {
        this.value = value;
        this.name = name;
        this.described = described;
    }

    /**
     * 描述
     */
    private String described;
    /**
     * 值
     */
    private String value;

    /**
     * 名称
     */
    private String name;

    public String getDescribed() {
        return described;
    }

    public void setDescribed(String described) {
        this.described = described;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 根据值获取名称
     * 
     * @param value
     * @return
     */
    public static String getNameByValue(String value){
        if (oConvertUtils.isEmpty(value)) {
            return null;
        }
        for (DepartCategoryEnum val : values()) {
            if (val.getValue().equals(value)) {
                return val.getName();
            }
        }
        return value;
    }
    
    /**
     * 根据名称获取值
     * 
     * @param name
     * @return
     */
    public static String getValueByName(String name){
        if (oConvertUtils.isEmpty(name)) {
            return null;
        }
        for (DepartCategoryEnum val : values()) {
            if (val.getName().equals(name)) {
                return val.getValue();
            }
        }
        return name;
    }
}
