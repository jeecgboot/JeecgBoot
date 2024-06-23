package org.jeecg.modules.system.vo;

import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: 用户租户类(用户数据租户数据)
 * @author: wangshuai
 * @date: 2023年01月08日 17:27
 */
@Data
public class SysUserTenantVo {

    /**
     * 用户id
     */
    private String id;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 用户昵称
     */
    private String realname;

    /**
     * 工号
     */
    private String workNo;
    
    /**
     * 邮箱
     */
    private String email; 
    
    /**
     * 手机号
     */
    private String phone;   
    
    /**
     * 头像
     */
    private String avatar; 
    
    /**
     * 职位
     */
    @Dict(dictTable ="sys_position",dicText = "name",dicCode = "id")
    private String post;

    /**
     * 审核状态
     */
    private String status;

    /**
     * 部门名称
     */
    private String orgCodeTxt;

    /**
     * 部门code
     */
    private String orgCode;

    /**
     * 租户id
     */
    private String relTenantIds;

    /**
     * 租户创建人
     */
    private String createBy;

    /**
     * 用户租户状态
     */
    private String userTenantStatus;

    /**
     * 用户租户id
     */
    private String tenantUserId;

    /**
     * 租户名称
     */
    private String name;

    /**
     * 所属行业
     */
    @Dict(dicCode = "trade")
    private String trade;
    
    /**
     * 门牌号
     */
    private String houseNumber;

    /**
     * 是否为会员
     */
    private String memberType;

    /**
     * 是否为租户管理员
     */
    private Boolean tenantAdmin = false;
}
