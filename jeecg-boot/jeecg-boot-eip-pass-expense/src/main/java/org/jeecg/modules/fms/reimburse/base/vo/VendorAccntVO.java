package org.jeecg.modules.fms.reimburse.base.vo;

import java.io.Serializable;

import org.jeecg.common.aspect.annotation.Dict;

import lombok.Data;

@Data
public class VendorAccntVO implements Serializable{
	private static final long serialVersionUID = 1L;
	/**供应商编号（主数据编号）*/
	private java.lang.String id;
	
	/**供应商编号（主数据编号）*/
	private java.lang.String mdCode;
	/**供应商名称*/
	private java.lang.String vendorName;
	/**是否主账号*/
	@Dict(dicCode = "fms_comm_yn")
	private java.lang.String mainAccountFlag;
	/**银行名称*/
	private java.lang.String bankName;
	/**银行编码*/
	private java.lang.String bankCode;
	/**支行名称*/
	private java.lang.String branchBank;
	/**联行号*/
	private java.lang.String cnapNumber;
	/**银行户名*/
	private java.lang.String accountName;
	/**银行账号*/
	private java.lang.String accountNumber;

}
