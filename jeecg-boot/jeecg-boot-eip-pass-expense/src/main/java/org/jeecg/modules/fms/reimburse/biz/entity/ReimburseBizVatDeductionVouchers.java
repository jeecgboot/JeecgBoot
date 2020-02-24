package org.jeecg.modules.fms.reimburse.biz.entity;

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
 * @Description: 报销单抵扣凭证
 * @Author: jeecg-boot
 * @Date:   2020-01-08
 * @Version: V1.0
 */
@Data
@TableName("reimburse_biz_vat_deduction_vouchers")
public class ReimburseBizVatDeductionVouchers implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	//@TableId(type = IdType.ID_WORKER_STR)
    @TableId(type = IdType.AUTO)
	private java.lang.Integer id;
	/**报销单编号*/
	private java.lang.String applyNo;
	/**序号*/
	@Excel(name = "序号", width = 15)
	private java.lang.Integer seq;
	/**发票号码*/
	@Excel(name = "发票号码", width = 15)
	private java.lang.String docNum;
	/**发票代码*/
	@Excel(name = "发票代码", width = 15)
	private java.lang.String invoiceCode;
	/**开票日期*/
	@Excel(name = "开票日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date docDate;
	/**是否需抵扣：Y-是/N-否，指发票本身是否需要进行抵扣*/
	@Excel(name = "是否需抵扣：Y-是/N-否，指发票本身是否需要进行抵扣", width = 15)
	@Dict(dicCode = "fms_comm_yn")
	private java.lang.String offsetFlag;
	/**原发票号码*/
	@Excel(name = "原发票号码", width = 15)
	private java.lang.String docNumOriginal;
	/**原发票代码*/
	@Excel(name = "原发票代码", width = 15)
	private java.lang.String invoiceCodeOriginal;
	/**增值税扣税凭证类型*/
	@Excel(name = "增值税扣税凭证类型", width = 15)
	@Dict(dicCode = "fms_vat_inv_type")
	private java.lang.String voucherType;
	/**增值税扣税凭证类型名称*/
	@Excel(name = "增值税扣税凭证类型名称", width = 15)
	private java.lang.String voucherTypeName;
	/**金额*/
	@Excel(name = "金额", width = 15)
	private java.math.BigDecimal invoiceAmount;
	/**税额*/
	@Excel(name = "税额", width = 15)
	private java.math.BigDecimal invoiceTaxAmount;
	/**税率*/
	@Excel(name = "税率", width = 15)
	private java.lang.String invoiceTaxRate;
	/**价税合计*/
	@Excel(name = "价税合计", width = 15)
	private java.math.BigDecimal invoiceTotalAmount;
	/**购方纳税人识别号*/
	@Excel(name = "购方纳税人识别号", width = 15)
	private java.lang.String buyTaxIdentiNum;
	/**购方纳税人名称*/
	@Excel(name = "购方纳税人名称", width = 15)
	private java.lang.String buyTaxIdentiName;
	/**销方纳税人识别号*/
	@Excel(name = "销方纳税人识别号", width = 15)
	private java.lang.String sellerTaxIdentiNum;
	/**销方纳税人名称*/
	@Excel(name = "销方纳税人名称", width = 15)
	private java.lang.String sellerTaxIdentiName;
	/**发票认证结果:1：认证通过；2：认证不通过；3：未认证; 4：无需认证*/
	@Excel(name = "发票认证结果:1：认证通过；2：认证不通过；3：未认证; 4：无需认证", width = 15)
	private java.lang.String invoiceResult;
	/**发票认证结果名称：1：认证通过；2：认证不通过；3：未认证; 4：无需认证*/
	@Excel(name = "发票认证结果名称：1：认证通过；2：认证不通过；3：未认证; 4：无需认证", width = 15)
	private java.lang.String invoiceResultName;
	/**报账单认证结果:1.都通过；2：部分通过；3：都未通过；4：认证异常*/
	@Excel(name = "报账单认证结果:1.都通过；2：部分通过；3：都未通过；4：认证异常", width = 15)
	private java.lang.String claimResult;
	/**认证日期*/
	@Excel(name = "认证日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date authenticationDate;
	/**发票签收时间*/
	@Excel(name = "发票签收时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date signDate;
	/**凭证是否退回*/
	@Excel(name = "凭证是否退回", width = 15)
	private java.lang.String voucherReturnFlag;
	/**报账单处理时间*/
	@Excel(name = "报账单处理时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date processDate;
	/**是否红字发票：N- 否，Y- 是*/
	@Excel(name = "是否红字发票：N- 否，Y- 是", width = 15)
	private java.lang.String redInvoiceFlag;
	/**红字发票通知单编号*/
	@Excel(name = "红字发票通知单编号", width = 15)
	private java.lang.String redInvoiceNum;
	/**红字发票通知单开具日期*/
	@Excel(name = "红字发票通知单开具日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date redInvoiceDate;
	/**是否汇总*/
	@Excel(name = "是否汇总", width = 15)
	private java.lang.String isSum;
	/**备注*/
	@Excel(name = "备注", width = 15)
	private java.lang.String remarks;
	/**红字发票描述*/
	@Excel(name = "红字发票描述", width = 15)
	private java.lang.String redInvoiceDescription;
	/**供应商编号*/
	@Excel(name = "供应商编号", width = 15)
	private java.lang.String vendorNum;
	/**供应商名称*/
	@Excel(name = "供应商名称", width = 15)
	private java.lang.String vendorName;
	/**项目属性：Y-动产/N-不动产*/
	@Excel(name = "项目属性：Y-动产/N-不动产", width = 15)
	private java.lang.String projectAttr;
	/**结转税额标识（0、动产项目或成本费用；1、不动产项目并且第一次结转；2、不动产项目并且第二次结转；3、动产转为不动产项目；4、不动产转为动产项目）*/
	@Excel(name = "结转税额标识（0、动产项目或成本费用；1、不动产项目并且第一次结转；2、不动产项目并且第二次结转；3、动产转为不动产项目；4、不动产转为动产项目）", width = 15)
	private java.lang.Integer transferTaxFlag;
	/**进项税额转出会计期间*/
	@Excel(name = "进项税额转出会计期间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date inputTaxTurnDate;
	/**不抵扣进项税凭证是否汇总：Y-是，N-否*/
	@Excel(name = "不抵扣进项税凭证是否汇总：Y-是，N-否", width = 15)
	private java.lang.String isSumBdk;
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
