package com.liupengzeng.jeecg.teaching.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("teaching_project")
public class TeachingProject {
    @TableId
    private Long id;
    private String projectCode;
    private String title;
    private String projectType;
    private Long applicantId;
    private Long deptId;
    private Date applyTime;
    private Date startDate;
    private Date endDate;
    private String status;
    private BigDecimal budget;
    private String description;
    private String attachments; // json array
    private Long createdBy;
    private Date createdTime;
    private Long updatedBy;
    private Date updatedTime;
}
