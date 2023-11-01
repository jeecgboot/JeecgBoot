package org.jeecg.modules.im.xxljob;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.im.base.tools.ToolDateTime;
import org.jeecg.modules.im.service.DeviceService;
import org.jeecg.modules.im.service.OnlineDataService;
import org.jeecg.modules.im.service.StatisticService;
import org.jeecg.modules.im.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 红包任务
 */
@Component
@Slf4j
public class RedPackBackJobHandler {

    @Resource
    private UserService userService;
    @Resource
    private DeviceService deviceService;
    @Resource
    private StatisticService statisticService;
    @Resource
    private OnlineDataService onlineDataService;

    //超时退回
    @XxlJob(value = "redPackBack")
    public ReturnT<String> redPackBack(String params) {
        String date = ToolDateTime.getDate(ToolDateTime.pattern_ymd);
        return ReturnT.SUCCESS;
    }
    //未领取提醒
    @XxlJob(value = "redPackRemind")
    public ReturnT<String> redPackRemind(String params) {
        String date = ToolDateTime.getDate(ToolDateTime.pattern_ymd);
        return ReturnT.SUCCESS;
    }
}

