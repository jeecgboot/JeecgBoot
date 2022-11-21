package org.jeecg.modules.demo.test.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.demo.test.entity.ActivityMonitor;
import org.jeecg.modules.demo.test.entity.ActivityResult;
import org.jeecg.modules.demo.test.mapper.ActivityMonitorMapper;
import org.jeecg.modules.demo.test.mapper.ActivityResultMapper;
import org.jeecg.modules.demo.test.service.IActivityMonitorService;
import org.jeecg.modules.demo.test.service.IActivityResultService;
import org.springframework.stereotype.Service;

@Service
public class ActivityResultServiceImpl extends ServiceImpl<ActivityResultMapper, ActivityResult>  implements IActivityResultService {

    @Override
    public void reset(String activityId) {
        getBaseMapper().reset(activityId);
    }

}
