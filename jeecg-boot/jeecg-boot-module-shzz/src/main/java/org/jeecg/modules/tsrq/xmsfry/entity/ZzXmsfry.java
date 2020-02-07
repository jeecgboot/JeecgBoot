package org.jeecg.modules.tsrq.xmsfry.entity;

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
 * @Description: 刑满释放人员
 * @Author: jeecg-boot
 * @Date:   2020-02-07
 * @Version: V1.0
 */
@Data
@TableName("zz_xmsfry")
public class ZzXmsfry implements Serializable {
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
	/**姓名*/
	@Excel(name = "姓名", width = 15)
    @ApiModelProperty(value = "姓名")
    private java.lang.String name;
	/**身份证*/
	@Excel(name = "身份证", width = 15)
    @ApiModelProperty(value = "身份证")
    private java.lang.String sfz;
	/**性别*/
	@Excel(name = "性别", width = 15)
    @ApiModelProperty(value = "性别")
    private java.lang.String sex;
	/**手机号*/
	@Excel(name = "手机号", width = 15)
    @ApiModelProperty(value = "手机号")
    private java.lang.String phone;
	/**是否累犯*/
	@Excel(name = "是否累犯", width = 15)
    @ApiModelProperty(value = "是否累犯")
    private java.lang.String sflf;
	/**原罪名*/
	@Excel(name = "原罪名", width = 15)
    @ApiModelProperty(value = "原罪名")
    private java.lang.String yzm;
	/**原判刑期*/
	@Excel(name = "原判刑期", width = 15)
    @ApiModelProperty(value = "原判刑期")
    private java.lang.String ypxq;
	/**服刑场所*/
	@Excel(name = "服刑场所", width = 15)
    @ApiModelProperty(value = "服刑场所")
    private java.lang.String fxcs;
	/**未安置原因*/
	@Excel(name = "未安置原因", width = 15)
    @ApiModelProperty(value = "未安置原因")
    private java.lang.String wazyy;
	/**帮教情况*/
	@Excel(name = "帮教情况", width = 15)
    @ApiModelProperty(value = "帮教情况")
    private java.lang.String bjqk;
	/**是否重新犯罪*/
	@Excel(name = "是否重新犯罪", width = 15)
    @ApiModelProperty(value = "是否重新犯罪")
    private java.lang.String sfcxfz;
	/**重新犯罪罪名*/
	@Excel(name = "重新犯罪罪名", width = 15)
    @ApiModelProperty(value = "重新犯罪罪名")
    private java.lang.String cxfzzm;
	/**释放日期*/
	@Excel(name = "释放日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "释放日期")
    private java.util.Date sfDate;
	/**危险评估类型*/
	@Excel(name = "危险评估类型", width = 15)
    @ApiModelProperty(value = "危险评估类型")
    private java.lang.String wxpglx;
	/**衔接日期*/
	@Excel(name = "衔接日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "衔接日期")
    private java.util.Date xjDate;
	/**衔接情况*/
	@Excel(name = "衔接情况", width = 15)
    @ApiModelProperty(value = "衔接情况")
    private java.lang.String xjqk;
	/**安置日期*/
	@Excel(name = "安置日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "安置日期")
    private java.util.Date azDate;
	/**安置情况*/
	@Excel(name = "安置情况", width = 15)
    @ApiModelProperty(value = "安置情况")
    private java.lang.String azqk;
}
