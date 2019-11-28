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
 * @Description: 集装箱业务统计表
 * @Author: Zhao
 * @Date:   2019-08-16
 * @Version: V1.0
 */
@Data
@TableName("container_tatistics")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="container_tatistics对象", description="集装箱业务统计表")
public class ContainerTatistics {

	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private Integer id;
	/**提单号*/
	@Excel(name = "提单号", width = 15)
    @ApiModelProperty(value = "提单号")
	private String blNo;
	/**海关放行日期*/
	@Excel(name = "海关放行日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "海关放行日期")
	private Date customsReleaseDate;
	/**箱号（柜号）*/
	@Excel(name = "箱号（柜号）", width = 15)
    @ApiModelProperty(value = "箱号（柜号）")
	private String containerNo;
	/**尺寸(1：20，2：40)*/
	@Excel(name = "尺寸(1：20，2：40)", width = 15, dicCode = "container_size")
    @ApiModelProperty(value = "尺寸(1：20，2：40)")
	@Dict(dicCode = "container_size")
	private Integer containerSize;
	/**规格（1：GP，2：HQ）*/
	@Excel(name = "规格（1：GP，2：HQ）", width = 15, dicCode = "container_spec")
    @ApiModelProperty(value = "规格（1：GP，2：HQ）")
	@Dict(dicCode = "container_spec")
	private Integer containerSpec;
	/**船东*/
	@Excel(name = "船东", width = 15)
    @ApiModelProperty(value = "船东")
	private String shipowner;
	/**提箱地点*/
	@Excel(name = "提箱地点", width = 15)
    @ApiModelProperty(value = "提箱地点")
	private String takeContainerAddress;
	/**拖箱地点（出口为装箱地点）*/
	@Excel(name = "拖箱地点（出口为装箱地点）", width = 15)
    @ApiModelProperty(value = "拖箱地点（出口为装箱地点）")
	private String dragContainerAddress;
	/**返箱地点*/
	@Excel(name = "返箱地点", width = 15)
    @ApiModelProperty(value = "返箱地点")
	private String returnContainerAddress;
	/**详细地址（出口为装箱详细地址）*/
	@Excel(name = "详细地址（出口为装箱详细地址）", width = 15)
    @ApiModelProperty(value = "详细地址（出口为装箱详细地址）")
	private String detailAddress;
	/**接单备注*/
	@Excel(name = "接单备注", width = 15)
    @ApiModelProperty(value = "接单备注")
	private String orderRemark;
	/**业务类型*/
	@Excel(name = "进出口类型", width = 15, dicCode = "business_type")
	@ApiModelProperty(value = "业务类型")
	@Dict(dicCode = "business_type")
	private Integer businessType;
	/**是否退载*/
	@Excel(name = "是否退载", width = 15, dicCode = "take_back")
	@ApiModelProperty(value = "是否退载")
	@Dict(dicCode = "take_back")
	private Integer takeBack;



	/**停靠码头*/
	@Excel(name = "停靠码头", width = 15)
	@ApiModelProperty(value = "停靠码头")
	private String outdepotnameCn;

	/**码头放行*/
	@Excel(name = "码头放行日期", width = 15)
	@ApiModelProperty(value = "码头放行")
	private String mtPassdate;




	/**箱进码头*/
	@Excel(name = "箱进码头", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "箱进码头")
	private Date ieDate;

	/**码头进场*/
	@Excel(name = "码头进场", width = 15)
	@ApiModelProperty(value = "码头进场")
	private String mtIndate;

	/**码头出场*/
	@Excel(name = "码头出场", width = 15)
	@ApiModelProperty(value = "码头出场")
	private String mtOutdate;



	/**返箱堆场*/
	@Excel(name = "返箱堆场", width = 15)
	@ApiModelProperty(value = "返箱堆场")
	private String indepotnameCn;
	/**堆场进闸*/
	@Excel(name = "堆场进闸", width = 15)
	@ApiModelProperty(value = "堆场进闸")
	private String dcIndate;

	/**堆场出闸*/
	@Excel(name = "堆场出闸", width = 15)
	@ApiModelProperty(value = "堆场出闸")
	private String dcOutdate;
}
