package org.jeecg.modules.airag.app.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 应用变量配置
 * @Author: jeecg-boot
 * @Date: 2025-02-26
 * @Version: V1.0
 */
@Data
public class AppVariableVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 变量名
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 是否启用
     */
    private Boolean enable;

    /**
     * 动作
     */
    private String action;

    /**
     * 排序
     */
    private Integer orderNum;
}
