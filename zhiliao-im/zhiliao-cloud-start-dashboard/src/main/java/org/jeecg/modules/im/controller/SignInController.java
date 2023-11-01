package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.query_helper.QSignIn;
import org.jeecg.modules.im.service.SignInService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/im/signIn")
public class SignInController extends BaseBackController {

    @Resource
    private SignInService signInService;

    @RequestMapping("/pagination")
    public Result<Object> adminLoginLog(QSignIn q){
        return success(signInService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }

}
