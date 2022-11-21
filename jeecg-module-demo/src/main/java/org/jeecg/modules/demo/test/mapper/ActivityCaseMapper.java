package org.jeecg.modules.demo.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;
import org.jeecg.modules.demo.test.entity.Activity;
import org.jeecg.modules.demo.test.entity.ActivityCase;

/**
 * @Description: 流程测试
 * @Author: jeecg-boot
 * @Date:   2019-05-14
 * @Version: V1.0
 */
public interface ActivityCaseMapper extends BaseMapper<ActivityCase> {

    @Update("update vote_case set recommand_count=0,un_recommand_count=0,not_recommand_count=0  WHERE activity_id = #{activityId}")
    void reset(String activityId);
}
