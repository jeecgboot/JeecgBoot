package com.ibm.fms.modules.reimburse.base.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.jeecg.common.aspect.annotation.Dict;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;

/**
 * @Description: 主数据供应商归属组织
 * @Author: jeecg-boot
 * @Date:   2020-01-17
 * @Version: V1.0
 */
@Data
@TableName("mdm_vendor_company_info")
public class MdmVendorCompanyInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**编号*/
	@TableId(type = IdType.ID_WORKER_STR)
	private java.lang.String id;
	/**供应商公司ID*/
	@Excel(name = "供应商公司ID", width = 15)
	private java.lang.Integer vendorCompanyId;
	/**供应商ID*/
	private java.lang.Integer vendorId;
	/**供应商编号（主数据编码）*/
	@Excel(name = "供应商编号（主数据编码）", width = 15)
	private java.lang.String mdCode;
	/**OU代码（公司段ID）*/
	@Excel(name = "OU代码（公司段ID）", width = 15)
	private java.lang.String ouCode;
	/**OU名称（公司段名称）*/
	@Excel(name = "OU名称（公司段名称）", width = 15)
	private java.lang.String orgName;
	/**父级节点*/
	@Excel(name = "父级节点", width = 15)
	private java.lang.String pid;
	/**公司层是否有效*/
	@Excel(name = "公司层是否有效", width = 15)
	private java.lang.String validFlag;
	/**是否有子节点*/
	@Excel(name = "是否有子节点", width = 15)
    @Dict(dicCode = "yn")
	private java.lang.String hasChild;
	/**公司层创建时间*/
	@Excel(name = "公司层创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date creationDate;
	/**失效日期*/
	@Excel(name = "失效日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date expiryDate;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date createTime;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	private java.lang.String createBy;
	/**更新时间*/
	@Excel(name = "更新时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date updateTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
	private java.lang.String updateBy;
}
