package com.exam.viewmodel.student.dashboard;


import java.util.List;

public class IndexVM {
    private List<PaperInfo> fixedPaper;
    private List<PaperInfoVM> timeLimitPaper;
    private List<PaperInfo> pushPaper;

    public List<PaperInfo> getFixedPaper() {
        return fixedPaper;
    }

    public void setFixedPaper(List<PaperInfo> fixedPaper) {
        this.fixedPaper = fixedPaper;
    }

    public List<PaperInfoVM> getTimeLimitPaper() {
        return timeLimitPaper;
    }

    public void setTimeLimitPaper(List<PaperInfoVM> timeLimitPaper) {
        this.timeLimitPaper = timeLimitPaper;
    }

    public List<PaperInfo> getPushPaper() {
        return pushPaper;
    }

    public void setPushPaper(List<PaperInfo> pushPaper) {
        this.pushPaper = pushPaper;
    }
}
