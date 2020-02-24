package org.jeecg.modules.fms.reimburse.biz.entity;

import java.io.Serializable;

import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * @Description: 报销单基本信息
 * @Author: jeecg-boot
 * @Date:   2020-01-08
 * @Version: V1.0
 */
@Data
@TableName("reimburse_biz_main_info")
public class ReimburseBizMainInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**流水号*/
	@TableId(type = IdType.ID_WORKER_STR)
    private java.lang.String id;
	/**报账单编号*/
    @Excel(name = "报账单编号", width = 15)
    private java.lang.String applyNo;
	/**报账单模板编号*/
    @Excel(name = "报账单模板编号", width = 15)
    private java.lang.String reimbursementTemplateCode;
	/**报账单模板名称*/
    @Excel(name = "报账单模板名称", width = 15)
    private java.lang.String reimbursementTemplateName;
	/**员工登录帐号*/
    @Excel(name = "员工登录帐号", width = 15)
    private java.lang.String userId;
	/**员工姓名*/
    @Excel(name = "员工姓名", width = 15)
    private java.lang.String userName;
	/**员工所属公司编码*/
    @Excel(name = "员工所属公司编码", width = 15)
    private java.lang.String userCompanyCode;
	/**员工所属公司名称*/
    @Excel(name = "员工所属公司名称", width = 15)
    private java.lang.String userCompanyName;
	/**员工所属部门编码*/
    @Excel(name = "员工所属部门编码", width = 15)
    private java.lang.String userDepartCode;
	/**员工所属部门名称*/
    @Excel(name = "员工所属部门名称", width = 15)
    private java.lang.String userDepartName;
	/**员工职务*/
    @Excel(name = "员工职务", width = 15)
    private java.lang.String userPosition;
	/**员工编号*/
    @Excel(name = "员工编号", width = 15)
    private java.lang.String userNo;
	/**员工邮箱*/
    @Excel(name = "员工邮箱", width = 15)
    private java.lang.String userEmail;
	/**员工手机号*/
    @Excel(name = "员工手机号", width = 15)
    private java.lang.String userMobile;
	/**报销事由*/
    @Excel(name = "报销事由", width = 15)
    private java.lang.String reimbursementItem;
	/**备注*/
    @Excel(name = "备注", width = 15)
    private java.lang.String remark;
	/**支出成本类型*/
    @Excel(name = "支出成本类型", width = 15)
    private java.lang.String costType;
	/**币种*/
    @Excel(name = "币种", width = 15)
    private java.lang.String currency;
	/**发票张数*/
    @Excel(name = "发票张数", width = 15)
    private java.lang.Integer attachmentNum;
	/**支付方式：转账（银企直联），结清（银行托收），网银支付*/
    @Excel(name = "支付方式：转账（银企直联），结清（银行托收），网银支付", width = 15)
    @Dict(dicCode = "fms_payment_type")
    private java.lang.String paymentType;
	/**发票金额(价税总额)*/
    @Excel(name = "发票金额(价税总额)", width = 15)
    private java.math.BigDecimal invoiceAmount;
	/**付款金额*/
    @Excel(name = "付款金额", width = 15)
    private java.math.BigDecimal paymentAmount;
	/**发票价款*/
    @Excel(name = "发票价款", width = 15)
    private java.math.BigDecimal invoicePriceAmount;
	/**发票税款*/
    @Excel(name = "发票税款", width = 15)
    private java.math.BigDecimal invoiceTaxAmount;
	/**付款金额-价款*/
    @Excel(name = "付款金额-价款", width = 15)
    private java.math.BigDecimal paymentPriceAmount;
	/**付款金额-税款*/
    @Excel(name = "付款金额-税款", width = 15)
    private java.math.BigDecimal paymentTaxAmount;
	/**本次冲减金额*/
    @Excel(name = "本次冲减金额", width = 15)
    private java.math.BigDecimal strikeAmount;
	/**审批确认金额*/
    @Excel(name = "审批确认金额", width = 15)
    private java.math.BigDecimal auditConfirmAmount;
	/**审批差异金额*/
    @Excel(name = "审批差异金额", width = 15)
    private java.math.BigDecimal auditDiscrepancyAmount;
	/**审批确认说明*/
    @Excel(name = "审批确认说明", width = 15)
    private java.lang.String auditConfirmRemark;
	/**供应商编号*/
    @Excel(name = "供应商编号", width = 15)
    private java.lang.String vendorCode;
	/**供应商名称*/
    @Excel(name = "供应商名称", width = 15)
    private java.lang.String vendorName;
	/**供应商地点*/
    @Excel(name = "供应商地点", width = 15)
    private java.lang.String vendorAddress;
	/**供应商纳税人类型: 一般纳税人/ 小规模纳税人/非增值税纳税人*/
    @Excel(name = "供应商纳税人类型: 一般纳税人/ 小规模纳税人/非增值税纳税人", width = 15)
    private java.lang.String vendorVatFlag;
	/**结算支付方式*/
    @Excel(name = "结算支付方式", width = 15)
    private java.lang.String balSeqPayType;
	/**来源系统代码*/
    @Excel(name = "来源系统代码", width = 15)
    private java.lang.String sourceSystemCode;
	/**来源系统名称*/
    @Excel(name = "来源系统名称", width = 15)
    private java.lang.String sourceSystemName;
	/**接收类型*/
    @Excel(name = "接收类型", width = 15)
    private java.lang.String receiveType;
	/**接收方式*/
    @Excel(name = "接收方式", width = 15)
    private java.lang.String receiveWay;
	/**月度预算期间*/
    @Excel(name = "月度预算期间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date monthBdgtPeriod;
	/**审核会计Id*/
    @Excel(name = "审核会计Id", width = 15)
    private java.lang.String auditUserId;
	/**审核会计姓名*/
    @Excel(name = "审核会计姓名", width = 15)
    private java.lang.String auditUserName;
	/**实物单流程审批状态*/
    @Excel(name = "实物单流程审批状态", width = 15)
    private java.lang.String docProcessState;
	/**税务审核会计ID*/
    @Excel(name = "税务审核会计ID", width = 15)
    private java.lang.String vatAuditUserId;
	/**税务审核会计姓名*/
    @Excel(name = "税务审核会计姓名", width = 15)
    private java.lang.String vatAuditUserName;
	/**抵扣联实物单流程状态*/
    @Excel(name = "抵扣联实物单流程状态", width = 15)
    private java.lang.String vatDocProcessState;
	/**流程模板*/
    @Excel(name = "流程模板", width = 15)
    private java.lang.String procTemplateName;
	/**流程提交时间*/
    @Excel(name = "流程提交时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date submitTime;
	/**审批完毕时间*/
    @Excel(name = "审批完毕时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date endTime;
	/**流程审批状态*/
    @Excel(name = "流程审批状态", width = 15)
    private java.lang.String procState;
	/**是否借款：N否，Y是*/
    @Excel(name = "是否借款：N否，Y是", width = 15)
    private java.lang.String isLoan;
	/**是否冲减：N否，Y是*/
    @Excel(name = "是否冲减：N否，Y是", width = 15)
    private java.lang.String isStrike;
	/**是否预付：N否，Y是*/
    @Excel(name = "是否预付：N否，Y是", width = 15)
    private java.lang.String isPrepay;
	/**是否有合同：N否，Y是*/
    @Excel(name = "是否有合同：N否，Y是", width = 15)
    private java.lang.String existContract;
	/**是否关联交易:N否，Y是*/
    @Excel(name = "是否关联交易:N否，Y是", width = 15)
    private java.lang.String isConntrans;
	/**关联交易类型*/
    @Excel(name = "关联交易类型", width = 15)
    private java.lang.String conntransType;
	/**是否包含订单：N-否/Y-是*/
    @Excel(name = "是否包含订单：N-否/Y-是", width = 15)
    private java.lang.String existOrder;
	/**是否不动产：Y-是/N-否*/
    @Excel(name = "是否不动产：Y-是/N-否", width = 15)
    private java.lang.String isImmovable;
	/**是否电子发票：Y-是/N-否*/
    @Excel(name = "是否电子发票：Y-是/N-否", width = 15)
    private java.lang.String isEleInvoice;
	/**是否包含进项抵扣凭证：Y-是/N-否*/
    @Excel(name = "是否包含进项抵扣凭证：Y-是/N-否", width = 15)
    private java.lang.String existOffsetFlag;
	/**抵扣联是否认证通过 Y-认证通过、N-未认证或认证失败*/
    @Excel(name = "抵扣联是否认证通过 Y-认证通过、N-未认证或认证失败", width = 15)
    private java.lang.String certified;
	/**是否成本下分：Y-是/N-否*/
    @Excel(name = "是否成本下分：Y-是/N-否", width = 15)
    private java.lang.String isPayDivide;
	/**是否包含视同销售：N-否/Y-是*/
    @Excel(name = "是否包含视同销售：N-否/Y-是", width = 15)
    private java.lang.String isSales;
	/**是否需要起草进项发票报账单：N-否，Y-是*/
    @Excel(name = "是否需要起草进项发票报账单：N-否，Y-是", width = 15)
    private java.lang.String isInputVatInvoice;
	/**是否已经预匹配订单：Y-是，N-否*/
    @Excel(name = "是否已经预匹配订单：Y-是，N-否", width = 15)
    private java.lang.String isPreMatchPo;
	/**归档时间*/
    @Excel(name = "归档时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date archivingTime;
	/**归档人*/
    @Excel(name = "归档人", width = 15)
    private java.lang.String archivingMan;
	/**创建时间*/
    @Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date createTime;
	/**创建人*/
    @Excel(name = "创建人", width = 15)
    private java.lang.String createBy;
	/**更新时间*/
    @Excel(name = "更新时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date updateTime;
	/**更新人*/
    @Excel(name = "更新人", width = 15)
    private java.lang.String updateBy;
	/**报账流程ERP统计信息扩展表ID*/
    @Excel(name = "报账流程ERP统计信息扩展表ID", width = 15)
    private java.lang.String procerpStatExtendId;
}
