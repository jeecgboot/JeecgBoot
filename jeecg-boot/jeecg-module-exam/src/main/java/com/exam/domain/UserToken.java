package com.exam.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserToken implements Serializable {

    private static final long serialVersionUID = -2414443061696200360L;

    private Integer id;

    /**
     * 用户token
     */
    private String token;

    /**
     * 用户Id
     */
    private Integer userId;

    /**
     * 微信小程序openId
     */
    private String wxOpenId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 用户名
     */
    private String userName;

}
