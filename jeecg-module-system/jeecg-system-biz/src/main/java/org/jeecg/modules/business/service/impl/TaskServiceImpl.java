package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.entity.Task;
import org.jeecg.modules.business.mapper.TaskMapper;
import org.jeecg.modules.business.service.ITaskService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: tasks
 * @Author: jeecg-boot
 * @Date:   2023-08-22
 * @Version: V1.0
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements ITaskService {

}
