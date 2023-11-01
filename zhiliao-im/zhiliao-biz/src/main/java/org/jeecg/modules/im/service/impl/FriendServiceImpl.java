package org.jeecg.modules.im.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.constant.MsgType;
import org.jeecg.modules.im.base.exception.BusinessException;
import org.jeecg.modules.im.base.util.Kv;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Friend;
import org.jeecg.modules.im.entity.SayHello;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.entity.query_helper.QFriend;
import org.jeecg.modules.im.mapper.FriendMapper;
import org.jeecg.modules.im.service.*;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.jeecg.modules.im.base.xmpp.MessageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 好友 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-02-03
 */
@Service
@Slf4j
public class FriendServiceImpl extends BaseServiceImpl<FriendMapper, Friend> implements FriendService {

    @Autowired
    private FriendMapper friendMapper;
    @Resource
    private UserService userService;
    @Resource
    private ClientConfigService clientConfigService;
    @Resource
    private SayHelloService sayHelloService;
    @Resource
    private MsgService msgService;
    @Resource
    private XMPPService xmppService;
    @Override
    public IPage<Friend> pagination(MyPage<Friend> page, QFriend q) {
        return friendMapper.pagination(page,q);
    }
    @Override
    public List<Friend> findAll(QFriend q) {
        return friendMapper.findAll(q);
    }

    @Override
    public Friend findByIdOfUser(Integer id, Integer userId) {
        Friend friend = new Friend();
        friend.setId(id);
        friend.setUserId(userId);
        return friendMapper.findByIdOfUser(friend);
    }
    @Override
    public Friend findOne(Integer userId, Integer toUserId) {
        if(Objects.equals(userId, toUserId)){
            return null;
        }
        Friend friend = friendMapper.findOne(userId,toUserId);
        if(friend==null){
            friend = new Friend();
            friend.setUserId(userId);
            friend.setToUserId(toUserId);
            friend.setTsCreate(getTs());
            friend.setStatus(Friend.Status.Stranger.getCode());
            friend.setAddType(Friend.AddType.Not.getCode());
            friend.setRemark(userService.findById(toUserId).getNickname());
            save(friend);
        }
        return friendMapper.findOne(userId,toUserId);
    }

    @Override
    public int getCountOfUser(Integer userId) {
        LambdaQueryWrapper<Friend> wrapper = new LambdaQueryWrapper<Friend>();
        wrapper.eq(Friend::getUserId,userId).eq(Friend::getStatus,Friend.Status.Friend.getCode());
        return (int)count(wrapper);
    }
    //主动方添加的需要保存加好友请求记录
    @Override
    public Result<Object> addFriend(Integer userId, Integer toUserId,boolean saveSayHello) {
        Friend friend = this.findOne(userId,toUserId);
        if(friend.getStatus()==Friend.Status.Friend.getCode()){
            return  fail("好友已存在");
        }
        User toUser = userService.findById(toUserId);
        if(toUser==null){
            return fail("被添加的用户不存在");
        }
        friend.setIsStar(false);
        friend.setTsLastTalk(0l);
        friend.setIsUnread(false);
        friend.setAddType(Friend.AddType.System.getCode());
        friend.setIsMsgArchive(false);
        friend.setIsNoDisturb(false);
        friend.setIsHide(false);
        friend.setTsPin(0l);
        friend.setStatus(Friend.Status.Friend.getCode());
        friend.setTsFriend(getTs());
        updateById(friend);

        if(saveSayHello){
            //保存添加记录
            SayHello hello = new SayHello();
            hello.setStatus(SayHello.Status.Accept.getCode());
            hello.setFromId(userId);
            hello.setToId(toUserId);
            hello.setMsg("你好");
            hello.setTsDeal(getTs());
            hello.setIsValid(true);
            hello.setResource(Friend.AddType.System.getCode());
            hello.setTsCreate(getTs());
            sayHelloService.save(hello);
            //xmpp添加好友
            xmppService.addFriend(userId,toUserId);
            //被添加方发送添加好友通知
            MessageBean messageBean = new MessageBean();
            messageBean.setUserId(userId);
            messageBean.setToUserId(toUserId);
            messageBean.setType(MsgType.makeFriendDirectly.getType());
            xmppService.sendMsgToOne(messageBean);
        }
        return success();
    }
    //关注用户
    @Override
    public Result<Object> followUser(Integer userId, Integer toUserId,boolean saveSayHello) {
        Friend friend = this.findOne(userId,toUserId);
        if(friend.getStatus()==Friend.Status.Follow.getCode()){
            return fail("已关注，请勿重复关注");
        }
        User toUser = userService.findById(toUserId);
        if(toUser==null){
            return fail("关注的用户不存在");
        }
        friend.setIsStar(false);
        friend.setTsLastTalk(0l);
        friend.setIsUnread(false);
        friend.setAddType(Friend.AddType.System.getCode());
        friend.setIsMsgArchive(false);
        friend.setIsNoDisturb(false);
        friend.setIsHide(false);
        friend.setTsPin(0l);
        friend.setStatus(Friend.Status.Follow.getCode());
        friend.setTsFriend(getTs());
        updateById(friend);

        if(saveSayHello){
            //保存添加记录
            SayHello hello = new SayHello();
            hello.setStatus(SayHello.Status.Accept.getCode());
            hello.setFromId(toUserId);
            hello.setToId(userId);
            hello.setMsg("欢迎关注~");
            hello.setTsDeal(getTs());
            hello.setIsValid(true);
            hello.setResource(Friend.AddType.System.getCode());
            hello.setType(SayHello.Type.FollowUser.getCode());
            hello.setTsCreate(getTs());
            sayHelloService.save(hello);
            //xmpp关注用户
            xmppService.followUser(userId,toUserId);
        }
        return success();
    }
    //主动方添加的需要保存加好友请求记录
    @Override
    public Result<Object> consoleAddFriend(Integer userId, Integer toUserId,boolean saveSayHello) {
        Friend friend = this.findOne(userId,toUserId);
        if(friend.getStatus()==Friend.Status.Friend.getCode()){
            return  fail("好友已存在");
        }
        User toUser = userService.findById(toUserId);
        if(toUser==null){
            return fail("被添加的用户不存在");
        }
        friend.setIsStar(false);
        friend.setTsLastTalk(0l);
        friend.setIsUnread(false);
        friend.setAddType(Friend.AddType.System.getCode());
        friend.setIsMsgArchive(false);
        friend.setIsNoDisturb(false);
        friend.setIsHide(false);
        friend.setTsPin(0l);
        friend.setStatus(Friend.Status.Friend.getCode());
        friend.setTsFriend(getTs());
        updateById(friend);

        if(saveSayHello) {
            //保存添加记录
            SayHello hello = new SayHello();
            hello.setStatus(SayHello.Status.Accept.getCode());
            hello.setFromId(userId);
            hello.setToId(toUserId);
            hello.setMsg("系统添加好友");
            hello.setTsDeal(getTs());
            hello.setIsValid(true);
            hello.setResource(Friend.AddType.System.getCode());
            sayHelloService.save(hello);
            //xmpp添加好友
            xmppService.addFriend(userId, toUserId);
        }
        //系统通知双方
        MessageBean messageBean = new MessageBean();
        messageBean.setToUserId(toUserId);
        messageBean.setType(MsgType.makeFriendDirectly.getType());
        xmppService.sendMsgBySys(messageBean);
        return success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> deleteOne(Integer userId, Integer toUserId) {
        Friend friend = this.findOne(userId,toUserId);
        if(friend==null||friend.getStatus()!=Friend.Status.Friend.getCode()){
            return  fail("好友不存在");
        }
        try {
            //双向删除
            //将好友关系更改为陌生人
            friend.setStatus(Friend.Status.Delete.getCode());
            friend.setIsStar(false);
            friend.setTsFriend(0l);
            friend.setTsLastTalk(0l);
            friend.setIsUnread(false);
            friend.setAddType(Friend.AddType.Not.getCode());
            friend.setIsMsgArchive(false);
            friend.setIsNoDisturb(false);
            friend.setIsHide(false);
            friend.setTsPin(0l);
            Friend toFriend = this.findOne(toUserId, userId);
            toFriend.setStatus(Friend.Status.Delete.getCode());
            toFriend.setIsStar(false);
            toFriend.setTsFriend(0l);
            toFriend.setTsLastTalk(0l);
            toFriend.setIsUnread(false);
            toFriend.setAddType(Friend.AddType.Not.getCode());
            toFriend.setIsMsgArchive(false);
            toFriend.setIsNoDisturb(false);
            toFriend.setIsHide(false);
            toFriend.setTsPin(0l);
            List<Friend> list = new ArrayList();
            Collections.addAll(list, friend, toFriend);
            updateBatchById(list);
            //删除单聊消息记录
            msgService.deleteLogic(userId,toUserId);
            msgService.deleteLogic(toUserId,userId);
            //系统通知对方已被好友删除
            User toUser = userService.findById(toUserId);
            MessageBean messageBean = new MessageBean();
            messageBean.setToUserId(toUser.getId());
            messageBean.setToUserName(toUser.getNickname());
            messageBean.setType(MsgType.del.getType());
            xmppService.sendMsgBySys(messageBean);
            return success();
        }catch (Exception e){
            log.error("删除好友失败：userId={},toUserId={},e={}", userId,toUserId, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail("删除好友失败");
        }
    }
    //拉黑
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Object> setBlack(Integer userId, Integer toUserId, boolean isBlack) {
        try{
            Friend friend = this.findOne(getCurrentUserId(),toUserId);
            Friend friend2 = this.findOne(toUserId,getCurrentUserId());
            //拉黑
            if(isBlack){
                if(friend.getTsBlack()>0){
                    return fail("对方已在黑名单中");
                }
                friend.setTsBlack(getTs());
                friend2.setTsBeenBlack(getTs());
            }else{
                //取消拉黑
                if(friend.getTsBlack()==0){
                    return fail("对方未在黑名单中");
                }
                friend.setTsBlack(0L);
                friend2.setTsBeenBlack(0L);
            }
            updateById(friend);
            updateById(friend2);

            Kv data = Kv.by("toUserId",toUserId).set("ts",isBlack?getTs():0);
            //发送给自己
            MessageBean messageBean = new MessageBean();
            messageBean.setUserId(userId);
            messageBean.setType(MsgType.black.getType());
            messageBean.setContent(JSONObject.toJSONString(data));
            xmppService.sendMsgToSelf(messageBean);
            //发送给对方
            messageBean.setToUserId(toUserId);
            xmppService.sendMsgToOne(messageBean);
            return success();
        }catch (Exception e){
            log.error("拉黑好友失败：userId={},toUserId={},e={}", userId,toUserId, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail();
        }
    }
    //阅后即焚
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Object> setReadDel(Integer userId, Integer toUserId, boolean isReadDel) {
        try{
            Friend friend = this.findOne(getCurrentUserId(),toUserId);
            friend.setIsReadDel(isReadDel);
            updateById(friend);

            Kv data = Kv.by("toUserId",toUserId).set("isReadDel",isReadDel?1:0);
            //发送给自己
            MessageBean messageBean = new MessageBean();
            messageBean.setUserId(userId);
            messageBean.setType(MsgType.readDel.getType());
            messageBean.setContent(JSONObject.toJSONString(data));
            xmppService.sendMsgToSelf(messageBean);
            return success();
        }catch (Exception e){
            log.error("设置阅后即焚失败：userId={},toUserId={},e={}", userId,toUserId, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail();
        }
    }
}
