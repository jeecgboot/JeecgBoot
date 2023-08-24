package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.TaskHistory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: task history
 * @Author: jeecg-boot
 * @Date:   2023-08-22
 * @Version: V1.0
 */
public interface ITaskHistoryService extends IService<TaskHistory> {

    TaskHistory getLatestRunningTask(String taskCode);

    List<TaskHistory> getAllRunningTasksByCode(String code);

    void insert(TaskHistory taskHistory);
}
