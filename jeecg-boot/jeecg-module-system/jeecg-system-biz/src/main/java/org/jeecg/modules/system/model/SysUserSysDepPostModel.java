package org.jeecg.modules.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 部门用户和部门岗位用户的Model
 * @author: wangshuai
 * @date: 2025/9/5 16:43
 */
@Data
public class SysUserSysDepPostModel {
    /**
     * 用户ID
     */
    private String id;

    /**
     * 用户名
     */
    private String username;
    
    /* 真实姓名 */
    private String realname;

    /**
     * 头像
     */
    @Excel(name = "头像", width = 15, type = 2)
    private String avatar;
    /**
     * 生日
     */
    @Excel(name = "生日", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    /**
     * 性别（1：男 2：女）
     */
    @Excel(name = "性别", width = 15, dicCode = "sex")
    @Dict(dicCode = "sex")
    private Integer sex;

    /**
     * 电子邮件
     */
    @Excel(name = "电子邮件", width = 15)
    private String email;

    /**
     * 电话
     */
    @Excel(name = "电话", width = 15)
    private String phone;

    /**
     * 状态(1：正常  2：冻结 ）
     */
    @Excel(name = "状态", width = 15, dicCode = "user_status")
    @Dict(dicCode = "user_status")
    private Integer status;

    /**
     * 删除状态（0，正常，1已删除）
     */
    @Excel(name = "删除状态", width = 15, dicCode = "del_flag")
    @TableLogic
    private Integer delFlag;

    /**
     * 座机号
     */
    @Excel(name = "座机号", width = 15)
    private String telephone;

    /**
     * 身份（0 普通成员 1 上级）
     */
    @Excel(name = "（1普通成员 2上级）", width = 15)
    private Integer userIdentity;

    /**
     * 负责部门
     */
    @Excel(name = "负责部门", width = 15, dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    private String departIds;

    /**
     * 多租户ids临时用，不持久化数据库(数据库字段不存在)
     */
    private String relTenantIds;

    /**
     * 同步工作流引擎(1-同步 0-不同步)
     */
    private String activitiSync;
    /**
     * 主岗位
     */
    @Excel(name = "主岗位", width = 15, dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    private String mainDepPostId;

    /**
     * 兼职岗位
     */
    @Excel(name = "兼职岗位", width = 15, dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @TableField(exist = false)
    private String otherDepPostId;

    /**
     * 部门名称
     */
    private String departName;
    /**
     * 主岗位
     */
    private String postName;
    
    /**
     * 兼职岗位
     */
    private String otherPostName;

    /**
     * 部门text
     */
    private String orgCodeTxt;

    /**
     * 职务
     */
    private String post;

    /**
     * 部门编码
     */
    private String orgCode;

    /**
     * 职务（字典）
     */
    private String positionType;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否隐藏联系方式 0否1是
     */
    private String izHideContact;

    /**
     * 工号
     */
    private String workNo;
}
