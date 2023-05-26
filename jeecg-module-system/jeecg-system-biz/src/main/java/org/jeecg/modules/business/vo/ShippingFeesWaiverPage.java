package org.jeecg.modules.business.vo;

import java.util.List;

import org.jeecg.modules.business.entity.ShippingFeesWaiver;
import org.jeecg.modules.business.entity.ShippingFeesWaiverProduct;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 采购运费免除
 * @Author: jeecg-boot
 * @Date: 2021-06-07
 * @Version: V1.1
 */
@Data
@ApiModel(value = "shipping_fees_waiverPage对象", description = "采购运费免除")
public class ShippingFeesWaiverPage {

    /**
     * 主键
     */
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
    @JsonFormat(timezone = "GMT+2", pattern = "yyyy-MM-dd HH:mm:ss")
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
    @JsonFormat(timezone = "GMT+2", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
    /**
     * 名称
     */
    @Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 免除所需购买量
     */
    @Excel(name = "免除所需购买量", width = 15)
    @ApiModelProperty(value = "免除所需购买量")
    private Integer threshold;

    /**
     * 免除费用
     */
    @Excel(name = "免除费用", width = 15)
    @ApiModelProperty(value = "免除费用")
    private java.math.BigDecimal fees;

    @ExcelCollection(name = "采购运费免除产品")
    @ApiModelProperty(value = "采购运费免除产品")
    private List<ShippingFeesWaiverProduct> shippingFeesWaiverProductList;

}
