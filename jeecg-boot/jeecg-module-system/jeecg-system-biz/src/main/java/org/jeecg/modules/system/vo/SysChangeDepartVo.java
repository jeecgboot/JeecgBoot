package org.jeecg.modules.system.vo;

import lombok.Data;

/**
* @Description: 部门修改替换类
*
* @author: wangshuai
* @date: 2025/9/28 18:52
*/
@Data
public class SysChangeDepartVo {
    /**
     * 最终停止的部门id
     */
    private String dropId;

    /**
     * 拖拽的部门id
     */
    private String dragId;

    /**
     * 拖拽位置（-1上方 1下方 0子级）
     */
    private Integer dropPosition;

    /**
     * 当前位置
     */
    private Integer sort;
}
