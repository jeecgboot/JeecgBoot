package com.exam.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = -7797183521247423117L;

    private Integer id;

    private String userUuid;

    /**
     * 用户名
     */
    private String userName;

    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    private Integer age;

    /**
     * 1.男 2女
     */
    private Integer sex;

    private Date birthDay;

    /**
     * 学生年级(1-12)
     */
    private Integer userLevel;

    private String phone;

    /**
     * 1.学生  3.管理员
     */
    private Integer role;

    /**
     * 1.启用 2禁用
     */
    private Integer status;

    /**
     * 头像地址
     */
    private String imagePath;

    private Date createTime;

    private Date modifyTime;

    private Date lastActiveTime;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 微信openId
     */
    private String wxOpenId;

}
