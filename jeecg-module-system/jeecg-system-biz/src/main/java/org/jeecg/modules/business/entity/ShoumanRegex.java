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

/**
 * @Description: shouman_regex
 * @Author: jeecg-boot
 * @Date: 2026-08-19
 * @Version: V1.0
 */
@ApiModel(value = "shouman_regex对象", description = "shouman_regex")
@Data
@TableName("shouman_regex")
public class ShoumanRegex implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
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
     * 首曼产品类别ID
     */
    @ApiModelProperty(value = "首曼产品类别ID")
    private java.lang.String shoumanCategoryId;
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
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String comment;
    /**
     * 是否启用
     */
    @Excel(name = "是否启用", width = 15, dicCode = "yn")
    @ApiModelProperty(value = "是否启用")
    private java.lang.String isActive;
    /**
     * 解析后内容前缀
     */
    @Excel(name = "解析后内容前缀", width = 15)
    @ApiModelProperty(value = "解析后内容前缀")
    private java.lang.String prefix;
    /**
     * 是否为解析尺寸正则式
     */
    @Excel(name = "是否为解析尺寸正则式", width = 15, dicCode = "yn")
    @ApiModelProperty(value = "是否为解析尺寸正则式")
    private java.lang.String isSizeRegex;
    /**
     * 是否为月份解析正则式
     */
    @Excel(name = "是否为月份解析正则式", width = 15, dicCode = "yn")
    @ApiModelProperty(value = "是否为月份解析正则式")
    private java.lang.String isMonthRegex;
    /**
     * 是否将定制内容转换为单行逗号分隔文字
     */
    @Excel(name = "是否将定制内容转换为单行逗号分隔文字", width = 15, dicCode = "yn")
    @ApiModelProperty(value = "是否将定制内容转换为单行逗号分隔文字")
    private java.lang.String isCommaSeparated;
    /**
     * 同类正则式之间优先级，越小越优先
     */
    @Excel(name = "同类正则式之间优先级，越小越优先", width = 15)
    @ApiModelProperty(value = "同类正则式之间优先级，越小越优先")
    private java.lang.Integer priority;
    /**
     * 是否将英寸尺寸转换为厘米
     */
    @Excel(name = "是否将英寸尺寸转换为厘米", width = 15, dicCode = "yn")
    @ApiModelProperty(value = "是否将英寸尺寸转换为厘米")
    private java.lang.String isInInches;
    /**
     * 是否用于解析买家留言
     */
    @Excel(name = "是否用于解析买家留言", width = 15, dicCode = "yn")
    @ApiModelProperty(value = "是否用于解析买家留言")
    private java.lang.String useOnBuyerMessage;
}
