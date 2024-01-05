package org.jeecg.modules.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * @Description: SKU价格表
 * @Author: jeecg-boot
 * @Date: 2023-05-10
 * @Version: V1.1
 */
@ApiModel(value = "sku对象", description = "SKU表")
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@TableName("sku_price")
public class SkuPrice implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    @Getter
    private String id;

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
     * SKU ID
     */
    @Dict(dictTable = "sku", dicText = "erp_code", dicCode = "id")
    @ApiModelProperty(value = "SKU ID")
    @Getter
    private String skuId;
    /**
     * 价格
     */
    @Excel(name = "价格", width = 15)
    @ApiModelProperty(value = "价格")
    @Getter
    private BigDecimal price;

    /**
     * 优惠价起订量
     */
    @Excel(name = "优惠价起订量", width = 15)
    @ApiModelProperty(value = "优惠价起订量")
    @Getter
    private Integer threshold;
    /**
     * 优惠价
     */
    @Excel(name = "优惠价", width = 15)
    @ApiModelProperty(value = "优惠价")
    @Getter
    private java.math.BigDecimal discountedPrice;
    /**
     * 生效日期
     */
    @Excel(name = "生效日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+2", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "生效日期")
    private Date date;
    /**
     * 人民币价格
     */
    @Excel(name = "人民币价格", width = 15)
    @ApiModelProperty(value = "人民币价格")
    @Getter
    private java.math.BigDecimal priceRmb;
    /**
     * 人民币优惠价
     */
    @Excel(name = "人民币优惠价", width = 15)
    @ApiModelProperty(value = "人民币优惠价")
    @Getter
    private java.math.BigDecimal discountedPriceRmb;

    /**
     * The price of a sku depends on its quantity, Given a quantity here, return the correspondent price.
     *
     * @param quantity a quantity
     * @param eurToRmb Exchange rate from EUR to RMB
     * @return the price correspondent to the quantity
     */
    public BigDecimal getPrice(int quantity, BigDecimal eurToRmb) {
        BigDecimal priceCandidate = price;
        BigDecimal discountedPriceCandidate = discountedPrice == null ? price : discountedPrice;
        if (priceRmb != null) {
            priceCandidate = priceRmb.divide(eurToRmb, RoundingMode.HALF_UP);
            discountedPriceCandidate = discountedPriceRmb == null ? priceCandidate : discountedPriceRmb.divide(eurToRmb, RoundingMode.HALF_UP);
        }
        if (threshold != null && quantity >= threshold) {
            return discountedPriceCandidate;
        }
        return priceCandidate;
    }

    public String toString() {
        return String.format("%s, %s[%d], %s(RMB), %s[%d](RMB)", price, discountedPrice, threshold, priceRmb, discountedPriceRmb, threshold);
    }
}
