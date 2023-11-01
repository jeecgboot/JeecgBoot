package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author junko
 * @since 2021-01-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(User.TABLE_NAME)
public class User extends BaseModel<User> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_user";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 账号
     */
    private String account;

    /**
     * 用户名
     * 可修改，5~20个字符，字母开头，大小写，数字，下划线
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;
    /**
     * 密码
     */
    private String password;
    /**
     * 是初始密码
     */
    private Boolean passwordIsInit;

    /**
     * 密码盐
     */
    private String salt;

    /**
     * 注册时间
     */
    private Long tsCreate;

    /**
     * 头像
     */
    private String avatar;
    /**
     * 缩略图头像
     */
    private String smallAvatar;
    ///主页背景图
    private String homeBg;
    /**
     * 解冻时间
     */
    private Long tsLocked;
    //禁止连接聊天服务
    private Long tsNoConnect;
    //支付密码盐
    private String paySalt;
    //支付密码
    private String payPassword;
    //手机号
    private String mobile;
    //在线
    private Boolean isOnline;
    //二维码随机数
    private String qrCode;

    private Boolean enableGoogleCode;
    /**
     * 谷歌密钥
     */
    private String googleCode;

    /**
     * 类型，0：普通用户，1：业务号，2：系统号，3：僵尸号，4：公众号
     */
    private Integer type;
    //最后在线时间
    private Long tsOnline;
    //最后离线时间
    private Long tsOffline;
    //禁言截止时间
    private Long tsMute;
    //禁言起始时间
    private Long tsMuteBegin;
    private Integer muteType;
    //账号来源
    private Integer resource;
    //默认好友
    private Boolean isDefaultFriend;
    //被添加时的自动回复
    private String welcomes;
    //注册编号，第N个注册
    private Integer regNo;

    @TableField(exist = false)
    private String matchType;
    @TableField(exist = false)
    private UserSetting userSetting;
    @TableField(exist = false)
    private Friend friend;
    @TableField(exist = false)
    private MucMember mucMember;
    @TableField(exist = false)
    private UserInfo info;

    public enum Type {
        common(0, "普通用户"),
        business(1, "业务号"),
        sysAccount(2, "系统号"),
        zombie(3, "僵尸号"),
        publicAccount(4, "公众号"),
        bot(5, "机器人"),
        tourist(6, "游客"),
        ;
        int code;
        String name;

        Type(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }
    public enum Resource {
        CONSOLE_CREATE(0, "后台创建"),
        MOBILE_REG(1, "手机号注册"),
        EMAIL_REG(2, "邮箱注册"),
        USERNAME_REG(3, "用户名注册"),
        THIRD_AUTH(4, "第三方授权登录"),
        ;
        int code;
        String name;

        Resource(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }
    public enum MuteType{
        normal(0),//正常
        newMember(1),//新群成员
        manager(2),//群管理员或群主
        rule(3),//规则
        admin(4);//后台
        private final int code;
        MuteType(int code){
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

}