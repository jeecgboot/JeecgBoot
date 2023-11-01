package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.service.SignInService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 签到
 */
@RestController
@RequestMapping("/a/signIn")
@Slf4j
public class SignInCtrl extends BaseApiCtrl {

    @Resource
    private SignInService signInService;

    @PostMapping
    public Result<Object> sign(){
        return success(signInService.sign());
    }

}
