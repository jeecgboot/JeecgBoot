package com.vone.mq.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
//@Entity
public class SysUser {
    @Id
    @GeneratedValue
    private String id;

    private String username;

    private String realname;

    private String password;

    private String salt;

    private String email;

    private boolean status;

    private boolean delFlag;

    private long createDate;

}
