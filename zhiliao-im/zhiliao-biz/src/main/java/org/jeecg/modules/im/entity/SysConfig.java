package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 系统配置
 * </p>
 *
 * @author junko
 * @since 2021-02-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(SysConfig.TABLE_NAME)
public class SysConfig extends BaseModel<SysConfig> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_sys_config";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 服务器维护中
     */
    private Boolean maintenance;
    /**
     * 金币兑人民币比率
     */
    private Integer coinRate;

    /**
     * xmpp主机
     */
    private String xmppHost;

    /**
     * xmpp虚拟域名
     */
    private String xmppDomain;

    /**
     * xmpp端口
     */
    private Integer xmppPort;
    /**
     * xmpp超时离线时长
     */
    private Integer xmppTimeout;
    /**
     * 保存单聊消息
     */
    private Boolean saveSgMsg;
    /**
     * 保存群聊消息
     */
    private Boolean saveMucMsg;
    /**
     * 关键词过滤
     */
    private Boolean keywordFilter;

    /**
     * 邮件服务器
     */
    private String emailHost;

    /**
     * 邮件服务器端口
     */
    private Integer emailPort;

    /**
     * 发件人昵称
     */
    private String emailUsername;

    /**
     * 邮件账号
     */
    private String emailAccount;

    /**
     * 邮件密码
     */
    private String emailPassword;
    /**
     * 存储方式
     */
    private String storageType;
    /**
     * CDN域名
     */
    private String cdnDomain;
    /**
     * OSS存储根目录
     */
    private String ossBasePath;
    /**
     * bucket名称
     */
    private String ossAliyunBucketName;
    /**
     * 外网访问地域节点
     */
    private String ossAliyunEndpoint;
    /**
     * 阿里云accessKeyId
     */
    private String ossAliyunAccessKeyId;
    /**
     * 阿里云accessKeySecret
     */
    private String ossAliyunAccessKeySecret;
    /**
     * 本地存储域名
     */
    private String resourceDomain;
    /**
     * 文件上传地址
     */
    private String uploadUrl;
    /**
     * 启用视频通话
     */
    private Boolean enableVideoCall;

    //agora appId
    private String agoraAppId;
    //agora appSecret
    private String agoraAppCertificate;

    public enum StorageType{
        aliyun_oss,minio
    }
}
