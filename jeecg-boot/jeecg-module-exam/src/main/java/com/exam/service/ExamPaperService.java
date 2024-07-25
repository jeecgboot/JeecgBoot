package com.exam.service;

import com.exam.domain.ExamPaper;
import com.exam.domain.User;
import com.exam.viewmodel.admin.exam.ExamPaperEditRequestVM;
import com.exam.viewmodel.admin.exam.ExamPaperPageRequestVM;
import com.exam.viewmodel.student.dashboard.PaperFilter;
import com.exam.viewmodel.student.dashboard.PaperInfo;
import com.exam.viewmodel.student.exam.ExamPaperPageVM;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ExamPaperService extends BaseService<ExamPaper> {

    PageInfo<ExamPaper> page(ExamPaperPageRequestVM requestVM);

    PageInfo<ExamPaper> taskExamPage(ExamPaperPageRequestVM requestVM);

    PageInfo<ExamPaper> studentPage(ExamPaperPageVM requestVM);

    ExamPaper savePaperFromVM(ExamPaperEditRequestVM examPaperEditRequestVM, User user);

    ExamPaperEditRequestVM examPaperToVM(Integer id);

    List<PaperInfo> indexPaper(PaperFilter paperFilter);

    Integer selectAllCount();

    List<Integer> selectMothCount();
}
