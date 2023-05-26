package org.jeecg.modules.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: 汇率表
 * @Author: jeecg-boot
 * @Date: 2021-06-26
 * @Version: V1.0
 */
@Data
@TableName("exchange_rates")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "exchange_rates对象", description = "汇率表")
public class ExchangeRates implements Serializable {
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
     * 汇率对代码
     */
    @Excel(name = "汇率对代码", width = 15)
    @ApiModelProperty(value = "汇率对代码")
    private java.lang.String code;
    /**
     * 初始币种
     */
    @Excel(name = "初始币种", width = 15, dictTable = "currency", dicText = "zh_name", dicCode = "code")
    @Dict(dictTable = "currency", dicText = "zh_name", dicCode = "code")
    @ApiModelProperty(value = "初始币种")
    private java.lang.String originalCurrency;
    /**
     * 目标币种
     */
    @Excel(name = "目标币种", width = 15, dictTable = "currency", dicText = "zh_name", dicCode = "code")
    @Dict(dictTable = "currency", dicText = "zh_name", dicCode = "code")
    @ApiModelProperty(value = "目标币种")
    private java.lang.String targetCurrency;
    /**
     * 汇率(1初始币种=？目标币种)
     */
    @Excel(name = "汇率(1初始币种=？目标币种)", width = 15)
    @ApiModelProperty(value = "汇率(1初始币种=？目标币种)")
    private java.math.BigDecimal rate;
    /**
     * 生效时间
     */
    @Excel(name = "生效时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+2", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "生效时间")
    private java.util.Date effectiveDate;
}
