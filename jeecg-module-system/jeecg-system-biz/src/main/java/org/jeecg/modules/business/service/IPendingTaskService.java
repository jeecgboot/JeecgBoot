package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.PendingTask;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: to know if a task in launched
 * @Author: jeecg-boot
 * @Date:   2023-08-17
 * @Version: V1.0
 */
public interface IPendingTaskService extends IService<PendingTask> {
    void setStatus(int status, String taskCode);
    String getStatus(String taskCode);
}
