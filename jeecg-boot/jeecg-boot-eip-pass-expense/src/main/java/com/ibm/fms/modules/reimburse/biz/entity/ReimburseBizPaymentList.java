package com.ibm.fms.modules.reimburse.biz.entity;

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
 * @Description: 报销单付款清单
 * @Author: jeecg-boot
 * @Date:   2020-01-08
 * @Version: V1.0
 */
@Data
@TableName("reimburse_biz_payment_list")
public class ReimburseBizPaymentList implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**流水号*/
	@TableId(type = IdType.ID_WORKER_STR)
	private java.lang.String id;
	/**报账单编号*/
	private java.lang.String applyNo;
	/**报账单模板编号*/
	@Excel(name = "报账单模板编号", width = 15)
	private java.lang.String reimbursementTemplateCode;
	/**报账单模板名称*/
	@Excel(name = "报账单模板名称", width = 15)
	private java.lang.String reimbursementTemplateName;
	/**付款明细行序号*/
	@Excel(name = "付款明细行序号", width = 15)
	private java.lang.Integer seq;
	/**费用明细行序号*/
	@Excel(name = "费用明细行序号", width = 15)
	private java.lang.Integer detailSeq;
	/**付款清单说明*/
	@Excel(name = "付款清单说明", width = 15)
	private java.lang.String remark;
	/**收方行分支机构全称（行名）*/
	@Excel(name = "收方行分支机构全称（行名）", width = 15)
	private java.lang.String bankBranchName;
	/**收方行分支机构联行号*/
	@Excel(name = "收方行分支机构联行号", width = 15)
	private java.lang.String bankCnapsCode;
	/**收方银行账号*/
	@Excel(name = "收方银行账号", width = 15)
	private java.lang.String bankAccountNo;
	/**收方银行账号户名*/
	@Excel(name = "收方银行账号户名", width = 15)
	private java.lang.String bankAccountName;
	/**账户类别*/
	@Excel(name = "账户类别", width = 15)
	private java.lang.String bankAccountType;
	/**支付结果联系人电话*/
	@Excel(name = "支付结果联系人电话", width = 15)
	private java.lang.String paymentContactorPhone;
	/**供应商代码*/
	@Excel(name = "供应商代码", width = 15)
	private java.lang.String vendorCode;
	/**供应商名称*/
	@Excel(name = "供应商名称", width = 15)
	private java.lang.String vendorName;
	/**供应商地点*/
	@Excel(name = "供应商地点", width = 15)
	private java.lang.String vendorAddress;
	/**ERP付款账户代码*/
	@Excel(name = "ERP付款账户代码", width = 15)
	private java.lang.String erpAccountComNo;
	/**ERP付款账户名称*/
	@Excel(name = "ERP付款账户名称", width = 15)
	private java.lang.String erpAccountComRemark;
	/**付方银行账号*/
	@Excel(name = "付方银行账号", width = 15)
	private java.lang.String paymentAccountNo;
	/**付方银行账号户名*/
	@Excel(name = "付方银行账号户名", width = 15)
	private java.lang.String paymentAccountName;
	/**本次支付金额*/
	@Excel(name = "本次支付金额", width = 15)
	private java.math.BigDecimal paymentAmount;
	/**银企银行类别：可发往不同银行接口*/
	@Excel(name = "银企银行类别：可发往不同银行接口", width = 15)
	private java.lang.String bankType;
	/**收方行所在省*/
	@Excel(name = "收方行所在省", width = 15)
	private java.lang.String bankLocProvince;
	/**收方行所在城市*/
	@Excel(name = "收方行所在城市", width = 15)
	private java.lang.String bankLocCity;
	/**本次支付状态:0 未付款 1 付款成功  2付款失败 3 付款失败已重提交*/
	@Excel(name = "本次支付状态:0 未付款 1 付款成功  2付款失败 3 付款失败已重提交", width = 15)
	private java.lang.String paymentStatus;
	/**本次支付状态描述*/
	@Excel(name = "本次支付状态描述", width = 15)
	private java.lang.String paymentStatusDesc;
	/**审核会计ID*/
	@Excel(name = "审核会计ID", width = 15)
	private java.lang.String auditUserId;
	/**业务分类代码*/
	@Excel(name = "业务分类代码", width = 15)
	private java.lang.String biztypeCode;
	/**业务分类名称*/
	@Excel(name = "业务分类名称", width = 15)
	private java.lang.String biztypeName;
	/**费用项代码*/
	@Excel(name = "费用项代码", width = 15)
	private java.lang.String feeItemCode;
	/**费用项名称*/
	@Excel(name = "费用项名称", width = 15)
	private java.lang.String feeItemName;
	/**本次支付对应ERP付款批模板ID*/
	@Excel(name = "本次支付对应ERP付款批模板ID", width = 15)
	private java.lang.Integer batchPayTplId;
	/**银行回单ID*/
	@Excel(name = "银行回单ID", width = 15)
	private java.lang.Integer bankReceiptId;
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
