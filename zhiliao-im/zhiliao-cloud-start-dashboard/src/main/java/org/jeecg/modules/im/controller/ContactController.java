package org.jeecg.modules.im.controller;


import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.query_helper.QContact;
import org.jeecg.modules.im.service.ContactService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/im/contact")
public class ContactController extends BaseBackController {
    @Resource
    private ContactService contactService;

    @RequestMapping("/pagination")
    public Result<Object> list(QContact q){
        return success(contactService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }
}
