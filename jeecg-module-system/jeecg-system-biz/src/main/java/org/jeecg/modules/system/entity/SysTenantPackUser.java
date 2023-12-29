package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: 租户产品包用户关系表
 * @Author: jeecg-boot
 * @Date:   2023-02-16
 * @Version: V1.0
 */
@Data
@TableName("sys_tenant_pack_user")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sys_tenant_pack_user对象", description="租户产品包用户关系表")
public class SysTenantPackUser implements Serializable {
    private static final long serialVersionUID = 1L;

    /**id*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**租户产品包ID*/
    @Excel(name = "租户产品包ID", width = 15)
    @ApiModelProperty(value = "租户产品包ID")
    private java.lang.String packId;
    /**用户ID*/
    @Excel(name = "用户ID", width = 15)
    @ApiModelProperty(value = "用户ID")
    private java.lang.String userId;
    /**租户ID*/
    @Excel(name = "租户ID", width = 15)
    @ApiModelProperty(value = "租户ID")
    private java.lang.Integer tenantId;
    /**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**创建时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
    /**更新时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;

    private transient String realname;

    private transient String packName;

    private transient String packCode;

    /**
     * 状态(申请状态0 正常状态1)
     */
    private Integer status;

    public SysTenantPackUser(){
        
    }
    public SysTenantPackUser(Integer tenantId, String packId, String userId) {
        this.packId = packId;
        this.userId = userId;
        this.tenantId = tenantId;
        this.status = 1;
    }

    public SysTenantPackUser(SysTenantPackUser param, String userId, String realname) {
        this.userId = userId;
        this.realname = realname;
        this.packId = param.getPackId();
        this.tenantId = param.getTenantId();
        this.packName = param.getPackName();
        this.status = 1;
    }
}
