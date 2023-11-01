package org.jeecg.modules.im.xmpp;

import lombok.extern.slf4j.Slf4j;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

@Slf4j
public class MyXMPPConnection {

    // 设置连接信息
    private static final String XMPP_HOST = "your.host.name";
    private static final int XMPP_PORT = 5222;
    private static final String XMPP_USERNAME = "your_username";
    private static final String XMPP_PASSWORD = "your_password";

    private static XMPPTCPConnection sConnection;

    // 获取单例连接实例
    public static synchronized XMPPTCPConnection getConnection(){
        // 如果连接可用，则直接返回已连接的实例
        if (sConnection != null && sConnection.isConnected()) {
            return sConnection;
        }
        try {

            // 创建新连接
            XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder()
                    .setHost(XMPP_HOST)
                    .setPort(XMPP_PORT)
                    .setUsernameAndPassword(XMPP_USERNAME, XMPP_PASSWORD)
                    .setXmppDomain(XMPP_HOST)
                    .setSecurityMode(XMPPTCPConnectionConfiguration.SecurityMode.disabled);
            XMPPTCPConnection connection = new XMPPTCPConnection(config.build());

            // 连接到服务器
            connection.connect();
            connection.login();

            // 将连接实例赋值给全局变量
            sConnection = connection;
            return sConnection;
        }catch (Exception e){
            log.error("获取smack连接异常：",e);
            return null;
        }
    }
}