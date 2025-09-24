package org.jeecg.modules.system.vo;

import lombok.Data;

/**
* @Description: 部门职务
*
* @author: wangshuai
* @date: 2025/8/18 10:11
*/
@Data
public class SysDepartPositionVo {

    /**
     * 部门id
     */
    private String id;

    /**
     * 是否为叶子节点(数据返回)
     */
    private Integer izLeaf;
    
    /**
     * 部门名称
     */
    private String departName;

    /**
     * 职务名称
     */
    private String positionName;

    /**
     * 父级id
     */
    private String parentId;

    /**
     * 部门编码
     */
    private String orgCode;

    /**
     * 机构类型
     */
    private String orgCategory;

    /**
     * 上级岗位id
     */
    private String depPostParentId;
}
