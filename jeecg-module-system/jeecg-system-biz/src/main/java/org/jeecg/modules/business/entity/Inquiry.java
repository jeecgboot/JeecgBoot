package org.jeecg.modules.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Data
@TableName("inquiry")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Inquiry", description = "Inquiry record")
public class Inquiry implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "Created by")
    private String createBy;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "Created time")
    private Date createTime;

    @ApiModelProperty(value = "Updated by")
    private String updateBy;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "Updated time")
    private Date updateTime;

    @Excel(name = "Status", width = 15)
    @ApiModelProperty(value = "Inquiry status")
    private String status;

    @Excel(name = "Client", width = 15, dictTable = "client", dicText = "internal_code", dicCode = "id")
    @Dict(dictTable = "client", dicText = "internal_code", dicCode = "id")
    @ApiModelProperty(value = "Client ID")
    private String clientId;

    @Excel(name = "Sales", width = 15, dictTable = "sys_user", dicText = "username", dicCode = "id")
    @Dict(dictTable = "sys_user", dicText = "username", dicCode = "id")
    @ApiModelProperty(value = "Sales ID")
    private String salesId;

    @Excel(name = "Link", width = 30)
    @ApiModelProperty(value = "Inquiry link")
    private String link;

    @Excel(name = "Country", width = 20, dictTable = "country", dicText = "name_en", dicCode = "id")
    @Dict(dictTable = "country", dicText = "name_en", dicCode = "id")
    @ApiModelProperty(value = "Country IDs, comma separated")
    private String countryId;

    @TableField(exist = false)
    @ApiModelProperty(value = "Country ID list for multi-select")
    private List<String> countryIds;

    @Excel(name = "Expected Sales", width = 15)
    @ApiModelProperty(value = "Expected sales")
    private Integer expectedSales;

    @Excel(name = "Photo", width = 30)
    @ApiModelProperty(value = "Photo")
    private String photo;

    @Excel(name = "Specification", width = 20)
    @ApiModelProperty(value = "Specification")
    private String specification;

    @Excel(name = "Attachments", width = 30)
    @ApiModelProperty(value = "Attachments")
    private String attachments;

    @Excel(name = "Remark", width = 30)
    @ApiModelProperty(value = "Remark")
    private String remark;

    @Excel(name = "Color", width = 20)
    @ApiModelProperty(value = "Product color")
    private String color;

    @TableField("prioritymode")
    @Excel(name = "Priority Mode", width = 20)
    @ApiModelProperty(value = "Priority mode")
    private String priorityMode;
}
