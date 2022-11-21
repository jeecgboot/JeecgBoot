package org.jeecg.modules.demo.test.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.demo.test.entity.ActivityCase;
import org.jeecg.modules.demo.test.entity.ActivityUser;
import org.jeecg.modules.demo.test.mapper.ActivityCaseMapper;
import org.jeecg.modules.demo.test.mapper.ActivityUserMapper;
import org.jeecg.modules.demo.test.service.IActivityCaseService;
import org.jeecg.modules.demo.test.service.IActivityUserService;
import org.springframework.stereotype.Service;

@Service
public class ActivityCaseServiceImpl extends ServiceImpl<ActivityCaseMapper, ActivityCase>  implements IActivityCaseService {

    @Override
    public void reset(String activityId) {
        getBaseMapper().reset(activityId);
    }
}
