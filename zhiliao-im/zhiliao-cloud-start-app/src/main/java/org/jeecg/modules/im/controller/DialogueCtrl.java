package org.jeecg.modules.im.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.constant.MsgType;
import org.jeecg.modules.im.base.util.Kv;
import org.jeecg.modules.im.entity.Friend;
import org.jeecg.modules.im.entity.MucMember;
import org.jeecg.modules.im.service.*;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.jeecg.modules.im.base.xmpp.MessageBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 对话
 */
@RestController
@Slf4j
@RequestMapping("/a/dialogue")
public class DialogueCtrl extends BaseApiCtrl {
    @Resource
    private FriendService friendService;
    @Resource
    private MucMemberService mucMemberService;
    @Resource
    private MucService mucService;
    @Resource
    private MsgService msgService;
    @Resource
    private XMPPService xmppService;
    /**
     * 置顶对话
     */
    @PostMapping("/top")
    public Result<Object> top(@RequestParam Integer toId, @RequestParam Boolean isMuc, @RequestParam(defaultValue = "false") Boolean isCancel) {
        long ts = getTs();
        if (isCancel) {
            ts = 0L;
        }
        try {
            if (isMuc) {
                MucMember mucMember = mucMemberService.findByMucIdOfUser(toId, getCurrentUserId());
                if (mucMember == null) {
                    return fail("聊天不存在");
                }
                mucMember.setTsPin(ts);
                mucMemberService.updateById(mucMember);
            } else {
                Friend friend = friendService.findOne(getCurrentUserId(), toId);
                if (friend == null) {
                    return fail("聊天不存在");
                }
                friend.setTsPin(ts);
                friendService.updateById(friend);
            }
            MessageBean messageBean = new MessageBean();
            messageBean.setUserId(getCurrentUserId());
            messageBean.setType(MsgType.dialoguePin.getType());
            Kv data = Kv.by("toId", toId).set("isMuc", isMuc ? 1 : 0).set("tsPin", ts);
            messageBean.setContent(JSONObject.toJSONString(data));
            xmppService.sendMsgToSelf(messageBean);
        }catch (Exception e){
            return fail();
        }
        return success(ts);
    }
    /**
     * 隐藏对话
     */
    @PostMapping("/hide")
    public Result<Object> hide(@RequestParam Integer toId, @RequestParam Boolean isMuc, @RequestParam(defaultValue = "false") Boolean isCancel) {
        try {
            if (isMuc) {
                MucMember mucMember = mucMemberService.findByMucIdOfUser(toId, getCurrentUserId());
                if (mucMember == null) {
                    return fail("聊天不存在");
                }
                mucMember.setIsHide(!isCancel);
                mucMemberService.updateById(mucMember);
            } else {
                Friend friend = friendService.findOne(getCurrentUserId(), toId);
                if (friend == null) {
                    return fail("聊天不存在");
                }
                friend.setIsHide(!isCancel);
                friendService.updateById(friend);
            }
            MessageBean messageBean = new MessageBean();
            messageBean.setUserId(getCurrentUserId());
            messageBean.setType(MsgType.hideDialogue.getType());
            Kv data = Kv.by("toId", toId).set("isMuc", isMuc ? 1 : 0).set("isCancel", isCancel?1:0);
            messageBean.setContent(JSONObject.toJSONString(data));
            xmppService.sendMsgToSelf(messageBean);
        }catch (Exception e){
            return fail();
        }
        return success();
    }

    /**
     * 对话免打扰
     */
    @PostMapping("/noDisturb")
    public Result<Object> noDisturb(@RequestParam Integer toId, @RequestParam Boolean isMuc, @RequestParam(defaultValue = "false") Boolean isCancel) {
        try {
            if (isMuc) {
                MucMember mucMember = mucMemberService.findByMucIdOfUser(toId, getCurrentUserId());
                if (mucMember == null) {
                    return fail("聊天不存在");
                }
                mucMember.setIsNoDisturb(!isCancel);
                mucMemberService.updateById(mucMember);
            } else {
                Friend friend = friendService.findOne(getCurrentUserId(), toId);
                if (friend == null) {
                    return fail("聊天不存在");
                }
                friend.setIsNoDisturb(!isCancel);
                friendService.updateById(friend);
            }
            MessageBean messageBean = new MessageBean();
            messageBean.setUserId(getCurrentUserId());
            messageBean.setType(MsgType.dialogueNoDisturb.getType());
            Kv data = Kv.by("toId", toId).set("isMuc", isMuc ? 1 : 0).set("flag", isCancel?0:1);
            messageBean.setContent(JSONObject.toJSONString(data));
            xmppService.sendMsgToSelf(messageBean);
            return success();
        }catch (Exception e){
            return fail();
        }
    }

    /**
     * /**
     * 对话归档
     */
    @PostMapping("/archive")
    public Result<Object> archive(@RequestParam Integer toId, @RequestParam Boolean isMuc, @RequestParam(defaultValue = "false") Boolean isCancel) {
        try {
            if (isMuc) {
                MucMember mucMember = mucMemberService.findByMucIdOfUser(toId, getCurrentUserId());
                if (mucMember == null) {
                    return fail("聊天不存在");
                }
                mucMember.setIsMsgArchive(!isCancel);
                mucMemberService.updateById(mucMember);
            } else {
                Friend friend = friendService.findOne(getCurrentUserId(), toId);
                if (friend == null) {
                    return fail("聊天不存在");
                }
                friend.setIsMsgArchive(!isCancel);
                friendService.updateById(friend);
            }
            MessageBean messageBean = new MessageBean();
            messageBean.setUserId(getCurrentUserId());
            messageBean.setType(MsgType.dialogueArchive.getType());
            Kv data = Kv.by("toId", toId).set("isMuc", isMuc ? 1 : 0).set("flag", isCancel?0:1);
            messageBean.setContent(JSONObject.toJSONString(data));
            xmppService.sendMsgToSelf(messageBean);
            return success();
        }catch (Exception e){
            return fail();
        }
    }

    /**
     * 对话已读未读
     * 取消表示已读
     * 非取消表示未读
     */
    @PostMapping("/unread")
    public Result<Object> unread(@RequestParam Integer toId, @RequestParam Boolean isMuc,@RequestParam Boolean isCancel) {
        try {
            if (isMuc) {
                MucMember mucMember = mucMemberService.findByMucIdOfUser(toId, getCurrentUserId());
                if (mucMember == null) {
                    return fail("聊天不存在");
                }
                mucMember.setIsUnread(!isCancel);
                mucMemberService.updateById(mucMember);
            } else {
                Friend friend = friendService.findOne(getCurrentUserId(), toId);
                if (friend == null) {
                    return fail("聊天不存在");
                }
                friend.setIsUnread(!isCancel);
                friendService.updateById(friend);
                //已读对方发给我的消息
                msgService.readAllReceive(friend.getUserId(),friend.getToUserId());
            }
            MessageBean messageBean = new MessageBean();
            messageBean.setUserId(getCurrentUserId());
            messageBean.setType(MsgType.dialogueRead.getType());
            Kv data = Kv.by("toId", toId).set("isMuc", isMuc ? 1 : 0).set("flag", isCancel ? 0 : 1);
            messageBean.setContent(JSONObject.toJSONString(data));
            xmppService.sendMsgToSelf(messageBean);
            return success();
        }catch (Exception e){
            log.error("标记已读异常：{0}",e);
            return  fail();
        }
    }


    /**
     * 删除对话
     */
    @PostMapping("/del")
    public Result<Object> del(@RequestParam Integer toId, @RequestParam Boolean isMuc, @RequestParam Boolean isClear) {
        if (isMuc) {
            MucMember mucMember = mucMemberService.findByMucIdOfUser(toId, getCurrentUserId());
            if (mucMember == null) {
                return fail("聊天不存在");
            }
            mucMember.setIsHide(true);
            //清空聊天记录,这里是设置隐藏
            if (isClear) {
                mucMember.setTsMsgVisible(getTs());
            }
            if (mucMemberService.updateById(mucMember)) {
                return success();
            }
            return fail();
        }
        //删除单聊记录
        return msgService.deleteLogic(getCurrentUserId(),toId);
    }
}
