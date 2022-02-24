package org.jeecg.common.constant.enums;

/**
 * LowApp 切面注解枚举
 * @date 2022-1-5
 */
public enum LowAppAopEnum {

    /**
     * 新增方法
     */
    ADD,
    /**
     * 删除方法（包含单个和批量删除）
     */
    DELETE,

    /**
     * Online表单专用：数据库表转Online表单
     */
    CGFORM_DB_IMPORT
}
