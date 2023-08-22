package org.jeecg.modules.business.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.PendingTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @Description: to know if a task in launched
 * @Author: jeecg-boot
 * @Date:   2023-08-17
 * @Version: V1.0
 */

@Repository
public interface PendingTaskMapper extends BaseMapper<PendingTask> {
    void setStatus(@Param("status") int status, @Param("code") String taskCode);
    String getStatus(@Param("code") String taskCode);
}
