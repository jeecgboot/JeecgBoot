package org.jeecg.modules.im.controller;


import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.query_helper.QFriend;
import org.jeecg.modules.im.service.FriendService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/im/friend")
public class FriendController extends BaseBackController {
    @Resource
    private FriendService friendService;

    @RequestMapping("/pagination")
    public Result<Object> list(QFriend q){
        return success(friendService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }
}
