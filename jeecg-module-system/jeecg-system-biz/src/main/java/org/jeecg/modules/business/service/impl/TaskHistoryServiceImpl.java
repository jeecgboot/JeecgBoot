package org.jeecg.modules.business.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.entity.TaskHistory;
import org.jeecg.modules.business.mapper.TaskHistoryMapper;
import org.jeecg.modules.business.service.ITaskHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: task history
 * @Author: jeecg-boot
 * @Date:   2023-08-22
 * @Version: V1.0
 */

@Slf4j
@Service
public class TaskHistoryServiceImpl extends ServiceImpl<TaskHistoryMapper, TaskHistory> implements ITaskHistoryService {
    @Autowired
    private TaskHistoryMapper taskHistoryMapper;
    @Override
    public TaskHistory getLatestRunningTask(String taskCode) {
        return taskHistoryMapper.getLatestRunningTask(taskCode);
    }

    @Override
    public List<TaskHistory> getAllRunningTasksByCode(String taskCode) {
        return taskHistoryMapper.getAllRunningTasks(taskCode);
    }

    @Override
    public void insert(TaskHistory taskHistory) {
        taskHistoryMapper.insert(taskHistory);
    }
}
