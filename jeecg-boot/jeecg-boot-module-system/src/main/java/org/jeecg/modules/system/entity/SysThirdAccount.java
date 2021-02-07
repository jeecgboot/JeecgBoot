package org.jeecg.modules.system.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 第三方登录账号表
 * @Author: jeecg-boot
 * @Date:   2020-11-17
 * @Version: V1.0
 */
@Data
@TableName("sys_third_account")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="sys_third_account对象", description="第三方登录账号表")
public class SysThirdAccount {
 
	/**编号*/
	@TableId(type = IdType.ASSIGN_ID)
  @ApiModelProperty(value = "编号")
	private java.lang.String id;
	/**第三方登录id*/
	@Excel(name = "第三方登录id", width = 15)
	@ApiModelProperty(value = "第三方登录id")
	private java.lang.String sysUserId;
	/**登录来源*/
	@Excel(name = "登录来源", width = 15)
	@ApiModelProperty(value = "登录来源")
	private java.lang.String thirdType;
	/**头像*/
	@Excel(name = "头像", width = 15)
	@ApiModelProperty(value = "头像")
	private java.lang.String avatar;
	/**状态(1-正常,2-冻结)*/
	@Excel(name = "状态(1-正常,2-冻结)", width = 15)
	@ApiModelProperty(value = "状态(1-正常,2-冻结)")
	private java.lang.Integer status;
	/**删除状态(0-正常,1-已删除)*/
	@Excel(name = "删除状态(0-正常,1-已删除)", width = 15)
	@ApiModelProperty(value = "删除状态(0-正常,1-已删除)")
	private java.lang.Integer delFlag;
	/**真实姓名*/
	@Excel(name = "真实姓名", width = 15)
	@ApiModelProperty(value = "真实姓名")
	private java.lang.String realname;
	/**真实姓名*/
	@Excel(name = "真实姓名", width = 15)
	@ApiModelProperty(value = "真实姓名")
	private java.lang.String thirdUserUuid;
}
