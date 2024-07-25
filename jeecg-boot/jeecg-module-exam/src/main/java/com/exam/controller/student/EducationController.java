package com.exam.controller.student;


import com.exam.base.BaseApiController;
import com.exam.base.RestResponse;
import com.exam.domain.Subject;
import com.exam.domain.User;
import com.exam.service.SubjectService;
import com.exam.viewmodel.student.education.SubjectEditRequestVM;
import com.exam.viewmodel.student.education.SubjectVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController("StudentEducationController")
@RequestMapping(value = "/api/student/education")
public class EducationController extends BaseApiController {

    private final SubjectService subjectService;

    @Autowired
    public EducationController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @RequestMapping(value = "/subject/list", method = RequestMethod.POST)
    public RestResponse<List<SubjectVM>> list() {
        User user = getCurrentUser();
        List<Subject> subjects = subjectService.getSubjectByLevel(user.getUserLevel());
        List<SubjectVM> subjectVMS = subjects.stream().map(d -> {
            SubjectVM subjectVM = modelMapper.map(d, SubjectVM.class);
            subjectVM.setId(String.valueOf(d.getId()));
            return subjectVM;
        }).collect(Collectors.toList());
        return RestResponse.ok(subjectVMS);
    }

    @RequestMapping(value = "/subject/select/{id}", method = RequestMethod.POST)
    public RestResponse<SubjectEditRequestVM> select(@PathVariable Integer id) {
        Subject subject = subjectService.selectById(id);
        SubjectEditRequestVM vm = modelMapper.map(subject, SubjectEditRequestVM.class);
        return RestResponse.ok(vm);
    }

}
