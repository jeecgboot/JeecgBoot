package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.query_helper.QMucMember;
import org.jeecg.modules.im.service.MucMemberService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 群组成员
 */
@RestController
@RequestMapping("/im/mucMember")
public class MucMemberController extends BaseBackController {
    @Resource
    private MucMemberService mucMemberService;

    @RequestMapping("/pagination")
    public Result<Object> list(QMucMember q){
        return success(mucMemberService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }

    /**
     * 邀请用户进群
     */
    @RequestMapping("/invite")
    public Result<Object> invite(@RequestParam Integer mucId, @RequestParam String userIds) {
        return mucMemberService.consoleInvite(mucId,userIds);
    }
}
