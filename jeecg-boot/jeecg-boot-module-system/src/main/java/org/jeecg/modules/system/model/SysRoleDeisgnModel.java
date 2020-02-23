package org.jeecg.modules.system.model;

import lombok.Data;

/**
 * Created by Administrator on 2019/12/12.
 */
@Data
public class SysRoleDeisgnModel {
    /**主键*/
    private java.lang.String id;
    /**变单设计器code*/
    private java.lang.String desformCode;
    /**变单设计器名称*/
    private java.lang.String desformName;
    /**变单设计器图标*/
    private java.lang.String desformIcon;
    /**流程类型*/
    private java.lang.String procType;
    /**流程名称*/
    private java.lang.String procName;
    /**标题表达式*/
    private java.lang.String titleExp;
}
