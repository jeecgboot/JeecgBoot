package org.jeecg.modules.system.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 角色首页配置
 * @Author: liusq
 * @Date:   2022-03-25
 * @Version: V1.0
 */
@Data
@TableName("sys_role_index")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(description="角色首页配置")
public class SysRoleIndex {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "id")
	private java.lang.String id;
	/**角色编码*/
	@Excel(name = "角色编码", width = 15)
    @Schema(description = "角色编码")
	private java.lang.String roleCode;
	/**路由地址*/
	@Excel(name = "路由地址", width = 15)
    @Schema(description = "路由地址")
	private java.lang.String url;
	/**路由地址*/
	@Excel(name = "路由地址", width = 15)
    @Schema(description = "组件")
	private java.lang.String component;
	/**
	 * 是否路由菜单: 0:不是  1:是（默认值1）
	 */
	@Excel(name = "是否路由菜单", width = 15)
	@Schema(description = "是否路由菜单")
	@TableField(value="is_route")
	private boolean route;
	/**优先级*/
	@Excel(name = "优先级", width = 15)
    @Schema(description = "优先级")
	private java.lang.Integer priority;
	/**路由地址*/
	@Excel(name = "状态", width = 15)
	@Schema(description = "状态")
	private java.lang.String status;
	/**创建人登录名称*/
	@Excel(name = "创建人登录名称", width = 15)
    @Schema(description = "创建人登录名称")
	private java.lang.String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建日期")
	private java.util.Date createTime;
	/**更新人登录名称*/
	@Excel(name = "更新人登录名称", width = 15)
    @Schema(description = "更新人登录名称")
	private java.lang.String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新日期")
	private java.util.Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    @Schema(description = "所属部门")
	private java.lang.String sysOrgCode;


	public SysRoleIndex() {

	}
	public SysRoleIndex(String componentUrl){
		this.component = componentUrl;
	}
}
