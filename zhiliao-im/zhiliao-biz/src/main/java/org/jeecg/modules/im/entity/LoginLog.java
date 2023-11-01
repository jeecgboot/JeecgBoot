package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author junko
 * @since 2021-01-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(LoginLog.TABLE_NAME)
public class LoginLog extends BaseModel<LoginLog> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_login_log";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    private Integer userId;

    private String ip;

    private String ipInfo;

    private Integer deviceId;
    /**
     * 设备信息
     */
    private String devicePlatform;

    private String deviceNo;
    private String deviceName;
    private String deviceSysVer;
    private String deviceClientVer;
    /**
     * 经度
     */
    private String latitude;
    /**
     * 纬度
     */
    private String longitude;
    /**
     * 位置信息
     */
    private String locationInfo;
    /**
     * 是注册
     */
    private Boolean isRegister;

    private Long tsCreate;
    /**
     * 登录方式  手机号/账号/短信验证码
     */
    private String way;

    @TableField(exist = false)
    private User user;

    public enum Way{
        Mobile("手机号"),Account("账号"),SmsCode("短信验证码"),AutoLogin("自动登录"),Username("用户名"),Email("邮箱"),Scan("扫码");
        String info;
        Way(String info){
            this.info = info;
        }
    }

}
