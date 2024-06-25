package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 部门角色权限
 * @Author: jeecg-boot
 * @Date:   2020-02-12
 * @Version: V1.0
 */
@Data
@TableName("sys_depart_role_permission")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(description="sys_depart_role_permission对象")
public class SysDepartRolePermission {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "id")
	private java.lang.String id;
	/**部门id*/
	@Excel(name = "部门id", width = 15)
    @Schema(description = "部门id")
	private java.lang.String departId;
	/**角色id*/
	@Excel(name = "角色id", width = 15)
    @Schema(description = "角色id")
	private java.lang.String roleId;
	/**权限id*/
	@Excel(name = "权限id", width = 15)
    @Schema(description = "权限id")
	private java.lang.String permissionId;
	/**dataRuleIds*/
	@Excel(name = "dataRuleIds", width = 15)
    @Schema(description = "dataRuleIds")
	private java.lang.String dataRuleIds;
	/** 操作时间 */
	@Excel(name = "操作时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Schema(description = "操作时间")
	private java.util.Date operateDate;
	/** 操作ip */
	private java.lang.String operateIp;

	public SysDepartRolePermission() {
	}

	public SysDepartRolePermission(String roleId, String permissionId) {
		this.roleId = roleId;
		this.permissionId = permissionId;
	}
}
