package com.vmq.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "pay_price_tmp")
public class TmpPrice {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String price;

}
