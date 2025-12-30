package com.liupengzeng.jeecg.teaching.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liupengzeng.jeecg.teaching.entity.TeachingProject;

public interface TeachingProjectService extends IService<TeachingProject> {
    void submitForApproval(Long projectId, Long userId);
}
