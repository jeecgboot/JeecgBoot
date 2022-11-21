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
@ApiModel(value="投票用例", description="活动对象")
@TableName("vote_case")
public class ActivityCase extends JeecgEntity implements Serializable {

	@Excel(name="账号",width=32)
	@ApiModelProperty(value = "账号")
	private String account;

	@Excel(name="密码",width=32)
	@ApiModelProperty(value = "账号")
	private String password;

	@ApiModelProperty(value = "活动id")
	@Excel(name="活动id",width=32)
	private String activity_id;



}
