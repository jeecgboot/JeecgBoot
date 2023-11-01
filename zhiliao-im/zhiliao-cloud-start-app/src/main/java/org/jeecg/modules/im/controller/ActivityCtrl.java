package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.ClientConfig;
import org.jeecg.modules.im.entity.Param;
import org.jeecg.modules.im.entity.SysConfig;
import org.jeecg.modules.im.service.*;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 配置
 */
@RestController
@RequestMapping("/a/activity")
public class ActivityCtrl extends BaseApiCtrl {

    @Resource
    private ActivityService activityService;
    @Resource
    private ActivityCommentService activityCommentService;

    @RequestMapping("/detail")
    public Result<Object> detail(@RequestParam Integer activityId) {
        return success(activityService.getById(activityId));
    }

    /**
     * 根据commentId查询评论列表
     * @param commentId
     * @return
     */
    @RequestMapping("/comment/listAll")
    public Result<Object> listAllByCommentId(@RequestParam Integer commentId) {
        return success(activityCommentService.findByCommentId(commentId));
    }
}
