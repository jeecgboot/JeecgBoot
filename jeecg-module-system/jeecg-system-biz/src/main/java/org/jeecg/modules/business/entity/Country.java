package org.jeecg.modules.business.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 国家
 * @Author: jeecg-boot
 * @Date: 2023-10-06
 * @Version: V1.0
 */
@Data
@TableName("country")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "country对象", description = "国家")
public class Country implements Serializable {
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
     * 英语全名
     */
    @Excel(name = "英语全名", width = 15)
    @ApiModelProperty(value = "英语全名")
    private java.lang.String nameEn;
    /**
     * 中文全名
     */
    @Excel(name = "中文全名", width = 15)
    @ApiModelProperty(value = "中文全名")
    private java.lang.String nameZh;
    /**
     * ISO 3166 代码
     */
    @Excel(name = "ISO 3166 代码", width = 15)
    @ApiModelProperty(value = "ISO 3166 代码")
    private java.lang.String code;
    /**
     * 特殊名称（匹配马帮用）
     */
    @Excel(name = "特殊名称（匹配马帮用）", width = 15)
    @ApiModelProperty(value = "特殊名称（匹配马帮用）")
    private java.lang.String specialName;

    public String getMabangName() {
        if (specialName == null || specialName.isEmpty()) {
            return nameEn;
        }
        return specialName;
    }
}
