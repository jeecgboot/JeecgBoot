package org.jeecg.modules.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * @Description: 物流渠道价格
 * @Author: jeecg-boot
 * @Date: 2023-02-09
 * @Version: V1.2
 */
@ApiModel(value = "logistic_channel对象", description = "物流渠道")
@Data
@TableName("logistic_channel_price")
public class LogisticChannelPrice implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+2", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+2", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
    /**
     * 物流渠道
     */
    @ApiModelProperty(value = "物流渠道")
    private String channelId;
    /**
     * 生效日期
     */
    @Excel(name = "生效日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+2", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "生效日期")
    private Date effectiveDate;
    /**
     * 有效国家, represented by code of 2 letters
     */
    @Excel(name = "有效国家", width = 15)
    @ApiModelProperty(value = "有效国家")
    private String effectiveCountry;
    /**
     * 重量范围起始
     */
    @Excel(name = "重量范围起始", width = 15)
    @ApiModelProperty(value = "重量范围起始")
    private Integer weightRangeStart;
    /**
     * 重量范围截至
     */
    @Excel(name = "重量范围截至", width = 15)
    @ApiModelProperty(value = "重量范围截至")
    private Integer weightRangeEnd;
    /**
     * 首重
     */
    @Excel(name = "首重", width = 15)
    @ApiModelProperty(value = "首重")
    private Integer minimumWeight;
    /**
     * 首重价格
     */
    @Excel(name = "首重价格", width = 15)
    @ApiModelProperty(value = "首重价格")
    private java.math.BigDecimal minimumWeightPrice;
    /**
     * 续重单位
     */
    @Excel(name = "续重单位", width = 15)
    @ApiModelProperty(value = "续重单位")
    private Integer calUnit;
    /**
     * 续重单价
     */
    @Excel(name = "续重单价", width = 15)
    @ApiModelProperty(value = "续重单价")
    private java.math.BigDecimal calUnitPrice;
    /**
     * 操作附加费
     */
    @Excel(name = "操作附加费", width = 15)
    @ApiModelProperty(value = "操作附加费")
    private java.math.BigDecimal additionalCost;
    /**
     * 挂号费
     */
    @Excel(name = "挂号费", width = 15)
    @ApiModelProperty(value = "挂号费")
    private java.math.BigDecimal registrationFee;
    /**
     * 包裹最低申报值
     */
    @Excel(name = "包裹最低申报值", width = 15)
    @ApiModelProperty(value = "包裹最低申报值")
    private java.math.BigDecimal minimumDeclaredValue;
    /**
     * 海外仓每件产品操作费
     */
    @Excel(name = "海外仓每件产品操作费", width = 15)
    @ApiModelProperty(value = "海外仓每件产品操作费")
    private java.math.BigDecimal pickingFeePerItem;
    /**
     * Calculate shipping price based on weight.
     *
     * @param weight weight to calculate
     * @return shipping price
     */
    public BigDecimal calculateShippingPrice(BigDecimal weight) {
        BigDecimal min = BigDecimal.valueOf(minimumWeight);
        // inferior to minimum weight
        if (weight.compareTo(min) < 0) {
            return minimumWeightPrice;
        }
        if (calUnit == 1) {
            return weight
                    .multiply(calUnitPrice)
                    .setScale(2, RoundingMode.UP);
        } else {
            BigDecimal calUnitBigDecimal = BigDecimal.valueOf(calUnit);
            return weight
                    .setScale(0, RoundingMode.UP)
                    .subtract(min)
                    .divide(calUnitBigDecimal, RoundingMode.UP)
                    .multiply(calUnitPrice)
                    .add(minimumWeightPrice)
                    .setScale(2, RoundingMode.UP);
        }
    }
}
