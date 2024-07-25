package com.exam.viewmodel.admin.dashboard;



import java.util.List;


public class IndexVM {
    private Integer examPaperCount;
    private Integer questionCount;
    private Integer doExamPaperCount;
    private Integer doQuestionCount;
    private List<Integer> mothDayUserActionValue;
    private List<Integer> mothDayDoExamQuestionValue;
    private List<String> mothDayText;

    public Integer getExamPaperCount() {
        return examPaperCount;
    }

    public void setExamPaperCount(Integer examPaperCount) {
        this.examPaperCount = examPaperCount;
    }

    public Integer getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }

    public Integer getDoExamPaperCount() {
        return doExamPaperCount;
    }

    public void setDoExamPaperCount(Integer doExamPaperCount) {
        this.doExamPaperCount = doExamPaperCount;
    }

    public Integer getDoQuestionCount() {
        return doQuestionCount;
    }

    public void setDoQuestionCount(Integer doQuestionCount) {
        this.doQuestionCount = doQuestionCount;
    }

    public List<Integer> getMothDayUserActionValue() {
        return mothDayUserActionValue;
    }

    public void setMothDayUserActionValue(List<Integer> mothDayUserActionValue) {
        this.mothDayUserActionValue = mothDayUserActionValue;
    }

    public List<Integer> getMothDayDoExamQuestionValue() {
        return mothDayDoExamQuestionValue;
    }

    public void setMothDayDoExamQuestionValue(List<Integer> mothDayDoExamQuestionValue) {
        this.mothDayDoExamQuestionValue = mothDayDoExamQuestionValue;
    }

    public List<String> getMothDayText() {
        return mothDayText;
    }

    public void setMothDayText(List<String> mothDayText) {
        this.mothDayText = mothDayText;
    }
}
