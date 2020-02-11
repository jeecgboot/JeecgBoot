package org.jeecg.modules.shza.wlaq.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 寄递物流安全管理
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Data
@TableName("zz_wlaq")
public class ZzWlaq implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**工商执照注册号*/
	@Excel(name = "工商执照注册号", width = 15)
    @ApiModelProperty(value = "工商执照注册号")
    private java.lang.String gszzCode;
	/**企业名称*/
	@Excel(name = "企业名称", width = 15)
    @ApiModelProperty(value = "企业名称")
    private java.lang.String businessName;
	/**所在地*/
	@Excel(name = "所在地", width = 15)
    @ApiModelProperty(value = "所在地")
    private java.lang.String adress;
	/**企业详址*/
	@Excel(name = "企业详址", width = 15)
    @ApiModelProperty(value = "企业详址")
    private java.lang.String qyAdress;
	/**企业联系方式*/
	@Excel(name = "企业联系方式", width = 15)
    @ApiModelProperty(value = "企业联系方式")
    private java.lang.String qyPhone;
	/**企业负责人姓名*/
	@Excel(name = "企业负责人姓名", width = 15)
    @ApiModelProperty(value = "企业负责人姓名")
    private java.lang.String qyfzrName;
	/**负责人联系方式*/
	@Excel(name = "负责人联系方式", width = 15)
    @ApiModelProperty(value = "负责人联系方式")
    private java.lang.String fzrPhone;
	/**登记注册类型*/
	@Excel(name = "登记注册类型", width = 15)
    @ApiModelProperty(value = "登记注册类型")
    private java.lang.String djzcType;
	/**控股情况*/
	@Excel(name = "控股情况", width = 15)
    @ApiModelProperty(value = "控股情况")
    private java.lang.String kgqk;
	/**经营范围*/
	@Excel(name = "经营范围", width = 15)
    @ApiModelProperty(value = "经营范围")
    private java.lang.String jyfw;
	/**企业类型*/
	@Excel(name = "企业类型", width = 15)
    @ApiModelProperty(value = "企业类型")
    private java.lang.String businessType;
	/**服务品牌*/
	@Excel(name = "服务品牌", width = 15)
    @ApiModelProperty(value = "服务品牌")
    private java.lang.String fwpp;
	/**从业人员数量*/
	@Excel(name = "从业人员数量", width = 15)
    @ApiModelProperty(value = "从业人员数量")
    private java.lang.Integer cyryNum;
	/**监控摄像机数量*/
	@Excel(name = "监控摄像机数量", width = 15)
    @ApiModelProperty(value = "监控摄像机数量")
    private java.lang.Integer sxjNum;
	/**X光机数量*/
	@Excel(name = "X光机数量", width = 15)
    @ApiModelProperty(value = "X光机数量")
    private java.lang.Integer xgj;
	/**是否落实100%先验视后封箱*/
	@Excel(name = "是否落实100%先验视后封箱", width = 15)
    @ApiModelProperty(value = "是否落实100%先验视后封箱")
    private java.lang.String sfys;
	/**是否落实100%寄递实名制*/
	@Excel(name = "是否落实100%寄递实名制", width = 15)
    @ApiModelProperty(value = "是否落实100%寄递实名制")
    private java.lang.String sfsmz;
	/**是否落实100%X光机安检*/
	@Excel(name = "是否落实100%X光机安检", width = 15)
    @ApiModelProperty(value = "是否落实100%X光机安检")
    private java.lang.String sfxgj;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.String longitude;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private java.lang.String latitude;
}
