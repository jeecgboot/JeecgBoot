package org.jeecg.modules.business.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: billing options
 * @Author: jeecg-boot
 * @Date:   2024-10-23
 * @Version: V1.0
 */
@Data
@TableName("billing_options")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="billing_options对象", description="billing options")
public class BillingOptions implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**是否开全部票*/
	@Excel(name = "是否开全部票", width = 15)
    @ApiModelProperty(value = "是否开全部票")
    private java.lang.Integer isComplete;
	/**is preshipping or postshipping*/
	@Excel(name = "is preshipping or postshipping", width = 15)
    @ApiModelProperty(value = "is preshipping or postshipping")
    private java.lang.Integer isPre;
	/**automatic billing or manual*/
	@Excel(name = "automatic billing or manual", width = 15)
    @ApiModelProperty(value = "automatic billing or manual")
    private java.lang.Integer isAutomatic;
	/**shop id*/
	@Excel(name = "shop id", width = 15, dictTable = "shop", dicText = "erp_code", dicCode = "id")
	@Dict(dictTable = "shop", dicText = "erp_code", dicCode = "id")
    @ApiModelProperty(value = "shop id")
    private java.lang.String shopId;
}
