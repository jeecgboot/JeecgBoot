package com.exam.event;

import com.exam.domain.ExamPaperAnswerInfo;
import org.springframework.context.ApplicationEvent;

/**
 * @version 3.3.0
 * @description: The type Calculate exam paper answer complete event.
 * Copyright (C), 2020-2024
 * @date 2021/5/25 10:45
 */
public class CalculateExamPaperAnswerCompleteEvent extends ApplicationEvent {


    private final ExamPaperAnswerInfo examPaperAnswerInfo;


    /**
     * Instantiates a new Calculate exam paper answer complete event.
     *
     * @param examPaperAnswerInfo the exam paper answer info
     */
    public CalculateExamPaperAnswerCompleteEvent(final ExamPaperAnswerInfo examPaperAnswerInfo) {
        super(examPaperAnswerInfo);
        this.examPaperAnswerInfo = examPaperAnswerInfo;
    }

    /**
     * Gets exam paper answer info.
     *
     * @return the exam paper answer info
     */
    public ExamPaperAnswerInfo getExamPaperAnswerInfo() {
        return examPaperAnswerInfo;
    }

}
