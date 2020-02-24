package com.ibm.fms.modules.reimburse.base.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.jeecgframework.poi.excel.annotation.Excel;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 主数据供应商信息
 * @Author: jeecg-boot
 * @Date:   2020-01-17
 * @Version: V1.0
 */
@Data
@TableName("mdm_vendor_info")
public class MdmVendorInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private java.lang.String id;
	/**供应商 ID*/
    @Excel(name = "供应商 ID", width = 15)
    private java.lang.Integer vendorId;
	/**供应商名称*/
    @Excel(name = "供应商名称", width = 15)
    private java.lang.String vendorName;
	/**供应商编号（主数据编号）*/
    @Excel(name = "供应商编号（主数据编号）", width = 15)
    private java.lang.String mdCode;
	/**供应商类型*/
    @Excel(name = "供应商类型", width = 15)
    private java.lang.String vendorType;
	/**是否三证合一：Y - 是，N - 否*/
    @Excel(name = "是否三证合一：Y - 是，N - 否", width = 15)
    private java.lang.String certificateFlag;
	/**组织机构代码*/
    @Excel(name = "组织机构代码", width = 15)
    private java.lang.String orgcertNumber;
	/**身份证号码*/
    @Excel(name = "身份证号码", width = 15)
    private java.lang.String identifyCode;
	/**统一社会信用代码*/
    @Excel(name = "统一社会信用代码", width = 15)
    private java.lang.String socIden;
	/**供应商创建时间*/
    @Excel(name = "供应商创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date vendorCreationDate;
	/**纳税标识：YBNSR 一般纳税人，XGM 小规模纳税人，FZZS 非增值税*/
    @Excel(name = "纳税标识：YBNSR 一般纳税人，XGM 小规模纳税人，FZZS 非增值税", width = 15)
    private java.lang.String vatFlag;
	/**纳税登记编号*/
    @Excel(name = "纳税登记编号", width = 15)
    private java.lang.String taxRegistrationNumber;
	/**是否上市公司：Y - 是，N - 否*/
    @Excel(name = "是否上市公司：Y - 是，N - 否", width = 15)
    private java.lang.String stockFlag;
	/**是否内部关联方：ZB-总部,QN-其他内部关联方,WZ-外部重要关联方,QW-其他外部供应商*/
    @Excel(name = "是否内部关联方：ZB-总部,QN-其他内部关联方,WZ-外部重要关联方,QW-其他外部供应商", width = 15)
    private java.lang.String innerFlag;
	/**内部关联方标识（往来段）*/
    @Excel(name = "内部关联方标识（往来段）", width = 15)
    private java.lang.String innerCode;
	/**是否交易方：Y - 是，N - 否*/
    @Excel(name = "是否交易方：Y - 是，N - 否", width = 15)
    private java.lang.String tradingParty;
	/**客户编码*/
    @Excel(name = "客户编码", width = 15)
    private java.lang.String cuetomerNumber;
	/**组织机构类型*/
    @Excel(name = "组织机构类型", width = 15)
    private java.lang.String corporationType;
	/**供应商最后更新时间*/
    @Excel(name = "供应商最后更新时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date lastUpdateDate;
	/**供应商状态：Y - 是，N - 否*/
    @Excel(name = "供应商状态：Y - 是，N - 否", width = 15)
    private java.lang.String vendorStatus;
	/**供应商曾用名*/
    @Excel(name = "供应商曾用名", width = 15)
    private java.lang.String vendorOldName;
	/**法定代表人/责任人*/
    @Excel(name = "法定代表人/责任人", width = 15)
    private java.lang.String legalRepresentative;
	/**税务登记地址*/
    @Excel(name = "税务登记地址", width = 15)
    private java.lang.String taxRegAddress;
	/**办公地址*/
    @Excel(name = "办公地址", width = 15)
    private java.lang.String officeAddress;
	/**注册地址*/
    @Excel(name = "注册地址", width = 15)
    private java.lang.String registryAddress;
	/**经营范围*/
    @Excel(name = "经营范围", width = 15)
    private java.lang.String businessRange;
	/**公司类型*/
    @Excel(name = "公司类型", width = 15)
    private java.lang.String companyType;
	/**营业期限*/
    @Excel(name = "营业期限", width = 15)
    private java.lang.String businessTimelimit;
	/**注册资金*/
    @Excel(name = "注册资金", width = 15)
    private java.lang.String registryFund;
	/**注册币种*/
    @Excel(name = "注册币种", width = 15)
    private java.lang.String registryCurrency;
	/**供应商所在国家*/
    @Excel(name = "供应商所在国家", width = 15)
    private java.lang.String country;
	/**供应商所在省份*/
    @Excel(name = "供应商所在省份", width = 15)
    private java.lang.String province;
	/**供应商所在城市*/
    @Excel(name = "供应商所在城市", width = 15)
    private java.lang.String city;
	/**供应商邮政编码*/
    @Excel(name = "供应商邮政编码", width = 15)
    private java.lang.String zip;
	/**来源系统*/
    @Excel(name = "来源系统", width = 15)
    private java.lang.String sourceCode;
	/**是否虚拟供应商：Y - 是，N - 否*/
    @Excel(name = "是否虚拟供应商：Y - 是，N - 否", width = 15)
    private java.lang.String virtualVendor;
	/**虚拟供应商类型*/
    @Excel(name = "虚拟供应商类型", width = 15)
    private java.lang.String virtualType;
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
