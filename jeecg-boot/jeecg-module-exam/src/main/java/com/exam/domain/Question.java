package com.exam.domain;

import com.exam.domain.enums.QuestionTypeEnum;
import com.exam.utility.ExamUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class Question implements Serializable {

    private static final long serialVersionUID = 8826266720383164363L;

    private Integer id;

    /**
     * 	1.单选题 2.多选题 3.判断题 4.填空题 5.简答题
     */
    private Integer questionType;

    /**
     * 学科
     */
    private Integer subjectId;

    /**
     * 题目总分(千分制)
     */
    private Integer score;

    /**
     * 级别
     */
    private Integer gradeLevel;

    /**
     * 题目难度
     */
    private Integer difficult;

    /**
     * 正确答案
     */
    private String correct;

    /**
     * 题目 填空、 题干、解析、答案等信息
     */
    private Integer infoTextContentId;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 1.正常
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    private Boolean deleted;

    public void setCorrectFromVM(String correct, List<String> correctArray) {
        int qType = this.getQuestionType();
        if (qType == QuestionTypeEnum.MultipleChoice.getCode()) {
            String correctJoin = ExamUtil.contentToString(correctArray);
            this.setCorrect(correctJoin);
        } else {
            this.setCorrect(correct);
        }
    }
}
