package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.query_helper.QLoginLog;
import org.jeecg.modules.im.service.LoginLogService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/im/loginLog")
public class LoginLogController extends BaseBackController {

    @Resource
    private LoginLogService loginLogService;

    @RequestMapping("/pagination")
    public Result<Object> loginLog(QLoginLog q){
        return success(loginLogService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }

}
