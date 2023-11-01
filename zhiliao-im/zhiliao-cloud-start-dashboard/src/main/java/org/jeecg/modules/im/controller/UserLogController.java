package org.jeecg.modules.im.controller;


import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.query_helper.QUserLog;
import org.jeecg.modules.im.service.UserLogService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/im/userLog")
public class UserLogController extends BaseBackController {
    @Resource
    UserLogService clientExceptionService;

    @RequestMapping("/pagination")
    public Result<Object> list(QUserLog q){
        return success(clientExceptionService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }
}
