package org.jeecg.modules.im.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.im.base.util.PasswordEncoder;
import org.jeecg.modules.im.base.util.ToolPassword;
import org.jeecg.modules.im.entity.MucConfig;
import org.jeecg.modules.im.base.xmpp.MessageBean;
import org.jeecg.modules.im.base.constant.ConstantXmpp;
import org.jeecg.modules.im.base.exception.BusinessException;
import org.jeecg.modules.im.base.util.UUIDTool;
import org.jeecg.modules.im.entity.Muc;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.service.UserService;
import org.jeecg.modules.im.service.XMPPService;
import org.jeecg.modules.im.xmpp.XMPPManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class XMPPServiceImpl implements XMPPService {
    @Autowired
    private XMPPManager xmppManager;
    @Resource
    private UserService userService;
    /**
     * 初始化xmpp系统账号
     */
    public void initSystemNo() {
        try {
            User user;
            for (ConstantXmpp.SystemNo sys : ConstantXmpp.SystemNo.values()) {
                //保存用户
                user = userService.findByAccount(sys.getAccount().toString());
                if(user!=null){
                    continue;
                }
                user = new User();
                user.setId(sys.getAccount());
                user.setAccount(sys.getAccount().toString());
                user.setNickname(sys.getName());
                user.setSalt(PasswordEncoder.createSalt(32));
                user.setPassword(ToolPassword.getEncPassword(user.getSalt(), DigestUtils.md5Hex(StringUtils.reverse(sys.getAccount().toString()))));
                user.setType(User.Type.sysAccount.getCode());
                user.setQrCode(UUIDTool.getUUID());
                user.setResource(User.Resource.CONSOLE_CREATE.getCode());
                user.setTsCreate(new Date().getTime());
                if(!userService.save(user)){
                    throw new BusinessException("创建系统账号失败");
                }

                if(!xmppManager.registerSystemNo(user.getId(), user.getPassword(),user.getNickname())){
                    throw new BusinessException("xmpp创建系统账号失败");
                }
            }
        } catch (Exception e) {
            log.error("初始化xmpp系统账号异常：", e);
        }
    }


    @Override
    public boolean registerUser(Integer userId,String password,String nickname) {
        return xmppManager.register(userId,password,nickname);
    }
    @Override
    public boolean modifyXmppPassword(Integer userId,String oldPwd,String newPwd) {
        return xmppManager.modifyPassword(userId,oldPwd,newPwd);
    }

    @Override
    public boolean sendMulti(MessageBean messageBean, List<String> userIdList) {
        return false;
    }

    @Override
    public boolean creatMuc(Integer masterUserId, Muc muc, MucConfig mucConfig, String inviteAccounts) {
        return xmppManager.createMuc(masterUserId, muc, mucConfig,inviteAccounts);
    }

    @Override
    public boolean joinMuc(Integer userId, Integer mucId) {
        return xmppManager.joinMuc(userId,mucId);
    }
    @Override
    public boolean updateMuc(Muc muc,String ownerPwd) {
        return xmppManager.updateMuc(muc,ownerPwd);
    }

    @Override
    public boolean consoleCreatMuc(Muc muc, MucConfig mucConfig) {
        return xmppManager.consoleCreateMuc(muc, mucConfig);
    }

    @Override
    public boolean consoleInviteUser(Integer userId,Integer mucId) {
        return xmppManager.inviteJoinMucBySystem(userId,mucId);
    }

    @Override
    public boolean consoleDestroyMuc(Integer mucId) {
        return xmppManager.consoleDestroyMuc(mucId);
    }
    @Override
    public boolean destroyMuc(Integer userId,Integer mucId) {
        return xmppManager.destroyMuc(userId,mucId);
    }

    //管理员发送单聊和群聊
    @Override
    public void sendMsgToOneAndMucBySys(MessageBean messageBean) {
        // 发送单聊
        sendMsgBySys(messageBean);
        //发送群聊到群组
        sendMucMsgBySys(messageBean);
    }
    //用户发送单聊和群聊
    @Override
    public void sendMsgToOneAndMuc(MessageBean messageBean) {
        // 发送单聊
        sendMsgToOne(messageBean);
        //发送群聊到群组
        sendMucMsg(messageBean);
    }

    @Override
    public boolean sendMucMsgBySys(MessageBean messageBean) {
        return xmppManager.sendMucMsgBySys(messageBean);
    }

    @Override
    public boolean sendMucMsg(MessageBean messageBean) {
        return xmppManager.sendMucMsg(messageBean);
    }
    //
    @Override
    public boolean sendMsgBySys(MessageBean messageBean) {
        return xmppManager.sendMsgBySys(messageBean);
    }
    //发送消息给自己
    @Override
    public boolean sendMsgToSelf(MessageBean messageBean) {
        return xmppManager.sendToSelf(messageBean);
    }

    //发给某人
    @Override
    public boolean sendMsgToOne(MessageBean messageBean) {
        return xmppManager.sendMsgToOne(messageBean);
    }
    //发送广播
    @Override
    public boolean sendBroadcast(MessageBean messageBean) {
        return xmppManager.sendBroadcast(messageBean);
    }

    @Override
    public boolean addFriend(Integer userId,Integer toUserId) {
        return xmppManager.addFriend(userId,toUserId);
    }
    @Override
    public boolean followUser(Integer userId,Integer toUserId) {
        return xmppManager.followUser(userId,toUserId);
    }

    @Override
    public boolean kickMembers(Integer mucId, String userIds) {
        return xmppManager.kickMembers(mucId,userIds);
    }
    @Override
    public boolean inviteUsers(Integer inviterId,Integer mucId, String userIds) {
        return xmppManager.inviteUsers(inviterId,mucId,userIds);
    }

    @Override
    public boolean revokeManagers(Integer mucId, String userIds) {
        return xmppManager.revokeManagers(mucId,userIds);
    }

    @Override
    public boolean grantManagers(Integer mucId, String userIds) {
        return xmppManager.grantManagers(mucId,userIds);
    }

    @Override
    public boolean transferMuc(Integer mucId, Integer userId) {
        return xmppManager.transferMuc(mucId,userId);
    }
}
