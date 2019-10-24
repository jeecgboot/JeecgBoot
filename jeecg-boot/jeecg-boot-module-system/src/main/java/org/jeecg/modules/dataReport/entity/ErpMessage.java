package org.jeecg.modules.dataReport.entity;

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
 * @Description: erp审批消息查询
 * @Author: Zhao
 * @Date:   2019-10-22
 * @Version: V1.0
 */
@Data
@TableName("erp_message_query")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="erp_message_query对象", description="erp审批消息查询")
public class ErpMessage implements  Serializable{

	private static final long serialVersionUID = 4536320420679451100L;
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private Integer id;
	/**审批人*/
	@Excel(name = "审批人", width = 15)
    @ApiModelProperty(value = "审批人")
	private String approver;
	/**提交人*/
	@Excel(name = "提交人", width = 15)
    @ApiModelProperty(value = "提交人")
	private String submitter;
	/**消息标题*/
	@Excel(name = "消息标题", width = 15)
    @ApiModelProperty(value = "消息标题")
	private String title;
	/**消息内容*/
	@Excel(name = "消息内容", width = 15)
    @ApiModelProperty(value = "消息内容")
	private String taskexpl;

	/**消息唯一号*/
	@Excel(name = "消息唯一号", width = 15)
	@ApiModelProperty(value = "消息唯一号")
	private String messageId;
}
