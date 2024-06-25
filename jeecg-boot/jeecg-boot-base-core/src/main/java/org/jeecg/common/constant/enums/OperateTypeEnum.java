package org.jeecg.common.constant.enums;

import org.jeecg.common.constant.CommonConstant;

/**
 * @Description: 操作类型
 * @author: jeecg-boot
 * @date: 2022/3/31 10:05
 */
public enum OperateTypeEnum {

    /**
     * 列表
     */
    LIST(CommonConstant.OPERATE_TYPE_1, "list"),

    /**
     * 新增
     */
    ADD(CommonConstant.OPERATE_TYPE_2, "add"),

    /**
     * 编辑
     */
    EDIT(CommonConstant.OPERATE_TYPE_3, "edit"),

    /**
     * 删除
     */
    DELETE(CommonConstant.OPERATE_TYPE_4, "delete"),

    /**
     * 导入
     */
    IMPORT(CommonConstant.OPERATE_TYPE_5, "import"),

    /**
     * 导出
     */
    EXPORT(CommonConstant.OPERATE_TYPE_6, "export");

    /**
     * 类型 1列表,2新增,3编辑,4删除,5导入,6导出
     */
    int type;

    /**
     * 编码(请求方式)
     */
    String code;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 构造器
     *
     * @param type 类型
     * @param code 编码(请求方式)
     */
    OperateTypeEnum(int type, String code) {
        this.type = type;
        this.code = code;
    }


    /**
     * 根据请求名称匹配
     *
     * @param methodName 请求名称
     * @return Integer 类型
     */
    public static Integer getTypeByMethodName(String methodName) {
        for (OperateTypeEnum e : OperateTypeEnum.values()) {
            if (methodName.startsWith(e.getCode())) {
                return e.getType();
            }
        }
        return CommonConstant.OPERATE_TYPE_1;
    }
}
