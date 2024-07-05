package org.springframework.base.system.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Person implements Serializable {
    private static final long serialVersionUID = -3059966024160185918L;

    private String id;
    private String f14id;
    private String createTime;
    private String createtime;
    private String username;
    private String password;
    private String realname;
    private String token;
    private String role;
    private String status;
    private String note;
    private String expiration;
    private String permission;
    private String datascope;
    private String clientId;
    private String lastLoginTime;
    private String lastLoginIp;
    private String failNumber;

}