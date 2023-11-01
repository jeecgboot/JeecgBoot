package org.jeecg.modules.im.controller;


import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.query_helper.QUserAvatar;
import org.jeecg.modules.im.service.UserAvatarService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/im/userAvatar")
public class UserAvatarController extends BaseBackController {
    @Resource
    UserAvatarService userAvatarService;

    @RequestMapping("/pagination")
    public Result<Object> list(QUserAvatar q){
        return success(userAvatarService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }
}
