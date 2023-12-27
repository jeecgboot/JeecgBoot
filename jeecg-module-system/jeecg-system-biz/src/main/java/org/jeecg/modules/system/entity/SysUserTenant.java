package org.jeecg.modules.system.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: sys_user_tenant_relation
 * @Author: jeecg-boot
 * @Date:   2022-12-23
 * @Version: V1.0
 */
@Data
@TableName("sys_user_tenant")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="sys_user_tenant")
public class SysUserTenant implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键id")
    private String id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
    @Schema(description = "用户id")
    private String userId;
	/**租户id*/
	@Excel(name = "租户id", width = 15)
    @Schema(description = "租户id")
    private Integer tenantId;
	/**状态(1 正常 2 冻结 3 待审核 4 拒绝)*/
	@Excel(name = "状态(1 正常 2 冻结 3 待审核 4 拒绝)", width = 15)
    @Schema(description = "状态(1 正常 2 冻结 3 待审核 4 拒绝)")
    private String status;
	/**创建人登录名称*/
    @Schema(description = "创建人登录名称")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Schema(description = "创建日期")
    private Date createTime;
	/**更新人登录名称*/
    @Schema(description = "更新人登录名称")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Schema(description = "更新日期")
    private Date updateTime;
}