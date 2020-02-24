package com.ibm.fms.modules.vendor.entity;

import java.io.Serializable;
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

/**
 * @Description: 供应商基本信息
 * @Author: jeecg-boot
 * @Date:   2019-08-27
 * @Version: V1.0
 */
@Data
@TableName("tab_jtmdm_supplier_info")
public class TabJtmdmSupplierInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**供应商代码*/
	@Excel(name = "供应商代码", width = 15)
	private java.lang.String vendorCode;
	/**供应商名称*/
	@Excel(name = "供应商名称", width = 15)
	private java.lang.String name;
	/**供应商简称*/
	@Excel(name = "供应商简称", width = 15)
	private java.lang.String sname;
	/**供应商英文名*/
	@Excel(name = "供应商英文名", width = 15)
	private java.lang.String ename;
	/**供应商别名*/
	@Excel(name = "供应商别名", width = 15)
	private java.lang.String aliasName;
	/**法人注册类型*/
	@Excel(name = "法人注册类型", width = 15)
	private java.lang.String orgType;
	/**法人注册编号*/
	@Excel(name = "法人注册编号", width = 15)
	private java.lang.String commerceBusinessLicenseNo;
	/**供应商合作状态*/
	@Excel(name = "供应商合作状态", width = 15)
	private java.lang.Integer cooperationStatus;
	/**法人代表*/
	@Excel(name = "法人代表", width = 15)
	private java.lang.String legalPerson;
	/**国税税务登记证号*/
	@Excel(name = "国税税务登记证号", width = 15)
	private java.lang.String countryTaxRegistryNo;
	/**地税税务登记证号*/
	@Excel(name = "地税税务登记证号", width = 15)
	private java.lang.String localTaxRegistryNo;
	/**是否关联交易供应商*/
	@Excel(name = "是否关联交易供应商", width = 15)
	private java.lang.String relateTransationSup;
	/**关联方代码*/
	@Excel(name = "关联方代码", width = 15)
	private java.lang.String relateCode;
	/**关联方名称*/
	@Excel(name = "关联方名称", width = 15)
	private java.lang.String relateName;
	/**银行资信等级*/
	@Excel(name = "银行资信等级", width = 15)
	private java.lang.String bankCreditRating;
	/**所属行业*/
	@Excel(name = "所属行业", width = 15)
	private java.lang.String industry;
	/**注册地址*/
	@Excel(name = "注册地址", width = 15)
	private java.lang.String registeredAddress;
	/**注册资金*/
	@Excel(name = "注册资金", width = 15)
	private java.lang.String registeredCapital;
	/**公司网址*/
	@Excel(name = "公司网址", width = 15)
	private java.lang.String websiteUrl;
	/**邮政编码*/
	@Excel(name = "邮政编码", width = 15)
	private java.lang.String postcode;
	/**公司传真*/
	@Excel(name = "公司传真", width = 15)
	private java.lang.String fax;
	/**主要联系人*/
	@Excel(name = "主要联系人", width = 15)
	private java.lang.String linkmanName;
	/**主要联系人电话*/
	@Excel(name = "主要联系人电话", width = 15)
	private java.lang.String linkmanPhone;
	/**主要联系人EMAIL地址*/
	@Excel(name = "主要联系人EMAIL地址", width = 15)
	private java.lang.String linkmanEmail;
	/**注册国家*/
	@Excel(name = "注册国家", width = 15)
	private java.lang.String registerCountry;
	/**注册省份*/
	@Excel(name = "注册省份", width = 15)
	private java.lang.String registerProvide;
	/**注册城市*/
	@Excel(name = "注册城市", width = 15)
	private java.lang.String registerCity;
	/**税务类型*/
	@Excel(name = "税务类型", width = 15)
	private java.lang.String taxType;
	/**是否有效*/
	@Excel(name = "是否有效", width = 15)
	private java.lang.String isvalidate;
	/**审核状态*/
	@Excel(name = "审核状态", width = 15)
	private java.lang.String status;
	/**创建人编号*/
	@Excel(name = "创建人编号", width = 15)
	private java.lang.Integer createUserId;
	/**创建人名称*/
	@Excel(name = "创建人名称", width = 15)
	private java.lang.String createUserName;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date createTime;
	/**更新人编号*/
	@Excel(name = "更新人编号", width = 15)
	private java.lang.Integer updateUserId;
	/**更新人名称*/
	@Excel(name = "更新人名称", width = 15)
	private java.lang.String updateUserName;
	/**更新时间*/
	@Excel(name = "更新时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date updateTime;
	/**集团一采映射编码*/
	@Excel(name = "集团一采映射编码", width = 15)
	private java.lang.String groupFirstPurchaseCode;
	/**组织机构代码证号*/
	@Excel(name = "组织机构代码证号", width = 15)
	private java.lang.String organizationCardNumber;
	/**适用增值税率*/
	@Excel(name = "适用增值税率", width = 15)
	private java.lang.String vatTaxRatecode;
	/**合作有效期限*/
	@Excel(name = "合作有效期限", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date contractValidateDate;
	/**法人注册证件有效期限*/
	@Excel(name = "法人注册证件有效期限", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date corpRegValidateDate;
	/**经营范围*/
	@Excel(name = "经营范围", width = 15)
	private java.lang.String businessScope;
	/**成立时间*/
	@Excel(name = "成立时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date regtime;
	/**操作开始日期*/
	@Excel(name = "操作开始日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date operationTermBegin;
	/**操作终止日期*/
	@Excel(name = "操作终止日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date operationTermEnd;
	/**公司类型*/
	@Excel(name = "公司类型", width = 15)
	private java.lang.String companyType;
	/**最新年检日期*/
	@Excel(name = "最新年检日期", width = 15)
	private java.lang.String latestInspectionDate;
	/**集采供应商标识*/
	@Excel(name = "集采供应商标识", width = 15)
	private java.math.BigDecimal centralPurchasingSign;
	/**集团统一供应商编码*/
	@Excel(name = "集团统一供应商编码", width = 15)
	private java.lang.String groupVendorNumber;
	/**参考字段1*/
	@Excel(name = "参考字段1", width = 15)
	private java.lang.String reference1;
	/**参考字段2*/
	@Excel(name = "参考字段2", width = 15)
	private java.lang.String reference2;
	/**参考字段3*/
	@Excel(name = "参考字段3", width = 15)
	private java.lang.String reference3;
	/**参考字段4*/
	@Excel(name = "参考字段4", width = 15)
	private java.lang.String reference4;
	/**参考字段5*/
	@Excel(name = "参考字段5", width = 15)
	private java.lang.String reference5;
	/**集团供应商ID*/
	@Excel(name = "集团供应商ID", width = 15)
	private java.lang.Integer vendorId;
	/**是否三证合一*/
	@Excel(name = "是否三证合一", width = 15)
	private java.lang.String certificateFlag;
	/**是否交易方*/
	@Excel(name = "是否交易方", width = 15)
	private java.lang.String tradingParty;
	/**注册币种*/
	@Excel(name = "注册币种", width = 15)
	private java.lang.String registryCurrency;
	/**来源系统*/
	@Excel(name = "来源系统", width = 15)
	private java.lang.String sourceCode;
	/**所属组织名称*/
	@Excel(name = "所属组织名称", width = 15)
	private java.lang.String orgName;
	/**所属组织代码*/
	@Excel(name = "所属组织代码", width = 15)
	private java.lang.String ouCode;
	/**业务合作期间*/
	@Excel(name = "业务合作期间", width = 15)
	private java.lang.String businessTimeliMit;
	/**是否上市公司*/
	@Excel(name = "是否上市公司", width = 15)
	private java.lang.String stockFlag;
	/**办公地址*/
	@Excel(name = "办公地址", width = 15)
	private java.lang.String officeAddress;
	/**虚拟供应商*/
	@Excel(name = "虚拟供应商", width = 15)
	private java.lang.String virtualVendor;
	/**身份证号码*/
	@Excel(name = "身份证号码", width = 15)
	private java.lang.String identifyCode;
	/**统一社会信用 代码*/
	@Excel(name = "统一社会信用 代码", width = 15)
	private java.lang.String socIden;
	/**虚拟供应商类型*/
	@Excel(name = "虚拟供应商类型", width = 15)
	private java.lang.String virtualType;
	/**税务登记地址*/
	@Excel(name = "税务登记地址", width = 15)
	private java.lang.String taxRegAddress;
	/**客户代码*/
	@Excel(name = "客户代码", width = 15)
	private java.lang.String customerNumber;
	/**供应商类型*/
	@Excel(name = "供应商类型", width = 15)
	private java.lang.String vendorType;
}
