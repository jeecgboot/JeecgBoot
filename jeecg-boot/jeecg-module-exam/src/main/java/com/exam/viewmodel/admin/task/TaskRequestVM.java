package com.exam.viewmodel.admin.task;

import com.exam.viewmodel.admin.exam.ExamResponseVM;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


public class TaskRequestVM {

    private Integer id;

    @NotNull
    private Integer gradeLevel;

    @NotNull
    private String title;

    @Size(min = 1, message = "请添加试卷")
    @Valid
    private List<ExamResponseVM> paperItems;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(Integer gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ExamResponseVM> getPaperItems() {
        return paperItems;
    }

    public void setPaperItems(List<ExamResponseVM> paperItems) {
        this.paperItems = paperItems;
    }
}
