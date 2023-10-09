package org.jeecg.modules.business.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
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
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: sensitive_attribute
 * @Author: jeecg-boot
 * @Date:   2023-10-03
 * @Version: V1.0
 */
@Data
@TableName("sensitive_attribute")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sensitive_attribute对象", description="sensitive_attribute")
public class SensitiveAttribute implements Serializable {
    private static final long serialVersionUID = 1L;

    /**id*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**中文描述*/
    @Excel(name = "中文描述", width = 15)
    @ApiModelProperty(value = "中文描述")
    private java.lang.String zhName;
    /**英文描述*/
    @Excel(name = "英文描述", width = 15)
    @ApiModelProperty(value = "英文描述")
    private java.lang.String enName;
    /**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**创建日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
    /**更新日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
    /**priority*/
    @Excel(name = "priority", width = 15)
    @ApiModelProperty(value = "priority")
    private java.lang.Integer priority;
}
