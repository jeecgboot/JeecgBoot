package org.jeecg.modules.demo.test.application.impl;

import lombok.var;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.demo.test.application.ActivityApplication;
import org.jeecg.modules.demo.test.service.IActivityCaseService;
import org.jeecg.modules.demo.test.service.IActivityMonitorService;
import org.jeecg.modules.demo.test.service.IActivityResultService;
import org.jeecg.modules.demo.test.service.IActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActivityApplicationImpl implements ActivityApplication {

    @Autowired
    private IActivityService activityService;

    @Autowired
    private IActivityMonitorService activityMonitorService;

    @Autowired
    private IActivityCaseService activityCaseService;

    @Autowired
    private IActivityResultService activityResultService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void reset(String id) {
        var activity = activityService.getById(id);
        if(activity == null){
            throw new RuntimeException("活动不存在！");
        }
        //清空所有的评分数据
        activityMonitorService.reset(id);
        activityCaseService.reset(id);
        activityResultService.reset(id);

        activity.setStatus("un_start");
        activityService.updateById(activity);

    }
}
