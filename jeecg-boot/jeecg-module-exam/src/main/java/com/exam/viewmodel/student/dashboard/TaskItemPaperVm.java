package com.exam.viewmodel.student.dashboard;


public class TaskItemPaperVm {
    private Integer examPaperId;
    private String examPaperName;
    private Integer examPaperAnswerId;
    private Integer status;

    public Integer getExamPaperId() {
        return examPaperId;
    }

    public void setExamPaperId(Integer examPaperId) {
        this.examPaperId = examPaperId;
    }

    public String getExamPaperName() {
        return examPaperName;
    }

    public void setExamPaperName(String examPaperName) {
        this.examPaperName = examPaperName;
    }

    public Integer getExamPaperAnswerId() {
        return examPaperAnswerId;
    }

    public void setExamPaperAnswerId(Integer examPaperAnswerId) {
        this.examPaperAnswerId = examPaperAnswerId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
