package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Notice;
import org.jeecg.modules.im.entity.query_helper.QNotice;
import org.jeecg.modules.im.service.NoticeService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 贴纸
 */
@RestController
@RequestMapping("/im/notice")
public class NoticeController extends BaseBackController {
    @Resource
    private NoticeService noticeService;

    @RequestMapping("/pagination")
    public Result<Object> list(QNotice q){
        return success(noticeService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }

    /**
     * 创建或更新
     */
    @RequestMapping("/createOrUpdate")
    public Result<Object> createOrUpdate(@RequestBody @Validated Notice notice, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return fail(bindingResult.getAllErrors().get(0));
        }
        return noticeService.createOrUpdate(notice);
    }

    @RequestMapping("/detail")
    public Result<Object> detail(@RequestParam String id){
        return success(noticeService.findById(id));
    }
}
