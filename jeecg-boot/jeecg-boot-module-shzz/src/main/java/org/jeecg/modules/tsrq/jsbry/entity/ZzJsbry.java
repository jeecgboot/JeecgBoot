package org.jeecg.modules.tsrq.jsbry.entity;

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
 * @Description: 精神病人员
 * @Author: jeecg-boot
 * @Date:   2020-02-07
 * @Version: V1.0
 */
@Data
@TableName("zz_jsbry")
public class ZzJsbry implements Serializable {
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
	/**家庭经济状况*/
	@Excel(name = "家庭经济状况", width = 15)
    @ApiModelProperty(value = "家庭经济状况")
    private java.lang.String jtjjzk;
	/**是否纳入低保*/
	@Excel(name = "是否纳入低保", width = 15)
    @ApiModelProperty(value = "是否纳入低保")
    private java.lang.String sfnrdb;
	/**监护人姓名*/
	@Excel(name = "监护人姓名", width = 15)
    @ApiModelProperty(value = "监护人姓名")
    private java.lang.String jhrName;
	/**监护人身份证*/
	@Excel(name = "监护人身份证", width = 15)
    @ApiModelProperty(value = "监护人身份证")
    private java.lang.String jhrShz;
	/**监护人电话*/
	@Excel(name = "监护人电话", width = 15)
    @ApiModelProperty(value = "监护人电话")
    private java.lang.String jhrPhone;
	/**初次发病日期*/
	@Excel(name = "初次发病日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "初次发病日期")
    private java.util.Date ccfbDate;
	/**目前诊断类型*/
	@Excel(name = "目前诊断类型", width = 15)
    @ApiModelProperty(value = "目前诊断类型")
    private java.lang.String mqzdlx;
	/**有无肇事肇祸*/
	@Excel(name = "有无肇事肇祸", width = 15)
    @ApiModelProperty(value = "有无肇事肇祸")
    private java.lang.String ywzszh;
	/**肇事肇祸次数*/
	@Excel(name = "肇事肇祸次数", width = 15)
    @ApiModelProperty(value = "肇事肇祸次数")
    private java.lang.String zszhcs;
	/**上次肇事肇祸日期*/
	@Excel(name = "上次肇事肇祸日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "上次肇事肇祸日期")
    private java.util.Date sczhrq;
	/**危险评估登记*/
	@Excel(name = "危险评估登记", width = 15)
    @ApiModelProperty(value = "危险评估登记")
    private java.lang.String wxpgdj;
	/**治疗情况*/
	@Excel(name = "治疗情况", width = 15)
    @ApiModelProperty(value = "治疗情况")
    private java.lang.String zlqk;
	/**治疗医院名称*/
	@Excel(name = "治疗医院名称", width = 15)
    @ApiModelProperty(value = "治疗医院名称")
    private java.lang.String zlyymc;
	/**住院治疗原因*/
	@Excel(name = "住院治疗原因", width = 15)
    @ApiModelProperty(value = "住院治疗原因")
    private java.lang.String zyzlyy;
	/**康复训练机构*/
	@Excel(name = "康复训练机构", width = 15)
    @ApiModelProperty(value = "康复训练机构")
    private java.lang.String kfxljg;
	/**参与管理人员*/
	@Excel(name = "参与管理人员", width = 15)
    @ApiModelProperty(value = "参与管理人员")
    private java.lang.String cyglry;
	/**帮扶情况*/
	@Excel(name = "帮扶情况", width = 15)
    @ApiModelProperty(value = "帮扶情况")
    private java.lang.String bfqk;
}
