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
@Data
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
     * 价格
     */
    @Excel(name = "价格", width = 15)
    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    /**
     * 优惠价起订量
     */
    @Excel(name = "优惠价起订量", width = 15)
    @ApiModelProperty(value = "优惠价起订量")
    private Integer threshold;
    /**
     * 优惠价
     */
    @Excel(name = "优惠价", width = 15)
    @ApiModelProperty(value = "优惠价")
    private java.math.BigDecimal discountedPrice;
    /**
     * 生效日期
     */
    @Excel(name = "生效日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+2", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "生效日期")
    private Date date;
    /**币种ID*/
    @Excel(name = "币种ID", width = 15, dictTable = "currency", dicText = "code", dicCode = "id")
    @Dict(dictTable = "currency", dicText = "code", dicCode = "id")
    @ApiModelProperty(value = "币种ID")
    private java.lang.String currencyId;

    public String toString() {
        return String.format("%s, %s[%d]", price, discountedPrice, threshold);
    }
}
