package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户日志
 * </p>
 *
 * @author junko
 * @since 2021-01-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(UserLog.TABLE_NAME)
public class UserLog extends BaseModel<UserLog> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_user_log";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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
     * 操作
     */
    private String type;
    /**
     * 方法
     */
    private String method;
    /**
     * 请求参数
     */
    private String reqParam;
    /**
     * 响应数据
     */
    private String resData;
    /**
     * 请求资源
     */
    private String uri;
    /**
     * 详情
     */
    private String detail;
    /**
     * 模块
     */
    private String module;

    private Long tsCreate;

    @TableField(exist = false)
    private User user;
}
