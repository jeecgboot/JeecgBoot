package org.jeecg.modules.im.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * xmpp配置
 */
@Component
@ConfigurationProperties(prefix="im")
@PropertySource("classpath:im-service.properties")
@Data
public class XMPPConfig {
    //主机地址
    private String host;
    //虚拟域
    private String xmppDomain;
    //群聊前缀
    private String mucPrefix;
    //端口
    private int port;
    //启用SSL/TLS连接
    private boolean tlsOn;
    //自签证书
    private boolean selfSigned;
    //keystore存放路径
    private String keystoreFile;
    //keystore密码
    private String keystorePass;
    //管理员账号
    private String adminUsername;
    //管理员昵称
    private String adminNickname;
    //管理员密码
    private String adminPassword;
    //
    private String resource;

}
