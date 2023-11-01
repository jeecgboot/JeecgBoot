package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.Feedback;
import org.jeecg.modules.im.service.FeedbackService;
import org.jeecg.modules.im.service.FeedbackTypeService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 意见反馈
 */
@RestController
@RequestMapping("/a/feedback")
public class FeedbackCtrl extends BaseApiCtrl {
    @Resource
    private FeedbackTypeService feedbackTypeService;
    @Resource
    private FeedbackService feedbackService;

    @RequestMapping("/allType")
    public Result<Object> allType(){
        return feedbackTypeService.findAll();
    }
    @RequestMapping("/all")
    public Result<Object> all(){
        return feedbackService.findAll();
    }
    @RequestMapping("/submit")
    public Result<Object> submit(Feedback feedback){
        feedback.setUserId(getCurrentUserId());
        feedback.setTsCreate(getTs());
        return success(feedbackService.save(feedback));
    }
}
