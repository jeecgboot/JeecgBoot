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
import java.util.Date;

/**
 * @Description: 首曼订单内容正则式
 * @Author: jeecg-boot
 * @Date: 2024-08-01
 * @Version: V1.0
 */
@ApiModel(value = "shouman_regex对象", description = "首曼订单内容正则式")
@Data
@TableName("shouman_regex")
public class ShoumanRegex implements Serializable {
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
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
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
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
    /**
     * 首曼产品类别ID
     */
    @ApiModelProperty(value = "首曼产品类别ID")
    private String shoumanCategoryId;
    /**
     * 定制内容识别正则式
     */
    @Excel(name = "定制内容识别正则式", width = 15)
    @ApiModelProperty(value = "定制内容识别正则式")
    private String contentRecRegex;
    /**
     * 定制内容提取正则式
     */
    @Excel(name = "定制内容提取正则式", width = 15)
    @ApiModelProperty(value = "定制内容提取正则式")
    private String contentExtRegex;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String comment;
    /**
     * 是否启用
     */
    @Excel(name = "是否启用", width = 15, dicCode = "yn")
    @ApiModelProperty(value = "是否启用")
    private String isActive;
    /**
     * 解析后内容前缀
     */
    @Excel(name = "解析后内容前缀", width = 15)
    @ApiModelProperty(value = "解析后内容前缀")
    private String prefix;
    /**
     * 是否为解析尺寸正则式
     */
    @Excel(name = "是否为解析尺寸正则式", width = 15, dicCode = "yn")
    @ApiModelProperty(value = "是否为解析尺寸正则式")
    private String isSizeRegex;
}
