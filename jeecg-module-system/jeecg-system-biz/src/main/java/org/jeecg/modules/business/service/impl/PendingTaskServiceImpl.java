package org.jeecg.modules.business.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.entity.PendingTask;
import org.jeecg.modules.business.mapper.PendingTaskMapper;
import org.jeecg.modules.business.service.IPendingTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: to know if a task in launched
 * @Author: jeecg-boot
 * @Date:   2023-08-17
 * @Version: V1.0
 */
@Service
@Slf4j
public class PendingTaskServiceImpl extends ServiceImpl<PendingTaskMapper, PendingTask> implements IPendingTaskService {
    @Autowired
    private PendingTaskMapper pendingTaskMapper;

    @Override
    public void setStatus(int status, String taskCode) {
        pendingTaskMapper.setStatus(status, taskCode);
    }
    @Override
    public String getStatus(String taskCode) {
        return pendingTaskMapper.getStatus(taskCode);
    }
}
