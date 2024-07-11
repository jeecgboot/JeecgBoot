package com.vone.mq.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "pay_price_tmp")
public class TmpPrice {
    @Id
    private String price;

}
