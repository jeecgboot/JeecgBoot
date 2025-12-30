package com.liupengzeng.jeecg.teaching.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liupengzeng.jeecg.teaching.entity.TeachingProject;
import com.liupengzeng.jeecg.teaching.service.TeachingProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/teaching/project")
public class TeachingProjectController {

    @Autowired
    private TeachingProjectService teachingProjectService;

    @PostMapping("/saveDraft")
    public TeachingProject saveDraft(@RequestBody TeachingProject project) {
        if (project.getId() == null) {
            teachingProjectService.save(project);
        } else {
            teachingProjectService.updateById(project);
        }
        return project;
    }

    @PostMapping("/submit/{id}")
    public String submit(@PathVariable Long id, @RequestParam Long userId) {
        teachingProjectService.submitForApproval(id, userId);
        return "submitted";
    }

    @GetMapping("/page")
    public Page<TeachingProject> page(@RequestParam Map<String, Object> params) {
        int pageNum = Integer.parseInt((String)params.getOrDefault("pageNum", "1"));
        int pageSize = Integer.parseInt((String)params.getOrDefault("pageSize", "10"));
        Page<TeachingProject> page = new Page<>(pageNum, pageSize);
        QueryWrapper<TeachingProject> qw = new QueryWrapper<>();
        if (params.containsKey("status")) {
            qw.eq("status", params.get("status"));
        }
        return teachingProjectService.page(page, qw);
    }

    @GetMapping("/{id}")
    public TeachingProject getById(@PathVariable Long id) {
        return teachingProjectService.getById(id);
    }
}
