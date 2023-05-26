package org.jeecg.modules.business.entity;

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
 * @Description: 店铺
 * @Author: jeecg-boot
 * @Date: 2021-08-11
 * @Version: V1.1
 */
@ApiModel(value = "Shop对象", description = "店铺")
@Data
@TableName("shop")
public class Shop implements Serializable {
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
     * 店主ID
     */
    @Dict(dictTable = "client", dicText = "internal_code", dicCode = "id")
    @ApiModelProperty(value = "店主ID")
    private java.lang.String ownerId;
    /**
     * ERP内名称
     */
    @Excel(name = "ERP内名称", width = 15)
    @ApiModelProperty(value = "ERP内名称")
    private java.lang.String erpCode;
    /**
     * 店铺名称
     */
    @Excel(name = "店铺名称", width = 15)
    @ApiModelProperty(value = "店铺名称")
    private java.lang.String name;
    /**
     * 网站地址
     */
    @Excel(name = "网站地址", width = 15)
    @ApiModelProperty(value = "网站地址")
    private java.lang.String website;
    /**
     * 物流折扣
     */
    @Excel(name = "物流折扣", width = 15)
    @ApiModelProperty(value = "物流折扣")
    private java.math.BigDecimal shippingDiscount;
    /**
     * 订单服务费
     */
    @Excel(name = "订单服务费", width = 15)
    @ApiModelProperty(value = "订单服务费")
    private java.math.BigDecimal orderServiceFee;
    /**
     * 包材费
     */
    @Excel(name = "包材费", width = 15)
    @ApiModelProperty(value = "包材费")
    private java.math.BigDecimal packagingMaterialFee;
}
