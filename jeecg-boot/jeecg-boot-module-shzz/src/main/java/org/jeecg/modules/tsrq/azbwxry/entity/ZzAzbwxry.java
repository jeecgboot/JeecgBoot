package org.jeecg.modules.tsrq.azbwxry.entity;

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
 * @Description: 艾滋病危险人员
 * @Author: jeecg-boot
 * @Date:   2020-02-07
 * @Version: V1.0
 */
@Data
@TableName("zz_azbwxry")
public class ZzAzbwxry implements Serializable {
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
	/**证件号码*/
	@Excel(name = "证件号码", width = 15)
    @ApiModelProperty(value = "证件号码")
    private java.lang.String sfz;
	/**性别*/
	@Excel(name = "性别", width = 15)
    @ApiModelProperty(value = "性别")
    private java.lang.String sex;
	/**手机号*/
	@Excel(name = "手机号", width = 15)
    @ApiModelProperty(value = "手机号")
    private java.lang.String phone;
	/**感染途径*/
	@Excel(name = "感染途径", width = 15)
    @ApiModelProperty(value = "感染途径")
    private java.lang.String grtj;
	/**是否有犯罪史*/
	@Excel(name = "是否有犯罪史", width = 15)
    @ApiModelProperty(value = "是否有犯罪史")
    private java.lang.String sfyfzs;
	/**违法犯罪情况*/
	@Excel(name = "违法犯罪情况", width = 15)
    @ApiModelProperty(value = "违法犯罪情况")
    private java.lang.String wffzqk;
	/**案件类别*/
	@Excel(name = "案件类别", width = 15)
    @ApiModelProperty(value = "案件类别")
    private java.lang.String ajlb;
	/**关注类型*/
	@Excel(name = "关注类型", width = 15)
    @ApiModelProperty(value = "关注类型")
    private java.lang.String gzlx;
	/**帮扶情况*/
	@Excel(name = "帮扶情况", width = 15)
    @ApiModelProperty(value = "帮扶情况")
    private java.lang.String bfqk;
	/**帮扶人姓名*/
	@Excel(name = "帮扶人姓名", width = 15)
    @ApiModelProperty(value = "帮扶人姓名")
    private java.lang.String bfrName;
	/**帮扶人电话*/
	@Excel(name = "帮扶人电话", width = 15)
    @ApiModelProperty(value = "帮扶人电话")
    private java.lang.String bfrPhone;
	/**收治情况*/
	@Excel(name = "收治情况", width = 15)
    @ApiModelProperty(value = "收治情况")
    private java.lang.String szqk;
	/**收治机构名称*/
	@Excel(name = "收治机构名称", width = 15)
    @ApiModelProperty(value = "收治机构名称")
    private java.lang.String szjgName;
}
