package org.jeecg.modules.auth.entity;

import java.io.Serializable;
import java.io.Serial;
import java.util.Date;
import java.util.List;
import org.jeecg.modules.auth.config.PermissionAction;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 资产字段角色权限表
 * 
 * @author: local.clk
 * @date: 2026-02-09
 */
@Data
@TableName("tenant_asset_field_role_permission")
@Schema(description = "资产字段角色权限表")
public class AssetFieldRolePermission implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "ID")
    private Long id;

//    /**
//     * 租户ID
//     */
//    @Schema(description = "租户ID")
//    @JsonIgnore
//    private String tenantId;

    /**
     * 字段定义ID
     */
    @Schema(description = "字段定义ID")
    @Excel(name = "字段定义ID", width = 15)
    private Long fieldDefinitionId;

    /**
     * 角色ID
     */
    @Schema(description = "角色ID")
    @Excel(name = "角色ID", width = 15)
    private String roleId;

    /**
     * 权限位
     */
    @Schema(description = "权限位",hidden = true)
    @Excel(name = "权限位", width = 15)
    @JsonIgnore
    private Integer permissionBits;
    /**
     * 创建人
     */
    @Schema(description = "创建人")
    @Excel(name = "创建人", width = 15)
    private String createBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人
     */
    @Schema(description = "更新人")
    @Excel(name = "更新人", width = 15)
    private String updateBy;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @TableField(exist = false)
    @Schema(description = "权限动作")
    @Excel(name = "权限动作", width = 15)
    private List<PermissionAction> permissionActions;

    public List<PermissionAction> getPermissionActions() {
        return PermissionAction.getPermissionActions(permissionBits);
    }
}
