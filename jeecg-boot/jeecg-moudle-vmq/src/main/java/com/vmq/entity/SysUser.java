package com.vmq.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String salt;

    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String phone;

    private boolean status;

    private boolean delFlag;

    private Date updateTime;

    private Date createTime;

}
