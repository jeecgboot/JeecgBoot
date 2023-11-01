package org.jeecg.modules.im.controller;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.constant.MsgType;
import org.jeecg.modules.im.base.util.Kv;
import org.jeecg.modules.im.configuration.ClientOperLog;
import org.jeecg.modules.im.entity.Friend;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.entity.query_helper.QFriend;
import org.jeecg.modules.im.service.FriendService;
import org.jeecg.modules.im.service.UserService;
import org.jeecg.modules.im.service.XMPPService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.jeecg.modules.im.base.xmpp.MessageBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 好友
 */
@RestController
@RequestMapping("/a/friend")
public class FriendCtrl extends BaseApiCtrl {
    @Resource
    private UserService userService;
    @Resource
    private FriendService friendService;
    @Resource
    private XMPPService xmppService;
    /**
     * 添加
     */
    @PostMapping("/")
    public Result<Object> add(@RequestParam int toUserId){
        return success();
    }
    /**
     * 删除某个好友
     */
    @PostMapping("/deleteOne")
    public Result<Object> deleteOne(@RequestParam int toUserId){
        return friendService.deleteOne(getCurrentUserId(),toUserId);
    }

    /**
     * 查询用户所有的好友
     */
    @ClientOperLog(module = "好友", type = "查询所有好友", desc = "")
    @PostMapping("/all")
    public Result<Object> allOfMy(){
        QFriend q = new QFriend();
        q.setUserId(getCurrentUserId());
        return success(friendService.findAll(q));
    }

    /**
     * 查询用户指定好友
     */
    @ClientOperLog(module = "好友", type = "查询某个好友", desc = "")
    @PostMapping("/one")
    public Result<Object> oneOfMy(@RequestParam Integer toUserId){
        return success(friendService.findOne(getCurrentUserId(),toUserId));
    }

    /**
     * 设置备注
     * 描述，电话号码等其他信息接口
     * 用户不一定是当前用户的好友
     */
    @PostMapping("/updateProfile")
    public Result<Object> updateProfile(@RequestParam Integer toUserId,
                                @RequestParam(required = false) String remark,
                                @RequestParam(required = false) String phone,
                                @RequestParam(required = false) String info,
                                @RequestParam(required = false) String tagIds) {
        Friend friend = friendService.findOne(getCurrentUserId(),toUserId);
        if(isEmpty(remark)){
            User user = userService.findById(friend.getToUserId());
            friend.setRemark(user.getNickname());
        }else{
            friend.setRemark(remark);
        }
        friend.setPhone(phone);
        friend.setInfo(info);
        friend.setTagIds(tagIds);
        Kv data = Kv.by("toUserId",friend.getToUserId()).set("remark",friend.getRemark()).set("phone",phone).set("info",info).set("tagIds",tagIds);
        if(friendService.updateById(friend)){
            //发送邀请入群消息给用户
            MessageBean messageBean = new MessageBean();
            messageBean.setUserId(friend.getUserId());
            messageBean.setContent(JSONObject.toJSONString(data));
            messageBean.setType(MsgType.updateFriend.getType());
            xmppService.sendMsgToSelf(messageBean);
        }
        return success();
    }
    /**
     * 设置星标
     */
    @PostMapping("/setStar")
    public Result<Object> setStar(@RequestParam Integer toUserId,@RequestParam boolean isStar){
        Integer userId = getCurrentUserId();
        Friend friend = friendService.findOne(userId,toUserId);
        friend.setIsStar(isStar);
        friendService.updateById(friend);
        Kv data = Kv.by("toUserId",toUserId).set("isStar",isStar?1:0);
        //发送给自己
        MessageBean messageBean = new MessageBean();
        messageBean.setUserId(userId);
        messageBean.setType(MsgType.star.getType());
        messageBean.setContent(JSONObject.toJSONString(data));
        xmppService.sendMsgToSelf(messageBean);
        return success();
    }
    /**
     * 拉黑
     */
    @PostMapping("/setBlack")
    public Result<Object> setBlack(@RequestParam Integer toUserId,@RequestParam boolean isBlack){
        return friendService.setBlack(getCurrentUserId(),toUserId,isBlack);
    }

    /**
     * 设置星标
     */
    @PostMapping("/setReadDel")
    public Result<Object> setReadDel(@RequestParam Integer toUserId,@RequestParam boolean isReadDel){
        return friendService.setReadDel(getCurrentUserId(),toUserId,isReadDel);

    }

}
