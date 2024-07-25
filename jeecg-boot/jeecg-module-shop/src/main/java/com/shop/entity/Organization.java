package com.shop.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 组织机构
 * Created by AutoGenerator on 2020-03-14 11:29:04
 */
@Data
@TableName("sys_organization")
public class Organization implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 机构id
     */
    @TableId(value = "organization_id", type = IdType.AUTO)
    private Integer organizationId;
    /**
     * 上级id,0是顶级
     */
    private Integer parentId;
    /**
     * 机构名称
     */
    private String organizationName;
    /**
     * 机构全称
     */
    private String organizationFullName;

    /**
     * 机构代码
     */
    private String organizationCode;
    /**
     * 机构类型
     */
    private Integer organizationType;
    /**
     * 负责人id
     */
    private Integer leaderId;
    /**
     * 排序号
     */
    private Integer sortNumber;
    /**
     * 备注
     */
    private String comments;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 是否删除,0否,1是
     */
    @TableLogic
    private Integer deleted;
    /**
     * 负责人姓名
     */
    @TableField(exist = false)
    private String leaderName;
    /**
     * 负责人账号
     */
    @TableField(exist = false)
    private String leaderAccount;
    /**
     * 上级名称
     */
    @TableField(exist = false)
    private String parentName;
    /**
     * 机构类型名称
     */
    @TableField(exist = false)
    private String organizationTypeName;

}
