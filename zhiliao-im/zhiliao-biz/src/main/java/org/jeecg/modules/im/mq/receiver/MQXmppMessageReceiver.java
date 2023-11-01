package org.jeecg.modules.im.mq.receiver;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.boot.starter.rabbitmq.core.BaseRabbiMqHandler;
import org.jeecg.boot.starter.rabbitmq.listenter.MqListener;
import org.jeecg.common.annotation.RabbitComponent;
import org.jeecg.common.base.BaseMap;
import org.jeecg.modules.im.base.constant.ConstantMQ;
import org.jeecg.modules.im.base.util.UUIDTool;
import org.jeecg.modules.im.config.XMPPConfig;
import org.jeecg.modules.im.entity.Muc;
import org.jeecg.modules.im.entity.MucMember;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.service.MucMemberService;
import org.jeecg.modules.im.service.MucService;
import org.jeecg.modules.im.service.UserService;
import org.jeecg.modules.im.base.xmpp.MessageBean;
import org.jeecg.modules.im.xmpp.XMPPManager;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.StanzaBuilder;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.ping.PingFailedListener;
import org.jivesoftware.smackx.ping.PingManager;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * xmpp消息消费者
 * （@RabbitListener声明类上，一个类只能监听一个队列）
 */
@Slf4j
@RabbitListener(queues = ConstantMQ.XMPP_MESSAGE)
@RabbitComponent(value = "mqXmppMessageReceiver")
public class MQXmppMessageReceiver extends BaseRabbiMqHandler<BaseMap> {
    private static final Gson gson = new Gson();
    @Autowired
    private XMPPManager xmppManager;
    @Autowired
    private XMPPConfig xmppConfig;
    @Resource
    private UserService userService;
    @Resource
    private MucService mucService;
    @Resource
    private MucMemberService mucMemberService;
    //xmpp管理账号连接集合
    private Map<String, XMPPTCPConnection> connMap = new ConcurrentHashMap<>();

    @RabbitHandler
    public void onMessage(BaseMap baseMap, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        super.onMessage(baseMap, deliveryTag, channel, new MqListener<BaseMap>() {
            @Override
            public void handler(BaseMap map, Channel channel) {
                try {
                    //业务处理
                    String msg = map.get("msg");
                    log.info("xmpp消息,msg= " + msg);
                    MessageBean messageBean = gson.fromJson(msg, MessageBean.class);
                    log.info(gson.toJson(messageBean));
                    if(messageBean.getMsgType()==MessageBean.MsgType.chat.getCode()){
                        sendChat(messageBean);
                    }else if(messageBean.getMsgType()==MessageBean.MsgType.groupChat.getCode()){
                        sendGroupChat(messageBean);
                    }else if(messageBean.getMsgType()==MessageBean.MsgType.broadcast.getCode()){
                        sendBroadcast(messageBean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //是否需要将消息放回队列？
                    log.error("消费xmpp消息异常：", e);
                }
            }
        });
    }

//    <message type="chat" xmlns="jabber:client" from="100@songguoim/Smack" to="1446305801697705985@songguoim" id="umLih-65">
//        <body>{"fileSize":0,"fromUserId":"100","fromUserName":"系统管理员","msgType":83,"stanzaId":"2702993a57c845878381d3c639035ee5","timeSend":1634982356,"toUserId":"1446305801697705985","toUserName":"junko","type":0}</body>
//    </message>
    //单聊消息
    private void sendChat(MessageBean messageBean) throws Exception {
        XMPPTCPConnection conn;
        Integer fromUserId = messageBean.getUserId();
//        if(fromUserId>= ConstantXmpp.sysAccountBegin&&fromUserId<=ConstantXmpp.sysAccountEnd){
//            conn = xmppManager.getSystemNoConnection(fromUserId);
//        }else{
            conn = xmppManager.getUserConnection(fromUserId,userService.getPassword(fromUserId));
//        }
        String toJid = messageBean.getToUserId()+xmppManager.getServiceName(conn);
        Message message = StanzaBuilder.buildMessage(UUIDTool.getUUID())
                .setBody(JSONObject.toJSONString(messageBean))
                .from(conn.getUser())
                .to(JidCreate.entityBareFrom(toJid))
                .ofType(Message.Type.chat)
                .build();
        conn.sendStanza(message);
//        conn.disconnect();
    }

    /**
     * 发送群聊消息
     */
    private void sendGroupChat(MessageBean messageBean) throws Exception {
        XMPPTCPConnection conn;
        Integer fromUserId = messageBean.getUserId();
//        if(fromUserId>= ConstantXmpp.sysAccountBegin&&fromUserId<=ConstantXmpp.sysAccountEnd){
//            conn = xmppManager.getSystemNoConnection(fromUserId);
//        }else{
        User fromUser;
        if(fromUserId!=null){
            fromUser = userService.findById(fromUserId);
        }else{
            Integer mucId = messageBean.getMucId();
            Muc muc = mucService.getById(mucId);
            if(muc==null||muc.getTsDelete()>0){
                log.info("群组不存在！");
                return;
            }
            MucMember master = mucMemberService.getMaster(mucId);
            if(master==null){
                log.info("群主不存在！");
                return;
            }
            messageBean.setUserId(master.getUserId());
            fromUser = userService.findById(master.getUserId());
        }
            conn = xmppManager.getUserConnection(fromUser.getId(),fromUser.getPassword());
//        }
        String mucJid = messageBean.getMucId() + xmppManager.getMucChatServiceName(conn);
        Message message = StanzaBuilder.buildMessage(UUIDTool.getUUID())
                .setBody(JSONObject.toJSONString(messageBean))
                .from(conn.getUser())
                .to(JidCreate.entityBareFrom(mucJid))
                .ofType(Message.Type.groupchat)
                .build();
        MultiUserChatManager muChatManager = MultiUserChatManager.getInstanceFor(conn);
        MultiUserChat muc = muChatManager.getMultiUserChat((EntityBareJid) message.getTo());
        if(!muc.isJoined()){
            muc.join(Resourcepart.from(fromUser.getId().toString()));
        }
        muc.sendMessage(message.asBuilder());
        log.info("消费群组消息成功！");
//        conn.disconnect();
    }

    //广播消息
    private void sendBroadcast(MessageBean messageBean) throws Exception{
//        conn.disconnect();
    }

//    /**
//     * 获取xmpp管理账号
//     */
//    public synchronized XMPPTCPConnection getConnection(String username) {
//        XMPPTCPConnection conn = null;
//        try {
//            conn = connMap.get(username);
//            String pwd = systemAdminMap.get(username);
//            if (conn != null && conn.isConnected()) {
//                if (conn.isAuthenticated()) {
//                    return conn;
//                    //PingManager.getInstanceFor(conn).setPingInterval(5);
//                }
//            } else if (null != conn && !conn.isConnected()) {
//                conn.connect();
//                conn.login(username, pwd);
//                connMap.put(username, conn);
//                return conn;
//            }
//            conn = new XMPPTCPConnection(xmppManager.getConfig());
//            //conn.setFromMode(FromMode.USER);
//            conn.setReplyTimeout(30000);
//            conn.connect();
//
//            conn.login(username, DigestUtils.md5Hex(pwd));
//            connMap.put(username, conn);
//            conn.addConnectionListener(new MyConnectionListener(conn, true));
//            conn.addStanzaAcknowledgedListener(new XmppserverReceivedListener());
//        } catch (Exception e) {
//            log.info("{} xmpp连接不上====> {}", username, e.getMessage());
//        }
//        return conn;
//    }
//
//    public class XmppserverReceivedListener implements StanzaListener {
//
//        @Override
//        public void processStanza(Stanza packet)
//                throws Exception {
//
//            if(StringUtils.isEmpty(packet.getStanzaId())){
//                log.info("packet.getStanzaId  ==== null Return ");
//                return;
//            }
//
//            MessageVo message=(MessageVo) messageMap.get(packet.getStanzaId());
//            if(message!=null){
//                messageMap.remove(packet.getStanzaId());
//            }
//        }
//
//    }

    public static class MyConnectionListener implements ConnectionListener {

        private XMPPTCPConnection conn;

        public XMPPTCPConnection getConn() {
            return conn;
        }

        public void setConn(XMPPTCPConnection conn) {
            this.conn = conn;
        }

        public MyConnectionListener() {
            // TODO Auto-generated constructor stub
        }

        public MyConnectionListener(XMPPTCPConnection conn, boolean flag) {
            // boolean
            this.conn = conn;
            if (conn.isAuthenticated()) {

                PingManager pingManager = PingManager.getInstanceFor(conn);
                pingManager.registerPingFailedListener(new PingFailedListener() {

                    @Override
                    public void pingFailed() {
                        log.info("xmpp ping pingFailed=====>");
                    }
                });

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
        public void connected(XMPPConnection connection) {
            log.info(connection.getUser() + " ====> connected");
        }

        @Override
        public void authenticated(XMPPConnection connection, boolean resumed) {
            //PingManager pingManager = PingManager.getInstanceFor(connection);

            log.info(connection.getUser() + " ====> authenticated  resumed " + resumed);
        }

    }


}