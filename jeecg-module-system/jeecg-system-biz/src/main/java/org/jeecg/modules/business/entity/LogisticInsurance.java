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
 * @Description: logistic insurance
 * @Author: jeecg-boot
 * @Date:   2025-03-04
 * @Version: V1.0
 */
@Data
@TableName("logistic_insurance")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="logistic_insurance对象", description="logistic insurance")
public class LogisticInsurance implements Serializable {
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
	/**logistic_channel_id*/
	@Excel(name = "logistic_channel_id", width = 15, dictTable = "logistic_channel WHERE active=1", dicText = "internal_name", dicCode = "id")
	@Dict(dictTable = "logistic_channel WHERE active=1", dicText = "internal_name", dicCode = "id")
    @ApiModelProperty(value = "logistic_channel_id")
    private java.lang.String logisticChannelId;
	/**sku_id*/
	@Excel(name = "sku_id", width = 15, dictTable = "sku WHERE status=3", dicText = "erp_code", dicCode = "id")
	@Dict(dictTable = "sku WHERE status=3", dicText = "erp_code", dicCode = "id")
    @ApiModelProperty(value = "sku_id")
    private java.lang.String skuId;
	/**amount in euro*/
	@Excel(name = "amount in euro", width = 15)
    @ApiModelProperty(value = "amount in euro")
    private java.math.BigDecimal price;
    /**生效日期*/
    @Excel(name = "生效日期", width = 15)
    @ApiModelProperty(value = "生效日期")
    private java.util.Date effectiveDate;
}
