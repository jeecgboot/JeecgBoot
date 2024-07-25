package com.exam.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ExamPaper implements Serializable {

    private static final long serialVersionUID = 8509645224550501395L;

    private Integer id;

    /**
     * 试卷名称
     */
    private String name;

    /**
     * 学科
     */
    private Integer subjectId;

    /**
     * 试卷类型( 1固定试卷 4.时段试卷 6.任务试卷)
     */
    private Integer paperType;

    /**
     * 年级
     */
    private Integer gradeLevel;

    /**
     * 试卷总分(千分制)
     */
    private Integer score;

    /**
     * 题目数量
     */
    private Integer questionCount;

    /**
     * 建议时长(分钟)
     */
    private Integer suggestTime;

    /**
     * 时段试卷 开始时间
     */
    private Date limitStartTime;

    /**
     * 时段试卷 结束时间
     */
    private Date limitEndTime;

    /**
     * 试卷框架 内容为JSON
     */
    private Integer frameTextContentId;

    private Integer createUser;

    private Date createTime;

    private Boolean deleted;

    private Integer taskExamId;
}
