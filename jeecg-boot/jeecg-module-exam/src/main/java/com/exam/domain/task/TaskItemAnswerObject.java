package com.exam.domain.task;


import lombok.Data;

@Data
public class TaskItemAnswerObject {
    private Integer examPaperId;
    private Integer examPaperAnswerId;
    private Integer status;

    public TaskItemAnswerObject(Integer examPaperId, Integer examPaperAnswerId, Integer status) {
        this.examPaperId = examPaperId;
        this.examPaperAnswerId = examPaperAnswerId;
        this.status = status;
    }
}
