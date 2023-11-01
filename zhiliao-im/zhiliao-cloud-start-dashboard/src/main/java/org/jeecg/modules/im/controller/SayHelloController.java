package org.jeecg.modules.im.controller;


import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.query_helper.QSayHello;
import org.jeecg.modules.im.service.SayHelloService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/im/sayHello")
public class SayHelloController extends BaseBackController {
    @Resource
    private SayHelloService sayHelloService;

    @RequestMapping("/pagination")
    public Result<Object> list(QSayHello q){
        return success(sayHelloService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }
}
