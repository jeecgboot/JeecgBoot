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
 * @Description: 供应商联系人
 * @Author: jeecg-boot
 * @Date:   2019-08-27
 * @Version: V1.0
 */
@Data
@TableName("tab_jtmdm_supplier_linkman")
public class TabJtmdmSupplierLinkman implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**联系人ID*/
	@TableId(type = IdType.UUID)
	private java.lang.Integer id;
	/**供应商编号*/
	@Excel(name = "供应商编号", width = 15)
	private java.lang.String supplierId;
	/**联系人姓名*/
	@Excel(name = "联系人姓名", width = 15)
	private java.lang.String linkmanName;
	/**联系人电话*/
	@Excel(name = "联系人电话", width = 15)
	private java.lang.String linkmanPhone;
	/**联系人邮件*/
	@Excel(name = "联系人邮件", width = 15)
	private java.lang.String linkmanEmail;
	/**创建人编号*/
	@Excel(name = "创建人编号", width = 15)
	private java.lang.Integer createUserId;
	/**供应创建人名称*/
	@Excel(name = "供应创建人名称", width = 15)
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
	/**联系人手机号*/
	@Excel(name = "联系人手机号", width = 15)
	private java.lang.String linkmanMobile;
	/**联系人传真号*/
	@Excel(name = "联系人传真号", width = 15)
	private java.lang.String linkmanFax;
	/**联系人收件地址*/
	@Excel(name = "联系人收件地址", width = 15)
	private java.lang.String linkmanMailingAddress;
	/**邮编*/
	@Excel(name = "邮编", width = 15)
	private java.lang.String zipCode;
	/**职责*/
	@Excel(name = "职责", width = 15)
	private java.lang.String duty;
	/**负责区域*/
	@Excel(name = "负责区域", width = 15)
	private java.lang.String responsibileRegion;
	/**负责产品*/
	@Excel(name = "负责产品", width = 15)
	private java.lang.String responsibileProduct;
	/**供应商头表ID*/
	@Excel(name = "供应商头表ID", width = 15)
	private java.lang.Integer vendorId;
	/**供应商公司层ID*/
	@Excel(name = "供应商公司层ID", width = 15)
	private java.lang.Integer vendorCompantId;
	/**供应商联系ID*/
	@Excel(name = "供应商联系ID", width = 15)
	private java.lang.Integer vendorContactsId;
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
}
