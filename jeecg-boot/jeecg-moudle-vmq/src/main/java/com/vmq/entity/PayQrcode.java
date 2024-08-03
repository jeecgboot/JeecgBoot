package com.vmq.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class PayQrcode {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String username;
    @Column(columnDefinition = "mediumtext")
    private String payUrl;
    private double price;
    private int type;

}
