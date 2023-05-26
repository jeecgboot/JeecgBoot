package org.jeecg.modules.business.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.UnsupportedEncodingException;

/**
 * @Description: 采购运费免除产品
 * @Author: jeecg-boot
 * @Date: 2021-06-02
 * @Version: V1.0
 */
@ApiModel(value = "shipping_fees_waiver对象", description = "采购运费免除")
@Data
@TableName("shipping_fees_waiver_product")
public class ShippingFeesWaiverProduct implements Serializable {
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
     * 免除ID
     */
    @Excel(name = "免除ID", width = 15, dictTable = "shipping_fees_waiver", dicText = "name", dicCode = "id")
    @Dict(dictTable = "shipping_fees_waiver", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "免除ID")
    private java.lang.String waiverId;
    /**
     * 产品ID
     */
    @Excel(name = "产品ID", width = 15, dictTable = "product", dicText = "code", dicCode = "id")
    @Dict(dictTable = "product", dicText = "code", dicCode = "id")
    @ApiModelProperty(value = "产品ID")
    private java.lang.String productId;
}
