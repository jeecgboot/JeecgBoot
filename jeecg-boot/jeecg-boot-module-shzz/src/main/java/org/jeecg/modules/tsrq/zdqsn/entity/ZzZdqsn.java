package org.jeecg.modules.tsrq.zdqsn.entity;

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
 * @Description: 重点青少年人员
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Data
@TableName("zz_zdqsn")
public class ZzZdqsn implements Serializable {
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
	/**手机号码*/
	@Excel(name = "手机号码", width = 15)
    @ApiModelProperty(value = "手机号码")
    private java.lang.String phone;
	/**人员类型*/
	@Excel(name = "人员类型", width = 15)
    @ApiModelProperty(value = "人员类型")
    private java.lang.String rylx;
	/**家庭情况*/
	@Excel(name = "家庭情况", width = 15)
    @ApiModelProperty(value = "家庭情况")
    private java.lang.String jtqk;
	/**监护人身份证*/
	@Excel(name = "监护人身份证", width = 15)
    @ApiModelProperty(value = "监护人身份证")
    private java.lang.String jhrSfz;
	/**监护人姓名*/
	@Excel(name = "监护人姓名", width = 15)
    @ApiModelProperty(value = "监护人姓名")
    private java.lang.String jhrName;
	/**与监护人关系*/
	@Excel(name = "与监护人关系", width = 15)
    @ApiModelProperty(value = "与监护人关系")
    private java.lang.String jhrGx;
	/**监护人电话*/
	@Excel(name = "监护人电话", width = 15)
    @ApiModelProperty(value = "监护人电话")
    private java.lang.String jhrPhone;
	/**监护人住址*/
	@Excel(name = "监护人住址", width = 15)
    @ApiModelProperty(value = "监护人住址")
    private java.lang.String jhrZz;
	/**是否违法犯罪*/
	@Excel(name = "是否违法犯罪", width = 15)
    @ApiModelProperty(value = "是否违法犯罪")
    private java.lang.String sfwffz;
	/**违法犯罪情况*/
	@Excel(name = "违法犯罪情况", width = 15)
    @ApiModelProperty(value = "违法犯罪情况")
    private java.lang.String wffzqk;
	/**帮扶人姓名*/
	@Excel(name = "帮扶人姓名", width = 15)
    @ApiModelProperty(value = "帮扶人姓名")
    private java.lang.String bfrName;
	/**帮扶人电话*/
	@Excel(name = "帮扶人电话", width = 15)
    @ApiModelProperty(value = "帮扶人电话")
    private java.lang.String bfrPhone;
	/**帮扶手段*/
	@Excel(name = "帮扶手段", width = 15)
    @ApiModelProperty(value = "帮扶手段")
    private java.lang.String bfsd;
	/**帮扶情况*/
	@Excel(name = "帮扶情况", width = 15)
    @ApiModelProperty(value = "帮扶情况")
    private java.lang.String bfqk;
}
