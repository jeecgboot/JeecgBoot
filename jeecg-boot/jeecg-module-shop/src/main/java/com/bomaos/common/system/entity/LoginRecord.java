package com.bomaos.common.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 登录日志
 * Created by Panyoujie on 2018-12-24 16:10
 */
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getOperType() {
        return operType;
    }

    public void setOperType(Integer operType) {
        this.operType = operType;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "LoginRecord{" +
                ", id=" + id +
                ", os=" + os +
                ", device=" + device +
                ", browser=" + browser +
                ", ip=" + ip +
                ", operType=" + operType +
                ", comments=" + comments +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", username=" + username +
                ", nickName=" + nickName +
                "}";
    }
}
