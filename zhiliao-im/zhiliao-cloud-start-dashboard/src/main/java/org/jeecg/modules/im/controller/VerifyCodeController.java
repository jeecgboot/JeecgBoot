package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.query_helper.QVerifyCode;
import org.jeecg.modules.im.service.VerifyCodeService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/im/verifyCode")
public class VerifyCodeController extends BaseBackController {


    @Resource
    private VerifyCodeService verifyCodeService;

    @RequestMapping("/pagination")
    public Result<Object> pagination(QVerifyCode q){
        return success(verifyCodeService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }

}
