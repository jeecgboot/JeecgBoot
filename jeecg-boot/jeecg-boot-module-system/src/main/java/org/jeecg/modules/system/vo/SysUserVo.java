package org.jeecg.modules.system.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.business.entity.CompanySysuser;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUserVo {



    private static final long serialVersionUID = 1L;


    public SysUserVo(SysUser sysUser) {
        this.setId( sysUser.getId());
        this.setUsername( sysUser.getUsername());
        this.setRealname( sysUser.getRealname( ));
        this.setAvatar(   sysUser.getAvatar());
        this.setBirthday( sysUser.getBirthday());
        this.setPassword(   sysUser.getPassword());
        this.setSalt( sysUser.getSalt());
        this.setSex(      sysUser.getSex( ));
        this.setEmail(    sysUser.getEmail( ));
        this.setPhone(    sysUser.getPhone( ));
        this.setOrgCode(  sysUser.getOrgCode( ));
        this.setStatus(   sysUser.getStatus( ));
        this.setDelFlag(  sysUser.getDelFlag( ));
        this.setWorkNo(   sysUser.getWorkNo( ));
        this.setPost(     sysUser.getPost( ));
        this.setTelephone(sysUser.getTelephone( ));
        this.setCreateBy( sysUser.getCreateBy(  ));
        this.setUpdateBy( sysUser.getUpdateBy(  ));
        this.setUpdateTime(sysUser.getUpdateTime( ));
        this.setActivitiSync(sysUser.getActivitiSync( ));
        this.setUserIdentity(sysUser.getUserIdentity( ));
        this.setDepartIds( sysUser.getDepartIds(   ));
        this.setThirdId( sysUser.getThirdId(      ));
        this.setThirdType( sysUser.getThirdType(   ));
        this.setAvatar(sysUser.getAvatar());


    }

    /**
     * id
     */
    private String id;

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
     * 密码
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /**
     * md5密码盐
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String salt;

    /**
     * 头像
     */
    @Excel(name = "头像", width = 15,type = 2)
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
    @Excel(name = "性别", width = 15,dicCode="sex")
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
     * 部门code(当前选择登录部门)
     */
    private String orgCode;

    /**
     * 状态(1：正常  2：冻结 ）
     */
    @Excel(name = "状态", width = 15,dicCode="user_status")
    @Dict(dicCode = "user_status")
    private Integer status;

    /**
     * 删除状态（0，正常，1已删除）
     */
    @Excel(name = "删除状态", width = 15,dicCode="del_flag")
    @TableLogic
    private Integer delFlag;

    /**
     * 工号，唯一键
     */
    @Excel(name = "工号", width = 15)
    private String workNo;

    /**
     * 职务，关联职务表
     */
    @Excel(name = "职务", width = 15)
    private String post;

    /**
     * 座机号
     */
    @Excel(name = "座机号", width = 15)
    private String telephone;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 同步工作流引擎1同步0不同步
     */
    private Integer activitiSync;

    /**
     * 身份（0 普通成员 1 上级）
     */
    @Excel(name="（1普通成员 2上级）",width = 15)
    private Integer userIdentity;

    /**
     * 负责部门
     */
    @Excel(name="负责部门",width = 15,dictTable ="sys_depart",dicText = "depart_name",dicCode = "id")
    @Dict(dictTable ="sys_depart",dicText = "depart_name",dicCode = "id")
    private String departIds;


    /**
     * 第三方登录的唯一标识
     */
    private String thirdId;

    /**
     * 第三方类型 <br>
     * （github/github，wechat_enterprise/企业微信，dingtalk/钉钉）
     */
    private String thirdType;

    //用户所对应的公司列表
    private List<String> companyIds = new ArrayList<>();

}
