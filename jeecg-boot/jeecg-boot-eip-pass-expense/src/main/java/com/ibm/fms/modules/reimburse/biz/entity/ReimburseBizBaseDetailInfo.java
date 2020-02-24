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
 * @Description: 报销单基本明细
 * @Author: jeecg-boot
 * @Date:   2020-01-08
 * @Version: V1.0
 */
@Data
@TableName("reimburse_biz_base_detail_info")
public class ReimburseBizBaseDetailInfo implements Serializable {
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
	/**明细序号*/
	@Excel(name = "明细序号", width = 15)
	private java.lang.Integer seq;
	/**退单原因代码*/
	@Excel(name = "退单原因代码", width = 15)
	private java.lang.Integer withdrawCause;
	/**费用项（业务大类）编码*/
	@Excel(name = "费用项（业务大类）编码", width = 15)
	private java.lang.String biztypeCode;
	/**费用项（业务大类）名称*/
	@Excel(name = "费用项（业务大类）名称", width = 15)
	private java.lang.String biztypeName;
	/**费用项代码*/
	@Excel(name = "费用项代码", width = 15)
	private java.lang.String feeItemCode;
	/**费用项名称*/
	@Excel(name = "费用项名称", width = 15)
	private java.lang.String feeItemName;
	/**发票金额（价税总额）*/
	@Excel(name = "发票金额（价税总额）", width = 15)
	private java.math.BigDecimal invoiceAmt;
	/**发票价额*/
	@Excel(name = "发票价额", width = 15)
	private java.math.BigDecimal invoicePriceAmt;
	/**发票税额*/
	@Excel(name = "发票税额", width = 15)
	private java.math.BigDecimal invoiceTaxAmt;
	/**付款（支付）金额*/
	@Excel(name = "付款（支付）金额", width = 15)
	private java.math.BigDecimal paymentAmt;
	/**付款（支付）价额*/
	@Excel(name = "付款（支付）价额", width = 15)
	private java.math.BigDecimal paymentPriceAmt;
	/**付款（支付）税额*/
	@Excel(name = "付款（支付）税额", width = 15)
	private java.math.BigDecimal paymentTaxAmt;
	/**税码*/
	@Excel(name = "税码", width = 15)
	private java.lang.String taxCode;
	/**税率*/
	@Excel(name = "税率", width = 15)
	private java.lang.String taxRate;
	/**报账单明细说明*/
	@Excel(name = "报账单明细说明", width = 15)
	private java.lang.String dtlDesc;
	/**会计科目代码*/
	@Excel(name = "会计科目代码", width = 15)
	private java.lang.String erpAccountNo;
	/**会计科目名称*/
	@Excel(name = "会计科目名称", width = 15)
	private java.lang.String erpAccountName;
	/**成本中心代码*/
	@Excel(name = "成本中心代码", width = 15)
	private java.lang.String costcenterCode;
	/**成本中心名称*/
	@Excel(name = "成本中心名称", width = 15)
	private java.lang.String costcenterName;
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
	/**高级明细ID*/
	@Excel(name = "高级明细ID", width = 15)
	private java.lang.String advanceDetailId;
	/**预算信息ID*/
	@Excel(name = "预算信息ID", width = 15)
	private java.lang.String budgetItemId;
	/**付款清单ID*/
	@Excel(name = "付款清单ID", width = 15)
	private java.lang.String paymentListId;
}
