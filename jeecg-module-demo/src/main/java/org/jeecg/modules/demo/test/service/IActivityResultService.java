package org.jeecg.modules.demo.test.service;

import org.jeecg.common.system.base.service.JeecgService;
import org.jeecg.modules.demo.test.entity.ActivityResult;

public interface IActivityResultService extends JeecgService<ActivityResult> {

    void reset(String activityId);
}
