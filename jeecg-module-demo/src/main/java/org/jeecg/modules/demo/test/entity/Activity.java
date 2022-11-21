package org.jeecg.modules.demo.test.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="活动对象", description="活动对象")
@TableName("vote_activity")
public class Activity extends JeecgEntity implements Serializable {

	@Excel(name="活动名称",width=32)
	@ApiModelProperty(value = "活动名称")
	private String act_name;

	@ApiModelProperty(value = "活动开始时间")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name="活动开始时间",width=20,format="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime act_start_time;

	@ApiModelProperty(value = "活动结束时间")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name="活动结束时间",width=20,format="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime act_end_time;

	@ApiModelProperty(value = "状态",example = "0")
	@Excel(name="状态",width=15)
	private String status;

	@ApiModelProperty(value = "活动链接")
	@Excel(name="活动链接",width=30)
	private String url;

}
