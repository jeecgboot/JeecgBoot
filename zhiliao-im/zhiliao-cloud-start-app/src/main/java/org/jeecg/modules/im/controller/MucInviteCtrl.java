package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.query_helper.QMucInvite;
import org.jeecg.modules.im.service.MucInviteService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 进群邀请
 */
@RestController
@RequestMapping("/a/mucInvite")
public class MucInviteCtrl extends BaseApiCtrl {
    @Resource
    private MucInviteService mucInviteService;

    /**
     * 分页查询
     */
    @RequestMapping("/pagination")
    public Result<Object> pagination(QMucInvite q){
        q.setIsNeedVerify(true);
        return success(mucInviteService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }
    /**
     * 发送邀请
     */
    @RequestMapping("/send")
    public Result<Object> send(@RequestParam Integer mucId,@RequestParam Integer toUserId){
        return mucInviteService.send(getCurrentUserId(),mucId, toUserId);
    }
    /**
     * 被邀请人自己通过
     */
    @RequestMapping("/passBySelf")
    public Result<Object> passBySelf(@RequestParam Integer inviteUserId, @RequestParam Integer mucId){
        return mucInviteService.passBySelf(getCurrentUserId(),inviteUserId,mucId);
    }
    /**
     * 批量邀请
     */
    @RequestMapping("/invite")
    public Result<Object> invite(@RequestParam Integer mucId,@RequestParam String userIds){
        return mucInviteService.inviteBatch(getCurrentUserId(),mucId,userIds);
    }
}
