package org.jeecg.modules.demo.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.jeecg.modules.demo.test.entity.ActivityResult;

/**
 * @Description: 流程测试
 * @Author: jeecg-boot
 * @Date:   2019-05-14
 * @Version: V1.0
 */
public interface ActivityResultMapper extends BaseMapper<ActivityResult> {

    @Delete("delete from vote_result WHERE activity_id = #{activityId}")
    void reset(String activityId);
}
