package com.exam.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ExamPaperQuestionCustomerAnswer implements Serializable {

    private static final long serialVersionUID = 3389482731220342366L;

    private Integer id;

    /**
     * 题目Id
     */
    private Integer questionId;

    /**
     * 试卷Id
     */
    private Integer examPaperId;

    /**
     * 答案Id
     */
    private Integer examPaperAnswerId;

    /**
     * 题型
     */
    private Integer questionType;

    /**
     * 学科
     */
    private Integer subjectId;

    /**
     * 得分
     */
    private Integer customerScore;

    /**
     * 题目原始分数
     */
    private Integer questionScore;

    /**
     * 问题内容
     */
    private Integer questionTextContentId;

    /**
     * 做题答案
     */
    private String answer;

    /**
     * 做题内容
     */
    private Integer textContentId;

    /**
     * 是否正确
     */
    private Boolean doRight;

    /**
     * 做题人
     */
    private Integer createUser;

    private Date createTime;

    private Integer itemOrder;

}
