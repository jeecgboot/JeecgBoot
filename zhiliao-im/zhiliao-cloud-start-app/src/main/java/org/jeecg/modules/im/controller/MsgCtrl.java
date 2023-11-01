package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.util.Kv;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Friend;
import org.jeecg.modules.im.entity.Msg;
import org.jeecg.modules.im.entity.query_helper.QFriend;
import org.jeecg.modules.im.entity.query_helper.QMsg;
import org.jeecg.modules.im.service.FriendService;
import org.jeecg.modules.im.service.MsgService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息
 */
@RestController
@RequestMapping("/a/msg")
public class MsgCtrl extends BaseApiCtrl {
    @Resource
    private FriendService friendService;
    @Resource
    private MsgService msgService;

    /**
     * 分页查询
     */
    @RequestMapping("/pagination")
    public Result<Object> paginationApi(QMsg q){
        q.setUserId(getCurrentUserId());
        if(q.getAfter()){
            q.setPageSize(Integer.MAX_VALUE);
        }
        return success(msgService.paginationApi(q));
    }
    /**
     * 分页查询
     */
    @RequestMapping("/syncAll")
    public Result<Object> syncAll(QMsg q){
        q.setUserId(getCurrentUserId());
        //查询我的好友
        QFriend qf = new QFriend();
        qf.setUserId(getCurrentUserId());
        List<Friend> friends = friendService.findAll(qf);
        List<Msg> msgs = new ArrayList<>();
        for (Friend friend : friends) {
            q.setToUserId(friend.getToUser().getId());
            msgs.addAll(msgService.paginationApi(q));
        }
        return success(msgs);
    }
}
