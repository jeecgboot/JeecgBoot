package org.jeecg.modules.fms.reimburse.biz.utils.VO;

import java.io.Serializable;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
@Data
public class UnifyWorkItemVO  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 报销单编号
	 */
	private String applyNo;
	
	/**
	 * 报销单类型代码
	 */
	private String rmbsTemplateCode;
	
	/**
	 * 报销单类型名称
	 */
	private String rmbsTemplateName;
	
	
	/**
	 * 报销事由
	 */
	private String reimbursementItem;
	
	
	/**
	 * 单据创建时间
	 */
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	
	/**
	 * 报销人
	 */
	private String userName;
	
	/**
	 * 报销人所在部门
	 */
	private String userDepartName;
	
	/**
	 * 报销单审批时间
	 */
	private String procState;
	
		
	/**
	 * 导入ERP状态
	 */
	private String importERPStatus;
}
