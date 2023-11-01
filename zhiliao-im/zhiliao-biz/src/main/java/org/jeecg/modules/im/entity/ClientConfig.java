package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 应用配置
 * </p>
 *
 * @author junko
 * @since 2021-02-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(ClientConfig.TABLE_NAME)
public class ClientConfig extends BaseModel<ClientConfig> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_client_config";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 允许普通用户创建群组
     */
    private Boolean allowCommonUserCreateMuc;
    /**
     * 允许普通用户创建频道
     */
    private Boolean allowCommonUserCreateChannel;
    /**
     * 群组名称唯一
     */
    private Boolean mucNameUnique;
    /**
     * 频道名称唯一
     */
    private Boolean channelNameUnique;
    /**
     * 普通用户最多可创建群聊数量
     */
    private Integer maxMucCreate;
    /**
     * 普通用户最多可加入群聊数量
     */
    private Integer maxMucJoin;
    //群管理上限
    private Integer maxMucManage;
    /**
     * 普通用户好友上限
     */
    private Integer maxFriend;
    //每日可添加好友上限
    private Integer maxFriendPerDay;
    //初始密码
    private String defaultPassword;

    /**
     * 邀请码类型：0:不启用，1：选填，2：必填
     */
    private Integer inviteCodeType;
    //自动添加邀请人为好友
    private Boolean autoAddInviter;
    //启用普通用户的邀请码
    private Boolean enableCommonUserInvite;

    /**
     * 昵称搜索用户
     */
    private Boolean nicknameSearch;
    //昵称精确搜索
    private Boolean nicknameSearchExact;

    /**
     * 手机号搜索用户
     */
    private Boolean mobileSearch;

    /**
     * 账号搜索用户
     */
    private Boolean accountSearch;
    /**
     * 用户名搜索用户
     */
    private Boolean usernameSearch;

    /**
     * 启用昵称添加好友
     */
    private Boolean nicknameAdd;

    /**
     * 启用手机号添加好友
     */
    private Boolean mobileAdd;

    /**
     * 启用账号添加好友
     */
    private Boolean accountAdd;
    /**
     * 启用用户名添加好友
     */
    private Boolean usernameAdd;

    /**
     * 启用名片添加好友
     */
    private Boolean cardAdd;

    /**
     * 启用群聊添加好友
     */
    private Boolean mucAdd;

    /**
     * 启用扫一扫添加好友
     */
    private Boolean scanAdd;
    /**
     * 启用定位
     */
    private Boolean location;
    //高德安卓key
    private String gdAndroidKey;
    //高德ioskey
    private String gdIosKey;
    //高德webkey
    private String gdWebKey;
    /**
     * 启用钱包
     */
    private Boolean wallet;
    /**
     * 提现费率，%
     */
    private Integer withdrawFee;
    /**
     * 最低提现金额
     */
    private Integer minWithdrawAmount;
    /**
     * 最高提现金额
     */
    private Integer maxWithdrawAmount;
    /**
     * 最低充值金额
     */
    private Integer minRechargeAmount;
    /**
     * 最高充值金额
     */
    private Integer maxRechargeAmount;
    /**
     * 启用签到
     */
    private Boolean signIn;
    /**
     * 连续签到奖励
     */
    private String signInReward;
    /**
     * 达到后重置天数，中断将重置天数。0为不开启连续签到
     */
    private Integer signInContinueDay;
    /**
     * 最大签到天数，达到后不可继续签到，需要申请提现并通过后才能继续签到，通过后重置天数，0为不限制
     */
    private Integer signInMaxDay;
    /**
     * 启用短信
     */
    private Boolean sms;
    /**
     * 允许注册
     */
    private Boolean allowRegister;
    /**
     * 手机号注册
     */
    private Boolean mobileRegister;
    /**
     * 用户名注册
     */
    private Boolean usernameRegister;
    /**
     * 昵称唯一
     */
    private Boolean nicknameUnique;
    /**
     * 邮箱注册
     */
    private Boolean emailRegister;
    /**
     * 注册赠送金币
     */
    private Integer registerPresent;
    /**
     * 开启红包
     */
    private Boolean allowRedPack;
    /**
     * 红包最大金额
     */
    private Integer redPackMax;
    /**
     * 开启转账
     */
    private Boolean allowTransfer;
    /**
     * 转账最大金额
     */
    private Integer transferMax;
    /**
     * 手机验证码登录
     */
    private Boolean mobileSmsLogin;
    /**
     * 邮箱验证码登录
     */
    private Boolean emailCodeLogin;
    /**
     * qq登录
     */
    private Boolean qqLogin;
    /**
     *qq appId
     */
    private String qqAppId;
    /**
     * 微信登录
     */
    private Boolean wechatLogin;
    /**
     * 微信appId
     */
    private String wechatAppId;
    /**
     * 微信appKey
     */
    private String wechatAppKey;
    /**
     * 连续登录失败禁止登录次数
     */
    private Integer loginFailedLockedTimes;
    /**
     * 连续登录失败禁止登录时长，单位：分钟
     */
    private Integer loginFailedLockedDuration;
    /**
     * 登录有效时长，单位：天
     */
    private Integer loginValidDay;
    /**
     * 用户上传的头衔需要审核
     */
    private Boolean userAvatarNeedAudit;
    //限制ip单位时间,秒
    private Integer limitIpDuration;
    //限制ip单位时间内注册用户数
    private Integer limitIpCount;
    //限制设备单位时间，秒
    private Integer limitDeviceDuration;
    //限制设备单位时间内注册用户数
    private Integer limitDeviceCount;
    //限制消息发送单位时间，秒
    private Integer limitMsgDuration;
    //限制消息发送单位时间内发送条数
    private Integer limitMsgCount;
    //支持的国家区号，多个用,隔开
    private String dialCodes;
    //启用朋友圈
    private Boolean activity;
    //显示通话记录
    private Boolean dialHistory;
}
