package com.vmq.constant;


/**
 * 日志操作类型
 */
public enum OperateTypeEnum {

    /**
     * 列表
     */
    LIST(Constant.NUMBER_1, "list"),

    /**
     * 新增
     */
    ADD(Constant.NUMBER_2, "add"),

    /**
     * 编辑
     */
    EDIT(Constant.NUMBER_3, "edit"),

    /**
     * 删除
     */
    DELETE(Constant.NUMBER_4, "delete"),

    /**
     * 导入
     */
    IMPORT(Constant.NUMBER_5, "import"),

    /**
     * 导出
     */
    EXPORT(Constant.NUMBER_6, "export");

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
        return Constant.NUMBER_1;
    }
}
