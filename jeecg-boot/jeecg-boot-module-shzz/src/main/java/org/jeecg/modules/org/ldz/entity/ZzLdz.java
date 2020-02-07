package org.jeecg.modules.org.ldz.entity;

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
 * @Description: 楼栋长
 * @Author: jeecg-boot
 * @Date:   2020-02-06
 * @Version: V1.0
 */
@Data
@TableName("zz_ldz")
public class ZzLdz implements Serializable {
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
	/**小区名称*/
	@Excel(name = "小区名称", width = 15)
    @ApiModelProperty(value = "小区名称")
    private java.lang.String villageName;
	/**楼栋名称*/
	@Excel(name = "楼栋名称", width = 15)
    @ApiModelProperty(value = "楼栋名称")
    private java.lang.String buildingName;
	/**建筑面积*/
	@Excel(name = "建筑面积", width = 15)
    @ApiModelProperty(value = "建筑面积")
    private java.lang.Integer buildArea;
	/**层数*/
	@Excel(name = "层数", width = 15)
    @ApiModelProperty(value = "层数")
    private java.lang.Integer floorNum;
	/**单元楼*/
	@Excel(name = "单元楼", width = 15)
    @ApiModelProperty(value = "单元楼")
    private java.lang.String dyl;
	/**楼栋户数*/
	@Excel(name = "楼栋户数", width = 15)
    @ApiModelProperty(value = "楼栋户数")
    private java.lang.Integer ldhs;
	/**楼栋人数*/
	@Excel(name = "楼栋人数", width = 15)
    @ApiModelProperty(value = "楼栋人数")
    private java.lang.Integer ldrs;
	/**楼栋长姓名*/
	@Excel(name = "楼栋长姓名", width = 15)
    @ApiModelProperty(value = "楼栋长姓名")
    private java.lang.String ldzName;
	/**性别*/
	@Excel(name = "性别", width = 15)
    @ApiModelProperty(value = "性别")
    private java.lang.String sex;
	/**民族*/
	@Excel(name = "民族", width = 15)
    @ApiModelProperty(value = "民族")
    private java.lang.String folk;
	/**政治面貌*/
	@Excel(name = "政治面貌", width = 15)
    @ApiModelProperty(value = "政治面貌")
    private java.lang.String politicalStatus;
	/**出生日期*/
	@Excel(name = "出生日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "出生日期")
    private java.util.Date birthday;
	/**学历*/
	@Excel(name = "学历", width = 15)
    @ApiModelProperty(value = "学历")
    private java.lang.String education;
	/**手机号码*/
	@Excel(name = "手机号码", width = 15)
    @ApiModelProperty(value = "手机号码")
    private java.lang.String phone;
	/**固定电话*/
	@Excel(name = "固定电话", width = 15)
    @ApiModelProperty(value = "固定电话")
    private java.lang.String tel;
	/**所在地*/
	@Excel(name = "所在地", width = 15)
    @ApiModelProperty(value = "所在地")
    private java.lang.String szd;
	/**所在地详址*/
	@Excel(name = "所在地详址", width = 15)
    @ApiModelProperty(value = "所在地详址")
    private java.lang.String szdxz;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.String longitude;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private java.lang.String latitude;
}
