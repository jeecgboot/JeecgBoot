package org.jeecg.common.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 流程审批意见DTO
 * @author scott
 * @date 2025-01-29
 */
@Data
public class ApprovalCommentDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 任务ID
     */
    private String taskId;
    
    /**
     * 任务名称
     */
    private String taskName;
    
    /**
     * 审批人ID
     */
    private String approverId;
    
    /**
     * 审批人姓名
     */
    private String approverName;
    
    /**
     * 审批意见
     */
    private String approvalComment;
    
    /**
     * 审批时间
     */
    private Date approvalTime;
}

