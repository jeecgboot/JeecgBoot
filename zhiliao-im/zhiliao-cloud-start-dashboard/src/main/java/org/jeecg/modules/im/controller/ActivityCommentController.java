package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.ActivityComment;
import org.jeecg.modules.im.entity.query_helper.QActivityComment;
import org.jeecg.modules.im.service.ActivityCommentService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 动态评论
 */
@RestController
@RequestMapping("/im/activityComment")
public class ActivityCommentController extends BaseBackController {
    @Resource
    private ActivityCommentService activityService;

    @RequestMapping("/pagination")
    public Result<Object> list(QActivityComment q){
        return success(activityService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }

    /**
     * 添加或更新
     */
    @RequestMapping("/createOrUpdate")
    public Result<Object> createOrUpdate(@RequestBody @Validated ActivityComment item, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return fail(bindingResult.getAllErrors().get(0));
        }
        return activityService.createOrUpdate(item);
    }

    /**
     * 批量删除
     */
    @RequestMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return activityService.del(ids);
    }
}
