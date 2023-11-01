package org.jeecg.modules.im.controller;


import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.query_helper.QUserLog;
import org.jeecg.modules.im.entity.query_helper.QUserNickname;
import org.jeecg.modules.im.service.UserNicknameService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/im/userNickname")
public class UserNicknameController extends BaseBackController {
    @Resource
    UserNicknameService userNicknameService;

    @RequestMapping("/pagination")
    public Result<Object> list(QUserNickname q){
        return success(userNicknameService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }
}
