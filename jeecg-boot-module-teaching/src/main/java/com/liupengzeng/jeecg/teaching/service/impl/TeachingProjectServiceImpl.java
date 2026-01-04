package com.liupengzeng.jeecg.teaching.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liupengzeng.jeecg.teaching.entity.TeachingProject;
import com.liupengzeng.jeecg.teaching.mapper.TeachingProjectMapper;
import com.liupengzeng.jeecg.teaching.service.TeachingProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeachingProjectServiceImpl extends ServiceImpl<TeachingProjectMapper, TeachingProject> implements TeachingProjectService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitForApproval(Long projectId, Long userId) {
        TeachingProject project = this.getById(projectId);
        if (project == null) {
            throw new IllegalArgumentException("project not found: " + projectId);
        }
        project.setStatus("申报中");
        this.updateById(project);
        // TODO: trigger workflow engine and create process instance with businessKey=projectId
    }
}
