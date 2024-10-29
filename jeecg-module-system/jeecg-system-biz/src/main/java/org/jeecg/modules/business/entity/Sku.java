package org.jeecg.modules.business.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: SKU表
 * @Author: jeecg-boot
 * @Date: 2021-08-13
 * @Version: V1.1
 */
@ApiModel(value = "sku对象", description = "SKU表")
@Data
@TableName("sku")
public class Sku implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
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
     * ERP中商品代码
     */
    @Excel(name = "ERP中商品代码", width = 15)
    @ApiModelProperty(value = "ERP中商品代码")
    private java.lang.String erpCode;
    /**中文名*/
    @Excel(name = "中文名", width = 15)
    @ApiModelProperty(value = "中文名")
    private String zhName;
    /**英文名*/
    @Excel(name = "英文名", width = 15)
    @ApiModelProperty(value = "英文名")
    private String enName;
    /**发票名称*/
    @Excel(name = "发票名称", width = 15)
    @ApiModelProperty(value = "发票名称")
    private String invoiceName;
    /**
     * 库存数量
     */
    @Excel(name = "库存数量", width = 15)
    @ApiModelProperty(value = "库存数量")
    private java.lang.Integer availableAmount;
    /**
     * 在途数量
     */
    @Excel(name = "在途数量", width = 15)
    @ApiModelProperty(value = "在途数量")
    private java.lang.Integer purchasingAmount;
    /**
     * 图片链接
     */
    @Excel(name = "图片链接", width = 15)
    @ApiModelProperty(value = "图片链接")
    private java.lang.String imageSource;
    /**
     * 运费折扣
     */
    @Excel(name = "运费折扣", width = 15)
    @ApiModelProperty(value = "运费折扣")
    private java.math.BigDecimal shippingDiscount;
    /**
     * 服务费
     */
    @Excel(name = "服务费", width = 15)
    @ApiModelProperty(value = "服务费")
    private java.math.BigDecimal serviceFee;
    /**
     * Status
     * 1:自动创建;2:待开发;3:正常;4:清仓;5:停止销售"
     * default : 3
     */
    @Excel(name = "Status", width = 15)
    @ApiModelProperty(value = "Status")
    private java.lang.Integer status;
    /**敏感属性ID*/
    @Excel(name = "敏感属性ID", width = 15, dictTable = "sensitive_attribute", dicText = "zh_name", dicCode = "id")
    @Dict(dictTable = "sensitive_attribute", dicText = "zh_name", dicCode = "id")
    @ApiModelProperty(value = "敏感属性ID")
    private String sensitiveAttributeId;
    /**最低采购数量*/
    @Excel(name = "最低采购数量", width = 15)
    @ApiModelProperty(value = "最低采购数量")
    private Integer moq;
    /**
     * 是否赠品1是;2否
     */
    @JSONField(name="isGift")
    private Integer isGift;
}
