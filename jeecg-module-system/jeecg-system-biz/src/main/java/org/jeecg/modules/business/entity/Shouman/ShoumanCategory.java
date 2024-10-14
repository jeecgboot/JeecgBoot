package org.jeecg.modules.business.entity.Shouman;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 首曼产品类别
 * @Author: jeecg-boot
 * @Date: 2023-12-01
 * @Version: V1.0
 */
@ApiModel(value = "shouman_category对象", description = "首曼产品类别")
@Data
@TableName("shouman_category")
public class ShoumanCategory implements Serializable {
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
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
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
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
    /**
     * 产品名称
     */
    @Excel(name = "产品名称", width = 15)
    @ApiModelProperty(value = "产品名称")
    private java.lang.String productName;
    /**
     * 内部名称
     */
    @Excel(name = "内部名称", width = 15)
    @ApiModelProperty(value = "内部名称")
    private java.lang.String internalName;
    /**
     * 产品款式图链接
     */
    @Excel(name = "产品款式图链接", width = 15)
    @ApiModelProperty(value = "产品款式图链接")
    private java.lang.String imageUrl;
    /**
     * 首曼内部SKU代码
     */
    @Excel(name = "首曼内部SKU代码", width = 15)
    @ApiModelProperty(value = "首曼内部SKU代码")
    private java.lang.String sku;
    /**
     * 单价
     */
    @Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    private java.math.BigDecimal price;
    /**
     * 定制内容识别正则式
     */
    @Excel(name = "定制内容识别正则式", width = 15)
    @ApiModelProperty(value = "定制内容识别正则式")
    private java.lang.String contentRecRegex;
    /**
     * 定制内容提取正则式
     */
    @Excel(name = "定制内容提取正则式", width = 15)
    @ApiModelProperty(value = "定制内容提取正则式")
    private java.lang.String contentExtRegex;
}
