package org.jeecg.modules.im.controller;


import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.query_helper.QInvitation;
import org.jeecg.modules.im.service.InvitationService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/im/invitation")
public class InvitationController extends BaseBackController {
    @Resource
    private InvitationService inviteCodeService;

    @RequestMapping("/pagination")
    public Result<Object> list(QInvitation q){
        return success(inviteCodeService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }
}
