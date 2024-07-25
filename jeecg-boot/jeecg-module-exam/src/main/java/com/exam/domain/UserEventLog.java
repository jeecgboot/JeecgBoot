package com.exam.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserEventLog implements Serializable {

    private static final long serialVersionUID = -3951198127152024633L;


    public UserEventLog() {

    }

    public UserEventLog(Integer userId, String userName, String realName, Date createTime) {
        this.userId = userId;
        this.userName = userName;
        this.realName = realName;
        this.createTime = createTime;
    }

    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 内容
     */
    private String content;

    /**
     * 时间
     */
    private Date createTime;

}
