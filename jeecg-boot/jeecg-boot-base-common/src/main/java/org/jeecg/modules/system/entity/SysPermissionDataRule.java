package org.jeecg.modules.system.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 菜单权限规则表
 * </p>
 *
 * @Author huangzhilin
 * @since 2019-03-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysPermissionDataRule implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * id
	 */
	@TableId(type = IdType.UUID)
	private String id;
	
	/**
	 * 对应的菜单id
	 */
	private String permissionId;
	
	/**
	 * 规则名称
	 */
	private String ruleName;
	
	/**
	 * 字段
	 */
	private String ruleColumn;
	
	/**
	 * 条件
	 */
	private String ruleConditions;
	
	/**
	 * 规则值
	 */
	private String ruleValue;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 创建人
	 */
	private String createBy;
	
	/**
	 * 修改时间
	 */
	private Date updateTime;
	
	/**
	 * 修改人
	 */
	private String updateBy;
}
