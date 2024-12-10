package org.jeecg.modules.business.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: extra fees content
 * @Author: jeecg-boot
 * @Date:   2024-11-15
 * @Version: V1.0
 */
@Data
@TableName("extra_fee")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="extra_fee对象", description="extra fee content")
public class ExtraFee implements Serializable {
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
    /**shop erpCode*/
    @Excel(name = "shop ID", width = 15)
    @ApiModelProperty(value = "shop_id")
    private java.lang.String shop_id;
	/**option_id*/
	@Excel(name = "option_id", width = 15)
    @ApiModelProperty(value = "option_id")
    private java.lang.String optionId;
	/**数量*/
	@Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量")
    private java.lang.Integer quantity;
	/**price per unit*/
	@Excel(name = "price per unit", width = 15)
    @ApiModelProperty(value = "price per unit")
    private java.math.BigDecimal unitPrice;
    /**Précision pour les champs "autres"*/
    @Excel(name = "description for others", width = 15)
    @ApiModelProperty(value = "description for others")
    private java.lang.String description;
	/**发票号*/
	@Excel(name = "发票号", width = 15)
    @ApiModelProperty(value = "发票号")
    private java.lang.String invoiceNumber;
}
