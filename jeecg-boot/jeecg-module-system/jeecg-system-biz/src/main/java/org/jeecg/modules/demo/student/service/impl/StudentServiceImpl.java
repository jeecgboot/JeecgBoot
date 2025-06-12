package org.jeecg.modules.demo.student.service.impl;

import org.jeecg.modules.demo.student.entity.Student;
import org.jeecg.modules.demo.student.mapper.StudentMapper;
import org.jeecg.modules.demo.student.service.IStudentService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 学生表
 * @Author: jeecg-boot
 * @Date:   2025-06-10
 * @Version: V1.0
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

}
