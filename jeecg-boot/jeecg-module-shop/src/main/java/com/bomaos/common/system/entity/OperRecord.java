package com.bomaos.common.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志
 * Created by Panyoujie on 2018-12-24 16:10
 */
@TableName("sys_oper_record")
public class OperRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 模块
     */
    private String model;
    /**
     * 方法
     */
    private String description;
    /**
     * 请求地址
     */
    private String url;
    /**
     * 请求方式
     */
    private String requestMethod;
    /**
     * 调用方法
     */
    private String operMethod;
    /**
     * 请求参数
     */
    private String param;
    /**
     * 返回结果
     */
    private String result;
    /**
     * ip地址
     */
    private String ip;
    /**
     * 消耗时间,单位毫秒
     */
    private Long spendTime;
    /**
     * 状态,0成功,1异常
     */
    private Integer state;
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
    /**
     * 用户账号
     */
    @TableField(exist = false)
    private String username;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getOperMethod() {
        return operMethod;
    }

    public void setOperMethod(String operMethod) {
        this.operMethod = operMethod;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(Long spendTime) {
        this.spendTime = spendTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "LoginRecord{" +
                ", id=" + id +
                ", userId=" + userId +
                ", model=" + model +
                ", description=" + description +
                ", url=" + url +
                ", requestMethod=" + requestMethod +
                ", operMethod=" + operMethod +
                ", param=" + param +
                ", result=" + result +
                ", ip=" + ip +
                ", spendTime=" + spendTime +
                ", state=" + state +
                ", comments=" + comments +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", nickName=" + nickName +
                "}";
    }
}
