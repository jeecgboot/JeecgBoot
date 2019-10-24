package org.jeecg.modules.dataReport.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 业务接单提单数据
 * @Author: Zhao
 * @Date:   2019-10-12
 * @Version: V1.0
 */
@Data
@TableName("tms_bill_order_data")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="tms_bill_order_data对象", description="业务接单提单数据")
public class BillOrderData {

	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private Integer id;
	/**放行日期*/
	@Excel(name = "放行日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "放行日期")
	private Date customsReleaseDate;
	/**实际到港日期*/
	@Excel(name = "实际到港日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "实际到港日期")
	private Date actualShipDate;
	/**预计到港日期*/
	@Excel(name = "预计到港日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "预计到港日期")
	private Date shippingDate;
	/**提单号*/
	@Excel(name = "提单号", width = 15)
    @ApiModelProperty(value = "提单号")
	private String blNo;
	/**货主(付款方)*/
	@Excel(name = "货主(付款方)", width = 15)
    @ApiModelProperty(value = "货主(付款方)")
	private String consignor;
	/**提箱地点*/
	@Excel(name = "提箱地点", width = 15)
    @ApiModelProperty(value = "提箱地点")
	private String takeContainerAddress;
	/**拖箱地点*/
	@Excel(name = "拖箱地点", width = 15)
    @ApiModelProperty(value = "拖箱地点")
	private String dragContainerAddress;
	/**返箱地点*/
	@Excel(name = "返箱地点", width = 15)
    @ApiModelProperty(value = "返箱地点")
	private String returnContainerAddress;
	/**船东*/
	@Excel(name = "船东", width = 15)
    @ApiModelProperty(value = "船东")
	private String shipowner;
	/**船代*/
	@Excel(name = "船代", width = 15)
    @ApiModelProperty(value = "船代")
	private String shippingCompanyCode;
	/**大品名*/
	@Excel(name = "大品名", width = 15)
    @ApiModelProperty(value = "大品名")
	private String bigGoodsType;
	/**箱量*/
	@Excel(name = "箱量", width = 15)
    @ApiModelProperty(value = "箱量")
	private Integer containerNum;
	/**箱型箱量*/
	@Excel(name = "箱型箱量", width = 15)
    @ApiModelProperty(value = "箱型箱量")
	private String containerDesc;
	/**进出口类型*/
	@Excel(name = "进出口类型", width = 15, dicCode = "business_type")
    @ApiModelProperty(value = "进出口类型")
	@Dict(dicCode = "business_type")
	private Integer businessType;
	/**是否退载*/
	@Excel(name = "是否退载", width = 15, dicCode = "take_back")
    @ApiModelProperty(value = "是否退载")
	@Dict(dicCode = "take_back")
	private Integer takeBack;
	/**业务员*/
	@Excel(name = "业务员", width = 15)
    @ApiModelProperty(value = "业务员")
	private String servicePersonnel;
	/**国家*/
	@Excel(name = "国家", width = 15)
    @ApiModelProperty(value = "国家")
	private String country;
	/**备注*/
	@Excel(name = "异常接单备注", width = 15)
	@ApiModelProperty(value = "异常接单备注")
	private String remark;
}
