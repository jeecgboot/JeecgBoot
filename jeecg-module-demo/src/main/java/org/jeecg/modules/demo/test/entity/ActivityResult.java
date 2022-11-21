package org.jeecg.modules.demo.test.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="投票明细结果", description="投票明细结果")
@TableName("vote_result")
public class ActivityResult extends JeecgEntity implements Serializable {

	@Excel(name="投票结果",width=3)
	@ApiModelProperty(value = "投票结果")
	private Integer vote_result;

	@ApiModelProperty(value = "作品id")
	@Excel(name="作品id",width=10)
	private String case_id;

	@ApiModelProperty(value = "活动id")
	@Excel(name="活动id",width=10)
	private String activity_id;

	@ApiModelProperty(value = "评委id")
	@Excel(name="评委id",width=10)
	private String account_id;

}
