package org.jeecg.modules.im.controller;


import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.query_helper.QExceptionLog;
import org.jeecg.modules.im.service.ExceptionLogService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/im/exceptionLog")
public class ExceptionLogController extends BaseBackController {
    @Resource
    ExceptionLogService exceptionService;

    @RequestMapping("/pagination")
    public Result<Object> list(QExceptionLog q){
        return success(exceptionService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }
}
