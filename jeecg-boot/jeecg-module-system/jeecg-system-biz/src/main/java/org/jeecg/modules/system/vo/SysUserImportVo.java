package org.jeecg.modules.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 低代码用户导入
 * @author: wangshuai
 * @date: 2025/8/27 11:58
 */
@Data
public class SysUserImportVo {

    /**
     * 登录账号
     */
    @Excel(name = "登录账号", width = 15)
    private String username;

    /**
     * 真实姓名
     */
    @Excel(name = "真实姓名", width = 15)
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
    private Integer status;

    /**
     * 删除状态（0，正常，1已删除）
     */
    @Excel(name = "删除状态", width = 15, dicCode = "del_flag")
    private Integer delFlag;

    /**
     * 工号，唯一键
     */
    @Excel(name = "工号", width = 15)
    private String workNo;
    
    /**
     * 主岗位
     */
    @Excel(name="主岗位",width = 15)
    private String mainDepPostId;

    /**
     * 兼职岗位
     */
    @Excel(name="兼职岗位",width = 15)
    private String otherDepPostId;

    /**
     * 职级
     */
    @Excel(name="职级", width = 15)
    private String postName;
    
    /**
     * 身份（0 普通成员 1 上级）
     */
    @Excel(name = "（1普通成员 2上级）", width = 15)
    private Integer userIdentity;

    /**
     * 角色名称
     */
    @Excel(name = "角色", width = 15)
    private String roleNames;

    /**
     * 部门名称
     */
    @Excel(name = "所属部门", width = 15)
    private String departNames;

    /**
     * 机构类型
     * 公司(1)、部门(2)、岗位(3)、子公司(4)
     */
    @Excel(name = "部门类型(1-公司,2-部门,3-岗位,4-子公司)",width = 15)
    private String orgCategorys;

    /**
     * 负责部门
     */
    @Excel(name = "负责部门", width = 15)
    private String departIds;
    
    /**
     * 职务
     */
    @Excel(name="职务", dicCode = "user_position")
    private String positionType;

}
