package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.constant.MsgType;
import org.jeecg.modules.im.base.exception.BusinessException;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.*;
import org.jeecg.modules.im.entity.query_helper.QSayHello;
import org.jeecg.modules.im.mapper.SayHelloMapper;
import org.jeecg.modules.im.service.*;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.jeecg.modules.im.base.xmpp.MessageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 加好友的回话记录 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-26
 */
@Service
public class SayHelloServiceImpl extends BaseServiceImpl<SayHelloMapper, SayHello> implements SayHelloService {
    @Autowired
    private SayHelloMapper sayHelloMapper;
    @Resource
    private UserService userService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private UserSettingService userSettingService;
    @Resource
    private ClientConfigService clientConfigService;
    @Resource
    private SayHelloService sayHelloService;
    @Resource
    private XMPPService xmppService;
    @Resource
    private FriendService friendService;
    @Resource
    private SayHelloReplyService sayHelloReplyService;

    @Override
    public IPage<SayHello> pagination(MyPage<SayHello> page, QSayHello q) {
        return sayHelloMapper.pagination(page,q);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> sendAdd(Integer userId, Integer toUserId, Integer resource, String who, String remark) {
        try {
            if(userId.equals(toUserId)){
                return fail("您不能添加自己");
            }
            //判断来源
            ClientConfig clientConfig = clientConfigService.get();
            if(resource==Friend.AddType.Account.getCode()){
                if(!clientConfig.getAccountAdd()){
                    return fail("已关闭通过账号添加好友");
                }
            }else if(resource==Friend.AddType.Mobile.getCode()){
                if(!clientConfig.getAccountAdd()){
                    return fail("已关闭通过手机号添加好友");
                }
            }else if(resource==Friend.AddType.Nickname.getCode()){
                if(!clientConfig.getAccountAdd()){
                    return fail("已关闭通过昵称添加好友");
                }
            }else if(resource==Friend.AddType.Username.getCode()){
                if(!clientConfig.getAccountAdd()){
                    return fail("已关闭通过用户名添加好友");
                }
            }else if(resource==Friend.AddType.Card.getCode()){
                if(!clientConfig.getAccountAdd()){
                    return fail("已关闭通过名片添加好友");
                }
            }else if(resource==Friend.AddType.Muc.getCode()){
                if(!clientConfig.getAccountAdd()){
                    return fail("已关闭通过群聊添加好友");
                }
            }else if(resource==Friend.AddType.Scan.getCode()){
                if(!clientConfig.getAccountAdd()){
                    return fail("已关闭通过扫码添加好友");
                }
            }
            User user = userService.findByIdWithInfo(userId);
            UserSetting userSetting = userSettingService.findByUserId(userId);
            if (userSetting.getIsNoAddFriend()) {
                return fail("你已被禁止加好友");
            }
            if (user.getType() > User.Type.business.getCode()) {
                return fail("非普通用户无法添加好友");
            }
            User toUser = userService.findByIdWithInfo(toUserId);
            UserSetting toUserSetting = userSettingService.findByUserId(toUserId);
            if (toUser == null) {
                return fail("该用户不存在");
            }
            if (toUser.getType() > User.Type.business.getCode()) {
                return fail("该用户不可添加");
            }
            if (toUserSetting.getIsNoBeAddFriend()) {
                return fail("该用户禁止被加好友");
            }
            if (toUser.getTsLocked()>0) {
                return fail("该用户已被冻结");
            }
            //判断对方是否允许该来源方式添加
            if(resource==Friend.AddType.Account.getCode()){
                if(!toUserSetting.getAccountAdd()){
                    return fail("对方不允许通过账号添加");
                }
            }else if(resource==Friend.AddType.Mobile.getCode()){
                if(!toUserSetting.getMobileAdd()){
                    return fail("对方不允许通过手机号添加");
                }
            }else if(resource==Friend.AddType.Nickname.getCode()){
                if(!toUserSetting.getNicknameAdd()){
                    return fail("对方不允许通过昵称添加");
                }
            }else if(resource==Friend.AddType.Username.getCode()){
                if(!toUserSetting.getUsernameAdd()){
                    return fail("对方不允许通过用户名添加");
                }
            }else if(resource==Friend.AddType.Card.getCode()){
                if(!toUserSetting.getCardAdd()){
                    return fail("对方不允许通过名片添加");
                }
            }else if(resource==Friend.AddType.Muc.getCode()){
                if(!toUserSetting.getMucAdd()){
                    return fail("对方不允许通过群聊添加");
                }
            }else if(resource==Friend.AddType.Scan.getCode()){
                if(!toUserSetting.getScanAdd()){
                    return fail("对方不允许通过扫码添加");
                }
            }
            //验证普通用户的好友数量上限
            if (user.getType() == User.Type.common.getCode()) {
                if (userSetting.getMaxFriend() > 0 && friendService.getCountOfUser(user.getId()) >= userSetting.getMaxFriend()) {
                    return fail("您的好友数量已达上限");
                }
            }
            //查询对方好友数量
            if (toUser.getType() == User.Type.common.getCode()) {
                if (toUserSetting.getMaxFriend() > 0 && friendService.getCountOfUser(toUser.getId()) >= toUserSetting.getMaxFriend()) {
                    return fail("对方好友数量已达上限");
                }
            }
            Friend friend2 = friendService.findOne(toUserId, userId);
            Friend friend = friendService.findOne(userId, toUserId);

            //判断是否被对方拉黑
            if(friend.getTsBeenBlack()>0){
                return fail("对方已将你拉黑,无法添加");
            }
            //判断是否拉黑对方
            if(friend.getTsBlack()>0){
                return fail("对方被你拉黑,请取消后再添加");
            }
            //判断对方是否发送过加好友请求
            if (friend2.getStatus() == Friend.Status.Ask.getCode()) {
                //对方发送过验证，表示我通过请求，双方成为好友
                //查询对方最后发的验证请求
                SayHello sayHello = findLatest(toUserId,userId);
                sayHello.setTsDeal(getTs());
                sayHello.setStatus(SayHello.Status.Accept.getCode());
                if(!updateById(sayHello)){
                    throw new BusinessException("更新请求失败");
                }
                friend.setStatus(Friend.Status.Friend.getCode());
                friend.setAddType(sayHello.getResource());
                friend.setTsFriend(getTs());
                if(!friendService.updateById(friend)){
                    throw new BusinessException("更新正向好友失败");
                }
                friend2.setStatus(Friend.Status.Friend.getCode());
                friend2.setAddType(sayHello.getResource());
                friend2.setTsFriend(getTs());
                if(!friendService.updateById(friend2)){
                    throw new BusinessException("更新反向好友失败");
                }
                //我发送通过好友请求
                MessageBean messageBean = new MessageBean();
                messageBean.setUserId(user.getId());
                messageBean.setType(MsgType.passFriendAddRequest.getType());
                xmppService.sendMsgToSelf(messageBean);
                return success("成功添加对方为好友！");
            }
            if (friend.getStatus() == Friend.Status.Friend.getCode()) {
                return fail("对方已是你的好友");
            }
            //等待对方验证
            if (friend.getStatus() == Friend.Status.Ask.getCode()) {
                //发送打招呼
                SayHello sayHello = findLatest(userId,toUserId);
                //打过招呼了，状态是有效的，并且是等待中的
                if(sayHello!=null&&sayHello.getIsValid()&&sayHello.getStatus()==SayHello.Status.Waiting.getCode()){
                    SayHelloReply sayHelloReply = new SayHelloReply();
                    sayHelloReply.setIsSend(true);
                    sayHelloReply.setTsCreate(getTs());
                    sayHelloReply.setHelloId(sayHello.getId());
                    sayHelloReply.setMsg(who);
                    sayHelloReplyService.save(sayHelloReply);
                    return success(sayHello);
                }
            }
            if(!isEmpty(remark)){
                if(!friend.getRemark().equals(remark)){
                    friend.setRemark(remark);
                    friendService.updateById(friend);
                }
            }
            //如果互为陌生人、删除
            if (friend.getStatus()==Friend.Status.Stranger.getCode() && friend2.getStatus()==Friend.Status.Stranger.getCode()||friend.getStatus()==Friend.Status.Delete.getCode() && friend2.getStatus()==Friend.Status.Delete.getCode()) {
                //发送打招呼
                SayHello sayHello = findLatest(userId,toUserId);
                //打过招呼了，状态是有效的，并且是等待中的
                if(sayHello!=null&&sayHello.getIsValid()&&sayHello.getStatus()==SayHello.Status.Waiting.getCode()){
                    SayHelloReply sayHelloReply = new SayHelloReply();
                    sayHelloReply.setIsSend(true);
                    sayHelloReply.setTsCreate(getTs());
                    sayHelloReply.setHelloId(sayHello.getId());
                    sayHelloReply.setMsg(who);
                    sayHelloReplyService.save(sayHelloReply);
                }else{
                    sayHello = new SayHello();
                    sayHello.setFromId(userId);
                    sayHello.setToId(toUserId);
                    sayHello.setTsCreate(getTs());
                    sayHello.setMsg(who);
                    sayHello.setResource(resource);
                    sayHello.setIsValid(true);
                    sayHello.setStatus(SayHello.Status.Waiting.getCode());
                    sayHelloService.save(sayHello);
                }
                friend.setStatus(Friend.Status.Ask.getCode());
                friendService.updateById(friend);
                return success(sayHello);
            } else {
                return fail("发送失败");
            }
        }catch (Exception e){
            log.error("发送请求加好友异常:{0}",e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return fail("发送请求加好友失败，请重试");
        }
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> sendFollow(Integer userId, Integer toUserId, Integer resource) {
        try {
            if(userId.equals(toUserId)){
                return fail("您不能关注自己");
            }
            User user = userService.findById(userId);
            User toUser = userService.findById(toUserId);
            if (toUser == null) {
                return fail("该公众号不存在");
            }
            if (toUser.getTsLocked()>0) {
                return fail("该公众号已被禁用");
            }
            Friend friend = friendService.findOne(userId, toUserId);
            //判断是否被对方拉黑
            Friend friend2 = friendService.findOne(toUserId, userId);
            if(friend.getTsBeenBlack()>0){
                return fail("对方已将你拉黑，无法添加");
            }
            if (friend.getStatus() == Friend.Status.Friend.getCode()) {
                return fail("你已关注对方，请勿重复关注");
            }
            //如果互为陌生人
            if (friend.getStatus()==Friend.Status.Stranger.getCode() && friend2.getStatus()==Friend.Status.Stranger.getCode()) {
                friend.setStatus(Friend.Status.Follow.getCode());
                friend.setTsFriend(getTs());//关注时间
                friend2.setStatus(Friend.Status.Fans.getCode());
                friend2.setTsFriend(getTs());//成为粉丝时间
                if(!friendService.updateById(friend)){
                    throw new BusinessException("更新关注失败");
                }
                if(!friendService.updateById(friend2)){
                    throw new BusinessException("更新粉丝失败");
                }
                return success("关注成功");
            } else {
                return fail("关注失败");
            }
        }catch (Exception e){
            log.error("处理好友请求异常:{0}",e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return fail("请求加好友失败，请重试");
        }
    }

    @Override
    public List<SayHello> findAllSend(Integer userId) {
        return sayHelloMapper.findAllSend(userId);
    }
    @Override
    public List<SayHello> findAllReceive(Integer userId) {
        return sayHelloMapper.findAllReceive(userId);
    }

    @Override
    public SayHello findLatest(Integer userId,Integer toUserId){
        return sayHelloMapper.findLatest(userId,toUserId);
    }
    @Override
    public SayHello findById(Integer userId,Integer id){
        return sayHelloMapper.findById(userId,id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> deal(Integer id, boolean isAccept, Integer userId) {
        try {
            SayHello sayHello = sayHelloMapper.selectById(id);
            if (sayHello == null || !sayHello.getToId().equals(userId)) {
                return fail("请求不存在");
            }
            if (!sayHello.getIsValid()) {
                return fail("请求已过期");
            }
            if (!sayHello.getStatus().equals(SayHello.Status.Waiting.getCode())) {
                return fail("请求已处理过");
            }
            //请求发起者
            User fromUser = userService.findByIdWithInfo(sayHello.getFromId());
            if (fromUser == null) {
                return fail("该用户不存在");
            }
            UserSetting fromUserSetting = userSettingService.findByUserId(fromUser.getId());
            if (fromUserSetting.getIsNoBeAddFriend()) {
                return fail("该用户禁止被加好友");
            }
            if (fromUser.getTsLocked()>0) {
                return fail("该用户已被冻结");
            }
            //被请求者
            User toUser = userService.findByIdWithInfo(sayHello.getToId());
            UserSetting toUserSetting = userSettingService.findByUserId(toUser.getId());

            //对方的好友数量
            if (fromUserSetting.getMaxFriend() > 0 && friendService.getCountOfUser(fromUser.getId()) >= fromUserSetting.getMaxFriend()) {
                return fail("对方好友数量已达上限");
            }
            //查询我的好友数量
            if (toUserSetting.getMaxFriend() > 0 && friendService.getCountOfUser(toUser.getId()) >= toUserSetting.getMaxFriend()) {
                return fail("你的好友数量已达上限");
            }
            //接受
            if (isAccept) {
                sayHello.setTsDeal(getTs());
                sayHello.setStatus(SayHello.Status.Accept.getCode());
                if(!updateById(sayHello)){
                    throw new BusinessException("更新请求失败");
                }
                Friend friend = friendService.findOne(sayHello.getFromId(), sayHello.getToId());
                Friend friend2 = friendService.findOne(sayHello.getToId(), sayHello.getFromId());
                friend.setStatus(Friend.Status.Friend.getCode());
                friend.setAddType(sayHello.getResource());
                friend.setTsFriend(getTs());
                if(!friendService.updateById(friend)){
                    throw new BusinessException("更新正向好友失败");
                }

                friend2.setStatus(Friend.Status.Friend.getCode());
                friend2.setAddType(sayHello.getResource());
                friend2.setTsFriend(getTs());
                if(!friendService.updateById(friend2)){
                    throw new BusinessException("更新反向好友失败");
                }
                //被添加方发送通过给添加方
                MessageBean messageBean = new MessageBean();
                messageBean.setUserId(toUser.getId());
                messageBean.setUserName(toUser.getNickname());
                messageBean.setToUserId(fromUser.getId());
                messageBean.setToUserName(fromUser.getNickname());
                messageBean.setType(MsgType.passFriendAddRequest.getType());
                xmppService.sendMsgToOne(messageBean);
                return success("成功添加对方为好友！");
            }
            //拒绝
            return fail("处理失败，请重试");
        }catch (Exception e){
            log.error("处理好友请求异常:{0}",e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return fail("处理失败，请重试");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> reply(Integer id, String msg, Integer userId) {
        try {
            if(isEmpty(msg)){
                return fail("回复内容不能为空");
            }
            SayHello sayHello = sayHelloMapper.selectById(id);
            if (sayHello == null || !(sayHello.getToId().equals(userId)||sayHello.getFromId().equals(userId))) {
                return fail("请求不存在");
            }
            if (!sayHello.getIsValid()) {
                return fail("请求已过期");
            }
            if (!sayHello.getStatus().equals(SayHello.Status.Waiting.getCode())) {
                return fail("请求已处理过");
            }
            boolean isSend = false;
            //当前用户是请求者
            if(sayHello.getFromId().equals(userId)){
                isSend = true;
                //判断对方是否已回复
                if(sayHelloReplyService.findByHelloId(id,false).isEmpty()){
                    return fail("请等待对方回复");
                }
            }
            SayHelloReply reply = new SayHelloReply();
            reply.setIsSend(isSend);
            reply.setHelloId(id);
            reply.setMsg(msg);
            reply.setTsCreate(getTs());
            if(!sayHelloReplyService.save(reply)){
                return fail("发送回复失败");
            }
            return success();
        }catch (Exception e){
            log.error("回复加好友请求异常:{0}",e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return fail("回复失败，请重试");
        }
    }
}
