package org.jeecg.modules.business.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.TaskHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @Description: task history
 * @Author: jeecg-boot
 * @Date:   2023-08-22
 * @Version: V1.0
 */

@Repository
public interface TaskHistoryMapper extends BaseMapper<TaskHistory> {

    TaskHistory getLatestRunningTask(@Param("code") String taskCode);

    List<TaskHistory> getAllRunningTasks(@Param("code") String taskCode);
}
