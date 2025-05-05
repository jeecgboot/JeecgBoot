package org.jeecg.modules.business.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

@Data
@ApiModel(value = "platform_order_content对象", description = "平台订单内容")
public class PlatformOrderContentFront implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 平台订单ID
     */
    @Excel(name = "平台订单ID", width = 15, dictTable = "platform_order", dicText = "platform_order_id", dicCode = "id")
    @Dict(dictTable = "platform_order", dicText = "platform_order_id", dicCode = "id")
    @ApiModelProperty(value = "平台订单ID")
    private String platformOrderId;
    /**
     * SKU ID
     */
    @Excel(name = "SKU ID", width = 15, dictTable = "sku", dicText = "erp_code", dicCode = "id")
    @Dict(dictTable = "sku", dicText = "erp_code", dicCode = "id")
    @ApiModelProperty(value = "SKU ID")
    private String skuId;
    /**
     * SKU数量
     */
    @Excel(name = "SKU数量", width = 15)
    @ApiModelProperty(value = "SKU数量")
    private Integer quantity;
    /**
     * 商品采购总费用
     */
    @Excel(name = "商品采购总费用", width = 15)
    @ApiModelProperty(value = "商品采购总费用")
    private java.math.BigDecimal purchaseFee;
    /**
     * 物流总费用
     */
    @Excel(name = "物流总费用", width = 15)
    @ApiModelProperty(value = "物流总费用")
    private java.math.BigDecimal shippingFee;
    /**
     * 服务总费用
     */
    @Excel(name = "服务总费用", width = 15)
    @ApiModelProperty(value = "服务总费用")
    private java.math.BigDecimal serviceFee;
    /**
     * 海外仓操作费
     */
    @Excel(name = "海外仓操作费", width = 15)
    @ApiModelProperty(value = "海外仓操作费")
    private java.math.BigDecimal pickingFee;
    /**
     * 物流保险费
     */
    @Excel(name = "物流保险费", width = 15)
    @ApiModelProperty(value = "物流保险费")
    private java.math.BigDecimal insuranceFee;
    /**
     * 增值税
     */
    @Excel(name = "增值税", width = 15)
    @ApiModelProperty(value = "增值税")
    private java.math.BigDecimal vat;
    /**
     * ERP中状态
     */
    @Excel(name = "ERP中状态", width = 15)
    @ApiModelProperty(value = "ERP中状态")
    private String erpStatus;

    /**
     * 有货（1=有，0=没有）
     */
    @Excel(name = "有货（1=有，0=没有）", width = 15)
    @ApiModelProperty(value = "有货（1=有，0=没有）")
    private String productAvailable;
    /**
     * 商品是否一致（1=一致，0=不一致）
     * if latest price or weight is not the same in Mongo and Mabang
     */
    @Excel(name = "商品是否一致（1=一致，0=不一致）", width = 15)
    @ApiModelProperty(value = "商品是否一致（1=一致，0=不一致）")
    private Integer isSynced;
}
