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
@ApiModel(value="评委状态监控", description="评委状态监控")
@TableName("vote_monitor")
public class ActivityMonitor extends JeecgEntity implements Serializable {

	@Excel(name="评委名称",width=4)
	@ApiModelProperty(value = "评委名称")
	private String name;

	@Excel(name="推荐数",width=3)
	@ApiModelProperty(value = "推荐数")
	private Integer recommand_count;

	@Excel(name="可推荐数",width=3)
	@ApiModelProperty(value = "可推荐数")
	private Integer un_recommand_count;

	@Excel(name="不推荐数",width=3)
	@ApiModelProperty(value = "不推荐数")
	private Integer not_recommand_count;

	@ApiModelProperty(value = "活动id")
	@Excel(name="活动id",width=10)
	private String activity_id;

	@ApiModelProperty(value = "评委id")
	@Excel(name="评委id",width=10)
	private String account_id;

}
