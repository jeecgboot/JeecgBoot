package org.jeecg.modules.system.vo;

import lombok.Data;

/**
 * @Author qinfeng
 * @Date 2020/1/2 21:58
 * @Description:
 * @Version 1.0
 */
@Data
public class SysUserDepVo {
    private String userId;
    private String departName;
    /**
     * 部门id
     */
    private String deptId;

    /**
     * 部门的父级id
     */
    private String parentId;
}
