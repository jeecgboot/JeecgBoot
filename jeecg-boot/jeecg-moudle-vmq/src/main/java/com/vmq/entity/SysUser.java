package com.vmq.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class SysUser {

    @Id
    @GeneratedValue
    private String id;

    private String username;

    private String realname;

    private String password;

    private String salt;

    private String email;

    private String phone;

    private boolean status;

    private boolean delFlag;

    private Date updateTime;

    private Date createTime;

}
