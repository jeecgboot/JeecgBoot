package com.shop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 登录日志
 * 2018-12-24 16:10
 */
@Data
@TableName("sys_login_record")
public class LoginRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int TYPE_LOGIN = 0;  // 登录
    public static final int TYPE_ERROR = 1;  // 登录失败
    public static final int TYPE_LOGOUT = 2;  // 退出登录
    public static final int TYPE_REFRESH = 3;  // 刷新token
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    private String username;
    /**
     * 操作系统
     */
    private String os;
    /**
     * 设备名
     */
    private String device;
    /**
     * 浏览器类型
     */
    private String browser;
    /**
     * ip地址
     */
    private String ip;
    /**
     * 操作类型,0登录成功,1登录失败,2退出登录,3刷新token
     */
    private Integer operType;
    /**
     * 备注
     */
    private String comments;
    /**
     * 操作时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 用户昵称
     */
    @TableField(exist = false)
    private String nickName;

}
