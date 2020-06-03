package org.jeecg.modules.business.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @Description: 在线监控验收信息
 * @Author: jeecg-boot
 * @Date:   2020-06-03
 * @Version: V1.0
 */
@Data
@TableName("company_online_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="company_online_info对象", description="在线监控验收信息")
public class CompanyOnlineInfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**数据状态*/
	@Excel(name = "数据状态", width = 15)
    @ApiModelProperty(value = "数据状态")
    private String status;
	/**企业id*/
	@Excel(name = "企业id", width = 15)
    @ApiModelProperty(value = "企业id")
    private String companyId;
	/**在线设备名称/型号*/
	@Excel(name = "在线设备名称/型号", width = 15)
    @ApiModelProperty(value = "在线设备名称/型号")
    private String equipmentName;
	/**设备生产厂家*/
	@Excel(name = "设备生产厂家", width = 15)
    @ApiModelProperty(value = "设备生产厂家")
    private String equipmentManufacturers;
	/**运维单位*/
	@Excel(name = "运维单位", width = 15)
    @ApiModelProperty(value = "运维单位")
    private String operationalUnit;
	/**投入使用日期*/
	@Excel(name = "投入使用日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "投入使用日期")
    private Date usedTime;
	/**安装位置*/
	@Excel(name = "安装位置", width = 15)
    @ApiModelProperty(value = "安装位置")
    private String installLocation;
	/**在线监控验收材料*/
	@Excel(name = "在线监控验收材料", width = 15)
    @ApiModelProperty(value = "在线监控验收材料")
    private String files;
}
