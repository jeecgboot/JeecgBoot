package org.jeecg.modules.demo.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;
import org.jeecg.modules.demo.test.entity.ActivityCase;
import org.jeecg.modules.demo.test.entity.ActivityMonitor;

/**
 * @Description: 流程测试
 * @Author: jeecg-boot
 * @Date:   2019-05-14
 * @Version: V1.0
 */
public interface ActivityMonitorMapper extends BaseMapper<ActivityMonitor> {

    @Delete("delete from vote_monitor WHERE activity_id = #{activityId}")
    void reset(String activityId);
}
