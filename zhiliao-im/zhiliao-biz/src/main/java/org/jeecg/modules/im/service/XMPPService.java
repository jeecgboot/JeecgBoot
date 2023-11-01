package org.jeecg.modules.im.service;

import org.jeecg.modules.im.entity.Muc;
import org.jeecg.modules.im.entity.MucConfig;
import org.jeecg.modules.im.base.xmpp.MessageBean;

import java.util.List;

public interface XMPPService {

    //初始化xmpp系统账号
    void initSystemNo();

    //注册
    boolean registerUser(Integer userId, String password,String nickname);

    //修改密码
    boolean modifyXmppPassword(Integer userId, String oldPwd,String newPwd);

    //发送多条单聊
    boolean sendMulti(MessageBean messageBean, List<String> userIdList);

    //创建群组
    boolean creatMuc(Integer masterUserId,  Muc muc, MucConfig mucConfig, String inviteAccounts);

    //加群
    boolean joinMuc(Integer userId, Integer mucId);
    //更新
    boolean updateMuc(Muc muc,String ownerPwd);

    //后台创建群组
    boolean consoleCreatMuc(Muc muc, MucConfig mucConfig);

    //后台邀请用户进群
    boolean consoleInviteUser(Integer userId, Integer mucId);

    //后台解散群组
    boolean consoleDestroyMuc(Integer mucId);

    //解散群聊
    boolean destroyMuc(Integer userId,Integer mucId);

    //系统管理员发送单聊消息给指定用户，并发送一条群聊消息
    void sendMsgToOneAndMucBySys(MessageBean messageBean);

    //用户发送发送单聊消息给指定用户，并发送一条群聊消息
    void sendMsgToOneAndMuc(MessageBean messageBean);

    //系统号发送单聊消息
    boolean sendMsgBySys(MessageBean messageBean);

    //系统号发送群聊消息
    boolean sendMucMsgBySys(MessageBean messageBean);

    //发送群聊消息
    boolean sendMucMsg(MessageBean messageBean);

    //用户发送给自己
    boolean sendMsgToSelf(MessageBean messageBean);

    //发送给某人
    boolean sendMsgToOne(MessageBean messageBean);

    //添加好友
    boolean addFriend(Integer userId, Integer toUserId);
    //关注用户
    boolean followUser(Integer userId, Integer toUserId);
    //批量移除成员
    boolean kickMembers(Integer mucId,String userIds);
    //批量邀请
    boolean inviteUsers(Integer inviterId, Integer mucId, String userIds);
    //移除管理员
    boolean revokeManagers(Integer mucId,String userIds);
    //添加管理员
    boolean grantManagers(Integer mucId,String userIds);
    //移交群主
    boolean transferMuc(Integer mucId,Integer userId);
    //发送广播消息
    boolean sendBroadcast(MessageBean messageBean);
}
