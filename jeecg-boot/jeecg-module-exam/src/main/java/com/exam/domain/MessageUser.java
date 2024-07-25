package com.exam.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MessageUser implements Serializable {

    private static final long serialVersionUID = -4042932811802896498L;

    private Integer id;

    /**
     * 消息内容ID
     */
    private Integer messageId;

    /**
     * 接收人ID
     */
    private Integer receiveUserId;

    /**
     * 接收人用户名
     */
    private String receiveUserName;

    /**
     * 接收人真实姓名
     */
    private String receiveRealName;

    /**
     * 是否已读
     */
    private Boolean readed;

    private Date createTime;

    /**
     * 阅读时间
     */
    private Date readTime;

}
