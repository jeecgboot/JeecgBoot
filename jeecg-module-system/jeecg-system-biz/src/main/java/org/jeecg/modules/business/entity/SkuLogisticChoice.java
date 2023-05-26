package org.jeecg.modules.business.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

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
 * @Description: SKU物流选择
 * @Author: jeecg-boot
 * @Date: 2021-10-07
 * @Version: V1.0
 */
@Data
@TableName("sku_logistic_choice")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "sku_logistic_choice对象", description = "SKU物流选择")
public class SkuLogisticChoice implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+2", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+2", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
    /**
     * SKU ID
     */
    @Excel(name = "SKU ID", width = 15, dictTable = "sku", dicText = "erp_code", dicCode = "id")
    @Dict(dictTable = "sku", dicText = "erp_code", dicCode = "id")
    @ApiModelProperty(value = "SKU ID")
    private java.lang.String skuId;
    /**
     * 国家ID
     */
    @Excel(name = "国家ID", width = 15, dictTable = "country", dicText = "code", dicCode = "id")
    @Dict(dictTable = "country", dicText = "code", dicCode = "id")
    @ApiModelProperty(value = "国家ID")
    private java.lang.String countryId;
    /**
     * 物流渠道ID
     */
    @Excel(name = "物流渠道ID", width = 15, dictTable = "logistic_channel", dicText = "internal_name", dicCode = "id")
    @Dict(dictTable = "logistic_channel", dicText = "internal_name", dicCode = "id")
    @ApiModelProperty(value = "物流渠道ID")
    private java.lang.String logisticChannelId;
    /**
     * 生效日期
     */
    @Excel(name = "生效日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+2", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "生效日期")
    private java.util.Date effectiveDate;
}
