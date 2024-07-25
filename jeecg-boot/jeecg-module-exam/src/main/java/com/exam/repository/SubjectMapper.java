package com.exam.repository;

import com.exam.domain.Subject;
import com.exam.viewmodel.admin.education.SubjectPageRequestVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SubjectMapper  extends BaseMapper<Subject> {

    List<Subject> getSubjectByLevel(Integer level);

    List<Subject> allSubject();

    List<Subject> page(SubjectPageRequestVM requestVM);
}
