package com.exam.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Message implements Serializable {

    private static final long serialVersionUID = -3510265139403747341L;

    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    private Date createTime;

    /**
     * 发送者用户ID
     */
    private Integer sendUserId;

    /**
     * 发送者用户名
     */
    private String sendUserName;

    /**
     * 发送者真实姓名
     */
    private String sendRealName;

    /**
     * 接收人数
     */
    private Integer receiveUserCount;

    /**
     * 已读人数
     */
    private Integer readCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getCreateTime() {
        return createTime;
    }

}
