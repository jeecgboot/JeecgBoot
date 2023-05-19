package org.jeecg.modules.wo.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 点餐用户
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Data
@TableName("wo_member")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="wo_member对象", description="点餐用户")
public class WoMember {
    
	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
	private Integer id;
	/**微信用户标识*/
	@Excel(name = "微信用户标识", width = 15)
    @ApiModelProperty(value = "微信用户标识")
	private String openId;
	/**用户名*/
	@Excel(name = "用户名", width = 15)
    @ApiModelProperty(value = "用户名")
	private String name;
	/**手机号*/
	@Excel(name = "手机号", width = 15)
    @ApiModelProperty(value = "手机号")
	private String phone;
	/**性别: 男_0，女_1*/
	@Excel(name = "性别", width = 15)
    @ApiModelProperty(value = "性别: 男1女2")
	@Dict(dicCode = "sex")
	private Integer sex;
	/**年龄*/
	@Excel(name = "年龄", width = 15)
    @ApiModelProperty(value = "年龄")
	private Integer age;
	/**喜好*/
	@Excel(name = "喜好", width = 15)
    @ApiModelProperty(value = "喜好")
	private Object favorite;
}
