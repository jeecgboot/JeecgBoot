package org.jeecg.modules.im.xmpp;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.constant.ConstantXmpp;
import org.jeecg.modules.im.base.util.ThreadUtil;
import org.jeecg.modules.im.base.util.UUIDTool;
import org.jeecg.modules.im.base.xmpp.MessageBean;
import org.jeecg.modules.im.config.XMPPConfig;
import org.jeecg.modules.im.entity.Muc;
import org.jeecg.modules.im.entity.MucConfig;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.mq.MQXmppMessageProducer;
import org.jeecg.modules.im.service.MucService;
import org.jeecg.modules.im.service.OfUserService;
import org.jeecg.modules.im.service.UserService;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.filter.StanzaIdFilter;
import org.jivesoftware.smack.packet.*;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.iqregister.packet.Registration;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.ping.PingManager;
import org.jivesoftware.smackx.xdata.form.FillableForm;
import org.jivesoftware.smackx.xdata.form.Form;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.jid.parts.Resourcepart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 连接xmpp服务器有三种方式
 * 纯文本
 * starttls
 * tls/ssl
 * 第二种是纯文本的扩展协议，如果双方都支持加密，则可将纯文本传输升级为加密传输，加密传输和非加密
 */
@Slf4j
@Component
public class XMPPManager {
//    private XMPPTCPConnection connection;

    @Autowired
    private XMPPConfig xmppConfig;

    @Autowired
    private MQXmppMessageProducer mqXmppMessageProducer;
    @Resource
    private OfUserService ofUserService;
    @Resource
    private MucService mucService;
    @Resource
    private UserService userService;

    //系统管理员连接
    private XMPPTCPConnection adminConnection;


//    //获取XMPP连接
//    public XMPPTCPConnection getConnection() {
//        if (connection == null || !connection.isConnected()) {
//            //初始化XMPP连接
//            try {
//                connection = new XMPPTCPConnection(getConfig());
//                connection.connect();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return connection;
//    }

    //获得xmpp配置
    public XMPPTCPConnectionConfiguration getConfig() throws Exception {
//        SSLContext ctx = SSLContext.getInstance("SSL");
//        TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
//        KeyStore tks = KeyStore.getInstance("JKS");
//        tks.load(XMPPManager.class.getResourceAsStream(xmppConfig.getKeystoreFile()), xmppConfig.getKeystorePass().toCharArray());
//        tmf.init(tks);
//        ctx.init(null, tmf.getTrustManagers(), null);
        XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder()
                .setHost(xmppConfig.getHost())
                .setXmppDomain(xmppConfig.getXmppDomain())
                .setPort(xmppConfig.getPort())
//                .setSslContextFactory(() -> ctx)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setResource(xmppConfig.getResource())
                .setCompressionEnabled(true);//压缩节流
//        //信任自签证书，关键
//        if (xmppConfig.isSelfSigned()) {
//            configBuilder.setCustomX509TrustManager(new X509TrustManager() {
//                public X509Certificate[] getAcceptedIssuers() {
//                    return new X509Certificate[0];
//                }
//
//                public void checkClientTrusted(
//                        X509Certificate[] certs, String authType) {
//                }
//
//                public void checkServerTrusted(
//                        X509Certificate[] certs, String authType) {
//                }
//            });
//        }
        return configBuilder.build();
    }

    /**
     * 用户注册
     */
    public boolean register(Integer userId, String password,String nickname) {
        XMPPTCPConnection conn = null;
        try {
            conn = getAdminConnection();
            AccountManager accountManager = AccountManager.getInstance(conn);
            //允许不安全连接执行敏感操作
            accountManager.sensitiveOperationOverInsecureConnection(true);
            Map<String, String> attributes = new HashMap<>();
            if(StringUtils.isNotBlank(nickname)){
                attributes.put("name", nickname);
            }
            accountManager.createAccount(Localpart.from(userId.toString()),password,attributes);
            return true;
        } catch (Exception e) {
            log.info("xmpp注册账号异常：{0}", e);
            return false;
        } finally {
            closeConnection(conn);
        }
    }
    /**
     * 密码修改
     */
    public boolean modifyPassword(Integer userId, String oldPwd,String newPwd) {
        XMPPTCPConnection conn = null;
        try {
            conn = getUserConnection(userId,oldPwd);
            AccountManager accountManager = AccountManager.getInstance(conn);
            accountManager.sensitiveOperationOverInsecureConnection(true);
            accountManager.changePassword(newPwd);
            return true;
        } catch (Exception e) {
            log.info("xmpp修改密码异常：{0}", e);
            return false;
        } finally {
            closeConnection(conn);
        }
    }
    /**
     * 删除账号
     */
    public boolean deleteAccount(Integer userId) {
        XMPPTCPConnection conn = null;
        try {
            conn = getUserConnection(userId,userService.getPassword(userId));
            AccountManager accountManager = AccountManager.getInstance(conn);
            accountManager.deleteAccount();
            return true;
        } catch (Exception e) {
            log.info("xmpp删除账号异常：{0}", e);
            return false;
        } finally {
            closeConnection(conn);
        }
    }

    //注册管理员账号
    public boolean registerAdmin() {
        XMPPTCPConnection conn = getAdminConnection();
        if(conn==null){
            log.info("获取管理员连接失败");
            return false;
        }
        try {
            Map<String, String> attributes = new HashMap<>();
            attributes.put("username", xmppConfig.getAdminUsername());
            attributes.put("password", xmppConfig.getAdminPassword());
            Registration reg = new Registration(attributes);
            reg.setType(IQ.Type.set);
            reg.setTo(conn.getXMPPServiceDomain());
            Stanza nextResultOrThrow = conn.createStanzaCollectorAndSend(new StanzaIdFilter(reg.getStanzaId()), reg).nextResultOrThrow();
            log.info("创建xmpp管理员账号：username={},result={}", xmppConfig.getAdminUsername(), nextResultOrThrow.toString());
            return true;
        } catch (Exception e) {
            log.info("xmpp创建管理号异常：{0}", e);
            return false;
        }
    }


    /**
     * 注册系统账号
     */
    public boolean registerSystemNo(Integer userId, String password,String nickname) throws Exception {
        String username = userId + "@" + getConfig().getXMPPServiceDomain();
        //查询openfire数据库，判断是否存在
        if (ofUserService.findByUsername(username) != null) {
            return true;
        }
        return register(userId, password,nickname);
    }

    //延迟关闭
    private void closeConnection(XMPPTCPConnection conn) {
        ThreadUtil.executeInThread(obj -> {
            try {
                if (null != conn) {
                    conn.disconnect();
                }
            } catch (Exception e) {
                log.info("xmpp关闭连接异常：{0}", e);
            }
        }, 15);
    }

    //获取管理员账连接
    public synchronized XMPPTCPConnection getAdminConnection() {
        //创建
        if (null == adminConnection) {
            try {
                adminConnection = new XMPPTCPConnection(getConfig());
                adminConnection.connect();
                try {
                    adminConnection.login(xmppConfig.getAdminUsername(), xmppConfig.getAdminPassword());
                } catch (XMPPException e) {
                    //登陆失败 可能是系统 账号不存在  重新注册
                    if(registerAdmin())
                        adminConnection.login(xmppConfig.getAdminUsername(), xmppConfig.getAdminPassword());
                }
                if(adminConnection.isConnected()){
                    Presence presence = PresenceBuilder.buildPresence()
                            .setMode(Presence.Mode.available)
                            .build();
                    adminConnection.sendStanza(presence);
                    adminConnection.addConnectionListener(new MyConnectionListener(adminConnection));
                }
            } catch (Exception e) {
                log.error("获取管理员xmpp连接异常：{0}", e);
                adminConnection = null;
            }
            return adminConnection;
        }
        //重连
        if (!adminConnection.isConnected()) {
            try {
                adminConnection.connect();
                adminConnection.login(xmppConfig.getAdminUsername(), xmppConfig.getAdminPassword());
            } catch (Exception e) {
                log.error("获取管理员xmpp连接异常：{0}", e);
                adminConnection = null;
            }
        }
        return adminConnection;
    }

    //获取指定用户的连接
    public XMPPTCPConnection getUserConnection(Integer userId, String password) {
        try {
            return UserConnectionManager.getConnection(userId.toString(),password,getConfig());
        } catch (Exception e) {
            log.error("获取用户xmpp连接异常：{},password={}", e,password);
        }
        return null;
    }

    /**
     * 系统管理员发送单聊消息
     */
    public boolean sendMsgBySys(MessageBean messageBean) {
        try {
            messageBean.setMsgType(MessageBean.MsgType.chat.getCode());
            messageBean.setStanzaId(UUIDTool.getUUID());

            messageBean.setUserId(ConstantXmpp.SystemNo.system.getAccount());
            messageBean.setUserName(ConstantXmpp.SystemNo.system.getName());
            Result<String> sendResult = mqXmppMessageProducer.send(messageBean);
            if (!sendResult.isSuccess()) {
                log.error("消息发送失败:{}", messageBean);
            } else {
                log.info("消息发送成功:{}", messageBean);
            }
            return sendResult.isSuccess();
        } catch (Exception e) {
            log.info("xmpp发送单聊消息异常：{0}", e);
            return false;
        }
    }
    //用户发送给自己
    public boolean sendToSelf(MessageBean messageBean) {
        try {
            messageBean.setToUserId(messageBean.getUserId());
            messageBean.setMsgType(MessageBean.MsgType.chat.getCode());
            messageBean.setStanzaId(UUIDTool.getUUID());
            Result<String> sendResult = mqXmppMessageProducer.send(messageBean);
            if (!sendResult.isSuccess()) {
                log.error("消息发送失败:{}", messageBean);
            } else {
                log.info("消息发送成功:{}", messageBean);
            }
            return sendResult.isSuccess();
        } catch (Exception e) {
            log.info("xmpp给自己发送消息异常：{0}", e);
            return false;
        }
    }
    //发给指定用户
    public boolean sendMsgToOne(MessageBean messageBean) {
        try {
            messageBean.setMsgType(MessageBean.MsgType.chat.getCode());
            messageBean.setStanzaId(UUIDTool.getUUID());
            Result<String> sendResult = mqXmppMessageProducer.send(messageBean);
            if (!sendResult.isSuccess()) {
                log.error("消息发送失败:{}", messageBean);
            } else {
                log.info("消息发送成功:{}", messageBean);
            }
            return sendResult.isSuccess();
        } catch (Exception e) {
            log.info("xmpp给某人发送消息异常：{0}", e);
            return false;
        }
    }
    //发送广播消息
    public boolean sendBroadcast(MessageBean messageBean) {
        try {
            messageBean.setMsgType(MessageBean.MsgType.chat.getCode());
            messageBean.setStanzaId(UUIDTool.getUUID());
            Result<String> sendResult = mqXmppMessageProducer.send(messageBean);
            if (!sendResult.isSuccess()) {
                log.error("消息发送失败:{}", messageBean);
            } else {
                log.info("消息发送成功:{}", messageBean);
            }
            return sendResult.isSuccess();
        } catch (Exception e) {
            log.info("xmpp给某人发送消息异常：{0}", e);
            return false;
        }
    }

    //系统管理员发送群聊消息
    public boolean sendMucMsgBySys(MessageBean messageBean) {
        try {
            messageBean.setMsgType(MessageBean.MsgType.groupChat.getCode());
            messageBean.setStanzaId(UUIDTool.getUUID());

            messageBean.setUserId(ConstantXmpp.SystemNo.system.getAccount());
            messageBean.setUserName(ConstantXmpp.SystemNo.system.getName());
            Result<String> sendResult = mqXmppMessageProducer.send(messageBean);
            if (!sendResult.isSuccess()) {
                log.error("消息发送失败:{}", messageBean);
            } else {
                log.info("消息发送成功:{}", messageBean);
            }
            return sendResult.isSuccess();
        } catch (Exception e) {
            log.info("xmpp发送群消息异常：{0}", e);
            return false;
        }
    }
    //发送群消息
    public boolean sendMucMsg(MessageBean messageBean) {
        try {
            messageBean.setMsgType(MessageBean.MsgType.groupChat.getCode());
            messageBean.setStanzaId(UUIDTool.getUUID());

            Result<String> sendResult = mqXmppMessageProducer.send(messageBean);
            if (!sendResult.isSuccess()) {
                log.error("消息发送失败:{}", messageBean);
            } else {
                log.info("消息发送成功:{}", messageBean);
            }
            return sendResult.isSuccess();
        } catch (Exception e) {
            log.info("xmpp发送群消息异常：{0}", e);
            return false;
        }
    }


    public String getMucChatServiceName(XMPPTCPConnection conn) {
        return "@"+xmppConfig.getMucPrefix()+"." + conn.getXMPPServiceDomain();
    }
    public String getServiceName(XMPPTCPConnection conn) {
        return "@" + conn.getXMPPServiceDomain();
    }

    public String getBareJid(String userId,XMPPTCPConnection conn) {
        return userId+"@" + conn.getXMPPServiceDomain();
    }

    /**
     * 创建群组
     *
     * @param masterUserId   群主账号
     *                 房间名称|muc#roomconfig_roomname
     *                 描述|muc#roomconfig_roomdesc
     *                 允许占有者更改主题|muc#roomconfig_changesubject
     *                 最大房间占有者人数|muc#roomconfig_maxusers
     *                 其 Presence 是 Broadcast 的角色|muc#roomconfig_presencebroadcast
     *                 列出目录中的房间|muc#roomconfig_publicroom
     *                 房间是持久的|muc#roomconfig_persistentroom
     *                 房间是适度的|muc#roomconfig_moderatedroom
     *                 房间仅对成员开放|muc#roomconfig_membersonly
     *                 允许占有者邀请其他人|muc#roomconfig_allowinvites
     *                 需要密码才能进入房间|muc#roomconfig_passwordprotectedroom
     *                 密码|muc#roomconfig_roomsecret
     *                 能够发现占有者真实 JID 的角色|muc#roomconfig_whois
     *                 登录房间对话|muc#roomconfig_enablelogging
     *                 仅允许注册的昵称登录|x-muc#roomconfig_reservednick
     *                 允许使用者修改昵称|x-muc#roomconfig_canchangenick
     *                 允许用户注册房间|x-muc#roomconfig_registration
     *                 房间管理员|muc#roomconfig_roomadmins
     *                 房间拥有者|muc#roomconfig_roomowners
     */
    public boolean createMuc(Integer masterUserId, Muc room, MucConfig mucConfig, String userIds) {
        XMPPTCPConnection conn = null;
        try {
            conn = getUserConnection(masterUserId, userService.getPassword(masterUserId));
            // 创建聊天室
            MultiUserChat muc = MultiUserChatManager.getInstanceFor(conn).getMultiUserChat(JidCreate.entityBareFrom(room.getId() + getMucChatServiceName(conn)));
            muc.create(Resourcepart.from(conn.getUser().getLocalpart().toString()));
            // 获得聊天室的配置表单
            Form form = muc.getConfigurationForm();
            // 根据原始表单创建一个要提交的新表单。
            FillableForm submitForm = form.getFillableForm();
//            // 设置聊天室的拥有者
            List owners = new ArrayList();
            owners.add(conn.getUser().asBareJid());
            submitForm.setAnswer("muc#roomconfig_roomowners", owners);

            // 设置聊天室的名字
            submitForm.setAnswer("muc#roomconfig_roomname", room.getName());
            // 设置聊天室描述
            if (StringUtils.isNotBlank(room.getInfo())) {
                submitForm.setAnswer("muc#roomconfig_roomdesc", room.getInfo());
            }
//            if(StringUtils.isNotBlank(room.getSubject())){
//                submitForm.setAnswer("muc#roominfo_subject", room.getSubject());
//            }
            // 登录房间对话
            submitForm.setAnswer("muc#roomconfig_enablelogging", true);
            // 允许修改主题
            // submitForm.setAnswer("muc#roomconfig_changesubject", true);
            // 允许占有者邀请其他人
            submitForm.setAnswer("muc#roomconfig_allowinvites", true);
            //最大人数
            submitForm.setAnswer("muc#roomconfig_maxusers", mucConfig.getMaxMemberCount());
//             公开的，允许被搜索到,任何人都可以发言
             submitForm.setAnswer("muc#roomconfig_publicroom", mucConfig.getIsPublic());
            // 设置聊天室是持久聊天室，即将要被保存下来
            submitForm.setAnswer("muc#roomconfig_persistentroom", true);

            //是否主持腾出空间(加了这个默认游客进去不能发言)
            // submitForm.setAnswer("muc#roomconfig_moderatedroom", true);
//             房间仅对成员开放
             submitForm.setAnswer("muc#roomconfig_membersonly", true);
            // 不需要密码
            // submitForm.setAnswer("muc#roomconfig_passwordprotectedroom",
            // false);
            // 房间密码
            // submitForm.setAnswer("muc#roomconfig_roomsecret", "111");
            // 允许主持 能够发现真实 JID
            // List<String> whois = new ArrayList<String>();
            // whois.add("anyone");
            // submitForm.setAnswer("muc#roomconfig_whois", whois);

            // 管理员
            // <field var='muc#roomconfig_roomadmins'>
            // <value>wiccarocks@shakespeare.lit<alue>
            // <value>hecate@shakespeare.lit<alue>
            // </field>

            // 仅允许注册的昵称登录
            // submitForm.setAnswer("x-muc#roomconfig_reservednick", true);
            // 允许使用者修改昵称
            // submitForm.setAnswer("x-muc#roomconfig_canchangenick", false);
            // 允许用户注册房间
            // submitForm.setAnswer("x-muc#roomconfig_registration", false);
            // 发送已完成的表单（有默认值）到服务器来配置聊天室
            muc.sendConfigurationForm(submitForm);
            log.info("创建群聊成功");
//            //发送邀请
            if(inviteUsers(masterUserId,room.getId(), userIds)){
                log.info("邀请用户进群成功");
            }
            return true;
        } catch (Exception e) {
            log.info("创建群聊异常：{0}", e);
            return false;
        } finally {
            closeConnection(conn);
        }
    }
    //控制台创建群聊
    public boolean consoleCreateMuc(Muc room, MucConfig mucConfig) {
        XMPPTCPConnection conn = getAdminConnection();
        if(conn==null){
            log.info("获取管理员连接失败");
            return false;
        }
        try {
            String roomJid = room.getId() + getMucChatServiceName(conn);
            // 创建聊天室
            MultiUserChat muc = MultiUserChatManager.getInstanceFor(conn).getMultiUserChat(JidCreate.entityBareFrom(room.getId() + getMucChatServiceName(conn)));
            muc.create(Resourcepart.from(conn.getUser().getLocalpart().toString()));
            // 获得聊天室的配置表单
            Form form = muc.getConfigurationForm();
            // 根据原始表单创建一个要提交的新表单。
            FillableForm submitForm = form.getFillableForm();
            // 设置聊天室的新拥有者
            List owners = new ArrayList();
            owners.add(conn.getUser().asBareJid());
            submitForm.setAnswer("muc#roomconfig_roomowners", owners);
            // 设置聊天室的名字
            submitForm.setAnswer("muc#roomconfig_roomname", room.getName());
            // 设置聊天室描述
            if (StringUtils.isNotBlank(room.getInfo())) {
                submitForm.setAnswer("muc#roomconfig_roomdesc", room.getInfo());
            }
//            if(StringUtils.isNotBlank(room.getSubject())){
//                submitForm.setAnswer("muc#roominfo_subject", room.getSubject());
//            }
            // 登录房间对话
            submitForm.setAnswer("muc#roomconfig_enablelogging", true);
            // 允许修改主题
            // submitForm.setAnswer("muc#roomconfig_changesubject", true);
            // 允许占有者邀请其他人
            submitForm.setAnswer("muc#roomconfig_allowinvites", true);
            //最大人数
            submitForm.setAnswer("muc#roomconfig_maxusers", mucConfig.getMaxMemberCount());
            // 公开的，允许被搜索到
            submitForm.setAnswer("muc#roomconfig_publicroom", mucConfig.getIsPublic());
            // 设置聊天室是持久聊天室，即将要被保存下来
            submitForm.setAnswer("muc#roomconfig_persistentroom", true);

//            if (password != null && password.length() != 0) {
//                // 进入是否需要密码
//                submitForm.setAnswer("muc#roomconfig_passwordprotectedroom", true);
//                // 设置进入密码
//                submitForm.setAnswer("muc#roomconfig_roomsecret", password);
//            }

            //是否主持腾出空间(加了这个默认游客进去不能发言)
            // submitForm.setAnswer("muc#roomconfig_moderatedroom", true);
            // 房间仅对成员开放
             submitForm.setAnswer("muc#roomconfig_membersonly", true);
            // 不需要密码
            // submitForm.setAnswer("muc#roomconfig_passwordprotectedroom",
            // false);
            // 房间密码
            // submitForm.setAnswer("muc#roomconfig_roomsecret", "111");
            // 允许主持 能够发现真实 JID
            // List<String> whois = new ArrayList<String>();
            // whois.add("anyone");
            // submitForm.setAnswer("muc#roomconfig_whois", whois);

            // 管理员
            // <field var='muc#roomconfig_roomadmins'>
            // <value>wiccarocks@shakespeare.lit<alue>
            // <value>hecate@shakespeare.lit<alue>
            // </field>

            // 仅允许注册的昵称登录
            // submitForm.setAnswer("x-muc#roomconfig_reservednick", true);
            // 允许使用者修改昵称
            // submitForm.setAnswer("x-muc#roomconfig_canchangenick", false);
            // 允许用户注册房间
            // submitForm.setAnswer("x-muc#roomconfig_registration", false);
            // 发送已完成的表单（有默认值）到服务器来配置聊天室
            muc.sendConfigurationForm(submitForm);
            log.info("后台创建群组成功");
            return true;
        } catch (Exception e) {
            log.info("后台创建群组异常：{0}", e);
            return false;
        } finally {
            closeConnection(conn);
        }
    }
    //更新群组
    public boolean updateMuc(Muc room,String ownerPwd) {
        XMPPTCPConnection conn = getUserConnection(room.getUserId(),ownerPwd);
        if(conn==null){
            log.info("获取群主连接失败");
            return false;
        }
        try {
            MultiUserChat muc = MultiUserChatManager.getInstanceFor(conn).getMultiUserChat(JidCreate.entityBareFrom(room.getId() + getMucChatServiceName(conn)));
            // 加入聊天室
            muc.join(Resourcepart.from(conn.getUser().getLocalpart().toString()));
            // 获得聊天室的配置表单
            FillableForm submitForm = muc.getConfigurationForm().getFillableForm();

//            // 设置群主
//            List owners = new ArrayList();
//            owners.add(conn.getUser().asBareJid());
//            submitForm.setAnswer("muc#roomconfig_roomowners", owners);

//            // 设置管理员
//            List admins = new ArrayList();
//            admins.add(conn.getUser().asBareJid());
//            submitForm.setAnswer("muc#roomconfig_roomadmins", owners);


            submitForm.setAnswer("muc#roomconfig_roomname", room.getName());
            // 设置聊天室描述
            if (StringUtils.isNotBlank(room.getInfo())) {
                submitForm.setAnswer("muc#roomconfig_roomdesc", room.getInfo());
            }
            muc.sendConfigurationForm(submitForm);
            log.info("更新群组成功");
            return true;
        } catch (Exception e) {
            log.info("更新群组异常：{0}", e);
            return false;
        } finally {
            closeConnection(conn);
        }
    }
//    <message to='coven@chat.shakespeare.lit'>
//  <x xmlns='http://jabber.org/protocol/muc#user'>
//    <invite to='hecate@shakespeare.lit'/>
//    <invite to='bard@shakespeare.lit'/>
//  </x>
//</message>
    public boolean inviteUsers(Integer inviterId,Integer roomId,String userIds){
        XMPPTCPConnection conn = null;
        try {
            User inviter = userService.getById(inviterId);
            conn = getUserConnection(inviter.getId(), inviter.getPassword());

            MultiUserChat muc = MultiUserChatManager.getInstanceFor(conn).getMultiUserChat(JidCreate.entityBareFrom(roomId + getMucChatServiceName(conn)));
            if(!StringUtils.isBlank(userIds)){
                for (String username : StringUtils.split(userIds, ",")) {
                    String userId = username+"@"+getConfig().getXMPPServiceDomain();
                    muc.invite(JidCreate.entityBareFrom(userId), "邀请入群");
                }
            }
            return true;
        } catch (Exception e) {
            log.info("xmpp邀请用户进群异常：{0}",e);
            return false;
        } finally {
            closeConnection(conn);
        }
    }
    //批量移除群成员
    public boolean kickMembers(Integer roomId,String userIds){
        XMPPTCPConnection conn = null;
        try {
            Muc room = mucService.getById(roomId);
            User master = userService.getById(room.getUserId());
            conn = getUserConnection(master.getId(), master.getPassword());
            MultiUserChat muc = MultiUserChatManager.getInstanceFor(conn).getMultiUserChat(JidCreate.entityBareFrom(roomId + getMucChatServiceName(conn)));
            if(!StringUtils.isBlank(userIds)){
                for (String userId : StringUtils.split(userIds, ",")) {
                    muc.kickParticipant(JidCreate.entityBareFrom(userId+"@"+getConfig().getXMPPServiceDomain()).getResourceOrEmpty(), "移除群聊");
                }
            }
            return true;
        } catch (Exception e) {
            log.info("xmpp移除群成员异常：{0}",e);
            return false;
        } finally {
            closeConnection(conn);
        }
    }
    //批量撤销管理员
    public boolean revokeManagers(Integer roomId,String userIds){
        XMPPTCPConnection conn = null;
        try {
            Muc room = mucService.getById(roomId);
            User master = userService.getById(room.getUserId());
            conn = getUserConnection(master.getId(), master.getPassword());
            MultiUserChat muc = MultiUserChatManager.getInstanceFor(conn).getMultiUserChat(JidCreate.entityBareFrom(roomId + getMucChatServiceName(conn)));
            if(!StringUtils.isBlank(userIds)){
                muc.join(Resourcepart.from(conn.getUser().getLocalpart().toString()));
                for (String userId : StringUtils.split(userIds, ",")) {
                    muc.revokeAdmin(JidCreate.entityBareFrom(userId+"@"+getConfig().getXMPPServiceDomain()));
                }
            }
            return true;
        } catch (Exception e) {
            log.info("xmpp批量撤销管理员异常：{0}",e);
            return false;
        } finally {
            closeConnection(conn);
        }
    }
    //批量设置管理员
    public boolean grantManagers(Integer roomId,String userIds){
        XMPPTCPConnection conn = null;
        try {
            Muc room = mucService.getById(roomId);
            User master = userService.getById(room.getUserId());
            conn = getUserConnection(master.getId(), master.getPassword());
            MultiUserChat muc = MultiUserChatManager.getInstanceFor(conn).getMultiUserChat(JidCreate.entityBareFrom(roomId + getMucChatServiceName(conn)));
            if(!StringUtils.isBlank(userIds)){
                muc.join(Resourcepart.from(conn.getUser().getLocalpart().toString()));
                for (String userId : StringUtils.split(userIds, ",")) {
                    muc.grantAdmin(JidCreate.entityBareFrom(userId+"@"+getConfig().getXMPPServiceDomain()));
                }
            }
            return true;
        } catch (Exception e) {
            log.info("xmpp批授权管理员异常：{0}",e);
            return false;
        } finally {
            closeConnection(conn);
        }
    }
    //移交群主
    public boolean transferMuc(Integer roomId,Integer userId){
        XMPPTCPConnection conn = null;
        try {
            Muc room = mucService.getById(roomId);
            User master = userService.getById(room.getUserId());
            conn = getUserConnection(master.getId(), master.getPassword());
            MultiUserChat muc = MultiUserChatManager.getInstanceFor(conn).getMultiUserChat(JidCreate.entityBareFrom(roomId + getMucChatServiceName(conn)));
            muc.grantOwnership(JidCreate.entityBareFrom(userId+"@"+getConfig().getXMPPServiceDomain()));
            muc.revokeOwnership(conn.getUser());
            return true;
        } catch (Exception e) {
            log.info("xmpp移交群主异常：{0}",e);
            return false;
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * 解散群组
     * @param mucId
     */
    public boolean consoleDestroyMuc(Integer mucId) {
        XMPPTCPConnection conn = getAdminConnection();
        if(conn==null){
            log.info("获取管理员连接失败");
            return false;
        }
        try {

            MultiUserChat muc = MultiUserChatManager.getInstanceFor(conn).getMultiUserChat(JidCreate.entityBareFrom(mucId + getMucChatServiceName(conn)));
            muc.join(Resourcepart.from(conn.getUser().getLocalpart().toString()));
            muc.destroy("解散群组", conn.getUser().asEntityBareJid());
            return true;
        } catch (Exception e) {
            if(e.getMessage().contains("There is no such muc")){
                log.info("xmpp解散群组错误：{}", mucId+"群不存在");
                return true;
            }
            log.info("xmpp解散群组异常：{0}", e);
            return false;
        } finally {
            closeConnection(conn);
        }
    }
    public boolean destroyMuc(Integer userId,Integer mucId) {
        XMPPTCPConnection conn = null;
        try {
            conn = getUserConnection(userId,userService.getPassword(userId));
            MultiUserChat muc = MultiUserChatManager.getInstanceFor(conn).getMultiUserChat(JidCreate.entityBareFrom(mucId + getMucChatServiceName(conn)));
            muc.join(Resourcepart.from(conn.getUser().getLocalpart().toString()));
            muc.destroy("解散群组", JidCreate.entityBareFrom(conn.getUser()));
            return true;
        } catch (Exception e) {
            if(e.getMessage().contains("There is no such muc")){
                log.info("xmpp解散群组错误：{}", mucId+"群不存在");
                return true;
            }
            log.info("xmpp解散群组异常：{0}", e);
            return false;
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * 用户自己加入群
     */
    public boolean joinMuc(Integer userId, Integer mucId) {
        XMPPTCPConnection conn = null;
        try {
            String password = userService.getPassword(userId);
            conn = getUserConnection(userId, password);
            MultiUserChat muc = MultiUserChatManager.getInstanceFor(conn).getMultiUserChat(JidCreate.entityBareFrom(mucId + getMucChatServiceName(conn)));
            if(!muc.isJoined()) {
                muc.join(Resourcepart.from(userId.toString()), password);
            }
            return true;
        } catch (Exception e) {
            log.info("xmpp加群异常：{0}", e);
            return false;
        } finally {
            closeConnection(conn);
        }
    }


    public boolean inviteJoinMucBySystem(Integer userId,Integer mucId) {
        XMPPTCPConnection conn = getAdminConnection();
        if(conn==null){
            log.info("获取管理员连接失败");
            return false;
        }
        try {
            MultiUserChat muc = MultiUserChatManager.getInstanceFor(conn).getMultiUserChat(JidCreate.entityBareFrom(mucId + getMucChatServiceName(conn)));
            String userJid = userId + "@" + getConfig().getXMPPServiceDomain();
            muc.invite(JidCreate.entityBareFrom(userJid), "邀请入群");
            return true;
        } catch (Exception e) {
            log.info("xmpp管理员邀请成员异常：{0}", e);
            return false;
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * 加好友，双方临时修改订阅接受模式为直接通过
     * A请求订阅B <presence from="A" to="B" type="subscribe"/>
     * B接受 <presence from="B" to="A" type="subscribed"/>
     * B请求订阅A <presence from="B" to="A" type="subscribe"/>
     * A接受 <presence from="A" to="B" type="subscribed"/>
     */
    public boolean addFriend(Integer userId,Integer toUserId) {
        XMPPTCPConnection userConn = getUserConnection(userId,userService.getPassword(userId));
        XMPPTCPConnection toUserConn = getUserConnection(toUserId,userService.getPassword(toUserId));
        if(userConn==null||toUserConn==null){
            log.info("获取用户连接失败");
            return false;
        }
        try {
            Roster roster1 = Roster.getInstanceFor(userConn);
            roster1.setSubscriptionMode(Roster.SubscriptionMode.accept_all);

            Roster roster2 = Roster.getInstanceFor(toUserConn);
            roster2.setSubscriptionMode(Roster.SubscriptionMode.accept_all);

            roster1.createItemAndRequestSubscription(toUserConn.getUser().asBareJid(), toUserId.toString(),null);
            roster2.createItemAndRequestSubscription(userConn.getUser().asBareJid(), userId.toString(),null);

//            roster1.setSubscriptionMode(Roster.SubscriptionMode.manual);
//            roster2.setSubscriptionMode(Roster.SubscriptionMode.manual);

        }catch (Exception e) {
            log.info("互相添加好友异常：{0}", e);
            return false;
        } finally {
            closeConnection(userConn);
            closeConnection(toUserConn);
        }
        return false;
    }
    public boolean followUser(Integer userId,Integer toUserId) {
        XMPPTCPConnection userConn = getUserConnection(userId,userService.getPassword(userId));
        XMPPTCPConnection toUserConn = getUserConnection(toUserId,userService.getPassword(toUserId));
        if(userConn==null||toUserConn==null){
            log.info("获取用户连接失败");
            return false;
        }
        try {
            Roster roster1 = Roster.getInstanceFor(userConn);
            roster1.setSubscriptionMode(Roster.SubscriptionMode.accept_all);

            Roster roster2 = Roster.getInstanceFor(toUserConn);
            roster2.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
            roster1.createItemAndRequestSubscription(toUserConn.getUser().asBareJid(), toUserId.toString(),new String[]{"follow"});

        }catch (Exception e) {
            log.info("关注用户异常：", e);
            return false;
        } finally {
            closeConnection(userConn);
            closeConnection(toUserConn);
        }
        return false;
    }


    public static class MyConnectionListener implements ConnectionListener {

        private XMPPTCPConnection conn;

        public MyConnectionListener(XMPPTCPConnection conn) {
            this.conn = conn;
            if (conn.isAuthenticated()) {
                PingManager pingManager = PingManager.getInstanceFor(conn);
                pingManager.registerPingFailedListener(() -> log.info("xmpp ping pingFailed=====>"));
            }
        }

        @Override
        public void connectionClosed() {
            log.info((null != conn ? conn.getUser() : "") + " ====> connectionClosed");
            conn = null;

        }

        @Override
        public void connectionClosedOnError(Exception e) {
            log.info((null != conn ? conn.getUser() : "") + " ====> connectionClosedOnError");

            if (null != conn)
                conn.disconnect();
            conn = null;

        }

        @Override
        public void connected(XMPPConnection c) {
            log.info(c.getUser() + " ====> connected");
        }
    }
}