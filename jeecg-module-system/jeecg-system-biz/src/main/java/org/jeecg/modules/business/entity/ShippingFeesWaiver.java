package org.jeecg.modules.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Description: 采购运费免除
 * @Author: jeecg-boot
 * @Date: 2021-06-04
 * @Version: V1.1
 */
@ApiModel(value = "shipping_fees_waiver对象", description = "采购运费免除")
@Data
@EqualsAndHashCode
@TableName("shipping_fees_waiver")
public class ShippingFeesWaiver implements Serializable {
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
     * 名称
     */
    @Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private java.lang.String name;
    /**
     * 免除所需购买量
     */
    @Excel(name = "免除所需购买量", width = 15)
    @ApiModelProperty(value = "免除所需购买量")
    private java.lang.Integer threshold;
    /**
     * 免除费用
     */
    @Excel(name = "免除费用", width = 15)
    @ApiModelProperty(value = "免除费用")
    private java.math.BigDecimal fees;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShippingFeesWaiver waiver = (ShippingFeesWaiver) o;
        return id.equals(waiver.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
