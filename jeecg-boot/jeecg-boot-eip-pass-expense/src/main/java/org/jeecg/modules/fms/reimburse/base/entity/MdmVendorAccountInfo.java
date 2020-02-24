package org.jeecg.modules.fms.reimburse.base.entity;

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
 * @Description: 主数据供应商银行账户
 * @Author: jeecg-boot
 * @Date:   2020-01-17
 * @Version: V1.0
 */
@Data
@TableName("mdm_vendor_account_info")
public class MdmVendorAccountInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**编号*/
	@TableId(type = IdType.ID_WORKER_STR)
	private java.lang.String id;
	/**供应商公司 ID（地市公司组织 ID）*/
	@Excel(name = "供应商公司 ID（地市公司组织 ID）", width = 15)
	private java.lang.Integer vendorCompanyId;
	/**供应商银行账户 ID*/
	@Excel(name = "供应商银行账户 ID", width = 15)
	private java.lang.Integer vendorAccountId;
	/**供应商 ID*/
	private java.lang.Integer vendorId;
	/**供应商编号（主数据编号）*/
	@Excel(name = "供应商编号（主数据编号）", width = 15)
	private java.lang.String mdCode;
	/**是否主账号*/
	@Excel(name = "是否主账号", width = 15)
	private java.lang.String mainAccountFlag;
	/**银行名称*/
	@Excel(name = "银行名称", width = 15)
	private java.lang.String bankName;
	/**银行编码*/
	@Excel(name = "银行编码", width = 15)
	private java.lang.String bankCode;
	/**支行名称*/
	@Excel(name = "支行名称", width = 15)
	private java.lang.String branchBank;
	/**支行编码*/
	@Excel(name = "支行编码", width = 15)
	private java.lang.String branchCode;
	/**联行号*/
	@Excel(name = "联行号", width = 15)
	private java.lang.String cnapNumber;
	/**银行户名*/
	@Excel(name = "银行户名", width = 15)
	private java.lang.String accountName;
	/**银行账号*/
	@Excel(name = "银行账号", width = 15)
	private java.lang.String accountNumber;
	/**账户币种*/
	@Excel(name = "账户币种", width = 15)
	private java.lang.String accountCurrency;
	/**银行账户是否有效*/
	@Excel(name = "银行账户是否有效", width = 15)
	private java.lang.String validFlag;
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
