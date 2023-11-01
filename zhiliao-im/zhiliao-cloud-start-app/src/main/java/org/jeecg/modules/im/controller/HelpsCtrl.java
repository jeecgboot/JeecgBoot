package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.anotation.NoNeedUserToken;
import org.jeecg.modules.im.entity.Feedback;
import org.jeecg.modules.im.service.FeedbackService;
import org.jeecg.modules.im.service.FeedbackTypeService;
import org.jeecg.modules.im.service.HelpsService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 帮助
 */
@RestController
@RequestMapping("/a/helps")
public class HelpsCtrl extends BaseApiCtrl {
    @Resource
    private HelpsService helpsService;

    @NoNeedUserToken
    @RequestMapping("/all")
    public Result<Object> all(){
        return helpsService.findAll();
    }
}
