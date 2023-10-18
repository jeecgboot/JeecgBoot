package org.jeecg.modules.system.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 租户产品包
 * @Author: jeecg-boot
 * @Date:   2022-12-31
 * @Version: V1.0
 */
@Data
@TableName("sys_tenant_pack")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sys_tenant_pack对象", description="租户产品包")
public class SysTenantPack implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private String id;
	/**租户id*/
	@Excel(name = "租户id", width = 15)
    @ApiModelProperty(value = "租户id")
    private Integer tenantId;
	/**产品包名*/
	@Excel(name = "产品包名", width = 15)
    @ApiModelProperty(value = "产品包名")
    private String packName;
	/**开启状态(0 未开启 1开启)*/
	@Excel(name = "开启状态(0 未开启 1开启)", width = 15)
    @ApiModelProperty(value = "开启状态(0 未开启 1开启)")
    private String status;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remarks;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    /**产品包类型(default 默认产品包 custom 自定义产品包)*/
    @Excel(name = "产品包类型", width = 15)
    @ApiModelProperty(value = "产品包类型")
	private String packType;
    
    /**菜单id 临时字段用于新增编辑菜单id传递*/
    @TableField(exist = false)
    private String permissionIds;
    
    
    /**
     * 编码
     */
    private String packCode;
    
    public SysTenantPack(){
        
    }

    public SysTenantPack(Integer tenantId, String packName, String packCode){
        this.tenantId = tenantId;
        this.packCode = packCode;
        this.packName = packName;
        this.status = "1";
    }
}
