package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Locale;

/**
 * <p>
 * 用户设备
 * </p>
 *
 * @author junko
 * @since 2021-02-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(Device.TABLE_NAME)
public class Device extends BaseModel<Device> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_device";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private String platform;

    private String no;
    private String name;
    private String detail;
    private String sysVer;
    private String clientVer;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String token;
    private Boolean isOnline;
    //最后在线时间
    private Long tsOnline;
    //最后离线时间
    private Long tsOffline;

    private Long tsCreate;
    //禁用截止时间
    private Long tsDisabled;
    //极光推送id
    private String jpushId;

    @TableField(exist = false)
    private Integer loginTimes;
    @TableField(exist = false)
    private LoginLog loginLog;


    public enum Platform {
        android, ios, macos, windows,linux,fuchsia,
        //自定义
        smack,junkopc,unknown;

        public static Platform getByName(String name) {
            for (Platform value : Platform.values()) {
                if (value.toString().equalsIgnoreCase(name)) {
                    return value;
                }
            }
            return Platform.unknown;
        }
    }
}
