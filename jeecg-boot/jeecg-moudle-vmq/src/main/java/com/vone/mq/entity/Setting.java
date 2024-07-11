package com.vone.mq.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "pay_setting")
public class Setting {
    @Id
    private String vkey;
    private String vvalue;

}
