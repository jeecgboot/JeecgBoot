package org.jeecg.modules.im.xmpp;

import lombok.extern.slf4j.Slf4j;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.ping.PingManager;

import java.util.HashMap;

@Slf4j
public class UserConnectionManager {

    private static final HashMap<String,XMPPTCPConnection> connections = new HashMap<>();

    public static XMPPTCPConnection getConnection(String username, String password,XMPPTCPConnectionConfiguration configuration) {
        XMPPTCPConnection connection = connections.get(username);
        if (connection == null || !connection.isConnected()) {
            connection = new XMPPTCPConnection(configuration);
            try {
                connection.connect();
                connection.login(username,password);
                connection.addConnectionListener(new ConnectionListener() {
                    @Override
                    public void connecting(XMPPConnection connection) {
                        ConnectionListener.super.connecting(connection);
                    }

                    @Override
                    public void connected(XMPPConnection connection) {
                        ConnectionListener.super.connected(connection);
                    }

                    @Override
                    public void authenticated(XMPPConnection connection, boolean resumed) {
                        ConnectionListener.super.authenticated(connection, resumed);
                        log.info("{}的smack连接已登录...",username);
                    }

                    @Override
                    public void connectionClosed() {
                        log.info("{}的smack连接已关闭...",username);
                        ConnectionListener.super.connectionClosed();
                    }

                    @Override
                    public void connectionClosedOnError(Exception e) {
                        log.info("{}的smack连接遇到异常时关闭...",username);
                        ConnectionListener.super.connectionClosedOnError(e);
                    }
                });
                connections.put(username,connection);
            } catch (Exception e) {
                log.error("获取用户连接异常",e);
            }
        }
        return connection;
    }
}
